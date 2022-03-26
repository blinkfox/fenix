package com.blinkfox.fenix.jpa.transformer;

/**
 * 字段均为连字符风格的查询结果实体对象转换器.
 *
 * <p>用来将英文的连字符（{@code '-'}）风格的各个查询结果列转换为 Java 规范中 VO 实例属性为小驼峰命名风格（{@code lowerCamelCase}）的对象属性.</p>
 *
 * @author blinkfox on 2022-03-26.
 * @since v2.7.0
 */
public class HyphenTransformer extends LowerCamelCaseTransformer {

    /**
     * 构造方法.
     */
    public HyphenTransformer() {
        super.separatorChars = new char[] {'-'};
    }

}
