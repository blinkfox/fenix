package com.blinkfox.fenix.specification.handler;

import com.blinkfox.fenix.exception.BuildSpecificationException;
import com.blinkfox.fenix.helper.StringHelper;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

import org.springframework.beans.BeanUtils;

/**
 * 用来动态构造 JPA 中 {@code Specification} 的抽象类.
 *
 * @author YangWenpeng on 2019-12-17
 * @author blinkfox on 2020-01-14
 * @since v2.2.0
 */
public abstract class AbstractSpecificationHandler {

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
     * 获取对应注解类的 {@code Class} 类型的值.
     *
     * @param <T> 范型 T
     * @return 范型 T 的 {@code Class} 类型的值
     */
    public abstract <T> Class<T> getAnnotation();

    /**
     * 构造 {@link Predicate} 实例的方法.
     *
     *
     * @param criteriaBuilder {@link CriteriaBuilder} 实例
     * @param from {@link From} 实例
     * @param name 实体类的属性名
     * @param value 对应属性的值
     * @param annotation 前字段使用的注解
     * @param <Z> 范型 Z
     * @param <X> 范型 X
     * @return {@link Predicate} 实例
     */
    protected abstract <Z, X> Predicate buildPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String name, Object value, Object annotation);

}
