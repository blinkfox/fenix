package com.blinkfox.fenix.vo.param;

import com.blinkfox.fenix.specification.annotation.Equals;
import com.blinkfox.fenix.specification.annotation.OrBetween;
import com.blinkfox.fenix.specification.handler.bean.BetweenValue;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 图书搜索的参数实体类.
 *
 * @author blinkfox on 2020-01-28.
 * @since v2.2.0
 */
@Getter
@Setter
@Accessors(chain = true)
public class BookSearch {

    /**
     * ID 集合.
     */
    @Equals
    private String id;

    /**
     * ID 集合.
     */
    @Equals
    private String isbn;

    /**
     * 区间查询页数的对象.
     * 该值的类型可以是 {@link BetweenValue} 类型，也可以是数组或 {@link java.util.List} 类型，
     * 但需要保证数组或集合包含开始值或结束值，两个值不能同时为 {@code null}.
     */
    @OrBetween("totalPage")
    private BetweenValue<Integer> totalPageValue;

}
