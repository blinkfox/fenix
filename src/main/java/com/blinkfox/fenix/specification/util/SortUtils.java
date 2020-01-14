package com.blinkfox.fenix.specification.util;

import com.blinkfox.fenix.helper.StringHelper;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

/**
 * 处理排序的工具类.
 *
 * @author YangWenpeng on 2019-12-17
 * @author blinkfox on 2020-01-14
 * @since v2.2.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SortUtils {

    /**
     * 将 {@code sort} 字符串转换为 {@link Order} 集合.
     * 例如：{@code -fyid,create_time" -> [Order(Direction.DESC, "fyid"), Order(Direction.ASC, "create_time")]}.
     *
     * @param sort 排序的字段
     * @return 排序的 {@link Order} 集合
     */
    public static List<Order> toOrders(String sort) {
        List<Order> orders = null;
        if (StringHelper.isNotBlank(sort)) {
            String[] sorts = sort.split(",");
            orders = new ArrayList<>();
            for (String s : sorts) {
                s = s.trim();
                orders.add(s.startsWith("-")
                        ? new Order(Direction.DESC, s.substring(1))
                        : new Order(Direction.ASC, s));
            }
        }
        return orders;
    }

}
