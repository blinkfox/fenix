package com.blinkfox.fenix.specification.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 操作类型的枚举.
 *
 * @author blinkfox on 2020-01-23.
 * @since v2.2.0
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum OperatorEnum {

    /**
     * {@code AND} 操作.
     */
    AND("and"),

    /**
     * {@code OR} 操作.
     */
    OR("or"),

    /**
     * {@code AND NOT} 操作.
     */
    AND_NOT("andnot"),

    /**
     * {@code OR NOT} 操作.
     */
    OR_NOT("ornot");

    /**
     * 操作符的关键字.
     */
    @Getter
    private String key;

}
