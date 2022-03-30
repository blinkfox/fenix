package com.blinkfox.fenix.repository.ar;

import com.blinkfox.fenix.FenixTestApplication;
import com.blinkfox.fenix.entity.ar.ArPageSort;
import java.util.Optional;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * ArPageSortRepository 的单元测试类.
 *
 * @author blinkfox on 2022-03-28.
 * @since v2.7.0
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = FenixTestApplication.class)
public class ArPageSortRepositoryTest extends BaseRepositoryTest {

    public static final String NAME = "这是 PageSort 测试名称";

    /**
     * 初始化数据.
     */
    @PostConstruct
    public void init() {
        super.initLoad();
    }

    @Test
    public void test01Save() {
        String name = NAME + "-save1";
        ArPageSort arPs = new ArPageSort().setName(name).save();

        // 查询断言保存的结果.
        Optional<ArPageSort> arPsOptional = arPs.findById();
        Assert.assertTrue(arPsOptional.isPresent());
        Assert.assertTrue(arPsOptional.get().getName().contains(NAME));
    }

}
