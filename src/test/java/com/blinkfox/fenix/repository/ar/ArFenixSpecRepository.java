package com.blinkfox.fenix.repository.ar;

import com.blinkfox.fenix.entity.ar.ArFenixSpec;
import com.blinkfox.fenix.specification.FenixJpaSpecificationExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * ArJpa 所对应的 Repository.
 *
 * @author blinkfox on 2022-03-28.
 * @since 1.0.0
 */
@Repository
public interface ArFenixSpecRepository
        extends CrudRepository<ArFenixSpec, String>, FenixJpaSpecificationExecutor<ArFenixSpec> {

    /**
     * 进行分页查询所有数据的方法.
     *
     * @param pageable 分页参数
     * @return 分页结果
     */
    @Query("select fs from ArFenixSpec as fs")
    Page<ArFenixSpec> findWithPaging(Pageable pageable);

}
