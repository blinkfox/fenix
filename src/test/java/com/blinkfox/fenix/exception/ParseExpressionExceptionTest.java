package com.blinkfox.fenix.exception;

import org.junit.Test;

/**
 * ParseExpressionException Test.
 *
 * @author blinkfox on 2019-09-01.
 */
public class ParseExpressionExceptionTest {

    /**
     * 测试异常情况.
     */
    @Test(expected = ParseExpressionException.class)
    public void testException() {
        throw new ParseExpressionException("这是单元测试抛出的 ParseExpressionException 异常，请忽略！", new RuntimeException());
    }

}
