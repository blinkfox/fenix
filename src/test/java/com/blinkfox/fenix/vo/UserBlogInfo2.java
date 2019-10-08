package com.blinkfox.fenix.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 用户博客信息的自定义业务实体类，用于测试 JPA 返回自定义实体的使用.
 *
 * @author blinkfox on 2019/8/9.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserBlogInfo2 {

    /**
     * 用户 ID.
     */
    private String USERID;

    /**
     * 用户名称.
     */
    private String NAME;

    /**
     * 用户博客 ID.
     */
    private String BLOGID;

    /**
     * 博客标题.
     */
    private String TITLE;

    /**
     * 博客原作者.
     */
    private String AUTHOR;

    /**
     * 博客内容.
     */
    private String CONTENT;

}
