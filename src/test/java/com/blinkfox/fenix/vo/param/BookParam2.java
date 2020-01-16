package com.blinkfox.fenix.vo.param;

import com.blinkfox.fenix.specification.annotation.Equals;
import com.blinkfox.fenix.specification.annotation.GreaterThanEqual;
import com.blinkfox.fenix.specification.annotation.In;
import com.blinkfox.fenix.specification.annotation.IsNotNull;
import com.blinkfox.fenix.specification.annotation.LessThanEqual;
import com.blinkfox.fenix.specification.annotation.Like;

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
public class BookParam2 {

    /**
     * ID 集合.
     */
    @In
    private String[] id;

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
    @GreaterThanEqual
    private Integer totalPage;

    /**
     * 出版日期.
     */
    @LessThanEqual
    private String publishAt;

    /**
     * 其它扩展信息.
     */
    @IsNotNull
    private String others;

}
