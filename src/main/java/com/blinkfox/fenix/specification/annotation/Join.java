package com.blinkfox.fenix.specification.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.persistence.criteria.JoinType;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 用于“连接”({@code JOIN})查询场景的注解.
 *
 * @author YangWenpeng on 2019-12-17
 * @author blinkfox on 2020-01-13
 * @since v2.2.0
 */
@Documented
@Target(FIELD)
@Retention(RUNTIME)
public @interface Join {

    /**
     * 注解的实体名称，默认为空或空字符串时将使用实体名称.
     *
     * @return 字符串值
     */
    String value() default "";

    /**
     * 连接类型 {@link JoinType}，默认内连接 {@link JoinType#INNER}.
     *
     * @return {@link JoinType} 连接类型
     */
    JoinType joinType() default JoinType.INNER;

    /**
     * 连接的实体类的 {@code class}.
     *
     * @return {@code Class} 类型
     */
    Class<?> targetClass();

}
