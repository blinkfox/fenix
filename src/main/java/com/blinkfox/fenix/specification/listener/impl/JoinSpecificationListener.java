package com.blinkfox.fenix.specification.listener.impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;

import org.springframework.stereotype.Component;

import com.blinkfox.fenix.specification.BuildSpecificationException;
import com.blinkfox.fenix.specification.SpecificationSupplier;
import com.blinkfox.fenix.specification.listener.AbstractListener;

/**
 * JoinSpecificationListener
 * @description Join条件监听
 * @author YangWenpeng
 * @date 2019年6月4日 下午3:14:04
 * @version v1.0.0
 */
@Component
public class JoinSpecificationListener extends AbstractListener {

    /**
     * @see com.thunisoft.framework.jpaplus.specification.listener.AbstractListener#getAnnotation() 
     */
    @SuppressWarnings("unchecked")
    @Override
    public Class<com.blinkfox.fenix.specification.annotation.Join> getAnnotation() {
        return com.blinkfox.fenix.specification.annotation.Join.class;
    }

    /**
     * @see com.thunisoft.framework.jpaplus.specification.listener.AbstractListener#buildPredicate(javax.persistence.criteria.CriteriaBuilder, javax.persistence.criteria.Root, java.lang.String, java.lang.Object, java.lang.Object) 
     */
    @Override
    protected <Z,X> Predicate buildPredicate(CriteriaBuilder criteriaBuilder, From<Z,X> from, String name, Object value, 
        Object annotation) {
        if(!(annotation instanceof com.blinkfox.fenix.specification.annotation.Join)) {
            throw new BuildSpecificationException("构建Join时,"+getClass().getName()+".getAnnotation()获取的值"+getAnnotation().getName()+"与字段使用的注解值"+annotation.getClass().getName()+"不同");
        }
        com.blinkfox.fenix.specification.annotation.Join joinAnnotation = (com.blinkfox.fenix.specification.annotation.Join)annotation;
        Join<X, ?> subJoin = createJoin(from, name, joinAnnotation.joinType(), joinAnnotation.targetClass());
        List<Predicate> predicates = SpecificationSupplier.paramToPredicate(subJoin, criteriaBuilder, value);
        return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
    }
    
    private <Z,X,C> Join<X, C> createJoin(From<Z, X> from,String name, JoinType joinType, Class<C> valueClass){
        return from.join(name, joinType);
    }

}
