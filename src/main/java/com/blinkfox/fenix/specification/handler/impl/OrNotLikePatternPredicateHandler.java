package com.blinkfox.fenix.specification.handler.impl;

import com.blinkfox.fenix.specification.annotation.OrNotLikePattern;
import com.blinkfox.fenix.specification.handler.AbstractPredicateHandler;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Predicate;
import java.lang.annotation.Annotation;

/**
 * 构建“或语句按指定模式不匹配”({@code field LIKE '%xx%'})场景的 {@link Predicate} 处理器.
 *
 * @author blinkfox on 2020-01-26
 * @since v2.2.0
 */
public class OrNotLikePatternPredicateHandler extends AbstractPredicateHandler {

    @Override
    public Class<OrNotLikePattern> getAnnotation() {
        return OrNotLikePattern.class;
    }

    @Override
    public <Z, X> Predicate buildPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value, Annotation annotation) {
        return criteriaBuilder.or(super.buildNotLikePatternPredicate(criteriaBuilder, from, fieldName, value));
    }

}
