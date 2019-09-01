package com.blinkfox.fenix.exception;

import org.junit.Test;

/**
 * ConfigNotFoundException Test.
 *
 * @author blinkfox on 2019-09-01.
 */
public class ConfigNotFoundExceptionTest {

    /**
     * 测试异常情况.
     */
    @Test(expected = ConfigNotFoundException.class)
    public void testException() {
        throw new ConfigNotFoundException("这是单元测试抛出的异常，请忽略！");
    }

}
