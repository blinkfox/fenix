package com.blinkfox.fenix.jpa;

import com.blinkfox.fenix.consts.Const;
import com.blinkfox.fenix.exception.FenixException;
import com.blinkfox.fenix.helper.StringHelper;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.util.*;

import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.property.access.internal.PropertyAccessStrategyBasicImpl;
import org.hibernate.property.access.internal.PropertyAccessStrategyChainedImpl;
import org.hibernate.property.access.internal.PropertyAccessStrategyFieldImpl;
import org.hibernate.property.access.internal.PropertyAccessStrategyMapImpl;
import org.hibernate.property.access.spi.PropertyAccessStrategy;
import org.hibernate.property.access.spi.Setter;
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
import org.springframework.data.convert.JodaTimeConverters;
import org.springframework.util.Assert;

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

    private boolean isInitialized;
    private String[] aliases;
    private Setter[] setters;

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
        this.isInitialized = false;

        // 将返回结果类中的所有属性保存到 Map 中，便于后续快速获取和判断.
        this.fieldMap = new HashMap<>();
        PropertyDescriptor[] propDescriptors = BeanUtils.getPropertyDescriptors(this.resultClass);
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
        try {
            if (!this.isInitialized) {
                this.initialize(aliases);
            } else {
                this.check(aliases);
            }

            Object result = this.resultClass.newInstance();

            for (int e = 0; e < aliases.length; ++e) {
                if (this.setters[e] != null) {
                    if (tuple[e] != null
                            && this.setters[e].getMethod() != null
                            && this.setters[e].getMethod().getParameterTypes().length > 0
                            && "java.lang.Character".equals(this.setters[e].getMethod().getParameterTypes()[0].getName())) {
                        this.setters[e].set(result, tuple[e].toString().charAt(0), (SessionFactoryImplementor) null);
                    } else if (tuple[e] != null
                            && this.setters[e].getMethod() != null
                            && this.setters[e].getMethod().getParameterTypes().length > 0
                            && "java.lang.Long".equals(this.setters[e].getMethod().getParameterTypes()[0].getName())) {
                        this.setters[e].set(result, Long.valueOf(tuple[e].toString()), (SessionFactoryImplementor) null);
                    } else if (tuple[e] != null
                            && this.setters[e].getMethod() != null
                            && this.setters[e].getMethod().getParameterTypes().length > 0
                            && "java.lang.Integer".equals(this.setters[e].getMethod().getParameterTypes()[0].getName())) {
                        this.setters[e].set(result, Integer.valueOf(tuple[e].toString()), (SessionFactoryImplementor) null);
                    } else if (tuple[e] != null
                            && this.setters[e].getMethod() != null
                            && this.setters[e].getMethod().getParameterTypes().length > 0
                            && "java.lang.Float".equals(this.setters[e].getMethod().getParameterTypes()[0].getName())) {
                        this.setters[e].set(result, Float.valueOf(tuple[e].toString()), (SessionFactoryImplementor) null);
                    } else if (tuple[e] != null
                            && this.setters[e].getMethod() != null
                            && this.setters[e].getMethod().getParameterTypes().length > 0
                            && "java.math.BigDecimal".equals(this.setters[e].getMethod().getParameterTypes()[0].getName())) {
                        this.setters[e].set(result, new BigDecimal(tuple[e].toString()), (SessionFactoryImplementor) null);
                    } else if (tuple[e] != null
                            && this.setters[e].getMethod() != null
                            && this.setters[e].getMethod().getParameterTypes().length > 0
                            && "java.lang.Boolean".equals(this.setters[e].getMethod().getParameterTypes()[0].getName())) {
                        if (Const.BOOLEAN_STRING_TRUE.equalsIgnoreCase(tuple[e].toString())
                                || Const.FLAG_STRING_TRUE.equals(tuple[e].toString())) {
                            this.setters[e].set(result, Boolean.TRUE, (SessionFactoryImplementor) null);
                        } else if (Const.BOOLEAN_STRING_FALSE.equalsIgnoreCase(tuple[e].toString())
                                || Const.FLAG_STRING_FALSE.equals(tuple[e].toString())) {
                            this.setters[e].set(result, Boolean.FALSE, (SessionFactoryImplementor) null);
                        }
                    } else if (tuple[e] != null
                            && (Const.BOOLEAN_STRING_TRUE.equals(tuple[e].toString()) || Const.BOOLEAN_STRING_FALSE.equals(tuple[e].toString()))
                            && this.setters[e].getMethod() != null
                            && this.setters[e].getMethod().getParameterTypes().length > 0
                            && "java.lang.Character".equals(this.setters[e].getMethod().getParameterTypes()[0].getName())) {
                        this.setters[e].set(result, Boolean.valueOf(tuple[e].toString()), (SessionFactoryImplementor) null);
                    } else {
                        this.setters[e].set(result, tuple[e], (SessionFactoryImplementor) null);
                    }
                }
            }
            return result;
        } catch (InstantiationException | IllegalAccessException var5) {
            throw new HibernateException("Could not instantiate resultclass: " + this.resultClass.getName());
        }
    }

    private void initialize(String[] aliases) {
        PropertyAccessStrategyChainedImpl propertyAccessStrategy = new PropertyAccessStrategyChainedImpl(new PropertyAccessStrategy[]{PropertyAccessStrategyBasicImpl.INSTANCE, PropertyAccessStrategyFieldImpl.INSTANCE, PropertyAccessStrategyMapImpl.INSTANCE});
        this.aliases = new String[aliases.length];
        this.setters = new Setter[aliases.length];

        Field[] fields = resultClass.getDeclaredFields();

        Class<?> tempClass = resultClass.getSuperclass();
        //获取当前类父类的属性
        while (tempClass != null && !tempClass.getName().toLowerCase().equals("java.lang.object")) {
            Field[] baseFields = tempClass.getDeclaredFields();
            fields = ArrayUtils.addAll(baseFields, fields);
            tempClass = tempClass.getSuperclass(); //得到父类,然后赋给自己
        }
        if (fields == null || fields.length == 0) {
            throw new RuntimeException(resultClass.getName() + " property is null");
        }
        for (int i = 0; i < aliases.length; ++i) {
            String alias = aliases[i];
            if (alias != null) {
                this.aliases[i] = alias;
                for (Field field : fields) {
                    if (field.getName().equalsIgnoreCase(alias.replaceAll("_", ""))) {
                        this.setters[i] = propertyAccessStrategy.buildPropertyAccess(this.resultClass, field.getName()).getSetter();
                        break;
                    }
                }
            }
        }

        this.isInitialized = true;

    }

    private void check(String[] aliases) {
        if (!Arrays.equals(aliases, this.aliases)) {
            throw new IllegalStateException("aliases are different from what is cached; aliases=" + Arrays.asList(aliases) + " cached=" + Arrays.asList(this.aliases));
        }
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
