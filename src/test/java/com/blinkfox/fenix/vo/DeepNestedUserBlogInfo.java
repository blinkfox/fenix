package com.blinkfox.fenix.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 作为深层嵌套属性进行测试.
 *
 * @author hanandjun on 2021-10-22.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeepNestedUserBlogInfo {

    /**
     * 博客原作者.
     */
    private String author;

    /**
     * 博客内容.
     */
    private String content;

}
