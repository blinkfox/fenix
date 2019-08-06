package com.blinkfox.fenix.core.concrete;

import com.blinkfox.fenix.bean.BuildSource;
import com.blinkfox.fenix.consts.XpathConst;
import com.blinkfox.fenix.core.FenixHandler;
import com.blinkfox.fenix.core.builder.XmlSqlInfoBuilder;
import com.blinkfox.fenix.helper.ParseHelper;
import com.blinkfox.fenix.helper.StringHelper;
import com.blinkfox.fenix.helper.XmlNodeHelper;

import org.dom4j.Node;

/**
 * 用于生成常规动态 JPQL 或者 SQL 片段的 {@link FenixHandler} 接口的实现类.
 * <p>常规 SQL 处理包括：等值、大于、小于、大于等于、小于等于、...</p>
 * <p>XML 标签示例如：</p>
 * <ul>
 *     <li>'<equal match="" field="" value="" />'</li>
 *     <li>'<andEqual match="" field="" value="" />'</li>
 *     <li>'<orEqual match="" field="" value="" />'</li>
 * </ul>
 *
 * @author blinkfox on 2019-08-06.
 */
public class NormalHandler implements FenixHandler {

    /**
     * 根据构建参数构建常规的 JPQL 或者 SQL 语句片段的信息.
     * <p>获取到 match 字段的值，如果没有或者为 true，就通过 field, value 来生成此 SQL 片段.</p>
     *
     * @param source 构建所需的资源对象
     */
    @Override
    public void buildSqlInfo(BuildSource source) {
        // 获取需要拼接的 XML 节点，判断必填的参数是否为空.
        Node node = source.getNode();

        // 如果没有 match 属性或者 match 属性中的表达式的值是 true，则认为是必然生成此 JPQL 或者 SQL 语句和参数.
        String matchText = XmlNodeHelper.getNodeText(node.selectSingleNode(XpathConst.ATTR_MATCH));
        if (StringHelper.isBlank(matchText) || ParseHelper.isTrue(matchText, source.getContext())) {
            new XmlSqlInfoBuilder(source).buildNormalSql(
                    XmlNodeHelper.getAndCheckNodeText(node, XpathConst.ATTR_FIELD),
                            XmlNodeHelper.getAndCheckNodeText(node, XpathConst.ATTR_VALUE), source.getSymbol());
        }
        source.resetPrefix();
    }

}
