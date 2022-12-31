package com.blinkfox.fenix.specification.predicate;

import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Predicate.BooleanOperator;
import lombok.Getter;
import lombok.Setter;

/**
 * {@link BooleanStaticAssertionPredicate} 的装饰类，主要用来返回永真和永假条件.
 *
 * <p>本库会根据该类的属性去除一些不必要的永真永假条件该类主要用于处理 {@code @In} 条件中传入了大小为 {@code 0} 的集合。
 * 建议使用者在调用框架之前对空集合进行过滤.</p>
 *
 * @author YangWenpeng on 2019-12-17
 * @author blinkfox 2020-01-14
 * @since v2.2.0
 */
public class FenixBooleanStaticPredicate extends AbstractSimplePredicate {

    private static final long serialVersionUID = 3479513712737513954L;

    @Getter
    private final BooleanStaticAssertionPredicate predicate;

    @Getter
    @Setter
    private Predicate.BooleanOperator operator;

    /**
     * 构造方法.
     *
     * @param criteriaBuilder {@link CriteriaBuilderImpl} 实例
     * @param assertedValue 布尔值
     * @param operator {@link BooleanOperator} 实例
     */
    public FenixBooleanStaticPredicate(
            CriteriaBuilderImpl criteriaBuilder, Boolean assertedValue, BooleanOperator operator) {
        super(criteriaBuilder);
        this.predicate = new BooleanStaticAssertionPredicate(criteriaBuilder, assertedValue);
        this.operator = operator;
    }

    @Override
    public String render(boolean isNegated, RenderingContext renderingContext) {
        return predicate.render(isNegated, renderingContext);
    }

    @Override
    public void registerParameters(ParameterRegistry registry) {
        predicate.registerParameters(registry);
    }

    /**
     * 获取断言的布尔值结果值.
     *
     * @return 布尔值结果
     */
    public boolean getAssertedValue() {
        return predicate.getAssertedValue();
    }

}
