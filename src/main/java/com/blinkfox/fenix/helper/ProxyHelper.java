package com.blinkfox.fenix.helper;

import com.blinkfox.fenix.exception.FenixException;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.util.ClassUtils;

/**
 * 代理相关处理的工具类.
 *
 * @author blinkfox 2019-10-08.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProxyHelper {

    /**
     * 获取代理的目标对象.
     *
     * @param proxy 代理对象
     * @param <T> 范型
     * @return 目标对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T getTarget(Object proxy) {
        if (Proxy.isProxyClass(proxy.getClass())) {
            return getJdkProxyTargetObject(proxy);
        } else if (ClassUtils.isCglibProxy(proxy)) {
            return getCglibProxyTargetObject(proxy);
        } else {
            return (T) proxy;
        }
    }

    /**
     * 获取 JDK 动态代理的目标对象.
     *
     * @param proxy 代理对象
     * @param <T> 范型
     * @return 目标对象
     */
    @SuppressWarnings("unchecked")
    private static <T> T getJdkProxyTargetObject(Object proxy) {
        try {
            Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
            h.setAccessible(true);
            Object proxyObj = h.get(proxy);
            Field f = proxyObj.getClass().getDeclaredField("target");
            f.setAccessible(true);
            return (T) f.get(proxyObj);
        } catch (Exception e) {
            throw new FenixException("【Fenix 异常】获取 JDK 动态代理的目标对象出错！", e);
        }
    }

    /**
     * 获取 cglib 动态代理的目标对象.
     *
     * @param proxy 代理对象
     * @param <T> 范型
     * @return 目标对象
     */
    @SuppressWarnings("unchecked")
    private static <T> T getCglibProxyTargetObject(Object proxy) {
        try {
            Field h = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
            h.setAccessible(true);
            Object dynamicAdvisedInterceptor = h.get(proxy);
            Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
            advised.setAccessible(true);
            return (T) (((AdvisedSupport) advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget());
        } catch (Exception e) {
            throw new FenixException("【Fenix 异常】获取 cglib 动态代理的目标对象出错！", e);
        }
    }

}
