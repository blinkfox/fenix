package com.blinkfox.fenix.id;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.hibernate.annotations.IdGeneratorType;

/**
 * Fenix 内置的雪花算法 36 进制 ID 生成器的注解.
 *
 * @author blinkfox on 2025-07-21
 * @see Snowflake36RadixIdGenerator
 * @since 3.1.0
 */
@IdGeneratorType(Snowflake36RadixIdGenerator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface Snowflake36RadixId {
}
