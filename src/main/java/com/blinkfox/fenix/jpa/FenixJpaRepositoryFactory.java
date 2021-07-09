package com.blinkfox.fenix.jpa;

import com.blinkfox.fenix.helper.AnnotationHelper;
import com.blinkfox.fenix.jpa.interceptor.SqlInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.jpa.provider.PersistenceProvider;
import org.springframework.data.jpa.provider.QueryExtractor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryComposition;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.QueryMethodEvaluationContextProvider;
import org.springframework.util.ReflectionUtils;

import javax.persistence.EntityManager;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
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

    /**
     * 重写此方法, 完成对注解sql的拦截(因为@Query的sql是静态不可变的,所以可以在这里进行唯一的一次拦截修改)
     * @param repositoryInterface
     * @param fragments
     * @param <T>
     * @return
     */
    @Override
    public <T> T getRepository(Class<T> repositoryInterface, RepositoryComposition.RepositoryFragments fragments) {
        this.sqlInterceptor(repositoryInterface);
        T repository = super.getRepository(repositoryInterface, fragments);

        return repository;
    }

    private <T> void sqlInterceptor(Class<T> repositoryInterface) {
        if (Objects.nonNull(sqlInterceptor)){
            Method[] japMethods = ReflectionUtils.getAllDeclaredMethods(repositoryInterface);
            Arrays.asList(japMethods).stream()
            .filter(m -> m.isAnnotationPresent(Query.class) && m.isAnnotationPresent(FenixSqlInterceptor.class))
            .forEach(m -> {
                Query query = m.getAnnotation(Query.class);
                String sql = query.value();
                // 这里sql传入null，也可以从@Query注解里面取出来sql
                String newSql = sqlInterceptor.onPrepareStatement(m, sql);
                if (Objects.nonNull(newSql)){
                    // 在生成 javax.persistence.Query之前替换掉注解里面的静态sql 为新的sql
                    AnnotationHelper.updateAnnotationProperty(query, "value", newSql);
                }
            });
        }
    }

    public SqlInterceptor getSqlInterceptor() {
        return sqlInterceptor;
    }

    public void setSqlInterceptor(SqlInterceptor sqlInterceptor) {
        this.sqlInterceptor = sqlInterceptor;
    }
}
