package com.blinkfox.fenix.jpa;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

/**
 * 用来构造 {@link FenixJpaRepositoryFactory} 的实例.
 *
 * @author blinkfox on 2019-08-31.
 * @see RepositoryFactorySupport
 */
public class FenixJpaRepositoryFactoryBean<T extends Repository<S, ID>, S, ID>
        extends JpaRepositoryFactoryBean<T, S, ID> {

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
        return new FenixJpaRepositoryFactory(entityManager);
    }

}
