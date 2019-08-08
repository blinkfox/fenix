package com.blinkfox.fenix.repository;

import com.blinkfox.fenix.entity.User;
import com.blinkfox.fenix.jpa.QueryFenix;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 博客数据的库持久化类.
 *
 * @author blinkfox on 2019/8/4.
 */
@Repository
public interface UnitTestRepository extends JpaRepository<User, String> {

    /**
     * 使用 {@link QueryFenix} 注解根据用户的实体 VO 类来查询用户信息.
     *
     * @param user 用户实体信息
     * @return 用户信息集合
     */
    @QueryFenix("UnitTestRepository.testEqual")
    List<User> testEqual(@Param("user") User user, @Param("userId") String userId);

    /**
     * 使用 {@link QueryFenix} 注解根据用户的实体 VO 类来查询用户信息.
     *
     * @param user 用户实体信息
     * @return 用户信息集合
     */
    @QueryFenix("UnitTestRepository.testEqual2")
    List<User> testEqual2(@Param("user") User user);

}
