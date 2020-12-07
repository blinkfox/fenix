package com.blinkfox.fenix.id;

import org.junit.Assert;
import org.junit.Test;

/**
 * {@link SnowflakeIdGenerator} 的单元测试类.
 *
 * @author blinkfox on 2020-12-07.
 * @since v2.4.0
 */
public class SnowflakeIdGeneratorTest {

    @Test
    public void generate() {
        Assert.assertNotNull(new SnowflakeIdGenerator().generate(null, null));
    }

}
