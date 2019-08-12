package com.blinkfox.fenix.core;

import com.blinkfox.fenix.config.FenixConfigManager;
import com.blinkfox.fenix.config.MyFenixConfig;
import com.blinkfox.fenix.entity.Blog;
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
 * Fenix Test.
 *
 * @author blinkfox on 2019-08-08.
 */
public class FenixTest {

    /**
     * 初始化上下文参数.
     */
    @BeforeClass
    public static void init() {
        FenixConfigManager.getInstance().initLoad(new MyFenixConfig());
    }

    /**
     * 测似 Fenix 的性能.
     */
    @Test
    public void getSqlInfo() {
        Map<String, Object> context = new HashMap<>(4);
        context.put("ids", Arrays.asList("1", "2", "3", "9", "10"));
        context.put("blog", new Blog().setAuthor("张三").setTitle("Spring").setUpdateTime(new Date()));
        Stalker.run(Options.of(5).warmups(5),
                () -> Assert.assertNotNull(Fenix.getXmlSqlInfo("BlogRepository.queryBlogs2", context)));
    }

}
