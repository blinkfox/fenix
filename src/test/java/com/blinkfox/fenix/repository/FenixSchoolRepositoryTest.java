package com.blinkfox.fenix.repository;

import com.alibaba.fastjson.JSON;
import com.blinkfox.fenix.FenixTestApplication;
import com.blinkfox.fenix.entity.School;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.PostConstruct;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.FileCopyUtils;

/**
 * {@link FenixSchoolRepository} 接口功能的单元测试类.
 *
 * @author blinkfox on 2020-12-05.
 * @since v2.4.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = FenixTestApplication.class)
public class FenixSchoolRepositoryTest {

    @Value("/data/school.json")
    private Resource schoolResource;

    @Autowired
    private FenixSchoolRepository fenixSchoolRepository;

    /**
     * key 为主键 ID，value 为 school 对象实体.
     */
    private final Map<String, School> schoolMap = new HashMap<>();

    /**
     * 从资源文件的中读取数据，初始化保存起来，便于后续读取或操作.
     */
    @PostConstruct
    public void init() throws IOException {
        for (School school :
                JSON.parseArray(new String(FileCopyUtils.copyToByteArray(schoolResource.getFile())), School.class)) {
            this.schoolMap.put(school.getId(), school);
        }
    }

    /**
     * 测试新增或更新实体的功能.
     */
    @Test
    public void saveOrUpdateByNotNullProperties() {
        // 先插入第一条数据.
        String id = "100";
        String name = "测试大学";
        Integer age = 139;
        Date now = new Date();
        School school = new School()
                .setId(id)
                .setName(name)
                .setCity("测试省份")
                .setAddress("测试的地址")
                .setAge(age)
                .setCreateTime(now)
                .setUpdateTime(now);
        fenixSchoolRepository.saveOrUpdateByNotNullProperties(school);
        Optional<School> schoolOptional = fenixSchoolRepository.findById(id);
        Assert.assertTrue(schoolOptional.isPresent());
        Assert.assertEquals(name, schoolOptional.get().getName());

        // 然后更新，判断部分属性是否更新和未更新.
        String name2 = "修改后的名称";
        School newSchool = new School()
                .setId(id)
                .setName(name2)
                .setCity("修改后的城市")
                .setAddress(null)
                .setAge(null)
                .setCreateTime(null)
                .setUpdateTime(new Date());
        fenixSchoolRepository.saveOrUpdateByNotNullProperties(newSchool);
        Optional<School> schoolOptional2 = fenixSchoolRepository.findById(id);
        Assert.assertTrue(schoolOptional2.isPresent());
        Assert.assertEquals(name2, schoolOptional2.get().getName());
        Assert.assertEquals(age, schoolOptional2.get().getAge());
    }

    /**
     * 测试新增或更新实体的功能.
     */
    @Test
    public void saveOrUpdateByNotNullPropertiesWithException() {
        // 先插入第一条数据.
        String id = "101";
        String name = "测试大学101";
        Integer age = 139;
        Date now = new Date();
        School school = new School()
                .setId(id)
                .setName(name)
                .setCity("测试省份101")
                .setAddress("测试的地址101")
                .setAge(age)
                .setCreateTime(now)
                .setUpdateTime(now);
        fenixSchoolRepository.saveOrUpdateByNotNullProperties(school);
        Optional<School> schoolOptional = fenixSchoolRepository.findById(id);
        Assert.assertTrue(schoolOptional.isPresent());
        Assert.assertEquals(name, schoolOptional.get().getName());


    }

    /**
     * 测试新增或更新所有实体类的功能.
     */
    @Test
    public void saveOrUpdateAllByNotNullProperties() {
        // 先插入一条数据.
        List<School> schools = new ArrayList<>();
        School school1 = schoolMap.get("1");
        schools.add(school1);
        fenixSchoolRepository.saveOrUpdateAllByNotNullProperties(schools);
        List<School> results = fenixSchoolRepository.findAll();
        Assert.assertTrue(results.size() >= 1);

        // 再插入一条和更新一条数据.
        List<School> schools2 = new ArrayList<>();
        schools2.add(schoolMap.get("2"));
        schools2.add(new School()
                .setId("1")
                .setAge(124)
                .setUpdateTime(new Date()));
        fenixSchoolRepository.saveOrUpdateAllByNotNullProperties(schools2);
        List<School> results2 = fenixSchoolRepository.findAll();
        Assert.assertTrue(results2.size() >= 2);
    }

}
