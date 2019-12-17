package com.blinkfox.fenix.specification.annotation;

import java.lang.annotation.*;

/**
 * In
 * @description In条件
 * 条件中传入了大小为0的集合会出现永真和永假条件。建议使用者在调用框架之前对空集合进行过滤。
 * @author YangWenpeng
 * @date 2019年3月27日 下午4:25:57
 * @version v1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface OrIn {

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
    
    /**
     * 
     * In
     * @description 是否允许为null
     * @return
     * @author YangWenpeng
     * @date 2019年5月5日 下午5:19:39
     * @version v1.0.0
     */
    boolean allowNull() default false;
    
    Class<?> type() default String.class;
}
