/**
 * @projectName jpaplus-core
 * @package com.thunisoft.framework.jpaplus.specification.listener.impl
 * @className com.thunisoft.framework.jpaplus.specification.listener.impl.EquelsSpecificationListener
 * @copyright Copyright 2019 Thuisoft, Inc. All rights reserved.
 */
package com.blinkfox.fenix.specification.listener.impl;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.blinkfox.fenix.specification.annotation.Equals;
import com.blinkfox.fenix.specification.listener.AbstractListener;

/**
 * EquelsSpecificationListener
 * 
 * @description 构造相等条件监听器
 * @author YangWenpeng
 * @date 2019年3月27日 下午4:28:03
 * @version v1.0.0
 */
@Component
public class EqualsSpecificationListener extends AbstractListener {

    Logger log = LoggerFactory.getLogger(getClass());

    
    @Override
    protected <Z,X> Predicate buildPredicate(CriteriaBuilder criteriaBuilder, From<Z,X> from, String name, Object value,
        Object annotation) {
        return criteriaBuilder.and(criteriaBuilder.equal(from.get(name), value));
    }
    
    /**
     * @see com.thunisoft.framework.jpaplus.specification.listener.SpecificationListener#getAnnotation()
     */
    @SuppressWarnings("unchecked")
    @Override
    public Class<Equals> getAnnotation() {
        return Equals.class;
    }

}
