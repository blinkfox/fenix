package com.blinkfox.fenix.repository.ar;

import com.blinkfox.fenix.entity.ar.ArEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * ArEntity 所对应的 Repository.
 *
 * @author blinkfox on 2022-03-28.
 * @since 1.0.0
 */
@Repository
public interface ArEntityRepository extends CrudRepository<ArEntity, String> {

}
