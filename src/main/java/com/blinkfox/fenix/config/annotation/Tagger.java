package com.blinkfox.fenix.config.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于配置 Fenix XML 标签和对应 Handler 的注解.
 *
 * @author blinkfox on 2019-08-04.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Tagger {

    /**
     * XML 标签的名称，如: 'in', 'equal' 等.
     *
     * @return 字符串值
     */
    String value();

    /**
     * 生成动态 SQL 时添加的前缀，如: ' AND ', ' OR ' 等.
     *
     * @return 字符串值
     */
    String prefix() default "";

    /**
     * SQL 操作符，如: ' = ', ' LIKE ', ' IN '等.
     *
     * @return 字符串值
     */
    String symbol() default "";

}