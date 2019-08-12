package com.blinkfox.fenix;

import static org.junit.Assert.assertEquals;

import com.blinkfox.fenix.bean.SqlInfo;
import com.blinkfox.fenix.core.Fenix;
import com.blinkfox.fenix.helper.ParamWrapper;

import java.util.Arrays;
import java.util.HashMap;
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
                .where("u.id = :u_id", "u_id","3")
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
                .text("u.id = :id ").param("id", 5)
                .text("and u.nickName like :nickName ").param("nickName", "lisi")
                .text("and u.sex in :sex ").param("sex", new Integer[]{2, 3, 4})
                .text("and u.city in :city ", "city", context.get("citys"))
                .end();

        assertEquals("select u.id, u.nickName from User as u where u.id = :id and u.nickName like :nickName "
                + "and u.sex in :sex and u.city in :city", sqlInfo.getSql());
        assertEquals(4, sqlInfo.getParams().size());
    }

    /**
     * equal相关方法测试.
     */
    @Test
    public void testEqual() {
        long start = System.currentTimeMillis();
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
        log.info("testEqual() 方法执行耗时: {} ms.", System.currentTimeMillis() - start);

        assertEquals("u.nickName = :u_nickName u.email = :u_email AND u.age = :u_age AND u.trueAge = "
                + ":u_trueAge AND u.trueAge = :u_trueAge u.nickName = :u_nickName OR u.email = :u_email "
                + "OR u.birthday = :u_birthday OR u.birthday = :u_birthday u.id = :u_id", sqlInfo.getSql());
        assertEquals(6, sqlInfo.getParams().size());
    }

}
