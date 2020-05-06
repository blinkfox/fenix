package com.blinkfox.fenix.core.concrete;

import com.blinkfox.fenix.bean.BuildSource;
import com.blinkfox.fenix.consts.XpathConst;
import com.blinkfox.fenix.core.FenixHandler;
import com.blinkfox.fenix.core.builder.XmlSqlInfoBuilder;
import com.blinkfox.fenix.helper.ParseHelper;
import com.blinkfox.fenix.helper.XmlNodeHelper;
import org.dom4j.Node;

/**
 * 用于生成 'BETWEEN :start AND :end'、'大于等于'、'小于等于' 区间查询
 * 的动态 JPQL 或者 SQL 片段的 {@link FenixHandler} 接口的实现类.
 *
 * <p>XML 标签示例如：</p>
 * <ul>
 *     <li>{@code <between match="" field="" startName="" start="" endName="" end="" />}</li>
 *     <li>{@code <andBetween match="" field="" startName="" start="" endName="" end="" />}</li>
 *     <li>{@code <orBetween match="" field="" startName="" start="" endName="" end="" />}</li>
 * </ul>
 *
 * <p>注：</p>
 * <ul>
 *     <li>获取到 match 字段的值，如果为空或者为 true，就生成此 SQL 片段；</li>
 *     <li>field 必填，tart 和 end 的值"不能"全为 null，match、startName、endName 的值均为非必填；</li>
 *     <li>start 和 end 的值"均不为 null"时，会生成 {@code 'BETWEEN :start AND :end'} 的 JPQL 语句；</li>
 *     <li>start 不为 null 和 end 为 null 时，会退化生成 {@code '>= :start'} 的 JPQL 语句；</li>
 *     <li>start 为 null 和 end 不为 null 时，会退化生成 {@code '<= :end'} 的 JPQL 语句；</li>
 * </ul>
 *
 * @author blinkfox on 2019-08-07.
 * @see NormalHandler
 * @see LikeHandler
 * @see InHandler
 */
public class BetweenHandler implements FenixHandler {

    /**
     * 根据 {@link BuildSource} 参数构建"区间查询"的 JPQL 或者 SQL 语句片段的信息.
     *
     * <p>如果 match 属性为空或者 match 属性中的表达式的值是 true，则生成此 JPQL 或者 SQL 的语句和参数.</p>
     *
     * @param source {@link BuildSource} 构建资源参数
     */
    @Override
    public void buildSqlInfo(BuildSource source) {
        Node node = source.getNode();
        if (ParseHelper.isMatch(XmlNodeHelper.getNodeAttrText(node, XpathConst.ATTR_MATCH), source.getContext())) {
            String[] valueTextArr = XmlNodeHelper.getRangeCheckNodeText(node);
            new XmlSqlInfoBuilder(source).buildBetweenSql(
                    XmlNodeHelper.getAndCheckNodeText(node, XpathConst.ATTR_FIELD),
                    XmlNodeHelper.getNodeAttrText(node, XpathConst.ATTR_START_NAME),
                    valueTextArr[0],
                    XmlNodeHelper.getNodeAttrText(node, XpathConst.ATTR_END_NAME),
                    valueTextArr[1]);
        }
    }

}
