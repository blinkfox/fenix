package com.blinkfox.fenix.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 用于增强 {@link JpaRepository} 接口功能的 Fenix JpaRepository 接口.
 *
 * @param <T> 实体类的泛型
 * @param <ID> 实体类的 ID
 * @author blinkfox on 2020-12-04.
 * @since v2.4.0
 */
@NoRepositoryBean
public interface FenixJpaRepository<T, ID> extends JpaRepository<T, ID> {

    /**
     * 保存或更新实体类中非 null 属性的字段值.
     *
     * <ul>
     *     <li>如果实体的主键 ID 为空，说明是新增的情况，就插入一条新的数据；</li>
     *     <li>如果实体的主键 ID 不为空，会先判断是否存在该 ID 的数据，如果不存在也会新增插入一条数据；
     *     否则说明是更新的情况，会仅更新实体类属性中不为 null 值的属性字段到数据库中；</li>
     * </ul>
     *
     * @param entity 实体类
     * @param <S> 泛型实体类
     * @return 原实体类，注意：如果是更新的情况，返回的值不一定有数据库中之前的值
     */
    <S extends T> S saveOrUpdateNotNullProperties(S entity);

}
