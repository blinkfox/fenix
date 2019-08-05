package com.blinkfox.fenix.core.concrete;

import com.blinkfox.fenix.bean.BuildSource;
import com.blinkfox.fenix.bean.SqlInfo;
import com.blinkfox.fenix.consts.XpathConst;
import com.blinkfox.fenix.core.IConditHandler;
import com.blinkfox.fenix.core.builder.XmlSqlInfoBuilder;
import com.blinkfox.fenix.helper.ParseHelper;
import com.blinkfox.fenix.helper.StringHelper;
import com.blinkfox.fenix.helper.XmlNodeHelper;

import org.dom4j.Node;

/**
 * 用于生成常规动态 JPQL 或者 SQL 片段的 {@link IConditHandler} 接口的实现类.
 * <p>常规 SQL 处理包括：等值、大于、小于、大于等于、小于等于等.</p>
 * <p>XML 标签如：</p>
 * <ul>
 *     <li>'<equal match="" field="" value="" />'</li>
 *     <li>'<andEqual match="" field="" value="" />'</li>
 *     <li>'<orEqual match="" field="" value="" />'</li>
 * </ul>
 *
 * @author blinkfox on 2019-08-06.
 */
public class NormalHandler implements IConditHandler {

    /**
     * 构建常规 JPQL 或者 SQL 片段的动态 {@link SqlInfo} 信息.
     *
     * @param source 构建所需的资源对象
     * @return 返回 {@link SqlInfo} 对象
     */
    @Override
    public SqlInfo buildSqlInfo(BuildSource source) {
        // 获取拼接的参数.
        SqlInfo sqlInfo = source.getSqlInfo();
        Node node = source.getNode();

        // 判断必填的参数是否为空.
        String fieldText = XmlNodeHelper.getAndCheckNodeText(node, XpathConst.ATTR_FIELD);
        String valueText = XmlNodeHelper.getAndCheckNodeText(node, XpathConst.ATTR_VALUE);

        // 如果没有 match 字段，则认为是必然生成项.
        Node matchNode = node.selectSingleNode(XpathConst.ATTR_MATCH);
        String matchText = XmlNodeHelper.getNodeText(matchNode);
        if (StringHelper.isBlank(matchText)) {
            sqlInfo = XmlSqlInfoBuilder.newInstace(source).buildNormalSql(fieldText, valueText, source.getSymbol());
        } else {
            // 如果 match 表达式的值为 true，则生成该 JPQL 或者 SQL 片段.
            if (ParseHelper.isTrue(matchText, source.getParamMap())) {
                sqlInfo = XmlSqlInfoBuilder.newInstace(source).buildNormalSql(fieldText, valueText, source.getSymbol());
            }
        }

        source.resetPrefix();
        return sqlInfo;
    }

}