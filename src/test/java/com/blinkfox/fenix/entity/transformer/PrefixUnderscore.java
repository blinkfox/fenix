package com.blinkfox.fenix.entity.transformer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

/**
 * 数据库字段带有特定前缀且有下划线的测试实体类.
 *
 * @author blinkfox on 2022-03-26.
 * @since v2.7.0
 */
@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "t_prefix_underscore")
public class PrefixUnderscore {

    /**
     * ID.
     */
    @Id
    @Column(name = "c_id")
    @GeneratedValue(generator = "snowflake36RadixId")
    @GenericGenerator(name = "snowflake36RadixId", strategy = "com.blinkfox.fenix.id.Snowflake36RadixIdGenerator")
    private String id;

    @Column(name = "c_name")
    private String name;

    @Column(name = "c_second_name")
    private String secondName;

    @Column(name = "n_integer_column")
    private Integer intColumn;

    @Column(name = "n_long_column")
    private long longColumn;

    @Column(name = "d_create_time")
    private Date createTime;

    @Column(name = "dt_last_update_time")
    private LocalDateTime lastUpdateTime;

}
