package com.blinkfox.fenix.repository;

import com.blinkfox.fenix.entity.User;
import com.blinkfox.fenix.jpa.QueryFenix;
import com.blinkfox.fenix.provider.UserSqlInfoProvider;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    @QueryFenix("queryUserWithIdEmail")
    List<User> queryUserWithIdEmail(@Param("userId") String userId, @Param("userEmail") String userEmail);

    /**
     * 使用 {@link QueryFenix} 注解和 Java 拼接 SQL 的方式来查询用户信息，不指定 method 将默认跟方法名保持一致的查询.
     *
     * @param userId 用户 ID
     * @param user 用户信息
     * @param age 年龄
     * @param userEmail 用户邮箱
     * @return 用户信息集合
     */
    @QueryFenix(provider = UserSqlInfoProvider.class)
    List<User> queryUsersWithJava(@Param("userId") String userId, @Param("user") User user,
            @Param("myAge") Integer age, @Param("userEmail") String userEmail);

    /**
     * 使用 {@link QueryFenix} 注解和 Java 拼接 SQL 的方式来查询用户信息，指定 method 来查询.
     *
     * @param userId 用户 ID
     * @param user 用户信息
     * @param age 年龄
     * @param userEmail 用户邮箱
     * @return 用户信息集合
     */
    @QueryFenix(provider = UserSqlInfoProvider.class, method = "queryUsersWithJava")
    List<User> queryUsersWithJava2(@Param("userId") String userId, @Param("user") User user,
            @Param("myAge") Integer age, @Param("userEmail") String userEmail);

    /**
     * 使用 {@link QueryFenix} 注解没有任何元数据参数时，使用默认约定来查询用户信息的方法.
     *
     * @param userMap 用户信息 map
     * @param user 用户实体
     * @return 用户信息集合
     */
    @QueryFenix
    List<User> queryUsersWithSameName(@Param("userMap") Map<String, Object> userMap, @Param("user") User user);

    /**
     * 分页查询数据.
     *
     * @param userMap 用户 Map 信息
     * @param pageable 分页对象
     * @return 分页数据
     */
    @QueryFenix(countQuery = "queryAllUsersCount", countMethod = "queryUsersCount")
    Page<User> queryUserByIds(@Param("userMap") Map<String, Object> userMap, Pageable pageable);

    /**
     * 根据名称分页查询其它目录下的 xml 文件的数据.
     *
     * @param user 用户信息
     * @return 分页数据
     */
    @QueryFenix("otherFenix.queryUsersByName")
    List<User> queryUsersByName(@Param("user") User user);

}
