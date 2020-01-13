package com.blinkfox.fenix.specification.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * LikeIn
 * 
 * @description like查询数组
 * @author lichuan
 * @date 2019年9月16日 下午5:05:34
 * @version TODO
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface LikeIn {

    /**
     * 
     * Likein
     * 
     * @description 字段名
     * @return
     * @author lichuan
     * @date 2019年9月16日 下午5:09:01
     * @version TODO
     */
    String value() default "";

}
