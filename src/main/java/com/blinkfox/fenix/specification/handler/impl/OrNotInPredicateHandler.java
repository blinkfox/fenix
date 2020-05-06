package com.blinkfox.fenix.specification.handler.impl;

import com.blinkfox.fenix.specification.annotation.OrNotIn;
import com.blinkfox.fenix.specification.handler.AbstractPredicateHandler;
import java.lang.annotation.Annotation;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

/**
 * 构建“或者不匹配范围条件”({@code OR field NOT IN ('xxx', 'yyy')})场景的 {@link Predicate} 处理器.
 *
 * @author YangWenpeng on 2019-12-17
 * @author blinkfox on 2020-01-14
 * @since v2.2.0
 */
public class OrNotInPredicateHandler extends AbstractPredicateHandler {

    @Override
    public Class<OrNotIn> getAnnotation() {
        return OrNotIn.class;
    }

    @Override
    public <Z, X> Predicate buildPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value, Annotation annotation) {
        return criteriaBuilder.or(criteriaBuilder.not(
                super.buildInPredicate(criteriaBuilder, from, fieldName, value, super.isAllowNull(annotation))));
    }

    @Override
    public Predicate buildPredicate(
            CriteriaBuilder criteriaBuilder, From<?, ?> from, String fieldName, Object value) {
        return criteriaBuilder.or(criteriaBuilder.not(
                super.buildInPredicate(criteriaBuilder, from, fieldName, value, false)));
    }

}
