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
     * 路径符号.
     */
    public static final String SLASH = "/";

    /**
     * 含一个空格的字符串.
     */
    public static final String SPACE = " ";

    /**
     *空字符串.
     *
     * @since v2.1.0
     */
    public static final String EMPTY = "";

    /**
     * 普通数组.
     */
    public static final int OBJTYPE_ARRAY = 1;

    /**
     * Java 集合.
     */
    public static final int OBJTYPE_COLLECTION = 2;

    /**
     * 默认的用来存放 fenix XML 文件的目录名常量.
     */
    public static final String DEFAULT_FENIX_XML_DIR = "fenix";

    /**
     * 节点类型 - 文本节点.
     */
    public static final String NODETYPE_TEXT = "Text";

    /**
     * 节点类型 - 元素节点.
     */
    public static final String NODETYPE_ELEMENT = "Element";

    /**
     * 类型的字符串常量.
     */
    public static final String TYPE = "type";
    
        /**
     * 常规字符串T、F标识
     */
    public static final String BOOLEAN_STRING_TRUE = "T";
    public static final String BOOLEAN_STRING_FALSE = "F";

    /**
     * 常规字符串0、1标识
     */
    public static final String FLAG_STRING_TRUE = "1";
    public static final String FLAG_STRING_FALSE = "0";
    public static final String FLAG_STRING_TRUE_CN = "是";
    public static final String FLAG_STRING_FALSE_CN = "否";

}
