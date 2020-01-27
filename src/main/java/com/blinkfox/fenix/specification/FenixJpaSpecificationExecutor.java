package com.blinkfox.fenix.specification;

import com.blinkfox.fenix.specification.predicate.FenixPredicate;

import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Fenix 中继承自 Spring Data JPA 中 {@link JpaSpecificationExecutor} 接口，
 * 允许基于 JPA {@code criteria} API 执行 {@link Specification} 的接口，用于扩展其中的一些 API.
 *
 * @author blinkfox on 2020-01-28.
 * @since v2.2.0
 */
public interface FenixJpaSpecificationExecutor<T> extends JpaSpecificationExecutor<T> {

    /**
     * Returns a single entity matching the given {@link Specification} or {@link Optional#empty()} if none found.
     *
     * @param fenixPredicate Fenix 中用于动态构造 {@code Predicate} 条件的接口
     * @return never {@literal null}.
     * @throws org.springframework.dao.IncorrectResultSizeDataAccessException if more than one entity found.
     */
    default Optional<T> findOne(FenixPredicate fenixPredicate) {
        return this.findOne(FenixSpecification.of(fenixPredicate));
    }

}
