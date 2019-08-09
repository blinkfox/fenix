package com.blinkfox.fenix.config;

import com.blinkfox.fenix.config.entity.NormalConfig;

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

}
