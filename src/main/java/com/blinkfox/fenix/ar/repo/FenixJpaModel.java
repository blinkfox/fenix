package com.blinkfox.fenix.ar.repo;

import com.blinkfox.fenix.exception.FenixException;
import com.blinkfox.fenix.helper.StringHelper;
import com.blinkfox.fenix.jpa.FenixJpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Fenix 提供的 ActiveRecord 模式的 Fenix 增强后的 JPA 相关操作的 Model 接口.
 *
 * <p>当实体类实现本接口时，需要该实体类所对应的 Spring Data JPA 中的 Repository 接口继承自 {@link FenixJpaRepository} 接口。
 * 本接口仅提供操作“单个”实体对象的若干“增删改查”的快捷方法，如果你想进行批量操作或者复杂查询，
 * 可以通过额外调用本类中的 {@link #getRepository()} 方法来实现即可.</p>
 *
 * @param <T> 实体类的的泛型参数
 * @param <ID> 主键 ID
 * @param <R> 实体类所对应的 Repository 接口
 * @author blinkfox on 2022-03-29.
 * @since v2.7.0
 */
public interface FenixJpaModel<T, ID, R extends FenixJpaRepository<T, ID>> extends JpaModel<T, ID, R> {

    /**
     * 校验 Repository 接口是否是 {@link FenixJpaRepository} 类型的接口.
     *
     * @param repository Spring 容器中的 repository 对象
     */
    @Override
    default void validRepository(Object repository) {
        assertNotNullRepository(repository);
        if (!(repository instanceof FenixJpaRepository)) {
            throw new FenixException(StringHelper.format("【Fenix 异常】获取到的 Spring Data JPA 的 Repository "
                    + "接口【{}】不是真正的 FenixJpaRepository 接口。", repository.getClass().getName()));
        }
    }

    /**
     * 新增或更新实体类中非 null 属性的字段值.
     *
     * <p>注意：该方法保存每条数据时会先查询判断是否存在，再进行插入或者更新.</p>
     *
     * <ul>
     *     <li>如果实体的主键 ID 为空，说明是新增的情况，就插入一条新的数据；</li>
     *     <li>如果实体的主键 ID 不为空，会先判断是否存在该 ID 的数据，如果不存在也会新增插入一条数据；
     *     否则说明是更新的情况，会仅更新实体类属性中不为 null 值的属性字段到数据库中；</li>
     * </ul>
     *
     * @param <S> 泛型实体类
     * @return 原实体类，注意：如果是更新的情况，返回的值不一定有数据库中之前的值
     */
    @Transactional
    @SuppressWarnings("unchecked")
    default <S extends T> S saveOrUpdateByNotNullProperties() {
        return this.getRepository().saveOrUpdateByNotNullProperties((S) this);
    }

}
