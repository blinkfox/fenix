package com.blinkfox.fenix.id;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.hibernate.annotations.IdGeneratorType;

/**
 * Fenix 内置各种类型的雪花算法 ID 生成器的注解.
 *
 * @author blinkfox on 2025-07-21
 * @see FenixSnowflakeIdGenerator
 * @since 3.1.0
 */
@IdGeneratorType(FenixSnowflakeIdGenerator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface SnowflakeId {

    /**
     * 生成雪花算法的 ID 类型.
     *
     * @return 雪花算法 ID 类型
     */
    SnowflakeIdType value() default SnowflakeIdType.DECIMAL;
}
