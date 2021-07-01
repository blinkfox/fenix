package com.blinkfox.fenix.jpa;

import org.springframework.data.annotation.QueryAnnotation;

import java.lang.annotation.*;

/**
 * 这个注解实在是不好怎么取名: 本意是当标注有这个注解的JPA方法里面的@Query(value = "select user_name from t_user") 里面的sql在运行时提供一个修改为 @Query注解的 value 的标记
 *
 */
@Documented
@QueryAnnotation
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FenixSqlInterceptor {

    /**
     * 可以预定义一些值,感觉定义成枚举更好 比如
     * camel2UnderLine: 驼峰转下划线
     * underLine2Camel: 下划线转驼峰
     * none: 不进行任何操作
     */
    SqlColumnStrategy strategy() default SqlColumnStrategy.NONE;

    public static enum SqlColumnStrategy {

        CAMEL2UNDERLINE("驼峰转下划线"),
        UNDERLINE2CAMEL("下划线转驼峰"),
        NONE("")
        ;

        private String remark;

        SqlColumnStrategy(String remark){
            this.remark = remark;
        }

        public String getRemark() {
            return remark;
        }
    }
}
