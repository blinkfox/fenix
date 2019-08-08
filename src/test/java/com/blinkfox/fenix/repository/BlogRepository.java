package com.blinkfox.fenix.repository;

import com.blinkfox.fenix.entity.Blog;
import com.blinkfox.fenix.jpa.QueryFenix;

import java.util.List;

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
     * @param title 标题
     * @return 博客信息集合
     */
    @Query("select b from Blog as b where b.title like :title")
    List<Blog> queryBlogsByTitle(@Param("title") String title);

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
     * @param blog 博客实体信息
     * @return 博客信息集合
     */
    @QueryFenix("BlogRepository.queryBlogs2")
    List<Blog> queryBlogs2(@Param("ids") List<String> idList, @Param("blog") Blog blog);

}
