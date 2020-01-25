package com.blinkfox.fenix.specification.handler.impl;

import com.blinkfox.fenix.specification.annotation.OrStartsWith;
import com.blinkfox.fenix.specification.handler.AbstractPredicateHandler;

import java.lang.annotation.Annotation;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

/**
 * 构建“或语句按前缀模糊匹配”({@code OR field LIKE '%xx%'})场景的 {@link Predicate} 处理器.
 *
 * @author blinkfox on 2020-01-25
 * @since v2.2.0
 */
public class OrStartsWithPredicateHandler extends AbstractPredicateHandler {

    @Override
    public Class<OrStartsWith> getAnnotation() {
        return OrStartsWith.class;
    }

    @Override
    public <Z, X> Predicate buildPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value, Annotation annotation) {
        return criteriaBuilder.or(super.buildStartsWithPredicate(criteriaBuilder, from, fieldName, value));
    }

}
