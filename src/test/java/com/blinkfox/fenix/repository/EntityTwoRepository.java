package com.blinkfox.fenix.repository;

import com.blinkfox.fenix.entity.EntityTwo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * EntityTwo 对应的 Repository.
 *
 * @author blinkfox on 2020-12-07.
 * @since v2.4.0
 */
@Repository
public interface EntityTwoRepository extends JpaRepository<EntityTwo, String> {

}
