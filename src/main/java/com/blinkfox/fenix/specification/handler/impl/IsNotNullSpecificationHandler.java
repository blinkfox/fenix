package com.blinkfox.fenix.specification.handler.impl;

import com.blinkfox.fenix.specification.annotation.IsNotNull;
import com.blinkfox.fenix.specification.handler.AbstractSpecificationHandler;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

/**
 * 构建“不是 NULL 条件”({@code field IS NOT NULL})场景的 Specification 监听器.
 *
 * @author YangWenpeng on 2019-12-17
 * @author blinkfox on 2020-01-14
 * @since v2.2.0
 */
public class IsNotNullSpecificationHandler extends AbstractSpecificationHandler {

    @Override
    protected <Z, X> Predicate buildPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String name, Object value, Object annotation) {
        return criteriaBuilder.and(criteriaBuilder.isNotNull(from.get(String.valueOf(value))));
    }

    @Override
    protected <Z, X> Predicate buildPredicate(CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value) {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<IsNotNull> getAnnotation() {
        return IsNotNull.class;
    }

}
