package com.blinkfox.fenix.config;

import com.blinkfox.fenix.config.entity.NormalConfig;
import com.blinkfox.fenix.handler.HelloTagHandler;

/**
 * 测试定义的 Fenix 配置类.
 *
 * @author blinkfox on 2019-08-06.
 */
public class MyFenixConfig extends FenixConfig {

    /**
     * 设置相关参数.
     *
     * @param normalConfig 常规配置实例
     */
    @Override
    public void configNormal(NormalConfig normalConfig) {
        normalConfig.setDebug(false).setPrintBanner(true).setPrintSqlInfo(true);
    }

    /**
     * 配置标签和其对应的处理类(默认了许多常用的标签，开发者可覆盖此方法来配置更多的自定义标签).
     */
    @Override
    public void configTagHandler() {
        // 子类可重写此方法，来增加新的 SQL 标签和该标签对应的处理器.
        add("hi", HelloTagHandler.class);
        add("andHi", " AND ", HelloTagHandler::new, " LIKE ");
    }

}
