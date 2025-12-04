package com.blinkfox.fenix.jpa;

import jakarta.persistence.EntityManager;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.repository.query.DeclaredQuery;
import org.springframework.data.jpa.repository.query.JpaQueryConfiguration;
import org.springframework.data.jpa.repository.query.JpaQueryLookupStrategy;
import org.springframework.data.jpa.repository.query.JpaQueryMethod;
import org.springframework.data.jpa.repository.query.JpaQueryMethodFactory;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.core.NamedQueries;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.util.StringUtils;

/**
 * 定义用来处理 {@link QueryFenix} 注解的查找策略类，该策略类实现了 {@link QueryLookupStrategy} 接口.
 * <p>该类主要重写了 {@link #resolveQuery} 这个方法，用来监测 JPA 的接口方法是否标注了 {@link QueryFenix} 注解.</p>
 * <ul>
 *     <li>如果标注了 {@link QueryFenix} 注解，就需要本 Fenix 扩展类库识别处理 XML 文件或 Java 中的 JPQL 语句；</li>
 *     <li>如果没有标注 {@link QueryFenix} 注解，就使用 JPA 默认的 {@link QueryLookupStrategy}.</li>
 * </ul>
 *
 * <p>v2.3.0 版本修改批注：由于 Spring Data JPA v2.3.0 版本新增了 {@link JpaQueryMethodFactory} 接口，
 * 为了保证与之前的 Fenix 版本兼容，做了较大改动.</p>
 *
 * <p>v4.0.0 版本修改批注：由于 Spring Data JPA v4.0.0 版本修改了 {@link JpaQueryMethodFactory} 接口，且变化较大，
 * 本类也同步进行较大程度的重构，以适配 4.x 的版本.
 *
 * @author blinkfox on 2019-08-04.
 * @since v1.0.0
 */
@Slf4j
public class FenixQueryLookupStrategy implements QueryLookupStrategy {

    /**
     * EntityManager 实体管理器.
     */
    private final EntityManager entityManager;

    /**
     * JPA 默认的 Query 查找策略实例.
     */
    private final QueryLookupStrategy jpaQueryLookupStrategy;

    private final JpaQueryMethodFactory queryMethodFactory;

    private FenixQueryLookupStrategy(
            EntityManager entityManager,
            JpaQueryMethodFactory queryMethodFactory,
            @Nullable Key key,
            JpaQueryConfiguration queryConfiguration) {
        this.entityManager = entityManager;
        this.queryMethodFactory = queryMethodFactory;
        this.jpaQueryLookupStrategy = JpaQueryLookupStrategy.create(
                entityManager, queryMethodFactory, key, queryConfiguration);
    }

    /**
     * 创建 {@link FenixQueryLookupStrategy} 实例.
     *
     * <p>注：本方法用于适配 Spring Data JPA v4.x.x 及以上版本。</p>
     *
     * @param entityManager entityManager
     * @param queryMethodFactory JpaQueryMethodFactory
     * @param key key
     * @param configuration JpaQueryConfiguration
     * @return QueryLookupStrategy
     * @since 3.1.0
     */
    static QueryLookupStrategy create(EntityManager entityManager, JpaQueryMethodFactory queryMethodFactory,
            @Nullable Key key, JpaQueryConfiguration configuration) {
        return new FenixQueryLookupStrategy(entityManager, queryMethodFactory, key, configuration);
    }

    /**
     * 判断执行的方法上是否有 {@link QueryFenix} 注解，如果有的话，就构造 {@link FenixJpaQuery} 实例，否则就是用 JPA 默认的处理方式.
     *
     * @param method will never be {@literal null}.
     * @param metadata will never be {@literal null}.
     * @param factory will never be {@literal null}.
     * @param namedQueries will never be {@literal null}.
     * @return RepositoryQuery
     */
    @Override
    public RepositoryQuery resolveQuery(Method method, RepositoryMetadata metadata, ProjectionFactory factory,
            NamedQueries namedQueries) {
        // 如果没有 QueryFenix 注解，就是用默认的 jpaQueryLookupStrategy.resolveQuery 来构造 RepositoryQuery 实例.
        QueryFenix queryFenixAnnotation = method.getAnnotation(QueryFenix.class);
        if (queryFenixAnnotation == null) {
            return this.jpaQueryLookupStrategy.resolveQuery(method, metadata, factory, namedQueries);
        }

        // 如果有 QueryFenix 注解，就构造 FenixJpaQuery 实例，并注入 QueryFenix 和调用方法的 class 到该实例中，便于后续使用.
        JpaQueryMethod queryMethod = this.queryMethodFactory.build(method, metadata, factory);
        FenixJpaQuery fenixJpaQuery = new FenixJpaQuery(queryMethod, this.entityManager);
        fenixJpaQuery.setQueryFenix(queryFenixAnnotation);
        fenixJpaQuery.setQueryClass(method.getDeclaringClass());
        fenixJpaQuery.setHasDeclaredCountQuery(
                getCountQuery(queryMethod, namedQueries, entityManager, queryFenixAnnotation.nativeQuery()) != null);
        return fenixJpaQuery;
    }

    /**
     * 判断是否 Count 查询，该方法复制于 Spring Data JPA 中 {@link JpaQueryLookupStrategy} 的 getCountQuery 方法.
     *
     * @param method JpaQueryMethod
     * @param namedQueries NamedQueries
     * @param entityManager EntityManager
     * @return DeclaredQuery
     */
    private @Nullable DeclaredQuery getCountQuery(
            JpaQueryMethod method, NamedQueries namedQueries, EntityManager entityManager, boolean nativeQuery) {
        String query = doGetCountQuery(method, namedQueries, entityManager);
        return StringUtils.hasText(query) ? getDeclaredQuery(query, nativeQuery) : null;
    }

    /**
     * 本方法复制于 Spring Data JPA 中 {@link JpaQueryLookupStrategy} 的 {@code doGetCountQuery()} 方法.
     *
     * @param method JpaQueryMethod
     * @param namedQueries NamedQueries
     * @param em EntityManager
     * @return String
     */
    private static @Nullable String doGetCountQuery(
            JpaQueryMethod method, NamedQueries namedQueries, EntityManager em) {
        if (StringUtils.hasText(method.getCountQuery())) {
            return method.getCountQuery();
        }
        String queryName = method.getNamedCountQueryName();
        if (!StringUtils.hasText(queryName)) {
            return method.getCountQuery();
        }
        if (namedQueries.hasQuery(queryName)) {
            return namedQueries.getQuery(queryName);
        }
        if (hasNamedQuery(em, queryName)) {
            return method.getQueryExtractor().extractQueryString(em.createNamedQuery(queryName));
        }
        return null;
    }

    /**
     * Returns whether the named query with the given name exists.
     *
     * <p>本方法复制于 Spring Data JPA 中
     * {@code org.springframework.data.jpa.repository.query.NamedQuery} 的 {@code hasNamedQuery} 方法。
     *
     * @param em must not be {@literal null}.
     * @param queryName must not be {@literal null}.
     */
    private static boolean hasNamedQuery(EntityManager em, String queryName) {
        try (EntityManager lookupEm = em.getEntityManagerFactory().createEntityManager()) {
            lookupEm.createNamedQuery(queryName);
            return true;
        } catch (IllegalArgumentException e) {
            if (log.isDebugEnabled()) {
                log.debug(String.format("Did not find named query %s", queryName));
            }
            return false;
        }
    }

    /**
     * 本方法复制于 Spring Data JPA 中 {@link JpaQueryMethod} 的 {@code getDeclaredQuery()} 方法.
     *
     * @param query 查询语句
     * @param isNativeQuery 是否原生查询
     * @return DeclaredQuery
     */
    private DeclaredQuery getDeclaredQuery(String query, boolean isNativeQuery) {
        return isNativeQuery ? DeclaredQuery.nativeQuery(query) : DeclaredQuery.jpqlQuery(query);
    }
}
