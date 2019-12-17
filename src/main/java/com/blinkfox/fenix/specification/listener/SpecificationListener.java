package com.blinkfox.fenix.specification.listener;

import java.lang.reflect.Field;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

/**
 * SpecificationFilter
 * 
 * @description Sql构造监听器
 * @author YangWenpeng
 * @date 2019年3月26日 下午6:00:35
 * @version v1.2.0
 */
public interface SpecificationListener {

    /**
     * SpecificationListener
     * 
     * @description 执行监听器逻辑
     * @param param 用来构建条件的参数实体
     * @param field 查询对象的当前字段
     * @param criteriaBuilder
     * @param root
     * @return
     * @author YangWenpeng
     * @date 2019年3月27日 下午4:16:48
     * @version v1.2.0
     * @param <T,Z,X>
     */
    <Z, X> Predicate execute(Object param, Field field, CriteriaBuilder criteriaBuilder, From<Z, X> root);

}
