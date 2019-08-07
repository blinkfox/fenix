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
     * 根据博客标题模糊查询博客信息.
     *
     * @param title 标题
     * @return 博客信息集合
     */
    @Query("select b from Blog as b where b.title like :title")
    List<Blog> queryBlogsByTitle(@Param("title") String title);

    /**
     * 根据博客的部分信息查询对应的博客信息集合.
     *
     * @param blog 博客实体信息
     * @return 博客信息集合
     */
    @QueryFenix("BlogRepository.queryMyBlogs")
    List<Blog> queryMyBlogs(@Param("blog") Blog blog);

}
