package com.blinkfox.fenix.specification.util;

import com.blinkfox.fenix.specification.SpecificationSupplier;
import com.blinkfox.fenix.specification.page.PageAndSort;
import com.blinkfox.fenix.specification.page.PageUtils;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

/**
 * 拼接 JPA 查询语句的工具类.
 *
 * @author wangj-1 on 2019-01-17
 * @author blinkfox on 2020-01-14
 * @since v2.2.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JpaQueryUtils {

    /**
     * 获取 JPA 动态查询的 {@link Specification} 实例，如果传入的字段排序方位 {@code null}，则默认为升序.
     *
     * @param pageAndSort      分页和排序的对象实例
     * @param defaultOrder     默认的排序字段
     * @param defaultDirection 排序方向
     * @param <T>              范型 T
     * @return {@link Specification} 实例
     */
    public static <T> Specification<T> getQuerySpecification(
            PageAndSort pageAndSort, String defaultOrder, Direction defaultDirection) {
        // 默认排序.
        if (CollectionUtils.isEmpty(pageAndSort.getOrders())) {
            List<Order> orderList = new ArrayList<>();
            orderList.add(PageUtils.sequence(defaultDirection == null ? Direction.ASC : defaultDirection, defaultOrder));
            pageAndSort.setOrders(orderList);
        }
        return SpecificationSupplier.buildSpecification(pageAndSort);
    }

    /**
     * 获取没有排序的 {@link Specification} 实例.
     *
     * @param param 参数对象
     * @param <T>   范型 T
     * @return {@link Specification} 实例
     */
    public static <T> Specification<T> getQuerySpecification(Object param) {
        return SpecificationSupplier.buildSpecification(param);
    }

}
