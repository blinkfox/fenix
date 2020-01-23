package com.blinkfox.fenix.specification.handler;

import com.blinkfox.fenix.exception.BuildSpecificationException;
import com.blinkfox.fenix.helper.StringHelper;
import com.blinkfox.fenix.specification.predicate.FenixBooleanStaticPredicate;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.criteria.internal.CriteriaBuilderImpl;
import org.springframework.beans.BeanUtils;

/**
 * 用来动态构造 JPA 中 {@link Predicate} 的抽象类.
 *
 * @author YangWenpeng on 2019-12-17
 * @author blinkfox on 2020-01-14
 * @since v2.2.0
 */
@Slf4j
public abstract class AbstractPredicateHandler {

    /**
     * 获取对应注解类的 {@code Class} 类型的值.
     *
     * @return {@code Class} 类型的值
     */
    public abstract Class<? extends Annotation> getAnnotation();

    /**
     * 构造 {@code AND} 关系的 {@link Predicate} 实例的方法.
     *
     * @param criteriaBuilder {@link CriteriaBuilder} 实例
     * @param from {@link From} 实例
     * @param fieldName 实体类的属性名
     * @param value 对应属性的值
     * @param annotation 前字段使用的注解
     * @param <Z> 范型 Z
     * @param <X> 范型 X
     * @return {@link Predicate} 实例
     */
    public abstract <Z, X> Predicate buildPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value, Object annotation);

    /**
     * 构造 {@code AND} 关系的 {@link Predicate} 实例的方法.
     *
     * @param criteriaBuilder {@link CriteriaBuilder} 实例
     * @param from {@link From} 实例
     * @param fieldName 属性字段名称
     * @param value 属性条件对应的值
     * @param <Z> 范型 Z
     * @param <X> 范型 X
     * @return {@link Predicate} 实例
     */
    public abstract <Z, X> Predicate buildPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value);

    /**
     * 执行构建 {@link Predicate} 的方法.
     *
     * @param param 对象参数
     * @param field 对应的字段
     * @param criteriaBuilder {@link CriteriaBuilder} 实例
     * @param root {@link From} 实例
     * @param <Z> 范型 Z
     * @param <X> 范型 X
     * @return 一个 {@link Predicate} 实例
     */
    public <Z, X> Predicate execute(Object param, Field field, CriteriaBuilder criteriaBuilder, From<Z, X> root) {
        Annotation annotation = field.getAnnotation(this.getAnnotation());
        if (annotation == null) {
            return null;
        }

        PropertyDescriptor descriptor = BeanUtils.getPropertyDescriptor(param.getClass(), field.getName());
        if (descriptor == null) {
            return null;
        }

        String name;
        Object value;
        try {
            name = (String) this.getAnnotation().getMethod("value").invoke(annotation);
            name = StringHelper.isBlank(name) ? field.getName() : name;
            value = descriptor.getReadMethod().invoke(param);
        } catch (ReflectiveOperationException e) {
            throw new BuildSpecificationException("【Fenix 异常】构建【" + this.getAnnotation().getName()
                    + "】注解的条件时，反射调用对应的属性值异常", e);
        }

        if (value == null) {
            return null;
        }

        if (field.getType() == String.class) {
            return StringHelper.isNotBlank(value.toString())
                    ? this.buildPredicate(criteriaBuilder, root, name, value, annotation)
                    : null;
        } else {
            return this.buildPredicate(criteriaBuilder, root, name, value, annotation);
        }
    }

    /**
     * 构造相等条件 {@code Equals} 的 {@link Predicate} 条件.
     *
     * @param criteriaBuilder {@link CriteriaBuilder} 实例
     * @param from {@link From} 实例
     * @param fieldName 实体类的属性名
     * @param value 对应属性的值
     * @param <Z> 泛型 Z
     * @param <X> 泛型 X
     * @return {@link Predicate} 实例
     */
    protected <Z, X> Predicate buildEqualsPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value) {
        return criteriaBuilder.equal(from.get(fieldName), value);
    }

    /**
     * 构造不相等条件 {@code Not Equals} 的 {@link Predicate} 条件.
     *
     * @param criteriaBuilder {@link CriteriaBuilder} 实例
     * @param from {@link From} 实例
     * @param fieldName 实体类的属性名
     * @param value 对应属性的值
     * @param <Z> 泛型 Z
     * @param <X> 泛型 X
     * @return {@link Predicate} 实例
     */
    protected <Z, X> Predicate buildNotEqualsPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value) {
        return criteriaBuilder.notEqual(from.get(fieldName), value);
    }

    /**
     * 构造大于查询的 {@link Predicate} 实例的方法.
     *
     * @param criteriaBuilder {@link CriteriaBuilder} 实例
     * @param from {@link From} 实例
     * @param fieldName 属性字段名称
     * @param value 属性条件对应的值
     * @param <Z> 范型 Z
     * @param <X> 范型 X
     * @return {@link Predicate} 实例
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected <Z, X> Predicate buildGreaterThanPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value) {
        this.isValueComparable(value);
        return criteriaBuilder.greaterThan(from.get(fieldName), (Comparable) value);
    }

    /**
     * 构造大于等于查询的 {@link Predicate} 实例的方法.
     *
     * @param criteriaBuilder {@link CriteriaBuilder} 实例
     * @param from {@link From} 实例
     * @param fieldName 属性字段名称
     * @param value 属性条件对应的值
     * @param <Z> 范型 Z
     * @param <X> 范型 X
     * @return {@link Predicate} 实例
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected <Z, X> Predicate buildGreaterThanEqualPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value) {
        this.isValueComparable(value);
        return criteriaBuilder.greaterThanOrEqualTo(from.get(fieldName), (Comparable) value);
    }

    /**
     * 构造小于查询的 {@link Predicate} 实例的方法.
     *
     * @param criteriaBuilder {@link CriteriaBuilder} 实例
     * @param from {@link From} 实例
     * @param fieldName 属性字段名称
     * @param value 属性条件对应的值
     * @param <Z> 范型 Z
     * @param <X> 范型 X
     * @return {@link Predicate} 实例
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected <Z, X> Predicate buildLessThanPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value) {
        this.isValueComparable(value);
        return criteriaBuilder.lessThan(from.get(fieldName), (Comparable) value);
    }

    /**
     * 构造小于等于查询的 {@link Predicate} 实例的方法.
     *
     * @param criteriaBuilder {@link CriteriaBuilder} 实例
     * @param from {@link From} 实例
     * @param fieldName 属性字段名称
     * @param value 属性条件对应的值
     * @param <Z> 范型 Z
     * @param <X> 范型 X
     * @return {@link Predicate} 实例
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected <Z, X> Predicate buildLessThanEqualPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value) {
        this.isValueComparable(value);
        return criteriaBuilder.lessThanOrEqualTo(from.get(fieldName), (Comparable) value);
    }

    /**
     * 检查值是否实现了 {@link Comparable} 接口.
     *
     * @param value 值
     */
    private void isValueComparable(Object value) {
        if (!(value instanceof Comparable)) {
            throw new BuildSpecificationException("【Fenix 异常】要比较的 value 值【" + value + "】不是可比较类型的，"
                    + "该值的类型必须实现了 java.lang.Comparable 接口才能正常参与比较，才能用于大于、大于等于、小于、小于等于之类的比较场景.");
        }
    }

    /**
     * 构造空条件 {@code IS NULL} 的 {@link Predicate} 条件.
     *
     * @param criteriaBuilder {@link CriteriaBuilder} 实例
     * @param from {@link From} 实例
     * @param value 对应属性的值
     * @param <Z> 泛型 Z
     * @param <X> 泛型 X
     * @return {@link Predicate} 实例
     */
    protected <Z, X> Predicate buildIsNullPredicate(CriteriaBuilder criteriaBuilder, From<Z, X> from, Object value) {
        return criteriaBuilder.isNull(from.get(String.valueOf(value)));
    }

    /**
     * 构造不是空条件 {@code IS NOT NULL} 的 {@link Predicate} 条件.
     *
     * @param criteriaBuilder {@link CriteriaBuilder} 实例
     * @param from {@link From} 实例
     * @param value 对应属性的值
     * @param <Z> 泛型 Z
     * @param <X> 泛型 X
     * @return {@link Predicate} 实例
     */
    protected <Z, X> Predicate buildIsNotNullPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, Object value) {
        return criteriaBuilder.isNotNull(from.get(String.valueOf(value)));
    }

    /**
     * 构造相等条件 {@code Equals} 的 {@link Predicate} 条件.
     *
     * @param criteriaBuilder {@link CriteriaBuilder} 实例
     * @param from {@link From} 实例
     * @param fieldName 实体类的属性名
     * @param value 对应属性的值
     * @param <Z> 泛型 Z
     * @param <X> 泛型 X
     * @return {@link Predicate} 实例
     */
    protected <Z, X> Predicate buildInPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value, boolean allowNull) {
        value = value.getClass().isArray() ? Arrays.asList((Object[]) value) : value;
        Path<Object> path = from.get(fieldName);
        CriteriaBuilder.In<Object> in = criteriaBuilder.in(path);

        if (value instanceof Collection) {
            Collection<?> list = (Collection<?>) value;
            if (list.isEmpty()) {
                return new FenixBooleanStaticPredicate(
                        (CriteriaBuilderImpl) criteriaBuilder, true, Predicate.BooleanOperator.AND);
            } else {
                list.forEach(in::value);
            }
        } else {
            in.value(value);
        }

        return allowNull ? criteriaBuilder.or(in, criteriaBuilder.isNull(path)) : in;
    }

    protected boolean isAllowNull(Object annotation) {
        try {
            return (boolean) this.getAnnotation().getMethod("allowNull").invoke(annotation);
        } catch (IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            log.error("【Fenix 错误提示】获取【@In】相关注解中的【allowNull】的值失败，将默认返回 false 的值.", e);
            return false;
        }
    }

}
