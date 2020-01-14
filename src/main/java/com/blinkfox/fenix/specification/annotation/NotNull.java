package com.blinkfox.fenix.specification.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于“是 NULL”({@code IS NULL})条件场景的注解.
 *
 * @author YangWenpeng on 2019-12-17
 * @author blinkfox on 2020-01-13
 * @since v2.2.0
 */
// TODO 名字应该是 IsNotNull 更合适点儿.
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NotNull {

    /**
     * 判断的字段名，默认空字符串.
     *
     * @return 字符串值
     */
    String value() default "";

}
