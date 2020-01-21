package com.blinkfox.fenix.specification.handler;

import com.blinkfox.fenix.exception.BuildSpecificationException;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

/**
 * 用来动态构造 JPA 中 {@code Specification} 的抽象类.
 *
 * @author YangWenpeng on 2019-12-17
 * @author blinkfox on 2020-01-14
 * @since v2.2.0
 */
public abstract class AbstractPredicateHandler {

    /**
     * 获取对应注解类的 {@code Class} 类型的值.
     *
     * @param <T> 范型 T
     * @return 范型 T 的 {@code Class} 类型的值
     */
    protected abstract <T> Class<T> getAndAnnotation();

    /**
     * 获取对应注解类的 {@code Class} 类型的值.
     *
     * @param <T> 范型 T
     * @return 范型 T 的 {@code Class} 类型的值
     */
    protected abstract <T> Class<T> getAndNotAnnotation();

    /**
     * 获取对应注解类的 {@code Class} 类型的值.
     *
     * @param <T> 范型 T
     * @return 范型 T 的 {@code Class} 类型的值
     */
    protected abstract <T> Class<T> getOrAnnotation();

    /**
     * 获取对应注解类的 {@code Class} 类型的值.
     *
     * @param <T> 范型 T
     * @return 范型 T 的 {@code Class} 类型的值
     */
    protected abstract <T> Class<T> getOrNotAnnotation();

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
    protected abstract <Z, X> Predicate buildAndPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value, Object annotation);

    /**
     * 构造 {@code AND} 关系的 {@link Predicate} 实例的方法.
     *
     * @param criteriaBuilder {@link CriteriaBuilder} 实例
     * @param from {@link From} 实例
     * @param fieldName 属性字段名称
     * @param value 属性条件对应的值
     * @param <Z> 范型 Z
     * @param <X> 范型 X
     * @return {@link Predicate} 实例
     */
    protected abstract <Z, X> Predicate buildAndPredicate(CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value);

    /**
     * 构造 {@code AND NOT} 关系的 {@link Predicate} 实例的方法.
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
    protected abstract <Z, X> Predicate buildAndNotPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value, Object annotation);

    /**
     * 构造 {@code AND NOT} 关系的 {@link Predicate} 实例的方法.
     *
     * @param criteriaBuilder {@link CriteriaBuilder} 实例
     * @param from {@link From} 实例
     * @param fieldName 属性字段名称
     * @param value 属性条件对应的值
     * @param <Z> 范型 Z
     * @param <X> 范型 X
     * @return {@link Predicate} 实例
     */
    protected abstract <Z, X> Predicate buildAndNotPredicate(CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value);

    /**
     * 构造 {@code OR} 关系的 {@link Predicate} 实例的方法.
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
    protected abstract <Z, X> Predicate buildOrPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value, Object annotation);

    /**
     * 构造 {@code OR} 关系的 {@link Predicate} 实例的方法.
     *
     * @param criteriaBuilder {@link CriteriaBuilder} 实例
     * @param from {@link From} 实例
     * @param fieldName 属性字段名称
     * @param value 属性条件对应的值
     * @param <Z> 范型 Z
     * @param <X> 范型 X
     * @return {@link Predicate} 实例
     */
    protected abstract <Z, X> Predicate buildOrPredicate(CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value);

    /**
     * 构造 {@code OR NOT} 关系的 {@link Predicate} 实例的方法.
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
    protected abstract <Z, X> Predicate buildOrNotPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value, Object annotation);

    /**
     * 构造 {@code OR NOT} 关系的 {@link Predicate} 实例的方法.
     *
     * @param criteriaBuilder {@link CriteriaBuilder} 实例
     * @param from {@link From} 实例
     * @param fieldName 属性字段名称
     * @param value 属性条件对应的值
     * @param <Z> 范型 Z
     * @param <X> 范型 X
     * @return {@link Predicate} 实例
     */
    protected abstract <Z, X> Predicate buildOrNotPredicate(CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value);

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

}
