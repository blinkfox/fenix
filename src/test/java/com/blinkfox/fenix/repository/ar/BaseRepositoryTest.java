package com.blinkfox.fenix.repository.ar;

import com.blinkfox.fenix.ar.CrudModel;
import com.blinkfox.fenix.config.FenixConfigManager;
import com.blinkfox.fenix.exception.FenixException;
import javax.annotation.Resource;
import lombok.Setter;
import org.springframework.context.ApplicationContext;

/**
 * 一个基础的公用的 Repository 的测试类.
 *
 * @author blinkfox on 2022-03-28.
 * @since 2.7.0
 */
public class BaseRepositoryTest {

    /**
     * 是否加载过的标识.
     */
    @Setter
    private static Boolean isLoad = false;

    @Resource
    private ApplicationContext applicationContext;

    /**
     * 初始化加载 Fenix 的配置.
     */
    protected void initLoad() {
        if (!isLoad) {
            // 初始化加载 Fenix 配置.
            FenixConfigManager.getInstance().initLoad();

            // 设置应用上下文.
            if (applicationContext == null) {
                throw new FenixException("未成功注入 Spring 应用上下文 applicationContext 的对象实例.");
            }
            CrudModel.setApplicationContext(applicationContext);

            // 初始化基础数据.
            this.initData();
            isLoad = true;
        }
    }

    /**
     * 初始化一些数据.
     */
    protected void initData() {
        // do nothing.
    }

}
