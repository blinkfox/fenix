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
 * @author blinkfox on 2020-01-14
 * @since v2.2.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface OrNotIn {

    /**
     * 匹配的字段名，默认空字符串.
     *
     * @return 值
     */
    String value() default "";

}
