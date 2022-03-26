package com.blinkfox.fenix.repository.transformer;

import com.blinkfox.fenix.FenixTestApplication;
import com.blinkfox.fenix.config.FenixConfigManager;
import com.blinkfox.fenix.entity.transformer.PrefixUnderscore;
import com.blinkfox.fenix.vo.transformer.PrefixUnderscoreVo;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * PrefixUnderscoreRepository 的单元测试类.
 *
 * @author blinkfox on 2022-03-26.
 * @since v2.7.0
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = FenixTestApplication.class)
public class PrefixUnderscoreRepositoryTest {

    private static final String COLUMN_VALUE = "prefix underscore column value, index is:";

    @Resource
    private PrefixUnderscoreRepository prefixUnderscoreRepository;

    /**
     * 是否加载过的标识.
     */
    @Setter
    private static Boolean isLoad = false;

    private static final int COUNT = 7;

    /**
     * 初始化.
     */
    @PostConstruct
    public void init() {
        if (!isLoad) {
            // 初始化加载 Fenix 配置.
            FenixConfigManager.getInstance().initLoad();

            // 构造实体集合.
            List<PrefixUnderscore> puEntities = new ArrayList<>();
            for (int i = 1; i <= COUNT; ++i) {
                puEntities.add(new PrefixUnderscore()
                        .setName(COLUMN_VALUE + i)
                        .setSecondName("这是 second name, index is :" + i)
                        .setIntColumn(i)
                        .setLongColumn(i * 1000)
                        .setCreateTime(new Date())
                        .setLastUpdateTime(LocalDateTime.now()));
            }
            this.prefixUnderscoreRepository.saveAll(puEntities);
            isLoad = true;
        }

        // 断言结果是否正确.
        Assert.assertEquals(COUNT, this.prefixUnderscoreRepository.count());
    }

    @Test
    public void findResultType() {
        int num = 5;
        List<PrefixUnderscoreVo> puVoList = this.prefixUnderscoreRepository.queryPrefixUnderscoreVoResultType(num);
        Assert.assertEquals(num - 1, puVoList.size());
        for (PrefixUnderscoreVo prefixUnderscoreVo : puVoList) {
            Assert.assertNotNull(prefixUnderscoreVo.getId());
            Assert.assertNotNull(prefixUnderscoreVo.getName());
            Assert.assertTrue(prefixUnderscoreVo.getName().startsWith(COLUMN_VALUE));
            Assert.assertNotNull(prefixUnderscoreVo.getSecondName());
            Assert.assertTrue(prefixUnderscoreVo.getIntegerColumn() < num);
            Assert.assertTrue(prefixUnderscoreVo.getLongColumn() > 0);
            Assert.assertNotNull(prefixUnderscoreVo.getCreateTime());
            Assert.assertNotNull(prefixUnderscoreVo.getLastUpdateTime());
        }
    }

    @Test
    public void findResultType2() {
        int num = 4;
        int limit = 3;
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Order.desc("c_id")));
        List<PrefixUnderscoreVo> puVoList = prefixUnderscoreRepository.queryPrefixUnderscoreVoWithXml(num, pageable);
        Assert.assertEquals(limit, puVoList.size());
        for (PrefixUnderscoreVo prefixUnderscoreVo : puVoList) {
            Assert.assertNotNull(prefixUnderscoreVo.getId());
            Assert.assertNotNull(prefixUnderscoreVo.getName());
            Assert.assertTrue(prefixUnderscoreVo.getName().startsWith(COLUMN_VALUE));
            Assert.assertNull(prefixUnderscoreVo.getSecondName());
            Assert.assertTrue(prefixUnderscoreVo.getIntegerColumn() >= num);
            Assert.assertTrue(prefixUnderscoreVo.getLongColumn() > 0);
            Assert.assertNull(prefixUnderscoreVo.getCreateTime());
            Assert.assertNotNull(prefixUnderscoreVo.getLastUpdateTime());
        }
    }

    /**
     * 销毁.
     */
    @PreDestroy
    public void destroy() {
        FenixConfigManager.getInstance().clear();
    }

}
