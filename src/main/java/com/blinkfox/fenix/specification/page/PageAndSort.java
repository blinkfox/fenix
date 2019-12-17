package com.blinkfox.fenix.specification.page;

import java.util.Arrays;
import java.util.List;

import org.springframework.data.domain.Sort.Order;

import com.blinkfox.fenix.specification.util.SortUtils;

/**
 * 
 * PageAndSort
 * 
 * @description 分页参数接收实体类
 * @author yangjunming
 * @date 2019年3月13日 下午2:38:50
 * @version 1.0
 */
public class PageAndSort {
    /**
     * 请求页index
     */
    private int page = PageConsts.DEFAULT_PAGE_PAGE;
    /**
     * 每页显示记录数
     */
    private int limit = PageConsts.DEFAULT_PAGE_LIMIT;

    /**
     * 偏移量
     */
    private Integer offset;

    /** 排序 **/
    private List<Order> orders;

    public PageAndSort() {
        super();
    }

    /**
     * PageAndSort
     * 
     * @description 构造
     * @param page
     * @param limit
     * @param offset
     * @param orders
     * @author YangWenpeng
     * @date 2019年6月10日 下午2:26:40
     * @version v1.0.0
     */
    public PageAndSort(int page, int limit, Integer offset, Order... orders) {
        super();
        this.page = page;
        this.limit = limit;
        this.offset = offset;
        this.orders = Arrays.asList(orders);
    }

    /**
     * PageAndSort
     * 
     * @description 构造
     * @param page
     * @param limit
     * @author YangWenpeng
     * @date 2019年6月10日 下午2:28:37
     * @version v1.0.0
     */
    public PageAndSort(int page, int limit) {
        super();
        this.page = page;
        this.limit = limit;
    }

    /**
     * PageAndSort
     * 
     * @description 构造
     * @param page
     * @param limit
     * @param offset
     * @author YangWenpeng
     * @date 2019年6月10日 下午2:28:37
     * @version v1.0.0
     */
    public PageAndSort(int page, int limit, Integer offset) {
        super();
        this.page = page;
        this.limit = limit;
        this.offset = offset;
    }

    /**
     * PageAndSort
     * 
     * @description 构造
     * @param page
     * @param limit
     * @param orders
     * @author YangWenpeng
     * @date 2019年6月10日 下午2:28:37
     * @version v1.0.0
     */
    public PageAndSort(int page, int limit, Order... orders) {
        super();
        this.page = page;
        this.limit = limit;
        this.orders = Arrays.asList(orders);
    }

    /**
     * 
     * PageAndSort
     * 
     * @description 通过 sort 构造排序
     * @param page
     * @param limit
     * @param offset
     * @param sort
     * @author lichuan
     * @date 2019年9月10日 下午1:49:08
     * @version v1.0.0
     */
    public PageAndSort(int page, int limit, Integer offset, String sort) {
        super();
        this.page = page;
        this.limit = limit;
        this.offset = offset;
        this.orders = SortUtils.toOrders(sort);
    }

    /**
     * @return the page
     */
    public int getPage() {
        return page;
    }

    /**
     * @param page the page to set
     */
    public void setPage(int page) {
        this.page = page;
    }

    /**
     * @return the limit
     */
    public int getLimit() {
        return limit;
    }

    /**
     * @param limit the limit to set
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
    
    /**
     * 
     * PageAndSort
     * 
     * @description 将 sort 字符串转换为 Order 集合<br>
     *              例如："-fyid,create_time" -> [Order(Direction.DESC, "fyid"),Order(Direction.ASC, "create_time")]
     * @param sort
     * @return
     * @author lichuan
     * @date 2019年9月10日 下午2:00:20
     * @version v1.0.0
     */
    public void setSort(String sort) {
        this.orders = SortUtils.toOrders(sort);
    }

}