/**
 * @projectName fenix
 * @package com.blinkfox.fenix
 * @className com.blinkfox.fenix.FenixConfiguration
 * @copyright Copyright 2019 Thuisoft, Inc. All rights reserved.
 */
package com.blinkfox.fenix;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * FenixConfiguration
 * @description Fenix的Configuration
 * @author YangWenpeng
 * @date 2019年12月17日 下午5:23:59
 * @version v1.0.0
 */
@Configuration
@ComponentScan(basePackages = "com.blinkfox.fenix.specification")
public class FenixConfiguration {

}
