package com.blinkfox.fenix.repository;

import com.blinkfox.fenix.entity.School;
import com.blinkfox.fenix.jpa.FenixJpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 用于测试 {@link FenixJpaRepository} 的 School Repository 接口.
 *
 * @author blinkfox on 2020-12-05.
 * @since v2.4.0
 */
@Repository
public interface FenixSchoolRepository extends FenixJpaRepository<School, String> {

}
