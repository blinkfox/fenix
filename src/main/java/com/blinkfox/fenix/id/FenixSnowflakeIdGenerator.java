package com.blinkfox.fenix.id;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.factory.spi.StandardGenerator;

/**
 * Fenix 内置的多种格式的雪花算法 ID 生成器.
 *
 * @author blinkfox on 2025-07-21
 * @see SnowflakeId
 * @see SnowflakeIdGenerator
 * @see Snowflake36RadixIdGenerator
 * @see Snowflake62RadixIdGenerator
 * @since 3.1.0
 */
@Slf4j
@NoArgsConstructor
public class FenixSnowflakeIdGenerator implements IdentifierGenerator, StandardGenerator {

    private static final IdWorker idWorker = new IdWorker();

    private SnowflakeIdType snowflakeIdType = SnowflakeIdType.DECIMAL;

    /**
     * 构造方法.
     *
     * @param snowflakeIdAnnotation 雪花算法 ID 的注解配置
     */
    public FenixSnowflakeIdGenerator(SnowflakeId snowflakeIdAnnotation) {
        this.snowflakeIdType = snowflakeIdAnnotation.value();
    }

    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) {
        return switch (snowflakeIdType) {
            case RADIX_36 -> idWorker.get36RadixId();
            case RADIX_62 -> idWorker.get62RadixId();
            default -> idWorker.getId();
        };
    }
}
