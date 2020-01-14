package com.blinkfox.fenix.specification.listener.impl;

import com.blinkfox.fenix.helper.CollectionHelper;
import com.blinkfox.fenix.specification.annotation.LikeIn;
import com.blinkfox.fenix.specification.listener.AbstractListener;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

import org.springframework.stereotype.Component;

/**
 * 构建“模糊条件”({@code field1 LIKE '%xx%' OR field2 LIKE '%yyy%'})场景的 Specification 监听器.
 *
 * @author YangWenpeng on 2019-12-17
 * @author blinkfox on 2020-01-14
 * @since v2.2.0
 */
@Component
public class LikeinSpecificationListener extends AbstractListener {

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
