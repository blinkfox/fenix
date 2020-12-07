package com.blinkfox.fenix.repository;

import com.blinkfox.fenix.FenixTestApplication;
import com.blinkfox.fenix.entity.EntityThree;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 基于 {@link EntityTwoRepository} 用来测试自定义 ID 生成策略功能的单元测试类.
 *
 * @author blinkfox on 2020-12-07.
 * @since v2.4.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = FenixTestApplication.class)
public class EntityThreeRepositoryTest {

    @Resource
    private EntityThreeRepository entityThreeRepository;

    /**
     * 测试保存所有数据的方法.
     */
    @Test
    public void saveAll() {
        // 测试单条插入.
        EntityThree entity = this.entityThreeRepository.save(new EntityThree(5));
        Assert.assertNotNull(entity.getId());

        // 测试批量保存数据时的 ID 生成.
        List<EntityThree> entityThrees = new ArrayList<>();
        for (int i = 0; i < 2; ++i) {
            entityThrees.add(new EntityThree(i + 1));
        }
        this.entityThreeRepository.saveBatch(entityThrees);
        List<EntityThree> threes = this.entityThreeRepository.findAll();
        Assert.assertTrue(threes.size() > 2);
        for (EntityThree entityThree : threes) {
            Assert.assertNotNull(entityThree.getId());
        }
    }

}