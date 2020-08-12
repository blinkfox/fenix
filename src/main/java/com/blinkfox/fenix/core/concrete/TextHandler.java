package com.blinkfox.fenix.core.concrete;

import com.blinkfox.fenix.bean.BuildSource;
import com.blinkfox.fenix.bean.SqlInfo;
import com.blinkfox.fenix.consts.Const;
import com.blinkfox.fenix.consts.XpathConst;
import com.blinkfox.fenix.core.FenixContext;
import com.blinkfox.fenix.core.FenixHandler;
import com.blinkfox.fenix.core.builder.XmlSqlInfoBuilder;
import com.blinkfox.fenix.exception.FenixException;
import com.blinkfox.fenix.helper.ParseHelper;
import com.blinkfox.fenix.helper.XmlNodeHelper;
import org.dom4j.Node;

/**
 * 用于生成 '任意' 文本的 JPQL 或者 SQL 片段的 {@link FenixHandler} 接口的实现类.
 *
 * <p>XML 标签示例如：</p>
 * <ul>
 *     <li>{@code <text match="" value=""></text>}</li>
 * </ul>
 * <p>注：</p>
 * <ul>
 *     <li>获取到 match 字段的值，如果为空或者为 true，就生成此 SQL 片段；</li>
 *     <li>{@code <text></text>} 标签中只能是文本值，不能包含其他的 XML 标签；</li>
 *     <li>value 的类型必须是 Map 类型的，否则将抛出 {@link FenixException} 异常；</li>
 *     <li>Map 中的 key 必须是“死”字符串，用于和 JPQL 的命名参数相呼应，value 的值才可以被动态解析；</li>
 * </ul>
 *
 * @author blinkfox on 2019-08-07.
 * @see NormalHandler
 * @see LikeHandler
 * @see InHandler
 * @since v1.0.0
 */
public class TextHandler implements FenixHandler {

    /**
     * 根据 {@link BuildSource} 参数构建 {@code <text></text>} 标签中的 JPQL 或者 SQL 语句片段的信息.
     *
     * <p>如果有非文本节点则抛出 {@link FenixException} 异常.</p>
     *
     * @param source {@link BuildSource} 构建资源参数
     */
    @Override
    public void buildSqlInfo(BuildSource source) {
        Node node = source.getNode();
        if (ParseHelper.isMatch(XmlNodeHelper.getNodeAttrText(node, XpathConst.ATTR_MATCH), source.getContext())) {
            // 获取所有子节点，如果子节点 node 是文本节点，则直接获取其文本，并将其拼接起来.
            SqlInfo sqlInfo = source.getSqlInfo();
            for (Node n : node.selectNodes(XpathConst.ATTR_CHILD)) {
                if (Const.NODETYPE_TEXT.equals(n.getNodeTypeName())) {
                    FenixContext.buildPlainTextSqlInfo(sqlInfo, n.getText());
                } else {
                    throw new FenixException("【Fenix 异常提示】<text></text> 标签中包含了【" + n.getName() + "】的 XML 标签，"
                            + "只能是文本元素，请检查！");
                }
            }

            // 获取、解析并拼接 JPQL 的参数.
            new XmlSqlInfoBuilder(source)
                    .buildTextSqlParams(XmlNodeHelper.getNodeAttrText(source.getNode(), XpathConst.ATTR_VALUE));
        }
    }

}
