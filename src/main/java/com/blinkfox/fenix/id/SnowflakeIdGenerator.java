package com.blinkfox.fenix.id;

import lombok.NoArgsConstructor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

/**
 * 雪花算法的 10 进制 {@code long} 型 ID 生成器类.
 *
 * <p>本生成器类会生成雪花算法的 10 进制 {@code long} 型的有序 ID. 生成示例如：{@code 2458424618421248}</p>
 *
 * @author blinkfox on 2020-12-07.
 * @since v2.4.0
 */
@NoArgsConstructor
public class SnowflakeIdGenerator implements IdentifierGenerator {

    private static final IdWorker idWorker = new IdWorker();

    /**
     * 用于生成雪花算法 10 进制 {@code long} 型 ID 的生成方法.
     *
     * @param s {@link SharedSessionContractImplementor} 实体
     * @param obj 对象
     * @return ID 结果
     */
    @Override
    public Object generate(SharedSessionContractImplementor s, Object obj) {
        return idWorker.getId();
    }

}
