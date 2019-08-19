# Fenix

[![Build Status](https://secure.travis-ci.org/blinkfox/fenix.svg)](https://travis-ci.org/blinkfox/fenix) [![HitCount](http://hits.dwyl.io/blinkfox/fenix.svg)](http://hits.dwyl.io/blinkfox/fenix) [![Javadocs](http://www.javadoc.io/badge/com.blinkfox/fenix.svg)](http://www.javadoc.io/doc/com.blinkfox/fenix) [![GitHub license](https://img.shields.io/github/license/blinkfox/fenix.svg)](https://github.com/blinkfox/fenix/blob/develop/LICENSE) [![fenix](https://img.shields.io/badge/fenix-v1.0.0-blue)](https://search.maven.org/artifact/com.blinkfox/fenix/1.0.0/jar) [![fenix starter](https://img.shields.io/badge/fenix%20spring%20boot%20starter-v1.0.0-blue)](https://search.maven.org/artifact/com.blinkfox/fenix-spring-boot-starter/1.0.0/jar) [![codecov](https://codecov.io/gh/blinkfox/fenix/branch/develop/graph/badge.svg)](https://codecov.io/gh/blinkfox/fenix)

> [Fenix](https://github.com/blinkfox/fenix)（菲尼克斯）是一个为了解决复杂动态 SQL (`JPQL`) 而生的 `Spring Data JPA` 扩展库，目的是辅助开发者更方便快捷的书写复杂、动态且易于维护的 SQL，支持 `XML` 和 Java 链式 `API` 两种方式来书写动态 SQL。

- [详细使用文档: https://blinkfox.github.io/fenix](https://blinkfox.github.io/fenix)

## 特性

- 简单、轻量级、无副作用的集成和使用；
- 作为 JPA 的扩展和增强，兼容 Spring Data JPA 的各种特性；
- 提供了 `XML` 和纯 Java API 两种方式来书写 SQL；
- `XML` 的方式功能强大，让 SQL 和 Java 代码解耦，易于维护；
- 也可以采用 Java 链式 `API` 来书写动态 SQL；
- 具有动态性、极致的可复用性和可调试性的优点；
- 具有可扩展性，可自定义 `XML` 语义标签和对应的标签处理器来生成自定义逻辑的 SQL 片段和参数；

## 支持场景

适用于 Java `Spring Data JPA` 项目，`JDK 1.8` 及以上。

## Spring Boot 项目集成

如果你是 Spring Boot 项目，那么直接集成 `fenix-spring-boot-starter` 库，并激活 `FenixJpaRepositoryFactoryBean`。如果不是 Spring Boot 项目，请参看[这里](https://blinkfox.github.io/fenix/#/quick-install?id=not-spring-boot-project)。

### Maven

```xml
<dependency>
    <groupId>com.blinkfox</groupId>
    <artifactId>fenix-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle

```bash
compile 'com.blinkfox:fenix-spring-boot-starter:1.0.0'
```

### 激活 Fenix FactoryBean

然后需要在你的 Spring Boot 应用的 `@EnableJpaRepositories` 注解中，配置
`repositoryFactoryBeanClass` 的属性值为 `FenixJpaRepositoryFactoryBean.class`。

```java
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
  xml-locations: fenix
  # 扫描你自定义的 XML 标签处理器的位置，默认为空，可以是包路径，也可以是 Java 或 class 文件的全路径名
  # 可以配置多个值，不过一般情况下，你不自定义自己的 XML 标签和处理器的话，不需要配置这个值.
  handler-locations: 
```

## 示例概览

以下是一个动态 SQL 示例，关于详细的使用文档可以[参看文档](https://blinkfox.github.io/fenix/#/quick-start)。

```java
/**
 * BlogRepository.
 *
 * @author blinkfox on 2019-08-16.
 */
public interface BlogRepository extends JpaRepository<Blog, String> {

    /**
     * 使用 {@link QueryFenix} 注解来演示根据散参数、博客信息Bean(可以是其它Bean 或者 Map)来多条件模糊分页查询博客信息.
     *
     * @param ids 博客信息 ID 集合
     * @param blog 博客信息实体类，可以是其它 Bean 或者 Map.
     * @param pageable JPA 分页排序参数
     * @return 博客分页信息
     */
    @QueryFenix
    Page<Blog> queryMyBlogs(@Param("ids") List<String> ids, @Param("blog") Blog blog, Pageable pageable);

}
```

XML 文件中的 SQL:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!-- 这是用来操作博客信息的 Fenix XML 文件，请填写 namespace 命名空间. -->
<fenixs namespace="com.blinkfox.fenix.example.repository.BlogRepository">

    <!-- 这是一条完整的 Fenix 查询语句块，必须填写 fenix 标签的 id 属性. -->
    <fenix id="queryMyBlogs">
        SELECT
            b
        FROM
            Blog AS b
        WHERE
        <in field="b.id" value="ids" match="ids != empty"/>
        <andLike field="b.author" value="blog.author" match="blog.author != empty"/>
        <andLike field="b.title" value="blog.title" match="blog.title != empty"/>
        <andBetween field="b.createTime" start="blog.createTime" end="blog.updateTime" match="(?blog.createTime != empty) || (?blog.updateTime != empty)"/>
    </fenix>

</fenixs>
```

## 开源许可证

本 `Fenix` 的 Spring Data JPA 扩展库遵守 [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0) 许可证。
