package com.blinkfox.fenix.core;

import java.util.Map;

/**
 * 执行任意自定义操作的函数式接口，目的是方便使用者可自定义任何 SQL 拼接处理等的相关操作.
 *
 * @author blinkfox on 2019-08-11.
 * @see Fenix
 * @since v1.0.0
 */
@FunctionalInterface
public interface FenixAction {

    /**
     * 执行语句.
     *
     * @param join 拼接 SQL 字符串的 {@link StringBuilder} 对象
     * @param params 存放命名参数的 {@link Map} 集合
     */
    void execute(final StringBuilder join, final Map<String, Object> params);

}
