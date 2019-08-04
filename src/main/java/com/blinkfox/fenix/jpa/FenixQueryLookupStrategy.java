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
 *     <li>如果标注了 {@link QueryFenix} 注解，就需要本 Fenix 扩展类库识别处理 XML 文件中的 JPQL 语句；</li>
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
    private QueryLookupStrategy jpaQueryLookupStrategy;

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
     * 创建 QueryLookupStrategy 实例.
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
     * Resolves a {@link RepositoryQuery} from the given QueryMethod that can be executed afterwards.
     *
     * @param method       will never be {@literal null}.
     * @param metadata     will never be {@literal null}.
     * @param factory      will never be {@literal null}.
     * @param namedQueries will never be {@literal null}.
     * @return RepositoryQuery
     */
    @Override
    public RepositoryQuery resolveQuery(Method method, RepositoryMetadata metadata, ProjectionFactory factory,
            NamedQueries namedQueries) {
        QueryFenix fenix = method.getAnnotation(QueryFenix.class);
        return fenix != null
                ? new FenixJpaQuery(new JpaQueryMethod(method, metadata, factory, extractor), entityManager, fenix)
                : this.jpaQueryLookupStrategy.resolveQuery(method, metadata, factory, namedQueries);
    }

}
