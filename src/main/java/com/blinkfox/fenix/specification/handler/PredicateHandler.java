package com.blinkfox.fenix.specification.handler;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

/**
 * Fenix 中用来动态构造 {@link Predicate} 条件的函数式接口.
 *
 * @author blinkfox on 2020-01-28.
 * @since v2.2.0
 */
@FunctionalInterface
public interface PredicateHandler {

    /**
     * 根据字段、值等参数来构造 {@link Predicate} 条件实例.
     *
     * @param criteriaBuilder {@link CriteriaBuilder} 实例
     * @param from {@link From} 实例
     * @param fieldName 属性字段名称
     * @param value 属性条件对应的值
     * @return {@link Predicate} 实例
     */
    Predicate buildPredicate(CriteriaBuilder criteriaBuilder, From<?, ?> from, String fieldName, Object value);

}
