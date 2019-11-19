package com.blinkfox.fenix.consts;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 一些常见 SQL 标记或操作符的常量类.
 *
 * @author blinkfox on 2019-08-06.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SymbolConst {

    /**
     * 等于.
     *
     * @since v2.1.0
     */
    public static final String WHERE = " WHERE ";

    /**
     * 等于.
     */
    public static final String EQUAL = " = ";

    /**
     * 不等于.
     */
    public static final String NOT_EQUAL = " <> ";

    /**
     * 大于.
     */
    public static final String GT = " > ";

    /**
     * 小于.
     */
    public static final String LT = " < ";

    /**
     * 大于等于.
     */
    public static final String GTE = " >= ";

    /**
     * 小于等于.
     */
    public static final String LTE = " <= ";

    /**
     * LIKE 匹配.
     */
    public static final String LIKE = " LIKE ";

    /**
     * NOT LIKE.
     */
    public static final String NOT_LIKE = " NOT LIKE ";

    /**
     * BETWEEN.
     */
    public static final String BETWEEN = " BETWEEN ";

    /**
     * AND.
     */
    public static final String AND = " AND ";

    /**
     * OR.
     */
    public static final String OR = " OR ";

    /**
     * IN.
     */
    public static final String IN = " IN ";

    /**
     * NOT IN.
     */
    public static final String NOT_IN = " NOT IN ";

    /**
     * IS NULL.
     */
    public static final String IS_NULL = " IS NULL ";

    /**
     * IS NOT NULL.
     */
    public static final String IS_NOT_NULL = " IS NOT NULL ";

    /**
     * SET.
     */
    public static final String SET = " SET ";

}
