package com.blinkfox.fenix.jpa.transformer;

/**
 * 字段均为下划线风格的查询结果实体对象转换器.
 *
 * <p>用来将下划线（{@code '_'}）风格的各个查询结果列转换为 Java 规范中 VO 实例属性为小驼峰命名风格（{@code lowerCamelCase}）的对象属性.</p>
 *
 * @author blinkfox on 2022-03-26.
 * @since v2.7.0
 */
public class UnderscoreTransformer extends LowerCamelCaseTransformer {

    /**
     * 构造方法.
     */
    public UnderscoreTransformer() {
        super.separatorChars = new char[] {'_'};
    }

}
