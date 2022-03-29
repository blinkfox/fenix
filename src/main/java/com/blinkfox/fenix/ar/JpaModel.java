package com.blinkfox.fenix.ar;

import com.blinkfox.fenix.exception.FenixException;
import com.blinkfox.fenix.helper.StringHelper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Fenix 提供的 ActiveRecord 模式的 JPA 相关操作的 Model 接口.
 *
 * <p>当实体类实现本接口时，需要该实体类所对应的 Spring Data JPA 中的 Repository 接口继承自 {@link JpaRepository} 接口。
 * 本接口仅提供操作“单个”实体对象的若干“增删改查”的快捷方法，如果你想进行批量操作或者复杂查询，
 * 可以通过额外调用本类中的 {@link #getRepository()} 方法来实现即可.</p>
 *
 * @param <T> 实体类的的泛型参数
 * @param <ID> 主键 ID
 * @param <R> 实体类所对应的 Repository 接口
 * @author blinkfox on 2022-03-29.
 * @since v2.7.0
 */
public interface JpaModel<T, ID, R extends JpaRepository<T, ID>> extends PagingAndSortingModel<T, ID, R> {

    /**
     * 校验 Repository 接口是否是 {@link JpaRepository} 类型的接口.
     *
     * @param repository Spring 容器中的 repository 对象
     */
    @Override
    default void validRepository(Object repository) {
        if (!(repository instanceof JpaRepository)) {
            throw new FenixException(StringHelper.format("【Fenix 异常】获取到的 Spring Data JPA 的 Repository 接口"
                    + "【{}】不是真正的 JpaRepository 接口。", repository.getClass().getName()));
        }
    }

    /**
     * 刷新最近有改变的数据到数据库中.
     */
    @Transactional
    default void flush() {
        this.getRepository().flush();
    }

    /**
     * 保存实体对象中的数据，并立即刷新到数据库中.
     *
     * @return 保存后的实体对象
     */
    @Transactional
    @SuppressWarnings("unchecked")
    default <S extends T> S saveAndFlush() {
        return this.getRepository().saveAndFlush((S) this);
    }

    /**
     * 根据本实体对象的 ID 查找数据库中的完整的实体对象记录信息.
     *
     * @return 实体对象
     */
    default T getById() {
        return this.getRepository().getById(this.getId());
    }

}
