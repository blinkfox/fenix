package com.blinkfox.fenix.specification.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

/**
 * 
 * SortUtils
 * 
 * @description 处理 Sort 工具类
 * @author lichuan
 * @date 2019年9月10日 下午1:54:56
 * @version 1.0.0
 */
public final class SortUtils {

    private SortUtils() {}

    /**
     * 
     * SortUtils
     * 
     * @description 将 sort 字符串转换为 Order 集合<br>
     *              例如："-fyid,create_time" -> [Order(Direction.DESC, "fyid"),Order(Direction.ASC, "create_time")]
     * @param sort
     * @return
     * @author lichuan
     * @date 2019年9月10日 下午2:00:20
     * @version v1.0.0
     */
    public static List<Order> toOrders(String sort) {
        List<Order> orders = null;
        if (!StringUtils.isBlank(sort)) {
            String[] sorts = sort.split(",");
            orders = new ArrayList<>();
            for (String s : sorts) {
                if (s.startsWith("-")) {
                    orders.add(new Order(Direction.DESC, s.substring(1)));
                } else {
                    orders.add(new Order(Direction.ASC, s));
                }
            }
        }
        return orders;
    }
    
}
