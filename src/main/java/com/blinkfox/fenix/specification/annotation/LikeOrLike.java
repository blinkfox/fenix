package com.blinkfox.fenix.specification.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于“模糊匹配某些符合条件的元素”({@code LIKE})场景的注解.
 *
 * <p>示例：{@code field1 like 'xxx' or field2 like 'xxx'}.</p>
 *
 * @author YangWenpeng 2019-12-17
 * @author blinkfox on 2020-01-13
 * @since v2.2.0
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LikeOrLike {

    /**
     * 默认为空即可，不需要额外设置.
     *
     * @return 字符串字段的数组
     */
    String value() default "";

    /**
     * 匹配的多个字段名.
     *
     * @return 字段数组
     */
    String[] fields();

}
