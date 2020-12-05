package com.blinkfox.fenix.service;

import com.blinkfox.fenix.FenixTestApplication;
import com.blinkfox.fenix.entity.School;
import com.blinkfox.fenix.repository.FenixSchoolRepository;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * {@link FenixSchoolRepository} 接口功能的单元测试类.
 *
 * @author blinkfox on 2020-12-05.
 * @since v2.4.0
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = FenixTestApplication.class)
public class FenixSchoolServiceTest {

    @Autowired
    private FenixSchoolService fenixSchoolService;

    @Autowired
    private FenixSchoolRepository fenixSchoolRepository;

    /**
     * 测试异常抛出，事务回滚的相关功能.
     */
    @Test
    public void saveOrUpdateByNotNullPropertiesWithException() {
        String id = "101";
        try {
            this.fenixSchoolService.saveOrUpdateByNotNullPropertiesWithException(id);
        } catch (Exception e) {
            log.debug("捕捉到了用于测试的异常.");
        }

        Optional<School> schoolOptional = this.fenixSchoolRepository.findById(id);
        Assert.assertTrue(schoolOptional.isPresent());
    }

}
