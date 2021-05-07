package com.blinkfox.fenix.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.blinkfox.fenix.bean.SqlInfo;
import com.blinkfox.fenix.config.FenixConfig;
import com.blinkfox.fenix.config.FenixConfigManager;
import com.blinkfox.fenix.entity.User;
import com.blinkfox.fenix.handler.HelloTagHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 用于测试 Fenix XML 标签生成 SQL 和参数的测试类.
 *
 * @author blinkfox on 2019-08-06.
 */
public class FenixXmlBuilderTest {

    private static final String NAME = "ZhangSan";

    private static final String EMAIL_KEY = "email";

    /**
     * 基础查询的 SQL 语句.
     */
    private static final String SELECT_QUERY = "SELECT u FROM User";

    /**
     * 带 WHERE 的基础查询的 SQL 语句.
     */
    private static final String BASE_QUERY = SELECT_QUERY + " WHERE";

    /**
     * email 的常量字符串.
     */
    private static final String EMAIL = "zhangsan@163.com";

    /**
     * 上下文参数 Map.
     */
    private static Map<String, Object> context;

    /**
     * 初始化上下文参数.
     */
    @BeforeClass
    public static void init() {
        FenixConfig.add("hi", HelloTagHandler.class);
        FenixConfig.add("andHi", " AND ", HelloTagHandler::new, " LIKE ");
        FenixConfig fenixConfig = new FenixConfig().setHandlerLocations("com.blinkfox.fenix.handler");
        FenixConfigManager.getInstance().initLoad(fenixConfig);

        context = new HashMap<>(4);
        context.put("entityName", User.class.getSimpleName());
        context.put("user", new User().setId("123").setName(NAME).setSex("0"));
        context.put(EMAIL_KEY, EMAIL);
    }

    /**
     * 测试 equal 标签的情况.
     */
    @Test
    public void testEqual() {
        SqlInfo sqlInfo = Fenix.getXmlSqlInfo("fenix.equal", context);
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
        SqlInfo sqlInfo = Fenix.getXmlSqlInfo("fenix.notEqual", context);
        assertEquals(BASE_QUERY + " u.id <> :user_id AND u.name <> :user_name OR u.email <> :email",
                sqlInfo.getSql());
        assertEquals(3, sqlInfo.getParams().size());
    }

    /**
     * 测试 greaterThan 标签的情况.
     */
    @Test
    public void greaterThan() {
        SqlInfo sqlInfo = Fenix.getXmlSqlInfo("fenix.greaterThan", context);
        assertEquals(BASE_QUERY + " u.id > :user_id AND u.name > :user_name OR u.email > :email",
                sqlInfo.getSql());
        assertEquals(3, sqlInfo.getParams().size());
    }

    /**
     * 测试 lessThan 标签的情况.
     */
    @Test
    public void lessThan() {
        SqlInfo sqlInfo = Fenix.getXmlSqlInfo("fenix.lessThan", context);
        assertEquals(BASE_QUERY + " u.id < :user_id AND u.name < :user_name OR u.email < :email",
                sqlInfo.getSql());
        assertEquals(3, sqlInfo.getParams().size());
    }

    /**
     * 测试 greaterThanEqual 标签的情况.
     */
    @Test
    public void greaterThanEqual() {
        SqlInfo sqlInfo = Fenix.getXmlSqlInfo("fenix.greaterThanEqual", context);
        assertEquals(BASE_QUERY + " u.id >= :user_id AND u.name >= :user_name OR u.email >= :email",
                sqlInfo.getSql());
        assertEquals(3, sqlInfo.getParams().size());
    }

    /**
     * 测试 lessThanEqual 标签的情况.
     */
    @Test
    public void lessThanEqual() {
        SqlInfo sqlInfo = Fenix.getXmlSqlInfo("fenix.lessThanEqual", context);
        assertEquals(BASE_QUERY + " u.id <= :user_id AND u.name <= :user_name OR u.email <= :email",
                sqlInfo.getSql());
        assertEquals(3, sqlInfo.getParams().size());
    }

    /**
     * 测试 like 标签的情况.
     */
    @Test
    public void like() {
        SqlInfo sqlInfo = Fenix.getXmlSqlInfo("fenix.like", context);
        assertEquals(BASE_QUERY + " u.id LIKE :user_id AND u.name LIKE :user_name OR u.email LIKE '%@163.com'",
                sqlInfo.getSql());
        assertEquals(2, sqlInfo.getParams().size());
    }

    /**
     * 测试 notLike 标签的情况.
     */
    @Test
    public void notLike() {
        SqlInfo sqlInfo = Fenix.getXmlSqlInfo("fenix.notLike", context);
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
        SqlInfo sqlInfo = Fenix.getXmlSqlInfo("fenix.startsWith", context);
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
        SqlInfo sqlInfo = Fenix.getXmlSqlInfo("fenix.notStartsWith", context);
        assertEquals(BASE_QUERY + " u.id NOT LIKE :user_id AND u.name NOT LIKE :user_name OR u.email NOT LIKE :email",
                sqlInfo.getSql());
        assertEquals(3, sqlInfo.getParams().size());
    }

    /**
     * 测试 endsWith 标签的情况.
     */
    @Test
    public void endsWith() {
        SqlInfo sqlInfo = Fenix.getXmlSqlInfo("fenix.endsWith", context);
        assertEquals(BASE_QUERY + " u.id LIKE :user_id AND u.name LIKE :user_name OR u.email LIKE :email",
                sqlInfo.getSql());
        assertEquals(3, sqlInfo.getParams().size());
    }

    /**
     * 测试 notEndsWith 标签的情况.
     */
    @Test
    public void notEndsWith() {
        SqlInfo sqlInfo = Fenix.getXmlSqlInfo("fenix.notEndsWith", context);
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

        SqlInfo sqlInfo = Fenix.getXmlSqlInfo("fenix.between", context);
        assertEquals(BASE_QUERY + " u.id >= :startId AND u.age BETWEEN :startAge AND :endAge "
                + "OR u.birthday <= :endBirthday", sqlInfo.getSql());
        assertEquals(4, sqlInfo.getParams().size());
    }

    /**
     * 测试 in 标签的情况.
     */
    @Test
    public void in() {
        this.buildInParams();
        SqlInfo sqlInfo = Fenix.getXmlSqlInfo("fenix.in", context);
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
        SqlInfo sqlInfo = Fenix.getXmlSqlInfo("fenix.notIn", context);
        assertEquals(BASE_QUERY + " u.id NOT IN :user_id AND u.name NOT IN :names OR u.email NOT IN :emails",
                sqlInfo.getSql());
        assertEquals(3, sqlInfo.getParams().size());
    }

    /**
     * 测试 isNull 标签的情况.
     */
    @Test
    public void isNull() {
        SqlInfo sqlInfo = Fenix.getXmlSqlInfo("fenix.isNull", context);
        assertEquals(BASE_QUERY + " u.id IS NULL AND u.name IS NULL OR u.email IS NULL", sqlInfo.getSql());
        assertEquals(0, sqlInfo.getParams().size());
    }

    /**
     * 测试 isNotNull 标签的情况.
     */
    @Test
    public void isNotNull() {
        SqlInfo sqlInfo = Fenix.getXmlSqlInfo("fenix.isNotNull", context);
        assertEquals(BASE_QUERY + " u.id IS NOT NULL AND u.name IS NOT NULL OR u.email IS NOT NULL",
                sqlInfo.getSql());
        assertEquals(0, sqlInfo.getParams().size());
    }

    /**
     * 测试 text 标签的情况.
     */
    @Test
    public void text() {
        SqlInfo sqlInfo = Fenix.getXmlSqlInfo("fenix.text", context);
        assertEquals(BASE_QUERY + " u.id = :userId AND u.name = :userName AND u.email LIKE :email",
                sqlInfo.getSql());
        assertEquals(3, sqlInfo.getParams().size());
    }

    /**
     * 测试 import 标签的情况.
     */
    @Test
    public void testImport() {
        context.put("names", new String[] {NAME, "Maliu"});
        SqlInfo sqlInfo = Fenix.getXmlSqlInfo("fenix.import", context);
        assertEquals("SELECT u.id, u.name, u.email FROM User WHERE u.id IS NOT NULL "
                        + "AND u.name IN :names OR u.email LIKE :email",
                sqlInfo.getSql());
        assertEquals(2, sqlInfo.getParams().size());
    }

    /**
     * 测试 choose 标签的情况.
     */
    @Test
    public void choose() {
        context.put("age", 25);
        SqlInfo sqlInfo = Fenix.getXmlSqlInfo("fenix.choose", context);
        assertEquals("UPDATE t_user SET u.c_sex = 'female' , u.c_status = 'no' , u.c_age = '青年' WHERE u.c_id = '123'",
                sqlInfo.getSql());
        assertEquals(0, sqlInfo.getParams().size());
    }

    /**
     * 测试 set 标签的情况.
     */
    @Test
    public void set() {
        context.put("age", 25);
        SqlInfo sqlInfo = Fenix.getXmlSqlInfo("fenix.set", context);
        assertEquals("UPDATE User SET name = :name, email = :email, sex = :sex WHERE u.c_id = '123'",
                sqlInfo.getSql());
        assertEquals(3, sqlInfo.getParams().size());
    }

    /**
     * 测试 hello 的自定义标签的情况.
     */
    @Test
    public void helloTagger() {
        String world = "World";
        Map<String, Object> contextParams = new HashMap<>(4);
        contextParams.put(world, world);
        contextParams.put("Blinkfox", "Blinkfox");
        contextParams.put("Fenix", "Fenix");
        SqlInfo sqlInfo = Fenix.getXmlSqlInfo("fenix.helloTagger", contextParams);
        assertEquals("Hello = :World AND Hello = :Blinkfox OR Hello = :Fenix", sqlInfo.getSql());
        assertEquals(3, sqlInfo.getParams().size());
        assertEquals(world, sqlInfo.getParams().get("World"));
    }

    /**
     * 测试 hi 的自定义标签的情况.
     */
    @Test
    public void hiTagger() {
        String name = "LiLei";
        Map<String, Object> contextParams = new HashMap<>(4);
        contextParams.put(name, name);
        contextParams.put("HanMeiMei", name);
        SqlInfo sqlInfo = Fenix.getXmlSqlInfo("fenix.hiTagger", contextParams);
        assertEquals("Hello :LiLei AND Hello LIKE :HanMeiMei", sqlInfo.getSql());
        assertEquals(2, sqlInfo.getParams().size());
        assertEquals(name, sqlInfo.getParams().get(name));
    }

    /**
     * 测试 where 标签的情况.
     */
    @Test
    public void testWhere() {
        SqlInfo sqlInfo = Fenix.getXmlSqlInfo("fenix.testWhere", context);
        assertEquals(BASE_QUERY + " u.id = :user_id AND u.name LIKE :user_name",
                sqlInfo.getSql());

        Map<String, Object> params = sqlInfo.getParams();
        assertEquals(2, params.size());
        assertEquals('%' + NAME + '%', params.get("user_name"));
    }

    /**
     * 测试 where 标签的情况2.
     */
    @Test
    public void testWhere2() {
        SqlInfo sqlInfo = Fenix.getXmlSqlInfo("fenix.testWhere2", context);
        assertEquals(SELECT_QUERY, sqlInfo.getSql());

        Map<String, Object> params = sqlInfo.getParams();
        Assert.assertTrue(params.isEmpty());
    }

    /**
     * 测试 where 标签的情况3.
     */
    @Test
    public void testWhere3() {
        SqlInfo sqlInfo = Fenix.getXmlSqlInfo("fenix.testWhere3", context);
        assertEquals(BASE_QUERY + " u.id = :user_id AND u.name LIKE :user_name ORDER BY u.updateTime DESC",
                sqlInfo.getSql());

        Map<String, Object> params = sqlInfo.getParams();
        assertEquals(2, params.size());
        assertEquals('%' + NAME + '%', params.get("user_name"));
    }

    /**
     * 测试 where 标签的情况4.
     */
    @Test
    public void testWhere4() {
        SqlInfo sqlInfo = Fenix.getXmlSqlInfo("fenix.testWhere4", context);
        assertEquals(BASE_QUERY + " u.name LIKE '%ZhangSan%' AND u.email = :email", sqlInfo.getSql());

        Map<String, Object> params = sqlInfo.getParams();
        assertEquals(1, params.size());
        assertEquals(EMAIL, params.get("email"));
    }

    /**
     * 测试 where 标签的情况5.
     */
    @Test
    public void testWhere5() {
        SqlInfo sqlInfo = Fenix.getXmlSqlInfo("fenix.testWhere5", context);
        assertEquals(SELECT_QUERY + " ORDER BY u.updateTime DESC", sqlInfo.getSql());
        assertTrue(sqlInfo.getParams().isEmpty());
    }

    /**
     * 测试 where 标签的情况6.
     */
    @Test
    public void testWhere6() {
        SqlInfo sqlInfo = Fenix.getXmlSqlInfo("fenix.testWhere6", context);
        assertEquals(SELECT_QUERY, sqlInfo.getSql());
        assertTrue(sqlInfo.getParams().isEmpty());
    }

    /**
     * 测试 where 标签的情况7.
     */
    @Test
    public void testWhere7() {
        SqlInfo sqlInfo = Fenix.getXmlSqlInfo("fenix.testWhere7", context);
        assertEquals(SELECT_QUERY, sqlInfo.getSql());
        assertTrue(sqlInfo.getParams().isEmpty());
    }

    /**
     * 测试 trimWhere 标签的情况8.
     */
    @Test
    public void testTrimWhere() {
        SqlInfo sqlInfo = Fenix.getXmlSqlInfo("fenix.testTrimWhere", context);
        assertEquals("SELECT * FROM ( SELECT t.name as name FROM User t ) a", sqlInfo.getSql());
        assertTrue(sqlInfo.getParams().isEmpty());
    }

    /**
     * 测试 trimWhere 标签的情况2.
     */
    @Test
    public void testTrimWhere2() {
        SqlInfo sqlInfo = Fenix.getXmlSqlInfo("fenix.testTrimWhere2", context);
        assertEquals("SELECT * FROM ( SELECT t.name as name FROM User t WHERE u.name LIKE :user_name ) a",
                sqlInfo.getSql());
        assertEquals(1, sqlInfo.getParams().size());
    }

    /**
     * 测试 trimWhere 标签的情况3.
     */
    @Test
    public void testTrimWhere3() {
        SqlInfo sqlInfo = Fenix.getXmlSqlInfo("fenix.testTrimWhere3", context);
        assertEquals(BASE_QUERY + " u.id = :user_id AND u.name LIKE :user_name ORDER BY u.updateTime DESC",
                sqlInfo.getSql());

        Map<String, Object> params = sqlInfo.getParams();
        assertEquals(2, params.size());
        assertEquals('%' + NAME + '%', params.get("user_name"));
    }

    /**
     * 测试 trimWhere 标签的情况4.
     */
    @Test
    public void testTrimWhere4() {
        SqlInfo sqlInfo = Fenix.getXmlSqlInfo("fenix.testTrimWhere4", context);
        assertEquals(SELECT_QUERY, sqlInfo.getSql());
        assertEquals(0, sqlInfo.getParams().size());
    }

    /**
     * 测试 trimWhere 标签的情况5.
     */
    @Test
    public void testTrimWhere5() {
        SqlInfo sqlInfo = Fenix.getXmlSqlInfo("fenix.testTrimWhere5", context);
        assertEquals(BASE_QUERY + " u.name LIKE '%ZhangSan%' AND u.email = :email", sqlInfo.getSql());

        Map<String, Object> params = sqlInfo.getParams();
        assertEquals(1, params.size());
        assertEquals(EMAIL, params.get("email"));
    }

    /**
     * 测试 trimWhere 标签的情况6.
     */
    @Test
    public void testTrimWhere6() {
        SqlInfo sqlInfo = Fenix.getXmlSqlInfo("fenix.testTrimWhere6", context);
        assertEquals(SELECT_QUERY, sqlInfo.getSql());
        assertTrue(sqlInfo.getParams().isEmpty());
    }

    /**
     * 测试 trimWhere 标签的情况6.
     */
    @Test
    public void testTrimWhere7() {
        SqlInfo sqlInfo = Fenix.getXmlSqlInfo("fenix.testTrimWhere7", context);
        assertEquals(SELECT_QUERY + " WHERE 1 = 1", sqlInfo.getSql());
        assertTrue(sqlInfo.getParams().isEmpty());
    }

}
