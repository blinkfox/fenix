package com.blinkfox.fenix.jpa;

import com.blinkfox.fenix.exception.FenixException;
import com.blinkfox.fenix.helper.StringHelper;
import java.lang.reflect.InvocationTargetException;
import java.sql.Blob;
import java.sql.Clob;
import java.util.List;
import lombok.Setter;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.type.descriptor.java.BlobJavaType;
import org.hibernate.type.descriptor.java.DataHelper;
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
     * 做一些初始化操作，默认空实现，各个实现类可视具体情况初始化一些数据.
     */
    public void init() {
        // do nothing
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
     * @param propertyName 属性名称
     * @param value 属性值
     */
    protected void setResultPropertyValue(BeanWrapper beanWrapper, String propertyName, Object value) {
        if (StringHelper.isNotBlank(propertyName)) {
            try {
                beanWrapper.setPropertyValue(propertyName, value);
            } catch (NotWritablePropertyException | TypeMismatchException e) {
                throw new FenixException(e, "【Fenix 异常】设置结果类【{}】的【{}】属性值为【{}】时异常，请检查该属性是否存在或者"
                        + "是否有 public 型的 Setter 方法，或者检查字段类型是否支持 JPA 的默认转换！",
                        beanWrapper.getWrappedClass().getName(), propertyName, value);
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
            return BlobJavaType.INSTANCE.toString(source);
        }
    }

}
