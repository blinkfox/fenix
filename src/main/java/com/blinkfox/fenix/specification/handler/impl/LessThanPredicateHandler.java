package com.blinkfox.fenix.specification.handler.impl;

import com.blinkfox.fenix.specification.annotation.LessThan;
import com.blinkfox.fenix.specification.handler.AbstractPredicateHandler;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Predicate;
import java.lang.annotation.Annotation;

/**
 * 构建“小于条件”({@code field < 'xxx'})场景的 {@link Predicate} 处理器.
 *
 * @author YangWenpeng on 2019-12-17
 * @author blinkfox on 2020-01-14
 * @since v2.2.0
 */
public class LessThanPredicateHandler extends AbstractPredicateHandler {

    @Override
    public Class<LessThan> getAnnotation() {
        return LessThan.class;
    }

    @Override
    public <Z, X> Predicate buildPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value, Annotation annotation) {
        return criteriaBuilder.and(super.buildLessThanPredicate(criteriaBuilder, from, fieldName, value));
    }

}
