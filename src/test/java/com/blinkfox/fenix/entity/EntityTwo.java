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
 * 一个用于测试 ID 生成器策略是否正确的实体类 2.
 *
 * @author blinkfox on 2020-12-07.
 * @since v2.4.0
 */
@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "t_entity_two")
@ToString
@NoArgsConstructor
public class EntityTwo {

    /**
     * 自定义的 62 进制的雪花算法 ID 字符串.
     */
    @Id
    @Column(name = "c_id")
    @GeneratedValue(generator = "snowflake62Radix")
    @GenericGenerator(name = "snowflake62Radix", strategy = "com.blinkfox.fenix.id.Snowflake62RadixIdGenerator")
    private String id;

    /**
     * 标题.
     */
    @Column(name = "c_title")
    private String title;

    /**
     * 基于名称的构造方法.
     *
     * @param title 名称
     */
    public EntityTwo(String title) {
        this.title = title;
    }

}
