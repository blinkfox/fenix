package com.blinkfox.fenix.bean;

import com.blinkfox.fenix.consts.Const;

import java.util.Map;

import org.dom4j.Node;

/**
 * 构建动态 JPQL 或 SQL 语句和命名参数相关的封装实体类.
 *
 * <p>使用此实体类，便于该类中的各种属性参数在类或方法间进行传递.</p>
 *
 * @author blinkfox on 2019-08-04.
 * @see SqlInfo
 */
public final class BuildSource {

    /**
     * XML 文件对应的命名空间.
     */
    private String namespace;

    /**
     * JPQL 或 SQL 的拼接信息.
     */
    private SqlInfo sqlInfo;

    /**
     * Dom4j 对应的 XML 节点.
     */
    private Node node;

    /**
     * 基于 JPQL 接口方法封装的参数对象Map.
     */
    private Map<String, Object> paramMap;

    /**
     * 拼接 JPQL 或者 SQL 片段时的前缀，如: 'and'、'or' 等.
     */
    private String prefix;

    /**
     * 拼接 JPQL 或者 SQL 片段的关键标记符号，如：'>'、'='、'LIKE' 等.
     */
    private String symbol;

    /**
     * 仅仅有 SqlInfo 的构造方法.
     *
     * @param sqlInfo SQL 拼接信息
     */
    public BuildSource(SqlInfo sqlInfo) {
        this.sqlInfo = sqlInfo;
        resetPrefix();
        resetSymbol();
    }

    /**
     * 含 XML 命名空间、SqlInfo、Node 节点、Map 参数上下文的构造方法.
     *
     * @param namespace XML 命名空间
     * @param sqlInfo SQL 拼接和参数对象
     * @param node 某查询 Fenix 的 dom4j 节点
     * @param paramMap Map 参数对象
     */
    public BuildSource(String namespace, SqlInfo sqlInfo, Node node, Map<String, Object> paramMap) {
        this.namespace = namespace;
        this.sqlInfo = sqlInfo;
        this.node = node;
        this.paramMap = paramMap;
        resetPrefix();
        resetSymbol();
    }

    /**
     * 重置前缀为默认一个空格.
     *
     * <p>注：为了防止 SQL 拼接时连在一起，默认前缀为一个空格的字符串.</p>
     */
    public void resetPrefix() {
        this.prefix = Const.ONE_SPACE;
    }

    /**
     * 重置 SQL 操作符为默认一个空格.
     *
     * <p>为了防止 SQL 拼接时连在一起，默认 SQL 操作符为一个空格的字符串.</p>
     */
    public void resetSymbol() {
        this.symbol = Const.ONE_SPACE;
    }

}
