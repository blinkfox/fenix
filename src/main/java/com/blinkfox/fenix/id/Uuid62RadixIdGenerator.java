package com.blinkfox.fenix.id;

import java.io.Serializable;
import lombok.NoArgsConstructor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.UUIDGenerator;

/**
 * 用于生成 62 进制字符串型 UUID 的 ID 生成器类.
 *
 * <p>本生成器类会生成 62 进制字符串型的 UUID，相比普通的 16 进制 UUID，结果更短.
 * 生成示例如：{@code FXOedrCvouduYPlYgul}，而通常的 16 进制 UUID 为：{@code 73b037d12c894a8ebe673fb6b1caecac}.</p>
 *
 * @author blinkfox on 2020-12-07.
 * @since v2.4.0
 */
@NoArgsConstructor
public class Uuid62RadixIdGenerator extends UUIDGenerator {

    /**
     * 用于生成 62 进制字符串型 UUID 的 ID 生成方法.
     *
     * @param s {@link SharedSessionContractImplementor} 实体
     * @param obj 对象
     * @return ID 结果
     */
    @Override
    public Serializable generate(SharedSessionContractImplementor s, Object obj) {
        return IdWorker.get62RadixUuid();
    }

}
