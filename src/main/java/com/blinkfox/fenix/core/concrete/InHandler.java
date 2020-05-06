package com.blinkfox.fenix.core.concrete;

import com.blinkfox.fenix.bean.BuildSource;
import com.blinkfox.fenix.consts.XpathConst;
import com.blinkfox.fenix.core.FenixHandler;
import com.blinkfox.fenix.core.builder.XmlSqlInfoBuilder;
import com.blinkfox.fenix.helper.ParseHelper;
import com.blinkfox.fenix.helper.XmlNodeHelper;
import org.dom4j.Node;

/**
 * 用于生成 'In' 范围查询的动态 JPQL 或者 SQL 片段的 {@link FenixHandler} 接口的实现类.
 *
 * <p>XML 标签示例如：</p>
 * <ul>
 *     <li>'&lt;in match="" field="" value="" /&gt;'</li>
 *     <li>'&lt;andIn match="" field="" value="" /&gt;'</li>
 *     <li>'&lt;orIn match="" field="" value="" /&gt;'</li>
 * </ul>
 * <p>注：</p>
 * <ul>
 *     <li>获取到 match 字段的值，如果为空或者为 true，就生成此 SQL 片段；</li>
 *     <li>value 的值可以是数组，也可以是 Collection 集合，还可以是单个的值；</li>
 *     <li>如果 value 的值是单个的值，本处理器会将该单个的值封装成数组；</li>
 * </ul>
 *
 * @author blinkfox on 2019-08-07.
 * @see NormalHandler
 * @see LikeHandler
 * @see BetweenHandler
 */
public class InHandler implements FenixHandler {

    /**
     * 根据 {@link BuildSource} 参数构建"IN 范围查询"的 JPQL 或者 SQL 语句片段的信息.
     *
     * <p>如果 match 属性为空或者 match 属性中的表达式的值是 true，则生成此 JPQL 或者 SQL 的语句和参数.</p>
     *
     * @param source {@link BuildSource} 构建资源参数
     */
    @Override
    public void buildSqlInfo(BuildSource source) {
        Node node = source.getNode();
        if (ParseHelper.isMatch(XmlNodeHelper.getNodeAttrText(node, XpathConst.ATTR_MATCH), source.getContext())) {
            new XmlSqlInfoBuilder(source).buildInSql(XmlNodeHelper.getAndCheckNodeText(node, XpathConst.ATTR_FIELD),
                    XmlNodeHelper.getAndCheckNodeText(node, XpathConst.ATTR_VALUE));
        }
    }

}
