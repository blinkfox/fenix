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
     * 冒号.
     */
    public static final String COLON = ":";

    /**
     * 下划线.
     */
    public static final String UNDERLINE = "_";

    /**
     * 单引号.
     */
    public static final String QUOTE = "'";

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

    /* ------------------- SQL 中的前缀常量. ------------------- */

    /** 空字符串. */
    public static final String EMPTY = "";
    /** 含空格的字符串. */
    public static final String ONE_SPACE = " ";

    /* ------------------- SQL 中的操作符常量. ------------------- */

}
