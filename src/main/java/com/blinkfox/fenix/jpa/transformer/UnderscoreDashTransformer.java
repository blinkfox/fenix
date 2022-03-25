package com.blinkfox.fenix.jpa.transformer;

import com.blinkfox.fenix.jpa.AbstractResultTransformer;

/**
 * 用来将下划线（{@code '_'}）或者短横线（{@code '-'}）转换为 Java 规范中 VO 实例属性为小驼峰命名风格（{@code lowerCamelCase}）的结果转换器类.
 *
 * @author blinkfox on 2022-03-26.
 * @see com.blinkfox.fenix.jpa.FenixResultTransformer
 * @since v2.7.0
 */
// TODO 待继续开发 ...
public class UnderscoreDashTransformer extends AbstractResultTransformer {

    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {
        return null;
    }

}
