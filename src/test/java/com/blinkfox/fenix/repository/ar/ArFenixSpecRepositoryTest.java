package com.blinkfox.fenix.repository.ar;

import com.blinkfox.fenix.FenixTestApplication;
import com.blinkfox.fenix.entity.ar.ArFenixSpec;
import com.blinkfox.fenix.helper.StringHelper;
import com.blinkfox.fenix.id.IdWorker;
import com.blinkfox.fenix.specification.handler.bean.BetweenValue;
import com.blinkfox.fenix.vo.param.ArSpecParam;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * ArFenixSpecRepository 的单元测试类.
 *
 * @author blinkfox on 2022-03-30.
 * @since v2.7.0
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = FenixTestApplication.class)
public class ArFenixSpecRepositoryTest extends BaseRepositoryTest {

    private static final int COUNT = 9;

    private static final String NAME = "这是 Fenix Spec 测试名称";

    private static final String SUMMARY = "这是 Fenix Spec 测试摘要信息";

    @Resource
    private ArFenixSpecRepository arFenixSpecRepository;

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
        List<ArFenixSpec> arSpecList = new ArrayList<>();
        for (int i = 1; i <= COUNT; ++i) {
            arSpecList.add(new ArFenixSpec().setName(NAME + i).setSummary(SUMMARY + i));
        }

        // 批量保存并查询判断.
        arFenixSpecRepository.saveAll(arSpecList);
        Assert.assertTrue(arFenixSpecRepository.count() >= COUNT);
    }

    @Test
    public void findOne() {
        // 初始化数据.
        List<ArFenixSpec> arSpecList = new ArrayList<>();
        long firstId = 0L;
        for (int i = 1; i <= COUNT; ++i) {
            long id = IdWorker.getSnowflakeId();
            if (i == 1) {
                firstId = id;
            }
            arSpecList.add(new ArFenixSpec()
                    .setId(id)
                    .setName(NAME + i)
                    .setSummary(SUMMARY + i)
                    .setStatus(i % 6)
                    .setBirthday(StringHelper.format("2022-03-0{}", i)));
        }
        Assert.assertTrue(firstId > 0);

        // 批量保存并查询判断.
        arFenixSpecRepository.saveAll(arSpecList);
        Assert.assertTrue(arFenixSpecRepository.count() >= COUNT);

        // 测试和断言根据 ID 查询.
        this.assertFindOneById(firstId);

        // 测试和断言根据 name 查询.
        this.assertFindOneByName();

        // 测试和断言根据 name 的 Bean 来查询.
        this.assertFindOneByBean();

        this.assertFindAll();
        this.assertFindAllPaging();
        this.assertFindAllSorting();
        this.assertCount();
    }

    private void assertFindOneById(long searchId) {
        Optional<ArFenixSpec> arSpecOptional = new ArFenixSpec().findOne(builder ->
                builder.andEquals("id", searchId).build());
        Assert.assertTrue(arSpecOptional.isPresent());
        Assert.assertTrue(arSpecOptional.get().getName().startsWith(NAME));
        Assert.assertTrue(arSpecOptional.get().getSummary().startsWith(SUMMARY));
    }

    private void assertFindOneByName() {
        int index = 2;
        String name = NAME + index;
        String summary = SUMMARY + index;
        Optional<ArFenixSpec> arSpecOptional2 = new ArFenixSpec().findOne(builder ->
                builder.andLike("name", name).build());
        Assert.assertTrue(arSpecOptional2.isPresent());
        Assert.assertEquals(arSpecOptional2.get().getName(), name);
        Assert.assertEquals(arSpecOptional2.get().getSummary(), summary);
    }

    private void assertFindOneByBean() {
        int index = 5;
        String name = NAME + index;
        String summary = SUMMARY + index;
        Optional<ArFenixSpec> arSpecOptional = new ArFenixSpec().findOneOfBean(new ArSpecParam().setName(name));
        Assert.assertTrue(arSpecOptional.isPresent());
        Assert.assertEquals(arSpecOptional.get().getName(), name);
        Assert.assertEquals(arSpecOptional.get().getSummary(), summary);
    }

    private void assertFindAll() {
        List<ArFenixSpec> arFenixSpecs = new ArFenixSpec().findAll(builder ->
                builder.andIn("status", new Integer[] {2, 3}).build());
        this.doAssertFindAll(arFenixSpecs);

        List<ArFenixSpec> arFenixSpecs2 = new ArFenixSpec().findAllOfBean(
                new ArSpecParam().setStates(Arrays.asList(2, 3)));
        this.doAssertFindAll(arFenixSpecs2);
    }

    private void doAssertFindAll(List<ArFenixSpec> arFenixSpecs) {
        Assert.assertEquals(arFenixSpecs.size(), 4);
        arFenixSpecs.forEach(arFenixSpec -> {
            Assert.assertTrue(arFenixSpec.getName().startsWith(NAME));
            Assert.assertTrue(arFenixSpec.getSummary().startsWith(SUMMARY));
            Assert.assertTrue(arFenixSpec.getStatus() == 2 || arFenixSpec.getStatus() == 3);
        });
    }

    private void assertFindAllPaging() {
        String startDate = "2022-03-02";
        String endDate = "2022-03-07";
        List<Integer> statusList = Arrays.asList(0, 1, 2, 3);
        Pageable pageable = PageRequest.of(0, 3, Sort.by(Sort.Order.desc("birthday")));
        Page<ArFenixSpec> arFenixSpecs = new ArFenixSpec().findAll(builder ->
                builder.andIn("status", statusList)
                        .andLike("name", NAME)
                        .andBetween("birthday", startDate, endDate)
                        .build(), pageable);
        this.doAssertFindAllPaging(arFenixSpecs, statusList);

        Page<ArFenixSpec> arFenixSpecs2 = new ArFenixSpec().findAllOfBean(new ArSpecParam()
                .setStates(statusList).setName(NAME).setBirthday(BetweenValue.of(startDate, endDate)), pageable);
        this.doAssertFindAllPaging(arFenixSpecs2, statusList);
    }

    private void doAssertFindAllPaging(Page<ArFenixSpec> arFenixSpecs, List<Integer> statusList) {
        Assert.assertEquals(arFenixSpecs.getTotalElements(), 4);
        arFenixSpecs.forEach(arFenixSpec -> {
            Assert.assertTrue(arFenixSpec.getName().startsWith(NAME));
            Assert.assertTrue(arFenixSpec.getSummary().startsWith(SUMMARY));
            Assert.assertTrue(statusList.contains(arFenixSpec.getStatus()));
        });
    }

    private void assertFindAllSorting() {
        List<Integer> statusList = Arrays.asList(0, 1, 2);
        Sort sort = Sort.by(Sort.Order.desc("birthday"));
        List<ArFenixSpec> arFenixSpecs = new ArFenixSpec().findAll(builder ->
                builder.andIn("status", statusList)
                        .andLike("name", NAME)
                        .build(), sort);
        this.doAssertFindAllSorting(arFenixSpecs, statusList);

        List<ArFenixSpec> arFenixSpecs2 = new ArFenixSpec().findAllOfBean(
                new ArSpecParam().setStates(statusList).setName(NAME), sort);
        this.doAssertFindAllSorting(arFenixSpecs2, statusList);
    }

    private void doAssertFindAllSorting(List<ArFenixSpec> arFenixSpecs, List<Integer> statusList) {
        Assert.assertEquals(arFenixSpecs.size(), 5);
        arFenixSpecs.forEach(arFenixSpec -> {
            Assert.assertTrue(arFenixSpec.getName().startsWith(NAME));
            Assert.assertTrue(arFenixSpec.getSummary().startsWith(SUMMARY));
            Assert.assertTrue(statusList.contains(arFenixSpec.getStatus()));
        });
    }

    private void assertCount() {
        List<Integer> statusList = Arrays.asList(0, 1);
        long count = new ArFenixSpec().count(builder ->
                builder.andIn("status", statusList).build());
        Assert.assertEquals(count, 3);

        long count2 = new ArFenixSpec().countOfBean(
                new ArSpecParam().setStates(statusList));
        Assert.assertEquals(count2, 3);
    }

}
