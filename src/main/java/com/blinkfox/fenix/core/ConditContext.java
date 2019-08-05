package com.blinkfox.fenix.core;

import com.blinkfox.fenix.bean.BuildSource;
import com.blinkfox.fenix.config.FenixDefaultConfig;
import com.blinkfox.fenix.config.entity.TagHandler;
import com.blinkfox.fenix.exception.FenixException;
import com.blinkfox.fenix.exception.NodeNotFoundException;
import com.blinkfox.fenix.helper.StringHelper;

import java.util.Map;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 构建动态 JPQL 或者 SQL 片段的上下文协调类.
 *
 * @author blinkfox on 2019-08-05.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class ConditContext {

    /**
     * 根据标签名称和对应的构建参数构造出对应标签的 JPQL (或者 SQL)与参数.
     *
     * @param source 构建所需的资源对象
     * @param tag 标签名称
     */
    static void buildSqlInfo(BuildSource source, String tag) {
        // 获取所有配置的标签和标签处理器的全局map对象，并得到对应标签的标签处理器
        // 如果符合就执行该标签中对应handler对象的方法
        Map<String, TagHandler> tagHandlerMap = FenixDefaultConfig.getTagHandlerMap();
        TagHandler handler = tagHandlerMap.get(tag);
        if (handler == null) {
            throw new NodeNotFoundException(StringHelper.format("【Fenix 异常】未找到该【<{}>】标签对应的处理器.", tag));
        }

        source.setPrefix(handler.getPrefix()).setSymbol(handler.getSymbol());
        try {
            // 使用反射获取该Handler对应的实例，并执行方法.
            handler.getHandlerCls().newInstance().buildSqlInfo(source);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new FenixException(StringHelper
                    .format("【Fenix 异常】访问或实例化【{}】class 出错!", handler.getHandlerCls().getName()), e);
        }
    }

}
