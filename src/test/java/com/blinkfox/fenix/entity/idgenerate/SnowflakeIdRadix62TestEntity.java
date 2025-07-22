package com.blinkfox.fenix.entity.idgenerate;

import com.blinkfox.fenix.id.Snowflake62RadixId;
import com.blinkfox.fenix.id.SnowflakeId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

/**
 * {@link SnowflakeId} 和 {@link Snowflake62RadixId} 的测试实体类.
 *
 * @author blinkfox on 2025-07-22
 * @since 3.1.0
 */
@Entity
@Getter
@Setter
public class SnowflakeIdRadix62TestEntity {

    @Id
    @Column(name = "c_id")
    @Snowflake62RadixId
    private String id;

    @Column(name = "c_name")
    private String name;
}
