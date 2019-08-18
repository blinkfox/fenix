package com.blinkfox.fenix.bean;

import com.blinkfox.fenix.consts.Const;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import org.dom4j.Node;

/**
 * 构建动态 JPQL 或 SQL 语句和命名参数相关的封装实体类.
 *
 * <p>使用此实体类，便于该类中的各种属性参数在类或方法间进行传递.</p>
 *
 * @author blinkfox on 2019-08-04.
 * @see SqlInfo
 */
@Getter
@Setter
@Accessors(chain = true)
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
     * 解析表达式或者渲染模版时需要的上下文参数（一般是 Bean 或者 map）.
     */
    private Object context;

    /**
     * 拼接 JPQL 或者 SQL 片段时的前缀，如: 'and'、'or' 等.
     */
    private String prefix;

    /**
     * 拼接 JPQL 或者 SQL 片段的关键标记符号，如：'>'、'='、'LIKE' 等.
     */
    private String symbol;

    /**
     * 其它资源条件，这是一个不定值.
     * <p>注：通常情况下这个值是 NULL，如果某些情况下，你需要传递额外的参数值，可以通过这个属性来传递，
     *      是为了方便传递或处理数据而设计的.</p>
     */
    private Map<String, Object> others;

    /**
     * 仅含 {@link SqlInfo} 实例的构造方法.
     *
     * @param sqlInfo SQL 拼接和参数对象
     */
    public BuildSource(SqlInfo sqlInfo) {
        this.sqlInfo = sqlInfo;
        resetPrefix();
        resetSymbol();
    }

    /**
     * 含 XML 命名空间、SqlInfo、Node 节点、上下文参数的构造方法.
     *
     * @param namespace XML 命名空间
     * @param sqlInfo SQL 拼接和参数对象
     * @param node 某查询 Fenix 的 dom4j 节点
     * @param context （一般是 Bean 或者 map）
     */
    public BuildSource(String namespace, SqlInfo sqlInfo, Node node, Object context) {
        this.namespace = namespace;
        this.sqlInfo = sqlInfo;
        this.node = node;
        this.context = context;
        resetPrefix();
        resetSymbol();
    }

    /**
     * 重置前缀为默认一个空格.
     *
     * <p>注：为了防止 SQL 拼接时连在一起，默认前缀为一个空格的字符串.</p>
     */
    public void resetPrefix() {
        this.prefix = Const.SPACE;
    }

    /**
     * 重置 SQL 操作符为默认一个空格.
     *
     * <p>为了防止 SQL 拼接时连在一起，默认 SQL 操作符为一个空格的字符串.</p>
     */
    public void resetSymbol() {
        this.symbol = Const.SPACE;
    }

    /**
     * 重置前缀、 SQL 操作符、和 others 参数的值.
     */
    public void reset() {
        this.prefix = Const.SPACE;
        this.symbol = Const.SPACE;
        this.others = null;
    }

}
