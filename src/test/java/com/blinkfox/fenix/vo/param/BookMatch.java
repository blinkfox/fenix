package com.blinkfox.fenix.vo.param;

import com.blinkfox.fenix.specification.annotation.Equals;
import com.blinkfox.fenix.specification.annotation.OrBetween;
import com.blinkfox.fenix.specification.handler.bean.BetweenValue;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 图书方法匹配的参数实体类.
 *
 * @author blinkfox on 2020-01-29.
 * @since v2.2.0
 */
@Getter
@Setter
@Accessors(chain = true)
public class BookMatch {

    /**
     * ID.
     */
    @Equals
    private String id;

    /**
     * ISBN.
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

    /**
     * 判断 id 属性是否匹配，结果为真就生成这个匹配条件，否则不生成.
     * 这里假定 ID 不为空，且 ID 不等于 1 时才能生成查询条件.
     *
     * @return 布尔值
     */
    public boolean id() {
        return id != null && !id.equals("1");
    }

}
