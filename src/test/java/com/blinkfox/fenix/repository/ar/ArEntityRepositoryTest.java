package com.blinkfox.fenix.repository.ar;

import com.blinkfox.fenix.FenixTestApplication;
import com.blinkfox.fenix.entity.ar.ArEntity;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * ArEntityRepository 的单元测试类.
 *
 * @author blinkfox on 2022-03-28.
 * @since v2.7.0
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = FenixTestApplication.class)
public class ArEntityRepositoryTest extends BaseRepositoryTest {

    public static final String TITLE = "这是测试标题";

    /**
     * 初始化.
     */
    @PostConstruct
    public void init() {
        super.initLoad();
    }

    @Test
    public void saveEntity() {
        ArEntity arEntity = new ArEntity()
                .setTitle(TITLE)
                .setDesc("这是测试的描述信息")
                .setAge(20)
                .setCreateTime(new Date())
                .setLastUpdateTime(LocalDateTime.now())
                .save();

        // 查询保存的结果.
        Optional<ArEntity> arOption = arEntity.findById();
        Assert.assertTrue(arOption.isPresent());

        // 断言查询结果是否正确.
        ArEntity newArEntity = arOption.get();
        Assert.assertNotNull(newArEntity.getId());
        Assert.assertEquals(newArEntity.getTitle(), TITLE);
        Assert.assertNotNull(newArEntity.getDesc());
        Assert.assertNotNull(newArEntity.getAge());
        Assert.assertNotNull(newArEntity.getCreateTime());
        Assert.assertNotNull(newArEntity.getLastUpdateTime());

        // 再次查询断言.
        Optional<ArEntity> arOption2 = newArEntity.getRepository().findById(newArEntity.getId());
        Assert.assertTrue(arOption2.isPresent());
        Assert.assertEquals(arOption2.get().getTitle(), newArEntity.getTitle());
    }

}