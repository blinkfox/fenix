package com.blinkfox.fenix.core.concrete;

import com.blinkfox.fenix.bean.BuildSource;
import com.blinkfox.fenix.consts.Const;
import com.blinkfox.fenix.consts.SymbolConst;
import com.blinkfox.fenix.consts.XpathConst;
import com.blinkfox.fenix.core.FenixHandler;
import com.blinkfox.fenix.exception.FenixException;
import com.blinkfox.fenix.helper.ParseHelper;
import com.blinkfox.fenix.helper.StringHelper;
import com.blinkfox.fenix.helper.XmlNodeHelper;
import java.util.Map;
import org.dom4j.Node;

/**
 * 'set' 标签是用于动态生成多个更新语句（'set = ?'）的 JPQL 或者 SQL 片段的 {@link FenixHandler} 接口的实现类.
 *
 * <p>XML 标签示例如：</p>
 * <ul>
 *     <li>{@code <set match1="" field="" value="" match2="" field2="" value2="" match3="" field3="" value3="" />}</li>
 * </ul>
 *
 * <p>注：</p>
 * <ul>
 *     <li>获取到的每个 match 字段的值，如果为空或者为 true，就生成对应字段的 Set 语句片段；</li>
 *     <li>程序会一直往后解析 match[k] 字段，只要该字段不存在，就不再生成后续的 SQL 语句片段；</li>
 *     <li>field 字段是死固定字符串，表示要更新的字段，value 是可以动态解析的字符串；</li>
 *     <li>field-value 必须一一对应，每写一对计数器就会向后累加，可以写 {@link Integer#MAX_VALUE} 个；</li>
 * </ul>
 *
 * @author blinkfox on 2019-08-07.
 * @see ChooseHandler
 */
public class SetHandler implements FenixHandler {

    /**
     * 根据 {@link BuildSource} 的相关参数来追加构建 'set' 标签的 JPQL 语句及参数信息.
     *
     * <p>循环所有的 field 的值，如果第 n 个 field 节点不存在（即为 null）的时候，就跳出循环不再执行后续的 set 拼接逻辑.</p>
     * <p>在循环过程中，如果解析到的第 x 个 match 不存在或者值为 true 时，才拼接这第 x 个 field-value 的值到更新语句中.</p>
     *
     * @param source 构建所需的 {@link BuildSource} 资源对象
     */
    @Override
    public void buildSqlInfo(BuildSource source) {
        Object context = source.getContext();
        String namespace = source.getNamespace();
        StringBuilder join = source.getSqlInfo().getJoin();
        Map<String, Object> params = source.getSqlInfo().getParams();
        Node node = source.getNode();

        // 每次循环加 1，如果是第一个，则，match-field-value 添加的后缀为空字符串.
        int i = 0;
        boolean isSet = false;
        while (true) {
            i++;
            String x = i == 1 ? "" : Integer.toString(i);

            // 获取第 i 个 field 属性，如果不存在就跳出循环.
            Node fieldNode = node.selectSingleNode(XpathConst.ATTR_FIELD + x);
            if (fieldNode == null) {
                break;
            }

            // 如果 match 匹配，就拼接 field-value 的 set 语句.
            if (ParseHelper.isMatch(XmlNodeHelper.getNodeAttrText(node, XpathConst.ATTR_MATCH + x), context)) {
                String fieldText = getAndCheckFieldText(fieldNode, namespace, i);

                if (!isSet) {
                    join.append(SymbolConst.SET);
                    isSet = true;
                } else {
                    join.append(Const.COMMA).append(Const.SPACE);
                }

                // 然后拼接 'field = :value' 的 SQL 和命名参数.
                Node valueNode = node.selectSingleNode(XpathConst.ATTR_VALUE + x);
                join.append(fieldText).append(SymbolConst.EQUAL).append(Const.COLON).append(fieldText);
                params.put(fieldText, valueNode == null ? null :
                        ParseHelper.parseExpressWithException(valueNode.getText(), context));
            }
        }
    }

    /**
     * 根据 field 的 Node 节点获取其文本值.
     *
     * @param fieldNode 字段节点
     * @return 字段文本值
     */
    private String getAndCheckFieldText(Node fieldNode, String namespace, int i) {
        String fieldText = fieldNode.getText();
        if (StringHelper.isBlank(fieldText)) {
            throw new FenixException("【Fenix 异常提示】namespace 为【" + namespace + "】的 XML 中，"
                    + "<set /> 标签中第【" + i + "】个 field 属性内容是空的，请检查！");
        }
        return fieldText;
    }

}
