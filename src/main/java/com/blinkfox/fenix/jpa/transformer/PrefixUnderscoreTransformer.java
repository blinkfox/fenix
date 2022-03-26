package com.blinkfox.fenix.jpa.transformer;

import java.util.HashSet;
import java.util.Set;

/**
 * 字段带有某些前缀且含有下划线风格的查询结果实体对象转换器.
 *
 * <p>用来去除某些数据库字段前缀且将下划线（{@code '_'}）风格的各个查询结果列转换为 Java 规范中 VO 实例属性
 * 为小驼峰命名风格（{@code lowerCamelCase}）的对象属性.</p>
 *
 * @author blinkfox on 2022-03-26.
 * @since v2.7.0
 */
public class PrefixUnderscoreTransformer extends UnderscoreTransformer {

    private static final Set<String> prefixSet = new HashSet<>();

    static {
        prefixSet.add("c_");
        prefixSet.add("n_");
        prefixSet.add("d_");
        prefixSet.add("dt_");
    }

    /**
     * 将原来的字符串名称转换为小驼峰风格的 Java 属性名称.
     *
     * @param name 待转换的原字符串
     * @return 转换后的小驼峰风格字符串
     */
    @Override
    protected String toLowerCamelCase(String name) {
        return super.toLowerCamelCase(this.getRemovedPrefixName(name));
    }

    /**
     * 获取移除了特定前缀名称的名称.
     *
     * @param name 名称
     * @return 可能移除了某些前缀后的名称
     */
    private String getRemovedPrefixName(String name) {
        name = name.toLowerCase();
        for (String prefix : prefixSet) {
            if (name.startsWith(prefix)) {
                return name.substring(prefix.length());
            }
        }
        return name;
    }

}
