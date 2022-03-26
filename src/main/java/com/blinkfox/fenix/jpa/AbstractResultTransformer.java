package com.blinkfox.fenix.jpa;

import com.blinkfox.fenix.exception.FenixException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Blob;
import java.sql.Clob;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Setter;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.type.BlobType;
import org.hibernate.type.descriptor.java.DataHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.NotWritablePropertyException;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;

/**
 * 抽象的、具有公共代码的结果转换类.
 *
 * @author blinkfox on 2022-03-25.
 * @see FenixResultTransformer
 * @since 2.7.0
 */
public abstract class AbstractResultTransformer implements ResultTransformer {

    /**
     * 返回结果类中所有属性描述信息的映射关系 Map，其中 key 为结果类 class 的全类路径名，value 为该类中每个字段所对应反射描述信息的 Map.
     */
    protected static final Map<String, Map<String, PropertyDescriptor>> classPropertiesMap = new ConcurrentHashMap<>();

    /**
     * 全局默认通用的类型转换服务.
     */
    protected static final DefaultConversionService defaultConversionService = new DefaultConversionService();

    static {
        defaultConversionService.addConverter(ClobToStringConverter.INSTANCE);
        defaultConversionService.addConverter(BlobToStringConverter.INSTANCE);
    }

    /**
     * 要转换类型的 class 实例.
     */
    @Setter
    protected Class<?> resultClass;

    /**
     * 做一些初始化操作.
     *
     * <p>本方法会判断这个结果类是否有缓存过，如果没有就初始化缓存该结果类中的各个属性字段信息到 Map 中，便于后续快速判断和使用.</p>
     */
    public void init() {
        Map<String, PropertyDescriptor> fieldsMap = classPropertiesMap.get(this.resultClass.getName());
        if (fieldsMap == null) {
            PropertyDescriptor[] propDescriptors = BeanUtils.getPropertyDescriptors(this.resultClass);
            fieldsMap = new HashMap<>(propDescriptors.length);
            for (PropertyDescriptor propDescriptor : propDescriptors) {
                fieldsMap.put(propDescriptor.getName(), propDescriptor);
            }
            classPropertiesMap.put(this.resultClass.getName(), fieldsMap);
        }
    }

    /**
     * 通过反射创建出一个新的查询结果对象实例，并返回其包装对象 {@link BeanWrapper}.
     *
     * <p>注意：不过该对象实例的所有属性都是空的，待进一步填充.</p>
     *
     * @return 查询结果对象的包装对象
     */
    protected BeanWrapper newResultBeanWrapper() {
        // 构造结果实例.
        Object resultObject;
        try {
            resultObject = this.resultClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException
                | NoSuchMethodException | InvocationTargetException e) {
            throw new FenixException(e, "【Fenix 异常】实例化【{}】类出错，请检查该类是否包含可公开访问的无参构造方法！", this.resultClass.getName());
        }
        return PropertyAccessorFactory.forBeanPropertyAccess(resultObject);
    }

    /**
     * 设置结果对象实例某个属性的属性值.
     *
     * @param beanWrapper 对象 Bean 的包装类
     * @param propDescriptor 属性描述信息的反射对象
     * @param value 属性值
     */
    protected void setResultPropertyValue(
            BeanWrapper beanWrapper, PropertyDescriptor propDescriptor, Object value) {
        if (propDescriptor != null) {
            try {
                beanWrapper.setPropertyValue(propDescriptor.getName(), value);
            } catch (NotWritablePropertyException | TypeMismatchException e) {
                throw new FenixException(e, "【Fenix 异常】设置结果类【{}】的【{}】属性值为【{}】时异常，请检查该属性是否存在或者"
                        + "是否有 public 型的 Getter 方法，或者检查是否支持该字段类型的属性转换！", beanWrapper.getWrappedClass().getName(),
                        propDescriptor.getName(), value);
            }
        }
    }

    /**
     * 转换成集合，直接返回集合本身即可.
     *
     * @param list 集合.
     * @return 集合
     */
    @Override
    public List<?> transformList(List list) {
        return list;
    }

    /**
     * Clob 转换为 String 的转换器类.
     *
     * @author blinkfox 2019-10-08.
     */
    protected enum ClobToStringConverter implements Converter<Clob, String> {

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
    protected enum BlobToStringConverter implements Converter<Blob, String> {

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
