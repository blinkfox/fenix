package com.blinkfox.fenix.specification.listener;

import com.blinkfox.fenix.exception.BuildSpecificationException;
import com.blinkfox.fenix.helper.StringHelper;
import com.blinkfox.fenix.specification.SpecificationSupplier;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

import org.springframework.beans.BeanUtils;

/**
 * 实现了 {@link SpecificationListener} 接口的抽象类.
 *
 * @author YangWenpeng on 2019-12-17
 * @author blinkfox on 2020-01-14
 * @since v2.2.0
 */
public abstract class AbstractListener implements SpecificationListener {

    /**
     * 构造方法.
     */
    public AbstractListener() {
        SpecificationSupplier.addListener(this.getAnnotation(), this);
    }

    @Override
    public <Z, X> Predicate execute(Object param, Field field, CriteriaBuilder criteriaBuilder, From<Z, X> from) {
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
                    ? this.buildPredicate(criteriaBuilder, from, name, value, annotation)
                    : null;
        } else {
            return this.buildPredicate(criteriaBuilder, from, name, value, annotation);
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
