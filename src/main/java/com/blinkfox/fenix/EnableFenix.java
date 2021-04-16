package com.blinkfox.fenix;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.blinkfox.fenix.jpa.FenixJpaRepositoryFactoryBean;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.persistence.EntityManagerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.repository.config.BootstrapMode;
import org.springframework.data.repository.config.DefaultRepositoryBaseClass;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * 用于开启（激活）Fenix 的注解.
 *
 * @author YangWenpeng on 2019-12-17
 * @author blinkfox on 2020-01-13
 * @since v2.2.0
 */
@Documented
@Target(TYPE)
@Retention(RUNTIME)
@Import(FenixJpaRepositoriesRegistrar.class)
public @interface EnableFenix {

    /**
     * Returns the {@link FactoryBean} class to be used for each repository instance. Defaults to.
     * {@link FenixJpaRepositoryFactoryBean}.
     *
     * @return FenixJpaRepositoryFactoryBean.
     */
    Class<?> repositoryFactoryBeanClass() default FenixJpaRepositoryFactoryBean.class;

    /**
     * Alias for the {@link #basePackages()} attribute. Allows for more concise annotation declarations e.g.:
     * {@code @EnableJpaRepositories("org.my.pkg")}
     * instead of {@code @EnableJpaRepositories(basePackages="org.my.pkg")}.
     *
     * @return value
     */
    String[] value() default {};

    /**
     * Base packages to scan for annotated components.
     * {@link #value()} is an alias for (and mutually exclusive with) this
     * attribute. Use {@link #basePackageClasses()} for a type-safe alternative to String-based package names.
     *
     * @return basePackages
     */
    String[] basePackages() default {};

    /**
     * Type-safe alternative to {@link #basePackages()} for specifying the packages to scan for annotated components.
     * The package of each class specified will be scanned.
     * Consider creating a special no-op marker class or interface in
     * each package that serves no purpose other than being referenced by this attribute.
     *
     * @return basePackageClasses
     */
    Class<?>[] basePackageClasses() default {};

    /**
     * Specifies which types are eligible for component scanning. Further narrows the set of candidate components from
     * everything in
     * {@link #basePackages()} to everything in the base packages that matches the given filter or filters.
     *
     * @return {@link org.springframework.context.annotation.ComponentScan.Filter}
     */
    ComponentScan.Filter[] includeFilters() default {};

    /**
     * Specifies which types are not eligible for component scanning.
     *
     * @return {@link org.springframework.context.annotation.ComponentScan.Filter}
     */
    ComponentScan.Filter[] excludeFilters() default {};

    /**
     * Returns the postfix to be used when looking up custom repository implementations. Defaults to {@literal Impl}. So
     * for a repository named {@code PersonRepository} the corresponding implementation class will be looked up scanning
     * for {@code PersonRepositoryImpl}.
     *
     * @return repositoryImplementationPostfix
     */
    String repositoryImplementationPostfix() default "Impl";

    /**
     * Configures the location of where to find the Spring Data named queries properties file. Will default to
     * {@code META-INF/jpa-named-queries.properties}.
     *
     * @return namedQueriesLocation
     */
    String namedQueriesLocation() default "";

    /**
     * Returns the key of the {@link QueryLookupStrategy} to be used for lookup queries for query methods. Defaults to
     * {@link QueryLookupStrategy.Key#CREATE_IF_NOT_FOUND}.
     *
     * @return queryLookupStrategy
     */
    QueryLookupStrategy.Key queryLookupStrategy() default QueryLookupStrategy.Key.CREATE_IF_NOT_FOUND;

    /**
     * Configure the repository base class to be used to create repository proxies for this particular configuration.
     *
     * @return repositoryBaseClass
     */
    Class<?> repositoryBaseClass() default DefaultRepositoryBaseClass.class;

    // JPA specific configuration

    /**
     * Configures the name of the {@link EntityManagerFactory} bean definition to be used to create repositories
     * discovered through this annotation. Defaults to {@code entityManagerFactory}.
     *
     * @return entityManagerFactoryRef
     */
    String entityManagerFactoryRef() default "entityManagerFactory";

    /**
     * Configures the name of the {@link PlatformTransactionManager} bean definition to be used to create repositories
     * discovered through this annotation. Defaults to {@code transactionManager}.
     *
     * @return transactionManagerRef
     */
    String transactionManagerRef() default "transactionManager";

    /**
     * Configures whether nested repository-interfaces (e.g. defined as inner classes) should be discovered by the
     * repositories infrastructure.
     *
     * @return isConsiderNestedRepositories
     */
    boolean considerNestedRepositories() default false;

    /**
     * Configures whether to enable default transactions for Spring Data JPA repositories.
     * Defaults to {@literal true}. If
     * disabled, repositories must be used behind a facade that's configuring transactions
     * (e.g. using Spring's annotation
     * driven transaction facilities) or repository methods have to be used to demarcate transactions.
     *
     * @return whether to enable default transactions, defaults to {@literal true}.
     */
    boolean enableDefaultTransactions() default true;

    /**
     * Configures when the repositories are initialized in the bootstrap lifecycle. {@link BootstrapMode#DEFAULT}
     * (default) means eager initialization except all repository interfaces annotated with {@link Lazy},
     * {@link BootstrapMode#LAZY} means lazy by default including injection of lazy-initialization proxies into client
     * beans so that those can be instantiated but will only trigger the initialization upon first repository usage
     * (i.e a method invocation on it).
     * This means repositories can still be uninitialized when the application context has
     * completed its bootstrap. {@link BootstrapMode#DEFERRED} is fundamentally the same as {@link BootstrapMode#LAZY},
     * but triggers repository initialization when the application context finishes its bootstrap.
     *
     * @return bootstrapMode
     */
    BootstrapMode bootstrapMode() default BootstrapMode.DEFAULT;

    /**
     * Configures what character is used to escape the wildcards {@literal _} and {@literal %} in derived queries with
     * {@literal contains}, {@literal startsWith} or {@literal endsWith} clauses.
     *
     * @return a single character used for escaping.
     */
    char escapeCharacter() default '\\';

}
