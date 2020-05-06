package com.blinkfox.fenix.core;

import com.blinkfox.fenix.config.FenixConfig;
import com.blinkfox.fenix.config.FenixConfigManager;
import com.blinkfox.fenix.entity.Blog;
import com.blinkfox.fenix.helper.StringHelper;
import com.blinkfox.stalker.Stalker;
import com.blinkfox.stalker.config.Options;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Fenix XML 拼接的性能测试类.
 *
 * @author blinkfox on 2019-08-08.
 */
public class FenixPerformanceTest {

    /**
     * 上下文参数.
     */
    private static Map<String, Object> context;

    /**
     * 初始化上下文参数.
     */
    @BeforeClass
    public static void init() {
        // 初始化 Fenix 配置信息.
        FenixConfigManager.getInstance()
                .initLoad(new FenixConfig().setPrintBanner(true).setPrintSqlInfo(false));

        // 设置上下文参数.
        context = new HashMap<>(4);
        context.put("ids", Arrays.asList("1", "2", "3", "9", "10"));
        context.put("blog", new Blog().setAuthor("张三").setTitle("Spring").setUpdateTime(new Date()));
    }

    /**
     * 测似 Fenix 读取和解析 XML 并拼接 SQL 语句和参数的性能.
     */
    @Test
    public void getXmlSqlInfo() {
        Runnable r = () -> Assert.assertNotNull(Fenix.getXmlSqlInfo("BlogRepository.queryBlogs2", context));

        // 预热 10 次，单线程运行一万次.
        Stalker.run(Options.of(10000).warmups(10), r);
        // 预热 10 次，1000个线程、50并发运行.
        Stalker.run(Options.of(1000, 50).warmups(10), r);
    }

    /**
     * 测似 Fenix 通过存 Java 拼接 SQL 语句和参数的性能.
     */
    @Test
    public void getSqlInfo() {
        Blog blog = (Blog) context.get("blog");
        Runnable r = () -> Assert.assertNotNull(Fenix.start()
                .select("b")
                .from("Blog").as("b")
                .where("b.id IN :ids", "ids", context.get("ids"))
                .andEqual("b.author", blog.getAuthor(), StringHelper.isNotBlank(blog.getAuthor()))
                .end());

        // 预热 10 次，单线程运行一万次.
        Stalker.run(Options.of(10000).warmups(10), r);
        // 预热 10 次，1000个线程、50并发运行.
        Stalker.run(Options.of(1000, 50).warmups(10), r);
    }

}
