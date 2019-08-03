package com.blinkfox.fenix.test.repository;

import com.blinkfox.fenix.test.entity.Blog;

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

}
