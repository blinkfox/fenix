package com.blinkfox.fenix.specification.handler.impl;

import com.blinkfox.fenix.specification.annotation.OrNotBetween;
import com.blinkfox.fenix.specification.handler.AbstractPredicateHandler;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Predicate;
import java.lang.annotation.Annotation;

/**
 * 构建“或语句区间不匹配条件”({@code AND field NOT BETWEEN ... AND ...})场景的 {@link Predicate} 处理器.
 * 若结束值为空，则退化生成为大于等于的条件，若开始值为空.则退化生成为小于等于的条件，若开始值或结束值都为空，则直接抛出异常.
 *
 * @author blinkfox on 2020-01-26
 * @since v2.2.0
 */
public class OrNotBetweenPredicateHandler extends AbstractPredicateHandler {

    @Override
    public Class<OrNotBetween> getAnnotation() {
        return OrNotBetween.class;
    }

    @Override
    public <Z, X> Predicate buildPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value, Annotation annotation) {
        return criteriaBuilder.or(criteriaBuilder.not(
                super.buildBetweenPredicate(criteriaBuilder, from, fieldName, value)));
    }

}
