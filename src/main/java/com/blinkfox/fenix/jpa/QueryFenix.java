package com.blinkfox.fenix.jpa;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.data.annotation.QueryAnnotation;

/**
 * 自定义的核心查询注解 {@link QueryFenix}，
 * 用于解决 {@link org.springframework.data.jpa.repository.Query} 注解在复杂或动态 SQL 方面表达能力弱的问题.
 *
 * <p>{@link QueryFenix} 注解可以用来关联外部的 SQL XML 文件，实现 SQL 与 Java 隔离，
 * 并通过模板或标签语法来处理生成复杂或动态的 JPQL 或 SQL 语句.</p>
 *
 * @author blinkfox on 2019-08-04.
 * @see FenixQueryLookupStrategy
 * @since v1.0.0
 */
@Documented
@QueryAnnotation
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryFenix {

    /**
     * 全 FenixId 标记，该值由 XML 文件的命名空间、'.'号和 Fenix XML 标签中的 id 组成.
     *
     * <p>即：'{namespace}' + '.' + '{fenixId}'，如：'blogRepostiry.getMyBlogs'</p>
     *
     * @return value
     */
    String value() default "";

    /**
     * 查询记录数 SQL 的全 FenixId 标记，该值由 XML 文件的命名空间、'.'号和 Fenix XML 标签中的 id 组成.
     *
     * <p>即：'{namespace}' + '.' + '{fenixId}'，如：'blogRepostiry.getMyBlogs'</p>
     *
     * @return countQuery
     */
    String countQuery() default "";

    /**
     * 是否原生 SQL 查询，默认为 false.
     *
     * @return 布尔值
     */
    boolean nativeQuery() default false;

    /**
     * 使用 Java 来做 SQL 拼接的提供类的 class.
     *
     * @return Class
     */
    Class<?> provider() default Void.class;

    /**
     * 使用 Java 来做 SQL 拼接的提供类的方法名.
     *
     * @return 方法名
     */
    String method() default "";

    /**
     * 使用 Java 来 countQuery 记录总数 SQL 拼接的提供类方法名.
     *
     * @return 方法名
     */
    String countMethod() default "";

    /**
     * 是否启用 distinct 检测.
     *
     * @return 布尔值
     */
    boolean enableDistinct() default false;

    /**
     * 自定义查询结果的 Class 类型.
     *
     * <p>该属性和 Fenix XML 文件中的 {@code resultType} 同义，后续推荐使用本属性来设置结果类型的 class，
     * 这样能更好的利用 Java 类的静态编译检查和代码重构后发现一些类路径修改所造成的隐患问题.</p>
     *
     * @since 2.7.0
     */
    Class<?> resultType() default Void.class;

    /**
     * 自定义查询结果的转换器的 Class 类型.
     *
     * <p>为了保持向前兼容，默认的转换器仍然使用的是之前版本使用的 {@link FenixResultTransformer} 类的 Class.</p>
     *
     * @since 2.7.0
     */
    Class<? extends AbstractResultTransformer> resultTransformer() default FenixResultTransformer.class;

}
