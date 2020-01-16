package com.blinkfox.fenix.specification.listener.impl;

import com.blinkfox.fenix.specification.annotation.OrIsNotNull;
import com.blinkfox.fenix.specification.listener.AbstractSpecificationHandler;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

/**
 * 构建“或者不是 NULL 条件”({@code OR field IS NOT NULL})场景的 Specification 监听器.
 *
 * @author YangWenpeng on 2019-12-17
 * @author blinkfox on 2020-01-14
 * @since v2.2.0
 */
public class OrIsNotNullSpecificationHandler extends AbstractSpecificationHandler {

    @Override
    protected <Z, X> Predicate buildPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String name, Object value, Object annotation) {
        return criteriaBuilder.or(criteriaBuilder.isNotNull(from.get(String.valueOf(value))));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<OrIsNotNull> getAnnotation() {
        return OrIsNotNull.class;
    }

}
