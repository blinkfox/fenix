package com.blinkfox.fenix.jpa;

import java.util.List;
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
     * @param entity 实体类
     * @param <S> 泛型实体类
     * @return 原实体类，注意：如果是更新的情况，返回的值不一定有数据库中之前的值
     */
    <S extends T> S saveOrUpdateByNotNullProperties(S entity);

    /**
     * 新增或更新所有实体类中非 null 属性的字段值.
     * 
     * <p>注意：该方法仅仅是循环调用 {@link #saveOrUpdateByNotNullProperties(Object)} 方法，
     * 保存每条数据时会先查询判断是否存在，再进行插入或者更新，通常在性能上较差.</p>
     *
     * <ul>
     *     <li>如果某个实体的主键 ID 为空，说明是新增的情况，就插入一条新的数据；</li>
     *     <li>如果某个实体的主键 ID 不为空，会先判断是否存在该 ID 的数据，如果不存在也会新增插入一条数据；
     *     否则说明是更新的情况，会仅更新实体类属性中不为 null 值的属性字段到数据库中；</li>
     * </ul>
     *
     * @param entities 可迭代的实体类集合
     * @param <S> 泛型实体类
     * @return 原实体类，注意：如果是更新的情况，返回的值不一定有数据库中之前的值
     */
    <S extends T> List<S> saveOrUpdateAllByNotNullProperties(Iterable<S> entities);

    /**
     * 批量新增实体类集合，该方法仅用于新增，不能用于有更新数据的场景，需要调用方事先做好处理，
     * 每次默认的批量大小为 {@link com.blinkfox.fenix.consts.Const#DEFAULT_BATCH_SIZE}.
     *
     * @param entities 实体类集合
     * @param <S> 泛型实体类
     */
    <S extends T> void saveBatch(Iterable<S> entities);

    /**
     * 批量新增实体类集合，该方法仅用于新增，不能用于有更新数据的场景，需要调用方事先做好处理.
     *
     * @param entities 实体类集合
     * @param batchSize 每次批量新增的大小
     * @param <S> <S> 泛型实体类
     */
    <S extends T> void saveBatch(Iterable<S> entities, int batchSize);

}
