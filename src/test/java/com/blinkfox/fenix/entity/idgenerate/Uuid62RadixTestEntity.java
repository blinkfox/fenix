package com.blinkfox.fenix.entity.idgenerate;

import com.blinkfox.fenix.id.Uuid62Radix;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

/**
 * {@link Uuid62Radix} 的测试实体类.
 *
 * @author blinkfox on 2025-07-22
 * @since 3.1.0
 */
@Entity
@Getter
@Setter
public class Uuid62RadixTestEntity {

    @Id
    @Uuid62Radix
    private String id;

    private String name;
}
