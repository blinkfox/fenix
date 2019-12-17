package com.blinkfox.fenix.specification.listener.impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

import org.springframework.util.CollectionUtils;

import com.blinkfox.fenix.specification.annotation.LikeIn;
import com.blinkfox.fenix.specification.listener.AbstractListener;

/**
 * 
 * LikeinSpecificationListener
 * 
 * @description like查询数组监听器
 * @author lichuan
 * @date 2019年9月16日 下午5:45:00
 * @version TODO
 */
public class LikeinSpecificationListener extends AbstractListener {

    @SuppressWarnings("unchecked")
    @Override
    public Class<LikeIn> getAnnotation() {
        return LikeIn.class;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <Z, X> Predicate buildPredicate(CriteriaBuilder criteriaBuilder, From<Z, X> from, String name,
        Object value, Object annotation) {
        Predicate p = null;
        List<Object> vs = (List<Object>)value;
        if (!CollectionUtils.isEmpty(vs)) {
            for (int i = 0; i < vs.size(); i++) {
                Object v = vs.get(i);
                if (i == 0) {
                    p = criteriaBuilder.like(from.get(name), "%" + String.valueOf(v).replace("%", "\\%") + "%");
                } else {
                    p = criteriaBuilder
                        .or(criteriaBuilder.like(from.get(name), "%" + String.valueOf(v).replace("%", "\\%") + "%"), p);
                }
            }
        }
        return criteriaBuilder.and(p);
    }

}
