package com.blinkfox.fenix.specification.handler.impl;

import com.blinkfox.fenix.specification.annotation.OrIn;
import com.blinkfox.fenix.specification.handler.AbstractSpecificationHandler;
import com.blinkfox.fenix.specification.predicate.FenixBooleanStaticPredicate;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Predicate.BooleanOperator;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.query.criteria.internal.CriteriaBuilderImpl;

/**
 * 构建“或者范围匹配条件”({@code field IN ('xxx', 'yyy')})场景的 Specification 监听器.
 *
 * @author YangWenpeng on 2019-12-17
 * @author blinkfox on 2020-01-14
 * @since v2.2.0
 */
@Slf4j
public class OrInSpecificationHandler extends AbstractSpecificationHandler {

    @Override
    protected <Z, X> Predicate buildPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String name, Object value, Object annotation) {
        Path<Object> path = from.get(name);
        CriteriaBuilder.In<Object> in = criteriaBuilder.in(path);

        value = value.getClass().isArray() ? Arrays.asList((Object[]) value) : value;
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
        return criteriaBuilder.or(
                this.getAllowNull(annotation) ? criteriaBuilder.or(in, criteriaBuilder.isNull(path)) : in);
    }

    @Override
    protected <Z, X> Predicate buildPredicate(CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value) {
        return null;
    }

    private boolean getAllowNull(Object annotation) {
        try {
            return (boolean) getAnnotation().getMethod("allowNull").invoke(annotation);
        } catch (IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            log.error("【Fenix 错误提示】获取【@In】注解中【allowNull】时失败，将默认该值为 false.", e);
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<OrIn> getAnnotation() {
        return OrIn.class;
    }

}
