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
     * 判断字符串是否不为空的单元测试类.
     */
    @Test
    public void isEmptyObject() {
        assertTrue(StringHelper.isEmptyObject(null));
        assertTrue(StringHelper.isEmptyObject(""));
        assertTrue(StringHelper.isEmptyObject(" "));
        assertFalse(StringHelper.isEmptyObject(0L));
    }

    /**
     * 测试字符串替换.
     */
    @Test
    public void replaceWhereAndOr() {
        Assert.assertEquals("aaa WHERE bbb WHERE ccc WHERE ddd",
                StringHelper.replaceWhereAndOr("aaa wheRe Or bbb WHERE AND ccc where OR ddd"));
        Assert.assertEquals("aaa WHERE bbb ccc WHERE ddd eee",
                StringHelper.replaceWhereAndOr("aaa where AND bbb ccc WHERE and ddd eee"));
        Assert.assertEquals("select * from table WHERE field1 = 'a' WHERE field2 = 'b'",
                StringHelper.replaceWhereAndOr("select * from table WHERE AND field1 = 'a' WHERE AND field2 = 'b'"));
        Assert.assertEquals("aaa   WHERE  bbb ccc  WHEREANDddd eee WHERE ",
                StringHelper.replaceWhereAndOr("aaa   WHERE and  bbb ccc  WHEREANDddd eee WHERE OR "));
        Assert.assertEquals("[aaa bbb] WHERE [def]",
                StringHelper.replaceWhereAndOr("[aaa bbb] WHERE [def] WHERE"));
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