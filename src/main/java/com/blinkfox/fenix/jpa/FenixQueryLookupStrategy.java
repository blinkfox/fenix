package com.blinkfox.fenix.jpa;

import java.lang.reflect.Method;
import javax.persistence.EntityManager;
import org.springframework.data.jpa.provider.QueryExtractor;
import org.springframework.data.jpa.repository.query.EscapeCharacter;
import org.springframework.data.jpa.repository.query.JpaQueryLookupStrategy;
import org.springframework.data.jpa.repository.query.JpaQueryMethod;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.core.NamedQueries;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.QueryMethodEvaluationContextProvider;
import org.springframework.data.repository.query.RepositoryQuery;

/**
 * 定义用来处理 {@link QueryFenix} 注解的查找策略类，该策略类实现了 {@link QueryLookupStrategy} 接口.
 * <p>该类主要重写了 {@link #resolveQuery} 这个方法，用来监测 JPA 的接口方法是否标注了 {@link QueryFenix} 注解.</p>
 * <ul>
 *     <li>如果标注了 {@link QueryFenix} 注解，就需要本 Fenix 扩展类库识别处理 XML 文件或 Java 中的 JPQL 语句；</li>
 *     <li>如果没有标注 {@link QueryFenix} 注解，就使用 JPA 默认的 {@link QueryLookupStrategy}.</li>
 * </ul>
 *
 * @author blinkfox on 2019-08-04.
 */
public class FenixQueryLookupStrategy implements QueryLookupStrategy {

    /**
     * EntityManager 实体管理器.
     */
    private final EntityManager entityManager;

    /**
     * QueryExtractor 查询提取器.
     */
    private final QueryExtractor extractor;

    /**
     * JPA 默认的 Query 查找策略实例.
     */
    private final QueryLookupStrategy jpaQueryLookupStrategy;

    /**
     * 构造方法.
     *
     * @param entityManager EntityManager
     * @param provider QueryExtractor 实例
     */
    private FenixQueryLookupStrategy(EntityManager entityManager, QueryLookupStrategy.Key key,
            QueryExtractor extractor, QueryMethodEvaluationContextProvider provider) {
        this.entityManager = entityManager;
        this.extractor = extractor;
        this.jpaQueryLookupStrategy = JpaQueryLookupStrategy
                .create(entityManager, key, extractor, provider, EscapeCharacter.DEFAULT);
    }

    /**
     * 创建 {@link FenixQueryLookupStrategy} 实例.
     *
     * @param entityManager entityManager
     * @param key key
     * @param extractor extractor
     * @param provider provider
     * @return MyQueryLookupStrategy
     */
    static QueryLookupStrategy create(EntityManager entityManager, QueryLookupStrategy.Key key,
            QueryExtractor extractor, QueryMethodEvaluationContextProvider provider) {
        return new FenixQueryLookupStrategy(entityManager, key, extractor, provider);
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
        QueryFenix queryFenixAnno = method.getAnnotation(QueryFenix.class);
        if (queryFenixAnno == null) {
            return this.jpaQueryLookupStrategy.resolveQuery(method, metadata, factory, namedQueries);
        }

        // 如果有 QueryFenix 注解，就构造 FenixJpaQuery 实例，并注入 QueryFenix 和调用方法的 class 到该实例中，便于后续使用.
        FenixJpaQuery fenixJpaQuery = new FenixJpaQuery(new JpaQueryMethod(method, metadata, factory, extractor),
                entityManager);
        fenixJpaQuery.setQueryFenix(queryFenixAnno);
        fenixJpaQuery.setQueryClass(method.getDeclaringClass());
        return fenixJpaQuery;
    }

}
