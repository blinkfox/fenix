package com.blinkfox.fenix.specification.handler.impl;

import com.blinkfox.fenix.specification.annotation.NotIn;
import com.blinkfox.fenix.specification.handler.AbstractPredicateHandler;
import com.blinkfox.fenix.specification.predicate.FenixBooleanStaticPredicate;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

import org.hibernate.query.criteria.internal.CriteriaBuilderImpl;

/**
 * 构建“范围不匹配条件”({@code field NOT IN ('xxx', 'yyy')})场景的 {@link Predicate} 处理器.
 *
 * @author YangWenpeng on 2019-12-17
 * @author blinkfox on 2020-01-14
 * @since v2.2.0
 */
public class NotInPredicateHandler extends AbstractPredicateHandler {

    @Override
    public Class<NotIn> getAnnotation() {
        return NotIn.class;
    }

    @Override
    public <Z, X> Predicate buildPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value, Annotation annotation) {
        return this.buildPredicate(criteriaBuilder, from, fieldName, value);
    }

    @Override
    public <Z, X> Predicate buildPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value) {
        CriteriaBuilder.In<Object> in = criteriaBuilder.in(from.get(fieldName));
        value = value.getClass().isArray() ? Arrays.asList((Object[]) value) : value;

        if (value instanceof Collection) {
            Collection<?> list = (Collection<?>) value;
            if (list.isEmpty()) {
                return new FenixBooleanStaticPredicate(
                        (CriteriaBuilderImpl) criteriaBuilder, true, Predicate.BooleanOperator.AND);
            } else {
                list.forEach(in::value);
            }
        } else {
            in.value(value);
        }
        return criteriaBuilder.and(criteriaBuilder.not(in));
    }

}
