package com.blinkfox.fenix.repository.ar;

import com.blinkfox.fenix.FenixTestApplication;
import com.blinkfox.fenix.entity.ar.ArCrud;
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
 * ArCrudRepository 的单元测试类.
 *
 * @author blinkfox on 2022-03-28.
 * @since v2.7.0
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = FenixTestApplication.class)
public class ArCrudRepositoryTest extends BaseRepositoryTest {

    public static final int COUNT = 6;

    public static final String TITLE = "这是测试标题";

    @Resource
    private ArCrudRepository arCrudRepository;

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
        List<ArCrud> arCrudList = new ArrayList<>();
        for (int i = 1; i <= COUNT; ++i) {
            arCrudList.add(new ArCrud()
                    .setTitle(TITLE + i)
                    .setDesc("这是测试的描述信息" + i)
                    .setAge(20 + i)
                    .setStatus(StatusEnum.of(i % 2 + 1))
                    .setCreateTime(new Date())
                    .setLastUpdateTime(LocalDateTime.now()));
        }

        // 批量保存并查询判断.
        CrudRepository<ArCrud, String> arCrudRepository = new ArCrud().getRepository();
        arCrudRepository.saveAll(arCrudList);
        Assert.assertTrue(arCrudRepository.count() >= COUNT);
    }

    @Test
    public void saveEntity() {
        ArCrud arCrud = new ArCrud()
                .setTitle(TITLE)
                .setDesc("这是测试的描述信息")
                .setAge(20)
                .setCreateTime(new Date())
                .setLastUpdateTime(LocalDateTime.now())
                .save();

        // 查询保存的结果.
        Optional<ArCrud> arOption = arCrud.findById();
        Assert.assertTrue(arOption.isPresent());

        // 断言查询结果是否正确.
        ArCrud newArCrud = arOption.get();
        Assert.assertNotNull(newArCrud.getId());
        Assert.assertEquals(TITLE, newArCrud.getTitle());
        Assert.assertNotNull(newArCrud.getDesc());
        Assert.assertNotNull(newArCrud.getAge());
        Assert.assertNotNull(newArCrud.getCreateTime());
        Assert.assertNotNull(newArCrud.getLastUpdateTime());

        // 再次查询断言.
        Optional<ArCrud> arOption2 = newArCrud.getRepository().findById(newArCrud.getId());
        Assert.assertTrue(arOption2.isPresent());
        Assert.assertEquals(arOption2.get().getTitle(), newArCrud.getTitle());
    }

    @Test
    public void save2() {
        initData();

        ArCrud arCrud = new ArCrud()
                .setTitle(TITLE + "flush")
                .setDesc("这是测试的描述信息-flush")
                .setAge(10)
                .setCreateTime(new Date())
                .setLastUpdateTime(LocalDateTime.now())
                .save();
        Assert.assertTrue(arCrud.existsById());

        String id = arCrud.getId();
        Assert.assertTrue(arCrud.getRepository().existsById(id));
        Assert.assertTrue(arCrudRepository.existsById(id));
        Assert.assertTrue(arCrudRepository.findByIdWithFenix(id).getTitle().startsWith(TITLE));
        Assert.assertTrue(arCrudRepository.findByAgeWithFenix(null).size() >= COUNT);
        Assert.assertTrue(arCrudRepository.findByAgeWithFenix(23).size() >= (COUNT - 3));
    }

    @Test
    public void delete() {
        ArCrud arCrud = new ArCrud()
                .setTitle(TITLE + "-delete")
                .setDesc("这是测试的描述信息-delete")
                .setAge(5)
                .setCreateTime(new Date())
                .setLastUpdateTime(LocalDateTime.now())
                .save();

        Assert.assertTrue(arCrud.existsById());
        arCrud.delete();
        Assert.assertFalse(arCrud.existsById());
    }

    @Test
    public void deleteById() {
        ArCrud arCrud = new ArCrud()
                .setTitle(TITLE + "-delete")
                .setDesc("这是测试的描述信息-delete")
                .setAge(5)
                .setCreateTime(new Date())
                .setLastUpdateTime(LocalDateTime.now())
                .save();

        // 判断是否存在
        String id = arCrud.getId();
        Assert.assertTrue(arCrud.existsById());

        // 单独设置 ID 来删除.
        ArCrud arCrud2 = new ArCrud().setId(id);
        arCrud2.deleteById();

        // 断言删除是否成功.
        Assert.assertFalse(arCrud.existsById());
        Assert.assertFalse(arCrud2.existsById());
    }

}