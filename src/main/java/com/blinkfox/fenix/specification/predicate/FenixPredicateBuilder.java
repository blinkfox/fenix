package com.blinkfox.fenix.specification.predicate;

import com.blinkfox.fenix.specification.handler.impl.EqualsPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.GreaterThanEqualPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.GreaterThanPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.InPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.LessThanEqualPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.LessThanPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.LikePredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.NotEqualsPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.NotInPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.NotLikePredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.OrEqualsPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.OrGreaterThanEqualPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.OrGreaterThanPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.OrInPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.OrLessThanEqualPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.OrLessThanPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.OrLikePredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.OrNotEqualsPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.OrNotLikePredicateHandler;

import java.util.ArrayList;
import java.util.Collection;
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

    /**
     * 生成 {@code IN} 范围匹配的 {@link Predicate} 条件.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 集合
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder andNotIn(String fieldName, Collection<?> value) {
        this.predicates.add(new NotInPredicateHandler().buildPredicate(criteriaBuilder, from, fieldName, value));
        return this;
    }

    /**
     * 生成 {@code IN} 范围匹配的 {@link Predicate} 条件，如果 {@code match} 值为 {@code true} 时则生成该条件，否则不生成.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 集合
     * @param match 是否匹配生成此 {@link Predicate} 条件
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder andNotIn(String fieldName, Collection<?> value, boolean match) {
        return match ? this.andNotIn(fieldName, value) : this;
    }

    /**
     * 生成 {@code IN} 范围匹配的 {@link Predicate} 条件.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 数组
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder andNotIn(String fieldName, Object[] value) {
        this.predicates.add(new NotInPredicateHandler().buildPredicate(criteriaBuilder, from, fieldName, value));
        return this;
    }

    /**
     * 生成 {@code IN} 范围匹配的 {@link Predicate} 条件，如果 {@code match} 值为 {@code true} 时则生成该条件，否则不生成.
     *
     * @param fieldName 实体属性或数据库字段
     * @param value 数组
     * @param match 是否匹配生成此 {@link Predicate} 条件
     * @return {@link FenixPredicateBuilder} 实例
     */
    public FenixPredicateBuilder andNotIn(String fieldName, Object[] value, boolean match) {
        return match ? this.andNotIn(fieldName, value) : this;
    }

}
