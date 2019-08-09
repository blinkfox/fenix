package com.blinkfox.fenix.repository;

import com.blinkfox.fenix.entity.Blog;
import com.blinkfox.fenix.jpa.QueryFenix;
import com.blinkfox.fenix.vo.UserBlogInfo;
import com.blinkfox.fenix.vo.UserBlogProjection;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 博客数据的库持久化类.
 *
 * @author blinkfox on 2019/8/4.
 */
@Repository
public interface BlogRepository extends JpaRepository<Blog, String> {

    /**
     * 使用原生的 {@link Query} 注解来模糊查询博客信息.
     *
     * @param idList ID 集合
     * @param title 标题
     * @return 博客信息集合
     */
    @Query("select b from Blog as b where b.id IN :ids and b.title like :title")
    List<Blog> queryBlogsByTitle(@Param("ids") String[] idList, @Param("title") String title);

    /**
     * 使用原生的 {@link Query} 注解来查询指定 ID 的博客信息.
     *
     * @param idList ID 集合
     * @return 博客信息集合
     */
    @Query(value = "select b from Blog as b where b.id IN :ids")
    Page<Blog> queryBlogsByIds(@Param("ids") String[] idList, Pageable pageable);

    /**
     * 使用 {@link QueryFenix} 注解根据博客的实体 VO 类来查询博客信息.
     *
     * @param blog 博客实体信息
     * @return 博客信息集合
     */
    @QueryFenix("BlogRepository.querySimplyDemo")
    List<Blog> querySimplyDemo(@Param("blog") Blog blog);

    /**
     * 使用 {@link QueryFenix} 注解根据博客的实体 VO 类和其他参数来查询博客信息.
     *
     * @param idList ID 集合
     * @param blog 博客实体信息
     * @return 博客信息集合
     */
    @QueryFenix(value = "BlogRepository.queryBlogs2", countQuery = "BlogRepository.queryBlogs2Count")
    Page<Blog> queryBlogs2(@Param("ids") String[] idList, @Param("blog") Blog blog, Pageable pageable);

    /**
     * 使用原生的 {@link Query} 注解来连表模糊查询用户博客信息.
     *
     * @param userId 用户 ID
     * @param title 标题
     * @return 用户博客信息集合
     */
    @Query("select new com.blinkfox.fenix.vo.UserBlogInfo(u.id, u.name, b.id, b.title, b.author, b.content) "
            + "from Blog as b, com.blinkfox.fenix.entity.User as u"
            + " where u.id = b.userId and b.userId = :userId and b.title like :title")
    List<UserBlogInfo> queryUserBlogsByTitle(@Param("userId") String userId, @Param("title") String title);

    /**
     * 使用 {@link QueryFenix} 注解来连表模糊查询用户博客信息.
     *
     * @param userId 用户ID
     * @param blog 博客实体信息
     * @return 用户博客信息集合
     */
    @QueryFenix("BlogRepository.queryUserBlogsByTitleWithFenix")
    List<UserBlogInfo> queryUserBlogsByTitleWithFenix(@Param("userId") String userId, @Param("blog") Blog blog);

    /**
     * 使用 {@link QueryFenix} 注解来连表模糊查询用户博客信息到自定义的 {@link com.blinkfox.fenix.vo.UserBlogProjection} 投影接口中.
     *
     * @param userId 用户ID
     * @param blog 博客实体信息
     * @return 用户博客信息投影的集合
     */
    @QueryFenix("BlogRepository.queryUserBlogsByProjection")
    List<UserBlogProjection> queryUserBlogsByProjection(@Param("userId") String userId, @Param("blog") Blog blog);

    /**
     * 使用原生的 {@link Query} 注解来连表模糊查询用户博客信息到自定义的 {@link com.blinkfox.fenix.vo.UserBlogProjection} 投影接口中.
     *
     * @param userId 用户 ID
     * @param title 标题
     * @return 用户博客信息投影的集合
     */
    @Query(value = "select u.c_id as userId, u.c_name as name, b.c_id as blogId, b.c_title as title, "
            + "b.c_author as author, b.c_content as content "
            + "from t_blog as b, t_user as u "
            + "where u.c_id = b.c_user_id and b.c_user_id = :userId and b.c_title like :title", nativeQuery = true)
    List<UserBlogProjection> queryNativeByProjection(@Param("userId") String userId, @Param("title") String title);

    /**
     * 使用 {@link QueryFenix} 注解的原生 SQL 来连表模糊查询用户博客信息到自定义的 {@link com.blinkfox.fenix.vo.UserBlogProjection} 投影接口中.
     *
     * @param userId 用户 ID
     * @param blog 博客信息
     * @return 用户博客信息投影的集合
     */
    @QueryFenix(value = "BlogRepository.queryFenixNativeByProjection", nativeQuery = true)
    List<UserBlogProjection> queryFenixNativeByProjection(@Param("userId") String userId,
            @Param("blog") Blog blog, Pageable pageable);

}
