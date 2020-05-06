package com.blinkfox.fenix.handler;

import com.blinkfox.fenix.bean.BuildSource;
import com.blinkfox.fenix.bean.SqlInfo;
import com.blinkfox.fenix.config.annotation.Tagger;
import com.blinkfox.fenix.core.FenixHandler;
import com.blinkfox.fenix.helper.ParseHelper;
import com.blinkfox.fenix.helper.XmlNodeHelper;
import org.dom4j.Node;

/**
 * 用于测试自定义标签处理器及 {@link com.blinkfox.fenix.config.annotation.Tagger} 注解的使用.
 *
 * <p>标签示例：'<hello value="" />'</p>
 *
 * @author blinkfox on 2019-08-10.
 */
@Tagger(value = "hello", symbol = " = ")
@Tagger(value = "andHello", prefix = "AND ", symbol = " = ")
@Tagger(value = "orHello", prefix = "OR ", symbol = " = ")
public class HelloTagHandler implements FenixHandler {

    /**
     * 根据 {@link BuildSource} 的相关参数来追加构建出 'hello' XML 标签的 JPQL 语句及参数信息.
     *
     * <p>拼接的 SQL 片段如：'Hello'</p>
     *
     * @param source 构建所需的 {@link BuildSource} 资源对象
     */
    @Override
    public void buildSqlInfo(BuildSource source) {
        // 获取 XML 标签中的 value 的显示文本值和解析后实际的值.
        Node node = source.getNode();
        String valueText = XmlNodeHelper.getNodeAttrText(node, "attribute::value");
        String value = (String) ParseHelper.parseExpressWithException(valueText, source.getContext());

        // 将获取到的 value 拼接到 SQL 和参数的 Map 中.
        // 如果 prefix 为空，symbol 为 ' = '，value 值为 'World'，
        // 则 SQL 片段将为 'Hello = :World'，参数为 {helloName: 'World'}.
        SqlInfo sqlInfo = source.getSqlInfo();
        sqlInfo.getJoin()
                .append(source.getPrefix())
                .append("Hello")
                .append(source.getSymbol() == null ? " " : source.getSymbol())
                .append(":")
                .append(valueText);
        sqlInfo.getParams().put(valueText, value);
    }

}
