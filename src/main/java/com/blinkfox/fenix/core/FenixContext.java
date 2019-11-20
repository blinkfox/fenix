package com.blinkfox.fenix.core;

import com.blinkfox.fenix.bean.BuildSource;
import com.blinkfox.fenix.bean.SqlInfo;
import com.blinkfox.fenix.config.FenixConfig;
import com.blinkfox.fenix.config.entity.TagHandler;
import com.blinkfox.fenix.consts.Const;
import com.blinkfox.fenix.consts.SymbolConst;
import com.blinkfox.fenix.exception.FenixException;
import com.blinkfox.fenix.exception.NodeNotFoundException;
import com.blinkfox.fenix.helper.StringHelper;

import java.lang.reflect.InvocationTargetException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.springframework.util.StringUtils;

/**
 * 追加构建动态 JPQL 或者 SQL 语句及参数的上下文协调类.
 *
 * @author blinkfox on 2019-08-05.
 * @see FenixHandlerFactory
 * @see FenixHandler
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FenixContext {

    /**
     * 构建纯文本的 {@link com.blinkfox.fenix.bean.SqlInfo} 信息.
     *
     * @param sqlInfo SQL 信息实例
     * @param plainText 纯文本字符串.
     * @since v2.1.0
     */
    public static void buildPlainTextSqlInfo(SqlInfo sqlInfo, String plainText) {
        // 如果是要追加 WHERE 关键字的情况，就是使用 <where> 标签的情况，就需要先追加 WHERE 关键字，
        // 然后判断去除掉后面是否紧跟了 AND 或者 OR 的关键字的情况.
        if (sqlInfo.isPrependWhere()) {
            String text = StringHelper.replaceBlank(plainText);
            if (StringHelper.isBlank(text)) {
                return;
            }

            // 前置添加完 WHERE 语句之后，必须将 prependWhere 设置为 false.
            sqlInfo.getJoin().append(SymbolConst.WHERE);
            sqlInfo.setPrependWhere(false);
            if (StringUtils.startsWithIgnoreCase(text, "AND ")) {
                text = text.substring(4);
            } else if (StringUtils.startsWithIgnoreCase(text, "OR ")) {
                text = text.substring(3);
            }
            sqlInfo.getJoin().append(text).append(Const.SPACE);
            return;
        }

        // 如果是不前置添加 WHERE 关键字的情况，就直接追加 SQL 纯文本即可.
        sqlInfo.getJoin().append(plainText);
    }

    /**
     * 根据标签名称和对应的构建参数构造出对应标签的 JPQL (或者 SQL)与参数.
     * <p>获取所有配置的标签和标签处理器的全局 map 对象，并得到对应标签的标签处理器.
     *      如果有对应的 handler 处理器，就执行该标签中对应 handler 对象的方法.</p>
     *
     * @param source 构建所需的资源对象
     * @param tag 标签名称
     */
    static void buildSqlInfo(BuildSource source, String tag) {
        TagHandler handler = FenixConfig.getTagHandlerMap().get(tag);
        if (handler == null) {
            throw new NodeNotFoundException(StringHelper.format("【Fenix 异常】未找到该【<{}>】标签对应的处理器.", tag));
        }

        // 首先获取 FenixHandlerFactory 对象，并判断其是否为空，如果不为空就直接调用 newInstance 来创建 FenixHandler 实例.
        source.setPrefix(handler.getPrefix()).setSymbol(handler.getSymbol());
        FenixHandlerFactory handlerFactory = handler.getHandlerFactory();
        if (handlerFactory != null) {
            handlerFactory.newInstance().buildSqlInfo(source);
            return;
        }

        try {
            // 否则就调用 getHandlerCls() 方法来使用反射获取该 Handler 对应的实例，并执行方法.
            handler.getHandlerCls().getDeclaredConstructor().newInstance().buildSqlInfo(source);
        } catch (InstantiationException | IllegalAccessException
                | NoSuchMethodException | InvocationTargetException e) {
            throw new FenixException(StringHelper.format("【Fenix 异常】访问或实例化【{}】class 出错!",
                    handler.getHandlerCls().getName()), e);
        }
    }

}
