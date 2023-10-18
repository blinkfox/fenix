package com.blinkfox.fenix.vo.transformer;

import jakarta.persistence.Column;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * ParentColumnVo.
 *
 * @author blinkfox on 2022-03-26.
 * @since v1.0.0
 */
@Getter
@Setter
@Accessors(chain = true)
public class ParentColumnVo {

    @Column(name = "id")
    private Long id;

    @Column(name = "column_last_update_time")
    private LocalDateTime lastUpdateTime;

}
