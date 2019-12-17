package com.blinkfox.fenix.specification.listener.impl;

import java.util.Collection;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Predicate.BooleanOperator;

import org.hibernate.query.criteria.internal.CriteriaBuilderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.blinkfox.fenix.specification.annotation.OrNotIn;
import com.blinkfox.fenix.specification.listener.AbstractListener;
import com.blinkfox.fenix.specification.predicate.FenixBooleanStaticPredicate;

/**
 * OrNotInSpecificationListener
 * 
 * @description 构造NotIn条件监听器
 * @author YangWenpeng
 * @date 2019年3月27日 下午4:28:03
 * @version v1.0.0
 */
@Component
public class OrNotInSpecificationListener extends AbstractListener {

    Logger log = LoggerFactory.getLogger(getClass());

    @Override
    protected <Z, X> Predicate buildPredicate(CriteriaBuilder criteriaBuilder, From<Z, X> from, String name,
        Object value, Object annotation) {
        Path<Object> path = from.get(name);
        CriteriaBuilder.In<Object> in = criteriaBuilder.in(path);
        if (value instanceof Collection) {
            Collection<?> statusList = (Collection<?>)value;
            if (((Collection<?>)value).isEmpty()) {
                return new FenixBooleanStaticPredicate((CriteriaBuilderImpl)criteriaBuilder, true, BooleanOperator.OR);
            } else {
                statusList.stream().forEach(in::value);
            }
        } else {
            in.value(value);
        }
        return criteriaBuilder.or(criteriaBuilder.not(in));
    }

    /**
     * @see com.thunisoft.framework.jpaplus.specification.listener.impl.SpecificationListener#getAnnotation()
     */
    @SuppressWarnings("unchecked")
    @Override
    public Class<OrNotIn> getAnnotation() {
        return OrNotIn.class;
    }
}
