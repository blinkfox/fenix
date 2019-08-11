package com.blinkfox.fenix.core.builder;

import com.blinkfox.fenix.bean.BuildSource;

import java.util.Collection;

/**
 * 构建使用Java拼接sql片段的工具类.
 * @author blinkfox on 2017-04-01.
 */
public class JavaSqlInfoBuilder extends SqlInfoBuilder {

    /**
     * 基于 {@link BuildSource} 实例的构造方法.
     */
    public JavaSqlInfoBuilder(BuildSource source) {
        super(source);
    }

    /**
     * 构建" IN "范围查询的sql信息.
     * @param fieldText 数据库字段文本
     * @param values 对象集合
     */
    public void buildInSqlByCollection(String fieldText, Collection<Object> values) {
        // TODO 待完善.
        //super.buildInSql(fieldText, values == null ? null : values.toArray());
    }

}
