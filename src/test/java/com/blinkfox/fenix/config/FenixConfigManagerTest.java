package com.blinkfox.fenix.config;

import com.blinkfox.fenix.exception.FenixException;

import org.junit.Test;

/**
 * FenixConfigManagerTest.
 *
 * @author blinkfox on 2019-09-01.
 */
public class FenixConfigManagerTest {

    /**
     * 测试 FenixConfig 为空的情况.
     */
    @Test(expected = FenixException.class)
    public void initLoadWithException() {
        FenixConfigManager.getInstance().initLoad(null);
    }

}
