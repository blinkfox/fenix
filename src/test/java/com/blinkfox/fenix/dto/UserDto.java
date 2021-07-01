package com.blinkfox.fenix.dto;

import com.blinkfox.fenix.jpa.annotation.JpaDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@JpaDto
@ToString
public class UserDto {

    private String id;

    private String name;

    private int age;

    private String sex;

    private String email;

    private String password;

    private Date birthday;

    private Date createTime;

    private Date updateTime;

    private String status;

}
