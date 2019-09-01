package com.blinkfox.fenix.exception;

import org.junit.Test;

/**
 * NodeNotFoundException Test.
 *
 * @author blinkfox on 2019-09-01.
 */
public class NodeNotFoundExceptionTest {

    /**
     * 测试异常情况.
     */
    @Test(expected = NodeNotFoundException.class)
    public void testException() {
        throw new NodeNotFoundException("这是单元测试抛出的 NodeNotFoundException 异常，请忽略！");
    }

}
