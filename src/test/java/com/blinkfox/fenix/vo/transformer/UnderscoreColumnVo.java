package com.blinkfox.fenix.vo.transformer;

import jakarta.persistence.Column;
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
public class UnderscoreColumnVo extends ParentColumnVo {

    @Column(name = "column_name")
    private String columnName;

    @Column(name = "column_long_name")
    private Long columnLongName;

    @Column(name = "column_four_test_name")
    private String columnFourTestName;

    /**
     * 这是故意不设置 @Column 注解的字段.
     */
    private Date createTime;

}
