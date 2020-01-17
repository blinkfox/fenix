package com.blinkfox.fenix.specification;

import com.blinkfox.fenix.config.FenixConfig;
import com.blinkfox.fenix.helper.CollectionHelper;
import com.blinkfox.fenix.helper.FieldHelper;
import com.blinkfox.fenix.specification.handler.AbstractSpecificationHandler;
import com.blinkfox.fenix.specification.predicate.FenixBooleanStaticPredicate;

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

    private static final Map<Class<?>, AbstractSpecificationHandler> pecificationHandlerMap =
            FenixConfig.getSpecificationHandlerMap();

    /**
     * 根据查询的实体 Bean 参数中的 Fenix 相关的注解来构造 {@link Specification} 实例.
     *
     * @param beanParam 待查询的实体 Bean
     * @param <T> 范型 T
     * @return {@link Specification} 实例
     */
    public static <T> Specification<T> of(Object beanParam) {
        return buildSpecification(beanParam);
    }

    /**
     * 根据参数对象构建 {@link Specification} 实例.
     *
     * @param beanParam 参数对象
     * @param <T>   范型 T
     * @return {@link Specification} 实例
     */
    public static <T> Specification<T> buildSpecification(Object beanParam) {
        return (root, query, builder) -> {
            Map<Predicate.BooleanOperator, List<Predicate>> predicatesMap = paramToPredicate(root, builder, beanParam)
                    .stream()
                    .collect(Collectors.groupingBy(Predicate::getOperator));

            // 将 AND 条件和 OR 条件进行合并.
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
        };
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
    public static <Z, X> List<Predicate> paramToPredicate(
            From<Z, X> from, CriteriaBuilder criteriaBuilder, Object beanParam) {
        Field[] fields = FieldHelper.getAllFields(beanParam.getClass());
        List<Predicate> predicates = new ArrayList<>(fields.length);
        for (Field field : fields) {
            Annotation[] annotations = field.getAnnotations();
            if (annotations == null) {
                continue;
            }


            for (Annotation annotation : annotations) {
                AbstractSpecificationHandler handler = pecificationHandlerMap.get(annotation.annotationType());
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
