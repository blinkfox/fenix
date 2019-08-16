对于很长的、复杂的动态或统计性的 SQL 采用注解 或者 Java 书写不仅冗长，且不易于调试和维护。因此，我更推荐你通过 `XML` 文件来书写 SQL，使得 SQL 和 Java 代码解耦，更易于维护和阅读。

下面我将以一个新的 `Spring Boot` 项目来演示 Fenix 的使用。

## 项目和数据准备

!> **注**：下面“项目和数据准备”的内容，除了集成 Fenix 配置之外，基本上与 Fenix 无关，你大概体验和预览下内容就行。

### 创建项目

在 [start.spring.io](https://start.spring.io/) 中创建一个自己的 SpringBoot2.x 项目，目前最新稳定版本是 `2.1.7`。选出了一些组件来生成项目，我这里仅选了如下几个：

- `JPA`: 这是**必须**组件，就是用来试用 `Spring Data JPA` 的 Fenix 扩展的
- `Lombok`: 可以通过注解大量减少 Java 中重复代码的书写
- `HSQLDB`: 内存数据库，用来做测试。

生成之后直接导入 IDE 开发工具，然后根据前面的 Fenix [Spring Boot 项目集成](quick-install?id=spring-boot-integrations) 的文档集成 Fenix 库到项目中即可，这里不再赘述。你也可以 [点击这里下载](https://github.com/blinkfox/fenix-example) 本示例项目的源码查看。

### Blog 实体类

以下将以一个简单的博客信息（`Blog`）作为实体来演示在 Fenix 中 `XML` 动态 SQL 的使用。`Blog` 实体类代码如下：

```java
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 博客信息实体类.
 *
 * @author blinkfox on 2019-08-16.
 */
@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "t_blog")
public class Blog {

    /**
     * ID.
     */
    @Id
    @Column(name = "c_id")
    private String id;

    /**
     * 发表博客的用户 ID.
     */
    @Column(name = "c_user_id")
    private String userId;

    /**
     * 博客标题.
     */
    @Column(name = "c_title")
    private String title;

    /**
     * 博客作者.
     */
    @Column(name = "c_author")
    private String author;

    /**
     * 博客内容.
     */
    @Column(name = "c_content")
    private String content;

    /**
     * 创建时间.
     */
    @Column(name = "dt_create_time")
    private Date createTime;

    /**
     * 更新时间.
     */
    @Column(name = "dt_update_time")
    private Date updateTime;

}
```

### 初始化结构和数据

我这里准备了一些初始化的数据表结构和数据脚本，当然也可用直接通过 JPA 特性和 API 代码来初始化数据。这里，我还是通过脚本的方式来初始化一些后续查询需要用到的数据。

在 `src/test/resources` 资源目录下新建 `db` 目录，在 `db` 目录下分别新建用于管理的表结构文件(`schema.sql`)和初始化数据文件(`data.sql`)的 SQL 脚本。

`schema.sql` 文件中的内容如下：

```sql
-- 创建数据库表所在的模式 schema 为 test.
CREATE SCHEMA test;
commit;

-- 在 test 模式下创建数据库表.
DROP TABLE IF EXISTS test.t_blog;
CREATE TABLE test.t_blog (
    c_id varchar(32) NOT NULL,
    c_user_id varchar(255),
    c_author varchar(255),
    c_title varchar(255),
    c_content varchar(255),
    dt_create_time timestamp(6) NULL,
    dt_update_time timestamp(6) NULL,
    constraint pk_test_blog primary key(c_id)
);
commit;
```

`data.sql` 文件中的内容如下：

```sql
-- 初始化插入一些博客信息数据，方便查询.
INSERT INTO test.t_blog VALUES ('1', '1', '张三-ZhangSan', 'Spring从入门到精通', '这是 Spring 相关的内容', '2019-03-01 00:41:33', '2019-03-01 00:41:36');
INSERT INTO test.t_blog VALUES ('2', '1', '李四-LiSi', 'Spring Data JPA 基础教程','这是 Spring Data JPA 相关的内容', '2019-05-01 00:41:33', '2019-05-01 00:41:36');
INSERT INTO test.t_blog VALUES ('3', '1', '张三-ZhangSan', 'Spring Data JPA 文档翻译','这是 Spring Data JPA 文档翻译的内容', '2019-07-01 00:41:33', '2019-07-01 00:41:36');
INSERT INTO test.t_blog VALUES ('4', '1', '张三-ZhangSan', 'MyBatis 中文教程','这是 MyBatis 中文教程的内容', '2019-07-05 00:41:33', '2019-07-05 00:41:36');
INSERT INTO test.t_blog VALUES ('5', '2', '张三-ZhangSan', 'SpringBoot 快速入门','这是 SpringBoot 快速入门的内容', '2019-07-08 00:41:33', '2019-07-08 00:41:36');
INSERT INTO test.t_blog VALUES ('6', '2', '张三-WangWu', 'Java 初级教程','这是 Java 初级教程的内容', '2019-07-12 00:41:33', '2019-07-12 00:41:36');
INSERT INTO test.t_blog VALUES ('7', '3', '王五-WangWu', '分库分表之最佳实践','这是分库分表之最佳实践的内容', '2019-08-01 00:41:33', '2019-08-01 00:41:36');
INSERT INTO test.t_blog VALUES ('8', '3', '王五-WangWu', '你不知道的 CSS 使用技巧','这是你不知道的 CSS 使用技巧的内容', '2019-08-03 00:41:33', '2019-08-03 00:41:36');
INSERT INTO test.t_blog VALUES ('9', '4', '马六-Maliu', 'Vue 项目实战','这是Vue 项目实战的内容', '2019-08-07 00:41:33', '2019-08-07 00:41:36');
INSERT INTO test.t_blog VALUES ('10', '5', '马六-Maliu', 'JavaScript 精粹','这是 JavaScript 精粹的内容', '2019-08-13 00:41:33', '2019-08-13 00:41:36');
```

### application.yml 配置

以下是 `application.yml` 配置文件的内容，供你参考：

```yaml
# 内存数据库 和 JPA 的配置.
spring:
  datasource:
    url: jdbc:hsqldb:mem:dbtest
    username: test
    password: 123456
    driver-class-name: org.hsqldb.jdbcDriver
    platform: hsqldb
    schema: classpath:db/schema.sql
    data: classpath:db/data.sql
  jpa:
    show-sql: true
    hibernate:
      ddl-auto: update

# Fenix 的几个配置，都有默认值，所以你视具体情况配置即可.
fenix:
  debug: false
  print-banner: true
  print-sql: true
  xml-locations:
  handler-locations:
```

## 创建 BlogRepository.java

定义博客信息操作的持久层代码 `BlogRepository` 接口，这里使用 `@QueryFenix` 注解来演示根据散参数、博客信息 Bean(可以是其它Bean 或者 Map)的参数来**多条件模糊分页查询**博客信息。注解的值是 Fenix XML 文件对应的命名空间 `namespace` 和 `<fenix id="queryMyBlogs"></fenix>` XML 标签的 `id` 属性值。

!> **注**：一些简单的查询 SQL 语句，我仍然建议你使用原生的 `@Query` 注解，而将 `@QueryFenix` 注解使用在较长的、复杂动态 SQL 的场景中。

```java
import com.blinkfox.fenix.example.entity.Blog;

import com.blinkfox.fenix.jpa.QueryFenix;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

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
    @QueryFenix("BlogRepository.queryMyBlogs")
    Page<Blog> queryMyBlogs(@Param("ids") List<String> ids, @Param("blog") Blog blog, Pageable pageable);

}
```

## 创建 BlogRepository.xml

在 `BlogRepository.java` 中的标注了 `@QueryFenix("BlogRepository.queryMyBlogs")` 注解，就需要根据此注解的信息找到 XML 文件中的 JPQL 语句块儿，才能生成 JPQL 语句和参数，并执行 JPQL 语句。

所以，接下来就需要在 `src/main/resources` 资源目录中新建 `fenix` 目录（因为默认扫描这个目录下的 XML 文件，当然你也可以自定义配置 Fenix 扫描的目录文件），然后在 `fenix` 目录中创建 `BlogRepository.xml` 文件，内容如下：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!-- 这是用来操作博客信息的 Fenix XML 文件，请填写 namespace 命名空间. -->
<fenixs namespace="BlogRepository">

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

## 创建并执行单元测试

最后，创建一个用于测试查询博客信息的单元测试类 `BlogRepositoryTest`，代码如下：

```java
package com.blinkfox.fenix.example.repository;

import com.blinkfox.fenix.example.entity.Blog;

import java.util.Date;
import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * BlogRepository 的单元测试类.
 *
 * @author blinkfox on 2019-08-16.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogRepositoryTest {

    @Resource
    private BlogRepository blogRepository;

    /**
     * 测试使用 {@link com.blinkfox.fenix.jpa.QueryFenix} 注解根据任意参数多条件模糊分页查询博客信息.
     */
    @Test
    public void queryMyBlogs() {
        // 构造查询的相关参数.
        List<String> ids = Arrays.asList("1", "2", "3", "4", "5", "6");
        Blog blog = new Blog().setAuthor("ZhangSan").setUpdateTime(new Date());
        Pageable pageable = PageRequest.of(0, 3, Sort.by(Sort.Order.desc("createTime")));

        // 查询并断言查询结果的正确性.
        Page<Blog> blogs = blogRepository.queryMyBlogs(ids, blog, pageable);
        Assert.assertEquals(4, blogs.getTotalElements());
        Assert.assertEquals(3, blogs.getContent().size());
    }

}
```

执行该单元测试，断言语句也会通过。在测试运行过程中，将打印出 Fenix 运行过程中生成的 JPQL 信息：

```sql
------------------------------------------------------------ Fenix 生成的 SQL 信息 ---------------------------------------------------------
-- Fenix xml: fenix/BlogRepository.xml -> queryMyBlogs
-------- SQL: SELECT b FROM Blog AS b WHERE b.id IN :ids AND b.author LIKE :blog_author AND b.createTime <= :blog_updateTime
----- Params: {blog_updateTime=Fri Aug 16 13:23:17 CST 2019, blog_author=%ZhangSan%, ids=[1, 2, 3, 4, 5, 6]}
-------------------------------------------------------------------------------------------------------------------------------------------
```

通过以上的示例，简单的演示了 Fenix 的使用，关于 [QueryFenix](http://localhost:3000/#/queryfenix-introduction) 注解的高级使用请继续往下查看。
