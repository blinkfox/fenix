package com.blinkfox.fenix.vo.param;

import com.blinkfox.fenix.specification.annotation.EndsWith;
import com.blinkfox.fenix.specification.annotation.Equals;
import com.blinkfox.fenix.specification.annotation.GreaterThan;
import com.blinkfox.fenix.specification.annotation.In;
import com.blinkfox.fenix.specification.annotation.IsNull;
import com.blinkfox.fenix.specification.annotation.LessThan;
import com.blinkfox.fenix.specification.annotation.Like;
import com.blinkfox.fenix.specification.annotation.LikeIn;
import com.blinkfox.fenix.specification.annotation.LikePattern;
import com.blinkfox.fenix.specification.annotation.NotEndsWith;
import com.blinkfox.fenix.specification.annotation.NotLikePattern;
import com.blinkfox.fenix.specification.annotation.NotStartsWith;
import com.blinkfox.fenix.specification.annotation.OrEndsWith;
import com.blinkfox.fenix.specification.annotation.OrEquals;
import com.blinkfox.fenix.specification.annotation.OrGreaterThan;
import com.blinkfox.fenix.specification.annotation.OrIn;
import com.blinkfox.fenix.specification.annotation.OrIsNull;
import com.blinkfox.fenix.specification.annotation.OrLessThan;
import com.blinkfox.fenix.specification.annotation.OrLike;
import com.blinkfox.fenix.specification.annotation.OrLikePattern;
import com.blinkfox.fenix.specification.annotation.OrNotEndsWith;
import com.blinkfox.fenix.specification.annotation.OrNotLikePattern;
import com.blinkfox.fenix.specification.annotation.OrNotStartsWith;
import com.blinkfox.fenix.specification.annotation.OrStartsWith;
import com.blinkfox.fenix.specification.annotation.StartsWith;
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
     * 按前缀匹配图书标题的字段.
     */
    @StartsWith("name")
    private String startsWithName;

    /**
     * 使用或语句按前缀匹配图书标题的字段.
     */
    @OrStartsWith("name")
    private String orStartsWithName;

    /**
     * 按前缀匹配图书标题的字段.
     */
    @NotStartsWith("name")
    private String notStartsWithName;

    /**
     * 使用或语句按前缀匹配图书标题的字段.
     */
    @OrNotStartsWith("name")
    private String orNotStartsWithName;

    /**
     * 按前缀匹配图书标题的字段.
     */
    @EndsWith("name")
    private String endsWithName;

    /**
     * 使用或语句按前缀匹配图书标题的字段.
     */
    @OrEndsWith("name")
    private String orEndsWithName;

    /**
     * 按前缀匹配图书标题的字段.
     */
    @NotEndsWith("name")
    private String notEndsWithName;

    /**
     * 使用或语句按前缀匹配图书标题的字段.
     */
    @OrNotEndsWith("name")
    private String orNotEndsWithName;

    /**
     * 按前缀匹配图书标题的字段.
     */
    @LikePattern("name")
    private String patternName;

    /**
     * 按前缀匹配图书标题的字段.
     */
    @OrLikePattern("name")
    private String orPatternName;

    /**
     * 按前缀匹配图书标题的字段.
     */
    @NotLikePattern("name")
    private String notPatternName;

    /**
     * 按前缀匹配图书标题的字段.
     */
    @OrNotLikePattern("name")
    private String orNotPatternName;

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
