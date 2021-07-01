package com.blinkfox.fenix.helper;

import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * 注解工具类
 */
public class AnnotationHelper {

    private AnnotationHelper(){

    }

    /**
     * 运行时修改注解的值
     * @param annotation   注解
     * @param propertyName 注解的属性
     * @param newValue     属性的新值
     */
    public static void updateAnnotationProperty(Annotation annotation, String propertyName, Object newValue){
        Object handler = Proxy.getInvocationHandler(annotation);
        try {
            // 获取注解上的配置字段
            Field f = ReflectionUtils.findField(handler.getClass(), "memberValues");
            ReflectionUtils.makeAccessible(f);
            Map<String, Object> memberValues = (Map<String, Object>)ReflectionUtils.getField(f, handler);

            // 取出原来的旧的值
            Object oldValue = memberValues.get(propertyName);

            // 更新注解的属性值
            memberValues.put(propertyName, newValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
