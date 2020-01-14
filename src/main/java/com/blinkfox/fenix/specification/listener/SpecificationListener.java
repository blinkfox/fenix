package com.blinkfox.fenix.specification.listener;

import java.lang.reflect.Field;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

/**
 * Spring Data JPA 中 {@link org.springframework.data.jpa.domain.Specification} 执行的 SQL 构造监听器.
 *
 * @author YangWenpeng on 2019-12-17
 * @author blinkfox on 2020-01-14
 * @since v2.2.0
 */
public interface SpecificationListener {

    /**
     * 执行的接口方法.
     *
     * @param param 对象参数
     * @param field 对应的字段
     * @param criteriaBuilder {@link CriteriaBuilder} 实例
     * @param root {@link From} 实例
     * @param <Z> 范型 Z
     * @param <X> 范型 X
     * @return 一个 {@link Predicate} 实例
     */
    <Z, X> Predicate execute(Object param, Field field, CriteriaBuilder criteriaBuilder, From<Z, X> root);

}
