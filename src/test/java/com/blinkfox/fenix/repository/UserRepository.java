package com.blinkfox.fenix.repository;

import com.blinkfox.fenix.entity.Blog;
import com.blinkfox.fenix.entity.User;
import com.blinkfox.fenix.jpa.QueryFenix;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 用户库持久化类.
 *
 * @author blinkfox on 2019-08-09.
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {

    /**
     * 使用 {@link QueryFenix} 注解根据用户 ID 和 Email 来查询用户信息.
     *
     * @param userId 用户 ID
     * @param userEmail 用户邮箱
     * @return 用户信息集合
     */
    @QueryFenix("UserRepository.queryUserWithIdEmail")
    List<User> queryUserWithIdEmail(@Param("userId") String userId, @Param("userEmail") String userEmail);

}
