package com.blinkfox.fenix.provider;

import com.blinkfox.fenix.bean.SqlInfo;
import com.blinkfox.fenix.entity.Blog;
import com.blinkfox.fenix.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;

/**
 * UserSqlProvider.
 *
 * @author blinkfox on 2019-08-11.
 */
@Slf4j
public final class UserSqlProvider {

    /**
     * 获取用户信息.
     *
     * @param userId 用户 ID
     * @param userEmail 用户邮箱
     * @return sqlInfo 对象
     */
    public static SqlInfo getUsers(@Param("userId") String userId, @Param("userEmail") String userEmail,
            @Param("user") User user, @Param("blog") Blog blog, @Param("myAge") Integer myAge) {
        System.out.println("这是 Java 写的 SQL.");
        log.info("userId: {}", userId);
        log.info("userEmail: {}", userEmail);
        return new SqlInfo();
    }

}
