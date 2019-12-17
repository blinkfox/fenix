package com.blinkfox.fenix.specification.annotation;

import java.lang.annotation.*;

/**
 * OrLesser
 * @description OrLesser条件
 * @author YangWenpeng
 * @date 2019年6月6日 下午5:28:26
 * @version v1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface OrLesser {

    /**
     * 
     * Equels
     * @description 字段名
     * @return
     * @author YangWenpeng
     * @date 2019年6月6日 下午5:28:26
     * @version v1.0.0
     */
    String value() default "";
}
