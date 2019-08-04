package com.blinkfox.fenix.consts;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * XPATH 语法相关的常量类.
 *
 * @author blinkfox on 2019-08-04.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class XpathConst {

    /**
     * 查找 FENIX 标签的 XPATH 常量.
     */
    public static final String FENIX_TAG = "fenixs/fenix";

    /**
     * 查找 XML 标签的命名空间常量.
     */
    public static final String ATTR_NAMESPACE = "attribute::namespace";

    /**
     * 查找标签子节点的 XPATH 常量.
     */
    public static final String ATTR_CHILD = "child::node()";

    /**
     * 查找 '<fenix id=""></fenix>' 标签的属性 ID 常量.
     */
    public static final String ATTR_ID = "attribute::id";

    public static final String ATTR_MATCH = "attribute::match";
    public static final String ATTR_FIELD = "attribute::field";
    public static final String ATTR_VALUE = "attribute::value";
    public static final String ATTR_PATTERN = "attribute::pattern";
    public static final String ATTR_START = "attribute::start";
    public static final String ATTR_ENT = "attribute::end";
    public static final String ATTR_NAME_SPACE = "attribute::namespace";
    public static final String ATTR_ZEALOT_ID = "attribute::zealotid";
    public static final String ATTR_WHEN = "attribute::when";
    public static final String ATTR_THEN = "attribute::then";
    public static final String ATTR_ELSE = "attribute::else";

}
