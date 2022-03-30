package com.blinkfox.fenix.ar.spec;

import com.blinkfox.fenix.ar.RepositoryModelContext;
import com.blinkfox.fenix.exception.FenixException;
import com.blinkfox.fenix.helper.StringHelper;
import com.blinkfox.fenix.specification.FenixJpaSpecificationExecutor;
import com.blinkfox.fenix.specification.FenixSpecification;
import com.blinkfox.fenix.specification.predicate.FenixPredicate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Fenix 提供的 ActiveRecord 模式的 Fenix 增强后的 JpaSpecificationExecutor 相关操作的 Model 接口.
 *
 * <p>当实体类实现本接口时，需要该实体类所对应的 Spring Data JPA 中的 Repository 接口继承自 {@link FenixJpaSpecificationExecutor} 接口。
 * 你还可以通过额外调用本类中的 {@link #getSpecExecutor()} 方法来实现更多的 {@link FenixJpaSpecificationExecutor} 中相关的功能.</p>
 *
 * @param <T> 实体类的泛型参数
 * @param <E> 实体类所对应的 Repository 接口，且它继承了 FenixJpaSpecificationExecutor 接口
 * @author blinkfox on 2022-03-30.
 * @since 2.7.0
 */
public interface FenixSpecModel<T, E extends FenixJpaSpecificationExecutor<T>> extends SpecModel<T, E> {

    /**
     * 懒加载获取本实体类所对应的 Repository（{@link E}）对象，该对象需要继承自 {@link FenixJpaSpecificationExecutor} 接口.
     *
     * @return 基本的 CrudRepository 对象
     */
    @SuppressWarnings("unchecked")
    default E getSpecExecutor() {
        return (E) RepositoryModelContext.getRepositoryObject(
                this.getRepositoryBeanName(), this.getClass().getName(), this::validRepository, this::validExecutor);
    }

    /**
     * 校验 Repository 接口是否是 {@link FenixJpaSpecificationExecutor} 类型的接口.
     *
     * @param repository Spring 容器中的 repository 对象
     */
    @Override
    default void validExecutor(Object repository) {
        assertNotNullRepository(repository);
        if (!(repository instanceof FenixJpaSpecificationExecutor)) {
            throw new FenixException(StringHelper.format("【Fenix 异常】获取到的 Spring Data JPA 的 Repository "
                    + "接口【{}】不是真正的 FenixJpaSpecificationExecutor 接口。", repository.getClass().getName()));
        }
    }

    /**
     * 基于 {@link FenixPredicate} 返回与之匹配的单个对象的 {@link Optional} 实例.
     *
     * @param fenixPredicate Fenix 中用于动态构造 {@link javax.persistence.criteria.Predicate} 条件的接口
     * @return 不可能是 {@literal null}.
     * @throws org.springframework.dao.IncorrectResultSizeDataAccessException 如果找到多个实例时抛出此异常.
     */
    default Optional<T> findOne(FenixPredicate fenixPredicate) {
        return this.getSpecExecutor().findOne(FenixSpecification.of(fenixPredicate));
    }

    /**
     * 基于有注解的实体 Bean 返回与之匹配的单个对象的 {@link Optional} 实例.
     *
     * @param beanParam 有 Fenix 注解的实体 Bean
     * @return 不可能是 {@literal null}.
     * @throws org.springframework.dao.IncorrectResultSizeDataAccessException 如果找到多个实例时抛出此异常.
     */
    default Optional<T> findOneOfBean(Object beanParam) {
        return this.getSpecExecutor().findOne(FenixSpecification.ofBean(beanParam));
    }

    /**
     * 基于 {@link FenixPredicate} 返回与之匹配的所有对象实例的集合.
     *
     * @param fenixPredicate Fenix 中用于动态构造 {@link javax.persistence.criteria.Predicate} 条件的接口
     * @return 不可能是 {@literal null}.
     */
    default List<T> findAll(FenixPredicate fenixPredicate) {
        return this.getSpecExecutor().findAll(FenixSpecification.of(fenixPredicate));
    }

    /**
     * 基于 {@link FenixPredicate} 和 {@link Pageable} 分页信息返回与之匹配的分页对象实例.
     *
     * @param fenixPredicate Fenix 中用于动态构造 {@link javax.persistence.criteria.Predicate} 条件的接口
     * @param pageable 分页信息，不能为 {@literal null}.
     * @return 分页结果，不可能是 {@literal null}.
     */
    default Page<T> findAll(FenixPredicate fenixPredicate, Pageable pageable) {
        return this.getSpecExecutor().findAll(FenixSpecification.of(fenixPredicate), pageable);
    }

    /**
     * 基于 {@link FenixPredicate} 和 {@link Sort} 排序信息返回所有与之匹配的对象实例.
     *
     * @param fenixPredicate Fenix 中用于动态构造 {@link javax.persistence.criteria.Predicate} 条件的接口
     * @param sort 排序信息，不能为 {@literal null}.
     * @return 排序结果，不可能是 {@literal null}.
     */
    default List<T> findAll(FenixPredicate fenixPredicate, Sort sort) {
        return this.getSpecExecutor().findAll(FenixSpecification.of(fenixPredicate), sort);
    }

    /**
     * 基于有注解的实体 Bean 返回与之匹配的所有对象实例的集合.
     *
     * @param beanParam 有 Fenix 注解的实体 Bean
     * @return 不可能是 {@literal null}.
     */
    default List<T> findAllOfBean(Object beanParam) {
        return this.getSpecExecutor().findAll(FenixSpecification.ofBean(beanParam));
    }

    /**
     * 基于有注解的实体 Bean 和 {@link Pageable} 分页信息返回与之匹配的分页对象实例.
     *
     * @param beanParam 有 Fenix 注解的实体 Bean
     * @param pageable 分页信息，不能为 {@literal null}.
     * @return 分页结果，不可能是 {@literal null}.
     */
    default Page<T> findAllOfBean(Object beanParam, Pageable pageable) {
        return this.getSpecExecutor().findAll(FenixSpecification.ofBean(beanParam), pageable);
    }

    /**
     * 基于有注解的实体 Bean 和 {@link Sort} 排序信息返回所有与之匹配的对象实例.
     *
     * @param beanParam 有 Fenix 注解的实体 Bean
     * @param sort 排序信息，不能为 {@literal null}.
     * @return 排序结果，不可能是 {@literal null}.
     */
    default List<T> findAllOfBean(Object beanParam, Sort sort) {
        return this.getSpecExecutor().findAll(FenixSpecification.ofBean(beanParam), sort);
    }

    /**
     * 基于 {@link FenixPredicate} 返回与之匹配的所有对象实例的总数量.
     *
     * @param fenixPredicate Fenix 中用于动态构造 {@link javax.persistence.criteria.Predicate} 条件的接口
     * @return 实例数量.
     */
    default long count(FenixPredicate fenixPredicate) {
        return this.getSpecExecutor().count(FenixSpecification.of(fenixPredicate));
    }

    /**
     * 基于有注解的实体 Bean 返回与之匹配的所有对象实例的总数量.
     *
     * @param beanParam 有 Fenix 注解的实体 Bean
     * @return 实例数量.
     */
    default long countOfBean(Object beanParam) {
        return this.getSpecExecutor().count(FenixSpecification.ofBean(beanParam));
    }

}
