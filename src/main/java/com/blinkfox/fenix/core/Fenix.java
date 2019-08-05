package com.blinkfox.fenix.core;

import com.blinkfox.fenix.bean.BuildSource;
import com.blinkfox.fenix.bean.SqlInfo;
import com.blinkfox.fenix.config.FenixDefaultConfig;
import com.blinkfox.fenix.config.entity.NormalConfig;
import com.blinkfox.fenix.consts.Const;
import com.blinkfox.fenix.consts.XpathConst;
import com.blinkfox.fenix.exception.FenixException;
import com.blinkfox.fenix.exception.NodeNotFoundException;
import com.blinkfox.fenix.helper.ParseHelper;
import com.blinkfox.fenix.helper.SqlInfoPrinter;
import com.blinkfox.fenix.helper.StringHelper;
import com.blinkfox.fenix.helper.XmlNodeHelper;

import java.util.List;
import java.util.Map;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.dom4j.Node;

/**
 * Fenix 解析和生成 JPQL、SQL 语句和参数的核心 API 类.
 *
 * @author blinkfox on 2019-08-05.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Fenix {

    /**
     * 通过传入 fullFenixId（命名空间和 Fenix 节点的 ID）以及 Map 参数对象，
     * 来简单快速的生成和获取 {@link SqlInfo} 信息(有参的SQL).
     *
     * @param fullFenixId XML 命名空间 'namespace' + '.' + 'fenixId' 的值，如: "student.queryStudentById".
     * @param paramMap Map 型参数
     * @return 返回 {@link SqlInfo} 对象
     */
    public static SqlInfo getSqlInfoSimply(String fullFenixId, Map<String, Object> paramMap) {
        if (!fullFenixId.contains(Const.DOT)) {
            throw new FenixException("【Fenix 异常】fullFenixId 参数的值必须是 XML 文件中的 namespace + '.' + fenixId 节点的值，"
                    + "如:【student.queryStudentById】。其中 student 为 namespace, queryStudentById 为 XML 文件中 fenixId。");
        }

        // 从 fullFenixId 中解析出 namespace 和 fenixId 的值，便于后续处理.
        int dotIndex = fullFenixId.lastIndexOf(Const.DOT);
        return getSqlInfo(fullFenixId.substring(0, dotIndex), fullFenixId.substring(dotIndex + 1), paramMap);
    }

    /**
     * 通过传入 Fenix XML 文件对应的命名空间、Fenix 节点的 ID 以及 Map 型参数对象，
     * 来生成和获取 {@link SqlInfo} 信息(有参的SQL).
     *
     * @param namespace XML 命名空间
     * @param fenixId XML 中的 fenixId
     * @param paramMap Map 型参数
     * @return 返回 {@link SqlInfo} 对象
     */
    public static SqlInfo getSqlInfo(String namespace, String fenixId, Map<String, Object> paramMap) {
        if (StringHelper.isBlank(namespace) || StringHelper.isBlank(fenixId)) {
            throw new FenixException("【Fenix 异常】请输入有效的 namespace 或者 fenixId 的值!");
        }

        // 获取 namespace 文档中的指定的 fenixId 的节点对应的 Node 节点，如果是 debug 模式，则实时获取；否则从缓存中获取.
        Node fenixNode = NormalConfig.getInstance().isDebug()
                ? XmlNodeHelper.getNodeBySpaceAndId(namespace, fenixId)
                : FenixDefaultConfig.getFenixs().get(StringHelper.concat(namespace, Const.DOT, fenixId));
        if (fenixNode == null) {
            throw new NodeNotFoundException(StringHelper.format("【Fenix 异常】未找到 namespace 为:【{}】,"
                    + " fenixId 为:【{}】的 XML 节点!", namespace, fenixId));
        }

        // 生成新的 SqlInfo 信息并打印出来.
        SqlInfo sqlInfo = buildNewSqlInfo(namespace, fenixNode, paramMap);
        if (NormalConfig.getInstance().isPrintSqlInfo()) {
            new SqlInfoPrinter().print(sqlInfo, true, namespace, fenixId);
        }
        return sqlInfo;
    }

    /**
     * 构建新的、完整的SqlInfo对象.
     *
     * @param nameSpace xml命名空间
     * @param node dom4j对象节点
     * @param paramObj 参数对象
     * @return 返回SqlInfo对象
     */
    private static SqlInfo buildNewSqlInfo(String nameSpace, Node node, Map<String, Object> paramObj) {
        return buildSqlInfo(nameSpace, new SqlInfo(), node, paramObj);
    }

    /**
     * 构建完整的SqlInfo对象.
     *
     * @param nameSpace xml命名空间
     * @param sqlInfo SqlInfo对象
     * @param node dom4j对象节点
     * @param paramObj 参数对象
     * @return 返回SqlInfo对象
     */
    public static SqlInfo buildSqlInfo(String nameSpace, SqlInfo sqlInfo, Node node, Map<String, Object> paramObj) {
        // 获取所有子节点，并分别将其使用StringBuilder拼接起来
        List<Node> nodes = node.selectNodes(XpathConst.ATTR_CHILD);
        for (Node n: nodes) {
            // 如果子节点node 是文本节点，则直接获取其文本.
            if (Const.NODETYPE_TEXT.equals(n.getNodeTypeName())) {
                sqlInfo.getJoin().append(n.getText());
            } else if (Const.NODETYPE_ELEMENT.equals(n.getNodeTypeName())) {
                // 如果子节点node 是元素节点，则再判断其是什么元素，动态判断条件和参数
                ConditContext.buildSqlInfo(new BuildSource(nameSpace, sqlInfo, n, paramObj), n.getName());
            }
        }

        return buildFinalSql(sqlInfo, paramObj);
    }

    /**
     * 根据标签拼接的SQL信息来生成最终的SQL.
     *
     * @param sqlInfo sql及参数信息
     * @param paramObj 参数对象信息
     * @return 返回SqlInfo对象
     */
    private static SqlInfo buildFinalSql(SqlInfo sqlInfo, Map<String, Object> paramObj) {
        // 得到生成的SQL，如果有MVEL的模板表达式，则执行计算出该表达式来生成最终的SQL
        String sql = sqlInfo.getJoin().toString();
        sql = ParseHelper.parseTemplate(sql, paramObj);
        sqlInfo.setSql(StringHelper.replaceBlank(sql));
        return sqlInfo;
    }

}