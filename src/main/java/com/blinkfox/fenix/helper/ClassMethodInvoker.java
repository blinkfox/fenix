package com.blinkfox.fenix.helper;

import com.blinkfox.fenix.bean.SqlInfo;
import com.blinkfox.fenix.exception.FenixException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.springframework.data.repository.query.Param;

/**
 * 通过仿射调用 class 方法中的指定方法的工具类.
 *
 * @author blinkfox on 2019-08-11.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ClassMethodInvoker {

    /**
     * 根据被调用类的 class、被调用类的方法名和参数的 Map 映射关系来调用此方法.
     *
     * @param cls 被调用类的 class
     * @param method 被调用类的方法名
     * @param paramMap 被调用类的方法的参数 Map 映射关系，即 key 是参数名，value 是参数值.
     * @return {@link SqlInfo} 对象
     */
    public static SqlInfo invoke(Class<?> cls, String method, Map<String, Object> paramMap) {
        Method[] methods = cls.getMethods();
        int n = methods.length;
        for (Method m : methods) {
            if (m.getName().equals(method)) {
                List<Object> paramValues = new ArrayList<>(n);
                for (Parameter p : m.getParameters()) {
                    Param param = p.getAnnotation(Param.class);
                    paramValues.add(param != null ? paramMap.get(param.value()) : null);
                }

                return invokeMethod(cls, m, paramValues);
            }
        }

        throw new FenixException("【Fenix 异常】未找到【" + cls.getName() + "】类中可执行的公共【" + method + "】方法，"
                + "请检查该方法是否存在或者访问权限是 public 型的！");
    }

    /**
     * 正式通过反射调用某个 class 的某个方法.
     *
     * @param cls 被调用类的 class
     * @param m 被调用类的方法名
     * @param paramValues 参数值的集合.
     * @return {@link SqlInfo} 对象
     */
    private static SqlInfo invokeMethod(Class<?> cls, Method m, List<Object> paramValues) {
        try {
            m.setAccessible(true);
            return (SqlInfo) m.invoke(cls.newInstance(), paramValues.toArray());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new FenixException("【Fenix 异常】创建【" + cls.getName() + "】类的实例异常，请检查构造方法是否是无参 public 型的，"
                    + "或者检查调用的【" + m.getName() + "】方法是否是 public 型的！", e);
        }
    }

}
