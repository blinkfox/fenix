package com.blinkfox.fenix.jpa;

import jakarta.persistence.EntityManager;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.provider.PersistenceProvider;
import org.springframework.data.jpa.provider.QueryExtractor;
import org.springframework.data.jpa.repository.query.DefaultJpaQueryMethodFactory;
import org.springframework.data.jpa.repository.query.EscapeCharacter;
import org.springframework.data.jpa.repository.query.JpaQueryConfiguration;
import org.springframework.data.jpa.repository.query.JpaQueryMethodFactory;
import org.springframework.data.jpa.repository.query.QueryEnhancerSelector;
import org.springframework.data.jpa.repository.query.QueryRewriterProvider;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.CachingValueExpressionDelegate;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.ValueExpressionDelegate;

/**
 * 扩展了 {@link JpaRepositoryFactory} JPA 规范类的 的 Repository 工厂类.
 * <p>该类主要重写了 {@link #getQueryLookupStrategy} 方法，
 * 在该方法中创建了 {@link FenixQueryLookupStrategy} 的实例.</p>
 *
 * @author blinkfox on 2019-08-04.
 * @since v1.0.0
 */
@Slf4j
public class FenixJpaRepositoryFactory extends JpaRepositoryFactory {

    /**
     * EntityManager 实体管理器.
     */
    private final EntityManager entityManager;

    /**
     * QueryExtractor 查询提取器.
     */
    private final QueryExtractor extractor;

    private final QueryEnhancerSelector queryEnhancerSelector = QueryEnhancerSelector.DEFAULT_SELECTOR;

    private final EscapeCharacter escapeCharacter = EscapeCharacter.DEFAULT;

    private final JpaQueryMethodFactory queryMethodFactory;

    private final QueryRewriterProvider queryRewriterProvider;

    /**
     * 创建 {@link JpaRepositoryFactory} 实例.
     *
     * @param entityManager must not be {@literal null}
     */
    public FenixJpaRepositoryFactory(EntityManager entityManager) {
        super(entityManager);
        this.entityManager = entityManager;
        this.extractor = PersistenceProvider.fromEntityManager(entityManager);
        this.queryMethodFactory = new DefaultJpaQueryMethodFactory(extractor);
        this.queryRewriterProvider = QueryRewriterProvider.simple();
    }

    /**
     * 创建 {@link QueryLookupStrategy} 策略实例.
     *
     * <p>注：本方法用于适配 Spring Data JPA v3.4.x 及以上版本。</p>
     *
     * @param key QueryLookupStrategy 的策略 Key
     * @param valueExpressionDelegate ValueExpressionDelegate 实例
     * @return FenixQueryLookupStrategy 策略实例
     * @since 3.0.1
     */
    @Override
    protected Optional<QueryLookupStrategy> getQueryLookupStrategy(
            QueryLookupStrategy.Key key, ValueExpressionDelegate valueExpressionDelegate) {
        JpaQueryConfiguration queryConfiguration = new JpaQueryConfiguration(
                queryRewriterProvider,
                queryEnhancerSelector,
                new CachingValueExpressionDelegate(valueExpressionDelegate),
                escapeCharacter);
        return Optional.of(FenixQueryLookupStrategy.create(entityManager, queryMethodFactory, key, queryConfiguration));
    }

    /**
     * 获取 Repository 的实现基类，这里使用 Fenix 中的 {@link FenixSimpleJpaRepository} 类.
     *
     * @param metadata 元数据
     * @return {@link FenixSimpleJpaRepository} 类
     */
    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
        return FenixSimpleJpaRepository.class;
    }

}
