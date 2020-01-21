package com.blinkfox.fenix.specification.predicate;

import com.blinkfox.fenix.specification.handler.impl.EqualsSpecificationHandler;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

import lombok.Getter;

/**
 * Fenix 中用来动态链式构造 {@link Predicate} 实例的构造器.
 *
 * @author blinkfox on 2020-01-21.
 * @since v2.2.0
 */
public class FenixPredicateBuilder {

    /**
     * 动态构建 {@link Predicate} 所需的 {@link CriteriaBuilder} 实例.
     */
    @Getter
    private CriteriaBuilder criteriaBuilder;

    /**
     * 动态构建 {@link Predicate} 所需的 {@link From} 实例.
     */
    @Getter
    private From<?, ?> from;

    /**
     * {@link CriteriaQuery} 的查询器实例.
     */
    @Getter
    private CriteriaQuery<?> criteriaQuery;

    /**
     * 动态条件列表集合，作为构造的最终结果返回.
     */
    private List<Predicate> predicates;

    /**
     * 构造方法.
     *
     * @param from {@link From} 实例
     * @param criteriaQuery {@code Criteria} 查询器
     * @param criteriaBuilder {@link CriteriaBuilder} 实例
     */
    public FenixPredicateBuilder(From<?, ?> from, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        this.from = from;
        this.criteriaBuilder = criteriaBuilder;
        this.criteriaQuery = criteriaQuery;
        this.predicates = new ArrayList<>();
    }

    /**
     * 结束 {@link Predicate} 的拼接，返回 {@link Predicate} 的 {@code List} 集合.
     *
     * @return {@link Predicate} 实例
     */
    public List<Predicate> build() {
        return this.predicates;
    }

    /**
     * 生成等值查询的 SQL 片段.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 值
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder equal(String fieldName, Object value) {
        this.predicates.add(new EqualsSpecificationHandler().buildAndPredicate(criteriaBuilder, from, fieldName, value));
        return this;
    }

    /**
     * 生成等值查询的 SQL 片段.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 值
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder equal(String fieldName, Object value, boolean match) {
        return match ? this.equal(fieldName, value) : this;
    }

}
