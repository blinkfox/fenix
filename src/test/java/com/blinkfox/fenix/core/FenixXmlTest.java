package com.blinkfox.fenix.core;

import com.blinkfox.fenix.bean.SqlInfo;
import com.blinkfox.fenix.config.FenixConfig;
import com.blinkfox.fenix.config.FenixConfigManager;
import com.blinkfox.fenix.entity.User;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 用于测试 Fenix XML 标签生成 SQL 和参数的测试类.
 *
 * @author blinkfox on 2019-08-06.
 */
public class FenixXmlTest {

    /**
     * 上下文参数 Map.
     */
    private static Map<String, Object> context;

    /**
     * 初始化上下文参数.
     */
    @BeforeClass
    public static void init() {
        FenixConfigManager.getInstance().initLoad(new FenixConfig());
        context = new HashMap<>(4);
        context.put("entityName", User.class.getSimpleName());
        context.put("user", new User().setId("123").setName("ZhangSan").setEmail("zhangsan@163.com"));
    }

    /**
     * 测试 equal 标签的情况.
     */
    @Test
    public void testEqual() {
        SqlInfo sqlInfo = Fenix.getSqlInfo("fenix.equal", context);
        Assert.assertEquals("SELECT u FROM User WHERE u.id = :u_id AND u.name = :u_name OR u.email = :u_email",
                sqlInfo.getSql());

        Map<String, Object> params = sqlInfo.getParams();
        Assert.assertEquals(3, params.size());
        Assert.assertEquals("ZhangSan", params.get("u_name"));
    }

}
