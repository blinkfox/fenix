package com.blinkfox.fenix.config.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Fenix XML 标签注解 Tagger 的数组，由于 Java8 以前不支持重复注解，为了更好的支持 Java6、Java7，设置此注解.
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
