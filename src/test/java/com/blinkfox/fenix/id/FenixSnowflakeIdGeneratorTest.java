package com.blinkfox.fenix.id;

import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * {@link FenixSnowflakeIdGenerator} 的单元测试类.
 *
 * @author blinkfox on 2025-07-22.
 * @since 3.1.0
 */
public class FenixSnowflakeIdGeneratorTest {

    @Test
    public void generate() {
        Object id = new FenixSnowflakeIdGenerator().generate(null, null);
        Assert.assertNotNull(id);
        Assert.assertTrue(id instanceof Long);
    }

    @Test
    public void generateWithDecimalType() {
        Map<String, Object> decimalAttrs = new HashMap<>();
        decimalAttrs.put("value", SnowflakeIdType.DECIMAL);
        SnowflakeId snowflakeId = AnnotationUtils.synthesizeAnnotation(decimalAttrs, SnowflakeId.class, null);
        Object id = new FenixSnowflakeIdGenerator(snowflakeId).generate(null, null);
        Assert.assertNotNull(id);
        Assert.assertTrue(id instanceof Long);
    }

    @Test
    public void generateWithRadix36Type() {
        Map<String, Object> radix36Attrs = new HashMap<>();
        radix36Attrs.put("value", SnowflakeIdType.RADIX_36);
        SnowflakeId snowflakeRadix36Id = AnnotationUtils.synthesizeAnnotation(radix36Attrs, SnowflakeId.class, null);
        Object id36 = new FenixSnowflakeIdGenerator(snowflakeRadix36Id).generate(null, null);
        Assert.assertNotNull(id36);
        Assert.assertTrue(id36 instanceof String);
    }

    @Test
    public void generateWithRadix62Type() {
        Map<String, Object> radix62Attrs = new HashMap<>();
        radix62Attrs.put("value", SnowflakeIdType.RADIX_62);
        SnowflakeId snowflakeRadix62Id = AnnotationUtils.synthesizeAnnotation(radix62Attrs, SnowflakeId.class, null);
        Object id62 = new FenixSnowflakeIdGenerator(snowflakeRadix62Id).generate(null, null);
        Assert.assertNotNull(id62);
        Assert.assertTrue(id62 instanceof String);
    }
}