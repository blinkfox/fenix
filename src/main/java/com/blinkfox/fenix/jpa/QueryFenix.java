package com.blinkfox.fenix.jpa;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.data.annotation.QueryAnnotation;

/**
 * 自定义的核心查询注解 '@QueryFenix'，用于解决 @Query 在复杂或动态 SQL 方面表达能力弱的问题.
 *
 * @author blinkfox on 2019-08-04.
 */
@Documented
@QueryAnnotation
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryFenix {

    /**
     * SQL 模板的字符串值.
     *
     * @return value
     */
    String value() default "";

    /**
     * SQL 所在的 XML 文件的命名空间，如果不设置此值，将默认使用 SQL 的 XML 文件名作为命名空间.
     *
     * @return namespace SQL 所在的 XML 文件的命名空间
     */
    String namespace() default "";

    /**
     * 每个 SQL 所在的 Fenix XML 文件的 id 值.
     *
     * @return zealotId
     */
    String fenixId() default "";

}
