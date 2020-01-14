package com.blinkfox.fenix.specification.listener.impl;

import com.blinkfox.fenix.exception.BuildSpecificationException;
import com.blinkfox.fenix.specification.SpecificationSupplier;
import com.blinkfox.fenix.specification.listener.AbstractListener;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;

import org.springframework.stereotype.Component;

/**
 * 构建“实体连接条件”({@code JOIN})场景的 Specification 监听器.
 *
 * @author YangWenpeng on 2019-12-17
 * @author blinkfox on 2020-01-14
 * @since v2.2.0
 */
@Component
public class JoinSpecificationListener extends AbstractListener {

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
        List<Predicate> predicates = SpecificationSupplier.paramToPredicate(subJoin, criteriaBuilder, value);
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<com.blinkfox.fenix.specification.annotation.Join> getAnnotation() {
        return com.blinkfox.fenix.specification.annotation.Join.class;
    }

}
