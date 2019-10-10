package com.blinkfox.fenix.bean;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * 构造用于拼接 JPQL (或 SQL) 语句的拼接器和 Map 参数.
 *
 * @author blinkfox on 2019-08-04.
 */
@Getter
@Setter
public class SqlInfo {

    /**
     * 拼接 JPQL 语句的 StringBuilder 对象.
     */
    private StringBuilder join;

    /**
     * JPQL 语句对应的 Map 参数.
     */
    private Map<String, Object> params;

    /**
     * 最终生成的 JPQL 或 SQL 语句.
     */
    private String sql;

    /**
     * 返回的结果类型字符串.
     * 通常情况下是实体 Bean 类的全路径名.
     *
     * @since v1.1.0
     */
    private String resultType;

    /**
     * 默认构造方法.
     */
    public SqlInfo() {
        this.join = new StringBuilder();
        this.params = new HashMap<>();
    }

    /**
     * 如果存在某子 SQL 字符串，则移除该子 SQL 字符串，常用于来消除 'WHERE 1 = 1 AND' 或其他不需要的SQL字符串的场景.
     * 注意该方法不会移除其对应的参数，所以，这里只应该移除静态 SQL 字符串，不应该移除包含命名参数占位符的 SQL.
     *
     * @param subSql 静态子 SQL 片段
     * @return {@code SqlInfo} 实例
     */
    public SqlInfo removeIfExist(String subSql) {
        this.sql = subSql != null && sql.contains(subSql) ? sql.replaceAll(subSql, "") : sql;
        return this;
    }

    /**
     * 设置查询的自定义返回结果类型的 class，这里主要是指实体 Bean 类的 class 实例.
     *
     * @param resultTypeClass 实体 Bean 类的 class 实例
     * @return {@code SqlInfo} 实例
     * @since v1.1.0
     */
    public SqlInfo setResultTypeClass(Class<?> resultTypeClass) {
        this.resultType = resultTypeClass.getName();
        return this;
    }

}
