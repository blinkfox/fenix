package com.blinkfox.fenix.repository.ar;

import com.blinkfox.fenix.FenixTestApplication;
import com.blinkfox.fenix.entity.ar.ArEntity;
import com.blinkfox.fenix.enums.StatusEnum;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.repository.CrudRepository;
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

    public static final int COUNT = 6;

    public static final String TITLE = "这是测试标题";

    @Resource
    private ArEntityRepository arEntityRepository;

    /**
     * 初始化.
     */
    @PostConstruct
    public void init() {
        super.initLoad();
    }

    /**
     * 初始化插入一些数据.
     */
    @Override
    public void initData() {
        List<ArEntity> arEntityList = new ArrayList<>();
        for (int i = 1; i <= COUNT; ++i) {
            arEntityList.add(new ArEntity()
                    .setTitle(TITLE + i)
                    .setDesc("这是测试的描述信息" + i)
                    .setAge(20 + i)
                    .setStatus(StatusEnum.of(i % 2 + 1))
                    .setCreateTime(new Date())
                    .setLastUpdateTime(LocalDateTime.now())
                    .save());
        }

        // 批量保存并查询判断.
        CrudRepository<ArEntity, String> arEntityRepository = new ArEntity().getRepository();
        arEntityRepository.saveAll(arEntityList);
        Assert.assertEquals(arEntityRepository.count(), COUNT);
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

    @Test
    public void save2() {
        ArEntity arEntity = new ArEntity()
                .setTitle(TITLE + "flush")
                .setDesc("这是测试的描述信息-flush")
                .setAge(10)
                .setCreateTime(new Date())
                .setLastUpdateTime(LocalDateTime.now())
                .save();

        Assert.assertTrue(arEntity.existsById());

        String id = arEntity.getId();
        Assert.assertTrue(arEntity.getRepository().existsById(id));
        Assert.assertTrue(arEntityRepository.existsById(id));
        Assert.assertTrue(arEntityRepository.findByIdWithFenix(id).getTitle().startsWith(TITLE));
        Assert.assertTrue(arEntityRepository.findByAgeWithFenix(null).size() >= COUNT);
        Assert.assertTrue(arEntityRepository.findByAgeWithFenix(23).size() >= (COUNT - 3));
    }

}