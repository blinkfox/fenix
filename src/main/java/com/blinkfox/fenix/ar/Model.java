package com.blinkfox.fenix.ar;

import com.blinkfox.fenix.helper.StringHelper;
import lombok.Setter;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

/**
 * Fenix 提供的 ActiveRecord 模式的基础 Model 类.
 *
 * @param <T> 实体类的的泛型参数
 * @param <ID> 主键 ID
 * @author blinkfox on 2022-03-28.
 * @since v2.7.0
 */
public abstract class Model<T, ID> {

    /**
     * Spring Bean 的应用上下文，用于动态获取 Spring Data JPA 中各种实体所对应的 Repository 的 Bean 实例.
     */
    @Setter
    protected static ApplicationContext applicationContext;

    /**
     * 用来存储本实体类所对应的 Repository 对象，便于后续快速获取，本对象会在第一次使用时动态获取并存储.
     */
    protected Repository<T, ID> repository;

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
     * 懒加载获取本实体类所对应的 Repository 实例.
     *
     * @return Repository 实例
     */
    public Repository<T, ID> getRepository() {
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
     * 设置 Repository 的值.
     */
    protected abstract void setRepository(Object repositoryBean);

    /**
     * 初始化检查并初始化本实体类所对应的 repository 的对象实例.
     */
    private void initCheckAndSetRepository() {
        // 尝试判断 Spring 容器中是否存在本实体类所对应的 Repository 的 Bean.
        String repositoryBeanName = this.getRepositoryBeanName();
        if (!applicationContext.containsBean(repositoryBeanName)) {
            throw new NoSuchBeanDefinitionException(StringHelper.format("【Fenix 异常】获取实体类【{}】所对应的 "
                            + "Spring Data JPA 的 Repository Bean【{}】的实例为 Null，请先定义该实体类的 Repository "
                            + "接口【{}】，并标注【@Repository】注解。", this.getClass().getName(), repositoryBeanName,
                    this.getRepositoryClassName()));
        }

        // 获取 repositoryBean 的实例，并注入到本实体对象的属性中.
        this.setRepository(applicationContext.getBean(repositoryBeanName));
    }

    /**
     * 获取本实体类所对应的 Repository 对象在 Spring 中 Bean 的名称.
     *
     * <p>Fenix 默认会根据实体类'首字母小写的类名称' + 'Repository' 后缀来拼接 Bean 的名称。
     * 如果你实际的 Bean 名称不是这样的，你可以在实体类中，重写本方法来提供你真正定义的 Repository 的 Spring Bean 名称。</p>
     *
     * @return Repository 对象在 Spring 中 Bean 的名称
     */
    protected String getRepositoryBeanName() {
        String entityName = this.getClass().getSimpleName();
        return entityName.substring(0, 1).toLowerCase() + entityName.substring(1) + "Repository";
    }

    /**
     * 获取本实体类所对应的 Repository 的类名称.
     *
     * @return Repository 的类名
     */
    protected String getRepositoryClassName() {
        return this.getClass().getSimpleName() + "Repository";
    }

}
