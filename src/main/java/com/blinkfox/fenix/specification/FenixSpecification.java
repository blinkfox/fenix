package com.blinkfox.fenix.specification;

import com.blinkfox.fenix.config.FenixConfig;
import com.blinkfox.fenix.helper.CollectionHelper;
import com.blinkfox.fenix.helper.FieldHelper;
import com.blinkfox.fenix.specification.handler.AbstractSpecificationHandler;
import com.blinkfox.fenix.specification.predicate.FenixBooleanStaticPredicate;
import com.blinkfox.fenix.specification.predicate.FenixPredicate;
import com.blinkfox.fenix.specification.predicate.PredicateBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.springframework.data.jpa.domain.Specification;

/**
 * Fenix 中构造 {@link Specification} 的核心 API 类.
 *
 * @author blinkfox on 2020-01-15.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FenixSpecification {

    private static final Map<Class<?>, AbstractSpecificationHandler> specificationHandlerMap =
            FenixConfig.getSpecificationHandlerMap();

    /**
     * 根据查询的实体 Bean 参数中的 Fenix 相关的注解来构造 {@link Specification} 实例.
     *
     * @param beanParam 待查询的实体 Bean
     * @param <T> 范型 T
     * @return {@link Specification} 实例
     */
    public static <T> Specification<T> ofBean(Object beanParam) {
        return (root, query, builder) ->
                mergePredicates(builder, beanParamToPredicate(root, builder, beanParam)
                        .stream()
                        .collect(Collectors.groupingBy(Predicate::getOperator)));
    }

    /**
     * 根据动态条件列表中构造 {@link Specification} 实例.
     *
     * @param predicates 动态条件列表
     * @param <T> 范型 T
     * @return {@link Specification} 实例
     */
    public static <T> Specification<T> of(List<Predicate> predicates) {
        return (root, query, builder) ->
                mergePredicates(builder, predicates.stream().collect(Collectors.groupingBy(Predicate::getOperator)));
    }

    /**
     * 根据查询的条件列表中构造 {@link Specification} 实例.
     *
     * @param fenixPredicate 动态 {@link Predicate} 的构造器接口
     * @param <T> 范型 T
     * @return {@link Specification} 实例
     */
    public static <T> Specification<T> of(FenixPredicate fenixPredicate) {
        return (root, query, builder) -> mergePredicates(builder,
                fenixPredicate.toPredicate(new PredicateBuilder(root, query, builder))
                        .stream()
                        .collect(Collectors.groupingBy(Predicate::getOperator)));
    }

    /**
     * 合并 AND 或者 OR 中的动态条件.
     *
     * @param builder {@link CriteriaBuilder} 实例
     * @param predicatesMap 动态条件的 Map
     * @return 条件.
     */
    private static Predicate mergePredicates(CriteriaBuilder builder,
            Map<Predicate.BooleanOperator, List<Predicate>> predicatesMap) {
        List<Predicate> andPredicates = predicatesMap.get(Predicate.BooleanOperator.AND);
        List<Predicate> orPredicates = predicatesMap.get(Predicate.BooleanOperator.OR);
        if (CollectionHelper.isNotEmpty(andPredicates) && CollectionHelper.isNotEmpty(orPredicates)) {
            return builder.or(builder.and(andPredicates.toArray(new Predicate[0])),
                    builder.or(orPredicates.toArray(new Predicate[0])));
        } else if (CollectionHelper.isNotEmpty(orPredicates)) {
            return builder.or(orPredicates.toArray(new Predicate[0]));
        } else if (CollectionHelper.isNotEmpty(andPredicates)) {
            return builder.and(andPredicates.toArray(new Predicate[0]));
        } else {
            return null;
        }
    }

    /**
     * 将参数对象转换成 {@link Predicate} 对象集合.
     *
     * @param from            {@link From} 实例
     * @param criteriaBuilder {@link CriteriaBuilder} 实例
     * @param beanParam           对象参数
     * @param <Z>             范型 Z
     * @param <X>             范型 X
     * @return {@link Predicate} 对象集合
     */
    public static <Z, X> List<Predicate> beanParamToPredicate(
            From<Z, X> from, CriteriaBuilder criteriaBuilder, Object beanParam) {
        Field[] fields = FieldHelper.getAllFields(beanParam.getClass());
        List<Predicate> predicates = new ArrayList<>(fields.length);
        for (Field field : fields) {
            Annotation[] annotations = field.getAnnotations();
            if (annotations == null) {
                continue;
            }

            for (Annotation annotation : annotations) {
                AbstractSpecificationHandler handler = specificationHandlerMap.get(annotation.annotationType());
                if (handler != null) {
                    Predicate predicate = handler.execute(beanParam, field, criteriaBuilder, from);
                    if (predicate != null && isValid(predicate)) {
                        predicates.add(predicate);
                    }
                }
            }
        }
        return predicates;
    }

    /**
     * 校验 {@link Predicate} 是否有效，有的 {@code predicate} 可以不用解析.
     *
     * @param predicate {@link Predicate} 实例
     * @return 布尔值
     */
    private static boolean isValid(Predicate predicate) {
        return !(predicate instanceof FenixBooleanStaticPredicate) || validateBooleanPredicate(predicate);
    }

    /**
     * 校验布尔类型的 {@link Predicate} 是否有效.
     *
     * @param predicate {@link Predicate} 实例
     * @return 布尔值
     */
    private static boolean validateBooleanPredicate(Predicate predicate) {
        FenixBooleanStaticPredicate boolPredicate = (FenixBooleanStaticPredicate) predicate;
        return !((boolPredicate.getAssertedValue() && predicate.getOperator() == Predicate.BooleanOperator.AND)
                || (!boolPredicate.getAssertedValue() && predicate.getOperator() == Predicate.BooleanOperator.OR));
    }

}
