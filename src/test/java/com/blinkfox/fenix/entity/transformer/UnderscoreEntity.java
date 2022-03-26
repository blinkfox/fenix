package com.blinkfox.fenix.entity.transformer;

import java.time.LocalDateTime;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

/**
 * 数据库字段都是下划线的测试实体类.
 *
 * @author blinkfox on 2022-03-26.
 * @since v2.7.0
 */
@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "t_underscore_table")
public class UnderscoreEntity {

    /**
     * ID.
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "snowflake")
    @GenericGenerator(name = "snowflake", strategy = "com.blinkfox.fenix.id.SnowflakeIdGenerator")
    private Long id;

    @Column(name = "column_name")
    private String columnName;

    @Column(name = "column_long_name")
    private Long columnLongName;

    @Column(name = "column_three_name")
    private String columnThreeName;

    @Column(name = "column_four_test_name")
    private String columnFourTestName;

    @Column(name = "column_create_time")
    private Date columnCreateTime;

    @Column(name = "column_last_update_time")
    private LocalDateTime columnLastUpdateTime;

}
