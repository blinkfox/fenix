package com.blinkfox.fenix.repository;

import com.blinkfox.fenix.entity.EntityThree;
import com.blinkfox.fenix.jpa.FenixJpaRepository;
import org.springframework.stereotype.Repository;

/**
 * EntityThree 对应的 Repository.
 *
 * @author blinkfox on 2020-12-07.
 * @since v2.4.0
 */
@Repository
public interface EntityThreeRepository extends FenixJpaRepository<EntityThree, String> {

}
