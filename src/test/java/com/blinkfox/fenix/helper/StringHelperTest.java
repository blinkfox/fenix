package com.blinkfox.fenix.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Test;

/**
 * StringHelper Test.
 *
 * @author blinkfox on 2019-08-04.
 */
public class StringHelperTest {

    /**
     * 判断字符串是否为空的单元测试类.
     */
    @Test
    public void testIsBlank() {
        assertTrue(StringHelper.isBlank(null));
        assertTrue(StringHelper.isBlank(""));
        assertTrue(StringHelper.isBlank(" "));
        assertTrue(StringHelper.isBlank("  \n "));
        assertFalse(StringHelper.isBlank(" a "));
    }

    /**
     * 判断字符串是否不为空的单元测试类.
     */
    @Test
    public void testIsNotBlank() {
        assertEquals(false, StringHelper.isNotBlank(null));
        assertEquals(false, StringHelper.isNotBlank(""));
        assertEquals(false, StringHelper.isNotBlank(" "));
        assertEquals(true, StringHelper.isNotBlank(" a "));
    }

    /**
     * 测试是否是XML文件.
     */
    @Test
    public void testIsXmlFile() {
        Assert.assertFalse(StringHelper.isXmlFile(null));
        Assert.assertFalse(StringHelper.isXmlFile("ab/.xmls"));
        Assert.assertTrue(StringHelper.isXmlFile("zealot/ab.xml"));
    }

    /**
     * 测试是否是XML文件.
     */
    @Test
    public void testIsJavaFile() {
        Assert.assertFalse(StringHelper.isJavaFile(null));
        Assert.assertFalse(StringHelper.isJavaFile("ab/.jav"));
        Assert.assertTrue(StringHelper.isJavaFile("com.blinkfox.zealot.Hello.java"));
    }

    /**
     * 测试是否是XML文件.
     */
    @Test
    public void testIsClassFile() {
        Assert.assertFalse(StringHelper.isClassFile(null));
        Assert.assertFalse(StringHelper.isClassFile("com.blinkfox.Test.classes"));
        Assert.assertTrue(StringHelper.isClassFile("com.blinkfox.zealot.Hello.class"));
    }

}