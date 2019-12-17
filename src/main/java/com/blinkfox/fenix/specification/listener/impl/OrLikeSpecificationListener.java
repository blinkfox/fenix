package com.blinkfox.fenix.specification.listener.impl;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.blinkfox.fenix.specification.annotation.OrLike;
import com.blinkfox.fenix.specification.listener.AbstractListener;

/**
 * OrLikeSpecificationListener
 * 
 * @description 构造or a like xx条件监听器
 * @author YangWenpeng
 * @date 2019年3月27日 下午4:28:03
 * @version v1.0.0
 */
@Component
public class OrLikeSpecificationListener extends AbstractListener {

    Logger log = LoggerFactory.getLogger(getClass());

    
    @Override
    protected <Z,X> Predicate buildPredicate(CriteriaBuilder criteriaBuilder, From<Z,X> from, String name, Object value,
        Object annotation) {
        String valueStr = value.toString().replace("%", "\\%");
        return criteriaBuilder.or(criteriaBuilder.like(from.get(name), "%"+valueStr+"%"));
    }

    /**
     * @see com.thunisoft.nwwz.specification.listener.SpecificationListener#getAnnotation()
     */
    @SuppressWarnings("unchecked")
    @Override
    public Class<OrLike> getAnnotation() {
        return OrLike.class;
    }
    
}
