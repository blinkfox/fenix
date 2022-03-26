package com.blinkfox.fenix.id;

import java.io.Serializable;
import lombok.NoArgsConstructor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.UUIDGenerator;

/**
 * 雪花算法的 62 进制 ID 字符串的生成器类.
 *
 * <p>本生成器类，会将雪花算法的 10 进制 {@code long} 型 ID 转换成 36 进制均为小写字母的 ID 字符串.
 * 这样作为字符串时能大幅度缩短 ID 的长度为 10 位，相比 10 进制的字符串形式 ID 更短，且仍然保证了 ID 的有序性.
 * 生成示例如：{@code utfb5ndx4w}</p>
 *
 * @author blinkfox on 2020-12-07.
 * @since v2.4.0
 */
@NoArgsConstructor
public class Snowflake36RadixIdGenerator extends UUIDGenerator {

    private static final IdWorker idWorker = new IdWorker();

    /**
     * 用于生成雪花算法 36 进制均为英文小写字母或数字的字符串生成方法.
     *
     * @param s {@link SharedSessionContractImplementor} 实体
     * @param obj 对象
     * @return ID 结果
     */
    @Override
    public Serializable generate(SharedSessionContractImplementor s, Object obj) {
        return idWorker.get36RadixId();
    }

}
