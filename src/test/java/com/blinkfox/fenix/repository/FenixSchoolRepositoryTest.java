package com.blinkfox.fenix.repository;

import com.blinkfox.fenix.FenixTestApplication;
import com.blinkfox.fenix.entity.School;
import java.util.Date;
import java.util.Optional;
import javax.annotation.Resource;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * {@link FenixSchoolRepository} 接口功能的单元测试类.
 *
 * @author blinkfox on 2020-12-05.
 * @since v2.4.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = FenixTestApplication.class)
public class FenixSchoolRepositoryTest {

    @Resource
    private FenixSchoolRepository fenixSchoolRepository;

    /**
     * 测试新增或更新实体的功能.
     */
    @Test
    public void saveOrUpdateNotNullProperties() {
        // 先插入第一条数据.
        String id = "1";
        String name = "北京大学";
        Integer age = 125;
        Date now = new Date();
        School school = new School()
                .setId(id)
                .setName(name)
                .setCity("北京")
                .setAddress("北京市海淀区颐和园路5号")
                .setAge(age)
                .setCreateTime(now)
                .setUpdateTime(now);
        fenixSchoolRepository.saveOrUpdateNotNullProperties(school);
        Optional<School> schoolOptional = fenixSchoolRepository.findById(id);
        Assert.assertTrue(schoolOptional.isPresent());
        Assert.assertEquals(name, schoolOptional.get().getName());

        // 然后更新，判断部分属性是否更新和未更新.
        String name2 = "浙江大学";
        School newSchool = new School()
                .setId(id)
                .setName(name2)
                .setCity("杭州")
                .setAddress(null)
                .setAge(null)
                .setCreateTime(null)
                .setUpdateTime(new Date());
        fenixSchoolRepository.saveOrUpdateNotNullProperties(newSchool);
        Optional<School> schoolOptional2 = fenixSchoolRepository.findById(id);
        Assert.assertTrue(schoolOptional2.isPresent());
        Assert.assertEquals(name2, schoolOptional2.get().getName());
        Assert.assertEquals(age, schoolOptional2.get().getAge());
    }

}
