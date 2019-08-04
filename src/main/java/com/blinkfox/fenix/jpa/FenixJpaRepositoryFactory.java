package com.blinkfox.fenix.jpa;

import java.util.Optional;
import javax.persistence.EntityManager;

import lombok.Getter;

import org.springframework.data.jpa.provider.PersistenceProvider;
import org.springframework.data.jpa.provider.QueryExtractor;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.QueryMethodEvaluationContextProvider;

/**
 * 扩展了 {@link JpaRepositoryFactory} JPA 规范类的 的 Repository 工厂类.
 * <p>该类主要重写了 {@link #getQueryLookupStrategy} 方法，
 * 在该方法中创建了 {@link FenixQueryLookupStrategy} 的实例.</p>
 *
 * @author blinkfox on 2019-08-04.
 */
public class FenixJpaRepositoryFactory extends JpaRepositoryFactory {

    /**
     * EntityManager 实体管理器.
     */
    @Getter
    private final EntityManager entityManager;

    /**
     * QueryExtractor 查询提取器.
     */
    private final QueryExtractor extractor;

    /**
     * 创建 {@link JpaRepositoryFactory} 实例.
     *
     * @param entityManager must not be {@literal null}
     */
    public FenixJpaRepositoryFactory(EntityManager entityManager) {
        super(entityManager);
        this.entityManager = entityManager;
        this.extractor = PersistenceProvider.fromEntityManager(entityManager);
    }

    /**
     * 创建 {@link QueryLookupStrategy} 实例.
     *
     * @param key QueryLookupStrategy 的策略 Key
     * @param provider QueryMethodEvaluationContextProvider 实例
     * @return FenixQueryLookupStrategy 实例
     */
    @Override
    protected Optional<QueryLookupStrategy> getQueryLookupStrategy(QueryLookupStrategy.Key key,
            QueryMethodEvaluationContextProvider provider) {
        return Optional.of(FenixQueryLookupStrategy.create(entityManager, key, this.extractor, provider));
    }

}
