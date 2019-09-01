package com.blinkfox.fenix.exception;

import org.junit.Test;

/**
 * XmlParseException Test.
 *
 * @author blinkfox on 2019-09-01.
 */
public class XmlParseExceptionTest {

    /**
     * 测试异常情况.
     */
    @Test(expected = XmlParseException.class)
    public void testException() {
        throw new XmlParseException("这是单元测试抛出的 XmlParseException 异常，请忽略！", new RuntimeException());
    }

}
