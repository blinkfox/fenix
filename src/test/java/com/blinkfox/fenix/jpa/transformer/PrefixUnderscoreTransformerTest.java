package com.blinkfox.fenix.jpa.transformer;

import java.util.Arrays;
import java.util.Set;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * {@link PrefixUnderscoreTransformer} 的单元测试类.
 *
 * @author blinkfox on 2020-12-07.
 * @since v2.4.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PrefixUnderscoreTransformerTest {

    @Test
    public void test01Prefix() {
        PrefixUnderscoreTransformer.getPrefixSet().clear();
        Assert.assertTrue(PrefixUnderscoreTransformer.getPrefixSet().isEmpty());

        Set<String> prefixSet = PrefixUnderscoreTransformer.getPrefixSet();
        prefixSet.addAll(Arrays.asList("c_", "n_", "d_", "dt_"));
        Assert.assertTrue(PrefixUnderscoreTransformer.getPrefixSet().contains("c_"));
    }

    @Test
    public void test02LowerCamelCase() {
        PrefixUnderscoreTransformer transformer = new PrefixUnderscoreTransformer();
        Assert.assertEquals("abcDef", transformer.toLowerCamelCase("c_abc_def"));
        Assert.assertEquals("abcDef", transformer.toLowerCamelCase("dt_abC_Def"));
        Assert.assertEquals("aBcd", transformer.toLowerCamelCase("a_bcd"));
        Assert.assertEquals("def", transformer.toLowerCamelCase("n_def"));
    }

}