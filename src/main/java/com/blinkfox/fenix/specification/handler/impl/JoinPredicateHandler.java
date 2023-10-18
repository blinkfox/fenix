package com.blinkfox.fenix.specification.handler.impl;

import com.blinkfox.fenix.exception.BuildSpecificationException;
import com.blinkfox.fenix.specification.FenixSpecification;
import com.blinkfox.fenix.specification.handler.AbstractPredicateHandler;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import java.lang.annotation.Annotation;

/**
 * 构建“实体连接条件”({@code JOIN})场景的 {@link Predicate} 处理器.
 *
 * @author YangWenpeng on 2019-12-17
 * @author blinkfox on 2020-01-14
 * @since v2.2.0
 */
public class JoinPredicateHandler extends AbstractPredicateHandler {

    @Override
    public Class<com.blinkfox.fenix.specification.annotation.Join> getAnnotation() {
        return com.blinkfox.fenix.specification.annotation.Join.class;
    }

    @Override
    public <Z, X> Predicate buildPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String name, Object value, Annotation annotation) {
        if (!(annotation instanceof com.blinkfox.fenix.specification.annotation.Join)) {
            throw new BuildSpecificationException("【Fenix 异常】使用【@Join】构建表连接时,【" + getClass().getName()
                    + ".getAnnotation()】获取到的值【" + this.getAnnotation().getName() + "】与字段使用的注解值【"
                    + annotation.getClass().getName() + "】不同");
        }

        Join<X, ?> subJoin = from.join(name,
                ((com.blinkfox.fenix.specification.annotation.Join) annotation).joinType());
        return criteriaBuilder.and(FenixSpecification.beanParamToPredicate(subJoin, criteriaBuilder, value)
                .toArray(new Predicate[0]));
    }

    @Override
    public Predicate buildPredicate(
            CriteriaBuilder criteriaBuilder, From<?, ?> from, String fieldName, Object value) {
        throw new BuildSpecificationException("本方法暂不支持.");
    }

}
