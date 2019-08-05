package com.blinkfox.fenix.core;

import com.blinkfox.fenix.bean.BuildSource;
import com.blinkfox.fenix.bean.SqlInfo;
import com.blinkfox.fenix.config.FenixDefaultConfig;
import com.blinkfox.fenix.config.entity.TagHandler;
import com.blinkfox.fenix.exception.NodeNotFoundException;

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
public final class ConditContext {

    /**
     * 根据标签名称和对应的构建参数构造出对应标签的sql和参数.
     * @param source 构建所需的资源对象
     * @param tag 标签名称
     * @return 返回SqlInfo对象
     */
    public static SqlInfo buildSqlInfo(BuildSource source, String tag) {
        // 获取所有配置的标签和标签处理器的全局map对象，并得到对应标签的标签处理器
        // 如果符合就执行该标签中对应handler对象的方法
        Map<String, TagHandler> tagHandlerMap = FenixDefaultConfig.getTagHandlerMap();
        if (!tagHandlerMap.containsKey(tag)) {
            throw new NodeNotFoundException("【Fenix 异常】未找到该【<" + tag + ">】标签对应的处理器.");
        }

        TagHandler handler = tagHandlerMap.get(tag);
        source.setPrefix(handler.getPrefix()).setSymbol(handler.getSymbol());
        return doBuildSqlInfo(source, handler);
    }

    /**
     * 执行构建SQL片段和参数的方法.
     *
     * @param source 构建所需的资源对象
     * @param handler 标签处理器实体
     * @return 返回SqlInfo对象
     */
    private static SqlInfo doBuildSqlInfo(BuildSource source, TagHandler handler) {
        try {
            // 使用反射获取该Handler对应的实例，并执行方法.
            return handler.getHandlerCls().newInstance().buildSqlInfo(source);
        } catch (InstantiationException e) {
            log.error("【Fenix 异常】实例化【" + handler.getHandlerCls().getName() + "】类出错!", e);
        } catch (IllegalAccessException e) {
            log.error("【Fenix 异常】访问 【" + handler.getHandlerCls().getName() + "】类出错!", e);
        }
        return source.getSqlInfo();
    }

}