package com.blinkfox.fenix.processor;

import com.blinkfox.fenix.interceptor.TransformMethodInterceptor;
import com.blinkfox.fenix.jpa.interceptor.SqlInterceptor;
import com.blinkfox.fenix.jpa.processor.FenixRepositoryProxyPostProcessor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

@Component
public class EnhanceRepositoryProxyPostProcessor implements FenixRepositoryProxyPostProcessor {

    private EntityManager entityManager;

    private JpaRepositoryFactory jpaRepositoryFactory;

    private JpaRepositoryFactoryBean jpaRepositoryFactoryBean;

    /**
     * 试试看拿到上封宝剑能不能为所欲为
     */
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext){
        this.applicationContext = applicationContext;
    }

    @Override
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void setJpaRepositoryFactory(JpaRepositoryFactory jpaRepositoryFactory) {
        this.jpaRepositoryFactory = jpaRepositoryFactory;
    }

    @Override
    public void setJpaRepositoryFactoryBean(JpaRepositoryFactoryBean jpaRepositoryFactoryBean) {
        this.jpaRepositoryFactoryBean = jpaRepositoryFactoryBean;
    }

    /**
     * 这是最有用的方法
     * @param factory
     * @param repositoryInformation
     */
    @Override
    public void postProcess(ProxyFactory factory, RepositoryInformation repositoryInformation) {
        // 添加新的方法调用拦截链
        factory.addAdvice(new TransformMethodInterceptor());
    }
}
