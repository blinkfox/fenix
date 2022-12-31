package com.blinkfox.fenix.repository.ar;

import com.blinkfox.fenix.FenixTestApplication;
import com.blinkfox.fenix.entity.ar.ArJpa;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * ArJpaRepository 的单元测试类.
 *
 * @author blinkfox on 2022-03-28.
 * @since v2.7.0
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = FenixTestApplication.class)
public class ArJpaRepositoryTest extends BaseRepositoryTest {

    private static final int COUNT = 4;

    private static final String NAME = "这是 JPA 测试名称";

    /**
     * 是否加载过的标识.
     */
    @Setter
    protected static boolean isInit = false;

    @Resource
    private ArJpaRepository arJpaRepository;

    /**
     * 初始化数据.
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
        List<ArJpa> arJpaList = new ArrayList<>();
        for (int i = 1; i <= COUNT; ++i) {
            arJpaList.add(new ArJpa().setName(NAME + i));
        }

        // 批量保存并查询判断.
        JpaRepository<ArJpa, String> arJpaRepository = new ArJpa().getRepository();
        arJpaRepository.saveAll(arJpaList);
        Assert.assertTrue(arJpaRepository.count() >= COUNT);
    }

    @Test
    public void save() {
        String name = NAME + "save1";
        ArJpa arJpa = new ArJpa().setName(name).save();
        arJpa.flush();

        // 查询断言保存的结果.
        ArJpa arJpa2 = arJpa.getById();
        Assert.assertNotNull(arJpa2);
        Assert.assertEquals(arJpa2.getName(), name);
    }

    @Test
    public void save2() {
        String name = NAME + "save2";
        ArJpa arJpa = new ArJpa().setName(name).saveAndFlush();

        // 查询断言保存的结果.
        ArJpa arJpa2 = this.arJpaRepository.getById(arJpa.getId());
        Assert.assertEquals(arJpa2.getName(), name);
        Assert.assertEquals(arJpa2.getId(), arJpa.getId());
    }

    @Test
    public void count() {
        initData();
        Assert.assertTrue(new ArJpa().getRepository().count() >= COUNT);
    }

    @Test
    public void findWithPaging() {
        initData();

        int limit = 3;
        Page<ArJpa> arJpaPage = this.arJpaRepository.findWithPaging(PageRequest.of(0, limit));
        System.out.println(arJpaPage);
        Assert.assertEquals(arJpaPage.getContent().size(), limit);

        Page<ArJpa> arJpaPage2 = new ArJpa().getRepository().findWithPaging(PageRequest.of(0, limit));
        List<ArJpa> arJpaList = arJpaPage2.getContent();
        Assert.assertEquals(arJpaList.size(), limit);
        for (ArJpa arJpa : arJpaList) {
            Assert.assertTrue(arJpa.getName().startsWith(NAME));
        }
    }

}