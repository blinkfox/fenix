package com.blinkfox.fenix.specification;

import com.blinkfox.fenix.helper.CollectionHelper;
import com.blinkfox.fenix.helper.FieldHelper;
import com.blinkfox.fenix.specification.listener.AbstractSpecificationHandler;
import com.blinkfox.fenix.specification.listener.SpecificationHandler;
import com.blinkfox.fenix.specification.predicate.FenixBooleanStaticPredicate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Predicate.BooleanOperator;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.springframework.data.jpa.domain.Specification;

/**
 * {@code Specification} 的提供者类.
 *
 * @author YangWenpeng on 2019-12-17
 * @author blinkfox on 2020-01-14
 * @since v2.2.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SpecificationSupplier {

    /**
     * 用来缓存注解的 {@code class} 实例和 {@link SpecificationHandler} 实例的 Map.
     */
    private static final Map<Class<AbstractSpecificationHandler>, SpecificationHandler> listenerMap = new HashMap<>();

    /**
     * 根据参数对象构建 {@link Specification} 实例.
     *
     * @param param 参数对象
     * @param <T>   范型 T
     * @return {@link Specification} 实例
     */
    public static <T> Specification<T> buildSpecification(Object param) {
        return (root, query, criteriaBuilder) -> {
            Map<BooleanOperator, List<Predicate>> predicatesMap = paramToPredicate(root, criteriaBuilder, param)
                    .stream()
                    .collect(Collectors.groupingBy(Predicate::getOperator));

            // 将 AND 条件和 OR 条件进行合并.
            List<Predicate> andPredicates = predicatesMap.get(BooleanOperator.AND);
            List<Predicate> orPredicates = predicatesMap.get(BooleanOperator.OR);
            if (CollectionHelper.isNotEmpty(andPredicates) && CollectionHelper.isNotEmpty(orPredicates)) {
                return criteriaBuilder.or(criteriaBuilder.and(andPredicates.toArray(new Predicate[0])),
                        criteriaBuilder.or(orPredicates.toArray(new Predicate[0])));
            } else if (CollectionHelper.isNotEmpty(orPredicates)) {
                return criteriaBuilder.or(orPredicates.toArray(new Predicate[0]));
            } else if (CollectionHelper.isNotEmpty(andPredicates)) {
                return criteriaBuilder.and(andPredicates.toArray(new Predicate[0]));
            } else {
                return null;
            }
        };
    }

    /**
     * 获取所有的 {@link SpecificationHandler} 的 Map 集合.
     *
     * @return Map
     */
    public static Map<Class<AbstractSpecificationHandler>, SpecificationHandler> getListeners() {
        return listenerMap;
    }

    /**
     * 根据 {@code class} 类添加 {@link SpecificationHandler} 实例到 Map 集合中.
     *
     * @param cls      {@code class} 类
     * @param listener {@link SpecificationHandler} 实例
     */
    public static synchronized void addListener(Class<AbstractSpecificationHandler> cls, SpecificationHandler listener) {
        SpecificationSupplier.listenerMap.put(cls, listener);
    }

    /**
     * 将参数对象转换成 {@link Predicate} 对象集合.
     *
     * @param from            {@link From} 实例
     * @param criteriaBuilder {@link CriteriaBuilder} 实例
     * @param param           对象参数
     * @param <Z>             范型 Z
     * @param <X>             范型 X
     * @return {@link Predicate} 对象集合
     */
    public static <Z, X> List<Predicate> paramToPredicate(
            From<Z, X> from, CriteriaBuilder criteriaBuilder, Object param) {
        Field[] fields = FieldHelper.getAllFields(param.getClass());
        List<Predicate> predicates = new ArrayList<>(fields.length);
        for (Field field : fields) {
            Annotation[] annotations = field.getAnnotations();
            if (annotations == null) {
                continue;
            }

            for (Annotation annotation : annotations) {
                SpecificationHandler specificationListener = listenerMap.get(annotation.annotationType());
                if (specificationListener != null) {
                    Predicate predicate = specificationListener.execute(param, field, criteriaBuilder, from);
                    if (predicate != null && validate(predicate)) {
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
    private static boolean validate(Predicate predicate) {
        return !(predicate instanceof FenixBooleanStaticPredicate) || validateBooleanPredicate(predicate);
    }

    /**
     * 校验布尔类型的 {@link Predicate} 是否有效.
     *
     * @param predicate {@link Predicate} 实例
     * @return 布尔值
     */
    private static boolean validateBooleanPredicate(Predicate predicate) {
        FenixBooleanStaticPredicate booleanStatAssertPredicate = (FenixBooleanStaticPredicate) predicate;
        return !((booleanStatAssertPredicate.getAssertedValue() && predicate.getOperator() == BooleanOperator.AND)
                || (!booleanStatAssertPredicate.getAssertedValue() && predicate.getOperator() == BooleanOperator.OR));
    }

}
