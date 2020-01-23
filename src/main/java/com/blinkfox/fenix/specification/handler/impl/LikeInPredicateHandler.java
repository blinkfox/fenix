package com.blinkfox.fenix.specification.handler.impl;

import com.blinkfox.fenix.helper.CollectionHelper;
import com.blinkfox.fenix.specification.annotation.LikeIn;
import com.blinkfox.fenix.specification.handler.AbstractPredicateHandler;

import java.lang.annotation.Annotation;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

/**
 * 构建“模糊条件”({@code field LIKE '%xx%' OR field LIKE '%yyy%'})场景的 {@link Predicate} 处理器.
 *
 * @author YangWenpeng on 2019-12-17
 * @author blinkfox on 2020-01-14
 * @since v2.2.0
 */
public class LikeInPredicateHandler extends AbstractPredicateHandler {

    @Override
    public Class<LikeIn> getAnnotation() {
        return LikeIn.class;
    }


    @Override
    public <Z, X> Predicate buildPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value, Annotation annotation) {
        return this.buildPredicate(criteriaBuilder, from, fieldName, value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <Z, X> Predicate buildPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value) {
        Predicate p = null;
        List<Object> list = (List<Object>) value;
        if (CollectionHelper.isNotEmpty(list)) {
            for (int i = 0, len = list.size(); i < len; i++) {
                Object v = list.get(i);
                String pattern = "%" + String.valueOf(v).replace("%", "\\%") + "%";
                if (i == 0) {
                    p = criteriaBuilder.like(from.get(fieldName), pattern);
                } else {
                    p = criteriaBuilder.or(criteriaBuilder.like(from.get(fieldName), pattern), p);
                }
            }
        }
        return criteriaBuilder.and(p);
    }

}
