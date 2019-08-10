package com.blinkfox.fenix.config.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Fenix XML 标签注解 {@link Tagger} 的数组.
 *
 * <p>Java8 及之后建议直接在类上重复使用 {@link Tagger} 注解，而不必再显示使用本 {@link Taggers} 注解.</p>
 *
 * @author blinkfox on 2019-08-04.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Taggers {

    /**
     * Tagger 注解的数组.
     *
     * @return 数组
     */
    Tagger[] value();

}
