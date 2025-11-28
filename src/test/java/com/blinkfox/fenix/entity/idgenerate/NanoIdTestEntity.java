package com.blinkfox.fenix.entity.idgenerate;

import com.blinkfox.fenix.id.NanoId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

/**
 * {@link NanoId} 的测试实体类.
 *
 * @author blinkfox on 2025-07-22
 * @since 3.1.0
 */
@Entity
@Getter
@Setter
public class NanoIdTestEntity {

    @Id
    @NanoId
    private String id;

    private String name;
}
