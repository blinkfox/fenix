package com.blinkfox.fenix.specification.listener.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

import org.springframework.stereotype.Component;

import com.blinkfox.fenix.specification.BuildSpecificationException;
import com.blinkfox.fenix.specification.annotation.LikeOrLike;
import com.blinkfox.fenix.specification.listener.AbstractListener;

/**
 * OrNullSpecificationListener
 * 
 * @description Or xxx is null
 * @author YangWenpeng
 * @date 2019年5月5日 下午4:33:32
 * @version v1.0.0
 */
@Component
public class LikeOrLikeSpecificationListener extends AbstractListener {

    /**
     * @see com.thunisoft.framework.jpaplus.specification.listener.AbstractListener#getAnnotation()
     */
    @SuppressWarnings("unchecked")
    @Override
    public Class<LikeOrLike> getAnnotation() {
        return LikeOrLike.class;
    }

    /**
     * @see com.thunisoft.framework.jpaplus.specification.listener.AbstractListener#buildPredicate(javax.persistence.criteria.CriteriaBuilder,
     *      javax.persistence.criteria.Root, java.lang.String, java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    @Override
    protected <Z, X> Predicate buildPredicate(CriteriaBuilder criteriaBuilder, From<Z, X> from, String name,
        Object value, Object annotation) {
        LikeOrLike likeOrlike = (LikeOrLike)annotation;
        String[] fileds = likeOrlike.fileds();
        List<Object> values = (List<Object>)value;
        if (fileds.length != values.size()) {
            throw new BuildSpecificationException(
                "对 " + name + " 使用LikeOrLike时，注解上fields长度和字段值的大小不同，fileds长为:" + fileds.length + ";字段值大小为：" + values.size());
        }
        List<Predicate> predicates = new ArrayList<>();
        for (int i = 0; i < fileds.length; i++) {
            predicates
                .add(criteriaBuilder.like(from.get(fileds[i]), "%" + values.get(i).toString().replace("%", "\\%") + "%"));
        }
        Predicate[] restrictions = new Predicate[predicates.size()];
        return criteriaBuilder.and(criteriaBuilder.or(predicates.toArray(restrictions)));
    }

}
