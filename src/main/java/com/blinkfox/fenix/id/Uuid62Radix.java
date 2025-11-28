package com.blinkfox.fenix.id;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.hibernate.annotations.IdGeneratorType;

/**
 * 62 进制 UUID 的 ID 生成器注解.
 *
 * @author blinkfox on 2025-07-21
 * @see Uuid62RadixIdGenerator
 * @since 3.1.0
 */
@IdGeneratorType(Uuid62RadixIdGenerator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface Uuid62Radix {
}
