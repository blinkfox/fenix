package com.blinkfox.fenix.service;

import com.blinkfox.fenix.entity.School;
import com.blinkfox.fenix.repository.FenixSchoolRepository;
import java.util.Date;
import java.util.Optional;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用于测试 {@link FenixSchoolRepository} 接口功能的 service 类.
 *
 * @author blinkfox on 2020-12-05.
 * @since v1.0.0
 */
@Service
public class FenixSchoolService {

    @Autowired
    private FenixSchoolRepository fenixSchoolRepository;

    /**
     * 测试异常事务回滚的情况.
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateByNotNullPropertiesWithException(String id) {
        // 先插入第一条数据.
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
        School school2 = schoolOptional.get();
        Assert.assertEquals(name, school2.getName());
        if (school2.getAge() >= 0) {
            throw new RuntimeException("测试异常抛出，该异常是用来验证事务的回滚情况的.");
        }
    }

}
