package com.blinkfox.fenix.repository;

import com.alibaba.fastjson.JSON;
import com.blinkfox.fenix.FenixTestApplication;
import com.blinkfox.fenix.config.FenixConfig;
import com.blinkfox.fenix.config.FenixConfigManager;
import com.blinkfox.fenix.entity.User;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.FileCopyUtils;

/**
 * 用来单元测试各个 Fenix 标签的数据库执行情况的测试类.
 *
 * @author blinkfox on 2019-08-08.
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = FenixTestApplication.class)
public class UnitTestRepositoryTest {

    /**
     * 是否加载过的标识.
     */
    @Setter
    private static Boolean isLoad = false;

    @Autowired
    private UnitTestRepository unitTestRepository;

    @Value("/data/unit.json")
    private Resource unitResource;

    /**
     * 初始化 Fenix 配置信息，并从资源文件的中读取数据，初始化保存起来，便于后续读取或操作.
     */
    @PostConstruct
    public void init() throws IOException {
        if (!isLoad) {
            FenixConfigManager.getInstance().initLoad(new FenixConfig());
            unitTestRepository.saveAll(
                    JSON.parseArray(new String(FileCopyUtils.copyToByteArray(unitResource.getFile())), User.class));

            // 验证读取的数据条数是否正确.
            Assert.assertEquals(10, unitTestRepository.findAll().size());
            setIsLoad(true);
        }
    }

    /**
     * 测试 UnitTestRepository.testEqual 中 XML SQL 的执行情况.
     */
    @Test
    public void testEqual() {
        List<User> users1 = unitTestRepository.testEqual(new User().setId("2"), "");
        Assert.assertEquals(1, users1.size());

        List<User> users2 = unitTestRepository.testEqual(new User().setId("2"), "1");
        Assert.assertEquals(2, users2.size());
    }

    /**
     * 测试 UnitTestRepository.testNotEqual 中 XML SQL 的执行情况.
     */
    @Test
    public void testNotEqual() {
        List<User> users = unitTestRepository.testNotEqual(new User().setAge(23).setStatus("0").setName("name-姓名-5"));
        Assert.assertEquals(3, users.size());
    }

    /**
     * 测试 UnitTestRepository.testLessThanEqual 中 XML SQL 的执行情况.
     */
    @Test
    public void testLessThanEqual() {
        List<User> users = unitTestRepository.testLessThanEqual(new User().setId("1").setAge(28));
        Assert.assertEquals(7, users.size());
    }

    /**
     * 测试 UnitTestRepository.testLike 中 XML SQL 的执行情况.
     */
    @Test
    public void testLike() {
        List<User> users = unitTestRepository.testLike(new User().setAge(25).setName("姓名").setSex("sex-性别-9"));
        Assert.assertEquals(4, users.size());
    }

    /**
     * 测试 UnitTestRepository.testStartsWith 中 XML SQL 的执行情况.
     */
    @Test
    public void testStartsWith() {
        List<User> users = unitTestRepository.testStartsWith(new User().setName("name-姓").setSex("sex-性别"));
        Assert.assertEquals(1, users.size());
    }

    /**
     * 测试 UnitTestRepository.testEndsWith 中 XML SQL 的执行情况.
     */
    @Test
    public void testEndsWith() {
        List<User> users = unitTestRepository.testEndsWith(new User().setName("名-8").setEmail("@qq.com"));
        Assert.assertEquals(2, users.size());
    }

    /**
     * 测试 UnitTestRepository.testBetween 中 XML SQL 的执行情况.
     */
    @Test
    public void testBetween() {
        // 设置开始和结束的年龄.
        Map<String, Object> context = new HashMap<>(8);
        context.put("startAge", 22);
        context.put("endAge", 28);

        // 仅设置开始日期.
        Calendar c = Calendar.getInstance();
        c.set(1991, Calendar.FEBRUARY, 1);
        context.put("startBirthday", c.getTime());

        Assert.assertEquals(4, unitTestRepository.testBetween(context).size());
    }

    /**
     * 测试 UnitTestRepository.testIn 中 XML SQL 的执行情况.
     */
    @Test
    public void testIn() {
        // 设置开始和结束的年龄.
        Map<String, Object> context = new HashMap<>(8);
        context.put("ids", new String[] {"1", "3", "5", "7", "9"});

        Set<String> names = new HashSet<>(4);
        names.add("name-姓名-1");
        names.add("name-姓名-3");
        names.add("name-姓名-5");
        context.put("names", names);

        context.put("sexs", "sex-性别-1");
        Assert.assertEquals(2, unitTestRepository.testIn(context).size());
    }

    /**
     * 测试 UnitTestRepository.testIsNull 中 XML SQL 的执行情况.
     */
    @Test
    public void testIsNull() {
        Assert.assertEquals(1, unitTestRepository.testIsNull(new User().setId("4")).size());
    }

    /**
     * 测试 UnitTestRepository.testText 中 XML SQL 的执行情况.
     */
    @Test
    public void testText() {
        // 设置开始和结束的年龄.
        Map<String, Object> context = new HashMap<>(8);
        context.put("ids", new String[] {"1", "3", "5", "7", "9"});
        Assert.assertEquals(4, unitTestRepository.testText(context, new User().setName("姓名").setEmail("1")).size());
    }

}
