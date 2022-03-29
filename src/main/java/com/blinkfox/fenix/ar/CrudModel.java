package com.blinkfox.fenix.ar;

import com.blinkfox.fenix.exception.FenixException;
import com.blinkfox.fenix.helper.StringHelper;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Fenix 提供的 ActiveRecord 模式的 CRUD 相关操作的 Model 接口.
 *
 * <p>当实体类实现本接口时，需要该实体类所对应的 Spring Data JPA 中的 Repository 接口继承自 {@link CrudRepository} 接口。
 * 本接口仅提供操作“单个”实体对象的若干“增删改查”的快捷方法，如果你想进行批量操作或者复杂查询，
 * 可以通过额外调用本类中的 {@link #getRepository()} 方法来实现即可.</p>
 *
 * @param <T> 实体类的泛型参数
 * @param <ID> 主键 ID
 * @param <R> 实体类所对应的 Repository 接口
 * @author blinkfox on 2022-03-29.
 * @since v2.7.0
 */
public interface CrudModel<T, ID, R extends CrudRepository<T, ID>> extends Model<T, ID, R> {

    /**
     * 校验 Repository 接口是否是 {@link CrudRepository} 类型的接口.
     *
     * @param repository Spring 容器中的 repository 对象
     */
    @Override
    default void validRepository(Object repository) {
        if (!(repository instanceof CrudRepository)) {
            throw new FenixException(StringHelper.format("【Fenix 异常】获取到的 Spring Data JPA 的 Repository 接口【{}】"
                    + "不是真正的 CrudRepository 接口。", repository.getClass().getName()));
        }
    }

    /**
     * 保存本实体对象.
     *
     * @return 保存后的对象
     */
    @Transactional
    @SuppressWarnings("unchecked")
    default T save() {
        return this.getRepository().save((T) this);
    }

    /**
     * 根据本实体对象的 ID 查找数据库中的完整的实体对象记录信息，本实体对象信息会以 Optional 来包裹返回.
     *
     * @return 本实体对象的 Optional 对象
     */
    default Optional<T> findById() {
        return this.getRepository().findById(this.getId());
    }

    /**
     * 根据本实体对象的 ID 查找数据库中是否存在该对象.
     *
     * @return 布尔值
     */
    default boolean existsById() {
        return this.getRepository().existsById(this.getId());
    }

    /**
     * 删除本实体对象.
     */
    @Transactional
    @SuppressWarnings("unchecked")
    default void delete() {
        this.getRepository().delete((T) this);
    }

    /**
     * 根据本实体对象的 ID 值查找并删除此对象.
     */
    @Transactional
    default void deleteById() {
        this.getRepository().deleteById(this.getId());
    }

}
