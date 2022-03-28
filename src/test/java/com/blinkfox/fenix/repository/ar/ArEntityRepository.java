package com.blinkfox.fenix.repository.ar;

import com.blinkfox.fenix.entity.ar.ArEntity;
import com.blinkfox.fenix.jpa.FenixJpaRepository;
import com.blinkfox.fenix.jpa.QueryFenix;
import java.util.List;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * ArEntity 所对应的 Repository.
 *
 * @author blinkfox on 2022-03-28.
 * @since 1.0.0
 */
@Repository
public interface ArEntityRepository extends FenixJpaRepository<ArEntity, String> {

    @QueryFenix
    ArEntity findByIdWithFenix(@Param("id") String id);

    /**
     * 使用 Fenix 根据年龄查询.
     *
     * @param age 年龄
     * @return 数据
     */
    @QueryFenix
    List<ArEntity> findByAgeWithFenix(@Param("age") Integer age);

}
