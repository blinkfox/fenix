package com.blinkfox.fenix.specification.handler.impl;

import com.blinkfox.fenix.specification.annotation.EndsWith;
import com.blinkfox.fenix.specification.handler.AbstractPredicateHandler;
import java.lang.annotation.Annotation;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

/**
 * 构建“按后缀模糊匹配”({@code AND field LIKE '%xx'})场景的 {@link Predicate} 处理器.
 *
 * @author blinkfox on 2020-01-25
 * @since v2.2.0
 */
public class EndsWithPredicateHandler extends AbstractPredicateHandler {

    @Override
    public Class<EndsWith> getAnnotation() {
        return EndsWith.class;
    }

    @Override
    public <Z, X> Predicate buildPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value, Annotation annotation) {
        return criteriaBuilder.and(super.buildEndsWithPredicate(criteriaBuilder, from, fieldName, value));
    }

}
