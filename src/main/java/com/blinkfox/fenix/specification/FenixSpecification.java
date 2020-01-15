package com.blinkfox.fenix.specification;

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

    /**
     * 根据查询的实体 Bean 参数中的 Fenix 相关的注解来构造 {@link Specification} 实例.
     *
     * @param beanParam 待查询的实体 Bean
     * @param <T> 范型 T
     * @return {@link Specification} 实例
     */
    public static <T> Specification<T> of(Object beanParam) {
        return SpecificationSupplier.buildSpecification(beanParam);
    }

}
