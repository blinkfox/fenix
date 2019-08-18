package com.blinkfox.fenix.core.concrete;

import com.blinkfox.fenix.bean.BuildSource;
import com.blinkfox.fenix.consts.XpathConst;
import com.blinkfox.fenix.core.FenixHandler;
import com.blinkfox.fenix.core.FenixXmlBuilder;
import com.blinkfox.fenix.exception.NodeNotFoundException;
import com.blinkfox.fenix.helper.ParseHelper;
import com.blinkfox.fenix.helper.StringHelper;
import com.blinkfox.fenix.helper.XmlNodeHelper;

import org.dom4j.Node;

/**
 * 'import' 标签是用于导入其它或公共 fenix 节点的 JPQL 或者 SQL 片段的 {@link FenixHandler} 接口的实现类.
 *
 * <p>XML 标签示例如：</p>
 * <ul>
 *     <li>'&lt;import match="" namespace="" fenixId="" value="" /&gt;'</li>
 * </ul>
 * <p>注：</p>
 * <ul>
 *     <li>获取到 match 字段的值，如果为空或者为 true，就生成此 SQL 片段；</li>
 *     <li>仅 fenixId 是必填属性，如果不填写 namespace，则代表就是从本 XML 命名空间中导入 fenix 节点；</li>
 *     <li>如果 value 不为空，则视为将此 value 的解析值再次传入到引入的模板中作为新的上下文参数；</li>
 * </ul>
 *
 * @author blinkfox on 2019-08-07.
 * @see NormalHandler
 * @see LikeHandler
 * @see InHandler
 * @see TextHandler
 */
public class ImportHandler implements FenixHandler {

    /**
     * 根据 {@link BuildSource} 参数构建 '&lt;import /&gt;' 标签中的 JPQL 或者 SQL 语句片段的信息.
     *
     * <p>如果 match 属性为空或者 match 属性中的表达式的值是 true，则生成此 JPQL 或者 SQL 的语句和参数.</p>
     *
     * @param source {@link BuildSource} 构建资源参数
     */
    @Override
    public void buildSqlInfo(BuildSource source) {
        // 判断 match 中计算的结果是 false，则不生成 此 JPQL 或者 SQL 片段.
        if (ParseHelper.isNotMatch(XmlNodeHelper.getNodeAttrText(source.getNode(), XpathConst.ATTR_MATCH),
                source.getContext())) {
            return;
        }

        // 获取命名空间的值，如果 import 中的命名空间是空的，则视为从本 XML 中的命名空间中去查找 fenixId.
        String nameSpaceText = XmlNodeHelper.getNodeAttrText(source.getNode(), XpathConst.ATTR_NAME_SPACE);
        String nameSpace = StringHelper.isNotBlank(nameSpaceText) ? nameSpaceText : source.getNamespace();

        // 获取 fenixId 及该 fenixId 所对应的 XML 节点.
        String fenixId = XmlNodeHelper.getNodeAttrText(source.getNode(), XpathConst.ATTR_FENIX_ID);
        if (StringHelper.isBlank(fenixId)) {
            throw new NodeNotFoundException("【Fenix 异常提示】<import /> 标签中存在 fenixId 为空的情况，请检查！");
        }

        Node node = XmlNodeHelper.getNodeBySpaceAndId(nameSpace, fenixId);
        if (node == null) {
            throw new NodeNotFoundException("【Fenix 异常提示】从 <import /> 标签中，未找到 namespace 为【" + nameSpace
                    + "】，fenixId 为【" + fenixId + "】的 XML 节点，请检查！");
        }

        // 获取 valueText 值，如果 valueText 不为空，则视为将此 valueText 的解析值再次传入到引入的模板中作为新的上下文参数.
        // 否则使用默认上下文参数对象传入到待解析的引入模板中.
        String valueText = XmlNodeHelper.getNodeAttrText(source.getNode(), XpathConst.ATTR_VALUE);
        FenixXmlBuilder.buildSqlInfo(nameSpace, source.getSqlInfo(), node, StringHelper.isNotBlank(valueText)
                        ? ParseHelper.parseExpressWithException(valueText, source.getContext()) : source.getContext());
    }

}
