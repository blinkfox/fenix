package com.blinkfox.fenix.specification.predicate;

import java.util.List;
import javax.persistence.criteria.Predicate;

/**
 * Fenix 中使用 {@link PredicateBuilder} 构建器来动态构造多个 {@link Predicate} 条件的函数式接口.
 *
 * @author blinkfox on 2020-01-21.
 * @since v2.2.0
 */
@FunctionalInterface
public interface FenixPredicate {

    /**
     * 基于 {@link PredicateBuilder} 对象来动态构造多个 {@link Predicate} 条件的接口方法.
     *
     * @param predicateBuilder {@link PredicateBuilder} 对象
     * @return {@link Predicate} 集合
     */
    List<Predicate> toPredicate(PredicateBuilder predicateBuilder);

}
