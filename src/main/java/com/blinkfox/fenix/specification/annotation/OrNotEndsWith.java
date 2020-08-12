package com.blinkfox.fenix.specification.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于“或语句按后缀模糊不匹配”({@code LIKE})场景的注解.
 *
 * @author blinkfox on 2020-01-26
 * @since v2.2.0
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OrNotEndsWith {

    /**
     * 注解的实体字段属性名称，默认为空或空字符串时将使用属性名称.
     *
     * @return 值
     */
    String value() default "";

}
