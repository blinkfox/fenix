package com.blinkfox.fenix.repository;

import com.blinkfox.fenix.entity.User;
import com.blinkfox.fenix.jpa.QueryFenix;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
    @QueryFenix("UnitTestRepository.testNotEqual")
    List<User> testNotEqual(@Param("user") User user);

    /**
     * 使用 {@link QueryFenix} 注解根据用户的实体 VO 类来查询用户信息.
     *
     * @param user 用户实体信息
     * @return 用户信息集合
     */
    @QueryFenix("UnitTestRepository.testLessThanEqual")
    List<User> testLessThanEqual(@Param("user") User user);

    /**
     * 使用 {@link QueryFenix} 注解根据用户的实体 VO 类来查询用户信息.
     *
     * @param user 用户实体信息
     * @return 用户信息集合
     */
    @QueryFenix("UnitTestRepository.testLike")
    List<User> testLike(@Param("user") User user);

    /**
     * 使用 {@link QueryFenix} 注解根据用户的实体 VO 类来查询用户信息.
     *
     * @param user 用户实体信息
     * @return 用户信息集合
     */
    @QueryFenix("UnitTestRepository.testStartsWith")
    List<User> testStartsWith(@Param("user") User user);

    /**
     * 使用 {@link QueryFenix} 注解根据用户的实体 VO 类来查询用户信息.
     *
     * @param user 用户实体信息
     * @return 用户信息集合
     */
    @QueryFenix("UnitTestRepository.testEndsWith")
    List<User> testEndsWith(@Param("user") User user);

    /**
     * 使用 {@link QueryFenix} 注解根据用户的实体 VO 类来查询用户信息.
     *
     * @param userMap 用 Map 存放的用户信息
     * @return 用户信息集合
     */
    @QueryFenix("UnitTestRepository.testBetween")
    List<User> testBetween(@Param("user") Map<String, Object> userMap);

    /**
     * 使用 {@link QueryFenix} 注解根据用户的实体 VO 类来查询用户信息.
     *
     * @param userMap 用 Map 存放的用户信息
     * @return 用户信息集合
     */
    @QueryFenix("UnitTestRepository.testIn")
    List<User> testIn(@Param("userMap") Map<String, Object> userMap);

    /**
     * 使用 {@link QueryFenix} 注解根据用户的实体 VO 类来查询用户信息.
     *
     * @param user 用户实体信息
     * @return 用户信息集合
     */
    @QueryFenix("UnitTestRepository.testIsNull")
    List<User> testIsNull(@Param("user") User user);

    /**
     * 使用 {@link QueryFenix} 注解根据用户的实体 VO 类来查询用户信息.
     *
     * @param userMap 用 Map 存放的用户信息
     * @return 用户信息集合
     */
    @QueryFenix("UnitTestRepository.testText")
    List<User> testText(@Param("userMap") Map<String, Object> userMap, @Param("user") User user);

    /**
     * 使用 {@link QueryFenix} 注解根据用户的实体 VO 类来查询用户信息.
     *
     * @param userMap 用 Map 存放的用户信息
     * @return 用户信息集合
     */
    @QueryFenix("UnitTestRepository.testImport")
    List<User> testImport(@Param("userMap") Map<String, Object> userMap, @Param("user") User user);

    /**
     * 使用 {@link QueryFenix} 注解根据用户的实体 VO 类来查询用户信息.
     *
     * @param user 用户实体信息
     * @return 用户信息集合
     */
    @Transactional
    @Modifying
    @QueryFenix("UnitTestRepository.testSet")
    int testSet(@Param("user") User user);

    /**
     * 使用原生的 JPA {@link org.springframework.data.jpa.repository.Query} 的注解的执行更新.
     *
     * @param name 名称
     * @return 用户信息集合
     */
    @Transactional
    @Modifying
    @Query("UPDATE User SET name = :name, email = :email, age = :age, sex = :sex WHERE id = '10'")
    int testUpdate(@Param("name") String name, @Param("email") String email,
                   @Param("age") int age, @Param("sex") String sex);

}
