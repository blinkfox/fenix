package com.blinkfox.fenix.consts;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Fenix XML 一些常见标签元素的常量类.
 *
 * @author blinkfox on 2019-08-06.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TagConst {

    /**
     * 等于.
     */
    public static final String EQUAL = "equal";

    /**
     * AND 等于.
     */
    public static final String AND_EQUAL = "andEqual";

    /**
     * OR 等于.
     */
    public static final String OR_EQUAL = "orEqual";

    /**
     * 不等于.
     */
    public static final String NOT_EQUAL = "notEqual";

    /**
     * AND 不等于.
     */
    public static final String AND_NOT_EQUAL = "andNotEqual";

    /**
     * OR 不等于.
     */
    public static final String OR_NOT_EQUAL = "orNotEqual";

    // 大于
    public static final String MORE = "moreThan";
    public static final String AND_MORE = "andMoreThan";
    public static final String OR_MORE = "orMoreThan";
    // 小于
    public static final String LESS = "lessThan";
    public static final String AND_LESS = "andLessThan";
    public static final String OR_LESS = "orLessThan";
    // 大于等于
    public static final String MORE_EQUAL = "moreEqual";
    public static final String AND_MORE_EQUAL = "andMoreEqual";
    public static final String OR_MORE_EQUAL = "orMoreEqual";
    // 小于等于
    public static final String LESS_EQUAL = "lessEqual";
    public static final String AND_LESS_EQUAL = "andLessEqual";
    public static final String OR_LESS_EQUAL = "orLessEqual";
    // "like"模糊
    public static final String LIKE = "like";
    public static final String AND_LIKE = "andLike";
    public static final String OR_LIKE = "orLike";
    // "not like"模糊
    public static final String NOT_LIKE = "notLike";
    public static final String AND_NOT_LIKE = "andNotLike";
    public static final String OR_NOT_LIKE = "orNotLike";
    // "between"区间
    public static final String BETWEEN = "between";
    public static final String AND_BETWEEN = "andBetween";
    public static final String OR_BETWEEN = "orBetween";
    // "in"范围
    public static final String IN = "in";
    public static final String AND_IN = "andIn";
    public static final String OR_IN = "orIn";
    // "not in"范围
    public static final String NOT_IN = "notIn";
    public static final String AND_NOT_IN = "andNotIn";
    public static final String OR_NOT_IN = "orNotIn";
    // "is null"判空
    public static final String IS_NULL = "isNull";
    public static final String AND_IS_NULL = "andIsNull";
    public static final String OR_IS_NULL = "orIsNull";
    // "is not null"判不为空
    public static final String IS_NOT_NULL = "isNotNull";
    public static final String AND_IS_NOT_NULL = "andIsNotNull";
    public static final String OR_IS_NOT_NULL = "orIsNotNull";
    // "text"文本
    public static final String TEXT = "text";
    // "import"导入文本
    public static final String IMPORT = "import";
    // "choose"多条件选择
    public static final String CHOOSE = "choose";

}
