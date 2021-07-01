package com.blinkfox.fenix.jpa;

import com.blinkfox.fenix.jpa.interceptor.SqlInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.provider.PersistenceProvider;
import org.springframework.data.jpa.provider.QueryExtractor;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.QueryMethodEvaluationContextProvider;

import javax.persistence.EntityManager;
import java.util.Optional;

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

    /**
     * 新增sql拦截器
     */
    private SqlInterceptor sqlInterceptor;

    /**
     * 创建 {@link JpaRepositoryFactory} 实例.
     *
     * @param entityManager must not be {@literal null}
     */
    public FenixJpaRepositoryFactory(EntityManager entityManager) {
        super(entityManager);
        this.entityManager = entityManager;
        this.extractor = PersistenceProvider.fromEntityManager(entityManager);

        // 为了兼容 Spring Data JPA v2.3.0 之前的版本，这里修改 class 中的字节码，来支持老版本中 JPA 的相关方法，防止编译报错.
        FenixJpaClassWriter.modify();
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
        FenixQueryLookupStrategy queryLookupStrategy = (FenixQueryLookupStrategy)FenixQueryLookupStrategy.create(entityManager, key, this.extractor, provider);
        queryLookupStrategy.setSqlInterceptor(this.sqlInterceptor);
        return Optional.of(queryLookupStrategy);
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

    public SqlInterceptor getSqlInterceptor() {
        return sqlInterceptor;
    }

    public void setSqlInterceptor(SqlInterceptor sqlInterceptor) {
        this.sqlInterceptor = sqlInterceptor;
    }
}
