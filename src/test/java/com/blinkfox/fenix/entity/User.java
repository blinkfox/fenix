package com.blinkfox.fenix.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 用户实体类.
 *
 * @author blinkfox on 2019-08-06.
 */
@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "t_user")
public class User {

    /**
     * ID.
     */
    @Id
    @Column(name = "c_id")
    private String id;

    /**
     * 名称.
     */
    @Column(name = "c_name")
    private String name;

    /**
     * 年龄.
     */
    @Column(name = "n_age")
    private int age;

    /**
     * 性别.
     */
    @Column(name = "c_sex")
    private String sex;

    /**
     * 邮箱.
     */
    @Column(name = "c_email")
    private String email;

    /**
     * 密码.
     */
    @Column(name = "c_password")
    private String password;

    /**
     * 出生日期.
     */
    @Column(name = "d_birthday")
    private Date birthday;

    /**
     * 创建时间.
     */
    @Column(name = "dt_create_time")
    private Date createTime;

    /**
     * 更新时间.
     */
    @Column(name = "dt_update_time")
    private Date updateTime;

    /**
     * 状态.
     */
    @Column(name = "c_status")
    private String status;

}
