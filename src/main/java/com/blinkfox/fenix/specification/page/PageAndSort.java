package com.blinkfox.fenix.specification.page;

import com.blinkfox.fenix.specification.util.SortUtils;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.springframework.data.domain.Sort.Order;

/**
 * 分页和排序的实体类.
 *
 * @author yangjunming on 2019-12-17
 * @author blinkfox on 2020-01-14
 * @since v2.2.0
 */
@Getter
@Setter
public class PageAndSort {

    /**
     * 分页默认每页数据量.
     */
    public static final Integer DEFAULT_PAGE_LIMIT = 10;

    /**
     * 分页默认当前页.
     */
    public static final Integer DEFAULT_PAGE_PAGE = 1;

    /**
     * 分页的默认页数.
     */
    private int page = DEFAULT_PAGE_PAGE;

    /**
     * 每页显示的分页条数.
     */
    private int limit = DEFAULT_PAGE_LIMIT;

    /**
     * 偏移量.
     */
    private Integer offset;

    /**
     * 排序.
     */
    private List<Order> orders;

    /**
     * 构造方法.
     */
    public PageAndSort() {
        super();
    }

    /**
     * 构造方法.
     *
     * @param page 页数
     * @param limit 每页条数
     * @param offset 开始偏移量
     * @param orders 顺序
     */
    public PageAndSort(int page, int limit, Integer offset, Order... orders) {
        super();
        this.page = page;
        this.limit = limit;
        this.offset = offset;
        this.orders = Arrays.asList(orders);
    }

    /**
     * 构造方法.
     *
     * @param page 页数
     * @param limit 每页条数
     */
    public PageAndSort(int page, int limit) {
        super();
        this.page = page;
        this.limit = limit;
    }

    /**
     * 构造方法.
     *
     * @param page 页数
     * @param limit 每页条数
     * @param offset 偏移量
     */
    public PageAndSort(int page, int limit, Integer offset) {
        super();
        this.page = page;
        this.limit = limit;
        this.offset = offset;
    }

    /**
     * 构造方法.
     *
     * @param page 页数
     * @param limit 每页条数
     * @param orders 不定个数的排序实例
     */
    public PageAndSort(int page, int limit, Order... orders) {
        super();
        this.page = page;
        this.limit = limit;
        this.orders = Arrays.asList(orders);
    }

    /**
     * 构造方法.
     *
     * @param page 页数
     * @param limit 每页条数
     * @param offset 偏移量
     * @param sort 不定个数的排序实例
     */
    public PageAndSort(int page, int limit, Integer offset, String sort) {
        super();
        this.page = page;
        this.limit = limit;
        this.offset = offset;
        this.orders = SortUtils.toOrders(sort);
    }

    /**
     * 将 sort 字符串转换为 {@link Order} 集合.
     * 例如：{@code "-fyid,create_time" -> [Order(Direction.DESC, "fyid"), Order(Direction.ASC, "create_time")]}
     *
     * @param sort 排序字段
     */
    public void setSort(String sort) {
        this.orders = SortUtils.toOrders(sort);
    }

}
