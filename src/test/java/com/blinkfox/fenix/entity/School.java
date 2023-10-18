package com.blinkfox.fenix.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 表示学校的实体类.
 *
 * @author blinkfox on 2020-12-05.
 * @since v1.0.0
 */
@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "t_school")
@ToString
@NoArgsConstructor
public class School {

    /**
     * ID.
     */
    @Id
    @Column(name = "c_id")
    private String id;

    /**
     * 学校名称.
     */
    @Column(name = "c_name")
    private String name;

    /**
     * 所在城市.
     */
    @Column(name = "c_city")
    private String city;

    /**
     * 所在位置.
     */
    @Column(name = "c_address")
    private String address;

    /**
     * 学校年龄.
     */
    @Column(name = "n_age")
    private Integer age;

    /**
     * 该条记录的创建时间.
     */
    @Column(name = "dt_create_date")
    private Date createTime;

    /**
     * 该条记录的最后更新时间.
     */
    @Column(name = "dt_update_date")
    private Date updateTime;

}
