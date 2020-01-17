package com.blinkfox.fenix.specification.handler.impl;

import com.blinkfox.fenix.helper.CollectionHelper;
import com.blinkfox.fenix.specification.annotation.LikeIn;
import com.blinkfox.fenix.specification.handler.AbstractSpecificationHandler;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

/**
 * 构建“模糊条件”({@code field LIKE '%xx%' OR field LIKE '%yyy%'})场景的 Specification 监听器.
 *
 * @author YangWenpeng on 2019-12-17
 * @author blinkfox on 2020-01-14
 * @since v2.2.0
 */
public class LikeinSpecificationHandler extends AbstractSpecificationHandler {

    @SuppressWarnings("unchecked")
    @Override
    protected <Z, X> Predicate buildPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String name, Object value, Object annotation) {
        Predicate p = null;
        List<Object> list = (List<Object>) value;
        if (CollectionHelper.isNotEmpty(list)) {
            for (int i = 0, len = list.size(); i < len; i++) {
                Object v = list.get(i);
                String pattern = "%" + String.valueOf(v).replace("%", "\\%") + "%";
                if (i == 0) {
                    p = criteriaBuilder.like(from.get(name), pattern);
                } else {
                    p = criteriaBuilder.or(criteriaBuilder.like(from.get(name), pattern), p);
                }
            }
        }
        return criteriaBuilder.and(p);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<LikeIn> getAnnotation() {
        return LikeIn.class;
    }

}
