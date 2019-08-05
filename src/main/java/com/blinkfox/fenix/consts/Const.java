package com.blinkfox.fenix.consts;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 常量类.
 *
 * @author blinkfox on 2019-08-04.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Const {

    /**
     * 点号.
     */
    public static final String DOT = ".";

    /**
     * 逗号.
     */
    public static final String COMMA = ",";

    /**
     * 普通数组.
     */
    public static final int OBJTYPE_ARRAY = 1;

    /**
     * Java集合.
     */
    public static final int OBJTYPE_COLLECTION = 2;

    /**
     * 节点类型 - 文本节点.
     */
    public static final String NODETYPE_TEXT = "Text";

    /**
     * 节点类型 - 元素节点.
     */
    public static final String NODETYPE_ELEMENT = "Element";

    /* ------------------- 自定义的 Fenix 的元素节点类型.------------------- */

    /** 等于. */
    public static final String EQUAL = "equal";
    /** and 等于. */
    public static final String AND_EQUAL = "andEqual";
    /** or 等于. */
    public static final String OR_EQUAL = "orEqual";

    // 不等于
    public static final String NOT_EQUAL = "notEqual";
    public static final String AND_NOT_EQUAL = "andNotEqual";
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

    /* ------------------- SQL 中的前缀常量. ------------------- */

    /** 空字符串. */
    public static final String EMPTY = "";
    /** 含空格的字符串. */
    public static final String ONE_SPACE = " ";
    /** 含 AND 的字符串. */
    public static final String AND_PREFIX = " AND ";
    /** 含 OR 的字符串. */
    public static final String OR_PREFIX = " OR ";

    /* ------------------- SQL 中的操作符常量. ------------------- */

    /** 等于. */
    public static final String EQUAL_SUFFIX = " = ? ";
    /** 大于. */
    public static final String GT_SUFFIX = " > ? ";
    /** 小于. */
    public static final String LT_SUFFIX = " < ? ";
    /** 大于等于. */
    public static final String GTE_SUFFIX = " >= ? ";
    /** 小于等于. */
    public static final String LTE_SUFFIX = " <= ? ";
    /** 不等于. */
    public static final String NOT_EQUAL_SUFFIX = " <> ? ";
    /** LIKE 匹配. */
    public static final String LIKE_KEY = " LIKE ";
    /** NOT LIKE 匹配. */
    public static final String NOT_LIKE_KEY = " NOT LIKE ";
    /** 区间匹配. */
    public static final String BT_AND_SUFFIX = " BETWEEN ? AND ? ";
    /** 范围匹配. */
    public static final String IN_SUFFIX = " IN ";
    /** NOT 范围匹配. */
    public static final String NOT_IN_SUFFIX = " NOT IN ";
    /** NULL. */
    public static final String IS_NULL_SUFFIX = " IS NULL ";
    /** NOT NULL. */
    public static final String IS_NOT_NULL_SUFFIX = " IS NOT NULL ";

}
