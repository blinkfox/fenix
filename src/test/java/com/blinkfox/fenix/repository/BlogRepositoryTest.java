package com.blinkfox.fenix.repository;

import com.alibaba.fastjson.JSON;
import com.blinkfox.fenix.FenixTestApplication;
import com.blinkfox.fenix.config.FenixConfig;
import com.blinkfox.fenix.config.FenixConfigManager;
import com.blinkfox.fenix.entity.Blog;
import com.blinkfox.fenix.entity.User;
import com.blinkfox.fenix.handler.HelloTagHandler;
import com.blinkfox.fenix.helper.StringHelper;
import com.blinkfox.fenix.jpa.QueryFenix;
import com.blinkfox.fenix.vo.UserBlogDto;
import com.blinkfox.fenix.vo.UserBlogInfo;
import com.blinkfox.fenix.vo.UserBlogProjection;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
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
import org.springframework.data.domain.Pageable;
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
            // 初始化定义 FenixConfig 的实例和一些配置.
            final FenixConfig myFenixConfig = new FenixConfig().setPrintSqlInfo(false);
            FenixConfig.add("hi", HelloTagHandler.class);
            FenixConfig.add("andHi", " AND ", HelloTagHandler::new, " LIKE ");

            // 下面可以随便写一些带前缀或者标志符的.
            FenixConfig.add("andHello", " AND ", HelloTagHandler.class);
            FenixConfig.add("hello", HelloTagHandler.class, "=");
            FenixConfig.add("orHello", " OR ", HelloTagHandler.class, "=");

            FenixConfigManager.getInstance().initLoad(myFenixConfig);
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
        Assert.assertTrue(StringHelper.isNotBlank(blogs.get(0).getTitle()));
    }

    /**
     * 测试使用原生的 {@link Query} 注解来模糊查询博客信息.
     */
    @Test
    public void queryBlogsByIds() {
        Page<Blog> blogPage = blogRepository.queryBlogsByIds(new String[] {"1", "2", "3", "5", "7"},
                PageRequest.of(1, 3, Sort.by(Sort.Order.asc("createTime"), Sort.Order.desc("id"))));
        Assert.assertFalse(blogPage.isEmpty());
        Assert.assertTrue(StringHelper.isNotBlank(blogPage.getContent().get(0).getTitle()));
    }

    /**
     * 测试使用 {@link QueryFenix} 注解根据博客的实体 VO 类来查询博客信息.
     */
    @Test
    public void querySimplyDemo() {
        List<Blog> blogs = blogRepository.querySimplyDemo(new Blog().setId("1"));
        Assert.assertFalse(blogs.isEmpty());
        Assert.assertTrue(StringHelper.isNotBlank(blogs.get(0).getTitle()));
    }

    /**
     * 测试使用 {@link QueryFenix} 注解根据博客的实体 VO 类和其他参数来查询博客信息.
     */
    @Test
    public void queryBlogs2() {
        Page<Blog> blogPage = blogRepository.queryBlogs2(new String[] {"1", "2", "3", "9", "10"},
                new Blog().setAuthor("ZhangSan").setTitle("Spring").setUpdateTime(new Date()),
                PageRequest.of(0, 1, Sort.by(Sort.Order.asc("createTime"), Sort.Order.desc("id"))));
        Assert.assertFalse(blogPage.isEmpty());
        Assert.assertTrue(StringHelper.isNotBlank(blogPage.getContent().get(0).getTitle()));
    }

    /**
     * 测试使用原生的 {@link Query} 注解来模糊查询用户博客信息.
     */
    @Test
    public void queryUserBlogsByTitle() {
        List<UserBlogInfo> userBlogs = blogRepository.queryUserBlogsByTitle("1", SPRING);
        Assert.assertFalse(userBlogs.isEmpty());
        Assert.assertTrue(StringHelper.isNotBlank(userBlogs.get(0).getName()));
    }

    /**
     * 测试使用 {@link QueryFenix} 注解和 Java 拼接 SQL 的方式来模糊查询并得到自定义的用户博客实体信息.
     */
    @Test
    public void queryUserBlogsWithFenixJava() {
        List<UserBlogInfo> userBlogs = blogRepository.queryUserBlogsWithFenixJava("1", SPRING);
        Assert.assertFalse(userBlogs.isEmpty());
        Assert.assertTrue(StringHelper.isNotBlank(userBlogs.get(0).getName()));
    }

    /**
     * 测试使用 {@link QueryFenix} 注解来模糊查询自定义的用户博客实体信息.
     */
    @Test
    public void queryUserBlogsWithFenixNative() {
        List<UserBlogInfo> userBlogs = blogRepository.queryUserBlogsWithFenixNative("1", SPRING);
        Assert.assertFalse(userBlogs.isEmpty());
        Assert.assertTrue(StringHelper.isNotBlank(userBlogs.get(0).getName()));
    }

    /**
     * 测试使用 {@link QueryFenix} 注解来模糊查询自定义的用户博客实体分页信息.
     */
    @Test
    public void queryUserBlogPageWithFenixNative() {
        Page<UserBlogInfo> userBlogPage = blogRepository.queryUserBlogPageWithFenixNative("1", SPRING,
                PageRequest.of(0, 3, Sort.by(Sort.Order.desc("c_id"))));
        Assert.assertFalse(userBlogPage.isEmpty());
        Assert.assertTrue(StringHelper.isNotBlank(userBlogPage.getContent().get(0).getName()));
    }

    /**
     * 测试使用 {@link QueryFenix} 注解来模糊查询用户博客信息.
     */
    @Test
    public void queryUserBlogsByTitleWithFenix() {
        List<UserBlogInfo> userBlogs = blogRepository.queryUserBlogsByTitleWithFenix("1",
                new Blog().setTitle(SPRING).setContent("-"));
        Assert.assertFalse(userBlogs.isEmpty());
        Assert.assertTrue(StringHelper.isNotBlank(userBlogs.get(0).getName()));
    }

    /**
     * 测试使用 {@link QueryFenix} 注解来模糊查询用户博客信息.
     */
    @Test
    public void queryUserBlogsWithFenixResultType() {
        List<UserBlogInfo> userBlogs = blogRepository.queryUserBlogsWithFenixResultType("1",
                new Blog().setTitle(SPRING).setContent("-"));
        Assert.assertFalse(userBlogs.isEmpty());
        Assert.assertTrue(StringHelper.isNotBlank(userBlogs.get(0).getName()));
    }

    /**
     * 测试使用 {@link QueryFenix} 注解来模糊查询用户博客信息.
     */
    @Test
    public void queryUserBlogsWithFenixResultType2() {
        List<UserBlogDto> userBlogs = blogRepository.queryUserBlogsWithFenixResultType2("1",
                new Blog().setTitle(SPRING).setContent("-"));
        Assert.assertFalse(userBlogs.isEmpty());
        Assert.assertTrue(StringHelper.isNotBlank(userBlogs.get(0).getName()));
    }

    /**
     * 测试使用 {@link QueryFenix} 注解来模糊查询用户博客信息.
     */
    @Test
    public void queryUserBlogPageWithFenixResultType() {
        Page<UserBlogInfo> userBlogPage = blogRepository.queryUserBlogPageWithFenixResultType("1",
                new Blog().setTitle(SPRING).setContent("-"), PageRequest.of(0, 3, Sort.by(Sort.Order.desc("id"))));
        Assert.assertFalse(userBlogPage.isEmpty());
        Assert.assertTrue(StringHelper.isNotBlank(userBlogPage.getContent().get(0).getName()));
    }

    /**
     * 测试使用 {@link QueryFenix} 注解来模糊查询用户博客信息到自定义的投影接口 {@link com.blinkfox.fenix.vo.UserBlogProjection} 中.
     */
    @Test
    public void queryUserBlogsByProjection() {
        List<UserBlogProjection> blogs = blogRepository.queryUserBlogsByProjection("1",
                new Blog().setTitle(SPRING).setContent("-"));
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
                new Blog().setTitle(SPRING).setContent("-"),
                PageRequest.of(0, 3, Sort.by(Sort.Order.asc("dt_create_time"), Sort.Order.desc("c_id"))));
        Assert.assertFalse(blogs.isEmpty());
        Assert.assertNotNull(blogs.getContent().get(0).getDescription());
    }

    /**
     * 测试使用原生 {@code Query} 注解来模糊查询用户博客信息，以 Map 的形式返回.
     */
    @Test
    public void queryUserBlogMap() {
        List<Map<String, Object>> userBlogMaps = blogRepository.queryUserBlogMap("1", SPRING);
        Assert.assertFalse(userBlogMaps.isEmpty());
        Assert.assertTrue(userBlogMaps.get(0).containsKey(("userId")));
    }

    /**
     * 测试使用原生 {@code Query} 注解和原生 SQL 来模糊查询用户博客信息，以 Map 的形式返回.
     */
    @Test
    public void queryUserBlogMapNative() {
        List<Map<String, Object>> userBlogMaps = blogRepository.queryUserBlogMapNative("1", SPRING);
        Assert.assertFalse(userBlogMaps.isEmpty());
        Assert.assertTrue(userBlogMaps.get(0).containsKey(("userId")));
    }

    /**
     * 测试使用原生 {@code Query} 注解来模糊查询用户博客信息，以 Map 的形式返回.
     */
    @Test
    public void queryUserBlogMapWithFenix() {
        List<Map<String, Object>> userBlogMaps = blogRepository.queryUserBlogMapWithFenix("1",
                new Blog().setTitle(SPRING).setContent("-"));
        Assert.assertFalse(userBlogMaps.isEmpty());
        Assert.assertTrue(userBlogMaps.get(0).containsKey(("content")));
    }

    /**
     * 并发测试使用原生的 {@link Query} 注解来模糊查询博客信息.
     */
    @Test
    public void queryWithConcurrent() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(2);
        new Thread(() -> {
            log.info("Fenix 并发测试【线程1】开始执行 ...");
            for (int i = 0; i < 100; ++i) {
                List<Blog> blogs = blogRepository.querySimplyDemo(new Blog().setId("1"));
                if (blogs.isEmpty()) {
                    log.error("Fenix 并发测试【线程1】得到的结果数据是空的，执行出错了!");
                }
                Assert.assertFalse(blogs.isEmpty());
            }
            log.info("Fenix 并发测试【线程1】执行结束了.");
            countDownLatch.countDown();
        }, "fenix-thread-1").start();

        new Thread(() -> {
            log.info("Fenix 并发测试【线程2】开始执行 ...");
            for (int i = 0; i < 100; ++i) {
                List<Blog> blogs2 = blogRepository.querySimplyDemo(new Blog().setId("0"));
                if (!blogs2.isEmpty()) {
                    log.error("Fenix 并发测试【线程2】数据不是空的，执行断言出错了，数据为：" + blogs2);
                }
                Assert.assertTrue(blogs2.isEmpty());
            }
            log.info("Fenix 并发测试【线程2】执行结束了.");
            countDownLatch.countDown();
        }, "fenix-thread-2").start();

        countDownLatch.await();
        log.info("Fenix 并发测试执行时，所有线程执行正常执行完毕.");
    }

    /**
     * 测试使用 {@link QueryFenix} 注解使用开启 distinct 检测的分页查询.
     */
    @Test
    public void queryBlogsWithDistinct() {
        Page<Blog> blogPage = blogRepository.queryBlogsWithDistinct(PageRequest.of(0, 10));
        Assert.assertFalse(blogPage.isEmpty());
        Assert.assertTrue(blogPage.getTotalElements() == 8);
    }

    /**
     * 测试使用 {@link QueryFenix} 注解使用开启 distinct 检测但是没有 distinct 关键字的分页查询.
     */
    @Test
    public void queryBlogsWithoutDistinct() {
        Page<Blog> blogPage = blogRepository.queryBlogsWithoutDistinct(PageRequest.of(0, 10));
        Assert.assertFalse(blogPage.isEmpty());
        Assert.assertTrue(blogPage.getTotalElements() == 10);
    }

    /**
     * 测试使用 {@link QueryFenix} 注解使用开启 distinct 检测但是没有 distinct 关键字的原生 sql 分页查询.
     */
    @Test
    public void queryBlogsWithoutDistinctNative() {
        Page<Blog> blogPage = blogRepository.queryBlogsWithoutDistinctNative(PageRequest.of(0, 10));
        Assert.assertFalse(blogPage.isEmpty());
        Assert.assertTrue(blogPage.getTotalElements() == 10);
    }

    /**
     * 测试使用 {@link QueryFenix} 注解使用开启 distinct 检测但是没有 distinct 关键字的原生 sql 分页查询.
     */
    @Test
    public void queryBlogsWithDistinctNative() {
        Page<Long> blogPage = blogRepository.queryBlogsWithDistinctNative(PageRequest.of(0, 10));
        Assert.assertFalse(blogPage.isEmpty());
        Assert.assertEquals(8, blogPage.getTotalElements());
    }

    /**
     * 测试使用 {@link QueryFenix} 注解使用开启 distinct 检测但是没有 distinct 关键字的原生 sql 分页查询.
     */
    @Test
    public void queryBlogsWithDistinctNative2() {
        Page<Long> blogPage = blogRepository.queryBlogsWithDistinctNative(Pageable.unpaged());
        Assert.assertFalse(blogPage.isEmpty());
        Assert.assertEquals(8, blogPage.getTotalElements());
    }

    /**
     * 单测执行完毕之后，清空资源.
     */
    @PreDestroy
    public void destroy() {
        FenixConfigManager.getInstance().clear();
    }

}
