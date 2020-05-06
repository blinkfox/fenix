package com.blinkfox.fenix.core.concrete;

import com.blinkfox.fenix.bean.BuildSource;
import com.blinkfox.fenix.consts.XpathConst;
import com.blinkfox.fenix.core.FenixHandler;
import com.blinkfox.fenix.core.builder.XmlSqlInfoBuilder;
import com.blinkfox.fenix.helper.ParseHelper;
import com.blinkfox.fenix.helper.XmlNodeHelper;
import org.dom4j.Node;

/**
 * 用于生成常规动态 JPQL 或者 SQL 片段的 {@link FenixHandler} 接口的实现类.
 *
 * <p>常规 SQL 处理包括：等值、大于、小于、大于等于、小于等于、...</p>
 * <p>XML 标签示例如：</p>
 * <ul>
 *     <li>{@code <equal match="" field="" value="" />}</li>
 *     <li>{@code <andEqual match="" field="" value="" />}</li>
 *     <li>{@code <orEqual match="" field="" value="" />}</li>
 * </ul>
 * <p>注：获取到 match 字段的值，如果没有或者为 true，就通过 field, value 来生成此 SQL 片段.</p>
 *
 * @author blinkfox on 2019-08-06.
 * @see LikeHandler
 */
public class NormalHandler implements FenixHandler {

    /**
     * 根据 {@link BuildSource} 构建参数构建常规的 JPQL 或者 SQL 语句片段的信息.
     *
     * <p>如果 match 属性为空或者 match 属性中的表达式的值是 true，则生成此 JPQL 或者 SQL 的语句和参数.</p>
     *
     * @param source {@link BuildSource} 构建资源参数
     */
    @Override
    public void buildSqlInfo(BuildSource source) {
        Node node = source.getNode();
        if (ParseHelper.isMatch(XmlNodeHelper.getNodeAttrText(node, XpathConst.ATTR_MATCH), source.getContext())) {
            new XmlSqlInfoBuilder(source).buildNormalSql(
                    XmlNodeHelper.getAndCheckNodeText(node, XpathConst.ATTR_FIELD),
                    XmlNodeHelper.getAndCheckNodeText(node, XpathConst.ATTR_VALUE));
        }
        source.resetPrefix();
    }

}
