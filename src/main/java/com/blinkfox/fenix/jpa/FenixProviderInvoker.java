package com.blinkfox.fenix.jpa;

import com.blinkfox.fenix.bean.SqlInfo;
import com.blinkfox.fenix.exception.FenixException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.repository.query.Param;

/**
 * FenixProviderSetter.
 *
 * @author blinkfox on 2019-08-11.
 */
public class FenixProviderInvoker {

    public static SqlInfo invoke(Class<?> cls, String method, Map<String, Object> paramMap) {
        Method[] methods = cls.getMethods();
        for (Method m : methods) {
            if (m.getName().equals(method)) {
                Parameter[] params = m.getParameters();
                List<Object> list = new ArrayList<>();
                for (Parameter p : params) {
                    Param param = p.getAnnotation(Param.class);
                    if (param != null) {
                        String paramName = param.value();
                        Object obj = paramMap.get(paramName);
                        list.add(obj);
                    } else {
                        list.add(null);
                    }
                }

                try {
                    return (SqlInfo) m.invoke(cls.newInstance(), list.toArray());
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new FenixException("创建实例异常！", e);
                } catch (InvocationTargetException e) {
                    throw new FenixException("调用方法异常！", e);
                }
            }
        }

        throw new FenixException("未找到对应的方法！");
    }

}
