package com.blinkfox.fenix.repository.idgenerate;

import com.blinkfox.fenix.entity.idgenerate.SnowflakeIdDecimalTestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * {@link SnowflakeIdDecimalTestEntity} 对应的 Repository.
 *
 * @author blinkfox on 2025-07-22
 * @since 3.1.0
 */
public interface SnowflakeIdDecimalTestEntityRepository extends JpaRepository<SnowflakeIdDecimalTestEntity, Long> {
}
