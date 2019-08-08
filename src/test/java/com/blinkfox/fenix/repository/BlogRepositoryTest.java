package com.blinkfox.fenix.repository;

import com.alibaba.fastjson.JSON;
import com.blinkfox.fenix.FenixTestApplication;
import com.blinkfox.fenix.config.FenixConfig;
import com.blinkfox.fenix.config.FenixConfigManager;
import com.blinkfox.fenix.entity.Blog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;

import com.blinkfox.fenix.jpa.QueryFenix;
import lombok.extern.slf4j.Slf4j;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
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

    @Autowired
    private BlogRepository blogRepository;

    @Value("/data/blog.json")
    private Resource blogResource;

    /**
     * 初始化 Fenix 配置信息，并从资源文件的中读取数据，初始化保存起来，便于后续读取或操作.
     */
    @PostConstruct
    public void init() throws IOException {
        FenixConfigManager.getInstance().initLoad(new FenixConfig());
        blogRepository.saveAll(
                JSON.parseArray(new String(FileCopyUtils.copyToByteArray(blogResource.getFile())), Blog.class));
    }

    /**
     * 测试使用原生的 {@link Query} 注解来模糊查询博客信息.
     */
    @Test
    public void queryBlogsByTitle() {
        List<Blog> blogs = blogRepository.queryBlogsByTitle("%Spring%");
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
        List<String> ids = Arrays.asList("1", "2", "3", "9", "10");
        List<Blog> blogs = blogRepository.queryBlogs2(ids, new Blog().setAuthor("张三"));
        Assert.assertFalse(blogs.isEmpty());
    }

}
