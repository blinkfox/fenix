package com.blinkfox.fenix.jpa;

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
 * FenixProviderSetter.
 *
 * @author blinkfox on 2019-08-11.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class FenixProviderInvoker {

    /**
     * 根据被调用类的 class、被调用类的方法名和参数的 Map 映射关系来调用此方法.
     *
     * @param cls 被调用类的 class
     * @param method 被调用类的方法名
     * @param paramMap 被调用类的方法的参数 Map 映射关系，即 key 是参数名，value 是参数值.
     * @return {@link SqlInfo} 对象
     */
    static SqlInfo invoke(Class<?> cls, String method, Map<String, Object> paramMap) {
        Method[] methods = cls.getMethods();
        int n = methods.length;
        for (Method m : methods) {
            if (m.getName().equals(method)) {
                Parameter[] params = m.getParameters();
                List<Object> paramValues = new ArrayList<>(n);
                for (Parameter p : params) {
                    Param param = p.getAnnotation(Param.class);
                    if (param != null) {
                        String paramName = param.value();
                        Object obj = paramMap.get(paramName);
                        paramValues.add(obj);
                    } else {
                        paramValues.add(null);
                    }
                }

                return invokeMethod(cls, m, paramValues);
            }
        }

        throw new FenixException("未找到对应的方法！");
    }

    /**
     * 正式调用某个 class 的某个方法.
     *
     * @param cls 被调用类的 class
     * @param m 被调用类的方法名
     * @param paramValues 参数值的集合.
     * @return {@link SqlInfo} 对象
     */
    private static SqlInfo invokeMethod(Class<?> cls, Method m, List<Object> paramValues) {
        try {
            return (SqlInfo) m.invoke(cls.newInstance(), paramValues.toArray());
        } catch (InstantiationException | IllegalAccessException e) {
            throw new FenixException("创建实例异常！", e);
        } catch (InvocationTargetException e) {
            throw new FenixException("调用方法异常！", e);
        }
    }

}
