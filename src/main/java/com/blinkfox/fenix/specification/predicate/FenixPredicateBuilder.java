package com.blinkfox.fenix.specification.predicate;

import com.blinkfox.fenix.specification.handler.AbstractPredicateHandler;
import com.blinkfox.fenix.specification.handler.PredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.BetweenPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.EndsWithPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.EqualsPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.GreaterThanEqualPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.GreaterThanPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.InPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.IsNotNullPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.IsNullPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.LessThanEqualPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.LessThanPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.LikePatternPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.LikePredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.NotBetweenPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.NotEndsWithPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.NotEqualsPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.NotInPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.NotLikePatternPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.NotLikePredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.NotStartsWithPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.OrBetweenPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.OrEndsWithPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.OrEqualsPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.OrGreaterThanEqualPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.OrGreaterThanPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.OrInPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.OrIsNotNullPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.OrIsNullPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.OrLessThanEqualPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.OrLessThanPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.OrLikePatternPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.OrLikePredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.OrNotBetweenPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.OrNotEndsWithPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.OrNotEqualsPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.OrNotInPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.OrNotLikePatternPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.OrNotLikePredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.OrNotStartsWithPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.OrStartsWithPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.StartsWithPredicateHandler;
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

}
