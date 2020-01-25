package com.blinkfox.fenix.specification.handler.impl;

import com.blinkfox.fenix.specification.annotation.LikePattern;
import com.blinkfox.fenix.specification.handler.AbstractPredicateHandler;

import java.lang.annotation.Annotation;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

/**
 * 构建“按指定模式匹配”({@code field LIKE '%xx%'})场景的 {@link Predicate} 处理器.
 *
 * @author blinkfox on 2020-01-26
 * @since v2.2.0
 */
public class LikePatternPredicateHandler extends AbstractPredicateHandler {

    @Override
    public Class<LikePattern> getAnnotation() {
        return LikePattern.class;
    }

    @Override
    public <Z, X> Predicate buildPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value, Annotation annotation) {
        return criteriaBuilder.and(super.buildLikePatternPredicate(criteriaBuilder, from, fieldName, value));
    }

}
