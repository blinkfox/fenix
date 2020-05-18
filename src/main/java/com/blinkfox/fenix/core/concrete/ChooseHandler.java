package com.blinkfox.fenix.core.concrete;

import com.blinkfox.fenix.bean.BuildSource;
import com.blinkfox.fenix.consts.XpathConst;
import com.blinkfox.fenix.core.FenixHandler;
import com.blinkfox.fenix.exception.FenixException;
import com.blinkfox.fenix.helper.ParseHelper;
import com.blinkfox.fenix.helper.StringHelper;
import com.blinkfox.fenix.helper.XmlNodeHelper;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Node;

/**
 * 基于多条件分支选择来动态生成 JPQL 或者 SQL 片段的 {@link FenixHandler} 接口的实现类.
 *
 * <p>XML 标签示例如：</p>
 * <ul>
 *     <li>{@code <choose when="" then="" when2="" then2="" else="" />}</li>
 * </ul>
 * <p>注：</p>
 * <ul>
 *     <li>when 表示匹配条件，结果为 true 时，执行对应的 then 块；</li>
 *     <li>then 表示需要执行的逻辑，和 when 一一对应，内容是字符串或字符串模版；</li>
 *     <li>else 表示所有 when 条件都不满足时才执行的逻辑，内容是字符串或者字符串模版；</li>
 *     <li>when-then 必须一一对应，每写一对计数器就会向后累加，可以写 {@link Integer#MAX_VALUE} 个；</li>
 * </ul>
 *
 * @author blinkfox on 2019-08-07.
 * @see NormalHandler
 * @see LikeHandler
 * @see InHandler
 * @since v1.0.0
 */
@Slf4j
public class ChooseHandler implements FenixHandler {

    /**
     * 根据 {@link BuildSource} 参数构建 'choose' 标签中的 JPQL 或者 SQL 语句片段的信息.
     *
     * @param source {@link BuildSource} 构建资源参数
     */
    @Override
    public void buildSqlInfo(BuildSource source) {
        Object context = source.getContext();
        StringBuilder join = source.getSqlInfo().getJoin();
        Node node = source.getNode();

        // 循环判断所有 when 的值，直到能找到第 n 个 when 的时候，如果都为 false，则跳出循环走 else 的逻辑.
        // 在循环过程中，如果判断到第 x 个 when 解析后的值为 true 时，则解析拼接这第 x 个 then 的值到 Sql 中，并直接返回.
        int i = 0;
        while (true) {
            // 每次循环加1，如果是第一个，则，when-then 添加的后缀为空字符串.
            i++;
            String x = i == 1 ? "" : Integer.toString(i);

            // 获取第 i 个 when 属性的文本值，如果其文本内容为空则不再拼接，进入 else 的分支条件来拼接Sql片段信息.
            String whenText = XmlNodeHelper.getNodeAttrText(node, XpathConst.ATTR_WHEN + x);
            if (StringHelper.isBlank(whenText)) {
                log.debug("【Fenix 提示】<choose /> 标签中第【" + i + "】个 when 属性不存在或者内容为空，将直接进入 else 的分支条件.");
                break;
            }

            // 如果 when 属性的解析值为 true，则拼接其对应的 then 块的 SQL 片段，then 块的值为解析其字符串模板的值，拼接到 SQL 中然后直接返回.
            // 否则，又进入下一次循环判断 when-then 的值.
            if (ParseHelper.isTrue(whenText, context)) {
                String thenText = XmlNodeHelper.getNodeAttrText(node, XpathConst.ATTR_THEN + x);
                if (StringHelper.isBlank(thenText)) {
                    throw new FenixException("【Fenix 异常提示】namespace 为【" + source.getNamespace() + "】的 XML 中，"
                            + "<choose /> 标签中第【" + i + "】个 when 属性为 true，但是 then 属性却是空的或未填写内容，请检查！");
                }

                join.append(ParseHelper.parseTemplate(thenText, context));
                return;
            }
        }

        // 如果没进入前面任何一个 when-then 的分支块，则判断是否存在 else 属性的分支，如果 else 不为空，则拼接 else 中的内容.
        String elseText = XmlNodeHelper.getNodeAttrText(node, XpathConst.ATTR_ELSE);
        if (StringHelper.isNotBlank(elseText)) {
            join.append(ParseHelper.parseTemplate(elseText, context));
        }
    }

}
