package com.blinkfox.fenix.jpa.processor;

import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.support.RepositoryProxyPostProcessor;

import javax.persistence.EntityManager;

/**
 * @author hexian on 2021-07-01.
 * @description: jpa方法调用拦截链
 * @since ？？？
 */
public interface FenixRepositoryProxyPostProcessor extends RepositoryProxyPostProcessor, Ordered {


    /**
     * 提供注入 ApplicationContext (如果感兴趣)
     * @param applicationContext
     */
    public default void setApplicationContext(ApplicationContext applicationContext){

    }

    /**
     * 提供注入 EntityManager (如果感兴趣)
     * @param entityManager
     */
    public default void setEntityManager(EntityManager entityManager){

    }

    /**
     * 提供注入 JpaRepositoryFactory (如果感兴趣)
     * @param jpaRepositoryFactory
     */
    public default void setJpaRepositoryFactory(JpaRepositoryFactory jpaRepositoryFactory){

    }

    /**
     * 提供注入 JpaRepositoryFactoryBean (如果感兴趣)
     * @param jpaRepositoryFactoryBean
     */
    public default void setJpaRepositoryFactoryBean(JpaRepositoryFactoryBean jpaRepositoryFactoryBean){

    }

    @Override
    public default int getOrder(){
        return Ordered.LOWEST_PRECEDENCE;
    }

}
