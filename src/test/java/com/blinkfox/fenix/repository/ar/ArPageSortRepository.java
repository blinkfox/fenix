package com.blinkfox.fenix.repository.ar;

import com.blinkfox.fenix.entity.ar.ArPageSort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * ArCrud 所对应的 Repository.
 *
 * @author blinkfox on 2022-03-28.
 * @since 2.7.0
 */
@Repository
public interface ArPageSortRepository extends JpaRepository<ArPageSort, String> {

}
