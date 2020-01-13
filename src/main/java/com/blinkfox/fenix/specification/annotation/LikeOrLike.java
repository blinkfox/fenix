package com.blinkfox.fenix.specification.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * NotIn
 * @description LikeOrLike条件 ( a like 'xxx' or b like 'xxx'),相应字段的值须要是List。
 * @author YangWenpeng
 * @date 2019年3月27日 下午4:25:57
 * @version v1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface LikeOrLike {

    /**
     * 
     * Equels
     * @description 该注解中该方法无用
     * @return
     * @author YangWenpeng
     * @date 2019年3月27日 下午4:27:02
     * @deprecated LikeOrLike条件中通过{@link fileds}获取列名，value方法将会无效。
     * @version v1.0.0
     */
    @Deprecated
    String value() default "";
    
    /**
     * 
     * LikeOrLike
     * @description 字段名
     * @return
     * @author YangWenpeng
     * @date 2019年5月31日 上午11:31:48
     * @version v1.0.0
     */
    String[] fileds();
}
