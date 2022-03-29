package com.blinkfox.fenix.ar;

import org.springframework.data.repository.Repository;

/**
 * Fenix 提供的 ActiveRecord 模式的基础公用 Model 接口.
 *
 * @param <ID> 主键 ID
 * @author blinkfox on 2022-03-29.
 * @since v2.7.0
 */
public interface Model<T, ID, R extends Repository<T, ID>> {

    /**
     * 获取本实体对象的 ID 值.
     *
     * <p>通常建议将实体类的 ID 字段取名为 {@code id}，这样通过实体类自身的 Getter 方法就能自动重写该方法。
     * 否则当你继承本抽象类时，就需要你自己手动实现此方法。</p>
     *
     * @return ID 值
     */
    ID getId();

    /**
     * 懒加载获取本实体类所对应的 Repository（{@link R}）对象.
     *
     * @return 基本的 CrudRepository 对象
     */
    @SuppressWarnings("unchecked")
    default R getRepository() {
        return (R) RepositoryModelContext.getRepositoryObject(
                this.getRepositoryBeanName(), this.getClass().getName(), this::validRepository);
    }

    /**
     * 校验 Repository 类型是否正确.
     *
     * @param repository Spring 容器中的 repository 对象
     */
    void validRepository(Object repository);

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
