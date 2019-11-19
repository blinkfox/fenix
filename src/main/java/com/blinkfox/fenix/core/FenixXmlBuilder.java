package com.blinkfox.fenix.core;

import com.blinkfox.fenix.bean.BuildSource;
import com.blinkfox.fenix.bean.SqlInfo;
import com.blinkfox.fenix.config.FenixConfig;
import com.blinkfox.fenix.config.FenixConfigManager;
import com.blinkfox.fenix.consts.Const;
import com.blinkfox.fenix.consts.XpathConst;
import com.blinkfox.fenix.exception.FenixException;
import com.blinkfox.fenix.exception.NodeNotFoundException;
import com.blinkfox.fenix.helper.ParseHelper;
import com.blinkfox.fenix.helper.SqlInfoPrinter;
import com.blinkfox.fenix.helper.StringHelper;
import com.blinkfox.fenix.helper.XmlNodeHelper;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.dom4j.Node;
import org.springframework.util.StringUtils;

/**
 * 用于读取和构建 XML 文件中的 {@link SqlInfo} JPQL 片段和参数的构建器，
 * 请使用 {@link Fenix} 类的 API 来读取和拼接 XML 中的信息.
 *
 * @author blinkfox on 2019/8/12.
 * @see Fenix
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FenixXmlBuilder {

    /**
     * 用于解析出 '#{' 和 '}' 符号之间文本内容的正则表达式 Pattern（包含 '#{' 和 '}').
     */
    private static final Pattern PATTERN = Pattern.compile("(#\\{[^}]+})");

    /**
     * 通过传入 fullFenixId（命名空间和 Fenix 节点的 ID）和上下文参数，
     * 来简单快速的生成和获取 {@link SqlInfo} 信息(有参的SQL).
     *
     * @param fullFenixId XML 命名空间 'namespace' + '.' + 'fenixId' 的值，如: "student.queryStudentById".
     * @param context 上下文参数（一般是 Bean 或者 map）
     * @return 返回 {@link SqlInfo} 对象
     */
    static SqlInfo getXmlSqlInfo(String fullFenixId, Object context) {
        if (!fullFenixId.contains(Const.DOT)) {
            throw new FenixException("【Fenix 异常】fullFenixId 参数的值必须是 XML 文件中的 namespace + '.' + fenixId 节点的值，"
                    + "如:【student.queryStudentById】。其中 student 为 namespace, queryStudentById 为 XML 文件中 fenixId。");
        }

        // 从 fullFenixId 中解析出 namespace 和 fenixId 的值，便于后续处理.
        int dotIndex = fullFenixId.lastIndexOf(Const.DOT);
        return getXmlSqlInfo(fullFenixId.substring(0, dotIndex), fullFenixId.substring(dotIndex + 1), context);
    }

    /**
     * 通过传入 Fenix XML 文件对应的命名空间、Fenix 节点的 ID 以及 Map 型参数对象，
     * 来生成和获取 {@link SqlInfo} 信息(有参的SQL).
     *
     * @param namespace XML 命名空间
     * @param fenixId XML 中的 fenixId
     * @param context 上下文参数（一般是 Bean 或者 map）
     * @return 返回 {@link SqlInfo} 对象
     */
    static SqlInfo getXmlSqlInfo(String namespace, String fenixId, Object context) {
        if (StringHelper.isBlank(namespace) || StringHelper.isBlank(fenixId)) {
            throw new FenixException("【Fenix 异常】请输入有效的 namespace 或者 fenixId 的值，两者之间用点号('.')隔开!");
        }

        // 获取 namespace 文档中的指定的 fenixId 的节点对应的 Node 节点，如果是 debug 模式，则实时获取；否则从缓存中获取.
        Node fenixNode = FenixConfig.getFenixs().get(StringHelper.concat(namespace, Const.DOT, fenixId));
        if (fenixNode == null) {
            throw new NodeNotFoundException(StringHelper.format("【Fenix 异常】未找到 namespace 为:【{}】,"
                    + " fenixId 为:【{}】的 XML 节点!", namespace, fenixId));
        }

        // 生成新的 SqlInfo 信息并打印出来.
        SqlInfo sqlInfo = buildNewSqlInfo(namespace, fenixNode, context);
        if (FenixConfigManager.getInstance().getFenixConfig().isPrintSqlInfo()) {
            new SqlInfoPrinter().print(sqlInfo, namespace, fenixId);
        }
        return sqlInfo;
    }

    /**
     * 构建新的、完整的 {@link SqlInfo} 对象.
     * <p>获取所有子节点，并分别将其使用 StringBuilder 拼接起来.</p>
     * <ul>
     *     <li>如果子节点 node 是文本节点，则直接获取其文本.</li>
     *     <li>如果子节点 node 是元素节点，则再判断其是什么元素，动态判断条件和参数.</li>
     * </ul>
     *
     * @param namespace XML 命名空间
     * @param node dom4j 对象节点
     * @param context 上下文参数（一般是 Bean 或者 map）
     * @return 返回 {@link SqlInfo} 对象
     */
    private static SqlInfo buildNewSqlInfo(String namespace, Node node, Object context) {
        // 创建 SqlInfo 对象，并获取设置 resultType 的属性值.
        SqlInfo sqlInfo = new SqlInfo();
        sqlInfo.setResultType(XmlNodeHelper.getNodeAttrText(node, XpathConst.ATTR_RESULT_TYPE));
        return buildSqlInfo(namespace, sqlInfo, node, context);
    }

    /**
     * 根据已有的 {@link SqlInfo} 信息来追加构建 {@link SqlInfo} 对象.
     * <ul>
     *     <li>如果子节点 node 是文本节点，则直接获取其文本.</li>
     *     <li>如果子节点 node 是元素节点，则再判断其是什么元素，动态判断条件和参数.</li>
     * </ul>
     *
     * @param namespace XML 命名空间
     * @param sqlInfo {@link SqlInfo} 信息
     * @param node dom4j 对象节点
     * @param context 上下文参数（一般是 Bean 或者 map）
     * @return 返回 {@link SqlInfo} 对象
     */
    public static SqlInfo buildSqlInfo(String namespace, SqlInfo sqlInfo, Node node, Object context) {
        List<Node> nodes = node.selectNodes(XpathConst.ATTR_CHILD);
        for (Node n: nodes) {
            String nodeTypeName = n.getNodeTypeName();
            if (Const.NODETYPE_TEXT.equals(nodeTypeName)) {
                FenixContext.buildPlainTextSqlInfo(sqlInfo, n.getText());
            } else if (Const.NODETYPE_ELEMENT.equals(nodeTypeName)) {
                FenixContext.buildSqlInfo(new BuildSource(namespace, sqlInfo, n, context), n.getName());
            }
        }

        // 根据标签拼接得到 SqlInfo 信息，如果有 MVEL 的模板表达式，则执行计算出该表达式，并移除多余的空白字符.
        // 如果 fenix 节点中，removeIfExist 属性值内容不为空，就移除指定的内容.
        renderSqlAndOtherParams(sqlInfo, context);
        String removeText = XmlNodeHelper.getNodeAttrText(node, XpathConst.ATTR_REMOVE);
        return StringHelper.isNotBlank(removeText) ? sqlInfo.removeIfExist(removeText) : sqlInfo;
    }

    /**
     * 根据构建出的 {@link SqlInfo} 信息再做最后的模板渲染，SQL 内容去多余的空白，替换其他命名参数等.
     *
     * <p>该方法主要做如下操作：</p>
     * <ul>
     *     <li>1. 使用 MVEL 渲染 SQL 字符串模板；</li>
     *     <li>2. 替换多余的空白、换行符等为一个空格；</li>
     *     <li>3. 替换 '#{xxx}' 中的内容为 ':xxx' 形式的命名参数；</li>
     * </ul>
     *
     * @param sqlInfo {@link SqlInfo} 信息
     * @param context 上下文参数（一般是 Bean 或者 map）
     */
    private static void renderSqlAndOtherParams(SqlInfo sqlInfo, Object context) {
        String sql = StringHelper.replaceBlank(ParseHelper.parseTemplate(sqlInfo.getJoin().toString(), context));

        // 获取并替换 SQL 字符串中 '#{xxx}' 的内容，并更换成命名参数的方式.
        Matcher matcher = PATTERN.matcher(sql);
        while (matcher.find()) {
            String hashTagText = matcher.group(1);
            String text = hashTagText.substring(2, hashTagText.length() - 1);
            String namedText = StringHelper.fixDot(text);
            sqlInfo.getParams().put(namedText, ParseHelper.parseExpressWithException(text, context));
            sql = sql.replace(hashTagText, Const.COLON + namedText);
        }
        sqlInfo.setSql(sql);
    }

}
