package com.blinkfox.fenix.bean;

import com.blinkfox.fenix.consts.Const;

import java.util.Map;

import org.dom4j.Node;

/**
 * 构建动态 JPQL 或 SQL 语句和命名参数相关的封装实体类.
 *
 * @author blinkfox on 2019-08-04.
 */
public final class BuildSource {

    /**
     * XML 文件对应的命名空间.
     */
    private String namespace;

    /**
     * SQL 拼接信息.
     */
    private SqlInfo sqlInfo;

    /**
     * XML节点.
     */
    private Node node;

    /**
     * 基于 JPQL 接口方法封装的 Map 型参数对象上下文.
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
     * @param sqlInfo SQL拼接和参数对象
     * @param node 某查询zealot的dom4j的节点
     * @param paramMap Map 参数对象
     */
    public BuildSource(String namespace, SqlInfo sqlInfo, Node node, Map<String, Object> paramMap) {
        super();
        this.namespace = namespace;
        this.sqlInfo = sqlInfo;
        this.node = node;
        this.paramMap = paramMap;
        resetPrefix();
        resetSymbol();
    }

    /**
     * 重置前缀为默认一个空格.
     * <p>注：为了防止SQL拼接时连在一起，默认前缀为一个空格的字符串，后缀为空字符串.</p>
     */
    public void resetPrefix() {
        this.prefix = Const.ONE_SPACE;
    }

    /**
     * 重置后缀为默认值.
     * <p>为了防止SQL拼接时连在一起，默认后缀为一个空格的字符串.</p>
     */
    public void resetSymbol() {
        this.symbol = Const.ONE_SPACE;
    }

}
