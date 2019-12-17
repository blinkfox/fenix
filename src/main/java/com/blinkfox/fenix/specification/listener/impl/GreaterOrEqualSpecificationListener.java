package com.blinkfox.fenix.specification.listener.impl;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.blinkfox.fenix.specification.annotation.GreaterOrEqual;
import com.blinkfox.fenix.specification.listener.AbstractListener;

/**
 * EquelsSpecificationListener
 * 
 * @description 构造大于等于条件监听器
 * @author YangWenpeng
 * @date 2019年3月27日 下午4:28:03
 * @version v1.0.0
 */
@Component
public class GreaterOrEqualSpecificationListener extends AbstractListener {

    Logger log = LoggerFactory.getLogger(getClass());

    
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    protected <Z,X> Predicate buildPredicate(CriteriaBuilder criteriaBuilder, From<Z,X> from, String name, Object value,
        Object annotation) {
        return criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(from.get(name), (Comparable)value));
    }

    /**
     * @see com.thunisoft.framework.jpaplus.specification.listener.SpecificationListener#getAnnotation()
     */
    @SuppressWarnings("unchecked")
    @Override
    public Class<GreaterOrEqual> getAnnotation() {
        return GreaterOrEqual.class;
    }
    
}
