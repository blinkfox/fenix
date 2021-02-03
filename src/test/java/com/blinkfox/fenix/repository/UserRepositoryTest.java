package com.blinkfox.fenix.repository;

import com.alibaba.fastjson.JSON;
import com.blinkfox.fenix.FenixTestApplication;
import com.blinkfox.fenix.config.FenixConfig;
import com.blinkfox.fenix.config.FenixConfigManager;
import com.blinkfox.fenix.entity.User;
import com.blinkfox.fenix.jpa.QueryFenix;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.Setter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.FileCopyUtils;

/**
 * UserRepository Test.
 *
 * @author blinkfox on 2019-08-04.
 */
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
            FenixConfigManager.getInstance().initLoad(new FenixConfig()
                    .setDebug(true)
                    .setPrintSqlInfo(true)
                    .setXmlLocations("my, fenix ,  , others/fenix-xml.xml , abc, def/ghi")
                    .setHandlerLocations("com.blinkfox.fenix.handler, , "));

            userRepository.saveAll(
                    JSON.parseArray(new String(FileCopyUtils.copyToByteArray(userResource.getFile())), User.class));
            List<User> users = userRepository.findAll();
            System.out.println("This is all Users info:" + users);
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
    public void queryUsersWithJava() {
        List<User> users = userRepository.queryUsersWithJava("5", new User().setId("2").setAge(32), 23, "qq");
        Assert.assertFalse(users.isEmpty());

        List<User> users2 = userRepository.queryUsersWithJava2("5", new User().setId("2").setAge(32), 23, "qq");
        Assert.assertFalse(users2.isEmpty());
    }

    /**
     * 测试使用 {@link QueryFenix} 注解没有任何元数据参数时，使用默认约定来查询用户信息的方法.
     */
    @Test
    public void queryUsersWithSameName() {
        Map<String, Object> userMap = new HashMap<>(2);
        userMap.put("ids", new String[] {"2", "4", "6", "8", "10"});
        List<User> users = userRepository.queryUsersWithSameName(userMap, new User().setName("name-"));
        Assert.assertFalse(users.isEmpty());
    }

    /**
     * 测试使用 {@link QueryFenix} 注解没有任何元数据参数时，使用默认约定来查询用户信息的方法.
     */
    @Test
    public void queryUserByIds() {
        Map<String, Object> userMap = new HashMap<>(2);
        userMap.put("ids", new String[] {"2", "4", "6", "8", "10"});
        Page<User> userPage = userRepository.queryUserByIds(userMap,
                PageRequest.of(0, 3, Sort.by(Sort.Order.desc("id"))));
        Assert.assertFalse(userPage.getContent().isEmpty());
    }

    /**
     * 测试使用 {@link QueryFenix} 注解没有任何元数据参数时，使用默认约定来查询用户信息的方法.
     */
    @Test
    public void queryUserByIdsWithUnPage() {
        Map<String, Object> userMap = new HashMap<>(2);
        userMap.put("ids", new String[] {"2", "4", "6", "8", "10"});
        Page<User> userPage = userRepository.queryUserByIds(userMap, Pageable.unpaged());
        Assert.assertFalse(userPage.getContent().isEmpty());
    }

    /**
     * 测试使用 {@link QueryFenix} 来根据名称分页查询其它目录下的 xml 文件的数据..
     */
    @Test
    public void queryUsersByName() {
        List<User> users = userRepository.queryUsersByName(new User()
                .setName("name-").setEmail("qq").setAge(22).setStatus("0"));
        Assert.assertFalse(users.isEmpty());
    }

}
