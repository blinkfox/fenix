package com.blinkfox.fenix;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.blinkfox.fenix.jpa.FenixJpaRepositoryFactoryBean;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 用于开启（激活）Fenix 的注解.
 *
 * @author YangWenpeng on 2019-12-17
 * @author blinkfox on 2020-01-13
 * @since v2.2.0
 */
@Documented
@Target(TYPE)
@Retention(RUNTIME)
@EnableJpaRepositories(repositoryFactoryBeanClass = FenixJpaRepositoryFactoryBean.class)
public @interface EnableFenix {

}
