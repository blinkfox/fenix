package com.blinkfox.fenix.id;

import org.junit.Assert;
import org.junit.Test;

/**
 * {@link Snowflake62RadixIdGenerator} 的单元测试类.
 *
 * @author blinkfox on 2020-12-07.
 * @since v2.4.0
 */
public class Snowflake62RadixIdGeneratorTest {

    @Test
    public void generate() {
        Assert.assertNotNull(new Snowflake62RadixIdGenerator().generate(null, null));
    }

}
