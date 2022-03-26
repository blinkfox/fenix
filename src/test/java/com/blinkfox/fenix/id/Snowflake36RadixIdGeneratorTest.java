package com.blinkfox.fenix.id;

import org.junit.Assert;
import org.junit.Test;

/**
 * {@link Snowflake36RadixIdGenerator} 的单元测试类.
 *
 * @author blinkfox on 2022-03-26.
 * @since v2.7.0
 */
public class Snowflake36RadixIdGeneratorTest {

    @Test
    public void generate() {
        Assert.assertNotNull(new Snowflake36RadixIdGenerator().generate(null, null));
    }

}
