package com.blinkfox.fenix.specification.handler.impl;

import com.blinkfox.fenix.specification.handler.AbstractPredicateHandler;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

/**
 * 构建“范围不匹配条件”({@code field NOT IN ('xxx', 'yyy')})场景的 Specification 监听器.
 *
 * @author YangWenpeng on 2019-12-17
 * @author blinkfox on 2020-01-14
 * @since v2.2.0
 */
public class NotInSpecificationHandler extends AbstractPredicateHandler {

//    @Override
//    protected <Z, X> Predicate buildPredicate(
//            CriteriaBuilder criteriaBuilder, From<Z, X> from, String name, Object value, Object annotation) {
//        CriteriaBuilder.In<Object> in = criteriaBuilder.in(from.get(name));
//        value = value.getClass().isArray() ? Arrays.asList((Object[]) value) : value;
//
//        if (value instanceof Collection) {
//            Collection<?> list = (Collection<?>) value;
//            if (list.isEmpty()) {
//                return new FenixBooleanStaticPredicate(
//                        (CriteriaBuilderImpl) criteriaBuilder, true, BooleanOperator.AND);
//            } else {
//                list.forEach(in::value);
//            }
//        } else {
//            in.value(value);
//        }
//        return criteriaBuilder.not(in);
//    }
//
//    @Override
//    protected <Z, X> Predicate buildPredicate(CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value) {
//        return null;
//    }
//
//    @SuppressWarnings("unchecked")
//    @Override
//    public Class<NotIn> getAnnotation() {
//        return NotIn.class;
//    }

    @Override
    protected <T> Class<T> getAndAnnotation() {
        return null;
    }

    @Override
    protected <T> Class<T> getAndNotAnnotation() {
        return null;
    }

    @Override
    protected <T> Class<T> getOrAnnotation() {
        return null;
    }

    @Override
    protected <T> Class<T> getOrNotAnnotation() {
        return null;
    }

    @Override
    protected <Z, X> Predicate buildAndPredicate(CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value, Object annotation) {
        return null;
    }

    @Override
    protected <Z, X> Predicate buildAndPredicate(CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value) {
        return null;
    }

    @Override
    protected <Z, X> Predicate buildAndNotPredicate(CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value, Object annotation) {
        return null;
    }

    @Override
    protected <Z, X> Predicate buildAndNotPredicate(CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value) {
        return null;
    }

    @Override
    protected <Z, X> Predicate buildOrPredicate(CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value, Object annotation) {
        return null;
    }

    @Override
    protected <Z, X> Predicate buildOrPredicate(CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value) {
        return null;
    }

    @Override
    protected <Z, X> Predicate buildOrNotPredicate(CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value, Object annotation) {
        return null;
    }

    @Override
    protected <Z, X> Predicate buildOrNotPredicate(CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value) {
        return null;
    }
}
