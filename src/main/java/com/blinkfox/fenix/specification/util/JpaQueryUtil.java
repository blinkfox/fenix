package com.blinkfox.fenix.specification.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import com.blinkfox.fenix.specification.SpecificationSupplier;
import com.blinkfox.fenix.specification.page.PageAndSort;
import com.blinkfox.fenix.specification.page.PageUtil;

/**
 * 
 * JpaQueryUtil
 *
 * @description 拼接jpa查询工具类
 * @author wangj-1
 * @date 2019年4月4日 上午9:55:46
 * @version v1.2.0
 */
public final class JpaQueryUtil {

    private JpaQueryUtil() {}

    /**
     * 
     * JpaQueryUtil
     *
     * @description 获取JPA查询Specification,默认传入的字段降序
     * @param param
     * @param defaultOrder
     * @param defaultDirection
     * @return
     * @author wangjing-1
     * @date 2019年4月4日 上午10:21:25
     * @version v1.0.0
     */
    public static <T> Specification<T> getQueryPre(PageAndSort param, String defaultOrder, Direction defaultDirection) {
        if (defaultDirection == null) {
            defaultDirection = Direction.DESC;
        }
        // 默认排序
        if (CollectionUtils.isEmpty(param.getOrders())) {
            List<Order> orderList = new ArrayList<>(10);
            orderList.add(PageUtil.sequence(defaultDirection, defaultOrder));
            param.setOrders(orderList);
        }
        return SpecificationSupplier.buildSpecification(param);
    }

    
    /**
     * 
     * JpaQueryUtil
     *
     * @description 没有排序的Specification
     * @param param
     * @return
     * @author wangjing-1
     * @date 2019年4月22日 下午6:06:56
     * @version v1.2.0
     */
    public static <T> Specification<T> getQueryPre(Object param) {
        return SpecificationSupplier.buildSpecification(param);
    }
    
}
