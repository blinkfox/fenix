package com.blinkfox.fenix.helper;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * ParseHelper Test.
 *
 * @author blinkfox on 2019-08-04.
 */
@Slf4j
public class ParseHelperTest {

    /**
     * 本测试类中全局使用的上下文参数 Map.
     */
    private static Map<String, Object> context;

    /**
     * 初始化方法.
     */
    @BeforeClass
    public static void init() {
        context = new HashMap<>(4);
        context.put("hello", "world");
    }

    /**
     * 测试解析表达式的方法.
     */
    @Test
    public void parseExpress() {
        Assert.assertEquals("world", ParseHelper.parseExpress("hello", context));
    }

    /**
     * 测试解析表达式会抛异常的方法.
     */
    @Test(expected = Exception.class)
    public void parseExpressWithException() {
        Assert.assertNull(ParseHelper.parseExpressWithException("abc", context));
    }

    /**
     * 测试解析表达式会抛异常的方法.
     */
    @Test(expected = Exception.class)
    public void parseTemplate() {
        Assert.assertEquals("say world.", ParseHelper.parseTemplate("say @{hello}.", context));
        Assert.assertNull(ParseHelper.parseTemplate("say @{abc}.", context));
    }

    /**
     * 测试计算模版的值.
     */
    @Test
    public void parseTemplate2() {
        // 构造查询的参数
        Map<String, Object> map = new HashMap<>(2);
        map.put("foo", "Hello");
        String result = ParseHelper.parseTemplate("@if{?foo != empty}@{foo} World!@end{}", map);
        log.info("testParseTemplate 结果:{}" + result);
        Assert.assertEquals("Hello World!", result);
    }

    /**
     * 测试解析表达式会抛异常的方法.
     */
    @Test
    public void isMatch() {
        Assert.assertTrue(ParseHelper.isMatch(null, context));
        Assert.assertTrue(ParseHelper.isMatch("", context));
        Assert.assertTrue(ParseHelper.isMatch("hello != null", context));
        Assert.assertFalse(ParseHelper.isMatch("?hello != 'world'", context));
    }

    /**
     * 测试计算表达式的值.
     */
    @Test
    public void parseWithMvel() {
        // 构造查询的参数
        Map<String, Object> map = new HashMap<>(4);
        map.put("foo", "Hello");
        map.put("bar", "World");
        String result = (String) ParseHelper.parseExpressWithException("foo + bar", map);
        log.info("testParseWithMvel 结果:" + result);
        Assert.assertEquals("HelloWorld", result);
    }

    /**
     * 测试解析普通字符串的方法.
     */
    @Test
    public void parseStr() {
        String result = ParseHelper.parseTemplate("zhangsan", ParamWrapper.newInstance("aa", "张三").toMap());
        log.info("testParseStr方法的结果:" + result);
        Assert.assertEquals("zhangsan", result);
    }

    /**
     * 测试解析普通字符串的方法.
     */
    @Test
    public void parseStr2() {
        Boolean result = (Boolean) ParseHelper.parseExpress("sex == 1", ParamWrapper.newInstance("sex", "1").toMap());
        Assert.assertNotNull(result);
        Assert.assertTrue(result);
    }

    /**
     * 测试解析普通字符串的方法.
     */
    @Test
    public void parseSpaceStr() {
        Assert.assertNull(ParseHelper.parseExpress("", ParamWrapper.newInstance("bb", "1").toMap()));
    }

}