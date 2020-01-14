package com.blinkfox.fenix.specification.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于“模糊匹配数组”({@code field1 LIKE '%xx%' OR field2 LIKE '%yyy%'})场景的注解.
 *
 * @author YangWenpeng 2019-12-17
 * @author blinkfox on 2020-01-13
 * @since v2.2.0
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LikeIn {

    /**
     * 匹配的字段名，默认空字符串.
     *
     * @return 字符串值
     */
    String value() default "";

}
