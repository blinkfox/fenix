package com.blinkfox.fenix.vo.param;

import com.blinkfox.fenix.specification.annotation.Equals;
import com.blinkfox.fenix.specification.annotation.GreaterThan;
import com.blinkfox.fenix.specification.annotation.In;
import com.blinkfox.fenix.specification.annotation.IsNull;
import com.blinkfox.fenix.specification.annotation.LessThan;
import com.blinkfox.fenix.specification.annotation.Like;
import com.blinkfox.fenix.specification.annotation.LikeIn;
import com.blinkfox.fenix.specification.annotation.OrEquals;
import com.blinkfox.fenix.specification.annotation.OrGreaterThan;
import com.blinkfox.fenix.specification.annotation.OrIn;
import com.blinkfox.fenix.specification.annotation.OrIsNull;
import com.blinkfox.fenix.specification.annotation.OrLessThan;
import com.blinkfox.fenix.specification.annotation.OrLike;

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
     * ID 集合.
     */
    @In("id")
    private String singleId;

    /**
     * ID 集合.
     */
    @OrIn("id")
    private List<String> orId;

    /**
     * ID 集合.
     */
    @OrIn("id")
    private String orSingleId;

    /**
     * 图书的 ISBN 编号.
     */
    @Equals
    private String isbn;

    /**
     * 图书的 ISBN 编号.
     */
    @OrEquals("isbn")
    private String orIsbn;

    /**
     * 图书标题.
     */
    @Like
    private String name;

    /**
     * 图书标题.
     */
    @OrLike("name")
    private String orName;

    /**
     * 图书作者.
     */
    @LikeIn("author")
    private List<String> authors;

    /**
     * 总页数.
     */
    @GreaterThan
    private Integer totalPage;

    /**
     * 总页数.
     */
    @OrGreaterThan("totalPage")
    private Integer orTotalPage;

    /**
     * 出版日期.
     */
    @LessThan
    private String publishAt;

    /**
     * 出版日期.
     */
    @OrLessThan("publishAt")
    private String orPublishAt;

    /**
     * 其它扩展信息.
     */
    @IsNull
    private String others;

    /**
     * 其它扩展信息.
     */
    @OrIsNull("others")
    private String orOthers;

}
