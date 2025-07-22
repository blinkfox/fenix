package com.blinkfox.fenix.id;

/**
 * 雪花算法 ID 类型的枚举.
 *
 * @author blinkfox on 2025-07-21
 * @see SnowflakeId
 * @since 3.1.0
 */
public enum SnowflakeIdType {

    /**
     * 10 进制 long 型 ID.
     */
    DECIMAL,

    /**
     * 36 进制字符串 ID（小写字母 + 数字）.
     */
    RADIX_36,

    /**
     * 62 进制字符串 ID（大小写字母 + 数字）.
     */
    RADIX_62,
}
