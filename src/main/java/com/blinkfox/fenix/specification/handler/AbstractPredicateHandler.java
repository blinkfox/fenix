package com.blinkfox.fenix.specification.handler;

import com.blinkfox.fenix.exception.BuildSpecificationException;
import com.blinkfox.fenix.specification.handler.bean.BetweenValue;
import com.blinkfox.fenix.specification.predicate.FenixBooleanStaticPredicate;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Predicate.BooleanOperator;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.query.criteria.internal.CriteriaBuilderImpl;

/**
 * 用来动态构造 JPA 中 {@link Predicate} 的抽象类.
 *
 * @author YangWenpeng on 2019-12-17
 * @author blinkfox on 2020-01-14
 * @since v2.2.0
 */
@Slf4j
public abstract class AbstractPredicateHandler implements PredicateHandler {

    /**
     * 获取对应注解类的 {@code Class} 类型的值.
     *
     * @return {@code Class} 类型的值
     */
    public abstract Class<? extends Annotation> getAnnotation();

    /**
     * 构造 {@code AND} 关系的 {@link Predicate} 实例的方法.
     *
     * @param criteriaBuilder {@link CriteriaBuilder} 实例
     * @param from {@link From} 实例
     * @param fieldName 实体类的属性名
     * @param value 对应属性的值
     * @param annotation 前字段使用的注解
     * @param <Z> 范型 Z
     * @param <X> 范型 X
     * @return {@link Predicate} 实例
     */
    public abstract <Z, X> Predicate buildPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value, Annotation annotation);

    /**
     * 构造 {@code AND} 关系的 {@link Predicate} 实例的方法.
     *
     * @param criteriaBuilder {@link CriteriaBuilder} 实例
     * @param from {@link From} 实例
     * @param fieldName 属性字段名称
     * @param value 属性条件对应的值
     * @return {@link Predicate} 实例
     */
    @Override
    public Predicate buildPredicate(
            CriteriaBuilder criteriaBuilder, From<?, ?> from, String fieldName, Object value) {
        return this.buildPredicate(criteriaBuilder, from, fieldName, value, null);
    }

    /**
     * 构造相等条件 {@code Equals} 的 {@link Predicate} 条件.
     *
     * @param criteriaBuilder {@link CriteriaBuilder} 实例
     * @param from {@link From} 实例
     * @param fieldName 实体类的属性名
     * @param value 对应属性的值
     * @param <Z> 泛型 Z
     * @param <X> 泛型 X
     * @return {@link Predicate} 实例
     */
    protected <Z, X> Predicate buildEqualsPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value) {
        return criteriaBuilder.equal(from.get(fieldName), value);
    }

    /**
     * 构造不相等条件 {@code Not Equals} 的 {@link Predicate} 条件.
     *
     * @param criteriaBuilder {@link CriteriaBuilder} 实例
     * @param from {@link From} 实例
     * @param fieldName 实体类的属性名
     * @param value 对应属性的值
     * @param <Z> 泛型 Z
     * @param <X> 泛型 X
     * @return {@link Predicate} 实例
     */
    protected <Z, X> Predicate buildNotEqualsPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value) {
        return criteriaBuilder.notEqual(from.get(fieldName), value);
    }

    /**
     * 构造大于查询的 {@link Predicate} 实例的方法.
     *
     * @param criteriaBuilder {@link CriteriaBuilder} 实例
     * @param from {@link From} 实例
     * @param fieldName 属性字段名称
     * @param value 属性条件对应的值
     * @param <Z> 范型 Z
     * @param <X> 范型 X
     * @return {@link Predicate} 实例
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected <Z, X> Predicate buildGreaterThanPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value) {
        this.isValueComparable(value);
        return criteriaBuilder.greaterThan(from.get(fieldName), (Comparable) value);
    }

    /**
     * 构造大于等于查询的 {@link Predicate} 实例的方法.
     *
     * @param criteriaBuilder {@link CriteriaBuilder} 实例
     * @param from {@link From} 实例
     * @param fieldName 属性字段名称
     * @param value 属性条件对应的值
     * @param <Z> 范型 Z
     * @param <X> 范型 X
     * @return {@link Predicate} 实例
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected <Z, X> Predicate buildGreaterThanEqualPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value) {
        this.isValueComparable(value);
        return criteriaBuilder.greaterThanOrEqualTo(from.get(fieldName), (Comparable) value);
    }

    /**
     * 构造小于查询的 {@link Predicate} 实例的方法.
     *
     * @param criteriaBuilder {@link CriteriaBuilder} 实例
     * @param from {@link From} 实例
     * @param fieldName 属性字段名称
     * @param value 属性条件对应的值
     * @param <Z> 范型 Z
     * @param <X> 范型 X
     * @return {@link Predicate} 实例
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected <Z, X> Predicate buildLessThanPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value) {
        this.isValueComparable(value);
        return criteriaBuilder.lessThan(from.get(fieldName), (Comparable) value);
    }

    /**
     * 构造小于等于查询的 {@link Predicate} 实例的方法.
     *
     * @param criteriaBuilder {@link CriteriaBuilder} 实例
     * @param from {@link From} 实例
     * @param fieldName 属性字段名称
     * @param value 属性条件对应的值
     * @param <Z> 范型 Z
     * @param <X> 范型 X
     * @return {@link Predicate} 实例
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected <Z, X> Predicate buildLessThanEqualPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value) {
        this.isValueComparable(value);
        return criteriaBuilder.lessThanOrEqualTo(from.get(fieldName), (Comparable) value);
    }

    /**
     * 检查值是否实现了 {@link Comparable} 接口.
     *
     * @param value 值
     */
    private void isValueComparable(Object value) {
        if (!(value instanceof Comparable)) {
            throw new BuildSpecificationException("【Fenix 异常】要比较的 value 值【" + value + "】不是可比较类型的，"
                    + "该值的类型必须实现了 java.lang.Comparable 接口才能正常参与比较，才能用于大于、大于等于、小于、小于等于之类的比较场景.");
        }
    }

    /**
     * 构造空条件 {@code IS NULL} 的 {@link Predicate} 条件.
     *
     * @param criteriaBuilder {@link CriteriaBuilder} 实例
     * @param from {@link From} 实例
     * @param value 对应属性的值
     * @param <Z> 泛型 Z
     * @param <X> 泛型 X
     * @return {@link Predicate} 实例
     */
    protected <Z, X> Predicate buildIsNullPredicate(CriteriaBuilder criteriaBuilder, From<Z, X> from, Object value) {
        return criteriaBuilder.isNull(from.get(String.valueOf(value)));
    }

    /**
     * 构造不是空条件 {@code IS NOT NULL} 的 {@link Predicate} 条件.
     *
     * @param criteriaBuilder {@link CriteriaBuilder} 实例
     * @param from {@link From} 实例
     * @param value 对应属性的值
     * @param <Z> 泛型 Z
     * @param <X> 泛型 X
     * @return {@link Predicate} 实例
     */
    protected <Z, X> Predicate buildIsNotNullPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, Object value) {
        return criteriaBuilder.isNotNull(from.get(String.valueOf(value)));
    }

    /**
     * 构造相等条件 {@code Equals} 的 {@link Predicate} 条件.
     *
     * @param criteriaBuilder {@link CriteriaBuilder} 实例
     * @param from {@link From} 实例
     * @param fieldName 实体类的属性名
     * @param value 对应属性的值
     * @param allowNull 是否允许 null 值
     * @param <Z> 泛型 Z
     * @param <X> 泛型 X
     * @return {@link Predicate} 实例
     */
    protected <Z, X> Predicate buildInPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value, boolean allowNull) {
        value = value.getClass().isArray() ? Arrays.asList((Object[]) value) : value;
        Path<Object> path = from.get(fieldName);
        CriteriaBuilder.In<Object> in = criteriaBuilder.in(path);

        if (value instanceof Collection) {
            Collection<?> list = (Collection<?>) value;
            if (list.isEmpty()) {
                return new FenixBooleanStaticPredicate(
                        (CriteriaBuilderImpl) criteriaBuilder, true, BooleanOperator.AND);
            } else {
                list.forEach(in::value);
            }
        } else {
            in.value(value);
        }

        return allowNull ? criteriaBuilder.or(in, criteriaBuilder.isNull(path)) : in;
    }

    protected boolean isAllowNull(Object annotation) {
        try {
            return (boolean) this.getAnnotation().getMethod("allowNull").invoke(annotation);
        } catch (IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            log.error("【Fenix 错误提示】获取【@In】、【@OrIn】、【@NotIn】、【@OrNotIn】相关注解中的【allowNull】的值失败，将默认返回 false 的值.", e);
            return false;
        }
    }

    /**
     * 构造模糊匹配条件 {@code LIKE} 的 {@link Predicate} 条件.
     *
     * @param criteriaBuilder {@link CriteriaBuilder} 实例
     * @param from {@link From} 实例
     * @param fieldName 实体类的属性名
     * @param value 对应属性的值
     * @param <Z> 泛型 Z
     * @param <X> 泛型 X
     * @return {@link Predicate} 实例
     */
    protected <Z, X> Predicate buildLikePredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value) {
        return criteriaBuilder.like(from.get(fieldName), "%" + this.convertValue(value) + "%");
    }

    /**
     * 构造模糊不匹配条件 {@code LIKE} 的 {@link Predicate} 条件.
     *
     * @param criteriaBuilder {@link CriteriaBuilder} 实例
     * @param from {@link From} 实例
     * @param fieldName 实体类的属性名
     * @param value 对应属性的值
     * @param <Z> 泛型 Z
     * @param <X> 泛型 X
     * @return {@link Predicate} 实例
     */
    protected <Z, X> Predicate buildNotLikePredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value) {
        return criteriaBuilder.notLike(from.get(fieldName), "%" + this.convertValue(value) + "%");
    }

    /**
     * 构造按前缀模糊匹配（{@code LIKE}）的 {@link Predicate} 条件.
     *
     * @param criteriaBuilder {@link CriteriaBuilder} 实例
     * @param from {@link From} 实例
     * @param fieldName 实体类的属性名
     * @param value 对应属性的值
     * @param <Z> 泛型 Z
     * @param <X> 泛型 X
     * @return {@link Predicate} 实例
     */
    protected <Z, X> Predicate buildStartsWithPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value) {
        return criteriaBuilder.like(from.get(fieldName), this.convertValue(value) + "%");
    }

    /**
     * 构造按前缀模糊不匹配（{@code LIKE}）的 {@link Predicate} 条件.
     *
     * @param criteriaBuilder {@link CriteriaBuilder} 实例
     * @param from {@link From} 实例
     * @param fieldName 实体类的属性名
     * @param value 对应属性的值
     * @param <Z> 泛型 Z
     * @param <X> 泛型 X
     * @return {@link Predicate} 实例
     */
    protected <Z, X> Predicate buildNotStartsWithPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value) {
        return criteriaBuilder.notLike(from.get(fieldName), this.convertValue(value) + "%");
    }

    /**
     * 构造按后缀模糊匹配（{@code LIKE}）的 {@link Predicate} 条件.
     *
     * @param criteriaBuilder {@link CriteriaBuilder} 实例
     * @param from {@link From} 实例
     * @param fieldName 实体类的属性名
     * @param value 对应属性的值
     * @param <Z> 泛型 Z
     * @param <X> 泛型 X
     * @return {@link Predicate} 实例
     */
    protected <Z, X> Predicate buildEndsWithPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value) {
        return criteriaBuilder.like(from.get(fieldName), "%" + this.convertValue(value));
    }

    /**
     * 构造按后缀模糊匹配（{@code LIKE}）的 {@link Predicate} 条件.
     *
     * @param criteriaBuilder {@link CriteriaBuilder} 实例
     * @param from {@link From} 实例
     * @param fieldName 实体类的属性名
     * @param value 对应属性的值
     * @param <Z> 泛型 Z
     * @param <X> 泛型 X
     * @return {@link Predicate} 实例
     */
    protected <Z, X> Predicate buildNotEndsWithPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value) {
        return criteriaBuilder.notLike(from.get(fieldName), "%" + this.convertValue(value));
    }

    /**
     * 构造按指定的模式做模糊匹配 {@code LIKE} 的 {@link Predicate} 条件.
     *
     * @param criteriaBuilder {@link CriteriaBuilder} 实例
     * @param from {@link From} 实例
     * @param fieldName 实体类的属性名
     * @param value 对应属性的值
     * @param <Z> 泛型 Z
     * @param <X> 泛型 X
     * @return {@link Predicate} 实例
     */
    protected <Z, X> Predicate buildLikePatternPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value) {
        return criteriaBuilder.like(from.get(fieldName), value.toString());
    }

    /**
     * 构造按指定的模式做模糊匹配 {@code LIKE} 的 {@link Predicate} 条件.
     *
     * @param criteriaBuilder {@link CriteriaBuilder} 实例
     * @param from {@link From} 实例
     * @param fieldName 实体类的属性名
     * @param value 对应属性的值
     * @param <Z> 泛型 Z
     * @param <X> 泛型 X
     * @return {@link Predicate} 实例
     */
    protected <Z, X> Predicate buildNotLikePatternPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value) {
        return criteriaBuilder.notLike(from.get(fieldName), value.toString());
    }

    /**
     * 构造多模糊条件 {@code LIKE OR LIKE} 的 {@link Predicate} 条件.
     *
     * @param criteriaBuilder {@link CriteriaBuilder} 实例
     * @param from {@link From} 实例
     * @param fields 实体类的属性名
     * @param values 对应属性的值
     * @param <Z> 泛型 Z
     * @param <X> 泛型 X
     * @return {@link Predicate} 实例
     */
    protected  <Z, X> List<Predicate> buildLikeOrLikePredicates(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String[] fields, List<?> values) {
        int len = fields.length;
        List<Predicate> predicates = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            predicates.add(criteriaBuilder.like(from.get(fields[i]), "%" + this.convertValue(values.get(i)) + "%"));
        }
        return predicates;
    }

    private String convertValue(Object value) {
        return value.toString().replace("%", "\\%");
    }

    /**
     * 构造区间查询的 {@link Predicate} 实例的方法.
     * 若结束值为空，则退化生成为大于等于的条件，若开始值为空.则退化生成为小于等于的条件，若开始值或结束值都为空，则直接抛出异常.
     *
     * @param criteriaBuilder {@link CriteriaBuilder} 实例
     * @param from {@link From} 实例
     * @param fieldName 属性字段名称
     * @param value 值，只能是数组或者 {@link List} 集合类型.
     * @param <Z> 范型 Z
     * @param <X> 范型 X
     * @return {@link Predicate} 实例
     */
    protected <Z, X> Predicate buildBetweenPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value) {
        if (value.getClass().isArray()) {
            Object[] arr = (Object[]) value;
            return this.buildBetweenPredicate(criteriaBuilder, from, fieldName, arr[0], arr[1]);
        } else if (value instanceof List) {
            List<?> list = (List<?>) value;
            return this.buildBetweenPredicate(criteriaBuilder, from, fieldName, list.get(0), list.get(1));
        } else if (value instanceof BetweenValue) {
            BetweenValue<?> bv = (BetweenValue<?>) value;
            return this.buildBetweenPredicate(criteriaBuilder, from, fieldName, bv.getStart(), bv.getEnd());
        } else {
            throw new BuildSpecificationException("【Fenix 异常】构建【@Between】注解区间查询时，参数值类型不是数组或 "
                    + "List 类型的集合，无法获取到前后的区间值。");
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private <Z, X> Predicate buildBetweenPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object startValue, Object endValue) {
        if (startValue != null && endValue != null) {
            this.isValueComparable(startValue);
            this.isValueComparable(endValue);
            return criteriaBuilder.between(from.get(fieldName), (Comparable) startValue, (Comparable) endValue);
        } else if (startValue != null) {
            this.isValueComparable(startValue);
            return criteriaBuilder.greaterThanOrEqualTo(from.get(fieldName), (Comparable) startValue);
        } else if (endValue != null) {
            this.isValueComparable(endValue);
            return criteriaBuilder.lessThanOrEqualTo(from.get(fieldName), (Comparable) endValue);
        } else {
            throw new BuildSpecificationException("【Fenix 异常】构建【@Between】注解区间查询时，开始和结束的区间值均为【null】"
                    + "，无法构造区间或大于等于、小于等于的 Predicate条件。");
        }
    }

}
