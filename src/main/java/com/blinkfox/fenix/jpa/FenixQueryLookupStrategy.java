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
 * 定义用来处理 '@QueryFenix' 注解的查找策略类.
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
    public FenixQueryLookupStrategy(EntityManager entityManager, QueryLookupStrategy.Key key, QueryExtractor extractor,
            QueryMethodEvaluationContextProvider provider) {
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
    public static QueryLookupStrategy create(EntityManager entityManager, Key key,
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
        QueryFenix myQuery = method.getAnnotation(QueryFenix.class);
        // TODO 需要完善.
        return null;
//        return myQuery != null
//                ? new MyJpaQuery(new JpaQueryMethod(method, metadata, factory, extractor), entityManager, myQuery)
//                : this.jpaQueryLookupStrategy.resolveQuery(method, metadata, factory, namedQueries);
    }

}
