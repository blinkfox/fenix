package com.blinkfox.fenix.jpa.transformer;

import com.blinkfox.fenix.exception.FenixException;
import com.blinkfox.fenix.helper.FieldHelper;
import com.blinkfox.fenix.helper.StringHelper;
import com.blinkfox.fenix.jpa.AbstractResultTransformer;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.persistence.Column;
import org.springframework.beans.BeanWrapper;

/**
 * 字段均含有 {@code @Column(name = "xxx")} 型的对象实体的查询结果对象转换器.
 *
 * <p>用来将各个查询结果列含有 {@code @Column(name = "xxx")} 注解的属性转换为 Java 规范中 VO 实例属性
 * 为小驼峰命名风格（{@code lowerCamelCase}）的对象属性.</p>
 *
 * @author blinkfox on 2022-03-26.
 * @since v2.7.0
 */
public class ColumnAnnotationTransformer extends AbstractResultTransformer {

    /**
     * 返回结果类中所有属性描述信息的映射关系 Map.
     *
     * <p>其中 key 为结果类 class 的全类路径名，value 为该类中每个数据库字段和实体 VO 类属性的子 Map.
     * 其中，子 Map 的 key 为 {@code @Column(name = "xxx")} 注解中的 name 值，value 为对应的实体 VO 类的属性名称.</p>
     */
    protected static final Map<String, Map<String, String>> classFieldsMap = new ConcurrentHashMap<>();

    /**
     * 做一些初始化操作.
     *
     * <p>本方法会判断这个结果类是否有缓存过，如果没有就初始化缓存该结果类中的各个字段名和属性信息到 Map 中，便于后续快速判断和使用.</p>
     */
    @Override
    public void init() {
        Map<String, String> fieldsMap = classFieldsMap.get(this.resultClass.getName());
        if (fieldsMap == null) {
            List<Field> fields = FieldHelper.getAllFieldsList(this.resultClass);
            fieldsMap = new HashMap<>(fields.size());
            for (Field field : fields) {
                Column columnAnnotation = field.getAnnotation(Column.class);
                if (columnAnnotation != null) {
                    fieldsMap.put(columnAnnotation.name().toLowerCase(), field.getName());
                }
            }
            classFieldsMap.put(this.resultClass.getName(), fieldsMap);
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
        Map<String, String> fieldsMap = classFieldsMap.get(super.resultClass.getName());

        // 遍历设置各个属性对应的值.
        for (int i = 0, len = aliases.length; i < len; ++i) {
            String column = aliases[i];
            if (StringHelper.isBlank(column)) {
                throw new FenixException(StringHelper.format("【Fenix 异常】将查询结果转换为【{}】对象时，第【{}】个查询"
                        + "结果列为空，请检查你是否开启了【nativeQuery = true】的原生 SQL 选项或者就要使用【as】“别名”的方式来显示声明"
                        + "查询结果列的名称！", super.resultClass.getName(), i));
            }

            // 如果该查询结果列存在，就设置值.
            super.setResultPropertyValue(beanWrapper, fieldsMap.get(column.toLowerCase()), tuple[i]);
        }
        return beanWrapper.getWrappedInstance();
    }

}
