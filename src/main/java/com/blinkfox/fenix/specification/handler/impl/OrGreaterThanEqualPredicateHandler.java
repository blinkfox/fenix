package com.blinkfox.fenix.specification.handler.impl;

import com.blinkfox.fenix.specification.annotation.OrGreaterThanEqual;
import com.blinkfox.fenix.specification.handler.AbstractPredicateHandler;
import java.lang.annotation.Annotation;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

/**
 * 构建“或者大于等于条件”({@code OR field >= 'xxx'})场景的 {@link Predicate} 处理器.
 *
 * @author YangWenpeng on 2019-12-17
 * @author blinkfox on 2020-01-14
 * @since v2.2.0
 */
public class OrGreaterThanEqualPredicateHandler extends AbstractPredicateHandler {

    @Override
    public Class<OrGreaterThanEqual> getAnnotation() {
        return OrGreaterThanEqual.class;
    }

    @Override
    public <Z, X> Predicate buildPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value, Annotation annotation) {
        return criteriaBuilder.or(super.buildGreaterThanEqualPredicate(criteriaBuilder, from, fieldName, value));
    }

}
