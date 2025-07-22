package com.blinkfox.fenix.entity.idgenerate;

import com.blinkfox.fenix.id.SnowflakeId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

/**
 * {@link SnowflakeId} 的测试实体类.
 *
 * @author blinkfox on 2025-07-22
 * @since 3.1.0
 */
@Entity
@Getter
@Setter
public class SnowflakeIdTestEntity {

    @Id
    @SnowflakeId
    private Long id;

    private String name;
}
