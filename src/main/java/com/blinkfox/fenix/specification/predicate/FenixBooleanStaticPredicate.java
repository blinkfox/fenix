/**
 * @projectName fenix
 * @package com.blinkfox.fenix.specification.predicate
 * @className com.blinkfox.fenix.specification.predicate.FenixBooleanStaticPredicate
 * @copyright Copyright 2019 Thuisoft, Inc. All rights reserved.
 */
package com.blinkfox.fenix.specification.predicate;

import org.hibernate.query.criteria.internal.CriteriaBuilderImpl;
import org.hibernate.query.criteria.internal.ParameterRegistry;
import org.hibernate.query.criteria.internal.compile.RenderingContext;
import org.hibernate.query.criteria.internal.predicate.AbstractSimplePredicate;
import org.hibernate.query.criteria.internal.predicate.BooleanStaticAssertionPredicate;

/**
 * FenixBooleanStaticPredicate
 * 
 * @description 装饰了{@link BooleanStaticAssertionPredicate}，主要用来返回永真和永假条件。<br>框架会根据该类的属性去除一些不必要的永真永假条件<br>
 * 该类主要用于处理@In条件中传入了大小为0的集合。建议使用者在调用框架之前对空集合进行过滤。
 * <br>具体使用请看{@link com.blinkfox.fenix.specification.SpecificationSupplier}
 * @author YangWenpeng
 * @date 2019年12月17日 下午7:20:22
 * @version v1.0.0
 */
public class FenixBooleanStaticPredicate extends AbstractSimplePredicate{

    private static final long serialVersionUID = 1L;


    private BooleanStaticAssertionPredicate predicate;
    
    private BooleanOperator operator;

    public FenixBooleanStaticPredicate(CriteriaBuilderImpl criteriaBuilder,Boolean assertedValue, BooleanOperator operator) {
        super(criteriaBuilder);
        predicate = new BooleanStaticAssertionPredicate( criteriaBuilder , assertedValue);
        this.operator = operator;
    }

    /**
     * @see org.hibernate.query.criteria.internal.predicate.PredicateImplementor#render(boolean,
     *      org.hibernate.query.criteria.internal.compile.RenderingContext)
     */
    @Override
    public String render(boolean isNegated, RenderingContext renderingContext) {
        return predicate.render(isNegated,renderingContext);
    }

    /**
     * @see org.hibernate.query.criteria.internal.ParameterContainer#registerParameters(org.hibernate.query.criteria.internal.ParameterRegistry)
     */
    @Override
    public void registerParameters(ParameterRegistry registry) {
        predicate.registerParameters(registry);
    }
    

    /**
     * @return the predicate
     */
    public BooleanStaticAssertionPredicate getPredicate() {
        return predicate;
    }

    /**
     * @return the operator
     */
    public BooleanOperator getOperator() {
        return operator;
    }

    /**
     * @param operator the operator to set
     */
    public void setOperator(BooleanOperator operator) {
        this.operator = operator;
    }

    public boolean getAssertedValue() {
        return predicate.getAssertedValue();
    }

}
