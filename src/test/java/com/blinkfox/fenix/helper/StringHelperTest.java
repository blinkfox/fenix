package com.blinkfox.fenix.helper;

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
    public void isBlank() {
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
    public void isNotBlank() {
        assertFalse(StringHelper.isNotBlank(null));
        assertFalse(StringHelper.isNotBlank(""));
        assertFalse(StringHelper.isNotBlank(" "));
        assertTrue(StringHelper.isNotBlank(" a "));
    }

    /**
     * 测试字符串格式化方法.
     */
    @Test
    public void format() {
        Assert.assertEquals("", StringHelper.format(null));
        Assert.assertEquals("aaa", StringHelper.format("aaa"));
        Assert.assertEquals("", StringHelper.format(null, ""));
        Assert.assertEquals("Hello 张三, I'm 李四.", StringHelper.format("Hello {}, I'm {}.", "张三", "李四"));
    }

    /**
     * 测试是否是 XML 文件.
     */
    @Test
    public void isXmlFile() {
        Assert.assertFalse(StringHelper.isXmlFile(null));
        Assert.assertFalse(StringHelper.isXmlFile("ab/.xmls"));
        Assert.assertTrue(StringHelper.isXmlFile("fenix/ab.xml"));
    }

    /**
     * 测试是否是 Java 文件.
     */
    @Test
    public void isJavaFile() {
        Assert.assertFalse(StringHelper.isJavaFile(null));
        Assert.assertFalse(StringHelper.isJavaFile("ab/.jav"));
        Assert.assertTrue(StringHelper.isJavaFile("com.blinkfox.fenix.Hello.java"));
    }

    /**
     * 测试是否是 clsss 文件.
     */
    @Test
    public void isClassFile() {
        Assert.assertFalse(StringHelper.isClassFile(null));
        Assert.assertFalse(StringHelper.isClassFile("com.blinkfox.Test.classes"));
        Assert.assertTrue(StringHelper.isClassFile("com.blinkfox.fenix.Hello.class"));
    }

}