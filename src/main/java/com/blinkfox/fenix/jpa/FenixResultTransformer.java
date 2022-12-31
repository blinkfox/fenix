package com.blinkfox.fenix.jpa;

import com.blinkfox.fenix.exception.FenixException;
import com.blinkfox.fenix.helper.StringHelper;
import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.convert.Jsr310Converters;

/**
 * 自定义查询结果的转换器.
 *
 * <p>注意：该类在 v2.7.0 版本进行了较大的重构，为了保持向以前的版本兼容，诸多功能仍然和以前保持一致。
 * 但新版本建议使用 {@link com.blinkfox.fenix.jpa.transformer.UnderscoreTransformer} 等相关的转换器.</p>
 *
 * @author blinkfox on 2019-10-08.
 * @author blinkfox on 2022-03-25 (v2.7.0) 做了代码重构，公共代码集成自 {@link AbstractResultTransformer}
 * @see com.blinkfox.fenix.jpa.transformer.UnderscoreTransformer
 * @see com.blinkfox.fenix.jpa.transformer.PrefixUnderscoreTransformer
 * @see com.blinkfox.fenix.jpa.transformer.ColumnAnnotationTransformer
 * @since v1.1.0
 */
public class FenixResultTransformer extends AbstractResultTransformer {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 4519223959994503529L;

    /**
     * 返回结果类中所有属性描述信息的映射关系 Map.
     *
     * <p>其中 key 为结果类 class 的全类路径名，value 为实体类中 Java 属性的 Map.
     * 该子 Map 的 key是属性的小写名称，value 是属性真正的名称.</p>
     */
    protected static final Map<String, Map<String, String>> classIgnoreCaseFieldsMap = new ConcurrentHashMap<>();

    private static final DefaultConversionService oldConversionService = new DefaultConversionService();

    static {
        // 添加一些默认的 ConversionService.
        Collection<Converter<?, ?>> convertersToRegister = Jsr310Converters.getConvertersToRegister();
        for (Converter<?, ?> converter : convertersToRegister) {
            oldConversionService.addConverter(converter);
        }
        oldConversionService.addConverter(ClobToStringConverter.INSTANCE);
        oldConversionService.addConverter(BlobToStringConverter.INSTANCE);
    }

    /**
     * 做一些初始化操作.
     *
     * <p>本方法的功能能向兼容的以前版本，重写了父类中的 {@code init()} 方法，结果类中属性的字段都是小写的，不区分大小写.</p>
     */
    @Override
    public void init() {
        Map<String, String> fieldsMap = classIgnoreCaseFieldsMap.get(this.resultClass.getName());
        if (fieldsMap == null) {
            PropertyDescriptor[] propDescriptors = BeanUtils.getPropertyDescriptors(this.resultClass);
            fieldsMap = new HashMap<>(propDescriptors.length);
            for (PropertyDescriptor propDescriptor : propDescriptors) {
                String propName = propDescriptor.getName();
                fieldsMap.put(propName.toLowerCase(), propName);
            }
            classIgnoreCaseFieldsMap.put(this.resultClass.getName(), fieldsMap);
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
        // 获取实际数据库查询结果对象的字段信息和 result class 类的属性信息
        BeanWrapper beanWrapper = super.newResultBeanWrapper();
        beanWrapper.setConversionService(oldConversionService);
        Map<String, String> fieldsMap = classIgnoreCaseFieldsMap.get(super.resultClass.getName());

        // 遍历设置各个属性对应的值.
        for (int i = 0, len = aliases.length; i < len; ++i) {
            String column = aliases[i];
            if (StringHelper.isBlank(column)) {
                throw new FenixException("【Fenix 异常】要映射为【" + super.resultClass.getName() + "】实体的查询结果列为空，"
                        + "请检查并保证每一个查询结果列都必须用【as】后加“别名”的方式！");
            }

            // 如果该查询结果列存在，就设置值.
            super.setResultPropertyValue(beanWrapper, fieldsMap.get(column.trim().toLowerCase()), tuple[i]);
        }
        return beanWrapper.getWrappedInstance();
    }

}
