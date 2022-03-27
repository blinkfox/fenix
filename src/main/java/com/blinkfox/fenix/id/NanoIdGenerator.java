package com.blinkfox.fenix.id;

import java.io.Serializable;
import lombok.NoArgsConstructor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.UUIDGenerator;

/**
 * Nano ID 字符串的生成器类.
 *
 * @author blinkfox on 2022-03-27.
 * @since v2.7.0
 */
@NoArgsConstructor
public class NanoIdGenerator extends UUIDGenerator {

    /**
     * 用于生成 Nano ID 字符串的生成方法.
     *
     * @param s {@link SharedSessionContractImplementor} 实体
     * @param obj 对象
     * @return ID 结果
     */
    @Override
    public Serializable generate(SharedSessionContractImplementor s, Object obj) {
        return IdWorker.getNanoId();
    }

}
