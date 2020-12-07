package com.blinkfox.fenix.repository;

import com.blinkfox.fenix.entity.EntityOne;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * EntityOne 对应的 Repository.
 *
 * @author blinkfox on 2020-12-07.
 * @since v2.4.0
 */
@Repository
public interface EntityOneRepository extends CrudRepository<EntityOne, Long> {

}
