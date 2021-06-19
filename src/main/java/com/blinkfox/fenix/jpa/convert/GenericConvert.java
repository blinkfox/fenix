package com.blinkfox.fenix.jpa.convert;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;
import org.springframework.core.convert.converter.Converter;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;

public class GenericConvert<T> implements Converter<Map, T> {

    private Class<T> clazz;

    public GenericConvert(Class<T> clazz){
        this.clazz = clazz;
    }


    @Override
    public T convert(Map source) {
        try {
            T obj = clazz.newInstance();
            /**
             * 1.这里可以扩展,注入的converter,实现sql查寻出的结果为数据库中带下划线的字段,通过程序转为驼峰命名再设置到实体中
             * 2.也可以做类型转换判断,这里未做类型判断,直接copy到dto中,类型不匹配的时候可能会出错
             */
            copyMapToObj(source, obj);

            return obj;
        } catch (Exception e) {
            throw new FatalBeanException("Jpa结果转换出错, class=" + clazz.getName(), e);
        }
    }

    /**
     * 1.将map中的值copy到bean中对应的字段上
     * @author bazhandao
     * @date 2020-03-26
     * @param map
     * @param target
     * @return
     */
    private void copyMapToObj(Map<String, Object> map, T target) {
        if(map == null || target == null || map.isEmpty()){
            return;
        }

        /**
         * 没有选择字段然后反射注入的原因: 可能存在父类, 那么处理父类的属性就稍微麻烦了,
         * 一般都会提供setter方法,
         * lombok 就不要使用 @Accessors(chain = true) 不然有坑!!!
         */
        PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(clazz);
        for(PropertyDescriptor targetPd : propertyDescriptors) {
            if(targetPd.getWriteMethod() == null) {
                continue;
            }

            try {
                String key = targetPd.getName();
                Object value = map.get(key);
                if (value == null) {
                    continue;
                }

                Method writeMethod = targetPd.getWriteMethod();
                if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                    writeMethod.setAccessible(true);
                }

                writeMethod.invoke(target, value);
            } catch (Exception ex) {
                throw new FatalBeanException("Could not copy properties from source to target", ex);
            }
        }
    }

}
