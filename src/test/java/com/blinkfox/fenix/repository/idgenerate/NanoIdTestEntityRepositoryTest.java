package com.blinkfox.fenix.repository.idgenerate;

import com.blinkfox.fenix.FenixTestApplication;
import com.blinkfox.fenix.entity.idgenerate.NanoIdTestEntity;
import jakarta.annotation.Resource;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 测试 {@link NanoIdTestEntity} 对应的相关功能.
 *
 * @author blinkfox on 2025-07-22
 * @since 3.1.0
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = FenixTestApplication.class)
public class NanoIdTestEntityRepositoryTest {

    @Resource
    private NanoIdTestEntityRepository nanoIdTestEntityRepository;

    @Test
    public void testSave() {
        String name = "testName";
        NanoIdTestEntity nanoIdTestEntity = new NanoIdTestEntity();
        nanoIdTestEntity.setName(name);
        nanoIdTestEntityRepository.save(nanoIdTestEntity);

        List<NanoIdTestEntity> list = nanoIdTestEntityRepository.findAll();
        Assert.assertEquals(1, list.size());
        NanoIdTestEntity currEntity = list.get(0);
        Assert.assertNotNull(currEntity.getId());
        Assert.assertEquals(name, currEntity.getName());
    }
}
