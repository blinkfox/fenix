package com.blinkfox.fenix.helper;

import java.util.HashMap;
import java.util.Map;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * ParamWrapper Test.
 *
 * @author blinkfox on 2019-08-04.
 */
public class ParamWrapperTest {

    private static Map<String, Object> paramMap;

    private static ParamWrapper paramWrapper;

    /**
     * 初始化ParamWrapper实例.
     */
    @BeforeClass
    public static void init() {
        paramMap = new HashMap<>(8);
        paramMap.put("name", "韩梅梅");
        paramMap.put("sex", "女");
        paramMap.put("age", 27);

        paramWrapper = ParamWrapper.newInstance();
    }

    /**
     * 测试构造新实例的方法.
     */
    @Test
    public void newInstance() {
        Map<String, Object> params = ParamWrapper.newInstance(paramMap)
                .put("birthday", "1990-05-18")
                .toMap();
        Assert.assertEquals(4, params.size());
        Assert.assertEquals(27, params.get("age"));
    }

    /**
     * 测试构造新实例的方法.
     */
    @Test
    public void newInstanceWithKeyValue() {
        Map<String, Object> params = ParamWrapper.newInstance("age", 30).toMap();
        Assert.assertEquals(1, params.size());
        Assert.assertEquals(30, params.get("age"));
    }

    /**
     * 测试put和get方法.
     */
    @Test
    public void put() {
        paramWrapper.put("aa", "张三").put("bb", "李四");
        Map<String, Object> params = paramWrapper.toMap();
        Assert.assertEquals(2, params.size());
        Assert.assertEquals("张三", params.get("aa"));
    }

    /**
     * 销毁 paramWrapper 实例的方法.
     */
    @AfterClass
    public static void destroy() {
        paramWrapper = null;
    }

}
