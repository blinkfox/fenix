package com.blinkfox.fenix.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * UserBlogInfo.
 *
 * @author blinkfox on 2019/8/9.
 */
@Getter
@Setter
public class UserBlogInfo {

    /**
     * 用户 ID.
     */
    private String userId;

    /**
     * 用户名称.
     */
    private String name;

    /**
     * 用户博客 ID.
     */
    private String blogId;

    /**
     * 博客标题.
     */
    private String title;

    /**
     * 博客原作者.
     */
    private String author;

    /**
     * 博客内容.
     */
    private String content;

    /**
     * 用户博客信息构造方法.
     *
     * @param userId 用户 ID
     * @param name 用户名称
     * @param blogId 博客 ID
     * @param title 标题
     * @param author 作者
     * @param content 内容
     */
    public UserBlogInfo(String userId, String name, String blogId, String title, String author, String content) {
        this.userId = userId;
        this.name = name;
        this.blogId = blogId;
        this.title = title;
        this.author = author;
        this.content = content;
    }
}
