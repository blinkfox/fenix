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
     * Saves an entity and flushes changes instantly.
     *
     * @param entity 实体类
     * @return 保存后的实体类
     */
    <S extends T> S saveOrUpdateNotNullFields(S entity);

}
