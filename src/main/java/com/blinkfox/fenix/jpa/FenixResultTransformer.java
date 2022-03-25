package com.blinkfox.fenix.jpa;

import com.blinkfox.fenix.exception.FenixException;
import com.blinkfox.fenix.helper.StringHelper;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Blob;
import java.sql.Clob;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.hibernate.type.BlobType;
import org.hibernate.type.descriptor.java.DataHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.NotWritablePropertyException;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.convert.JodaTimeConverters;

/**
 * 自定义查询结果的转换器.
 *
 * @author blinkfox on 2019-10-08.
 * @author blinkfox on 2022-03-25 (v2.7.0) 做了代码重构，公共代码集成自 {@link AbstractResultTransformer}
 * @since v1.1.0
 */
public class FenixResultTransformer extends AbstractResultTransformer {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 4519223959994503529L;

    private static final DefaultConversionService conversionService = new DefaultConversionService();

    /**
     * 返回结果各属性字段名称及对应的对象间的映射关系 Map.
     */
    private transient Map<String, PropertyDescriptor> fieldMap;

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
     * 结果 类 class 的 Setter 方法.
     *
     * @param resultClass 结果类的 class
     */
    @Override
    public void setResultClass(Class<?> resultClass) {
        // 调用父类设置结果 class 的方法.
        super.setResultClassValue(resultClass);

        // 将返回结果类中的所有属性保存到 Map 中，便于后续快速获取和判断.
        PropertyDescriptor[] propDescriptors = BeanUtils.getPropertyDescriptors(this.resultClass);
        this.fieldMap = new HashMap<>(propDescriptors.length);
        for (PropertyDescriptor propDescriptor : propDescriptors) {
            this.fieldMap.put(propDescriptor.getName().toLowerCase(), propDescriptor);
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
        Object resultObject;
        try {
            resultObject = this.resultClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException
                | NoSuchMethodException | InvocationTargetException e) {
            throw new FenixException("实例化【" + this.resultClass + "】类出错，请检查该类是否包含可公开访问的无参构造方法！", e);
        }

        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(resultObject);
        beanWrapper.setConversionService(conversionService);

        // 遍历设置各个属性对应的值.
        for (int i = 0, len = aliases.length; i < len; i++) {
            String column = aliases[i];
            if (StringHelper.isBlank(column)) {
                throw new FenixException("【Fenix 异常】要映射为【" + this.resultClass + "】实体的查询结果列为空，"
                        + "请检查并保证每一个查询结果列都必须用【as】后加“别名”的方式！");
            }

            PropertyDescriptor propDescriptor = this.fieldMap.get(column.replaceAll(" ", "").toLowerCase());
            if (propDescriptor == null) {
                continue;
            }

            try {
                beanWrapper.setPropertyValue(propDescriptor.getName(), tuple[i]);
            } catch (NotWritablePropertyException | TypeMismatchException e) {
                throw new FenixException("【Fenix 异常】设置字段【" + column + "】的值到属性【"
                        + propDescriptor.getName() + "】中出错，请检查该字段或属性是否存在或者可公开访问！", e);
            }
        }
        return resultObject;
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
