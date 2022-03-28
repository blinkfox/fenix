package com.blinkfox.fenix.ar;

import com.blinkfox.fenix.helper.StringHelper;
import java.util.Optional;
import lombok.Setter;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Fenix 提供的 ActiveRecord 模式的基础 Model 类.
 *
 * <p>本类仅提供操作单个实体对象的若干快捷方法，如果你想进行批量操作或者查询，可以通过本类中的 {@link #getRepository()} 方法来调用或实现.</p>
 *
 * @param <T> 实体类的的泛型参数
 * @param <ID> 主键 ID
 * @author blinkfox on 2022-03-27.
 * @since v2.7.0
 */
// TODO 需要拆分为 CrudModel、JpaModel 和 FenixJpaModel 三个继承关系的 Model.
public abstract class FenixModel<T, ID> {

    /**
     * Spring Bean 的应用上下文，用于动态获取 Spring Data JPA 中各种实体所对应的 Repository 的 Bean 实例.
     */
    @Setter
    private static ApplicationContext applicationContext;

    /**
     * 用来存储本实体类所对应的 Repository 对象，便于后续快速获取，本对象会在第一次使用时动态获取并存储.
     */
    private CrudRepository<T, ID> repository;

    /**
     * 获取本实体对象的 ID 值.
     *
     * <p>通常建议将实体类的 ID 字段取名为 {@code id}，这样就能自动重写该字段的值，
     * 否则当你继承本抽象类时，就需要你自己手动实现此方法.</p>
     *
     * @return ID 值
     */
    public abstract ID getId();

    /**
     * 获取本实体类所对应的 Repository 对象在 Spring 中 Bean 的名称.
     *
     * <p>Fenix 默认会根据实体类'首字母小写的类名称' + 'Repository' 后缀来拼接 Bean 的名称。
     * 如果你实际的 Bean 名称不是这样的，你可以在实体类中，重写本方法来提供你真正定义的 Repository 的 Spring Bean 名称。</p>
     *
     * @return Repository 对象在 Spring 中 Bean 的名称
     */
    public String getRepositoryBeanName() {
        String entityName = this.getClass().getSimpleName();
        return entityName.substring(0, 1).toLowerCase() + entityName.substring(1) + "Repository";
    }

    /**
     * 获取本实体类所对应的 Repository 的类名称.
     *
     * @return Repository 的类名
     */
    private String getRepositoryClassName() {
        return this.getClass().getSimpleName() + "Repository";
    }

    /**
     * 获取本实体类所对应的 Repository 对象.
     *
     * @return 基本的 CrudRepository 对象
     */
    public CrudRepository<T, ID> getRepository() {
        if (this.repository != null) {
            return this.repository;
        }

        // 如果之前没有初始化过 repository 的对象实例，就同步并再次检查判断或初始化 repository 的对象实例.
        synchronized (this) {
            if (this.repository == null) {
                this.initCheckAndSetRepository();
            }
            return this.repository;
        }
    }

    /**
     * 初始化检查并初始化本实体类所对应的 repository 的对象实例.
     */
    @SuppressWarnings("unchecked")
    private void initCheckAndSetRepository() {
        // 尝试判断 Spring 容器中是否存在本实体类所对应的 Repository 的 Bean.
        String repositoryBeanName = this.getRepositoryBeanName();
        if (!applicationContext.containsBean(repositoryBeanName)) {
            throw new NoSuchBeanDefinitionException(StringHelper.format("【Fenix 异常】获取实体类【{}】所对应的 "
                            + "Spring Data JPA 的 Repository Bean【{}】的实例为 Null，请先定义该实体类的 Repository "
                            + "接口【{}】，并标注【@Repository】注解。", this.getClass().getName(), repositoryBeanName,
                    this.getRepositoryClassName()));
        }

        // 获取 repositoryBean，并判断是否是 Repository.
        Object repositoryBean = applicationContext.getBean(repositoryBeanName);
        if (repositoryBean instanceof CrudRepository) {
            this.repository = (CrudRepository<T, ID>) repositoryBean;
        } else {
            throw new NoSuchBeanDefinitionException(StringHelper.format("【Fenix 异常】获取到的实体类【{}】"
                            + "所对应的 Spring Data JPA 的 Repository Bean【{}】的实例不是真正的 CrudRepository 接口或子接口。",
                    this.getClass().getName(), repositoryBeanName));
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
