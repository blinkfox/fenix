package com.blinkfox.fenix.repository.idgenerate;

import com.blinkfox.fenix.entity.idgenerate.NanoIdTestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * {@link NanoIdTestEntity} 对应的 Repository.
 *
 * @author blinkfox on 2025-07-22
 * @since 3.1.0
 */
public interface NanoIdTestEntityRepository extends JpaRepository<NanoIdTestEntity, String> {
}
