package com.blinkfox.fenix.repository.idgenerate;

import com.blinkfox.fenix.FenixTestApplication;
import com.blinkfox.fenix.entity.idgenerate.SnowflakeIdDecimalTestEntity;
import jakarta.annotation.Resource;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 测试 {@link SnowflakeIdDecimalTestEntity} 对应的相关功能.
 *
 * @author blinkfox on 2025-07-22
 * @since 3.1.0
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = FenixTestApplication.class)
public class SnowflakeIdDecimalTestEntityRepositoryTest {

    @Resource
    private SnowflakeIdDecimalTestEntityRepository snowflakeIdDecimalTestEntityRepository;

    @Test
    public void testSave() {
        String name = "testName";
        SnowflakeIdDecimalTestEntity testEntity = new SnowflakeIdDecimalTestEntity();
        testEntity.setName(name);
        snowflakeIdDecimalTestEntityRepository.save(testEntity);

        List<SnowflakeIdDecimalTestEntity> list = snowflakeIdDecimalTestEntityRepository.findAll();
        Assert.assertEquals(1, list.size());
        SnowflakeIdDecimalTestEntity curEntity = list.get(0);
        Assert.assertNotNull(curEntity.getId());
        Assert.assertEquals(name, curEntity.getName());
    }
}
