package com.blinkfox.fenix.jpa;

import jakarta.persistence.EntityManager;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.provider.QueryExtractor;
import org.springframework.data.jpa.repository.query.DefaultJpaQueryMethodFactory;
import org.springframework.data.jpa.repository.query.EscapeCharacter;
import org.springframework.data.jpa.repository.query.JpaQueryLookupStrategy;
import org.springframework.data.jpa.repository.query.JpaQueryMethodFactory;
import org.springframework.data.jpa.repository.query.QueryRewriterProvider;
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
 * <p>v2.3.0 版本修改批注：由于 Spring Data JPA v2.3.0 版本新增了 {@link JpaQueryMethodFactory} 接口，
 * 为了保证与之前的 Fenix 版本兼容，做了较大改动.</p>
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
     * QueryExtractor 查询提取器.
     */
    private final QueryExtractor extractor;

    /**
     * JPA 默认的 Query 查找策略实例.
     */
    private final QueryLookupStrategy jpaQueryLookupStrategy;

    /**
     * 用于创建 JPA Query 方法的工厂类对象.
     *
     * <p>注意：{@link DefaultJpaQueryMethodFactory} 类是在 JPA v2.3.0 版本才引入的，为了兼容之前的 Fenix 版本，
     * 保证运行时不报错，使用 {@code Object} 类型来引用，使用时强转为 {@link JpaQueryMethodFactory} 接口即可.</p>
     *
     * @since v2.3.1
     */
    private Object queryMethodFactory;

    /**
     * 构造方法.
     *
     * <p>v2.3.1 版本修改批注 (2020-05-16)：在 Spring Data JPA 的 v2.3.0 版本中，修改了 {@link JpaQueryLookupStrategy#create} 这个方法的参数，
     * 新增了 {@link DefaultJpaQueryMethodFactory} 实现及对应的接口，所以，本构造方法也必须给予修改.</p>
     *
     * @param entityManager EntityManager
     * @param provider QueryExtractor 实例
     */
    private FenixQueryLookupStrategy(EntityManager entityManager, QueryLookupStrategy.Key key,
            QueryExtractor extractor, QueryMethodEvaluationContextProvider provider) {
        this.entityManager = entityManager;
        this.extractor = extractor;

        // 如果当前 JVM 中有 JpaQueryMethodFactory 接口的 class，说明是 v2.3.0 及之后的版本，API 有所变化，需要特殊处理.
        if (FenixJpaClassWriter.hasDefaultJpaQueryMethodFactoryClass()) {
            this.queryMethodFactory = new DefaultJpaQueryMethodFactory(extractor);
            this.jpaQueryLookupStrategy = JpaQueryLookupStrategy.create(entityManager,
                    (JpaQueryMethodFactory) this.queryMethodFactory, key, provider, QueryRewriterProvider.simple(),
                    EscapeCharacter.DEFAULT);
        } else {
            // 为了兼容 Spring Data JPA v2.3.0 之前的版本，此处使用 Javassist 来重写下面方法中的字节码，来解决老版本的编译错误.
            this.jpaQueryLookupStrategy = this.createOldJpaQueryLookupStrategy(entityManager, key, extractor, provider,
                    EscapeCharacter.DEFAULT);
        }
    }

    /**
     * 创建 Spring Data JPA v2.3.0 之前的版本的 {@link QueryLookupStrategy} 对象。
     *
     * @param entityManager EntityManager 对象
     * @param key QueryLookupStrategy.Key
     * @param extractor QueryExtractor
     * @param provider QueryMethodEvaluationContextProvider
     * @param character EscapeCharacter
     * @return QueryLookupStrategy
     * @author blinkfox on 2020-05-17.
     * @since v2.3.1
     */
    public QueryLookupStrategy createOldJpaQueryLookupStrategy(EntityManager entityManager, QueryLookupStrategy.Key key,
            QueryExtractor extractor, QueryMethodEvaluationContextProvider provider, EscapeCharacter character) {
        // 本方法会使用 Javassist 重写本方法中的内容，切勿随意修改方法名和参数.
        return null;
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
        QueryFenix queryFenixAnnotation = method.getAnnotation(QueryFenix.class);
        if (queryFenixAnnotation == null) {
            return this.jpaQueryLookupStrategy.resolveQuery(method, metadata, factory, namedQueries);
        }

        // 如果有 QueryFenix 注解，就构造 FenixJpaQuery 实例，并注入 QueryFenix 和调用方法的 class 到该实例中，便于后续使用.
        FenixJpaQuery fenixJpaQuery;
        if (this.queryMethodFactory == null) {
            // 为了兼容 SpringData JPA v2.3.0 之前的版本，此处使用字节码注入的方式来解决编译错误.
            fenixJpaQuery = this.createOldFenixJpaQuery(method, metadata, factory, this.extractor, this.entityManager);
        } else {
            fenixJpaQuery = new FenixJpaQuery(((JpaQueryMethodFactory) this.queryMethodFactory)
                    .build(method, metadata, factory), this.entityManager);
        }

        fenixJpaQuery.setQueryFenix(queryFenixAnnotation);
        fenixJpaQuery.setQueryClass(method.getDeclaringClass());
        return fenixJpaQuery;
    }

    /**
     * 创建了 Spring Data JPA v2.3.0 版本之前的 {@code JpaQueryMethod} 方法.
     *
     * @param method 方法
     * @param metadata 元数据
     * @param factory 工厂类
     * @param extractor extractor
     * @param entityManager 实体管理器实例
     * @return {@link FenixJpaQuery} 对象
     * @author blinkfox on 2020-05-17.
     * @since v2.3.1
     */
    public FenixJpaQuery createOldFenixJpaQuery(Method method, RepositoryMetadata metadata,
            ProjectionFactory factory, QueryExtractor extractor, EntityManager entityManager) {
        // 本方法会使用 Javassist 重写本方法中的内容，切勿随意修改方法名和参数.
        return null;
    }

}
