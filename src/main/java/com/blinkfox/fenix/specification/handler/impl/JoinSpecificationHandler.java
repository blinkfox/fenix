package com.blinkfox.fenix.specification.handler.impl;

import com.blinkfox.fenix.exception.BuildSpecificationException;
import com.blinkfox.fenix.specification.FenixSpecification;
import com.blinkfox.fenix.specification.handler.AbstractSpecificationHandler;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;

/**
 * 构建“实体连接条件”({@code JOIN})场景的 Specification 监听器.
 *
 * @author YangWenpeng on 2019-12-17
 * @author blinkfox on 2020-01-14
 * @since v2.2.0
 */
public class JoinSpecificationHandler extends AbstractSpecificationHandler {

    @Override
    protected <Z, X> Predicate buildPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String name, Object value, Object annotation) {
        if (!(annotation instanceof com.blinkfox.fenix.specification.annotation.Join)) {
            throw new BuildSpecificationException("【Fenix 异常】使用【@Join】构建表连接时,【" + getClass().getName()
                    + ".getAnnotation()】获取到的值【" + this.getAnnotation().getName() + "】与字段使用的注解值【"
                    + annotation.getClass().getName() + "】不同");
        }

        Join<X, ?> subJoin = from.join(name,
                ((com.blinkfox.fenix.specification.annotation.Join) annotation).joinType());
        List<Predicate> predicates = FenixSpecification.beanParamToPredicate(subJoin, criteriaBuilder, value);
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    @Override
    protected <Z, X> Predicate buildPredicate(CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value) {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<com.blinkfox.fenix.specification.annotation.Join> getAnnotation() {
        return com.blinkfox.fenix.specification.annotation.Join.class;
    }

}
