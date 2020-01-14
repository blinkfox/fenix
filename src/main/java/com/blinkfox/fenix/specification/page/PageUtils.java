package com.blinkfox.fenix.specification.page;

import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

/**
 * 分页的工具类.
 *
 * @author YangWenpeng on 2019-12-17
 * @author blinkfox on 2020-01-14
 * @since v2.2.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PageUtils {

    /**
     * 构建分页参数请求.
     *
     * @param pageAndSort 分页排序参数
     * @return 分页请求
     */
    public static PageRequest buildPageRequest(PageAndSort pageAndSort) {
        return pageAndSort.getOffset() != null
                ? PageRequest.of(pageAndSort.getOffset() / pageAndSort.getLimit(),
                        pageAndSort.getLimit(), sequence(pageAndSort))
                : PageRequest.of(pageAndSort.getPage() - 1, pageAndSort.getLimit(), sequence(pageAndSort));
    }

    /**
     * 根据分页排序参数构建排序的结果.
     *
     * @param pageAndSort 分页排序参数
     * @return {@link Sort} 实例
     */
    public static Sort sequence(PageAndSort pageAndSort) {
        return pageAndSort.getOrders() != null && !pageAndSort.getOrders().isEmpty()
                ? Sort.by(pageAndSort.getOrders())
                : Sort.unsorted();
    }

    /**
     * 根据分页排序参数构建排序的结果.
     *
     * @param direction 排序方向
     * @param field 排序字段
     * @return {@link Order} 实例
     */
    public static Order sequence(Direction direction, String field) {
        return new Order(direction, field);
    }

    /**
     * 将集合转换为分页数据.
     *
     * @param list 集合
     * @param pageable 分页信息
     * @param <T> 范型 T
     * @return 分页数据
     */
    public static <T> Page<T> listConvertToPage(List<T> list, PageAndSort pageable) {
        int start = pageable.getOffset();
        return new PageImpl<>(list.subList(start, Math.min(start + pageable.getLimit(), list.size())),
                PageUtils.buildPageRequest(pageable), list.size());
    }

}
