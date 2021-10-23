package com.blinkfox.fenix.jpa;

import com.blinkfox.fenix.exception.FenixException;
import com.blinkfox.fenix.helper.StringHelper;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Blob;
import java.sql.Clob;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.type.BlobType;
import org.hibernate.type.descriptor.java.DataHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.NotWritablePropertyException;
import org.springframework.beans.PropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.convert.JodaTimeConverters;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * 自定义查询结果的转换器.
 *
 * @param <T> 要转换的范型 T
 * @author blinkfox on 2019-10-08.
 * @since v1.1.0
 */
public class FenixResultTransformer<T> implements ResultTransformer {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 4519223959994503529L;

    private static final DefaultConversionService conversionService = new DefaultConversionService();

    /**
     * 要转换类型的 class 实例.
     */
    private final Class<T> resultClass;

    /**
     * 返回结果各属性字段名称及对应的对象间的映射关系 Map.
     */
    private final transient Map<String, PropertyDescriptor> fieldMap;

    static {
        // 添加一些默认的 ConversionService.
        Collection<Converter<?, ?>> convertersToRegister = JodaTimeConverters.getConvertersToRegister();
        for (Converter<?, ?> converter : convertersToRegister) {
            conversionService.addConverter(converter);
        }
        conversionService.addConverter(ClobToStringConverter.INSTANCE);
        conversionService.addConverter(BlobToStringConverter.INSTANCE);
    }

    /**
     * 基于要转换的 {@code resultClass} 类型的构造方法.
     *
     * @param resultClass 转换类型的 class
     */
    public FenixResultTransformer(Class<T> resultClass) {
        Assert.notNull(resultClass, "【Fenix 异常】resultClass cannot be null.");
        this.resultClass = resultClass;

        // 将返回结果类中的所有属性保存到 Map 中，便于后续快速获取和判断.
        this.fieldMap = new HashMap<>();
        PropertyDescriptor[] propDescriptors = BeanUtils.getPropertyDescriptors(this.resultClass);
        handleFieldMap(propDescriptors, null);
    }

    /**
     * 处理结果类中的所有属性，包括深度嵌套的属性到 fieldMap 中.
     *
     * @param propDescriptors 给定类的 PropertyDescriptors 数组
     * @param nestedPropertyPrefix 嵌套属性的前缀
     */
    private void handleFieldMap(PropertyDescriptor[] propDescriptors, String nestedPropertyPrefix) {
        for (PropertyDescriptor propDescriptor : propDescriptors) {
            Class<?> propertyType = propDescriptor.getPropertyType();
            // 不是"简单"类型的递归执行
            if (!BeanUtils.isSimpleProperty(propertyType)) {
                String propertyTypeName = propertyType.getSimpleName();
                propertyTypeName = StringUtils.uncapitalize(propertyTypeName);
                PropertyDescriptor[] propertyDescriptors =
                        BeanUtils.getPropertyDescriptors(propertyType);
                String tempNestedPropertyPrefix = nestedPropertyPrefix;
                if (StringUtils.hasText(nestedPropertyPrefix)) {
                    nestedPropertyPrefix =
                            nestedPropertyPrefix + propertyTypeName + PropertyAccessor.NESTED_PROPERTY_SEPARATOR;
                } else {
                    nestedPropertyPrefix = propertyTypeName + PropertyAccessor.NESTED_PROPERTY_SEPARATOR;
                }
                handleFieldMap(propertyDescriptors, nestedPropertyPrefix);
                // 重置
                nestedPropertyPrefix = tempNestedPropertyPrefix;
            }

            String key;
            if (StringUtils.hasText(nestedPropertyPrefix)) {
                key = nestedPropertyPrefix + propDescriptor.getName();
            } else {
                key = propDescriptor.getName();
            }

            this.fieldMap.put(key, propDescriptor);
        }
    }

    /**
     * 将每行的“元组”数据转换为对应的 {@link #resultClass} 的结果类型.
     *
     * @param tuple 元组数据
     * @param aliases 元组数据的别名数组
     * @return 转换后的 {@link #resultClass} 的结果类型
     */
    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {
        // 构造结果实例.
        T resultObject;
        try {
            resultObject = this.resultClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException
                | NoSuchMethodException | InvocationTargetException e) {
            throw new FenixException("实例化【" + this.resultClass + "】类出错，请检查该类是否包含可公开访问的无参构造方法！", e);
        }

        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(resultObject);
        beanWrapper.setConversionService(conversionService);
        beanWrapper.setAutoGrowNestedPaths(true);

        // 遍历设置各个属性对应的值.
        for (int i = 0, len = aliases.length; i < len; i++) {
            String column = aliases[i];
            if (StringHelper.isBlank(column)) {
                throw new FenixException("【Fenix 异常】要映射为【" + this.resultClass + "】实体的查询结果列为空，"
                        + "请检查并保证每一个查询结果列都必须用【as】后加“别名”的方式！");
            }

            column = column.replaceAll(" ", "");
            // TODO: 2021/10/23 HSQL(可能还有其他的数据库)返回的 aliases 是所有大写的名称，需要做适配，临时用双引号规避掉这个问题
            PropertyDescriptor propDescriptor = this.fieldMap.get(column);
            if (propDescriptor == null) {
                continue;
            }

            try {
                beanWrapper.setPropertyValue(column, tuple[i]);
            } catch (NotWritablePropertyException | TypeMismatchException e) {
                throw new FenixException("【Fenix 异常】设置字段【" + column + "】的值到属性【"
                        + propDescriptor.getName() + "】中出错，请检查该字段或属性是否存在或者可公开访问！", e);
            }
        }
        return resultObject;
    }

    /**
     * 直接返回集合本身即可.
     *
     * @param list 集合.
     * @return 集合
     */
    @SuppressWarnings("rawtypes")
    @Override
    public List<?> transformList(List list) {
        return list;
    }

    /**
     * Clob 转换为 String 的转换器类.
     *
     * @author blinkfox 2019-10-08.
     */
    private enum ClobToStringConverter implements Converter<Clob, String> {

        /**
         * 单实例.
         */
        INSTANCE;

        @Override
        public String convert(Clob source) {
            return DataHelper.extractString(source);
        }
    }

    /**
     * Blob 转换为 String 的转换器类.
     *
     * @author blinkfox 2019-10-08.
     */
    private enum BlobToStringConverter implements Converter<Blob, String> {

        /**
         * 单实例.
         */
        INSTANCE;

        @Override
        public String convert(Blob source) {
            return BlobType.INSTANCE.toString(source);
        }
    }

}
