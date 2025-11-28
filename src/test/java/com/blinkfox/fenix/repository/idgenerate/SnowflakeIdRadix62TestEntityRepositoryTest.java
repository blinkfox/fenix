package com.blinkfox.fenix.repository.idgenerate;

import com.blinkfox.fenix.FenixTestApplication;
import com.blinkfox.fenix.entity.idgenerate.SnowflakeIdRadix62TestEntity;
import jakarta.annotation.Resource;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 测试 {@link SnowflakeIdRadix62TestEntity} 对应的相关功能.
 *
 * @author blinkfox on 2025-07-22
 * @since 3.1.0
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = FenixTestApplication.class)
public class SnowflakeIdRadix62TestEntityRepositoryTest {

    @Resource
    private SnowflakeIdRadix62TestEntityRepository snowflakeIdRadix62TestEntityRepository;

    @Test
    public void testSave() {
        String name = "testName";
        SnowflakeIdRadix62TestEntity testEntity = new SnowflakeIdRadix62TestEntity();
        testEntity.setName(name);
        snowflakeIdRadix62TestEntityRepository.save(testEntity);

        List<SnowflakeIdRadix62TestEntity> list = snowflakeIdRadix62TestEntityRepository.findAll();
        Assert.assertEquals(1, list.size());
        SnowflakeIdRadix62TestEntity curEntity = list.get(0);
        Assert.assertNotNull(curEntity.getId());
        Assert.assertEquals(name, curEntity.getName());
    }
}
