package com.blinkfox.fenix.specification.predicate;

import com.blinkfox.fenix.specification.handler.impl.EqualsSpecificationHandler;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

/**
 * Fenix 中用来动态链式构造 {@link Predicate} 的实例.
 *
 * @author blinkfox on 2020-01-17.
 */
public class FenixPredicate {

    /**
     * 动态构建 {@link Predicate} 所需的 {@link CriteriaBuilder} 实例.
     */
    private CriteriaBuilder criteriaBuilder;

    /**
     * 动态构建 {@link Predicate} 所需的 {@link From} 实例.
     */
    private From<?, ?> from;

    /**
     * 构造方法.
     *
     * @param criteriaBuilder {@link CriteriaBuilder} 实例
     * @param from {@link From} 实例
     */
    private FenixPredicate(CriteriaBuilder criteriaBuilder, From<?, ?> from) {
        this.criteriaBuilder = criteriaBuilder;
        this.from = from;
    }

    /**
     * 开始创建 {@link FenixPredicate} 实例的方法.
     *
     * @param criteriaBuilder {@link CriteriaBuilder} 实例
     * @param from {@link From} 实例
     * @return {@link FenixPredicate} 实例
     */
    public static FenixPredicate start(CriteriaBuilder criteriaBuilder, From<?, ?> from) {
        return new FenixPredicate(criteriaBuilder, from);
    }

    /**
     * 结束 {@link Predicate} 的拼接，返回 {@link Predicate} 实例.
     *
     * @return {@link Predicate} 实例
     */
    public Predicate end() {
        return null;
    }

    /**
     * 生成等值查询的 SQL 片段.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 值
     * @return {@link FenixPredicate} 实例
     */
    public FenixPredicate equal(String fieldName, Object value) {
        new EqualsSpecificationHandler().buildPredicate(criteriaBuilder, from, fieldName, value);
        return this;
    }

}
