package com.blinkfox.fenix.repository;

import com.alibaba.fastjson.JSON;
import com.blinkfox.fenix.FenixTestApplication;
import com.blinkfox.fenix.config.FenixConfig;
import com.blinkfox.fenix.config.FenixConfigManager;
import com.blinkfox.fenix.entity.Blog;
import com.blinkfox.fenix.entity.User;
import com.blinkfox.fenix.jpa.QueryFenix;
import com.blinkfox.fenix.vo.UserBlogInfo;
import com.blinkfox.fenix.vo.UserBlogProjection;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.FileCopyUtils;

/**
 * BlogRepository Test.
 *
 * @author blinkfox on 2019-08-04.
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = FenixTestApplication.class)
public class BlogRepositoryTest {

    private static final String SPRING = "%Spring%";

    /**
     * 是否加载过的标识.
     */
    @Setter
    private static Boolean isLoad = false;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private UserRepository userRepository;

    @Value("/data/blog.json")
    private Resource blogResource;

    @Value("/data/user.json")
    private Resource userResource;

    /**
     * 初始化 Fenix 配置信息，并从资源文件的中读取数据，初始化保存起来，便于后续读取或操作.
     */
    @PostConstruct
    public void init() throws IOException {
        if (!isLoad) {
            FenixConfigManager.getInstance().initLoad(new FenixConfig());
            blogRepository.saveAll(
                    JSON.parseArray(new String(FileCopyUtils.copyToByteArray(blogResource.getFile())), Blog.class));
            userRepository.saveAll(
                    JSON.parseArray(new String(FileCopyUtils.copyToByteArray(userResource.getFile())), User.class));
            setIsLoad(true);
        }
    }

    /**
     * 测试使用原生的 {@link Query} 注解来模糊查询博客信息.
     */
    @Test
    public void queryBlogsByTitle() {
        List<Blog> blogs = blogRepository.queryBlogsByTitle(new String[] {"1", "2", "3", "9", "10"}, SPRING);
        Assert.assertFalse(blogs.isEmpty());
    }

    /**
     * 测试使用原生的 {@link Query} 注解来模糊查询博客信息.
     */
    @Test
    public void queryBlogsByIds() {
        Page<Blog> blogs = blogRepository.queryBlogsByIds(new String[] {"1", "2", "3", "5", "7"},
                PageRequest.of(1, 3, Sort.by(Sort.Order.asc("createTime"), Sort.Order.desc("id"))));
        Assert.assertFalse(blogs.isEmpty());
    }

    /**
     * 测试使用 {@link QueryFenix} 注解根据博客的实体 VO 类来查询博客信息.
     */
    @Test
    public void querySimplyDemo() {
        List<Blog> blogs = blogRepository.querySimplyDemo(new Blog().setId("1"));
        Assert.assertFalse(blogs.isEmpty());
    }

    /**
     * 测试使用 {@link QueryFenix} 注解根据博客的实体 VO 类和其他参数来查询博客信息.
     */
    @Test
    public void queryBlogs2() {
        Page<Blog> blogs = blogRepository.queryBlogs2(new String[] {"1", "2", "3", "9", "10"},
                new Blog().setAuthor("张三").setTitle("Spring").setUpdateTime(new Date()),
                PageRequest.of(0, 1, Sort.by(Sort.Order.asc("createTime"), Sort.Order.desc("id"))));
        Assert.assertFalse(blogs.isEmpty());
    }

    /**
     * 测试使用原生的 {@link Query} 注解来模糊查询用户博客信息.
     */
    @Test
    public void queryUserBlogsByTitle() {
        List<UserBlogInfo> blogs = blogRepository.queryUserBlogsByTitle("1", SPRING);
        Assert.assertFalse(blogs.isEmpty());
    }

    /**
     * 测试使用 {@link QueryFenix} 注解来模糊查询用户博客信息.
     */
    @Test
    public void queryUserBlogsByTitleWithFenix() {
        List<UserBlogInfo> blogs = blogRepository.queryUserBlogsByTitleWithFenix("1",
                new Blog().setTitle(SPRING).setContent("一"));
        Assert.assertFalse(blogs.isEmpty());
    }

    /**
     * 测试使用 {@link QueryFenix} 注解来模糊查询用户博客信息到自定义的投影接口 {@link com.blinkfox.fenix.vo.UserBlogProjection} 中.
     */
    @Test
    public void queryUserBlogsByProjection() {
        List<UserBlogProjection> blogs = blogRepository.queryUserBlogsByProjection("1",
                new Blog().setTitle(SPRING).setContent("一"));
        Assert.assertFalse(blogs.isEmpty());
        Assert.assertNotNull(blogs.get(0).getDescription());
    }

    /**
     * 测试使用 {@link QueryFenix} 注解来模糊查询用户博客信息到自定义的投影接口 {@link com.blinkfox.fenix.vo.UserBlogProjection} 中.
     */
    @Test
    public void queryNativeByProjection() {
        List<UserBlogProjection> blogs = blogRepository.queryNativeByProjection("1", SPRING);
        Assert.assertFalse(blogs.isEmpty());
        Assert.assertNotNull(blogs.get(0).getDescription());
    }

    /**
     * 测试使用 {@link QueryFenix} 注解来模糊查询用户博客信息到自定义的投影接口 {@link com.blinkfox.fenix.vo.UserBlogProjection} 中.
     */
    @Test
    public void queryFenixNativeByProjection() {
        Page<UserBlogProjection> blogs = blogRepository.queryFenixNativeByProjection("1",
                new Blog().setTitle(SPRING).setContent("一"),
                PageRequest.of(0, 3, Sort.by(Sort.Order.asc("dt_create_time"), Sort.Order.desc("c_id"))));
        Assert.assertFalse(blogs.isEmpty());
        Assert.assertNotNull(blogs.getContent().get(0).getDescription());
    }

}
