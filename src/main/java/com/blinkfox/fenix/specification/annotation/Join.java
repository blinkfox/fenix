package com.blinkfox.fenix.specification.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.persistence.criteria.JoinType;

@Documented
@Retention(RUNTIME)
@Target(FIELD)
/**
 * Join
 * @description 连接查询
 * @author YangWenpeng
 * @date 2019年6月4日 下午3:07:18
 * @version v1.0.0
 */
public @interface Join {
    /**
     * 
     * Join
     * @description 字段名
     * @return
     * @author YangWenpeng
     * @date 2019年3月27日 下午4:27:02
     * @version v1.0.0
     */
    String value() default "";
    
    /**
     * 
     * Join
     * @description 联接类型
     * @return
     * @author YangWenpeng
     * @date 2019年6月4日 下午3:12:30
     * @version v1.0.0
     */
    JoinType joinType() default JoinType.INNER;
    
    /**
     * 
     * Join
     * @description 关联的实体类
     * @return
     * @author YangWenpeng
     * @date 2019年6月4日 下午3:12:30
     * @version v1.0.0
     */
    Class<?> targetClass();
}
