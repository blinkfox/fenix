package com.blinkfox.fenix.vo.param;

import com.blinkfox.fenix.specification.annotation.LikeOrLike;
import com.blinkfox.fenix.specification.annotation.OrLikeOrLike;
import java.util.List;
import java.util.Set;
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
public class BookParam3 {

    /**
     * 图书名称或作者.
     */
    @LikeOrLike(fields = {"name", "author"})
    private List<String> nameOrAuthor;

    /**
     * 图书名称或作者.
     */
    @LikeOrLike(fields = {"name", "author"})
    private Set<String> nameOrAuthorSet;

    /**
     * 图书名称或作者.
     */
    @OrLikeOrLike(fields = {"name", "author"})
    private String[] orNameOrAuthor;

    /**
     * 图书名称或作者.
     */
    @OrLikeOrLike(fields = {"name", "author"})
    private Set<String> orNameOrAuthorSet;

}
