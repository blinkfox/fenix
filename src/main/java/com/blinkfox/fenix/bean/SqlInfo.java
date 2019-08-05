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
     * 默认构造方法.
     */
    public SqlInfo() {
        this.join = new StringBuilder();
        this.params = new HashMap<>();
    }

}
