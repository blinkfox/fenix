package com.blinkfox.fenix.specification.handler.impl;

import com.blinkfox.fenix.specification.annotation.OrIsNotNull;
import com.blinkfox.fenix.specification.handler.AbstractPredicateHandler;
import java.lang.annotation.Annotation;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

/**
 * 构建“或者不是 NULL 条件”({@code OR field IS NOT NULL})场景的 {@link Predicate} 处理器.
 *
 * @author YangWenpeng on 2019-12-17
 * @author blinkfox on 2020-01-14
 * @since v2.2.0
 */
public class OrIsNotNullPredicateHandler extends AbstractPredicateHandler {

    @Override
    public Class<OrIsNotNull> getAnnotation() {
        return OrIsNotNull.class;
    }

    @Override
    public <Z, X> Predicate buildPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value, Annotation annotation) {
        return criteriaBuilder.or(super.buildIsNotNullPredicate(criteriaBuilder, from, value));
    }

}
