package com.blinkfox.fenix.entity.idgenerate;

import com.blinkfox.fenix.id.SnowflakeId;
import com.blinkfox.fenix.id.SnowflakeIdType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

/**
 * {@link SnowflakeId} 和 {@link SnowflakeIdType#RADIX_36} 的测试实体类.
 *
 * @author blinkfox on 2025-07-22
 * @since 3.1.0
 */
@Entity
@Getter
@Setter
public class SnowflakeIdRadix36TestEntity {

    @Id
    @Column(name = "c_id")
    @SnowflakeId(SnowflakeIdType.RADIX_36)
    private String id;

    @Column(name = "c_name")
    private String name;
}
