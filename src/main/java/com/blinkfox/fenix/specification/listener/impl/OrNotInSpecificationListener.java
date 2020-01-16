package com.blinkfox.fenix.specification.listener.impl;

import com.blinkfox.fenix.specification.annotation.OrNotIn;
import com.blinkfox.fenix.specification.listener.AbstractSpecificationListener;
import com.blinkfox.fenix.specification.predicate.FenixBooleanStaticPredicate;

import java.util.Arrays;
import java.util.Collection;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Predicate.BooleanOperator;

import org.hibernate.query.criteria.internal.CriteriaBuilderImpl;
import org.springframework.stereotype.Component;

/**
 * 构建“或者不匹配范围条件”({@code OR field NOT IN ('xxx', 'yyy')})场景的 Specification 监听器.
 *
 * @author YangWenpeng on 2019-12-17
 * @author blinkfox on 2020-01-14
 * @since v2.2.0
 */
@Component
public class OrNotInSpecificationListener extends AbstractSpecificationListener {

    @Override
    protected <Z, X> Predicate buildPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String name, Object value, Object annotation) {
        CriteriaBuilder.In<Object> in = criteriaBuilder.in(from.get(name));
        if (value.getClass().isArray()) {
            value = Arrays.asList((Object[]) value);
        }

        if (value instanceof Collection) {
            Collection<?> list = (Collection<?>) value;
            if (list.isEmpty()) {
                return new FenixBooleanStaticPredicate(
                        (CriteriaBuilderImpl) criteriaBuilder, true, BooleanOperator.OR);
            } else {
                list.forEach(in::value);
            }
        } else {
            in.value(value);
        }
        return criteriaBuilder.or(criteriaBuilder.not(in));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<OrNotIn> getAnnotation() {
        return OrNotIn.class;
    }

}
