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
import java.util.UUID;
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
     * 测试新增或更新所有实体类的功能.
     */
    @Test
    public void saveOrUpdateAllByNotNullProperties() {
        String id = "1";
        // 先插入一条数据.
        List<School> schools = new ArrayList<>();
        School school1 = schoolMap.get(id);
        schools.add(school1);
        fenixSchoolRepository.saveOrUpdateAllByNotNullProperties(schools);
        List<School> results = fenixSchoolRepository.findAll();
        Assert.assertTrue(results.size() >= 1);

        // 再插入一条和更新一条数据.
        Integer age = 124;
        List<School> schools2 = new ArrayList<>();
        schools2.add(schoolMap.get("2"));
        schools2.add(new School()
                .setId(id)
                .setAge(age)
                .setUpdateTime(new Date()));
        fenixSchoolRepository.saveOrUpdateAllByNotNullProperties(schools2);
        List<School> results2 = fenixSchoolRepository.findAll();
        Assert.assertTrue(results2.size() >= 2);

        // 验证是否是增量更新的字段.
        Optional<School> schoolResult = fenixSchoolRepository.findById(id);
        Assert.assertTrue(schoolResult.isPresent());
        Assert.assertEquals(school1.getName(), schoolResult.get().getName());
        Assert.assertEquals(age, schoolResult.get().getAge());
    }

    /**
     * 测试新增或更新所有实体类的功能.
     */
    @Test
    public void saveBatch() {
        // 构造批量的数据.
        int count = 15;
        fenixSchoolRepository.saveBatch(this.buildSchools(count), 10);
        List<School> allSchools = fenixSchoolRepository.findAll();
        Assert.assertTrue(allSchools.size() >= count);
    }

    /**
     * 测试新增或更新所有实体类的功能.
     */
    @Test
    public void saveBatchWithDefault() {
        // 构造批量的数据.
        int count = 5;
        fenixSchoolRepository.saveBatch(this.buildSchools(count));
        List<School> allSchools = fenixSchoolRepository.findAll();
        Assert.assertTrue(allSchools.size() >= count);
    }

    /**
     * 构建 School 集合.
     *
     * @param count 总数
     * @return 集合
     */
    private List<School> buildSchools(int count) {
        List<School> schools = new ArrayList<>();
        Date now = new Date();
        for (int i = 0; i < count; ++i) {
            schools.add(new School()
                    .setId(UUID.randomUUID().toString().replace("-",""))
                    .setName("测试名称" + i)
                    .setAge(50 + i)
                    .setCity("城市" + i)
                    .setAddress("地址" + i)
                    .setCreateTime(now)
                    .setUpdateTime(now));
        }
        return schools;
    }

}
