package com.blinkfox.fenix.specification.handler.impl;

import com.blinkfox.fenix.specification.annotation.NotLike;
import com.blinkfox.fenix.specification.handler.AbstractPredicateHandler;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

/**
 * 构建“模糊不匹配条件”({@code field NOT LIKE '%xx%'})场景的 {@link Predicate} 处理器.
 *
 * @author YangWenpeng on 2019-12-17
 * @author blinkfox on 2020-01-14
 * @since v2.2.0
 */
public class NotLikePredicateHandler extends AbstractPredicateHandler {

    @Override
    public Class<NotLike> getAnnotation() {
        return NotLike.class;
    }

    @Override
    public <Z, X> Predicate buildPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value, Object annotation) {
        return criteriaBuilder.and(super.buildNotLikePredicate(criteriaBuilder, from, fieldName, value));
    }

    @Override
    public <Z, X> Predicate buildPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value) {
        return criteriaBuilder.and(super.buildNotLikePredicate(criteriaBuilder, from, fieldName, value));
    }

}
