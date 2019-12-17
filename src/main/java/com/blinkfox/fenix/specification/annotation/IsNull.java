package com.blinkfox.fenix.specification.annotation;

import java.lang.annotation.*;

/**
 * IsNull
 * @description 为空条件
 * @author YangWenpeng
 * @date 2019年3月27日 下午4:25:57
 * @version v1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface IsNull{

    /**
     * 
     * Equels
     * @description 字段名
     * @return
     * @author YangWenpeng
     * @date 2019年3月27日 下午4:27:02
     * @version v1.0.0
     */
    String value() default "";
}
