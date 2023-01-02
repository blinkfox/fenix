# 🍋 快速集成 :id=title

## 🏖️ 一、支持场景 :id=support-scenarios

适用于 Java `Spring Data JPA` 项目，`JDK 1.8` 及以上，Spring Data JPA 的版本须保证 `2.1.8.RELEASE` 及以上；如果你是 Spring Boot 项目，则 Spring Boot 的版本须保证 `2.1.5.RELEASE` 及以上。因为后续版本的 Spring Data JPA 对其中 `QueryLookupStrategy` 的代码有较大改动。

## ☘️ 二、Spring Boot 项目集成 :id=spring-boot-integrations

如果你是 Spring Boot 项目，那么直接集成 `fenix-spring-boot-starter` 库，并使用 `@EnableFenix` 激活 Fenix 的相关配置信息。

!> **💡 注**：请确保你使用的 Spring Boot 版本是 **`v2.1.5.RELEASE` 及以上**，如果 Spring Boot 版本是 `v2.2.x.RELEASE` 及以上，则 Fenix 版本必须是 `v2.0.0` 版本及以上。

### 🌾 1. Maven :id=spring-boot-maven

```xml
<dependency>
    <groupId>com.blinkfox</groupId>
    <artifactId>fenix-spring-boot-starter</artifactId>
    <version>3.0.0</version>
</dependency>
```

### 🌵 2. Gradle :id=spring-boot-gradle

```bash
compile 'com.blinkfox:fenix-spring-boot-starter:3.0.0'
```

### 🏕️ 3. 激活 Fenix (@EnableFenix) :id=enable-fenix

然后需要在你的 Spring Boot 应用中使用 `@EnableFenix` 激活 Fenix 的相关配置信息。

```java
import com.blinkfox.fenix.EnableFenix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 请在 Spring Boot 应用中标注 {code @EnableFenix} 注解.
 *
 * @author blinkfox on 2020-02-01.
 */
@EnableFenix
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```

**💡 注意事项**：

- 🔹 `@EnableFenix` 注解中实质上是使用的是 `FenixJpaRepositoryFactoryBean`。而 `FenixJpaRepositoryFactoryBean` 继承自 Spring Data JPA 默认的 `JpaRepositoryFactoryBean`。所以，Fenix 与 JPA 的各种注解和特性完全兼容，并提供了更加强大的 `@QueryFenix` 注解和其他更多动态的能力。
- 🔹 如果你是多数据源，则你可以根据自身情况，在需要的数据源中使用 `@EnableFenix` 注解即可。或者你也可以在 `@EnableJpaRepositories` 注解中单独设置 `repositoryFactoryBeanClass` 的值为：`FenixJpaRepositoryFactoryBean.class`。示例如：`@EnableJpaRepositories(repositoryFactoryBeanClass = FenixJpaRepositoryFactoryBean.class)`。

### 🏝️ 4. application.yml 配置（可选的） :id=spring-boot-config

!> **💡 注**： Fenix 采用了**约定优于配置**的方式，所以通常情况下，你可以不用做下面任何的 Fenix 配置，下面的配置信息供你参考使用。

要修改 Fenix 的配置信息，你需要在你的 Spring Boot 项目中，在 `application.yml` 或者 `application.properties` 中去修改配置信息。

以下通过 `application.yml` 文件来说明 Fenix 中的几个配置项、默认值和说明信息，供你参考。

```yaml
# Fenix 的几个配置项、默认值及详细说明，通常情况下你不需要填写这些配置信息（下面的配置代码也都可以删掉）.
fenix:
  # v2.4.1 版本新增，表示是否开启 debug 调试模式，默认 false。
  # 当开启之后，对 XML 中的 SQL 会进行实时文件流的读取和解析，不需要重启服务。切记仅在开发环境中开启此功能.
  debug: false
  # 成功加载 Fenix 配置信息后，是否打印启动 banner，默认 true.
  print-banner: true
  # 是否打印 Fenix 生成的 SQL 信息，默认为空.
  # 当该值为空时，会读取 'spring.jpa.show-sql' 的值，为 true 就打印 SQL 信息，否则不打印.
  # 当该值为 true 时，就打印 SQL 信息，否则不打印. 生产环境不建议设置为 true.
  print-sql:
  # 扫描 Fenix XML 文件的所在位置，默认是 fenix 目录及子目录，可以用 yaml 文件方式配置多个值.
  xml-locations: fenix
  # 扫描你自定义的 XML 标签处理器的位置，默认为空，可以是包路径，也可以是 Java 或 class 文件的全路径名
  # 可以配置多个值，不过一般情况下，你不自定义自己的 XML 标签和处理器的话，不需要配置这个值.
  handler-locations:
  # v2.2.0 新增的配置项，表示自定义的继承自 AbstractPredicateHandler 的子类的全路径名
  # 可以配置多个值，通常情况下，你也不需要配置这个值.
  predicate-handlers:
  # v2.7.0 新增的配置项，表示带前缀下划线转换时要移除的自定义前缀，多个值用英文逗号隔开，通常你不用配置这个值.
  underscore-transformer-prefix:
```

## 🍁 三、非 SpringBoot 项目集成 :id=not-spring-boot-project

如果你**不是 Spring Boot 项目**，而是通过其他方式来使用 Spring Data JPA。那么，你可以通过以下的方式来集成原生的 `fenix` 库，除了通过 `@EnableFenix` 激活之外，**最重要的是需要在你应用启动的过程中，手动加载某些 Fenix 配置信息到内存中**。

!> **注**：请确保你引入的 Spring Data JPA 版本是 **`2.1.8.RELEASE` 及以上**，如果 Spring Data JPA 版本是 `v2.2.x.RELEASE` 及以上，则 Fenix 版本必须是 `v2.0.0` 版本及以上。。

### 🌼 1. Maven :id=project-maven

```xml
<dependency>
    <groupId>com.blinkfox</groupId>
    <artifactId>fenix</artifactId>
    <version>3.0.0</version>
</dependency>
```

### 🌻 2. Gradle :id=project-gradle

```bash
compile 'com.blinkfox:fenix:3.0.0'
```

### 🏔️ 3. 激活 Fenix :id=project-enable-fenix

跟前面 Spring Boot 激活 Fenix FactoryBean 一样，需要在启动类中使用 `@EnableFenix` 激活 Fenix，也可以直接在 `@EnableJpaRepositories` 注解中，配置 `repositoryFactoryBeanClass` 的属性值为 `FenixJpaRepositoryFactoryBean.class`。

主要注解配置的代码如下：

```java
// 第一种方式使用 @EnableFenix 激活
@EnableFenix

// 第二种方式，也可以直接在 @EnableJpaRepositories 注解中设置 FenixJpaRepositoryFactoryBean.
@EnableJpaRepositories(repositoryFactoryBeanClass = FenixJpaRepositoryFactoryBean.class)
```

### 🚣 4. 加载 Fenix 配置信息 :id=project-config

最后，需要在你的应用启动过程中加载 Fenix 配置信息到内存中。我这里作为示例在 Spring Bean 中的 `@PostConstruct` 的方法中来加载 Fenix 配置。当然，你也可以在你想初始化的任何代码中去加执 Fenix 的初始化加载的代码。

最简单的加载配置信息的代码如下：

```java
/**
 * Spring 应用启动过程中，本类的实例创建后执行的方法.
 */
@PostConstruct
public void init() {
    // 最简单的代码来加载 Fenix 配置信息，Fenix 的各个配置项也都会有默认值，默认打印启动 banner，默认不打印 SQL 信息.
    // 默认加载资源文件(一般是 resources)下名为 'fenix' 的目录及子目录下的所有 Fenix XML 文件.
    FenixConfigManager.getInstance().initLoad();
}
```

更加自定义的、完整的加载配置信息的代码如下：

```java
/**
 * Spring 应用启动过程中，本类的实例创建后执行的方法.
 */
@PostConstruct
public void init() {
    // 配置 Fenix XML 文件所在的目录位置，可以是目录，也可以是具体的 XML 文件，
    // 默认扫描位置是资源文件下的 fenix 目录及子目录下，XML 位置可以配置多个，多个用英文逗号隔开即可.
    String xmlLocations = "fenix, myxml/dir, others/abc/def.xml";

    // 配置自定义的 XML 语义标签处理器的位置，默认是空。一般情况下，你也不用配置这个值.
    // 该值可以是包路径，也可以是具体的 Java 或者 class 文件的路径，可配置多个值，多个用英文逗号隔开即可.
    String handlerLocations = "com.xxxx.yyyy.handler, com.xxxx.zzzz.MyHandler.java";

    // 在 FenixConfig 实例中，配置是否是否打印 banner，是否打印 SQL 信息等.

    // 加载 FenixConfig 实例，配置是否打印 banner、SQL 信息、XML 文件位置和自定义的 XML 语义标签处理器的位置.
    FenixConfigManager.getInstance().initLoad(new FenixConfig()
            .setDebug(false)
            .setPrintBanner(true)
            .setPrintSqlInfo(true)
            .setXmlLocations(xmlLocations)
            .setHandlerLocations(handlerLocations));

    // 下面是添加用户自定义的 XML 标签和对应标签处理器的代码示例，可以添加多个，还有其他更多的重载方法 API.
    FenixConfig.add("andHello", " AND ", HelloTagHandler.class);
    FenixConfig.add("andHi", " AND ", HelloTagHandler::new, " LIKE ");

    // 下面是添加用户自定义的继承自 AbstractPredicateHandler 的子类对象实例的示例，可以添加多个.
    FenixConfig.add(myPredicateHandler1);
    FenixConfig.add(myPredicateHandler2);
}
```
