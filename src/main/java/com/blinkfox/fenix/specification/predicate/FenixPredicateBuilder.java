package com.blinkfox.fenix.specification.predicate;

import com.blinkfox.fenix.helper.LambdaHelper;
import com.blinkfox.fenix.helper.LambdaHelper.SFunction;
import com.blinkfox.fenix.specification.handler.AbstractPredicateHandler;
import com.blinkfox.fenix.specification.handler.PredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.*;
import lombok.Getter;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    private final CriteriaBuilder criteriaBuilder;

    /**
     * 动态构建 {@link Predicate} 所需的 {@link From} 实例.
     */
    @Getter
    private final From<?, ?> from;

    /**
     * {@link CriteriaQuery} 的查询器实例.
     */
    @Getter
    private final CriteriaQuery<?> criteriaQuery;

    /**
     * 动态条件列表集合，作为构造的最终结果返回.
     */
    private final List<Predicate> predicates;

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
     * 生成等值查询的 {@link Predicate} 条件.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 值
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder andEquals(String fieldName, Object value) {
        this.predicates.add(new EqualsPredicateHandler().buildPredicate(criteriaBuilder, from, fieldName, value));
        return this;
    }

    public <T, K> FenixPredicateBuilder andEquals(SFunction<? super T, ? extends K> methodRef, Object value) {
        return this.andEquals(getFieldName(methodRef), value);
    }


    /**
     * 生成等值查询的 {@link Predicate} 条件，如果 {@code match} 值为 {@code true} 时则生成该条件，否则不生成.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 值
     * @param match 是否匹配生成此 {@link Predicate} 条件
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder andEquals(String fieldName, Object value, boolean match) {
        return match ? this.andEquals(fieldName, value) : this;
    }

    public <T, K> FenixPredicateBuilder andEquals(SFunction<? super T, ? extends K> methodRef, Object value, boolean match) {
        return this.andEquals(getFieldName(methodRef), value, match);
    }

    /**
     * 生成或语句等值查询的 {@link Predicate} 条件.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 值
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder orEquals(String fieldName, Object value) {
        this.predicates.add(new OrEqualsPredicateHandler().buildPredicate(criteriaBuilder, from, fieldName, value));
        return this;
    }

    public <T, K> FenixPredicateBuilder orEquals(SFunction<? super T, ? extends K> methodRef, Object value) {
        return this.orEquals(getFieldName(methodRef), value);
    }

    /**
     * 生成或语句等值查询的 {@link Predicate} 条件，如果 {@code match} 值为 {@code true} 时则生成该条件，否则不生成.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 值
     * @param match 是否匹配生成此 {@link Predicate} 条件
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder orEquals(String fieldName, Object value, boolean match) {
        return match ? this.orEquals(fieldName, value) : this;
    }

    public <T, K> FenixPredicateBuilder orEquals(SFunction<? super T, ? extends K> methodRef, Object value, boolean match) {
        return this.orEquals(getFieldName(methodRef), value, match);
    }

    /**
     * 生成或语句不等值查询的 {@link Predicate} 条件.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 值
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder andNotEquals(String fieldName, Object value) {
        this.predicates.add(new NotEqualsPredicateHandler().buildPredicate(criteriaBuilder, from, fieldName, value));
        return this;
    }

    public <T, K> FenixPredicateBuilder andNotEquals(SFunction<? super T, ? extends K> methodRef, Object value) {
        this.predicates.add(new NotEqualsPredicateHandler().buildPredicate(criteriaBuilder, from, getFieldName(methodRef), value));
        return this.andNotEquals(getFieldName(methodRef), value);
    }

    /**
     * 生成不等值查询的 {@link Predicate} 条件，如果 {@code match} 值为 {@code true} 时则生成该条件，否则不生成.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 值
     * @param match 是否匹配生成此 {@link Predicate} 条件
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder andNotEquals(String fieldName, Object value, boolean match) {
        return match ? this.andNotEquals(fieldName, value) : this;
    }

    public <T, K> FenixPredicateBuilder andNotEquals(SFunction<? super T, ? extends K> methodRef, Object value, boolean match) {
        return this.andNotEquals(getFieldName(methodRef), value, match);
    }

    /**
     * 生成或语句不等值查询的 {@link Predicate} 条件.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 值
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder orNotEquals(String fieldName, Object value) {
        this.predicates.add(new OrNotEqualsPredicateHandler().buildPredicate(criteriaBuilder, from, fieldName, value));
        return this;
    }

    public <T, K> FenixPredicateBuilder orNotEquals(SFunction<? super T, ? extends K> methodRef, Object value) {
        return this.orNotEquals(getFieldName(methodRef), value);
    }

    /**
     * 生成或语句不等值查询的 {@link Predicate} 条件，如果 {@code match} 值为 {@code true} 时则生成该条件，否则不生成.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 值
     * @param match 是否匹配生成此 {@link Predicate} 条件
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder orNotEquals(String fieldName, Object value, boolean match) {
        return match ? this.orNotEquals(fieldName, value) : this;
    }

    public <T, K> FenixPredicateBuilder orNotEquals(SFunction<? super T, ? extends K> methodRef, Object value, boolean match) {
        return  this.orNotEquals(getFieldName(methodRef), value, match);
    }

    /**
     * 生成大于查询的 {@link Predicate} 条件.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 值
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder andGreaterThan(String fieldName, Object value) {
        this.predicates.add(new GreaterThanPredicateHandler()
                .buildPredicate(criteriaBuilder, from, fieldName, value));
        return this;
    }

    public <T, K> FenixPredicateBuilder andGreaterThan(SFunction<? super T, ? extends K> methodRef, Object value) {
        return this.andGreaterThan(getFieldName(methodRef), value);
    }

    /**
     * 生成大于查询的 {@link Predicate} 条件，如果 {@code match} 值为 {@code true} 时则生成该条件，否则不生成.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 值
     * @param match 是否匹配生成此 {@link Predicate} 条件
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder andGreaterThan(String fieldName, Object value, boolean match) {
        return match ? this.andGreaterThan(fieldName, value) : this;
    }

    public <T, K> FenixPredicateBuilder andGreaterThan(SFunction<? super T, ? extends K> methodRef, Object value, boolean match) {
        return this.andGreaterThan(getFieldName(methodRef), value, match);
    }

    /**
     * 生成或语句大于查询的 {@link Predicate} 条件.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 值
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder orGreaterThan(String fieldName, Object value) {
        this.predicates.add(new OrGreaterThanPredicateHandler()
                .buildPredicate(criteriaBuilder, from, fieldName, value));
        return this;
    }

    public <T, K> FenixPredicateBuilder orGreaterThan(SFunction<? super T, ? extends K> methodRef, Object value) {
        return this.orGreaterThan(getFieldName(methodRef), value);
    }

    /**
     * 生成或语句大于查询的 {@link Predicate} 条件，如果 {@code match} 值为 {@code true} 时则生成该条件，否则不生成.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 值
     * @param match 是否匹配生成此 {@link Predicate} 条件
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder orGreaterThan(String fieldName, Object value, boolean match) {
        return match ? this.orGreaterThan(fieldName, value) : this;
    }

    public <T, K> FenixPredicateBuilder orGreaterThan(SFunction<? super T, ? extends K> methodRef, Object value, boolean match) {
        return this.orGreaterThan(getFieldName(methodRef), value, match);
    }

    /**
     * 生成大于等于查询的 {@link Predicate} 条件.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 值
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder andGreaterThanEqual(String fieldName, Object value) {
        this.predicates.add(new GreaterThanEqualPredicateHandler()
                .buildPredicate(criteriaBuilder, from, fieldName, value));
        return this;
    }

    public <T, K> FenixPredicateBuilder andGreaterThanEqual(SFunction<? super T, ? extends K> methodRef, Object value) {
        return this.andGreaterThanEqual(getFieldName(methodRef), value);
    }

    /**
     * 生成大于等于查询的 {@link Predicate} 条件，如果 {@code match} 值为 {@code true} 时则生成该条件，否则不生成.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 值
     * @param match 是否匹配生成此 {@link Predicate} 条件
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder andGreaterThanEqual(String fieldName, Object value, boolean match) {
        return match ? this.andGreaterThanEqual(fieldName, value) : this;
    }

    public <T, K> FenixPredicateBuilder andGreaterThanEqual(SFunction<? super T, ? extends K> methodRef, Object value, boolean match) {
        return this.andGreaterThanEqual(getFieldName(methodRef), value, match);
    }

    /**
     * 生成或语句大于等于查询的 {@link Predicate} 条件.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 值
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder orGreaterThanEqual(String fieldName, Object value) {
        this.predicates.add(new OrGreaterThanEqualPredicateHandler()
                .buildPredicate(criteriaBuilder, from, fieldName, value));
        return this;
    }

    public <T, K> FenixPredicateBuilder orGreaterThanEqual(SFunction<? super T, ? extends K> methodRef, Object value) {
        return this.orGreaterThanEqual(getFieldName(methodRef), value);
    }

    /**
     * 生成或语句大于等于查询的 {@link Predicate} 条件，如果 {@code match} 值为 {@code true} 时则生成该条件，否则不生成.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 值
     * @param match 是否匹配生成此 {@link Predicate} 条件
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder orGreaterThanEqual(String fieldName, Object value, boolean match) {
        return match ? this.orGreaterThanEqual(fieldName, value) : this;
    }

    public <T, K> FenixPredicateBuilder orGreaterThanEqual(SFunction<? super T, ? extends K> methodRef, Object value, boolean match) {
        return this.orGreaterThanEqual(getFieldName(methodRef), value, match);
    }

    /**
     * 生成小于查询的 {@link Predicate} 条件.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 值
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder andLessThan(String fieldName, Object value) {
        this.predicates.add(new LessThanPredicateHandler().buildPredicate(criteriaBuilder, from, fieldName, value));
        return this;
    }

    public <T, K> FenixPredicateBuilder andLessThan(SFunction<? super T, ? extends K> methodRef, Object value) {
        return this.andLessThan(getFieldName(methodRef), value);
    }

    /**
     * 生成小于查询的 {@link Predicate} 条件，如果 {@code match} 值为 {@code true} 时则生成该条件，否则不生成.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 值
     * @param match 是否匹配生成此 {@link Predicate} 条件
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder andLessThan(String fieldName, Object value, boolean match) {
        return match ? this.andLessThan(fieldName, value) : this;
    }

    public <T, K> FenixPredicateBuilder andLessThan(SFunction<? super T, ? extends K> methodRef, Object value, boolean match) {
        return this.andLessThan(getFieldName(methodRef), value, match);
    }

    /**
     * 生成或语句小于查询的 {@link Predicate} 条件.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 值
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder orLessThan(String fieldName, Object value) {
        this.predicates.add(new OrLessThanPredicateHandler()
                .buildPredicate(criteriaBuilder, from, fieldName, value));
        return this;
    }

    public <T, K> FenixPredicateBuilder orLessThan(SFunction<? super T, ? extends K> methodRef, Object value) {
        return this.orLessThan(getFieldName(methodRef), value);
    }


    /**
     * 生成或语句小于查询的 {@link Predicate} 条件，如果 {@code match} 值为 {@code true} 时则生成该条件，否则不生成.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 值
     * @param match 是否匹配生成此 {@link Predicate} 条件
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder orLessThan(String fieldName, Object value, boolean match) {
        return match ? this.orLessThan(fieldName, value) : this;
    }

    public <T, K> FenixPredicateBuilder orLessThan(SFunction<? super T, ? extends K> methodRef, Object value, boolean match) {
        return this.orLessThan(getFieldName(methodRef), value, match);
    }

    /**
     * 生成小于等于查询的 {@link Predicate} 条件.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 值
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder andLessThanEqual(String fieldName, Object value) {
        this.predicates.add(new LessThanEqualPredicateHandler()
                .buildPredicate(criteriaBuilder, from, fieldName, value));
        return this;
    }

    public <T, K> FenixPredicateBuilder andLessThanEqual(SFunction<? super T, ? extends K> methodRef, Object value) {
        return this.andLessThanEqual(getFieldName(methodRef), value);
    }

    /**
     * 生成小于等于查询的 {@link Predicate} 条件，如果 {@code match} 值为 {@code true} 时则生成该条件，否则不生成.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 值
     * @param match 是否匹配生成此 {@link Predicate} 条件
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder andLessThanEqual(String fieldName, Object value, boolean match) {
        return match ? this.andLessThanEqual(fieldName, value) : this;
    }

    public <T, K> FenixPredicateBuilder andLessThanEqual(SFunction<? super T, ? extends K> methodRef, Object value, boolean match) {
        return  this.andLessThanEqual(getFieldName(methodRef), value, match);
    }

    /**
     * 生成或语句小于等于查询的 {@link Predicate} 条件.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 值
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder orLessThanEqual(String fieldName, Object value) {
        this.predicates.add(new OrLessThanEqualPredicateHandler()
                .buildPredicate(criteriaBuilder, from, fieldName, value));
        return this;
    }

    public <T, K> FenixPredicateBuilder orLessThanEqual(SFunction<? super T, ? extends K> methodRef, Object value) {
        return this.orLessThanEqual(getFieldName(methodRef), value);
    }

    /**
     * 生成或语句小于等于查询的 {@link Predicate} 条件，如果 {@code match} 值为 {@code true} 时则生成该条件，否则不生成.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 值
     * @param match 是否匹配生成此 {@link Predicate} 条件
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder orLessThanEqual(String fieldName, Object value, boolean match) {
        return match ? this.orLessThanEqual(fieldName, value) : this;
    }

    public <T, K> FenixPredicateBuilder orLessThanEqual(SFunction<? super T, ? extends K> methodRef, Object value, boolean match) {
        return this.orLessThanEqual(getFieldName(methodRef), value, match);
    }

    /**
     * 生成区间匹配时的 {@link Predicate} 条件.
     * 若结束值为空，则退化生成为大于等于的条件，若开始值为空.则退化生成为小于等于的条件，若开始值或结束值都为空，则直接抛出异常.
     *
     * @param fieldName 实体属性或数据库字段
     * @param startValue 区间开始值
     * @param endValue 区间结束值
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder andBetween(String fieldName, Object startValue, Object endValue) {
        this.predicates.add(new BetweenPredicateHandler()
                .buildPredicate(criteriaBuilder, from, fieldName, new Object[] {startValue, endValue}));
        return this;
    }

    public <T, K> FenixPredicateBuilder andBetween(SFunction<? super T, ? extends K> methodRef, Object startValue, Object endValue) {
        return this.andBetween(getFieldName(methodRef), startValue, endValue);
    }

    /**
     * 生成区间匹配时的 {@link Predicate} 条件，如果 {@code match} 值为 {@code true} 时则生成该条件，否则不生成.
     * 若结束值为空，则退化生成为大于等于的条件，若开始值为空.则退化生成为小于等于的条件，若开始值或结束值都为空，则直接抛出异常.
     *
     * @param fieldName 实体属性或数据库字段
     * @param startValue 区间开始值
     * @param endValue 区间结束值
     * @param match 是否匹配生成此 {@link Predicate} 条件
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder andBetween(String fieldName, Object startValue, Object endValue, boolean match) {
        return match ? this.andBetween(fieldName, startValue, endValue) : this;
    }

    public <T, K> FenixPredicateBuilder andBetween(SFunction<? super T, ? extends K> methodRef, Object startValue, Object endValue, boolean match) {
        return this.andBetween(getFieldName(methodRef), startValue, endValue, match);
    }

    /**
     * 生成或语句区间匹配时的 {@link Predicate} 条件.
     * 若结束值为空，则退化生成为大于等于的条件，若开始值为空.则退化生成为小于等于的条件，若开始值或结束值都为空，则直接抛出异常.
     *
     * @param fieldName 实体属性或数据库字段
     * @param startValue 区间开始值
     * @param endValue 区间结束值
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder orBetween(String fieldName, Object startValue, Object endValue) {
        this.predicates.add(new OrBetweenPredicateHandler()
                .buildPredicate(criteriaBuilder, from, fieldName, new Object[] {startValue, endValue}));
        return this;
    }

    public <T, K> FenixPredicateBuilder orBetween(SFunction<? super T, ? extends K> methodRef, Object startValue, Object endValue) {
        return this.orBetween(getFieldName(methodRef), startValue, endValue);
    }

    /**
     * 生成或语句区间匹配时的 {@link Predicate} 条件，如果 {@code match} 值为 {@code true} 时则生成该条件，否则不生成.
     * 若结束值为空，则退化生成为大于等于的条件，若开始值为空.则退化生成为小于等于的条件，若开始值或结束值都为空，则直接抛出异常.
     *
     * @param fieldName 实体属性或数据库字段
     * @param startValue 区间开始值
     * @param endValue 区间结束值
     * @param match 是否匹配生成此 {@link Predicate} 条件
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder orBetween(String fieldName, Object startValue, Object endValue, boolean match) {
        return match ? this.orBetween(fieldName, startValue, endValue) : this;
    }

    public <T, K> FenixPredicateBuilder orBetween(SFunction<? super T, ? extends K> methodRef, Object startValue, Object endValue, boolean match) {
        return this.orBetween(getFieldName(methodRef), startValue, endValue, match) ;
    }

    /**
     * 生成区间不匹配时的 {@link Predicate} 条件.
     * 若结束值为空，则退化生成为大于等于的条件，若开始值为空.则退化生成为小于等于的条件，若开始值或结束值都为空，则直接抛出异常.
     *
     * @param fieldName 实体属性或数据库字段
     * @param startValue 区间开始值
     * @param endValue 区间结束值
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder andNotBetween(String fieldName, Object startValue, Object endValue) {
        this.predicates.add(new NotBetweenPredicateHandler()
                .buildPredicate(criteriaBuilder, from, fieldName, new Object[] {startValue, endValue}));
        return this;
    }

    public <T, K> FenixPredicateBuilder andNotBetween(SFunction<? super T, ? extends K> methodRef, Object startValue, Object endValue) {
        return this.andNotBetween(getFieldName(methodRef), startValue, endValue);
    }

    /**
     * 生成区间不匹配时的 {@link Predicate} 条件，如果 {@code match} 值为 {@code true} 时则生成该条件，否则不生成.
     * 若结束值为空，则退化生成为大于等于的条件，若开始值为空.则退化生成为小于等于的条件，若开始值或结束值都为空，则直接抛出异常.
     *
     * @param fieldName 实体属性或数据库字段
     * @param startValue 区间开始值
     * @param endValue 区间结束值
     * @param match 是否匹配生成此 {@link Predicate} 条件
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder andNotBetween(String fieldName, Object startValue, Object endValue, boolean match) {
        return match ? this.andNotBetween(fieldName, startValue, endValue) : this;
    }

    public <T, K> FenixPredicateBuilder andNotBetween(SFunction<? super T, ? extends K> methodRef, Object startValue, Object endValue, boolean match) {
        return this.andNotBetween(getFieldName(methodRef), startValue, endValue, match) ;
    }

    /**
     * 生成或语句区间不匹配时的 {@link Predicate} 条件.
     * 若结束值为空，则退化生成为大于等于的条件，若开始值为空.则退化生成为小于等于的条件，若开始值或结束值都为空，则直接抛出异常.
     *
     * @param fieldName 实体属性或数据库字段
     * @param startValue 区间开始值
     * @param endValue 区间结束值
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder orNotBetween(String fieldName, Object startValue, Object endValue) {
        this.predicates.add(new OrNotBetweenPredicateHandler()
                .buildPredicate(criteriaBuilder, from, fieldName, new Object[] {startValue, endValue}));
        return this;
    }

    public <T, K> FenixPredicateBuilder orNotBetween(SFunction<? super T, ? extends K> methodRef, Object startValue, Object endValue) {
        return this.orNotBetween(getFieldName(methodRef), startValue, endValue);
    }

    /**
     * 生成或语句区间不匹配时的 {@link Predicate} 条件，如果 {@code match} 值为 {@code true} 时则生成该条件，否则不生成.
     * 若结束值为空，则退化生成为大于等于的条件，若开始值为空.则退化生成为小于等于的条件，若开始值或结束值都为空，则直接抛出异常.
     *
     * @param fieldName 实体属性或数据库字段
     * @param startValue 区间开始值
     * @param endValue 区间结束值
     * @param match 是否匹配生成此 {@link Predicate} 条件
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder orNotBetween(String fieldName, Object startValue, Object endValue, boolean match) {
        return match ? this.orNotBetween(fieldName, startValue, endValue) : this;
    }

    public <T, K> FenixPredicateBuilder orNotBetween(SFunction<? super T, ? extends K> methodRef, Object startValue, Object endValue, boolean match) {
        return this.orNotBetween(getFieldName(methodRef), startValue, endValue, match) ;
    }

    /**
     * 生成 {@code LIKE} 模糊查询的 {@link Predicate} 条件.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 值
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder andLike(String fieldName, Object value) {
        this.predicates.add(new LikePredicateHandler().buildPredicate(criteriaBuilder, from, fieldName, value));
        return this;
    }

    public <T, K> FenixPredicateBuilder andLike(SFunction<? super T, ? extends K> methodRef, Object value) {
        return this.andLike(getFieldName(methodRef), value);
    }

    /**
     * 生成 {@code LIKE} 模糊查询的 {@link Predicate} 条件，如果 {@code match} 值为 {@code true} 时则生成该条件，否则不生成.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 值
     * @param match 是否匹配生成此 {@link Predicate} 条件
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder andLike(String fieldName, Object value, boolean match) {
        return match ? this.andLike(fieldName, value) : this;
    }

    public <T, K> FenixPredicateBuilder andLike(SFunction<? super T, ? extends K> methodRef, Object value, boolean match) {
        return this.andLike(getFieldName(methodRef), value, match) ;
    }

    /**
     * 生成或语句 {@code LIKE} 模糊查询的 {@link Predicate} 条件.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 值
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder orLike(String fieldName, Object value) {
        this.predicates.add(new OrLikePredicateHandler().buildPredicate(criteriaBuilder, from, fieldName, value));
        return this;
    }

    public <T, K> FenixPredicateBuilder orLike(SFunction<? super T, ? extends K> methodRef, Object value) {
        return this.orLike(getFieldName(methodRef), value);
    }

    /**
     * 生成或语句 {@code LIKE} 模糊查询的 {@link Predicate} 条件，如果 {@code match} 值为 {@code true} 时则生成该条件，否则不生成.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 值
     * @param match 是否匹配生成此 {@link Predicate} 条件
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder orLike(String fieldName, Object value, boolean match) {
        return match ? this.orLike(fieldName, value) : this;
    }

    public <T, K> FenixPredicateBuilder orLike(SFunction<? super T, ? extends K> methodRef, Object value, boolean match) {
        return this.orLike(getFieldName(methodRef), value, match);
    }

    /**
     * 生成 {@code NOT LIKE} 模糊不匹配查询的 {@link Predicate} 条件.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 值
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder andNotLike(String fieldName, Object value) {
        this.predicates.add(new NotLikePredicateHandler().buildPredicate(criteriaBuilder, from, fieldName, value));
        return this;
    }

    public <T, K> FenixPredicateBuilder andNotLike(SFunction<? super T, ? extends K> methodRef, Object value) {
        return this.andNotLike(getFieldName(methodRef), value);
    }

    /**
     * 生成 {@code NOT LIKE} 模糊不匹配查询的 {@link Predicate} 条件，如果 {@code match} 值为 {@code true} 时则生成该条件，否则不生成.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 值
     * @param match 是否匹配生成此 {@link Predicate} 条件
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder andNotLike(String fieldName, Object value, boolean match) {
        return match ? this.andNotLike(fieldName, value) : this;
    }

    public <T, K> FenixPredicateBuilder andNotLike(SFunction<? super T, ? extends K> methodRef, Object value, boolean match) {
        return this.andNotLike(getFieldName(methodRef), value, match);
    }

    /**
     * 生成或语句 {@code OR NOT LIKE} 模糊不匹配查询的 {@link Predicate} 条件.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 值
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder orNotLike(String fieldName, Object value) {
        this.predicates.add(new OrNotLikePredicateHandler().buildPredicate(criteriaBuilder, from, fieldName, value));
        return this;
    }

    public <T, K> FenixPredicateBuilder orNotLike(SFunction<? super T, ? extends K> methodRef, Object value) {
        return this.orNotLike(getFieldName(methodRef), value);
    }

    /**
     * 生成或语句 {@code OR NOT LIKE} 模糊不匹配查询的 {@link Predicate} 条件，如果 {@code match} 值为 {@code true} 时则生成该条件，否则不生成.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 值
     * @param match 是否匹配生成此 {@link Predicate} 条件
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder orNotLike(String fieldName, Object value, boolean match) {
        return match ? this.orNotLike(fieldName, value) : this;
    }

    public <T, K> FenixPredicateBuilder orNotLike(SFunction<? super T, ? extends K> methodRef, Object value, boolean match) {
        return this.orNotLike(getFieldName(methodRef), value,match);
    }

    /**
     * 生成 {@code LIKE} 按前缀模糊匹配查询的 {@link Predicate} 条件.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 值
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder andStartsWith(String fieldName, Object value) {
        this.predicates.add(new StartsWithPredicateHandler().buildPredicate(criteriaBuilder, from, fieldName, value));
        return this;
    }

    public <T, K> FenixPredicateBuilder andStartsWith(SFunction<? super T, ? extends K> methodRef, Object value) {
        return this.andStartsWith(getFieldName(methodRef), value);
    }

    /**
     * 生成 {@code LIKE} 按前缀模糊匹配查询的 {@link Predicate} 条件，如果 {@code match} 值为 {@code true} 时则生成该条件，否则不生成.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 值
     * @param match 是否匹配生成此 {@link Predicate} 条件
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder andStartsWith(String fieldName, Object value, boolean match) {
        return match ? this.andStartsWith(fieldName, value) : this;
    }

    public <T, K> FenixPredicateBuilder andStartsWith(SFunction<? super T, ? extends K> methodRef, Object value, boolean match) {
        return this.andStartsWith(getFieldName(methodRef), value,match);
    }

    /**
     * 生成或语句 {@code LIKE} 按前缀模糊匹配查询的 {@link Predicate} 条件.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 值
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder orStartsWith(String fieldName, Object value) {
        this.predicates.add(new OrStartsWithPredicateHandler()
                .buildPredicate(criteriaBuilder, from, fieldName, value));
        return this;
    }

    public <T, K> FenixPredicateBuilder orStartsWith(SFunction<? super T, ? extends K> methodRef, Object value) {
        return this.orStartsWith(getFieldName(methodRef), value);
    }

    /**
     * 生成或语句 {@code LIKE} 按前缀模糊匹配查询的 {@link Predicate} 条件，如果 {@code match} 值为 {@code true} 时则生成该条件，否则不生成.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 值
     * @param match 是否匹配生成此 {@link Predicate} 条件
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder orStartsWith(String fieldName, Object value, boolean match) {
        return match ? this.orStartsWith(fieldName, value) : this;
    }

    public <T, K> FenixPredicateBuilder orStartsWith(SFunction<? super T, ? extends K> methodRef, Object value, boolean match) {
        return this.orStartsWith(getFieldName(methodRef), value, match);
    }

    /**
     * 生成 {@code LIKE} 按前缀模糊不匹配查询的 {@link Predicate} 条件.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 值
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder andNotStartsWith(String fieldName, Object value) {
        this.predicates.add(new NotStartsWithPredicateHandler()
                .buildPredicate(criteriaBuilder, from, fieldName, value));
        return this;
    }

    public <T, K> FenixPredicateBuilder andNotStartsWith(SFunction<? super T, ? extends K> methodRef, Object value) {
        return this.andNotStartsWith(getFieldName(methodRef), value);
    }

    /**
     * 生成 {@code LIKE} 按前缀模糊不匹配查询的 {@link Predicate} 条件，如果 {@code match} 值为 {@code true} 时则生成该条件，否则不生成.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 值
     * @param match 是否匹配生成此 {@link Predicate} 条件
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder andNotStartsWith(String fieldName, Object value, boolean match) {
        return match ? this.andNotStartsWith(fieldName, value) : this;
    }

    public <T, K> FenixPredicateBuilder andNotStartsWith(SFunction<? super T, ? extends K> methodRef, Object value, boolean match) {
        return this.andNotStartsWith(getFieldName(methodRef), value,match);
    }

    /**
     * 生成或语句 {@code LIKE} 按前缀模糊不匹配查询的 {@link Predicate} 条件.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 值
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder orNotStartsWith(String fieldName, Object value) {
        this.predicates.add(new OrNotStartsWithPredicateHandler()
                .buildPredicate(criteriaBuilder, from, fieldName, value));
        return this;
    }

    public <T, K> FenixPredicateBuilder orNotStartsWith(SFunction<? super T, ? extends K> methodRef, Object value) {
        return this.orNotStartsWith(getFieldName(methodRef), value);
    }

    /**
     * 生成或语句 {@code LIKE} 按前缀模糊不匹配查询的 {@link Predicate} 条件，如果 {@code match} 值为 {@code true} 时则生成该条件，否则不生成.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 值
     * @param match 是否匹配生成此 {@link Predicate} 条件
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder orNotStartsWith(String fieldName, Object value, boolean match) {
        return match ? this.orNotStartsWith(fieldName, value) : this;
    }

    public <T, K> FenixPredicateBuilder orNotStartsWith(SFunction<? super T, ? extends K> methodRef, Object value, boolean match) {
        return this.orNotStartsWith(getFieldName(methodRef), value, match);
    }

    /**
     * 生成 {@code LIKE} 按后缀模糊匹配查询的 {@link Predicate} 条件.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 值
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder andEndsWith(String fieldName, Object value) {
        this.predicates.add(new EndsWithPredicateHandler().buildPredicate(criteriaBuilder, from, fieldName, value));
        return this;
    }

    public <T, K> FenixPredicateBuilder andEndsWith(SFunction<? super T, ? extends K> methodRef, Object value) {
        return this.andEndsWith(getFieldName(methodRef), value);
    }


    /**
     * 生成 {@code LIKE} 按后缀模糊匹配查询的 {@link Predicate} 条件，如果 {@code match} 值为 {@code true} 时则生成该条件，否则不生成.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 值
     * @param match 是否匹配生成此 {@link Predicate} 条件
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder andEndsWith(String fieldName, Object value, boolean match) {
        return match ? this.andEndsWith(fieldName, value) : this;
    }

    public <T, K> FenixPredicateBuilder andEndsWith(SFunction<? super T, ? extends K> methodRef, Object value, boolean match) {
        return this.andEndsWith(getFieldName(methodRef), value, match);
    }

    /**
     * 生成或语句 {@code LIKE} 按后缀模糊匹配查询的 {@link Predicate} 条件.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 值
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder orEndsWith(String fieldName, Object value) {
        this.predicates.add(new OrEndsWithPredicateHandler()
                .buildPredicate(criteriaBuilder, from, fieldName, value));
        return this;
    }

    public <T, K> FenixPredicateBuilder orEndsWith(SFunction<? super T, ? extends K> methodRef, Object value) {
        return this.orEndsWith(getFieldName(methodRef), value);
    }

    /**
     * 生成或语句 {@code LIKE} 按后缀模糊匹配查询的 {@link Predicate} 条件，如果 {@code match} 值为 {@code true} 时则生成该条件，否则不生成.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 值
     * @param match 是否匹配生成此 {@link Predicate} 条件
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder orEndsWith(String fieldName, Object value, boolean match) {
        return match ? this.orEndsWith(fieldName, value) : this;
    }

    public <T, K> FenixPredicateBuilder orEndsWith(SFunction<? super T, ? extends K> methodRef, Object value, boolean match) {
        return this.orEndsWith(getFieldName(methodRef), value, match);
    }

    /**
     * 生成 {@code LIKE} 按后缀模糊不匹配查询的 {@link Predicate} 条件.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 值
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder andNotEndsWith(String fieldName, Object value) {
        this.predicates.add(new NotEndsWithPredicateHandler().buildPredicate(criteriaBuilder, from, fieldName, value));
        return this;
    }

    public <T, K> FenixPredicateBuilder andNotEndsWith(SFunction<? super T, ? extends K> methodRef, Object value) {
        return this.andNotEndsWith(getFieldName(methodRef), value);
    }

    /**
     * 生成 {@code LIKE} 按后缀模糊不匹配查询的 {@link Predicate} 条件，如果 {@code match} 值为 {@code true} 时则生成该条件，否则不生成.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 值
     * @param match 是否匹配生成此 {@link Predicate} 条件
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder andNotEndsWith(String fieldName, Object value, boolean match) {
        return match ? this.andNotEndsWith(fieldName, value) : this;
    }

    public <T, K> FenixPredicateBuilder andNotEndsWith(SFunction<? super T, ? extends K> methodRef, Object value, boolean match) {
        return this.andNotEndsWith(getFieldName(methodRef), value, match);
    }

    /**
     * 生成或语句 {@code LIKE} 按后缀模糊不匹配查询的 {@link Predicate} 条件.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 值
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder orNotEndsWith(String fieldName, Object value) {
        this.predicates.add(new OrNotEndsWithPredicateHandler()
                .buildPredicate(criteriaBuilder, from, fieldName, value));
        return this;
    }

    public <T, K> FenixPredicateBuilder orNotEndsWith(SFunction<? super T, ? extends K> methodRef, Object value) {
        return this.orNotEndsWith(getFieldName(methodRef), value);
    }

    /**
     * 生成或语句 {@code LIKE} 按后缀模糊不匹配查询的 {@link Predicate} 条件，如果 {@code match} 值为 {@code true} 时则生成该条件，否则不生成.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 值
     * @param match 是否匹配生成此 {@link Predicate} 条件
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder orNotEndsWith(String fieldName, Object value, boolean match) {
        return match ? this.orNotEndsWith(fieldName, value) : this;
    }

    public <T, K> FenixPredicateBuilder orNotEndsWith(SFunction<? super T, ? extends K> methodRef, Object value, boolean match) {
        return this.orNotEndsWith(getFieldName(methodRef), value, match);
    }

    /**
     * 生成 {@code LIKE} 按指定模式模糊匹配的 {@link Predicate} 条件.
     *
     * @param fieldName 实体属性或数据库字段
     * @param pattern 匹配的模式字符串
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder andLikePattern(String fieldName, String pattern) {
        this.predicates.add(new LikePatternPredicateHandler()
                .buildPredicate(criteriaBuilder, from, fieldName, pattern));
        return this;
    }

    public <T, K> FenixPredicateBuilder andLikePattern(SFunction<? super T, ? extends K> methodRef, String pattern) {
        return this.andLikePattern(getFieldName(methodRef), pattern);
    }

    /**
     * 生成 {@code LIKE} 按指定模式模糊匹配的 {@link Predicate} 条件，如果 {@code match} 值为 {@code true} 时则生成该条件，否则不生成.
     *
     * @param fieldName 实体属性或数据库字段
     * @param pattern 匹配的模式字符串
     * @param match 是否匹配生成此 {@link Predicate} 条件
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder andLikePattern(String fieldName, String pattern, boolean match) {
        return match ? this.andLikePattern(fieldName, pattern) : this;
    }

    public <T, K> FenixPredicateBuilder andLikePattern(SFunction<? super T, ? extends K> methodRef, String pattern, boolean match) {
        return this.andLikePattern(getFieldName(methodRef), pattern, match);
    }

    /**
     * 生成或语句 {@code LIKE} 按指定模式模糊匹配的 {@link Predicate} 条件.
     *
     * @param fieldName 实体属性或数据库字段
     * @param pattern 匹配的模式字符串
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder orLikePattern(String fieldName, String pattern) {
        this.predicates.add(new OrLikePatternPredicateHandler()
                .buildPredicate(criteriaBuilder, from, fieldName, pattern));
        return this;
    }

    public <T, K> FenixPredicateBuilder orLikePattern(SFunction<? super T, ? extends K> methodRef, String pattern) {
        return this.orLikePattern(getFieldName(methodRef), pattern);
    }

    /**
     * 生成或语句 {@code LIKE} 按指定模式模糊匹配的 {@link Predicate} 条件，如果 {@code match} 值为 {@code true} 时则生成该条件，否则不生成.
     *
     * @param fieldName 实体属性或数据库字段
     * @param pattern 匹配的模式字符串
     * @param match 是否匹配生成此 {@link Predicate} 条件
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder orLikePattern(String fieldName, String pattern, boolean match) {
        return match ? this.orLikePattern(fieldName, pattern) : this;
    }

    public <T, K> FenixPredicateBuilder orLikePattern(SFunction<? super T, ? extends K> methodRef, String pattern, boolean match) {
        return this.orLikePattern(getFieldName(methodRef), pattern, match);
    }

    /**
     * 生成 {@code LIKE} 按指定模式模糊不匹配的 {@link Predicate} 条件.
     *
     * @param fieldName 实体属性或数据库字段
     * @param pattern 匹配的模式字符串
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder andNotLikePattern(String fieldName, String pattern) {
        this.predicates.add(new NotLikePatternPredicateHandler()
                .buildPredicate(criteriaBuilder, from, fieldName, pattern));
        return this;
    }

    public <T, K> FenixPredicateBuilder andNotLikePattern(SFunction<? super T, ? extends K> methodRef, String pattern) {
        return this.andNotLikePattern(getFieldName(methodRef), pattern);
    }

    /**
     * 生成 {@code LIKE} 按指定模式模糊不匹配的 {@link Predicate} 条件，如果 {@code match} 值为 {@code true} 时则生成该条件，否则不生成.
     *
     * @param fieldName 实体属性或数据库字段
     * @param pattern 匹配的模式字符串
     * @param match 是否匹配生成此 {@link Predicate} 条件
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder andNotLikePattern(String fieldName, String pattern, boolean match) {
        return match ? this.andNotLikePattern(fieldName, pattern) : this;
    }

    public <T, K> FenixPredicateBuilder andNotLikePattern(SFunction<? super T, ? extends K> methodRef, String pattern, boolean match) {
        return this.andNotLikePattern(getFieldName(methodRef), pattern, match);
    }

    /**
     * 生成或语句 {@code LIKE} 按指定模式模糊不匹配的 {@link Predicate} 条件.
     *
     * @param fieldName 实体属性或数据库字段
     * @param pattern 匹配的模式字符串
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder orNotLikePattern(String fieldName, String pattern) {
        this.predicates.add(new OrNotLikePatternPredicateHandler()
                .buildPredicate(criteriaBuilder, from, fieldName, pattern));
        return this;
    }

    public <T, K> FenixPredicateBuilder orNotLikePattern(SFunction<? super T, ? extends K> methodRef, String pattern) {
        return this.orNotLikePattern(getFieldName(methodRef), pattern);
    }

    /**
     * 生成或语句 {@code LIKE} 按指定模式模糊不匹配的 {@link Predicate} 条件，如果 {@code match} 值为 {@code true} 时则生成该条件，否则不生成.
     *
     * @param fieldName 实体属性或数据库字段
     * @param pattern 匹配的模式字符串
     * @param match 是否匹配生成此 {@link Predicate} 条件
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder orNotLikePattern(String fieldName, String pattern, boolean match) {
        return match ? this.orNotLikePattern(fieldName, pattern) : this;
    }

    public <T, K> FenixPredicateBuilder orNotLikePattern(SFunction<? super T, ? extends K> methodRef, String pattern, boolean match) {
        return this.orNotLikePattern(getFieldName(methodRef), pattern, match);
    }

    /**
     * 生成 {@code IN} 范围匹配的 {@link Predicate} 条件.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 集合
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder andIn(String fieldName, Collection<?> value) {
        this.predicates.add(new InPredicateHandler().buildPredicate(criteriaBuilder, from, fieldName, value));
        return this;
    }

    public <T, K> FenixPredicateBuilder andIn(SFunction<? super T, ? extends K> methodRef, Collection<?> value) {
        return this.andIn(getFieldName(methodRef), value);
    }

    /**
     * 生成 {@code IN} 范围匹配的 {@link Predicate} 条件，如果 {@code match} 值为 {@code true} 时则生成该条件，否则不生成.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 集合
     * @param match 是否匹配生成此 {@link Predicate} 条件
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder andIn(String fieldName, Collection<?> value, boolean match) {
        return match ? this.andIn(fieldName, value) : this;
    }

    public <T, K> FenixPredicateBuilder andIn(SFunction<? super T, ? extends K> methodRef, Collection<?> value, boolean match) {
        return this.andIn(getFieldName(methodRef), value, match);
    }

    /**
     * 生成 {@code IN} 范围匹配的 {@link Predicate} 条件.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 数组
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder andIn(String fieldName, Object[] value) {
        this.predicates.add(new InPredicateHandler().buildPredicate(criteriaBuilder, from, fieldName, value));
        return this;
    }

    public <T, K> FenixPredicateBuilder andIn(SFunction<? super T, ? extends K> methodRef, Object[] value) {
        return this.andIn(getFieldName(methodRef), value);
    }

    /**
     * 生成 {@code IN} 范围匹配的 {@link Predicate} 条件，如果 {@code match} 值为 {@code true} 时则生成该条件，否则不生成.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 数组
     * @param match 是否匹配生成此 {@link Predicate} 条件
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder andIn(String fieldName, Object[] value, boolean match) {
        return match ? this.andIn(fieldName, value) : this;
    }

    public <T, K> FenixPredicateBuilder andIn(SFunction<? super T, ? extends K> methodRef, Object[] value, boolean match) {
        return this.andIn(getFieldName(methodRef), value, match);
    }

    /**
     * 生成或语句 {@code IN} 范围匹配的 {@link Predicate} 条件.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 集合
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder orIn(String fieldName, Collection<?> value) {
        this.predicates.add(new OrInPredicateHandler().buildPredicate(criteriaBuilder, from, fieldName, value));
        return this;
    }

    public <T, K> FenixPredicateBuilder orIn(SFunction<? super T, ? extends K> methodRef, Collection<?> value) {
        return this.orIn(getFieldName(methodRef), value);
    }

    /**
     * 生成或语句 {@code IN} 范围匹配的 {@link Predicate} 条件，如果 {@code match} 值为 {@code true} 时则生成该条件，否则不生成.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 集合
     * @param match 是否匹配生成此 {@link Predicate} 条件
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder orIn(String fieldName, Collection<?> value, boolean match) {
        return match ? this.orIn(fieldName, value) : this;
    }

    public <T, K> FenixPredicateBuilder orIn(SFunction<? super T, ? extends K> methodRef, Collection<?> value, boolean match) {
        return this.orIn(getFieldName(methodRef), value, match);
    }

    /**
     * 生成或语句 {@code IN} 范围匹配的 {@link Predicate} 条件.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 数组
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder orIn(String fieldName, Object[] value) {
        this.predicates.add(new OrInPredicateHandler().buildPredicate(criteriaBuilder, from, fieldName, value));
        return this;
    }

    public <T, K> FenixPredicateBuilder orIn(SFunction<? super T, ? extends K> methodRef, Object[] value) {
        return this.orIn(getFieldName(methodRef), value);
    }

    /**
     * 生成或语句 {@code IN} 范围匹配的 {@link Predicate} 条件，如果 {@code match} 值为 {@code true} 时则生成该条件，否则不生成.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 数组
     * @param match 是否匹配生成此 {@link Predicate} 条件
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder orIn(String fieldName, Object[] value, boolean match) {
        return match ? this.orIn(fieldName, value) : this;
    }

    public <T, K> FenixPredicateBuilder orIn(SFunction<? super T, ? extends K> methodRef, Object[] value, boolean match) {
        return this.orIn(getFieldName(methodRef), value, match);
    }

    /**
     * 生成 {@code AND NOT IN} 范围不匹配的 {@link Predicate} 条件.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 集合
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder andNotIn(String fieldName, Collection<?> value) {
        this.predicates.add(new NotInPredicateHandler().buildPredicate(criteriaBuilder, from, fieldName, value));
        return this;
    }

    public <T, K> FenixPredicateBuilder andNotIn(SFunction<? super T, ? extends K> methodRef, Collection<?> value) {
        return this.andNotIn(getFieldName(methodRef), value);
    }

    /**
     * 生成 {@code AND NOT IN} 范围不匹配的 {@link Predicate} 条件，如果 {@code match} 值为 {@code true} 时则生成该条件，否则不生成.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 集合
     * @param match 是否匹配生成此 {@link Predicate} 条件
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder andNotIn(String fieldName, Collection<?> value, boolean match) {
        return match ? this.andNotIn(fieldName, value) : this;
    }

    public <T, K> FenixPredicateBuilder andNotIn(SFunction<? super T, ? extends K> methodRef, Collection<?> value, boolean match) {
        return this.andNotIn(getFieldName(methodRef), value, match);
    }

    /**
     * 生成 {@code AND NOT IN} 范围不匹配的 {@link Predicate} 条件.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 数组
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder andNotIn(String fieldName, Object[] value) {
        this.predicates.add(new NotInPredicateHandler().buildPredicate(criteriaBuilder, from, fieldName, value));
        return this;
    }

    public <T, K> FenixPredicateBuilder andNotIn(SFunction<? super T, ? extends K> methodRef, Object[] value) {
        return this.andNotIn(getFieldName(methodRef), value);
    }

    /**
     * 生成 {@code AND NOT IN} 范围不匹配的 {@link Predicate} 条件，如果 {@code match} 值为 {@code true} 时则生成该条件，否则不生成.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 数组
     * @param match 是否匹配生成此 {@link Predicate} 条件
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder andNotIn(String fieldName, Object[] value, boolean match) {
        return match ? this.andNotIn(fieldName, value) : this;
    }

    public <T, K> FenixPredicateBuilder andNotIn(SFunction<? super T, ? extends K> methodRef, Object[] value, boolean match) {
        return this.andNotIn(getFieldName(methodRef), value, match);
    }

    /**
     * 生成或语句 {@code OR NOT IN} 范围不匹配的 {@link Predicate} 条件.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 集合
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder orNotIn(String fieldName, Collection<?> value) {
        this.predicates.add(new OrNotInPredicateHandler().buildPredicate(criteriaBuilder, from, fieldName, value));
        return this;
    }

    public <T, K> FenixPredicateBuilder orNotIn(SFunction<? super T, ? extends K> methodRef, Collection<?> value) {
        return this.orNotIn(getFieldName(methodRef), value);
    }

    /**
     * 生成或语句 {@code OR NOT IN} 范围不匹配的 {@link Predicate} 条件，如果 {@code match} 值为 {@code true} 时则生成该条件，否则不生成.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 集合
     * @param match 是否匹配生成此 {@link Predicate} 条件
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder orNotIn(String fieldName, Collection<?> value, boolean match) {
        return match ? this.orNotIn(fieldName, value) : this;
    }

    public <T, K> FenixPredicateBuilder orNotIn(SFunction<? super T, ? extends K> methodRef, Collection<?> value, boolean match) {
        return this.orNotIn(getFieldName(methodRef), value, match);
    }

    /**
     * 生成或语句 {@code OR NOT IN} 范围不匹配的 {@link Predicate} 条件.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 数组
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder orNotIn(String fieldName, Object[] value) {
        this.predicates.add(new OrNotInPredicateHandler().buildPredicate(criteriaBuilder, from, fieldName, value));
        return this;
    }

    public <T, K> FenixPredicateBuilder orNotIn(SFunction<? super T, ? extends K> methodRef, Object[] value) {
        return this.orNotIn(getFieldName(methodRef), value);
    }

    /**
     * 生成或语句 {@code OR NOT IN} 范围不匹配的 {@link Predicate} 条件，如果 {@code match} 值为 {@code true} 时则生成该条件，否则不生成.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 数组
     * @param match 是否匹配生成此 {@link Predicate} 条件
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder orNotIn(String fieldName, Object[] value, boolean match) {
        return match ? this.orNotIn(fieldName, value) : this;
    }

    public <T, K> FenixPredicateBuilder orNotIn(SFunction<? super T, ? extends K> methodRef, Object[] value, boolean match) {
        return this.orNotIn(getFieldName(methodRef), value, match);
    }

    /**
     * 生成 {@code IS NULL} 是空的 {@link Predicate} 条件.
     *
     * @param fieldName 实体属性或数据库字段
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder andIsNull(String fieldName) {
        this.predicates.add(new IsNullPredicateHandler().buildPredicate(criteriaBuilder, from, fieldName, fieldName));
        return this;
    }

    public <T, K> FenixPredicateBuilder andIsNull(SFunction<? super T, ? extends K> methodRef) {
        return this.andIsNull(getFieldName(methodRef));
    }

    /**
     * 生成 {@code IS NULL} 是空的 {@link Predicate} 条件，如果 {@code match} 值为 {@code true} 时则生成该条件，否则不生成.
     *
     * @param fieldName 实体属性或数据库字段
     * @param match 是否匹配生成此 {@link Predicate} 条件
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder andIsNull(String fieldName, boolean match) {
        return match ? this.andIsNull(fieldName) : this;
    }

    public <T, K> FenixPredicateBuilder andIsNull(SFunction<? super T, ? extends K> methodRef, boolean match) {
        return this.andIsNull(getFieldName(methodRef), match);
    }

    /**
     * 生成或语句 {@code OR field IS NULL} 是空的 {@link Predicate} 条件.
     *
     * @param fieldName 实体属性或数据库字段
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder orIsNull(String fieldName) {
        this.predicates.add(new OrIsNullPredicateHandler()
                .buildPredicate(criteriaBuilder, from, fieldName, fieldName));
        return this;
    }

    public <T, K> FenixPredicateBuilder orIsNull(SFunction<? super T, ? extends K> methodRef) {
        return this.orIsNull(getFieldName(methodRef));
    }

    /**
     * 生成或语句 {@code OR field IS NULL} 是空的 {@link Predicate} 条件，如果 {@code match} 值为 {@code true} 时则生成该条件，否则不生成.
     *
     * @param fieldName 实体属性或数据库字段
     * @param match 是否匹配生成此 {@link Predicate} 条件
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder orIsNull(String fieldName, boolean match) {
        return match ? this.orIsNull(fieldName) : this;
    }

    public <T, K> FenixPredicateBuilder orIsNull(SFunction<? super T, ? extends K> methodRef, boolean match) {
        return this.orIsNull(getFieldName(methodRef), match);
    }

    /**
     * 生成 {@code AND field IS NOT NULL} 不是空的 {@link Predicate} 条件.
     *
     * @param fieldName 实体属性或数据库字段
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder andIsNotNull(String fieldName) {
        this.predicates.add(new IsNotNullPredicateHandler()
                .buildPredicate(criteriaBuilder, from, fieldName, fieldName));
        return this;
    }

    public <T, K> FenixPredicateBuilder andIsNotNull(SFunction<? super T, ? extends K> methodRef) {
        return this.andIsNotNull(getFieldName(methodRef));
    }

    /**
     * 生成 {@code AND field IS NOT NULL} 不是空的 {@link Predicate} 条件，如果 {@code match} 值为 {@code true} 时则生成该条件，否则不生成.
     *
     * @param fieldName 实体属性或数据库字段
     * @param match 是否匹配生成此 {@link Predicate} 条件
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder andIsNotNull(String fieldName, boolean match) {
        return match ? this.andIsNotNull(fieldName) : this;
    }

    public <T, K> FenixPredicateBuilder andIsNotNull(SFunction<? super T, ? extends K> methodRef, boolean match) {
        return this.andIsNotNull(getFieldName(methodRef), match);
    }

    /**
     * 生成或语句 {@code OR field IS NOT NULL} 不是空的 {@link Predicate} 条件.
     *
     * @param fieldName 实体属性或数据库字段
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder orIsNotNull(String fieldName) {
        this.predicates.add(new OrIsNotNullPredicateHandler()
                .buildPredicate(criteriaBuilder, from, fieldName, fieldName));
        return this;
    }

    public <T, K> FenixPredicateBuilder orIsNotNull(SFunction<? super T, ? extends K> methodRef) {
        return this.orIsNotNull(getFieldName(methodRef));
    }

    /**
     * 生成或语句 {@code OR field IS NOT NULL} 不是空的 {@link Predicate} 条件，如果 {@code match} 值为 {@code true} 时则生成该条件，否则不生成.
     *
     * @param fieldName 实体属性或数据库字段
     * @param match 是否匹配生成此 {@link Predicate} 条件
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder orIsNotNull(String fieldName, boolean match) {
        return match ? this.orIsNotNull(fieldName) : this;
    }

    public <T, K> FenixPredicateBuilder orIsNotNull(SFunction<? super T, ? extends K> methodRef, boolean match) {
        return this.orIsNotNull(getFieldName(methodRef), match);
    }

    /**
     * 根据字段、值和 {@link AbstractPredicateHandler} 的子类实例来自定义构造 {@link Predicate} 条件.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 字段条件对应的值
     * @param handler {@link AbstractPredicateHandler} 的子类实例
     * @return 当前的 {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder doAny(String fieldName, Object value, AbstractPredicateHandler handler) {
        this.predicates.add(handler.buildPredicate(criteriaBuilder, from, fieldName, value));
        return this;
    }

    public <T, K> FenixPredicateBuilder doAny(SFunction<? super T, ? extends K> methodRef, Object value, AbstractPredicateHandler handler) {
        return this.doAny(getFieldName(methodRef), value, handler);
    }

    /**
     * 根据字段、值和 {@link AbstractPredicateHandler} 的子类实例来自定义构造 {@link Predicate} 条件.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 字段条件对应的值
     * @param handler {@link AbstractPredicateHandler} 的子类实例
     * @param match 是否匹配生成此 {@link Predicate} 条件
     * @return 当前的 {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder doAny(
            String fieldName, Object value, AbstractPredicateHandler handler, boolean match) {
        return match ? this.doAny(fieldName, value, handler) : this;
    }

    public <T, K> FenixPredicateBuilder doAny(
            SFunction<? super T, ? extends K> methodRef, Object value, AbstractPredicateHandler handler, boolean match) {
        return this.doAny(getFieldName(methodRef), value, handler, match);
    }

    /**
     * 根据字段、值和 {@link PredicateHandler} 的实现类实例来自定义构造 {@link Predicate} 条件，可使用 {@code Lambda} 表达式.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 字段条件对应的值
     * @param handler {@link PredicateHandler} 的实现类实例，可使用 {@code Lambda} 表达式
     * @return 当前的 {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder doAny(String fieldName, Object value, PredicateHandler handler) {
        this.predicates.add(handler.buildPredicate(criteriaBuilder, from, fieldName, value));
        return this;
    }

    public <T, K> FenixPredicateBuilder doAny(SFunction<? super T, ? extends K> methodRef, Object value, PredicateHandler handler) {
        return this.doAny(getFieldName(methodRef), value, handler);
    }

    /**
     * 根据字段、值和 {@link PredicateHandler} 的实现类实例来自定义构造 {@link Predicate} 条件，可使用 {@code Lambda} 表达式.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 字段条件对应的值
     * @param handler {@link PredicateHandler} 的实现类实例，可使用 {@code Lambda} 表达式
     * @param match 是否匹配生成此 {@link Predicate} 条件
     * @return 当前的 {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder doAny(String fieldName, Object value, PredicateHandler handler, boolean match) {
        return match ? this.doAny(fieldName, value, handler) : this;
    }

    public <T, K> FenixPredicateBuilder doAny(SFunction<? super T, ? extends K> methodRef, Object value, PredicateHandler handler, boolean match) {
        return this.doAny(getFieldName(methodRef), value, handler, match);
    }

    private static String getFieldName(SFunction lambda){
        return LambdaHelper.getProperty(lambda);
    }

}
