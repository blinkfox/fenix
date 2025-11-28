package com.blinkfox.fenix.repository.idgenerate;

import com.blinkfox.fenix.entity.idgenerate.SnowflakeIdRadix62TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * {@link SnowflakeIdRadix62TestEntity} 对应的 Repository.
 *
 * @author blinkfox on 2025-07-22
 * @since 3.1.0
 */
public interface SnowflakeIdRadix62TestEntityRepository extends JpaRepository<SnowflakeIdRadix62TestEntity, String> {
}
