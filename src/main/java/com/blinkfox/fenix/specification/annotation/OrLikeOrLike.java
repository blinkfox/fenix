package com.blinkfox.fenix.specification.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于“或者模糊匹配某些元素”({@code OR ( field1 LIKE 'xxx' OR field2 LIKE 'xxx')})场景的注解.
 *
 * @author YangWenpeng 2019-12-17
 * @author blinkfox on 2020-01-14
 * @since v2.2.0
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OrLikeOrLike {

    /**
     * 模糊匹配的字段名，默认空字符串.
     *
     * @return 值
     * @deprecated LikeOrLike 条件中通过 {@link #fileds} 获取列名，本方法已不建议使用.
     */
    @Deprecated
    String value() default "";

    /**
     * 匹配的多个字段名，默认空字符串.
     *
     * @return 字符串字段的数组
     */
    String[] fileds();

}
