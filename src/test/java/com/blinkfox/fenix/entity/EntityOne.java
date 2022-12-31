package com.blinkfox.fenix.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

/**
 * 一个用于测试 ID 生成器策略是否正确的实体类 1.
 *
 * @author blinkfox on 2020-12-07.
 * @since v2.4.0
 */
@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "t_entity_one")
@ToString
@NoArgsConstructor
public class EntityOne {

    /**
     * 自定义的雪花算法 ID.
     */
    @Id
    @Column(name = "c_id")
    @GeneratedValue(generator = "snowflake")
    @GenericGenerator(name = "snowflake", strategy = "com.blinkfox.fenix.id.SnowflakeIdGenerator")
    private Long id;

    /**
     * 名称.
     */
    @Column(name = "c_name")
    private String name;

    /**
     * 基于名称的构造方法.
     *
     * @param name 名称
     */
    public EntityOne(String name) {
        this.name = name;
    }

}
