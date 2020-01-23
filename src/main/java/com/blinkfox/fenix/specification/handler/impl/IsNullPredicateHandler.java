package com.blinkfox.fenix.specification.handler.impl;

import com.blinkfox.fenix.specification.annotation.IsNull;
import com.blinkfox.fenix.specification.handler.AbstractPredicateHandler;

import java.lang.annotation.Annotation;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

/**
 * 构建“是 NULL 条件”({@code field IS NULL})场景的 {@link Predicate} 处理器.
 *
 * @author YangWenpeng on 2019-12-17
 * @author blinkfox on 2020-01-14
 * @since v2.2.0
 */
public class IsNullPredicateHandler extends AbstractPredicateHandler {

    @Override
    public Class<IsNull> getAnnotation() {
        return IsNull.class;
    }

    @Override
    public <Z, X> Predicate buildPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String name, Object value, Annotation annotation) {
        return criteriaBuilder.and(super.buildIsNullPredicate(criteriaBuilder, from, value));
    }

}
