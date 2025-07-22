package com.blinkfox.fenix.id;

import java.io.Serializable;
import lombok.NoArgsConstructor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.factory.spi.StandardGenerator;

/**
 * 雪花算法的 62 进制 ID 字符串的生成器类.
 *
 * <p>本生成器类，会将雪花算法的 10 进制 {@code long} 型 ID 转换成 62 进制的 ID 字符串.
 * 这样作为字符串时能大幅度缩短 ID 的长度为 9 位，相比 10 进制的字符串形式 ID 更短，且仍然保证了 ID 的有序性.
 * 生成示例如：{@code BG5skT7pI}</p>
 *
 * @author blinkfox on 2020-12-07.
 * @since v2.4.0
 */
@NoArgsConstructor
public class Snowflake62RadixIdGenerator implements IdentifierGenerator, StandardGenerator {

    private static final IdWorker idWorker = new IdWorker();

    /**
     * 用于生成雪花算法 62 进制 ID 字符串的生成方法.
     *
     * @param s {@link SharedSessionContractImplementor} 实体
     * @param obj 对象
     * @return ID 结果
     */
    @Override
    public Serializable generate(SharedSessionContractImplementor s, Object obj) {
        return idWorker.get62RadixId();
    }

}
