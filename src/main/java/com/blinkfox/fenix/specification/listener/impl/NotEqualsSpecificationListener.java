package com.blinkfox.fenix.specification.listener.impl;

import com.blinkfox.fenix.specification.annotation.NotEquals;
import com.blinkfox.fenix.specification.listener.AbstractSpecificationListener;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

import org.springframework.stereotype.Component;

/**
 * 构建“不等于条件”({@code field <> 'xxx'})场景的 Specification 监听器.
 *
 * @author YangWenpeng on 2019-12-17
 * @author blinkfox on 2020-01-14
 * @since v2.2.0
 */
@Component
public class NotEqualsSpecificationListener extends AbstractSpecificationListener {

    @Override
    protected <Z, X> Predicate buildPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String name, Object value, Object annotation) {
        return criteriaBuilder.and(criteriaBuilder.notEqual(from.get(name), value));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<NotEquals> getAnnotation() {
        return NotEquals.class;
    }

}
