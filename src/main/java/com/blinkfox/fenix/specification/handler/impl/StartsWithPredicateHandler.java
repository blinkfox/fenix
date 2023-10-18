package com.blinkfox.fenix.specification.handler.impl;

import com.blinkfox.fenix.specification.annotation.StartsWith;
import com.blinkfox.fenix.specification.handler.AbstractPredicateHandler;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Predicate;
import java.lang.annotation.Annotation;

/**
 * 构建“按前缀模糊匹配”({@code AND field LIKE 'xx%'})场景的 {@link Predicate} 处理器.
 *
 * @author blinkfox on 2020-01-25
 * @since v2.2.0
 */
public class StartsWithPredicateHandler extends AbstractPredicateHandler {

    @Override
    public Class<StartsWith> getAnnotation() {
        return StartsWith.class;
    }

    @Override
    public <Z, X> Predicate buildPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value, Annotation annotation) {
        return criteriaBuilder.and(super.buildStartsWithPredicate(criteriaBuilder, from, fieldName, value));
    }

}
