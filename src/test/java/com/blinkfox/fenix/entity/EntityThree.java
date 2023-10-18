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
 * 一个用于测试 ID 生成器策略是否正确的实体类 3.
 *
 * @author blinkfox on 2020-12-07.
 * @since v2.4.0
 */
@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "t_entity_three")
@ToString
@NoArgsConstructor
public class EntityThree {

    /**
     * 自定义的 62 进制的雪花算法 ID 字符串.
     */
    @Id
    @Column(name = "c_id")
    @GeneratedValue(generator = "uuid62Radix")
    @GenericGenerator(name = "uuid62Radix", strategy = "com.blinkfox.fenix.id.Uuid62RadixIdGenerator")
    private String id;

    /**
     * 类型.
     */
    @Column(name = "n_type")
    private Integer type;

    /**
     * 基于类型的构造方法.
     *
     * @param type 类型
     */
    public EntityThree(Integer type) {
        this.type = type;
    }

}
