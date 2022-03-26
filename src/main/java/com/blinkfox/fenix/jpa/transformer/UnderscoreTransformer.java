package com.blinkfox.fenix.jpa.transformer;

import com.blinkfox.fenix.exception.FenixException;
import com.blinkfox.fenix.helper.StringHelper;
import com.blinkfox.fenix.jpa.AbstractResultTransformer;
import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.BeanUtils;
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
     * 用来存储返回结果类 class 中所有属性信息的映射关系 Map，其中 key 为结果类 class 的全类路径名，value 为该类的所有属性集合.
     */
    protected static final Map<String, Set<String>> classPropertiesMap = new ConcurrentHashMap<>();

    /**
     * 做一些初始化操作.
     *
     * <p>本方法会判断这个结果类是否有缓存过，如果没有就初始化缓存该结果类中的各个属性字段信息到 Map 中，便于后续快速判断和使用.</p>
     */
    @Override
    public void init() {
        Set<String> propertySet = classPropertiesMap.get(this.resultClass.getName());
        if (propertySet == null) {
            PropertyDescriptor[] propDescriptors = BeanUtils.getPropertyDescriptors(this.resultClass);
            propertySet = new HashSet<>(propDescriptors.length);
            for (PropertyDescriptor propDescriptor : propDescriptors) {
                propertySet.add(propDescriptor.getName());
            }
            classPropertiesMap.put(this.resultClass.getName(), propertySet);
        }
    }

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
        Set<String> propertySet = classPropertiesMap.get(super.resultClass.getName());

        // 遍历设置各个属性对应的值.
        for (int i = 0, len = aliases.length; i < len; ++i) {
            String column = aliases[i];
            if (StringHelper.isBlank(column)) {
                throw new FenixException(StringHelper.format("【Fenix 异常】将查询结果转换为【{}】对象时，第【{}】个查询"
                        + "结果列为空，请检查你是否开启了【nativeQuery = true】的原生 SQL 选项或者就要使用【as】“别名”的方式来显示声明"
                        + "查询结果列的名称！", super.resultClass.getName(), i));
            }

            // 如果该查询结果列转换为小驼峰后，存在相等的属性，就设置该属性值.
            String propertyName = this.toLowerCamelCase(column);
            if (propertySet.contains(propertyName)) {
                super.setResultPropertyValue(beanWrapper, propertyName, tuple[i]);
            }
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
