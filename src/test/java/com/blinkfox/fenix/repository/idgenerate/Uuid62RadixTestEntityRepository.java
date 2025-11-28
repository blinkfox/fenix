package com.blinkfox.fenix.repository.idgenerate;

import com.blinkfox.fenix.entity.idgenerate.Uuid62RadixTestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * {@link Uuid62RadixTestEntity} 对应的 Repository.
 *
 * @author blinkfox on 2025-07-22
 * @since 3.1.0
 */
public interface Uuid62RadixTestEntityRepository extends JpaRepository<Uuid62RadixTestEntity, String> {
}
