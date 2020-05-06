package com.blinkfox.fenix.vo.param;

import com.blinkfox.fenix.specification.annotation.Between;
import com.blinkfox.fenix.specification.annotation.GreaterThanEqual;
import com.blinkfox.fenix.specification.annotation.IsNotNull;
import com.blinkfox.fenix.specification.annotation.LessThanEqual;
import com.blinkfox.fenix.specification.annotation.Like;
import com.blinkfox.fenix.specification.annotation.NotBetween;
import com.blinkfox.fenix.specification.annotation.NotEquals;
import com.blinkfox.fenix.specification.annotation.NotIn;
import com.blinkfox.fenix.specification.annotation.OrBetween;
import com.blinkfox.fenix.specification.annotation.OrGreaterThanEqual;
import com.blinkfox.fenix.specification.annotation.OrIsNotNull;
import com.blinkfox.fenix.specification.annotation.OrLessThanEqual;
import com.blinkfox.fenix.specification.annotation.OrNotBetween;
import com.blinkfox.fenix.specification.annotation.OrNotEquals;
import com.blinkfox.fenix.specification.annotation.OrNotIn;
import com.blinkfox.fenix.specification.handler.bean.BetweenValue;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 查询图书的相关参数实体类.
 *
 * @author blinkfox on 2020-01-14.
 * @since v2.2.0
 */
@Getter
@Setter
@Accessors(chain = true)
public class BookParam2 {

    /**
     * ID 集合.
     */
    @NotIn
    private String[] id;

    /**
     * ID 集合.
     */
    @NotIn("id")
    private String singleId;

    /**
     * ID 集合.
     */
    @OrNotIn("id")
    private String[] orIds;

    /**
     * ID 集合.
     */
    @OrNotIn("id")
    private String orSingleId;

    /**
     * 图书的 ISBN 编号.
     */
    @NotEquals
    private String isbn;

    /**
     * 图书的 ISBN 编号.
     */
    @OrNotEquals("isbn")
    private String orIsbn;

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
     * 总页数.
     */
    @OrGreaterThanEqual("totalPage")
    private Integer orTotalPage;

    /**
     * 用于总页数区间查询的数组.
     */
    @Between("totalPage")
    private Integer[] totalPageArr;

    /**
     * 用于总页数区间查询的数组.
     */
    @OrBetween("totalPage")
    private List<Integer> totalPages;

    /**
     * 用于总页数区间查询的数组.
     */
    @NotBetween("totalPage")
    private Integer[] notTotalPageArr;

    /**
     * 用于总页数区间查询的数组.
     */
    @OrNotBetween("totalPage")
    private List<Integer> notTotalPages;

    /**
     * 用于总页数区间查询的数组.
     */
    @Between("totalPage")
    private BetweenValue<Integer> totalPageValue;

    /**
     * 出版日期.
     */
    @LessThanEqual
    private String publishAt;

    /**
     * 出版日期.
     */
    @OrLessThanEqual("publishAt")
    private String orPublishAt;

    /**
     * 其它扩展信息.
     */
    @IsNotNull
    private String others;

    /**
     * 其它扩展信息.
     */
    @OrIsNotNull("others")
    private String orOthers;

}
