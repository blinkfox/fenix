package com.blinkfox.fenix.config.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 一些通常的配置信息的单例类.
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
     * 是否打印本类库的 Banner 信息.
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
     * 获取唯一的具有默认属性的 {@link NormalConfig} 实例.
     *
     * <p>默认配置为: debug 模式为 false, 加载配置信息完毕后打印 Banner，打印 SQL 信息.</p>
     * @return NormalConfig 唯一实例.
     */
    public static NormalConfig getInstance() {
        return normalConfig;
    }

}