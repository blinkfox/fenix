package com.blinkfox.fenix.jpa;

import java.lang.reflect.Method;
import javax.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.provider.QueryExtractor;
import org.springframework.data.jpa.repository.query.DefaultJpaQueryMethodFactory;
import org.springframework.data.jpa.repository.query.EscapeCharacter;
import org.springframework.data.jpa.repository.query.JpaQueryLookupStrategy;
import org.springframework.data.jpa.repository.query.JpaQueryMethodFactory;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.core.NamedQueries;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.QueryMethodEvaluationContextProvider;
import org.springframework.data.repository.query.RepositoryQuery;

/**
 * 定义用来处理 {@link QueryFenix} 注解的查找策略类，该策略类实现了 {@link QueryLookupStrategy} 接口.
 * <p>该类主要重写了 {@link #resolveQuery} 这个方法，用来监测 JPA 的接口方法是否标注了 {@link QueryFenix} 注解.</p>
 * <ul>
 *     <li>如果标注了 {@link QueryFenix} 注解，就需要本 Fenix 扩展类库识别处理 XML 文件或 Java 中的 JPQL 语句；</li>
 *     <li>如果没有标注 {@link QueryFenix} 注解，就使用 JPA 默认的 {@link QueryLookupStrategy}.</li>
 * </ul>
 *
 * <p>v2.3.0 版本修改批注：由于 Spring Data JPA v2.3.0 版本新增了 {@link JpaQueryMethodFactory} 接口，
 * 为了保证与之前的 Fenix 版本兼容，做了较大改动.</p>
 *
 * @author blinkfox on 2019-08-04.
 * @since v1.0.0
 */
@Slf4j
public class FenixQueryLookupStrategy implements QueryLookupStrategy {

    /**
     * {@link DefaultJpaQueryMethodFactory} 类的 class 全路径名常量.
     */
    private static final String JPA_METHOD_FACTORY_NAME =
            "org.springframework.data.jpa.repository.query.DefaultJpaQueryMethodFactory";

    /**
     * EntityManager 实体管理器.
     */
    private final EntityManager entityManager;

    /**
     * QueryExtractor 查询提取器.
     */
    private final QueryExtractor extractor;

    /**
     * JPA 默认的 Query 查找策略实例.
     */
    private final QueryLookupStrategy jpaQueryLookupStrategy;

    /**
     * 用于创建 JPA Query 方法的工厂类对象.
     *
     * <p>注意：{@link DefaultJpaQueryMethodFactory} 类是在 JPA v2.3.0 版本才引入的，为了兼容之前的 Fenix 版本，
     * 保证运行时不报错，使用 {@code Object} 类型来引用，使用时强转为 {@link JpaQueryMethodFactory} 接口即可.</p>
     *
     * @since v2.3.1
     */
    private Object queryMethodFactory;

    /**
     * 构造方法.
     *
     * <p>v2.3.1 版本修改批注 (2020-05-16)：在 Spring Data JPA 的 v2.3.0 版本中，修改了 {@link JpaQueryLookupStrategy#create} 这个方法的参数，
     * 新增了 {@link DefaultJpaQueryMethodFactory} 实现及对应的接口，所以，本构造方法也必须给予修改.</p>
     *
     * @param entityManager EntityManager
     * @param provider QueryExtractor 实例
     */
    private FenixQueryLookupStrategy(EntityManager entityManager, QueryLookupStrategy.Key key,
            QueryExtractor extractor, QueryMethodEvaluationContextProvider provider) {
        this.entityManager = entityManager;
        this.extractor = extractor;

        // 如果当前 JVM 中有 JpaQueryMethodFactory 接口的 class，说明是 v2.3.0 及之后的版本，API 有所变化，需要特殊处理.
        if (this.hasDefaultJpaQueryMethodFactoryClass()) {
            this.queryMethodFactory = this.newDefaultJpaQueryMethodFactoryInstance();
            this.jpaQueryLookupStrategy = JpaQueryLookupStrategy.create(entityManager,
                    (JpaQueryMethodFactory) this.queryMethodFactory, key, provider, EscapeCharacter.DEFAULT);
        } else {
            // TODO 为了兼容之前的版本，此处考虑字节码注入的方式来解决编译错误.
            //            this.jpaQueryLookupStrategy = JpaQueryLookupStrategy
            //                    .create(entityManager, key, extractor, provider, EscapeCharacter.DEFAULT);
            this.queryMethodFactory = this.newDefaultJpaQueryMethodFactoryInstance();
            this.jpaQueryLookupStrategy = JpaQueryLookupStrategy.create(entityManager,
                    (JpaQueryMethodFactory) this.queryMethodFactory, key, provider, EscapeCharacter.DEFAULT);
        }
    }

    /**
     * 创建 {@link FenixQueryLookupStrategy} 实例.
     *
     * @param entityManager entityManager
     * @param key key
     * @param extractor extractor
     * @param provider provider
     * @return MyQueryLookupStrategy
     */
    static QueryLookupStrategy create(EntityManager entityManager, QueryLookupStrategy.Key key,
            QueryExtractor extractor, QueryMethodEvaluationContextProvider provider) {
        return new FenixQueryLookupStrategy(entityManager, key, extractor, provider);
    }

    /**
     * 判断执行的方法上是否有 {@link QueryFenix} 注解，如果有的话，就构造 {@link FenixJpaQuery} 实例，否则就是用 JPA 默认的处理方式.
     *
     * @param method will never be {@literal null}.
     * @param metadata will never be {@literal null}.
     * @param factory will never be {@literal null}.
     * @param namedQueries will never be {@literal null}.
     * @return RepositoryQuery
     */
    @Override
    public RepositoryQuery resolveQuery(Method method, RepositoryMetadata metadata, ProjectionFactory factory,
            NamedQueries namedQueries) {
        // 如果没有 QueryFenix 注解，就是用默认的 jpaQueryLookupStrategy.resolveQuery 来构造 RepositoryQuery 实例.
        QueryFenix queryFenixAnno = method.getAnnotation(QueryFenix.class);
        if (queryFenixAnno == null) {
            return this.jpaQueryLookupStrategy.resolveQuery(method, metadata, factory, namedQueries);
        }

        // 如果有 QueryFenix 注解，就构造 FenixJpaQuery 实例，并注入 QueryFenix 和调用方法的 class 到该实例中，便于后续使用.
        FenixJpaQuery fenixJpaQuery;
        if (this.queryMethodFactory == null) {
            // TODO 为了兼容之前的版本，此处考虑字节码注入的方式来解决编译错误.
            //            fenixJpaQuery = new FenixJpaQuery(new JpaQueryMethod(method, metadata, factory, extractor),
            //                    this.entityManager);
            fenixJpaQuery = new FenixJpaQuery(((JpaQueryMethodFactory) this.queryMethodFactory)
                    .build(method, metadata, factory), this.entityManager);
        } else {
            fenixJpaQuery = new FenixJpaQuery(((JpaQueryMethodFactory) this.queryMethodFactory)
                    .build(method, metadata, factory), this.entityManager);
        }

        fenixJpaQuery.setQueryFenix(queryFenixAnno);
        fenixJpaQuery.setQueryClass(method.getDeclaringClass());
        return fenixJpaQuery;
    }

    /**
     * 判断当前的 JPA 版本是否有 {@link DefaultJpaQueryMethodFactory} 类.
     *
     * <p>注意：{@link JpaQueryMethodFactory} 接口和 {@link DefaultJpaQueryMethodFactory} 类是在 JPA v2.3.0 版本才引入的，
     * 为了兼容之前的 Fenix 版本，我们需要判断是否有这个接口的 {@code class}.</p>
     *
     * @return 布尔值
     * @author blinkfox on 2020-05-17.
     * @since v2.3.1
     */
    private boolean hasDefaultJpaQueryMethodFactoryClass() {
        try {
            Thread.currentThread().getContextClassLoader().loadClass(JPA_METHOD_FACTORY_NAME);
            return true;
        } catch (ClassNotFoundException e) {
            log.debug("【Fenix -> 'JPA 版本检测' 提示】检查到你的项目中没有【{}】类，说明你的 Spring Data JPA 版本"
                    + "是 v2.3.0 之前的版本.", JPA_METHOD_FACTORY_NAME);
            return false;
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug("【Fenix -> 'JPA 版本检测' 提示】检查你的项目中是否有【{}】类时出错，将默认你的 Spring Data JPA 版本"
                        + "是 v2.3.0 之前的版本.", JPA_METHOD_FACTORY_NAME, e);
            } else {
                log.error("【Fenix -> 'JPA 版本检测' 错误】检查你的项目中是否有【{}】类时出错，将默认你的 Spring Data JPA 版本"
                                + "是 v2.3.0 之前的版本，检测时的出错原因是：【{}】，若想看更全的错误堆栈日志信息，请开启 debug 日志级别.",
                        JPA_METHOD_FACTORY_NAME, e.getMessage());
            }
            return false;
        }
    }

    /**
     * 创建 {@link DefaultJpaQueryMethodFactory} 类的实例对象.
     *
     * <p>注意：{@link DefaultJpaQueryMethodFactory} 类是在 JPA v2.3.0 版本才引入的，
     * 为了兼容之前的 Fenix 版本，运行时不报错，不使用属性引用的方式创建对象，而是使用方法的形式来创建，并返回 Object 对象.</p>
     *
     * @return {@link DefaultJpaQueryMethodFactory} 类的实例对象
     * @author blinkfox on 2020-05-17.
     * @since v2.3.1
     */
    private Object newDefaultJpaQueryMethodFactoryInstance() {
        return new DefaultJpaQueryMethodFactory(extractor);
    }

}
