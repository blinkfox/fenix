package com.blinkfox.fenix.jpa;

import com.blinkfox.fenix.jpa.interceptor.SqlInterceptor;
import com.blinkfox.fenix.jpa.processor.FenixRepositoryProxyPostProcessor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * 用来构造 {@link FenixJpaRepositoryFactory} 的实例.
 *
 * @author blinkfox on 2019-08-04.
 * @see RepositoryFactorySupport
 * @since v1.0.0
 */
public class FenixJpaRepositoryFactoryBean<T extends Repository<S, ID>, S, ID>
        extends JpaRepositoryFactoryBean<T, S, ID> {

    /**
     * 注入方法拦截链
     */
    @Autowired(required = false)
    private ObjectProvider<List<FenixRepositoryProxyPostProcessor>> postProcessors;

    @Autowired(required = false)
    private ApplicationContext applicationContext;

    @Autowired(required = false)
    private SqlInterceptor sqlInterceptor;

    /**
     * 用来创建一个新的 {@link FenixJpaRepositoryFactoryBean} 实例的构造方法.
     *
     * @param repositoryInterface must not be {@literal null}.
     */
    public FenixJpaRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
        super(repositoryInterface);
    }

    /**
     * 返回 {@link FenixJpaRepositoryFactory}.
     *
     * @param entityManager EntityManager 实体管理器.
     * @return FenixJpaRepositoryFactory 实例
     */
    @Override
    protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
        FenixJpaRepositoryFactory repositoryFactory = new FenixJpaRepositoryFactory(entityManager);
        // 加入sql拦截器
        repositoryFactory.setSqlInterceptor(sqlInterceptor);
        this.addRepositoryProxyPostProcessor(entityManager, repositoryFactory);

        return repositoryFactory;
    }

    /**
     * 新增JPA方法调用拦截处理器
     * @param entityManager
     * @param repositoryFactory
     */
    private void addRepositoryProxyPostProcessor(EntityManager entityManager, FenixJpaRepositoryFactory repositoryFactory) {
        if (Objects.nonNull(this.postProcessors)){
            List<FenixRepositoryProxyPostProcessor> postProcessorList = this.postProcessors.getIfAvailable();
            if (!CollectionUtils.isEmpty(postProcessorList)){
                // 排序集合(数字越小优先级越高)
                postProcessorList.sort(Comparator.comparingInt(FenixRepositoryProxyPostProcessor::getOrder));
                for (int i = 0; i < postProcessorList.size(); i++) {
                    FenixRepositoryProxyPostProcessor postProcessor = postProcessorList.get(i);

                    postProcessor.setApplicationContext(applicationContext);
                    postProcessor.setEntityManager(entityManager);
                    postProcessor.setJpaRepositoryFactory(repositoryFactory);
                    postProcessor.setJpaRepositoryFactoryBean(this);

                    // 方法调用链增加拦截链方法
                    repositoryFactory.addRepositoryProxyPostProcessor(postProcessor);
                }
            }
        }
    }

}
