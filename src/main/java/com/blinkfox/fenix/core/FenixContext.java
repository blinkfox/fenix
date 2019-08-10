package com.blinkfox.fenix.core;

import com.blinkfox.fenix.bean.BuildSource;
import com.blinkfox.fenix.config.FenixConfig;
import com.blinkfox.fenix.config.entity.TagHandler;
import com.blinkfox.fenix.exception.FenixException;
import com.blinkfox.fenix.exception.NodeNotFoundException;
import com.blinkfox.fenix.helper.StringHelper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 追加构建动态 JPQL 或者 SQL 语句及参数的上下文协调类.
 *
 * @author blinkfox on 2019-08-05.
 * @see FenixHandlerFactory
 * @see FenixHandler
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class FenixContext {

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
            handler.getHandlerCls().newInstance().buildSqlInfo(source);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new FenixException(StringHelper
                    .format("【Fenix 异常】访问或实例化【{}】class 出错!", handler.getHandlerCls().getName()), e);
        }
    }

}
