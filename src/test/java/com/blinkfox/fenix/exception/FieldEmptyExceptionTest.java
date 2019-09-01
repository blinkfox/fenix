package com.blinkfox.fenix.exception;

import org.junit.Test;

/**
 * FieldEmptyException Test.
 *
 * @author blinkfox on 2019-09-01.
 */
public class FieldEmptyExceptionTest {

    /**
     * 测试异常情况.
     */
    @Test(expected = FieldEmptyException.class)
    public void testException() {
        throw new FieldEmptyException("这是单元测试抛出的 FieldEmptyException 异常，请忽略！");
    }

}
