package com.blinkfox.fenix.repository.ar;

import com.blinkfox.fenix.entity.ar.ArJpa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * ArJpa 所对应的 Repository.
 *
 * @author blinkfox on 2022-03-28.
 * @since 1.0.0
 */
@Repository
public interface ArJpaRepository extends JpaRepository<ArJpa, String> {

    /**
     * 进行分页查询所有数据的方法.
     *
     * @param pageable 分页参数
     * @return 分页结果
     */
    @Query("select aj from ArJpa as aj")
    Page<ArJpa> findWithPaging(Pageable pageable);

}
