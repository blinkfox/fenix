package com.blinkfox.fenix.repository;

import com.alibaba.fastjson.JSON;
import com.blinkfox.fenix.FenixTestApplication;
import com.blinkfox.fenix.config.FenixConfig;
import com.blinkfox.fenix.config.FenixConfigManager;
import com.blinkfox.fenix.entity.User;

import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;

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

    @Autowired
    private UnitTestRepository unitTestRepository;

    @Value("/data/unit.json")
    private Resource unitResource;

    /**
     * 初始化 Fenix 配置信息，并从资源文件的中读取数据，初始化保存起来，便于后续读取或操作.
     */
    @PostConstruct
    public void init() throws IOException {
        FenixConfigManager.getInstance().initLoad(new FenixConfig());
        unitTestRepository.saveAll(
                JSON.parseArray(new String(FileCopyUtils.copyToByteArray(unitResource.getFile())), User.class));

        // 验证读取的数据条数是否正确.
        Assert.assertEquals(10, unitTestRepository.findAll().size());
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
     * 测试 UnitTestRepository.testEqual2 中 XML SQL 的执行情况.
     */
    @Test
    public void testEqual2() {
        List<User> users1 = unitTestRepository.testEqual2(new User().setAge(23).setStatus("0").setName("name-姓名-5"));
        Assert.assertEquals(3, users1.size());
    }

}
