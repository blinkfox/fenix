package com.blinkfox.fenix.ar;

import com.blinkfox.fenix.helper.StringHelper;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Fenix 提供的 ActiveRecord 模式的 JPA 相关操作的 Model 类.
 *
 * @param <T> 实体类的的泛型参数
 * @param <ID> 主键 ID
 * @author blinkfox on 2022-03-29.
 * @since v2.7.0
 */
public abstract class JpaModel<T, ID> extends CrudModel<T, ID> {

    /**
     * 获取本实体类所对应的 JpaRepository 对象.
     *
     * @return 基本的 JpaRepository 对象
     */
    @Override
    public JpaRepository<T, ID> getRepository() {
        return (JpaRepository<T, ID>) super.getRepository();
    }

    /**
     * 设置 Repository 的值.
     */
    @Override
    @SuppressWarnings("unchecked")
    protected void setRepository(Object repositoryBean) {
        if (repositoryBean instanceof JpaRepository) {
            this.repository = (JpaRepository<T, ID>) repositoryBean;
        } else {
            throw new NoSuchBeanDefinitionException(StringHelper.format("【Fenix 异常】获取到的实体类【{}】"
                            + "所对应的 Spring Data JPA 的 Repository Bean【{}】的实例不是真正的 JpaRepository 接口或子接口。",
                    this.getClass().getName(), this.getRepositoryBeanName()));
        }
    }

}
