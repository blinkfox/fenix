package com.blinkfox.fenix.specification;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Predicate.BooleanOperator;

import org.hibernate.query.criteria.internal.predicate.BooleanStaticAssertionPredicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import com.blinkfox.fenix.specification.listener.SpecificationListener;
import com.blinkfox.fenix.specification.predicate.FenixBooleanStaticPredicate;
import com.blinkfox.fenix.specification.util.FieldUtils;

/**
 * SpecificationSupplier
 * 
 * @description 动态生成Specification
 * @author YangWenpeng
 * @date 2019年3月26日 下午5:15:10
 * @version v1.0.0
 */
public final class SpecificationSupplier {

    private static Map<Class<?>, SpecificationListener> listenerMap = new HashMap<>();

    private SpecificationSupplier() {

    }

    public static <T> Specification<T> buildSpecification(Object param) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = paramToPredicate(root, criteriaBuilder, param);
            Map<BooleanOperator, List<Predicate>> predicatesMap
                = predicates.stream().collect(Collectors.groupingBy(Predicate::getOperator));
            List<Predicate> andPredicates = predicatesMap.get(BooleanOperator.AND);
            andPredicates = andPredicates == null ? Collections.emptyList() : andPredicates;
            List<Predicate> orPredicates = predicatesMap.get(BooleanOperator.OR);
            orPredicates = orPredicates == null ? Collections.emptyList() : orPredicates;
            return meragePredicate(criteriaBuilder, andPredicates, orPredicates);
        };
    }

    /**
     * SpecificationSupplier
     * 
     * @description 将and条件和or条件进行合并
     * @param criteriaBuilder
     * @param andPredicates
     * @param orPredicates
     * @return
     * @author YangWenpeng
     * @date 2019年10月31日 下午1:58:36
     * @version v1.0.0
     */
    private static Predicate meragePredicate(CriteriaBuilder criteriaBuilder, List<Predicate> andPredicates,
        List<Predicate> orPredicates) {
        if (!CollectionUtils.isEmpty(andPredicates) && !CollectionUtils.isEmpty(orPredicates)) {
            // 若有and和or条件
            return criteriaBuilder.or(criteriaBuilder.and(andPredicates.toArray(new Predicate[andPredicates.size()])),
                criteriaBuilder.or(orPredicates.toArray(new Predicate[orPredicates.size()])));
        } else if (!CollectionUtils.isEmpty(orPredicates)) {
            // 只有or条件
            return criteriaBuilder.or(orPredicates.toArray(new Predicate[orPredicates.size()]));
        } else if (!CollectionUtils.isEmpty(andPredicates)) {
            // 只有and条件
            return criteriaBuilder.and(andPredicates.toArray(new Predicate[andPredicates.size()]));
        }
        // and和or均没有
        return null;
    }

    /**
     * @return the listeners
     */
    public static Map<Class<?>, SpecificationListener> getListeners() {
        return listenerMap;
    }

    public static synchronized void addListener(Class<?> cls, SpecificationListener listener) {
        SpecificationSupplier.listenerMap.put(cls, listener);
    }

    public static <Z, X> List<Predicate> paramToPredicate(From<Z, X> from, CriteriaBuilder criteriaBuilder,
        Object param) {
        List<Predicate> predicates = new ArrayList<>();
        Field[] fields = FieldUtils.getAllFields(param.getClass());
        for (Field field : fields) {
            Annotation[] annotations = field.getAnnotations();
            if (annotations == null) {
                continue;
            }
            for (int i = 0; i < annotations.length; i++) {
                Annotation annotation = annotations[i];
                SpecificationListener specificationListener = listenerMap.get(annotation.annotationType());
                if (null == specificationListener) {
                    continue;
                }
                Predicate predicate = specificationListener.execute(param, field, criteriaBuilder, from);
                if (null != predicate && validate(predicate)) {
                    predicates.add(predicate);
                }
            }
        }
        return predicates;
    }

    /**
     * SpecificationSupplier
     * 
     * @description 验证{@link Predicate}，有的predicate可以不用解析<br>
     *              防止部分1=1和1=0的出现。
     * @param predicate
     * @return
     * @author YangWenpeng
     * @date 2019年12月17日 下午6:55:42
     * @version v1.0.0
     */
    private static boolean validate(Predicate predicate) {
        if (predicate instanceof FenixBooleanStaticPredicate) {
            return validateBooleanPredicate(predicate);
        }
        return true;
    }

    /**
     * 
     * SpecificationSupplier
     * 
     * @description 验证{@link BooleanStaticAssertionPredicate}，有的predicate可以不用解析
     * @param predicate
     * @return
     * @author YangWenpeng
     * @date 2019年12月17日 下午7:09:15
     * @version v1.0.0
     */
    private static boolean validateBooleanPredicate(Predicate predicate) {
        FenixBooleanStaticPredicate booleanStaticAssertionPredicate = (FenixBooleanStaticPredicate)predicate;
        return !((booleanStaticAssertionPredicate.getAssertedValue() && predicate.getOperator() == BooleanOperator.AND)
            || (!booleanStaticAssertionPredicate.getAssertedValue() && predicate.getOperator() == BooleanOperator.OR));
    }

}
