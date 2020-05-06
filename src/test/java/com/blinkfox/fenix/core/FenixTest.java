package com.blinkfox.fenix.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.blinkfox.fenix.bean.SqlInfo;
import com.blinkfox.fenix.helper.ParamWrapper;
import com.blinkfox.fenix.helper.StringHelper;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * {@link Fenix} 核心 API 的单元测试类.
 *
 * <p>下面的拼接程序不一定是正确可执行的 SQL，仅仅用来测试程序，包括正常或异常的情况等等.</p>
 *
 * @author blinkfox on 2019-08-12.
 */
@Slf4j
public class FenixTest {

    /**
     * 上下文参数对象.
     */
    private static Map<String, Object> context;

    /**
     * 初始化.
     */
    @BeforeClass
    public static void init() {
        context = new HashMap<>(32);
        context.put("id", "3");
        context.put("name", "zhagnsan");
        context.put("myEmail", "zhagnsan@163.com");
        context.put("myAge", 25);
        context.put("startAge", 18);
        context.put("endAge", 26);
        context.put("myBirthday", "1990-03-31");
        context.put("startBirthday", null);
        context.put("endBirthday", "2010-05-28");
        context.put("sexs", new Integer[] {0, 1});
        context.put("citys", Arrays.asList("四川", "北京", "上海"));
    }

    /**
     * start方法.
     */
    @Test
    public void start() {
        SqlInfo sqlInfo = Fenix.start().end();
        assertEquals("", sqlInfo.getJoin().toString());
        assertEquals(0, sqlInfo.getParams().size());
    }

    /**
     * 测试 INSERT 的一些拼接方式.
     */
    @Test
    public void testInsert() {
        SqlInfo sqlInfo = Fenix.start()
                .insertInto("user")
                .values("('3', 'lisi')")
                .text(false, "a", ParamWrapper.newInstance("a", ":").toMap())
                .end();

        assertEquals("INSERT INTO user VALUES ('3', 'lisi')", sqlInfo.getSql());
        assertEquals(0, sqlInfo.getParams().size());
    }

    /**
     * 测试 UPDATE 的一些拼接方式.
     */
    @Test
    public void testUpdate() {
        SqlInfo sqlInfo = Fenix.start()
                .update("user")
                .set("nick_name = 'wangwu'")
                .where("user_id = :userId", ParamWrapper.newInstance("userId", "1").toMap())
                .end();

        assertEquals("UPDATE user SET nick_name = 'wangwu' WHERE user_id = :userId", sqlInfo.getSql());
        assertEquals(1, sqlInfo.getParams().size());
    }

    /**
     * 测试 DELETE 的一些拼接方式.
     */
    @Test
    public void testDelete() {
        SqlInfo sqlInfo = Fenix.start()
                .deleteFrom("User").as("u")
                .where("u.id = :id", "id", "2")
                .end();

        assertEquals("DELETE FROM User AS u WHERE u.id = :id", sqlInfo.getSql());
        assertEquals(1, sqlInfo.getParams().size());
    }

    /**
     * 一个综合测试 SELECT 的一些拼接方式.
     */
    @Test
    public void testSelect() {
        SqlInfo sqlInfo = Fenix.start()
                .select("u.id, u.nickName, u.email")
                .from("user").as("u")
                .innerJoin("Corp as c").on("u.corpId = c.id")
                .leftJoin("Dept").as("d").on("u.deptId = d.id")
                .rightJoin("Office").as("o").on("u.officeId = o.id")
                .fullJoin("UserDetail").as("ud").on("u.detailId = ud.id")
                .where("u.id = :u_id", "u_id", "3")
                .and("u.nickName like '%zhang%'")
                .text(true, "AND u.email = :u_email", "u_email", "san@163.com")
                .doAny(true, (join, params) -> {
                    join.append(" hi");
                    params.put("hi", 5);
                    log.info("执行了自定义操作，你可以任意拼接 JPQL 或者 SQL 字符串和命名参数...");
                })
                .groupBy("u.id").having("u.id")
                .orderBy("u.id").desc().text(", u.nickName", "u_nick_name", "zhang").asc()
                .unionAll()
                .select("u.id, u.nickName, u.email")
                .from("user2")
                .limit("5")
                .offset("3")
                .end();
        String sql = sqlInfo.getSql();

        assertEquals("SELECT u.id, u.nickName, u.email FROM user AS u INNER JOIN Corp as c ON u.corpId = c.id "
                + "LEFT JOIN Dept AS d ON u.deptId = d.id RIGHT JOIN Office AS o ON u.officeId = o.id "
                + "FULL JOIN UserDetail AS ud ON u.detailId = ud.id WHERE u.id = :u_id AND u.nickName like '%zhang%' "
                + "AND u.email = :u_email hi GROUP BY u.id HAVING u.id ORDER BY u.id DESC , u.nickName ASC "
                + "UNION ALL SELECT u.id, u.nickName, u.email FROM user2 LIMIT 5 OFFSET 3", sql);
        assertEquals(4, sqlInfo.getParams().size());
    }

    /**
     * 测试 text 任何文本和参数的相关方法测试.
     */
    @Test
    public void testText() {
        SqlInfo sqlInfo = Fenix.start()
                .text("select u.id, u.nickName from User as u where ")
                .text("u.id = :id ", "id", 5)
                .text("and u.nickName like :nickName ", "nickName", "lisi")
                .text("and u.sex in :sex ").param("sex", new Integer[] {2, 3, 4})
                .text("and u.city in :city ", "city", context.get("citys"))
                .end();

        assertEquals("select u.id, u.nickName from User as u where u.id = :id and u.nickName like :nickName "
                + "and u.sex in :sex and u.city in :city", sqlInfo.getSql());
        assertEquals(4, sqlInfo.getParams().size());
    }

    /**
     * equal 相关方法的测试.
     */
    @Test
    public void testEqual() {
        SqlInfo sqlInfo = Fenix.start()
                .equal("u.id", context.get("id"), "4".equals(context.get("id")))
                .equal("u.nickName", context.get("name"))
                .equal("u.email", context.get("myEmail"), context.get("myEmail") != null)
                .andEqual("u.age", context.get("myAge"))
                .andEqual("u.trueAge", context.get("myAge"))
                .andEqual("u.trueAge", context.get("myAge"), context.get("myAge") != null)
                .andEqual("u.email", context.get("myAge"), context.get("myEmail") == null)
                .equal("u.nickName", context.get("name"))
                .orEqual("u.email", context.get("myEmail"))
                .orEqual("u.birthday", context.get("myBirthday"))
                .orEqual("u.birthday", context.get("myBirthday"), context.get("myBirthday") != null)
                .orEqual("u.nickName", context.get("myBirthday"), context.get("name") == null)
                .equal("u.id", context.get("id"))
                .end();

        assertEquals("u.nickName = :u_nickName u.email = :u_email AND u.age = :u_age AND u.trueAge = "
                + ":u_trueAge AND u.trueAge = :u_trueAge u.nickName = :u_nickName OR u.email = :u_email "
                + "OR u.birthday = :u_birthday OR u.birthday = :u_birthday u.id = :u_id", sqlInfo.getSql());
        assertEquals(6, sqlInfo.getParams().size());
    }

    /**
     * notEqual 相关方法的测试.
     */
    @Test
    public void testNotEqual() {
        SqlInfo sqlInfo = Fenix.start()
                .notEqual("u.id", context.get("id"), "4".equals(context.get("id")))
                .notEqual("u.nickName", context.get("name"))
                .notEqual("u.email", context.get("myEmail"), context.get("myEmail") != null)
                .andNotEqual("u.age", context.get("myAge"))
                .andNotEqual("u.trueAge", context.get("myAge"))
                .andNotEqual("u.trueAge", context.get("myAge"), context.get("myAge") != null)
                .andNotEqual("u.email", context.get("myAge"), context.get("myEmail") == null)
                .notEqual("u.nickName", context.get("name"))
                .orNotEqual("u.email", context.get("myEmail"))
                .orNotEqual("u.birthday", context.get("myBirthday"))
                .orNotEqual("u.birthday", context.get("myBirthday"), context.get("myBirthday") != null)
                .orNotEqual("u.nickName", context.get("myBirthday"), context.get("name") == null)
                .notEqual("u.id", context.get("id"))
                .end();

        assertEquals("u.nickName <> :u_nickName u.email <> :u_email AND u.age <> :u_age AND u.trueAge <> "
                + ":u_trueAge AND u.trueAge <> :u_trueAge u.nickName <> :u_nickName OR u.email <> :u_email "
                + "OR u.birthday <> :u_birthday OR u.birthday <> :u_birthday u.id <> :u_id", sqlInfo.getSql());
        assertEquals(6, sqlInfo.getParams().size());
    }

    /**
     * 测试 大于、小于、大于等于、小于等于 等系列方法.
     */
    @Test
    public void testThan() {
        SqlInfo sqlInfo = Fenix.start()
                .select("u")
                .from("User")
                .where()
                .greaterThan("u.age", 18)
                .and("").greaterThan("u.age", 25, false)
                .andGreaterThan("u.birthday", "1990-02-17", false)
                .orGreaterThan("u.age", 30, true)
                .orGreaterThan("u.salary", 5000)
                .or("")
                .or().lessThan("u.age", 19)
                .and().lessThan("u.age", 27, true)
                .andLessThan("u.birthday", "1990-07-15", false)
                .orLessThan("u.age", 56)
                .orLessThan("u.salary", 8000, true)
                .union()
                .select("u")
                .from("userBak")
                .where("1 = 1")
                .greaterThanEqual("u.age", 18)
                .and().greaterThanEqual("u.age", 25, false)
                .andGreaterThanEqual("u.birthday", "1990-02-17", false)
                .orGreaterThanEqual("u.age", 29)
                .orGreaterThanEqual("u.salary", 4500, true)
                .and().lessThanEqual("u.age", 19)
                .and().lessThanEqual("u.age", 27, true)
                .andLessThanEqual("u.birthday", "1990-07-15", false)
                .orLessThanEqual("u.salary", 9700)
                .orLessThanEqual("u.age", 85, false)
                .end();

        assertEquals("SELECT u FROM User WHERE u.age > :u_age AND OR u.age > :u_age OR u.salary > :u_salary "
                + "OR OR u.age < :u_age AND u.age < :u_age OR u.age < :u_age OR u.salary < :u_salary UNION SELECT u "
                + "FROM userBak WHERE 1 = 1 u.age >= :u_age AND OR u.age >= :u_age OR u.salary >= :u_salary AND "
                + "u.age <= :u_age AND u.age <= :u_age OR u.salary <= :u_salary", sqlInfo.getSql());
        assertEquals(2, sqlInfo.getParams().size());
    }

    /**
     * LIKE 片段相关方法的测试.
     */
    @Test
    public void testLike() {
        SqlInfo sqlInfo = Fenix.start()
                .like("u.id", context.get("id"), "4".equals(context.get("id")))
                .like("u.nickName", context.get("name"))
                .like("u.email", context.get("myEmail"), context.get("myEmail") != null)
                .andLike("u.age", context.get("myAge"))
                .andLike("u.trueAge", context.get("myAge"))
                .andLike("u.trueAge", context.get("myAge"), context.get("myAge") != null)
                .andLike("u.email", context.get("myAge"), context.get("myEmail") == null)
                .like("u.nickName", context.get("name"))
                .orLike("u.email", context.get("myEmail"))
                .orLike("u.birthday", context.get("myBirthday"))
                .orLike("u.birthday", context.get("myBirthday"), context.get("myBirthday") != null)
                .orLike("u.nickName", context.get("myBirthday"), context.get("name") == null)
                .like("u.id", context.get("id"))
                .end();

        assertEquals("u.nickName LIKE :u_nickName u.email LIKE :u_email AND u.age LIKE :u_age AND u.trueAge "
                + "LIKE :u_trueAge AND u.trueAge LIKE :u_trueAge u.nickName LIKE :u_nickName OR u.email LIKE :u_email "
                + "OR u.birthday LIKE :u_birthday OR u.birthday LIKE :u_birthday u.id LIKE :u_id", sqlInfo.getSql());
        assertEquals(6, sqlInfo.getParams().size());
    }

    /**
     * NOT LIKE 片段相关方法的测试.
     */
    @Test
    public void testNotLike() {
        SqlInfo sqlInfo = Fenix.start()
                .notLike("u.id", context.get("id"), "4".equals(context.get("id")))
                .notLike("u.nickName", context.get("name"))
                .notLike("u.email", context.get("myEmail"), context.get("myEmail") != null)
                .andNotLike("u.age", context.get("myAge"))
                .andNotLike("u.trueAge", context.get("myAge"), context.get("myAge") != null)
                .andNotLike("u.email", context.get("myAge"), context.get("myEmail") == null)
                .orNotLike("u.email", context.get("myEmail"))
                .orNotLike("u.birthday", context.get("myBirthday"), context.get("myBirthday") != null)
                .orNotLike("u.nickName", context.get("myBirthday"), context.get("name") == null)
                .end();

        assertEquals("u.nickName NOT LIKE :u_nickName u.email NOT LIKE :u_email AND u.age NOT LIKE :u_age "
                        + "AND u.trueAge NOT LIKE :u_trueAge OR u.email NOT LIKE :u_email OR u.birthday NOT LIKE "
                        + ":u_birthday",
                sqlInfo.getSql());
        assertEquals(5, sqlInfo.getParams().size());
    }

    /**
     * StartsWith 片段相关方法的测试.
     */
    @Test
    public void testStartsWith() {
        SqlInfo sqlInfo = Fenix.start()
                .startsWith("u.id", context.get("id"), "4".equals(context.get("id")))
                .startsWith("u.nickName", context.get("name"))
                .startsWith("u.email", context.get("myEmail"), context.get("myEmail") != null)
                .andStartsWith("u.age", context.get("myAge"))
                .andStartsWith("u.trueAge", context.get("myAge"))
                .andStartsWith("u.trueAge", context.get("myAge"), context.get("myAge") != null)
                .andStartsWith("u.email", context.get("myAge"), context.get("myEmail") == null)
                .startsWith("u.nickName", context.get("name"))
                .orStartsWith("u.email", context.get("myEmail"))
                .orStartsWith("u.birthday", context.get("myBirthday"))
                .orStartsWith("u.birthday", context.get("myBirthday"), context.get("myBirthday") != null)
                .orStartsWith("u.nickName", context.get("myBirthday"), context.get("name") == null)
                .startsWith("u.id", context.get("id"))
                .end();

        assertEquals("u.nickName LIKE :u_nickName u.email LIKE :u_email AND u.age LIKE :u_age AND u.trueAge "
                + "LIKE :u_trueAge AND u.trueAge LIKE :u_trueAge u.nickName LIKE :u_nickName OR u.email LIKE :u_email "
                + "OR u.birthday LIKE :u_birthday OR u.birthday LIKE :u_birthday u.id LIKE :u_id", sqlInfo.getSql());
        assertEquals(6, sqlInfo.getParams().size());
    }

    /**
     * NotStartsWith 片段相关方法的测试.
     */
    @Test
    public void testNotStartsWith() {
        SqlInfo sqlInfo = Fenix.start()
                .notStartsWith("u.id", context.get("id"), "4".equals(context.get("id")))
                .notStartsWith("u.nickName", context.get("name"))
                .notStartsWith("u.email", context.get("myEmail"), context.get("myEmail") != null)
                .andNotStartsWith("u.age", context.get("myAge"))
                .andNotStartsWith("u.trueAge", context.get("myAge"), context.get("myAge") != null)
                .andNotStartsWith("u.email", context.get("myAge"), context.get("myEmail") == null)
                .orNotStartsWith("u.email", context.get("myEmail"))
                .orNotStartsWith("u.birthday", context.get("myBirthday"), context.get("myBirthday") != null)
                .orNotStartsWith("u.nickName", context.get("myBirthday"), context.get("name") == null)
                .end();

        assertEquals("u.nickName NOT LIKE :u_nickName u.email NOT LIKE :u_email AND u.age NOT LIKE :u_age "
                        + "AND u.trueAge NOT LIKE :u_trueAge OR u.email NOT LIKE :u_email OR u.birthday NOT LIKE "
                        + ":u_birthday",
                sqlInfo.getSql());
        assertEquals(5, sqlInfo.getParams().size());
    }

    /**
     * EndsWith 片段相关方法的测试.
     */
    @Test
    public void testEndsWith() {
        SqlInfo sqlInfo = Fenix.start()
                .endsWith("u.id", context.get("id"), "4".equals(context.get("id")))
                .endsWith("u.nickName", context.get("name"))
                .endsWith("u.email", context.get("myEmail"), context.get("myEmail") != null)
                .andEndsWith("u.age", context.get("myAge"))
                .andEndsWith("u.trueAge", context.get("myAge"))
                .andEndsWith("u.trueAge", context.get("myAge"), context.get("myAge") != null)
                .andEndsWith("u.email", context.get("myAge"), context.get("myEmail") == null)
                .endsWith("u.nickName", context.get("name"))
                .orEndsWith("u.email", context.get("myEmail"))
                .orEndsWith("u.birthday", context.get("myBirthday"))
                .orEndsWith("u.birthday", context.get("myBirthday"), context.get("myBirthday") != null)
                .orEndsWith("u.nickName", context.get("myBirthday"), context.get("name") == null)
                .endsWith("u.id", context.get("id"))
                .end();

        assertEquals("u.nickName LIKE :u_nickName u.email LIKE :u_email AND u.age LIKE :u_age AND u.trueAge "
                + "LIKE :u_trueAge AND u.trueAge LIKE :u_trueAge u.nickName LIKE :u_nickName OR u.email LIKE :u_email "
                + "OR u.birthday LIKE :u_birthday OR u.birthday LIKE :u_birthday u.id LIKE :u_id", sqlInfo.getSql());
        assertEquals(6, sqlInfo.getParams().size());
    }

    /**
     * NotEndsWith 片段相关方法的测试.
     */
    @Test
    public void testNotEndsWith() {
        SqlInfo sqlInfo = Fenix.start()
                .notEndsWith("u.id", context.get("id"), "4".equals(context.get("id")))
                .notEndsWith("u.nickName", context.get("name"))
                .notEndsWith("u.email", context.get("myEmail"), context.get("myEmail") != null)
                .andNotEndsWith("u.age", context.get("myAge"))
                .andNotEndsWith("u.trueAge", context.get("myAge"), context.get("myAge") != null)
                .andNotEndsWith("u.email", context.get("myAge"), context.get("myEmail") == null)
                .orNotEndsWith("u.email", context.get("myEmail"))
                .orNotEndsWith("u.birthday", context.get("myBirthday"), context.get("myBirthday") != null)
                .orNotEndsWith("u.nickName", context.get("myBirthday"), context.get("name") == null)
                .end();

        assertEquals("u.nickName NOT LIKE :u_nickName u.email NOT LIKE :u_email AND u.age NOT LIKE :u_age "
                        + "AND u.trueAge NOT LIKE :u_trueAge OR u.email NOT LIKE :u_email OR u.birthday NOT LIKE "
                        + ":u_birthday",
                sqlInfo.getSql());
        assertEquals(5, sqlInfo.getParams().size());
    }

    /**
     * 根据指定模式生成 LIKE 片段相关方法的测试.
     */
    @Test
    public void testLikePattern() {
        SqlInfo sqlInfo = Fenix.start()
                .likePattern("u.id", "4%", "4".equals(context.get("id")))
                .likePattern("u.nickName", context.get("name") + "%")
                .likePattern("u.email", "%" + context.get("myEmail"), context.get("myEmail") != null)
                .andLikePattern("u.age", context.get("myAge") + "%")
                .andLikePattern("u.trueAge", context.get("myAge") + "%", context.get("myAge") != null)
                .andLikePattern("u.email", context.get("myAge") + "%", context.get("myEmail") == null)
                .orLikePattern("u.email", context.get("myEmail") + "%")
                .orLikePattern("u.birthday", "%" + context.get("myBirthday") + "%", context.get("myBirthday") != null)
                .end();

        assertEquals("u.nickName LIKE 'zhagnsan%' u.email LIKE '%zhagnsan@163.com' AND u.age LIKE '25%' AND "
                        + "u.trueAge LIKE '25%' OR u.email LIKE 'zhagnsan@163.com%' OR u.birthday LIKE '%1990-03-31%'",
                sqlInfo.getSql());
        assertTrue(sqlInfo.getParams().isEmpty());
    }

    /**
     * 根据指定模式生成 NOT LIKE 片段相关方法的测试.
     */
    @Test
    public void testNotLikePattern() {
        SqlInfo sqlInfo = Fenix.start()
                .notLikePattern("u.id", "4%", "4".equals(context.get("id")))
                .notLikePattern("u.nickName", context.get("name") + "%")
                .notLikePattern("u.email", "%" + context.get("myEmail"), context.get("myEmail") != null)
                .andNotLikePattern("u.age", context.get("myAge") + "%")
                .andNotLikePattern("u.trueAge", context.get("myAge") + "%", context.get("myAge") != null)
                .andNotLikePattern("u.email", context.get("myAge") + "%", context.get("myEmail") == null)
                .orNotLikePattern("u.email", context.get("myEmail") + "%")
                .orNotLikePattern("u.birthday", context.get("myBirthday") + "%", context.get("myBirthday") != null)
                .end();

        assertEquals("u.nickName NOT LIKE 'zhagnsan%' u.email NOT LIKE '%zhagnsan@163.com' AND u.age NOT LIKE '25%'"
                + " AND u.trueAge NOT LIKE '25%' OR u.email NOT LIKE 'zhagnsan@163.com%' OR u.birthday "
                + "NOT LIKE '1990-03-31%'", sqlInfo.getSql());
        assertTrue(sqlInfo.getParams().isEmpty());
    }

    /**
     * BETWEEN 相关方法的测试.
     */
    @Test
    public void testBetween() {
        Integer startAge = (Integer) context.get("startAge");
        Integer endAge = (Integer) context.get("endAge");
        String startBirthday = (String) context.get("startBirthday");
        String endBirthday = (String) context.get("endBirthday");

        SqlInfo sqlInfo = Fenix.start()
                .between("u.age", startAge, endAge)
                .between("u.age", startAge, endAge, startAge == null && endAge == null)
                .between("u.birthday", startBirthday, endBirthday)
                .between("u.birthday", startBirthday, endBirthday, startBirthday != null)
                .andBetween("u.age", startAge, endAge)
                .andBetween("u.age", startAge, endAge, startAge != null && endAge != null)
                .andBetween("u.birthday", startBirthday, endBirthday)
                .andBetween("u.birthday", startBirthday, endBirthday, startBirthday != null)
                .orBetween("u.age", startAge, endAge)
                .orBetween("u.age", startAge, endAge, startAge != null && endAge != null)
                .orBetween("u.birthday", startBirthday, endBirthday)
                .orBetween("u.birthday", startBirthday, endBirthday, startBirthday != null)
                .end();

        assertEquals("u.age BETWEEN :u_age_start AND :u_age_end u.birthday <= :u_birthday_end AND u.age "
                + "BETWEEN :u_age_start AND :u_age_end AND u.age BETWEEN :u_age_start AND :u_age_end AND "
                + "u.birthday <= :u_birthday_end OR u.age BETWEEN :u_age_start AND :u_age_end OR u.age "
                + "BETWEEN :u_age_start AND :u_age_end OR u.birthday <= :u_birthday_end", sqlInfo.getSql());
        assertEquals(3, sqlInfo.getParams().size());
    }

    /**
     * IN 相关方法的测试.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testIn() {
        Integer[] sexs = (Integer[]) context.get("sexs");
        List<String> citys = (List<String>) context.get("citys");

        SqlInfo sqlInfo = Fenix.start()
                .in("u.sex", sexs)
                .in("u.city", citys)
                .in("u.sex", sexs, sexs != null)
                .in("u.city", citys, citys == null)
                .andIn("u.sex", sexs)
                .andIn("u.city", citys)
                .andIn("u.sex", sexs, sexs != null)
                .andIn("u.city", citys, citys == null)
                .orIn("u.sex", sexs)
                .orIn("u.city", citys)
                .orIn("u.sex", sexs, sexs != null)
                .orIn("u.city", citys, citys == null)
                .end();

        assertEquals("u.sex IN :u_sex u.city IN :u_city u.sex IN :u_sex AND u.sex IN :u_sex "
                + "AND u.city IN :u_city AND u.sex IN :u_sex OR u.sex IN :u_sex OR u.city IN :u_city "
                + "OR u.sex IN :u_sex", sqlInfo.getSql());
        assertEquals(2, sqlInfo.getParams().size());
    }

    /**
     * NOT IN 相关方法的测试.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testNotIn() {
        Integer[] sexs = (Integer[]) context.get("sexs");
        List<String> citys = (List<String>) context.get("citys");

        SqlInfo sqlInfo = Fenix.start()
                .notIn("u.sex", sexs)
                .notIn("u.city", citys)
                .notIn("u.sex", sexs, sexs != null)
                .notIn("u.city", citys, citys == null)
                .andNotIn("u.sex", sexs)
                .andNotIn("u.city", citys)
                .andNotIn("u.sex", sexs, sexs != null)
                .andNotIn("u.city", citys, citys == null)
                .orNotIn("u.sex", sexs)
                .orNotIn("u.city", citys)
                .orNotIn("u.sex", sexs, sexs != null)
                .orNotIn("u.city", citys, citys == null)
                .end();

        assertEquals("u.sex NOT IN :u_sex u.city NOT IN :u_city u.sex NOT IN :u_sex AND u.sex NOT IN :u_sex "
                + "AND u.city NOT IN :u_city AND u.sex NOT IN :u_sex OR u.sex NOT IN :u_sex OR u.city NOT "
                + "IN :u_city OR u.sex NOT IN :u_sex", sqlInfo.getSql());
        assertEquals(2, sqlInfo.getParams().size());
    }

    /**
     * IS NULL 相关方法的测试.
     */
    @Test
    public void testIsNull() {
        SqlInfo sqlInfo = Fenix.start()
                .isNull("a.name")
                .isNull("b.email")
                .isNull("a.name", true)
                .isNull("b.email", false)
                .andIsNull("a.name")
                .andIsNull("b.email")
                .andIsNull("a.name", false)
                .andIsNull("b.email", true)
                .orIsNull("a.name")
                .orIsNull("b.email")
                .orIsNull("a.name", false)
                .orIsNull("b.email", true)
                .end();

        assertEquals("a.name IS NULL b.email IS NULL a.name IS NULL AND a.name IS NULL AND b.email IS NULL "
                + "AND b.email IS NULL OR a.name IS NULL OR b.email IS NULL OR b.email IS NULL", sqlInfo.getSql());
        assertTrue(sqlInfo.getParams().isEmpty());
    }

    /**
     * IS NOT NULL 相关方法的测试.
     */
    @Test
    public void testIsNotNull() {
        SqlInfo sqlInfo = Fenix.start()
                .isNotNull("a.name")
                .isNotNull("b.email")
                .isNotNull("a.name", true)
                .isNotNull("b.email", false)
                .andIsNotNull("a.name")
                .andIsNotNull("b.email")
                .andIsNotNull("a.name", false)
                .andIsNotNull("b.email", true)
                .orIsNotNull("a.name")
                .orIsNotNull("b.email")
                .orIsNotNull("a.name", false)
                .orIsNotNull("b.email", true)
                .end();

        assertEquals("a.name IS NOT NULL b.email IS NOT NULL a.name IS NOT NULL AND a.name IS NOT NULL "
                + "AND b.email IS NOT NULL AND b.email IS NOT NULL OR a.name IS NOT NULL OR b.email IS NOT NULL "
                + "OR b.email IS NOT NULL", sqlInfo.getSql());
        assertTrue(sqlInfo.getParams().isEmpty());
    }

    /**
     * 综合测试使用 Fenix 书写的 SQL.
     */
    @Test
    public void testSql() {
        String userName = "zhang";
        String startBirthday = "1990-03-25";
        String endBirthday = "2010-08-28";
        Integer[] sexs = new Integer[] {0, 1};

        SqlInfo sqlInfo = Fenix.start()
                .select("u.id, u.name, u.email, d.birthday, d.address")
                .from("User AS u")
                .leftJoin("UserDetail AS d").on("u.id = d.userId")
                .where("u.id != ''")
                .andLike("u.name", userName, StringHelper.isNotBlank(userName))
                .doAny((join, params) -> {
                    join.append(" AND 1 = 1");
                    params.put("abc", 5);
                    log.info("执行了自定义操作，你可以任意拼接 SQL 字符串和命名参数.");
                })
                .doAny(false, (join, params) -> log.info("match 为 false，将不会执行该自定义操作."))
                .andGreaterThan("u.age", 21)
                .andLessThan("u.age", 13)
                .andGreaterThanEqual("d.birthday", startBirthday)
                .andLessThanEqual("d.birthday", endBirthday)
                .andBetween("d.birthday", startBirthday, endBirthday)
                .andIn("u.sex", sexs)
                .andIsNotNull("u.state")
                .orderBy("d.birthday").desc()
                .end();

        assertEquals("SELECT u.id, u.name, u.email, d.birthday, d.address FROM User AS u "
                + "LEFT JOIN UserDetail AS d ON u.id = d.userId WHERE u.id != '' AND u.name LIKE :u_name "
                + "AND 1 = 1 AND u.age > :u_age AND u.age < :u_age AND d.birthday >= :d_birthday "
                + "AND d.birthday <= :d_birthday AND d.birthday BETWEEN :d_birthday_start AND :d_birthday_end "
                + "AND u.sex IN :u_sex AND u.state IS NOT NULL ORDER BY d.birthday DESC", sqlInfo.getSql());
        assertEquals(7, sqlInfo.getParams().size());
    }

    /**
     * where 动态方法的测试.
     */
    @Test
    public void testWhere() {
        SqlInfo sqlInfo = Fenix.start()
                .select("u")
                .from("User")
                .where(fenix ->
                        fenix.andEqual("u.id", context.get("id"), context.get("id") != null)
                                .andLike("u.nickName", context.get("name"), context.get("name") != null)
                                .andLike("u.email", context.get("email"), context.get("email") != null))
                .andIn("u.sex", (Object[]) context.get("sexs"), context.get("sexs") != null)
                .orderBy("u.updateTime").desc()
                .end();

        assertEquals("SELECT u FROM User WHERE u.id = :u_id AND u.nickName LIKE :u_nickName AND "
                + "u.sex IN :u_sex ORDER BY u.updateTime DESC", sqlInfo.getSql());
        assertEquals(3, sqlInfo.getParams().size());
    }

    /**
     * where 动态方法的测试，测试动态条件都不成立的情况.
     */
    @Test
    public void testWhere2() {
        SqlInfo sqlInfo = Fenix.start()
                .select("u")
                .from("User")
                .whereDynamic()
                .andEqual("u.id", context.get("id"), context.get("id_a") != null)
                .andLike("u.nickName", context.get("name"), context.get("name_b") != null)
                .orderBy("u.updateTime").desc()
                .end();

        assertEquals("SELECT u FROM User ORDER BY u.updateTime DESC", sqlInfo.getSql());
        assertEquals(0, sqlInfo.getParams().size());
    }

    /**
     * where 动态方法的测试，测试前缀为存文本的情况，是否能正常处理.
     */
    @Test
    public void testWhere3() {
        SqlInfo sqlInfo = Fenix.start()
                .select("u")
                .from("User")
                .whereDynamic()
                .andEqual("u.id", context.get("id"), context.get("id_a") != null)
                .andLike("u.nickName", context.get("name"), context.get("name") != null)
                .andIn("u.sex", (Object[]) context.get("sexs"), context.get("sexs") != null)
                .orderBy("u.updateTime").desc()
                .end();

        assertEquals("SELECT u FROM User WHERE u.nickName LIKE :u_nickName "
                + "AND u.sex IN :u_sex ORDER BY u.updateTime DESC", sqlInfo.getSql());
        assertEquals(2, sqlInfo.getParams().size());
    }

}
