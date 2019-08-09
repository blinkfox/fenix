package com.blinkfox.fenix.vo;

import org.springframework.beans.factory.annotation.Value;

/**
 * 用户博客信息的投影，用于测试 JPA 投影的使用.
 *
 * @author blinkfox on 2019-08-09.
 */
public interface UserBlogProjection {

    /**
     * 用户 ID.
     *
     * @return 用户 ID
     */
    String getUserId();

    /**
     * 用户名称.
     *
     * @return 用户名称
     */
    String getName();

    /**
     * 博客 ID.
     *
     * @return 博客 ID
     */
    String getBlogId();

    /**
     * 博客标题.
     *
     * @return 博客标题
     */
    String getTitle();

    /**
     * 作者.
     *
     * @return 作者
     */
    String getAuthor();

    /**
     * 博客内容.
     *
     * @return 博客内容
     */
    String getContent();

    /**
     * 自定义的描述信息.
     *
     * @return 描述信息
     */
    @Value("#{'【' + target.name + '】用户发表了博客:《' + target.title + '》。'}")
    String getDescription();

}
