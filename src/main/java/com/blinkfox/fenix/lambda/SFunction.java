package com.blinkfox.fenix.lambda;

import java.io.Serializable;
import java.util.function.Function;

/**
 * @author miemie
 *
 * 抄自 mybatis-plus 项目的 com.baomidou.mybatisplus.core.toolkit.support.SFunction
 *
 * 在Idea 的VM options配置-Djdk.internal.lambda.dumpProxyClasses
 */
@FunctionalInterface
public interface SFunction<T, R> extends Function<T, R>, Serializable {

    R apply(T t);

}
