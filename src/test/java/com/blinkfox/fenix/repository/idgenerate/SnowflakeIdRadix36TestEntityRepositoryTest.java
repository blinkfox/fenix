package com.blinkfox.fenix.repository.idgenerate;

import com.blinkfox.fenix.FenixTestApplication;
import com.blinkfox.fenix.entity.idgenerate.SnowflakeIdRadix36TestEntity;
import jakarta.annotation.Resource;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 测试 {@link SnowflakeIdRadix36TestEntity} 对应的相关功能.
 *
 * @author blinkfox on 2025-07-22
 * @since 3.1.0
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = FenixTestApplication.class)
public class SnowflakeIdRadix36TestEntityRepositoryTest {

    @Resource
    private SnowflakeIdRadix36TestEntityRepository snowflakeIdRadix36TestEntityRepository;

    @Test
    public void testSave() {
        String name = "testName";
        SnowflakeIdRadix36TestEntity testEntity = new SnowflakeIdRadix36TestEntity();
        testEntity.setName(name);
        snowflakeIdRadix36TestEntityRepository.save(testEntity);

        List<SnowflakeIdRadix36TestEntity> list = snowflakeIdRadix36TestEntityRepository.findAll();
        Assert.assertEquals(1, list.size());
        SnowflakeIdRadix36TestEntity curEntity = list.get(0);
        Assert.assertNotNull(curEntity.getId());
        Assert.assertEquals(name, curEntity.getName());
    }
}
