package com.blinkfox.fenix.repository;

import com.blinkfox.fenix.FenixTestApplication;
import com.blinkfox.fenix.config.FenixConfig;
import com.blinkfox.fenix.config.FenixConfigManager;
import com.blinkfox.fenix.entity.Blog;

import java.util.Date;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * BlogRepositoryTest.
 *
 * @author blinkfox on 2019-08-04.
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = FenixTestApplication.class)
public class BlogRepositoryTest {

    @Autowired
    private BlogRepository blogRepository;

    /**
     * 初始化上下文参数.
     */
    @BeforeClass
    public static void init() {
        FenixConfigManager.getInstance().initLoad(new FenixConfig());
    }

    /**
     * 测试根据标题模糊查询博客信息.
     */
    @Test
    public void queryBlogsByTitle() {
        Date date = new Date();
        blogRepository.save(new Blog()
                .setId("1")
                .setTitle("SpringBoot 实战")
                .setAuthor("张三")
                .setContent("这是内容博客内容!")
                .setCreateTime(date)
                .setUpdateTime(date));

        List<Blog> blogs = blogRepository.queryBlogsByTitle("%Spring%");
        Assert.assertEquals(1, blogs.size());
        Assert.assertEquals("张三", blogs.get(0).getAuthor());

        List<Blog> myBlogs = blogRepository.queryMyBlogs(new Blog().setId("1"));
        log.info("我的博客信息：【{}】.", myBlogs);
    }

}
