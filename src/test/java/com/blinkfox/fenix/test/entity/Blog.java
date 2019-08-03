package com.blinkfox.fenix.test.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 博客实体表.
 *
 * @author blinkfox on 2019/7/30.
 */
@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "t_blog")
@NoArgsConstructor
public class Blog {

    /**
     * ID.
     */
    @Id
    @Column(name = "c_id")
    private String id;

    /**
     * 博客标题.
     */
    @Column(name = "c_title")
    private String title;

    /**
     * 博客作者.
     */
    @Column(name = "c_author")
    private String author;

    /**
     * 博客内容.
     */
    @Column(name = "c_content")
    private String content;

    /**
     * 创建时间.
     */
    @Column(name = "dt_create_time")
    private Date createTime;

    /**
     * 更新时间.
     */
    @Column(name = "dt_update_time")
    private Date updateTime;

    /**
     * 根据 id、标题和内容的构造方法.
     *
     * @param id 博客ID
     * @param title 博客标题
     * @param content 博客内容
     */
    public Blog(String id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

}
