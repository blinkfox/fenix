package com.blinkfox.fenix.config.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 包含一些常规配置信息的配置单例类.
 *
 * @author blinkfox on 2019-08-04.
 */
@Getter
@Setter
@Accessors(chain = true)
public class NormalConfig {

    /**
     * 是否开启调试模式.
     */
    private boolean debug;

    /**
     * 是否打印本 Fenix 类库的 Banner 信息.
     */
    private boolean printBanner;

    /**
     * 是否打印调用执行时的 SqlInfo 信息.
     */
    private boolean printSqlInfo;

    /**
     * NormalConfig 的唯一实例.
     */
    private static final NormalConfig normalConfig = new NormalConfig();

    /**
     * 私有构造方法.
     */
    private NormalConfig() {
        this.setDebug(false).setPrintBanner(true).setPrintSqlInfo(true);
    }

    /**
     * 获取具有默认属性的 {@link NormalConfig} 唯一单实例.
     *
     * <p>默认配置为: debug 模式为 false, 加载配置信息完毕后打印 Banner，打印 SQL 信息.</p>
     * @return NormalConfig 的唯一实例.
     */
    public static NormalConfig getInstance() {
        return normalConfig;
    }

}
