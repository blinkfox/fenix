package com.blinkfox.fenix.ar;

import com.blinkfox.fenix.helper.StringHelper;
import org.springframework.util.Assert;

/**
 * 基础通用的 Model 接口.
 *
 * @author blinkfox on 2022-03-30.
 * @since 2.7.0
 */
public interface BaseModel<R> {

    /**
     * 懒加载获取本实体类所对应的 Repository（{@link R}）对象.
     *
     * @return 基本的 CrudRepository 对象
     */
    @SuppressWarnings("unchecked")
    default R getRepository() {
        return (R) RepositoryModelContext.getRepositoryObject(
                this.getRepositoryBeanName(), this.getClass().getName(), this::validRepository, this::validExecutor);
    }

    /**
     * 校验 Repository 类型是否正确，默认不做任何校验.
     *
     * @param repository Spring 容器中的 repository 对象
     */
    default void validRepository(Object repository) {
        // do nothing.
    }

    /**
     * 校验 {@link org.springframework.data.jpa.repository.JpaSpecificationExecutor} 类型是否正确，默认不做任何校验.
     *
     * @param specExecutor Spring 容器中的 JpaSpecificationExecutor 对象
     */
    default void validExecutor(Object specExecutor) {
        // do nothing.
    }

    /**
     * 断言 Repository 的对象实例不为空，如果为空，就抛出异常.
     *
     * @param repository Spring 容器中的 repository 对象
     */
    default void assertNotNullRepository(Object repository) {
        Assert.notNull(repository, StringHelper.format("【Fenix 异常】获取到 Spring 容器中 Spring Data JPA 的 "
                + "Repository 接口的 Bean【{}】为 Null。", this.getRepositoryBeanName()));
    }

    /**
     * 获取本实体类所对应的 Repository 对象在 Spring 中 Bean 的名称.
     *
     * <p>Fenix 默认会根据实体类'首字母小写的类名称' + 'Repository' 后缀来拼接 Bean 的名称。
     * 如果你实际的 Bean 名称不是这样的，你可以在实体类中，重写本方法来提供你真正定义的 Repository 的 Spring Bean 名称。</p>
     *
     * @return Repository 对象在 Spring 中 Bean 的名称
     */
    default String getRepositoryBeanName() {
        String entityName = this.getClass().getSimpleName();
        return entityName.substring(0, 1).toLowerCase() + entityName.substring(1) + "Repository";
    }

}
