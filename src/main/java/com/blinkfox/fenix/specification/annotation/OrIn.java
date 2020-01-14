package com.blinkfox.fenix.specification.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于“或者范围”({@code OR field IN ?})条件场景的注解.
 * 条件中传入了大小为0的集合会出现永真和永假条件。建议使用者在调用框架之前对空集合进行过滤。
 *
 * @author YangWenpeng on 2019-12-17
 * @author blinkfox on 2020-01-13
 * @since v2.2.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface OrIn {

    /**
     * 待比较的字符串值，默认为空字符串.
     *
     * @return 字符串值
     */
    String value() default "";

    /**
     * 是否允许为 {@code null} 值，默认为 {@code false}.
     *
     * @return 值
     */
    boolean allowNull() default false;

    /**
     * 范围中值的 {@code Class} 类型，默认是字符串类型 {@code String.class}.
     *
     * @return {@code Class} 类型
     */
    Class<?> type() default String.class;

}
