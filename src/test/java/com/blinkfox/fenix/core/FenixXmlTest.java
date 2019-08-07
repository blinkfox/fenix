package com.blinkfox.fenix.core;

import static org.junit.Assert.assertEquals;

import com.blinkfox.fenix.bean.SqlInfo;
import com.blinkfox.fenix.config.FenixConfig;
import com.blinkfox.fenix.config.FenixConfigManager;
import com.blinkfox.fenix.entity.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 用于测试 Fenix XML 标签生成 SQL 和参数的测试类.
 *
 * @author blinkfox on 2019-08-06.
 */
public class FenixXmlTest {

    private static final String NAME = "ZhangSan";

    private static final String EMAIL_KEY = "email";

    /**
     * 基础查询的 SQL 语句.
     */
    private static final String BASE_QUERY = "SELECT u FROM User WHERE";

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
        context.put("user", new User().setId("123").setName(NAME));
        context.put(EMAIL_KEY, "zhangsan@163.com");
    }

    /**
     * 测试 equal 标签的情况.
     */
    @Test
    public void testEqual() {
        SqlInfo sqlInfo = Fenix.getSqlInfo("fenix.equal", context);
        assertEquals(BASE_QUERY + " u.id = :user_id AND u.name = :user_name OR u.email = :email",
                sqlInfo.getSql());

        Map<String, Object> params = sqlInfo.getParams();
        assertEquals(3, params.size());
        assertEquals(NAME, params.get("user_name"));
        assertEquals("zhangsan@163.com", params.get(EMAIL_KEY));
    }

    /**
     * 测试 notEqual 标签的情况.
     */
    @Test
    public void notEqual() {
        SqlInfo sqlInfo = Fenix.getSqlInfo("fenix.notEqual", context);
        assertEquals(BASE_QUERY + " u.id <> :user_id AND u.name <> :user_name OR u.email <> :email",
                sqlInfo.getSql());
        assertEquals(3, sqlInfo.getParams().size());
    }

    /**
     * 测试 greaterThan 标签的情况.
     */
    @Test
    public void greaterThan() {
        SqlInfo sqlInfo = Fenix.getSqlInfo("fenix.greaterThan", context);
        assertEquals(BASE_QUERY + " u.id > :user_id AND u.name > :user_name OR u.email > :email",
                sqlInfo.getSql());
        assertEquals(3, sqlInfo.getParams().size());
    }

    /**
     * 测试 lessThan 标签的情况.
     */
    @Test
    public void lessThan() {
        SqlInfo sqlInfo = Fenix.getSqlInfo("fenix.lessThan", context);
        assertEquals(BASE_QUERY + " u.id < :user_id AND u.name < :user_name OR u.email < :email",
                sqlInfo.getSql());
        assertEquals(3, sqlInfo.getParams().size());
    }

    /**
     * 测试 greaterThanEqual 标签的情况.
     */
    @Test
    public void greaterThanEqual() {
        SqlInfo sqlInfo = Fenix.getSqlInfo("fenix.greaterThanEqual", context);
        assertEquals(BASE_QUERY + " u.id >= :user_id AND u.name >= :user_name OR u.email >= :email",
                sqlInfo.getSql());
        assertEquals(3, sqlInfo.getParams().size());
    }

    /**
     * 测试 lessThanEqual 标签的情况.
     */
    @Test
    public void lessThanEqual() {
        SqlInfo sqlInfo = Fenix.getSqlInfo("fenix.lessThanEqual", context);
        assertEquals(BASE_QUERY + " u.id <= :user_id AND u.name <= :user_name OR u.email <= :email",
                sqlInfo.getSql());
        assertEquals(3, sqlInfo.getParams().size());
    }

    /**
     * 测试 like 标签的情况.
     */
    @Test
    public void like() {
        SqlInfo sqlInfo = Fenix.getSqlInfo("fenix.like", context);
        assertEquals(BASE_QUERY + " u.id LIKE :user_id AND u.name LIKE :user_name OR u.email LIKE '%@163.com'",
                sqlInfo.getSql());
        assertEquals(2, sqlInfo.getParams().size());
    }

    /**
     * 测试 notLike 标签的情况.
     */
    @Test
    public void notLike() {
        SqlInfo sqlInfo = Fenix.getSqlInfo("fenix.notLike", context);
        assertEquals(BASE_QUERY + " u.id NOT LIKE :user_id AND u.name NOT LIKE :user_name "
                        + "OR u.email NOT LIKE '%@163.com'",
                sqlInfo.getSql());
        assertEquals(2, sqlInfo.getParams().size());
    }

    /**
     * 测试 startsWith 标签的情况.
     */
    @Test
    public void startsWith() {
        SqlInfo sqlInfo = Fenix.getSqlInfo("fenix.startsWith", context);
        assertEquals(BASE_QUERY + " u.id LIKE :user_id AND u.name LIKE :user_name OR u.email LIKE :email",
                sqlInfo.getSql());

        Map<String, Object> params = sqlInfo.getParams();
        assertEquals(3, params.size());
        assertEquals("zhangsan@163.com%", params.get(EMAIL_KEY));
    }

    /**
     * 测试 notStartsWith 标签的情况.
     */
    @Test
    public void notStartsWith() {
        SqlInfo sqlInfo = Fenix.getSqlInfo("fenix.notStartsWith", context);
        assertEquals(BASE_QUERY + " u.id NOT LIKE :user_id AND u.name NOT LIKE :user_name OR u.email NOT LIKE :email",
                sqlInfo.getSql());
        assertEquals(3, sqlInfo.getParams().size());
    }

    /**
     * 测试 endsWith 标签的情况.
     */
    @Test
    public void endsWith() {
        SqlInfo sqlInfo = Fenix.getSqlInfo("fenix.endsWith", context);
        assertEquals(BASE_QUERY + " u.id LIKE :user_id AND u.name LIKE :user_name OR u.email LIKE :email",
                sqlInfo.getSql());
        assertEquals(3, sqlInfo.getParams().size());
    }

    /**
     * 测试 notEndsWith 标签的情况.
     */
    @Test
    public void notEndsWith() {
        SqlInfo sqlInfo = Fenix.getSqlInfo("fenix.notEndsWith", context);
        assertEquals(BASE_QUERY + " u.id NOT LIKE :user_id AND u.name NOT LIKE :user_name OR u.email NOT LIKE :email",
                sqlInfo.getSql());
        assertEquals(3, sqlInfo.getParams().size());
    }

    /**
     * 构建 IN 区间查询的相关参数.
     */
    private void buildInParams() {
        context.put("names", new String[] {NAME, "Lisi", "WangWu"});

        List<String> emails = new ArrayList<>(3);
        emails.add("zs@163.com");
        emails.add("lisi@gmail.com");
        emails.add("wangwu@qq.com");
        context.put("emails", emails);
    }

    /**
     * 测试 between 标签的情况.
     */
    @Test
    public void between() {
        // 构建 between 区间查询的相关参数.
        context.put("startId", "100");
        context.put("endId", null);
        context.put("startAge", 18);
        context.put("endAge", 30);
        context.put("startBirthday", null);
        context.put("endBirthday", "2019-08-07");

        SqlInfo sqlInfo = Fenix.getSqlInfo("fenix.between", context);
        assertEquals(BASE_QUERY + " u.id >= :startId AND u.age BETWEEN :startAge AND :endAge OR u.birthday <= :endBirthday",
                sqlInfo.getSql());
        assertEquals(4, sqlInfo.getParams().size());
    }

    /**
     * 测试 in 标签的情况.
     */
    @Test
    public void in() {
        this.buildInParams();
        SqlInfo sqlInfo = Fenix.getSqlInfo("fenix.in", context);
        assertEquals(BASE_QUERY + " u.id IN :user_id AND u.name IN :names OR u.email IN :emails",
                sqlInfo.getSql());
        assertEquals(3, sqlInfo.getParams().size());
    }

    /**
     * 测试 notIn 标签的情况.
     */
    @Test
    public void notIn() {
        this.buildInParams();
        SqlInfo sqlInfo = Fenix.getSqlInfo("fenix.notIn", context);
        assertEquals(BASE_QUERY + " u.id NOT IN :user_id AND u.name NOT IN :names OR u.email NOT IN :emails",
                sqlInfo.getSql());
        assertEquals(3, sqlInfo.getParams().size());
    }

    /**
     * 测试 isNull 标签的情况.
     */
    @Test
    public void isNull() {
        SqlInfo sqlInfo = Fenix.getSqlInfo("fenix.isNull", context);
        assertEquals(BASE_QUERY + " u.id IS NULL AND u.name IS NULL OR u.email IS NULL", sqlInfo.getSql());
        assertEquals(0, sqlInfo.getParams().size());
    }

    /**
     * 测试 isNotNull 标签的情况.
     */
    @Test
    public void isNotNull() {
        SqlInfo sqlInfo = Fenix.getSqlInfo("fenix.isNotNull", context);
        assertEquals(BASE_QUERY + " u.id IS NOT NULL AND u.name IS NOT NULL OR u.email IS NOT NULL",
                sqlInfo.getSql());
        assertEquals(0, sqlInfo.getParams().size());
    }

}
