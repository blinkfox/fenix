package com.blinkfox.fenix.handler;

import com.blinkfox.fenix.bean.BuildSource;
import com.blinkfox.fenix.config.annotation.Tagger;
import com.blinkfox.fenix.consts.Const;
import com.blinkfox.fenix.consts.SymbolConst;
import com.blinkfox.fenix.consts.XpathConst;
import com.blinkfox.fenix.core.FenixHandler;
import com.blinkfox.fenix.helper.ParseHelper;
import com.blinkfox.fenix.helper.StringHelper;
import com.blinkfox.fenix.helper.XmlNodeHelper;
import java.util.Map;
import org.dom4j.Node;

/**
 * 自定义的 ID 和 Email 条件查询的SQL处理器.
 *
 * @author blinkfox on 2019-08-10.
 */
@Tagger(value = "userIdEmail")
public class UserIdEmailHandler implements FenixHandler {

    /**
     * 根据 {@link BuildSource} 的相关参数来追加构建出对应 XML 标签的 JPQL 语句及参数信息.
     *
     * @param source 构建所需的 {@link BuildSource} 资源对象
     */
    @Override
    public void buildSqlInfo(BuildSource source) {
        /* 获取拼接的参数和 Fenix 节点 */
        Node node = source.getNode();

        /* 获取 match 属性值,如果匹配中 字符值没有，则认为是必然生成项. */
        if (ParseHelper.isMatch(XmlNodeHelper.getNodeAttrText(node, XpathConst.ATTR_MATCH), source.getContext())) {
            StringBuilder join = source.getSqlInfo().getJoin();
            Map<String, Object> params = source.getSqlInfo().getParams();

            // 获取配置的属性值,getAndCheckNodeText()方法会检测属性值是否为空，如果为空，会抛出运行时异常
            String idFieldText = XmlNodeHelper.getAndCheckNodeText(node, "attribute::idField");
            String emailFieldText = XmlNodeHelper.getAndCheckNodeText(node, "attribute::emailField");
            // getAndCheckNodeText()方法仅仅只获取属性的值，即使未配置或书写值，也会返回空字符串
            String idValueText = XmlNodeHelper.getNodeAttrText(node, "attribute::idValue");
            String emailValueText = XmlNodeHelper.getNodeAttrText(node, "attribute::emailValue");

            // 如果 userId 不为空，则根据 id 来做等值查询.
            String id = (String) ParseHelper.parseExpressWithException(idValueText, source.getContext());
            if (id != null) {
                // prefix 是前缀，如 " and ", " or " 之类，没有则默认为空字符串""
                String idNamed = StringHelper.fixDot(idValueText);
                join.append(source.getPrefix()).append(idFieldText).append(SymbolConst.EQUAL)
                        .append(Const.COLON).append(idNamed);
                params.put(idNamed, id);
                return;
            }

            // 获取 userEmail 的值,如果 userEmail 不为空，则根据 email 来做模糊查询.
            String email = (String) ParseHelper.parseExpressWithException(emailValueText, source.getContext());
            if (StringHelper.isNotBlank(email)) {
                String emailNamed = StringHelper.fixDot(emailValueText);
                join.append(source.getPrefix()).append(emailFieldText).append(SymbolConst.LIKE)
                        .append(Const.COLON).append(emailNamed);
                params.put(emailNamed, "%" + email + "%");
            }
        }
    }

}
