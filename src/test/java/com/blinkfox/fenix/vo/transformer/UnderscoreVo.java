package com.blinkfox.fenix.vo.transformer;

import java.time.LocalDateTime;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 下划线字段的 VO 类.
 *
 * @author blinkfox on 2022-03-26.
 * @since v2.7.0
 */
@Getter
@Setter
@Accessors(chain = true)
public class UnderscoreVo {

    private Long id;

    private String columnName;

    private Long columnLongName;

    private String columnThreeName;

    private String columnFourTestName;

    private Date columnCreateTime;

    private LocalDateTime columnLastUpdateTime;

}
