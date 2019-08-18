## 支持场景

适用于 Java `Spring Data JPA` 项目，`JDK 1.8` 及以上。

## Spring Boot 项目集成 :id=spring-boot-integrations

如果你是 Spring Boot 项目，那么直接集成 `fenix-spring-boot-starter` 库，并激活 `FenixJpaRepositoryFactoryBean`。

### Maven

```xml
<dependency>
    <groupId>com.blinkfox</groupId>
    <artifactId>fenix-spring-boot-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

### Gradle

```bash
compile 'com.blinkfox:fenix-spring-boot-starter:1.0.0-SNAPSHOT'
```

### 激活 Fenix FactoryBean

然后需要在你的 Spring Boot 应用的 `@EnableJpaRepositories` 注解中，配置
`repositoryFactoryBeanClass` 的属性值为 `FenixJpaRepositoryFactoryBean.class`。

```java
import com.blinkfox.fenix.jpa.FenixJpaRepositoryFactoryBean;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 请在 Spring Boot 应用中配置 {@link EnableJpaRepositories#repositoryFactoryBeanClass}
 * 的值为 {@link FenixJpaRepositoryFactoryBean}.
 *
 * @author blinkfox on 2019-08-15.
 */
@EnableJpaRepositories(repositoryFactoryBeanClass = FenixJpaRepositoryFactoryBean.class)
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```

> **注**： `FenixJpaRepositoryFactoryBean` 继承自 Spring Data JPA 默认的 `JpaRepositoryFactoryBean`。所以，Fenix 与 JPA 的各种注解和特性完全兼容，并提供了更加强大的 `@QueryFenix` 注解。

### application.yml 配置项

要修改 Fenix 的配置信息，你需要在你的 Spring Boot 项目中，在 `application.yml` 或者 `application.properties` 中去修改配置信息。

以下通过 `application.yml` 文件来说明 Fenix 中的几个配置项、默认值和说明信息，供你参考。

```yaml
# Fenix 的几个配置项、默认值及详细说明.
fenix:
  # 是否开启 debug 模式，默认 false，一般情况下，不建议开启此配置项.
  debug: false
  # 成功加载 Fenix 配置信息后，是否打印启动 banner，默认 true.
  print-banner: true
  # 是否打印 Fenix 生成的 SQL 信息，默认为 true. 切记，生产环境一定要改为 false.
  print-sql: true
  # 扫描 Fenix XML 文件的所在位置，默认是 fenix 目录及子目录，可以用 yaml 文件方式配置多个值.
  xml-locations:
    - fenix
  # 扫描你自定义的 XML 标签处理器的位置，默认为空，可以是包路径，也可以是 Java 或 class 文件的全路径名
  # 可以配置多个值，不过一般情况下，你不自定义自己的 XML 标签和处理器的话，不需要配置这个值.
  handler-locations: 
```

## 非 SpringBoot 项目集成 :id=not-spring-boot-project

如果你**不是 Spring Boot 项目**，而是通过其他方式来使用 Spring Data JPA。那么，你可以通过以下的方式来集成原生的 `fenix` 库，除了激活 `FenixJpaRepositoryFactoryBean` 之外，**最重要的是需要在你应用启动的过程中，手动加载 Fenix 配置信息到内存中**。

### Maven

```xml
<dependency>
    <groupId>com.blinkfox</groupId>
    <artifactId>fenix</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

### Gradle

```bash
compile 'com.blinkfox:fenix:1.0.0-SNAPSHOT'
```

### 激活 Fenix FactoryBean

跟前面 Spring Boot 激活 Fenix FactoryBean 一样，需要在 `@EnableJpaRepositories` 注解中，配置
`repositoryFactoryBeanClass` 的属性值为 `FenixJpaRepositoryFactoryBean.class`。

主要注解配置的代码如下：

```java
@EnableJpaRepositories(repositoryFactoryBeanClass = FenixJpaRepositoryFactoryBean.class)
```

### 加载 Fenix 配置信息

最后，需要在你的应用启动过程中加载 Fenix 配置信息到内存中。我这里作为示例在 Spring Bean 中的 `@PostConstruct` 的方法中来加载 Fenix 配置。

最简单的加载配置信息的代码如下：

```java
/**
 * Spring 应用启动过程中，本类的实例创建后执行的方法.
 */
@PostConstruct
public void init() {
    // 最简单的代码来加载 Fenix 配置信息，Fenix 的各个配置项也都会有默认值，
    // 默认打印启动 banner，默认打印 SQL 信息，默认加载资源文件(一般是 resources)下名为 'fenix' 的目录及子目录下的所有 fenix XML 文件.
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
    // 在 FenixConfig 实例中，配置是否开启 debug 模式，是否打印 banner，是否打印 SQL 信息等.
    FenixConfig fenixConfig = new FenixConfig()
            .setDebug(false)
            .setPrintBanner(true)
            .setPrintSqlInfo(true);

    // 配置 Fenix XML 文件所在的目录位置，默认扫描位置是资源文件下的 fenix 目录及子目录下，
    // XML 位置可以配置多个，多个用英文逗号隔开即可.
    String xmlLocations = "fenix, myxml/dir";

    // 配置自定义的 XML 语义标签处理器的位置，默认是空。一般情况下，你也不用配置这个值.
    // 该值可以是包路径，也可以是具体的 Java 或者 class 文件的路径，可配置多个值，多个用英文逗号隔开即可.
    String handlerLocations = "com.xxxx.yyyy.handler, com.xxxx.zzzz.MyHandler.java";

    // 正式加载前面赋予的几个
    FenixConfigManager.getInstance().initLoad(fenixConfig, xmlLocations, handlerLocations);
}
```
