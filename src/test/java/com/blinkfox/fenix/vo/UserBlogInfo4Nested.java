package com.blinkfox.fenix.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 用户博客信息的自定义业务实体类，用于测试 JPA 返回自定义实体中嵌套属性的使用.
 *
 * @author hanandjun on 2021-10-22.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserBlogInfo4Nested {

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

    private NestedUserBlogInfo nestedUserBlogInfo;

}
