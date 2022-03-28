package com.blinkfox.fenix.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 测试的状态枚举类.
 *
 * @author blinkfox on 2022-03-28.
 * @since 2.7.0
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum StatusEnum {

    /**
     * 成功.
     */
    SUCCESS(1),

    /**
     * 失败.
     */
    Failure(2);

    /**
     * 代码值.
     */
    @Getter
    private final int code;

    /**
     * 根据 code 构造实例.
     *
     * @param code code 值
     * @return 状态枚举
     */
    public static StatusEnum of(int code) {
        for (StatusEnum status : StatusEnum.values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("不存在代码值为【" + code + "】的枚举实例.");
    }

}
