package com.blinkfox.fenix.repository.ar;

import com.blinkfox.fenix.FenixTestApplication;
import com.blinkfox.fenix.entity.ar.ArFenixJpa;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
 * ArFenixJpaRepository 的单元测试类.
 *
 * @author blinkfox on 2022-03-28.
 * @since v2.7.0
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = FenixTestApplication.class)
public class ArFenixJpaRepositoryTest extends BaseRepositoryTest {

    private static final String NAME = "这是 FenixJpa 测试名称";

    private static final String DESC = "这是 FenixJpa 测试描述";

    private static final int COUNT = 7;

    @Resource
    private ArFenixJpaRepository arFenixJpaRepository;

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
        List<ArFenixJpa> arJpaList = new ArrayList<>();
        for (int i = 1; i <= COUNT; ++i) {
            arJpaList.add(new ArFenixJpa().setName(NAME + i).setDesc(DESC + i));
        }

        // 批量保存并查询判断.
        JpaRepository<ArFenixJpa, String> arJpaRepository = new ArFenixJpa().getRepository();
        arJpaRepository.saveAll(arJpaList);
        Assert.assertTrue(arJpaRepository.count() >= COUNT);
    }

    @Test
    public void saveAndUpdate() {
        String name = NAME + "-save1";
        String desc = DESC + "-save1";
        ArFenixJpa afj = new ArFenixJpa().setName(name).setDesc(desc).save();

        // 查询断言保存的结果.
        Optional<ArFenixJpa> afjOptional = afj.findById();
        Assert.assertTrue(afjOptional.isPresent());
        Assert.assertEquals(afjOptional.get().getName(), name);

        ArFenixJpa ajf2 = new ArFenixJpa().setId(afj.getId()).setDesc(desc).saveOrUpdateByNotNullProperties();
        Optional<ArFenixJpa> afjOptional2 = ajf2.findById();
        Assert.assertTrue(afjOptional2.isPresent());
        Assert.assertEquals(afjOptional2.get().getName(), name);
        Assert.assertEquals(afjOptional2.get().getDesc(), desc);
    }

    @Test
    public void findWithFenixPaging() {
        // 初始化插入数据.
        initData();

        int limit = 3;
        Page<ArFenixJpa> arFenixJpaPage = this.arFenixJpaRepository.findWithPaging(PageRequest.of(0, limit));
        System.out.println(arFenixJpaPage);
        Assert.assertEquals(arFenixJpaPage.getContent().size(), limit);

        Page<ArFenixJpa> arJpaPage2 = new ArFenixJpa().getRepository().findWithPaging(PageRequest.of(0, limit));
        List<ArFenixJpa> arJpaList = arJpaPage2.getContent();
        Assert.assertEquals(arJpaList.size(), limit);
        for (ArFenixJpa arJpa : arJpaList) {
            Assert.assertTrue(arJpa.getName().startsWith(NAME));
            Assert.assertTrue(arJpa.getDesc().startsWith(DESC));
        }
    }

}
