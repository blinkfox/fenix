package com.blinkfox.fenix;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.blinkfox.fenix.jpa.FenixJpaRepositoryFactoryBean;

/**
 * 
 * EnableJpaPlus
 * @description 开启Fenix配置
 * @author YangWenpeng
 * @date 2019年12月17日 下午6:35:12
 * @version v1.0.0
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
@Import(FenixConfiguration.class)
@EnableJpaRepositories(repositoryFactoryBeanClass = FenixJpaRepositoryFactoryBean.class)
public @interface EnableFenix {

}
