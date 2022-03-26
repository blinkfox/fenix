package com.blinkfox.fenix.jpa.transformer;

import com.blinkfox.fenix.exception.FenixException;
import com.blinkfox.fenix.helper.StringHelper;
import com.blinkfox.fenix.jpa.AbstractResultTransformer;
import java.beans.PropertyDescriptor;
import java.util.Map;
import org.springframework.beans.BeanWrapper;

/**
 * 字段均为下划线风格的查询结果实体对象转换器.
 *
 * <p>用来将下划线（{@code '_'}）风格的各个查询结果列转换为 Java 规范中 VO 实例属性为小驼峰命名风格（{@code lowerCamelCase}）的对象属性.</p>
 *
 * @author blinkfox on 2022-03-26.
 * @since v2.7.0
 */
public class UnderscoreTransformer extends AbstractResultTransformer {

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
            if (c == '_') {
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
