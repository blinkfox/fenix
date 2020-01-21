package com.blinkfox.fenix.specification.handler.impl;

import com.blinkfox.fenix.specification.annotation.Equals;
import com.blinkfox.fenix.specification.annotation.NotEquals;
import com.blinkfox.fenix.specification.annotation.OrEquals;
import com.blinkfox.fenix.specification.annotation.OrNotEquals;
import com.blinkfox.fenix.specification.handler.AbstractPredicateHandler;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

/**
 * 构建“等值条件”({@code field = 'xxx'})场景的 Specification 监听器.
 *
 * @author YangWenpeng on 2019-12-17
 * @author blinkfox on 2020-01-14
 * @since v2.2.0
 */
public class EqualsSpecificationHandler extends AbstractPredicateHandler {

    /**
     * 获取 {@link Equals} 注解的 {@code Class} 类型的值.
     *
     * @return {@link Equals} 注解的 {@code Class} 类型的值
     */
    @SuppressWarnings("unchecked")
    @Override
    public Class<Equals> getAndAnnotation() {
        return Equals.class;
    }

    /**
     * 获取 {@link NotEquals} 注解的 {@code Class} 类型的值.
     *
     * @return {@link NotEquals} 注解的 {@code Class} 类型的值
     */
    @SuppressWarnings("unchecked")
    @Override
    public Class<NotEquals> getAndNotAnnotation() {
        return NotEquals.class;
    }

    /**
     * 获取 {@link OrEquals} 注解的 {@code Class} 类型的值.
     *
     * @return {@link OrEquals} 注解的 {@code Class} 类型的值
     */
    @SuppressWarnings("unchecked")
    @Override
    public Class<OrEquals> getOrAnnotation() {
        return OrEquals.class;
    }

    /**
     * 获取 {@link OrNotEquals} 注解的 {@code Class} 类型的值.
     *
     * @return {@link OrNotEquals} 注解的 {@code Class} 类型的值
     */
    @SuppressWarnings("unchecked")
    @Override
    public Class<OrNotEquals> getOrNotAnnotation() {
        return OrNotEquals.class;
    }

    /**
     * 构造 {@code AND} 关系的 {@link Predicate} 实例的方法.
     *
     * @param criteriaBuilder {@link CriteriaBuilder} 实例
     * @param from            {@link From} 实例
     * @param fieldName       实体类的属性名
     * @param value           对应属性的值
     * @param annotation      前字段使用的注解
     * @return {@link Predicate} 实例
     */
    @Override
    public <Z, X> Predicate buildAndPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value, Object annotation) {
        return criteriaBuilder.and(this.buildEqualsPredicate(criteriaBuilder, from, fieldName, value));
    }

    /**
     * 构造 {@code AND} 关系的 {@link Predicate} 实例的方法.
     *
     * @param criteriaBuilder {@link CriteriaBuilder} 实例
     * @param from            {@link From} 实例
     * @param fieldName       属性字段名称
     * @param value           属性条件对应的值
     * @return {@link Predicate} 实例
     */
    @Override
    public <Z, X> Predicate buildAndPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value) {
        return criteriaBuilder.and(this.buildEqualsPredicate(criteriaBuilder, from, fieldName, value));
    }

    /**
     * 构造 {@code AND NOT} 关系的 {@link Predicate} 实例的方法.
     *
     * @param criteriaBuilder {@link CriteriaBuilder} 实例
     * @param from            {@link From} 实例
     * @param fieldName       实体类的属性名
     * @param value           对应属性的值
     * @param annotation      前字段使用的注解
     * @return {@link Predicate} 实例
     */
    @Override
    public <Z, X> Predicate buildAndNotPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value, Object annotation) {
        return criteriaBuilder.and(this.buildNotEqualsPredicate(criteriaBuilder, from, fieldName, value));
    }

    /**
     * 构造 {@code AND NOT} 关系的 {@link Predicate} 实例的方法.
     *
     * @param criteriaBuilder {@link CriteriaBuilder} 实例
     * @param from            {@link From} 实例
     * @param fieldName       属性字段名称
     * @param value           属性条件对应的值
     * @return {@link Predicate} 实例
     */
    @Override
    public <Z, X> Predicate buildAndNotPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value) {
        return criteriaBuilder.and(this.buildNotEqualsPredicate(criteriaBuilder, from, fieldName, value));
    }

    /**
     * 构造 {@code OR} 关系的 {@link Predicate} 实例的方法.
     *
     * @param criteriaBuilder {@link CriteriaBuilder} 实例
     * @param from            {@link From} 实例
     * @param fieldName       实体类的属性名
     * @param value           对应属性的值
     * @param annotation      前字段使用的注解
     * @return {@link Predicate} 实例
     */
    @Override
    public <Z, X> Predicate buildOrPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value, Object annotation) {
        return criteriaBuilder.or(this.buildEqualsPredicate(criteriaBuilder, from, fieldName, value));
    }

    /**
     * 构造 {@code OR} 关系的 {@link Predicate} 实例的方法.
     *
     * @param criteriaBuilder {@link CriteriaBuilder} 实例
     * @param from            {@link From} 实例
     * @param fieldName       属性字段名称
     * @param value           属性条件对应的值
     * @return {@link Predicate} 实例
     */
    @Override
    public <Z, X> Predicate buildOrPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value) {
        return criteriaBuilder.or(this.buildEqualsPredicate(criteriaBuilder, from, fieldName, value));
    }

    /**
     * 构造 {@code OR NOT} 关系的 {@link Predicate} 实例的方法.
     *
     * @param criteriaBuilder {@link CriteriaBuilder} 实例
     * @param from            {@link From} 实例
     * @param fieldName       实体类的属性名
     * @param value           对应属性的值
     * @param annotation      前字段使用的注解
     * @return {@link Predicate} 实例
     */
    @Override
    public <Z, X> Predicate buildOrNotPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value, Object annotation) {
        return criteriaBuilder.or(this.buildNotEqualsPredicate(criteriaBuilder, from, fieldName, value));
    }

    /**
     * 构造 {@code OR NOT} 关系的 {@link Predicate} 实例的方法.
     *
     * @param criteriaBuilder {@link CriteriaBuilder} 实例
     * @param from            {@link From} 实例
     * @param fieldName       属性字段名称
     * @param value           属性条件对应的值
     * @return {@link Predicate} 实例
     */
    @Override
    public <Z, X> Predicate buildOrNotPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value) {
        return criteriaBuilder.or(this.buildNotEqualsPredicate(criteriaBuilder, from, fieldName, value));
    }

    private <Z, X> Predicate buildEqualsPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value) {
        return criteriaBuilder.equal(from.get(fieldName), value);
    }

    private <Z, X> Predicate buildNotEqualsPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value) {
        return criteriaBuilder.notEqual(from.get(fieldName), value);
    }

}
