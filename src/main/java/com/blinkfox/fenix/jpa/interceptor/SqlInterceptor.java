package com.blinkfox.fenix.jpa.interceptor;

import org.hibernate.EmptyInterceptor;

import java.lang.reflect.Method;

/**
 * 定义这个类的目的是仿照 Hibernate 的 Interceptor 提供一个在执行时膝盖sql的机会
 * @see EmptyInterceptor#onPrepareStatement(java.lang.String)
 */
public interface SqlInterceptor {

    public default String onPrepareStatement(Method method, String sql) {
        return sql;
    }

}
