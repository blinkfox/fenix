package com.blinkfox.fenix.specification.listener.impl;

import com.blinkfox.fenix.exception.BuildSpecificationException;
import com.blinkfox.fenix.specification.annotation.OrLikeOrLike;
import com.blinkfox.fenix.specification.listener.AbstractSpecificationHandler;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

/**
 * 构建“或者模糊条件”({@code OR (field1 LIKE '%xx%' OR field2 LIKE '%yyy%')})场景的 Specification 监听器.
 *
 * @author YangWenpeng on 2019-12-17
 * @author blinkfox on 2020-01-14
 * @since v2.2.0
 */
public class OrLikeOrLikeSpecificationHandler extends AbstractSpecificationHandler {

    @SuppressWarnings("unchecked")
    @Override
    protected <Z, X> Predicate buildPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String name, Object value, Object annotation) {
        String[] fileds = ((OrLikeOrLike) annotation).fileds();
        List<Object> values = (List<Object>) value;
        int length = fileds.length;
        if (length != values.size()) {
            throw new BuildSpecificationException(
                    "【Fenix 异常】对【" + name + "】使用【@LikeOrLike】时，注解上【fields】长度和字段值的大小不同，fileds长为:【"
                            + fileds.length + "】,字段值大小为:【" + values.size() + "】.");
        }

        List<Predicate> predicates = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            predicates.add(criteriaBuilder.like(from.get(fileds[i]),
                    "%" + values.get(i).toString().replace("%", "\\%") + "%"));
        }
        return criteriaBuilder.or(criteriaBuilder.or(predicates.toArray(new Predicate[0])));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<OrLikeOrLike> getAnnotation() {
        return OrLikeOrLike.class;
    }

}
