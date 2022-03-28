package com.blinkfox.fenix.ar;

import com.blinkfox.fenix.helper.StringHelper;
import java.util.Optional;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Fenix 提供的 ActiveRecord 模式的 CRUD 相关操作的 Model 类.
 *
 * <p>本类仅提供操作单个实体对象的若干增删改查的快捷方法，如果你想进行批量操作或者查询，可以通过本类中的 {@link #getRepository()} 方法来调用或实现.</p>
 *
 * @param <T> 实体类的的泛型参数
 * @param <ID> 主键 ID
 * @author blinkfox on 2022-03-27.
 * @since v2.7.0
 */
public abstract class CrudModel<T, ID> extends Model<T, ID> {

    /**
     * 获取本实体类所对应的 Repository 对象.
     *
     * @return 基本的 CrudRepository 对象
     */
    @Override
    public CrudRepository<T, ID> getRepository() {
        return (CrudRepository<T, ID>) super.getRepository();
    }

    /**
     * 设置 Repository 的值.
     */
    @Override
    @SuppressWarnings("unchecked")
    protected void setRepository(Object repositoryBean) {
        if (repositoryBean instanceof CrudRepository) {
            this.repository = (CrudRepository<T, ID>) repositoryBean;
        } else {
            throw new NoSuchBeanDefinitionException(StringHelper.format("【Fenix 异常】获取到的实体类【{}】"
                            + "所对应的 Spring Data JPA 的 Repository Bean【{}】的实例不是真正的 CrudRepository 接口或子接口。",
                    this.getClass().getName(), this.getRepositoryBeanName()));
        }
    }

    /**
     * 保存本实体对象.
     *
     * @return 保存后的对象
     */
    @Transactional
    @SuppressWarnings("unchecked")
    public T save() {
        return this.getRepository().save((T) this);
    }

    /**
     * 根据本实体对象的 ID 查找数据库中的完整的实体对象记录信息，本实体对象信息会以 Optional 来包裹返回.
     *
     * @return 本实体对象的 Optional 对象
     */
    public Optional<T> findById() {
        return this.getRepository().findById(this.getId());
    }

    /**
     * 根据本实体对象的 ID 查找数据库中是否存在该对象.
     *
     * @return 布尔值
     */
    public boolean existsById() {
        return this.getRepository().existsById(this.getId());
    }

    /**
     * 删除本实体对象.
     */
    @Transactional
    @SuppressWarnings("unchecked")
    public void delete() {
        this.getRepository().delete((T) this);
    }

    /**
     * 根据本实体对象的 ID 值查找并删除此对象.
     */
    @Transactional
    public void deleteById() {
        this.getRepository().deleteById(this.getId());
    }

}
