package com.blinkfox.fenix.jpa.transformer;

import com.blinkfox.fenix.exception.FenixException;
import com.blinkfox.fenix.helper.StringHelper;
import com.blinkfox.fenix.jpa.AbstractResultTransformer;
import java.beans.PropertyDescriptor;
import java.util.Map;
import org.springframework.beans.BeanWrapper;

/**
 * 这是用来将各种形式的字段名称转换为 Java 规范中 VO 对象属性均为小驼峰命名风格（{@code lowerCamelCase}）的通用结果转换器类.
 *
 * @author blinkfox on 2022-03-26.
 * @see com.blinkfox.fenix.jpa.FenixResultTransformer
 * @since v2.7.0
 */
public class LowerCamelCaseTransformer extends AbstractResultTransformer {

    /**
     * 分隔符的 char 数组，通常只有一两种分隔符的情况.
     */
    protected char[] separatorChars;

    /**
     * 用来将各个查询结果列的别名和值注入到 {@link super#resultClass} 的结果对象中的方法.
     *
     * @param tuple 值数组
     * @param aliases 别名数组
     * @return 转换后的结果对象
     */
    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {
        BeanWrapper beanWrapper = super.newResultBeanWrapper();
        beanWrapper.setConversionService(defaultConversionService);
        Map<String, PropertyDescriptor> fieldsMap = classPropertiesMap.get(super.resultClass.getName());

        // 遍历设置各个属性对应的值.
        for (int i = 0, len = aliases.length; i < len; ++i) {
            String column = aliases[i];
            if (StringHelper.isBlank(column)) {
                throw new FenixException(StringHelper.format("【Fenix 异常】将查询结果转换为【{}】对象时，第【{}】个查询"
                        + "结果列为空，请检查你是否开启了【nativeQuery = true】的原生 SQL 选项或者就要使用【as】“别名”的方式来显示声明"
                        + "查询结果列的名称！", super.resultClass.getName(), i));
            }

            // 如果该查询结果列存在，就设置值.
            super.setResultPropertyValue(beanWrapper, fieldsMap.get(this.toLowerCamelCase(column)), tuple[i]);
        }
        return beanWrapper.getWrappedInstance();
    }

    /**
     * 判断给定的字符是否是分隔符.
     *
     * @param c char 字符
     * @return 布尔值
     */
    protected boolean isSeparator(char c) {
        if (separatorChars != null && separatorChars.length > 0) {
            for (char sc : separatorChars) {
                if (sc == c) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 将原来的字符串名称转换为小驼峰风格的 Java 属性名称.
     *
     * @param name 待转换的原字符串
     * @return 转换后的小驼峰风格字符串
     */
    protected String toLowerCamelCase(String name) {
        final StringBuilder builder = new StringBuilder();

        char c;
        boolean capitalize = false;
        for (int i = 0, len = name.length(); i < len; ++i) {
            c = name.charAt(i);
            if (this.isSeparator(c)) {
                capitalize = true;
                continue;
            }

            if (capitalize) {
                builder.append(Character.toUpperCase(c));
                capitalize = false;
            } else {
                builder.append(Character.toLowerCase(c));
            }
        }
        return builder.toString();
    }

}
