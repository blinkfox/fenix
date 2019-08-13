package com.blinkfox.fenix.provider;

import com.blinkfox.fenix.bean.SqlInfo;
import com.blinkfox.fenix.core.Fenix;
import com.blinkfox.fenix.entity.Blog;
import com.blinkfox.fenix.entity.User;
import com.blinkfox.fenix.helper.StringHelper;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.springframework.data.repository.query.Param;

/**
 * 使用 Java 拼接用户动态 SQL 的 Java 类.
 *
 * @author blinkfox on 2019-08-11.
 */
@Slf4j
public final class UserSqlInfoProvider {

    /**
     * 获取用户信息.
     *
     * @param userId 用户 ID
     * @param userEmail 用户邮箱
     * @return sqlInfo 对象
     */
    public static SqlInfo queryUsersWithJava(@Param("userId") String userId, @Param("userEmail") String userEmail,
            @Param("user") User user, @Param("blog") Blog blog, @Param("myAge") Integer myAge) {
        log.info("获取到的 blog: {}", blog);
        return Fenix.start()
                .select("u")
                .from("User").as("u")
                .where("u.id <> :userId", "userId", userId)
                .andLike("u.name", user.getName(), StringHelper.isNotBlank(user.getName()))
                .andLike("u.email", userEmail)
                .andGreaterThan("u.age", myAge)
                .end();
    }

    /**
     * 获取用户信息总数.
     *
     * @param userMap 用户 Map
     * @return sqlInfo 对象
     */
    public static SqlInfo queryUsersCount(@Param("userMap") Map<String, Object> userMap) {
        return Fenix.start()
                .select("count(*)")
                .from("User").as("u")
                .where()
                .in("u.id", (Object[]) userMap.get("ids"))
                .andNotEqual("u.id", "2")
                .end();
    }

}
