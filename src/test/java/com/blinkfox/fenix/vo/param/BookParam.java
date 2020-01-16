package com.blinkfox.fenix.vo;

import com.blinkfox.fenix.specification.annotation.Equals;
import com.blinkfox.fenix.specification.annotation.GreaterThan;
import com.blinkfox.fenix.specification.annotation.In;
import com.blinkfox.fenix.specification.annotation.Like;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 查询图书的相关参数实体类.
 *
 * @author blinkfox on 2020-01-14.
 */
@Getter
@Setter
@Accessors(chain = true)
public class BookParam {

    /**
     * ID 集合.
     */
    @In
    private List<String> id;

    /**
     * 图书的 ISBN 编号.
     */
    @Equals
    private String isbn;

    /**
     * 图书标题.
     */
    @Like
    private String name;

    /**
     * 图书作者.
     */
    @Like
    private String author;

    /**
     * 总页数.
     */
    @GreaterThan
    private Integer totalPage;

    /**
     * 出版日期.
     */
    @GreaterThan
    private Date publishDate;

}
