package com.blinkfox.fenix.id;

import org.junit.Assert;
import org.junit.Test;

/**
 * NanoIdGenerator 的单元测试类.
 *
 * @author blinkfox on 2020-03-27.
 * @since v2.7.0
 */
public class NanoIdGeneratorTest {

    @Test
    public void generate() {
        Assert.assertNotNull(new NanoIdGenerator().generate(null, null));
    }

}