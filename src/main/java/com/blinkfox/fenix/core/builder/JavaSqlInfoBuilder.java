package com.blinkfox.fenix.core.builder;

import com.blinkfox.fenix.bean.BuildSource;

/**
 * 继承自 {@link SqlInfoBuilder} 类的用于构建使用 Java 拼接 JPQL 或者 SQL 语句片段和参数的类.
 *
 * @author blinkfox on 2019-08-11.
 * @see SqlInfoBuilder
 * @see XmlSqlInfoBuilder
 */
public class JavaSqlInfoBuilder extends SqlInfoBuilder {

    /**
     * 基于 {@link BuildSource} 实例的构造方法.
     *
     * @param source 构建的资源参数
     */
    public JavaSqlInfoBuilder(BuildSource source) {
        super(source);
    }

}
