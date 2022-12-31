package com.blinkfox.fenix.specification.handler.impl;

import com.blinkfox.fenix.specification.annotation.Equals;
import com.blinkfox.fenix.specification.handler.AbstractPredicateHandler;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Predicate;
import java.lang.annotation.Annotation;

/**
 * 构建“等值条件”({@code field = 'xxx'})场景的 {@link Predicate} 处理器.
 *
 * @author YangWenpeng on 2019-12-17
 * @author blinkfox on 2020-01-14
 * @since v2.2.0
 */
public class EqualsPredicateHandler extends AbstractPredicateHandler {

    /**
     * 获取 {@link Equals} 注解的 {@code Class} 类型的值.
     *
     * @return {@link Equals} 注解的 {@code Class} 类型的值
     */
    @Override
    public Class<Equals> getAnnotation() {
        return Equals.class;
    }

    @Override
    public <Z, X> Predicate buildPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value, Annotation annotation) {
        return criteriaBuilder.and(super.buildEqualsPredicate(criteriaBuilder, from, fieldName, value));
    }

}
