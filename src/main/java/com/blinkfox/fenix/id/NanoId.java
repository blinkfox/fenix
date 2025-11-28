package com.blinkfox.fenix.id;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.hibernate.annotations.IdGeneratorType;

/**
 * Nano ID 字符串生成器的注解.
 *
 * @author blinkfox on 2025-07-21
 * @see NanoIdGenerator
 * @since 3.1.0
 */
@IdGeneratorType(NanoIdGenerator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface NanoId {
}
