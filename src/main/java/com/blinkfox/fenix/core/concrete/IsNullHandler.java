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
 * 用于生成 'IS NULL' 的动态 JPQL 或者 SQL 片段的 {@link FenixHandler} 接口的实现类.
 *
 * <p>XML 标签示例如：</p>
 * <ul>
 *     <li>'<isNull match="" field="" />'</li>
 *     <li>'<andIsNull match="" field="" />'</li>
 *     <li>'<orIsNull match="" field="" />'</li>
 * </ul>
 * <p>注：</p>
 * <ul>
 *     <li>获取到 match 字段的值，如果为空或者为 true，就生成此 SQL 片段；</li>
 * </ul>
 *
 * @author blinkfox on 2019-08-07.
 * @see NormalHandler
 * @see LikeHandler
 * @see InHandler
 */
public class IsNullHandler implements FenixHandler {

    /**
     * 根据 {@link BuildSource} 参数构建 "IS NULL" 的 JPQL 或者 SQL 语句片段的信息.
     *
     * <p>如果 match 属性为空或者 match 属性中的表达式的值是 true，则生成此 JPQL 或者 SQL 的语句和参数.</p>
     *
     * @param source  {@link BuildSource} 构建资源参数
     */
    @Override
    public void buildSqlInfo(BuildSource source) {
        Node node = source.getNode();
        String matchText = XmlNodeHelper.getNodeAttrText(node, XpathConst.ATTR_MATCH);
        if (StringHelper.isBlank(matchText) || ParseHelper.isTrue(matchText, source.getContext())) {
            new XmlSqlInfoBuilder(source)
                    .buildIsNullSql(XmlNodeHelper.getAndCheckNodeText(node, XpathConst.ATTR_FIELD));
        }
    }

}
