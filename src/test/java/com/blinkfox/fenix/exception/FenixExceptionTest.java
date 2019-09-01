package com.blinkfox.fenix.exception;

import org.junit.Test;

/**
 * FenixException Test.
 *
 * @author blinkfox on 2019-09-01.
 */
public class FenixExceptionTest {

    /**
     * 测试异常情况.
     */
    @Test(expected = FenixException.class)
    public void testException() {
        throw new FenixException("这是单元测试抛出的 FenixException 异常，请忽略！");
    }

    /**
     * 测试异常情况.
     */
    @Test(expected = FenixException.class)
    public void testException2() {
        throw new FenixException("这是单元测试抛出的 FenixException 异常，请忽略！", new RuntimeException());
    }

}
