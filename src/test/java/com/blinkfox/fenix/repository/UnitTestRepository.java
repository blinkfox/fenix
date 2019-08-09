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
     * 使用 {@link QueryFenix} 注解使用原生 SQL 来查询用户信息.
     *
     * @param userId 用户ID
     * @return 用户信息集合
     */
    @QueryFenix(value = "UnitTestRepository.testNativeEqual", nativeQuery = true)
    List<User> testNativeEqual(@Param("user") User user, @Param("userId") String userId);

    /**
     * 使用 {@link QueryFenix} 注解使用原生 SQL 来查询部分用户字段的信息.
     *
     * @param userId 用户ID
     * @return 用户信息集合
     */
    @Query(value = "SELECT u.c_id, u.c_name, u.n_age, u.c_email, u.dt_create_time FROM t_user AS u WHERE u.c_id = :userId", nativeQuery = true)
    // TODO 使用 QueryFenix 注解还有问题，目前只能使用原生的语法才行.
    // @QueryFenix(value = "UnitTestRepository.testNativeEqualPartFields", nativeQuery = true)
    List<Map<String, Object>> testNativeEqualPartFields(@Param("userId") String userId);

    /**
     * 使用原生 {@link Query} 注解来查询用户信息.
     *
     * @param userId 用户ID
     * @return 用户信息集合
     */
    @Query(value = "SELECT * FROM t_user AS u WHERE u.c_id = :userId", nativeQuery = true)
    List<User> testOriginNativeEqual(@Param("userId") String userId);

    /**
     * 使用 {@link QueryFenix} 注解根据用户的实体 VO 类来查询用户信息.
     *
     * @param user 用户实体信息
     * @return 用户信息集合
     */
    @QueryFenix("UnitTestRepository.testNotEqual")
    List<User> testNotEqual(@Param("user") User user);

    /**
     * 使用 {@link QueryFenix} 注解及原生 SQL 来查询用户信息.
     *
     * @param user 用户实体信息
     * @return 用户信息集合
     */
    @QueryFenix(value = "UnitTestRepository.testNativeNotEqual", nativeQuery = true)
    List<User> testNativeNotEqual(@Param("user") User user);

    /**
     * 使用 {@link QueryFenix} 注解根据用户的实体 VO 类来查询用户信息.
     *
     * @param user 用户实体信息
     * @return 用户信息集合
     */
    @QueryFenix("UnitTestRepository.testLessThanEqual")
    List<User> testLessThanEqual(@Param("user") User user);

    /**
     * 使用 {@link QueryFenix} 注解及原生 SQL 来查询用户信息.
     *
     * @param user 用户实体信息
     * @return 用户信息集合
     */
    @QueryFenix(value = "UnitTestRepository.testNativeLessThanEqual", nativeQuery = true)
    List<User> testNativeLessThanEqual(@Param("user") User user);

    /**
     * 使用 {@link QueryFenix} 注解根据用户的实体 VO 类来查询用户信息.
     *
     * @param user 用户实体信息
     * @return 用户信息集合
     */
    @QueryFenix("UnitTestRepository.testLike")
    List<User> testLike(@Param("user") User user);

    /**
     * 使用 {@link QueryFenix} 注解及原生 SQL 来查询用户信息.
     *
     * @param user 用户实体信息
     * @return 用户信息集合
     */
    @QueryFenix(value = "UnitTestRepository.testNativeLike", nativeQuery = true)
    List<User> testNativeLike(@Param("user") User user);

    /**
     * 使用 {@link QueryFenix} 注解根据用户的实体 VO 类来查询用户信息.
     *
     * @param user 用户实体信息
     * @return 用户信息集合
     */
    @QueryFenix("UnitTestRepository.testStartsWith")
    List<User> testStartsWith(@Param("user") User user);

    /**
     * 使用 {@link QueryFenix} 注解及原生 SQL 来查询用户信息.
     *
     * @param user 用户实体信息
     * @return 用户信息集合
     */
    @QueryFenix(value = "UnitTestRepository.testNativeStartsWith", nativeQuery = true)
    List<User> testNativeStartsWith(@Param("user") User user);

    /**
     * 使用 {@link QueryFenix} 注解根据用户的实体 VO 类来查询用户信息.
     *
     * @param user 用户实体信息
     * @return 用户信息集合
     */
    @QueryFenix("UnitTestRepository.testEndsWith")
    List<User> testEndsWith(@Param("user") User user);

    /**
     * 使用 {@link QueryFenix} 注解及原生 SQL 类来查询用户信息.
     *
     * @param user 用户实体信息
     * @return 用户信息集合
     */
    @QueryFenix(value = "UnitTestRepository.testNativeEndsWith", nativeQuery = true)
    List<User> testNativeEndsWith(@Param("user") User user);

    /**
     * 使用 {@link QueryFenix} 注解根据用户的实体 VO 类来查询用户信息.
     *
     * @param userMap 用 Map 存放的用户信息
     * @return 用户信息集合
     */
    @QueryFenix("UnitTestRepository.testBetween")
    List<User> testBetween(@Param("user") Map<String, Object> userMap);

    /**
     * 使用 {@link QueryFenix} 注解及原生 SQL 类来查询用户信息.
     *
     * @param userMap 用 Map 存放的用户信息
     * @return 用户信息集合
     */
    @QueryFenix(value = "UnitTestRepository.testNativeBetween", nativeQuery = true)
    List<User> testNativeBetween(@Param("user") Map<String, Object> userMap);

    /**
     * 使用 {@link QueryFenix} 注解根据用户的实体 VO 类来查询用户信息.
     *
     * @param userMap 用 Map 存放的用户信息
     * @return 用户信息集合
     */
    @QueryFenix("UnitTestRepository.testIn")
    List<User> testIn(@Param("userMap") Map<String, Object> userMap);

    /**
     * 使用 {@link QueryFenix} 注解及原生 SQL 类来查询用户信息.
     *
     * @param userMap 用 Map 存放的用户信息
     * @return 用户信息集合
     */
    @QueryFenix(value = "UnitTestRepository.testNativeIn", nativeQuery = true)
    List<User> testNativeIn(@Param("userMap") Map<String, Object> userMap);

    /**
     * 使用 {@link QueryFenix} 注解根据用户的实体 VO 类来查询用户信息.
     *
     * @param user 用户实体信息
     * @return 用户信息集合
     */
    @QueryFenix("UnitTestRepository.testIsNull")
    List<User> testIsNull(@Param("user") User user);

    /**
     * 使用 {@link QueryFenix} 注解原生 SQL 类来查询用户信息.
     *
     * @param user 用户实体信息
     * @return 用户信息集合
     */
    @QueryFenix(value = "UnitTestRepository.testNativeIsNull", nativeQuery = true)
    List<User> testNativeIsNull(@Param("user") User user);

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
     * 使用 {@link QueryFenix} 注解根据用户的实体 VO 类来更新用户信息.
     *
     * @param user 用户实体信息
     * @return 用户信息集合
     */
    @Transactional
    @Modifying
    @QueryFenix("UnitTestRepository.testSet")
    int testSet(@Param("user") User user);

    /**
     * 使用 {@link QueryFenix} 注解及原生 SQL 类来更新用户信息.
     *
     * @param user 用户实体信息
     * @return 用户信息集合
     */
    @Transactional
    @Modifying
    @QueryFenix(value = "UnitTestRepository.testNativeSet", nativeQuery = true)
    void testNativeSet(@Param("user") User user);

    /**
     * 使用原生的 JPA {@link org.springframework.data.jpa.repository.Query} 的注解的执行更新.
     *
     * @param name 名称
     * @param email 邮箱
     * @param age 年龄
     * @param sex 性别
     * @return 用户信息集合
     */
    @Transactional
    @Modifying
    @Query("UPDATE User SET name = :name, email = :email, age = :age, sex = :sex WHERE id = '10'")
    int testUpdate(@Param("name") String name, @Param("email") String email,
                   @Param("age") int age, @Param("sex") String sex);

    /**
     * 使用原生 SQL {@link org.springframework.data.jpa.repository.Query} 来执行更新.
     *
     * @param name 名称
     * @param email 邮箱
     * @param sex 性别
     * @return 用户信息集合
     */
    @Transactional
    @Modifying
    @Query(value = "UPDATE t_user SET c_name = :c_name, c_email = :c_email, c_sex = :c_sex WHERE c_id = '10'",
            nativeQuery = true)
    int testNativeUpdate(@Param("c_name") String name, @Param("c_email") String email, @Param("c_sex") String sex);

}
