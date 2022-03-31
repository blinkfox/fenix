# 🔥 Fenix

<div align="center"><img style="display: block; margin: 0 auto;" src="https://blinkfox.github.io/fenix/assets/images/logo.png" alt="fenix logo" /></div>

[![Build Status](https://secure.travis-ci.org/blinkfox/fenix.svg)](https://travis-ci.org/blinkfox/fenix) [![Javadocs](http://www.javadoc.io/badge/com.blinkfox/fenix.svg)](http://www.javadoc.io/doc/com.blinkfox/fenix) [![GitHub license](https://img.shields.io/github/license/blinkfox/fenix.svg)](https://github.com/blinkfox/fenix/blob/develop/LICENSE) [![fenix](https://img.shields.io/badge/fenix-v2.7.0-blue)](https://search.maven.org/artifact/com.blinkfox/fenix/2.7.0/jar) [![fenix starter](https://img.shields.io/badge/fenix%20spring%20boot%20starter-v2.7.0-blue)](https://search.maven.org/artifact/com.blinkfox/fenix-spring-boot-starter/2.7.0/jar) [![codecov](https://codecov.io/gh/blinkfox/fenix/branch/develop/graph/badge.svg)](https://codecov.io/gh/blinkfox/fenix)

> [🔥 Fenix](https://github.com/blinkfox/fenix)（菲尼克斯）是一个为了解决复杂动态 SQL (`JPQL`) 而生的 `Spring Data JPA` 扩展库，能辅助开发者更方便快捷的书写复杂、动态且易于维护的 SQL，支持 ActiveRecord 模式和多种查询方式。

[📖 使用文档](https://blinkfox.github.io/fenix) | [✨ Intellij lIDEA 插件](https://plugins.jetbrains.com/plugin/17158-fenix) | [🍉 示例项目 (fenix-example)](https://github.com/blinkfox/fenix-example)

## 💎 一、特性

- 简单、轻量级、无副作用的集成和使用，jar 包仅 `215 KB`；
- 作为 JPA 的扩展和增强，兼容 Spring Data JPA 原有功能和各种特性；
- 提供了 `XML`、Java 链式 `API` 和动态条件注解等四种方式来书写动态 SQL；
- 支持 `ActiveRecord` 模式；
- `XML` 的方式功能强大，让 SQL 和 Java 代码解耦，易于维护；
- 可以采用 Java 链式 `API` 来书写动态 SQL；
- 可以采用动态条件注解和Java 链式 `API` 来书写出动态的 `Specification`；
- 增强了更快速高效的 JPA 批量“增删改”的支持，支持非 `null` 属性的增量更新；
- 支持**雪花算法**和 `NanoId` 的主键 ID 生成策略；
- SQL 执行结果可返回任意自定义的实体对象，支持多种结果转换方式，比使用 JPA 自身的投影方式更加简单；
- 具有可扩展性，如：可自定义 `XML` 语义标签和对应的标签处理器来生成自定义逻辑的 SQL 片段和参数；

## 🏖️️ 二、支持场景

适用于 Java `Spring Data JPA` 项目，`JDK 8` 及以上，Spring Data JPA 的版本须保证 `2.1.8.RELEASE` 及以上；如果你是 Spring Boot 项目，则 Spring Boot 的版本须保证 `2.1.5.RELEASE` 及以上。

## ☘️ 三、Spring Boot 项目集成

如果你是 Spring Boot 项目，那么直接集成 `fenix-spring-boot-starter` 库，并使用 `@EnableFenix` 激活 Fenix 的相关配置信息。

如果你**不是 Spring Boot 项目**，请参看[这里](https://blinkfox.github.io/fenix/#/quick-install?id=not-spring-boot-project) 的配置方式。

> **注**：请确保你使用的 Spring Boot 版本是 **`v2.1.5.RELEASE` 及以上**，如果 Spring Boot 版本是 `v2.2.x.RELEASE` 及以上，则 Fenix 版本必须是 `v2.0.0` 版本及以上。

### 🌾 1. Maven

```xml
<dependency>
    <groupId>com.blinkfox</groupId>
    <artifactId>fenix-spring-boot-starter</artifactId>
    <version>2.7.0</version>
</dependency>
```

### 🌵 2. Gradle

```bash
compile 'com.blinkfox:fenix-spring-boot-starter:2.7.0'
```

### 🏕️ 3. 激活 Fenix (@EnableFenix)

然后需要在你的 Spring Boot 应用中使用 `@EnableFenix` 激活 Fenix 的相关配置信息。

```java
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

> **💡 注**： 
> 1. `@EnableFenix` 注解中实质上是使用的是 `FenixJpaRepositoryFactoryBean`。而 `FenixJpaRepositoryFactoryBean` 继承自 Spring Data JPA 默认的 `JpaRepositoryFactoryBean`。所以，Fenix 与 JPA 的各种注解和特性完全兼容，并提供了更加强大的 `@QueryFenix` 注解和其他更多动态的能力。
> 2. 如果你是多数据源，则你可以根据自身情况，在需要的数据源中的 `@EnableJpaRepositories` 注解中单独设置 `repositoryFactoryBeanClass` 的值为：`FenixJpaRepositoryFactoryBean.class`。示例如：`@EnableJpaRepositories(repositoryFactoryBeanClass = FenixJpaRepositoryFactoryBean.class)`。

### 🏝️ 4. application.yml 配置（可选的）

> **注**：Fenix 采用了**约定优于配置**的方式，所以通常情况下，你可以不用做任何的 Fenix 配置。

如果你要修改 Fenix 的配置信息，你需要在你的 Spring Boot 项目中，在 `application.yml` 或者 `application.properties` 中去修改配置信息。

以下通过 `application.yml` 文件来展示 Fenix 中的几个配置项、默认值和说明信息，供你参考。

```yaml
# Fenix 的几个配置项、默认值及详细说明，通常情况下你不需要填写这些配置信息（下面的配置代码也都可以删掉）.
fenix:
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
  # v2.2.0 版本新增的配置项，表示自定义的继承自 AbstractPredicateHandler 的子类的全路径名
  # 可以配置多个值，通常情况下，你也不需要配置这个值.
  predicate-handlers:
  # v2.7.0 新增的配置项，表示带前缀下划线转换时要移除的自定义前缀，多个值用英文逗号隔开，通常你不用配置这个值.
  underscore-transformer-prefix:
```

## 🍔 四、示例概览

Fenix 中支持四种方式书写动态 SQL，分别是：

- 基于 JPQL (或 SQL) 的 XML 方式
- 基于 JPQL (或 SQL) 的 Java API 方式
- 基于 `Specification` 的 Java API 方式
- 基于 `Specification` 的 Java Bean 注解方式

以下的四种方式的示例均以博客信息数据作为示例，你可以根据自己的场景或喜欢的方式来选择动态查询的方式。关于详细的使用文档可以[参看文档](https://blinkfox.github.io/fenix/#/)。

### 1. 🍖 基于 JPQL (或 SQL) 的 XML 方式

在 `BlogRepository` 中的查询方法使用 `QueryFenix` 注解，用来分页查询博客信息数据：

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

在 `BlogRepository.xml` 文件中，定义一个跟查询方法同名的 fenix 节点，内容如下：

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

下面是 `queryMyBlogs` 接口方法的单元测试：

```java
/**
 * 测试使用 {@link QueryFenix} 注解根据任意参数多条件模糊分页查询博客信息.
 */
@Test
public void queryMyBlogs() {
    // 模拟构造查询的相关参数.
    List<String> ids = Arrays.asList("1", "2", "3", "4", "5", "6");
    Blog blog = new Blog().setAuthor("ZhangSan").setUpdateTime(new Date());
    Pageable pageable = PageRequest.of(0, 3, Sort.by(Sort.Order.desc("createTime")));

    // 查询并断言查询结果的正确性.
    Page<Blog> blogs = blogRepository.queryMyBlogs(ids, blog, pageable);
    Assert.assertEquals(4, blogs.getTotalElements());
    Assert.assertEquals(3, blogs.getContent().size());
}
```

### 2. 🍟 基于 JPQL (或 SQL) 的 Java API 方式

在 `BlogRepository` 中的查询方法使用 `QueryFenix` 注解，用来查询所有符合条件的博客信息数据：

```java
public interface BlogRepository extends JpaRepository<Blog, String> {

    /**
     * 使用 {@link QueryFenix} 注解和 Java API 来拼接 SQL 的方式来查询博客信息.
     *
     * @param blog 博客信息实体
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param blogIds 博客 ID 集合
     * @return 用户信息集合
     */
    @QueryFenix(provider = BlogSqlProvider.class)
    List<Blog> queryBlogsWithJava(@Param("blog") Blog blog, @Param("startTime") Date startTime,
            @Param("endTime") Date endTime, @Param("blogIds") String[] blogIds);

}
```

创建 `BlogSqlProvider` 类，定义一个与查询方法同名的方法 `queryBlogsWithJava` 方法，用来使用 Java 的方式来动态拼接 JPQL (或 SQL) 语句。

```java
public class BlogSqlProvider {

    /**
     * 通过 Java API 来拼接得到 {@link SqlInfo} 的方式来查询博客信息.
     *
     * @param blogIds 博客 ID 集合
     * @param blog 博客信息实体
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return {@link SqlInfo} 示例
     */
    public SqlInfo queryBlogsWithJava(@Param("blogIds") String[] blogIds, @Param("blog") Blog blog,
            @Param("startTime") Date startTime, @Param("endTime") Date endTime) {
        return Fenix.start()
                .select("b")
                .from("Blog").as("b")
                .where()
                .in("b.id", blogIds, CollectionHelper.isNotEmpty(blogIds))
                .andLike("b.title", blog.getTitle(), StringHelper.isNotBlank(blog.getTitle()))
                .andLike("b.author", blog.getAuthor(), StringHelper.isNotBlank(blog.getAuthor()))
                .andBetween("b.createTime", startTime, endTime, startTime != null || endTime != null)
                .end();
    }

}
```

下面是 `queryBlogsWithJava` 接口方法的单元测试：

```java
/**
 * 测试使用 {@link QueryFenix} 注解和 Java API 来拼接 SQL 的方式来查询博客信息.
 */
@Test
public void queryBlogsWithJava() {
    // 构造查询的相关参数.
    String[] ids = new String[]{"1", "2", "3", "4", "5", "6", "7", "8"};
    Blog blog = new Blog().setAuthor("ZhangSan");
    Date startTime = Date.from(LocalDateTime.of(2019, Month.APRIL, 8, 0, 0, 0)
            .atZone(ZoneId.systemDefault()).toInstant());
    Date endTime = Date.from(LocalDateTime.of(2019, Month.OCTOBER, 8, 0, 0, 0)
            .atZone(ZoneId.systemDefault()).toInstant());

    // 查询并断言查询结果的正确性.
    List<Blog> blogs = blogRepository.queryBlogsWithJava(blog, startTime, endTime, ids);
    Assert.assertEquals(3, blogs.size());
}
```

### 3. 🍭 基于 Specification 的 Java API 方式

基于 `Specification` 的方式，只须要 `BlogRepository` 接口继承 `FenixJpaSpecificationExecutor` 接口即可。

```java
// JpaRepository<Blog, String> 和 FenixJpaSpecificationExecutor<Blog> 可以混用，也可以只使用某一个.
public interface BlogRepository extends JpaRepository<Blog, String>, FenixJpaSpecificationExecutor<Blog> {

}
```

基于 `Specification` 的方式，不需要定义额外的查询方法，也不需要写 `JPQL` (或 SQL) 语句，简单直接。下面是通过 Java 链式的 API 方式来做单元测试的使用方式示例：

```java
/**
 * 测试使用 Fenix 中的  {@link FenixSpecification} 的链式 Java API 来动态查询博客信息.
 */
@Test
public void queryBlogsWithSpecifition() {
    // 这一段代码是在模拟构造前台传递查询的相关 map 型参数，当然也可以使用其他 Java 对象，作为查询参数.
    Map<String, Object> params = new HashMap<>();
    params.put("ids", new String[]{"1", "2", "3", "4", "5", "6", "7", "8"});
    params.put("author", "ZhangSan");
    params.put("startTime", Date.from(LocalDateTime.of(2019, Month.APRIL, 8, 0, 0, 0)
            .atZone(ZoneId.systemDefault()).toInstant()));
    params.put("endTime", Date.from(LocalDateTime.of(2019, Month.OCTOBER, 8, 0, 0, 0)
            .atZone(ZoneId.systemDefault()).toInstant()));

    // 开始真正的查询，使用.
    Object[] ids = (Object[]) params.get("ids");
    List<Blog> blogs = blogRepository.findAll(builder ->
            builder.andIn("id", ids, ids != null && ids.length > 0)
                    .andLike("title", params.get("title"), params.get("title") != null)
                    .andLike("author", params.get("author"))
                    .andBetween("createTime", params.get("startTime"), params.get("endTime"))
            .build());

    // 单元测试断言查询结果的正确性.
    Assert.assertEquals(3, blogs.size());
    blogs.forEach(blog -> Assert.assertTrue(blog.getAuthor().endsWith("ZhangSan")));
}
```

### 4. 🥯 基于 Specification 的 Java Bean 注解方式

本方式是指通过将 Java Bean 作为参数传递，在 Java Bean 对象的属性中通过查询的条件注解来表明是何种查询匹配方式。当然，同第三种方式一样，`BlogRepository` 接口也须要继承 `FenixJpaSpecificationExecutor` 接口。

```java
// JpaRepository<Blog, String> 和 FenixJpaSpecificationExecutor<Blog> 可以混用，也可以只使用某一个.
public interface BlogRepository extends JpaRepository<Blog, String>, FenixJpaSpecificationExecutor<Blog> {

}
```

然后，定义一个用于表示各种查询条件的普通 Java Bean 类 `BlogParam`，当然该类也可以是前台传递过来的对象参数，也可以单独定义。该类的各个属性对应某个查询字段，属性上的注解对应查询的匹配方式，某个字段是否生成查询条件的默认判断依据是该属性值是否为空。

```java
import com.blinkfox.fenix.specification.annotation.Between;
import com.blinkfox.fenix.specification.annotation.In;
import com.blinkfox.fenix.specification.annotation.Like;
import com.blinkfox.fenix.specification.handler.bean.BetweenValue;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 用于测试 {@code FenixSpecification} 注解动态查询的博客 VO 类.
 *
 * @author blinkfox on 2020-01-28.
 */
@Getter
@Setter
@Accessors(chain = true)
public class BlogParam {

    /**
     * 用于 IN 范围查询的 ID 集合，{@link In} 注解的属性值可以是数组，也可以是 {@link java.util.Collection} 集合数据.
     */
    @In("id")
    private List<String> ids;

    /**
     * 模糊查询博客信息的作者名称关键字内容的字符串.
     */
    @Like
    private String author;

    /**
     * 用于根据博客创建时间 {@link Between} 区间查询博客信息的开始值和介绍值，
     * 区间查询的值类型建议是 {@link BetweenValue} 类型的.
     * 当然值类型也可以是二元数组，也可以是 {@link List} 集合，如果是这两种类型的值，元素的顺序必须是开始值和结束值才行.
     */
    @Between("createTime")
    private BetweenValue<Date> createTime;

}
```

下面是单元测试的使用方式示例：

```java
/**
 * 测试使用 Fenix 中的  {@link FenixSpecification} 的 Java Bean 条件注解的方式来动态查询博客信息.
 */
@Test
public void queryBlogsWithAnnotaion() {
    // 这一段代码是在模拟构造前台传递的或单独定义的 Java Bean 对象参数.
    Date startTime = Date.from(LocalDateTime.of(2019, Month.APRIL, 8, 0, 0, 0)
            .atZone(ZoneId.systemDefault()).toInstant());
    Date endTime = Date.from(LocalDateTime.of(2019, Month.OCTOBER, 8, 0, 0, 0)
            .atZone(ZoneId.systemDefault()).toInstant());
    BlogParam blogParam = new BlogParam()
            .setIds(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8"))
            .setAuthor("ZhangSan")
            .setCreateTime(BetweenValue.of(startTime, endTime));

    // 开始真正的查询.
    List<Blog> blogs = blogRepository.findAllOfBean(blogParam);

    // 单元测试断言查询结果的正确性.
    Assert.assertEquals(3, blogs.size());
    blogs.forEach(blog -> Assert.assertTrue(blog.getAuthor().endsWith("ZhangSan")));
}
```

## 🙋 五、其他贡献者 :id=contributor

感谢如下贡献者，没有他们， `Fenix` 不会如此完美。

- [@pengten](https://github.com/pengten)
- [@jgaybjone](https://github.com/jgaybjone)
- [@imhansai](https://github.com/imhansai)

## 🙏 六、鸣谢

感谢 [JetBrains 公司](https://www.jetbrains.com/?from=fenix) 为本开源项目提供的免费正版 Intellij IDEA 的 License 支持。

## 📝 七、开源许可证

本 `Fenix` 的 Spring Data JPA 扩展库遵守 [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0) 许可证。
