package com.blinkfox.fenix.specification.listener.impl;

import com.blinkfox.fenix.specification.annotation.NotNull;
import com.blinkfox.fenix.specification.listener.AbstractListener;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

import org.springframework.stereotype.Component;

/**
 * 构建“不是 NULL 条件”({@code field IS NOT NULL})场景的 Specification 监听器.
 *
 * @author YangWenpeng on 2019-12-17
 * @author blinkfox on 2020-01-14
 * @since v2.2.0
 */
@Component
public class NotNullSpecificationListener extends AbstractListener {

    @Override
    protected <Z, X> Predicate buildPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String name, Object value, Object annotation) {
        return criteriaBuilder.and(criteriaBuilder.isNotNull(from.get((String) value)));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<NotNull> getAnnotation() {
        return NotNull.class;
    }

}
