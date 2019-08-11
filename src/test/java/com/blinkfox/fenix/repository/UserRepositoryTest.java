package com.blinkfox.fenix.repository;

import com.alibaba.fastjson.JSON;
import com.blinkfox.fenix.FenixTestApplication;
import com.blinkfox.fenix.config.FenixConfig;
import com.blinkfox.fenix.config.FenixConfigManager;
import com.blinkfox.fenix.entity.User;
import com.blinkfox.fenix.jpa.QueryFenix;

import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.FileCopyUtils;

/**
 * UserRepositoryTest Test.
 *
 * @author blinkfox on 2019-08-04.
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = FenixTestApplication.class)
public class UserRepositoryTest {

    /**
     * 是否加载过的标识.
     */
    @Setter
    private static Boolean isLoad = false;

    @Autowired
    private UserRepository userRepository;


    @Value("/data/user.json")
    private Resource userResource;

    /**
     * 初始化 Fenix 配置信息，并从资源文件的中读取数据，初始化保存起来，便于后续读取或操作.
     */
    @PostConstruct
    public void init() throws IOException {
        if (!isLoad) {
            FenixConfigManager.getInstance()
                    .initLoadHandlerLocations("com.blinkfox.fenix.handler")
                    .initLoad(new FenixConfig());
            userRepository.saveAll(
                    JSON.parseArray(new String(FileCopyUtils.copyToByteArray(userResource.getFile())), User.class));
            setIsLoad(true);
        }
    }

    /**
     * 测试使用 {@link com.blinkfox.fenix.jpa.QueryFenix} 注解来模糊查询博客信息.
     */
    @Test
    public void queryBlogsByTitle() {
        List<User> users = userRepository.queryUserWithIdEmail("1", "email");
        Assert.assertFalse(users.isEmpty());

        List<User> users2 = userRepository.queryUserWithIdEmail(null, "@qq.com");
        Assert.assertFalse(users2.isEmpty());
    }

    /**
     * 测试使用 {@link QueryFenix} 注解和 Java 拼接 SQL 的方式来根据用户 ID 和 Email 来查询用户信息.
     */
    @Test
    @Ignore
    public void queryWithJava() {
        List<User> users = userRepository.queryWithJava("1", new User().setId("2").setAge(32), 5, "email");
        Assert.assertFalse(users.isEmpty());
    }

}
