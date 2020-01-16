package com.blinkfox.fenix.specification.listener.impl;

import com.blinkfox.fenix.specification.annotation.OrLike;
import com.blinkfox.fenix.specification.listener.AbstractSpecificationHandler;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

import org.springframework.stereotype.Component;

/**
 * 构建“或者模糊条件”({@code OR field1 LIKE '%xx%'})场景的 Specification 监听器.
 *
 * @author YangWenpeng on 2019-12-17
 * @author blinkfox on 2020-01-14
 * @since v2.2.0
 */
@Component
public class OrLikeSpecificationHandler extends AbstractSpecificationHandler {

    @Override
    protected <Z, X> Predicate buildPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String name, Object value, Object annotation) {
        return criteriaBuilder.or(criteriaBuilder.like(from.get(name),
                "%" + value.toString().replace("%", "\\%") + "%"));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<OrLike> getAnnotation() {
        return OrLike.class;
    }

}
