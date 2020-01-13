package com.blinkfox.fenix;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Fenix 的 Spring 自动配置类.
 *
 * @author YangWenpeng on 2019-12-17
 * @author blinkfox on 2020-01-13
 * @since v2.2.0
 */
@Configuration
@ComponentScan(basePackages = "com.blinkfox.fenix.specification")
public class FenixConfiguration {

}
