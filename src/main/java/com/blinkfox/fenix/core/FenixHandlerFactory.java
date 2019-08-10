package com.blinkfox.fenix.core;

/**
 * 标签处理器接口 {@link FenixHandler} 实现类的实例化工厂类.
 *
 * @author blinkfox on 2019-08-10.
 */
@FunctionalInterface
public interface FenixHandlerFactory {

    /**
     * 创建 {@link FenixHandler} 接口下的实现类的实例方法.
     *
     * @return {@link FenixHandler} 实例
     */
    FenixHandler newInstance();

}
