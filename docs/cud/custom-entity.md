# 🛰️ 返回自定义的实体对象 :id=return-custom-entity

Fenix 中除了上面介绍的一些功能之外，还有其他额外的辅助、简化开发的功能，以下对返回自定义实体对象的功能作简要介绍。

## 📡 一、初衷 :id=intention

JPA 本身支持通过“[投影](https://docs.spring.io/spring-data/jpa/docs/2.2.0.RELEASE/reference/html/#projections)”的方式来返回自定义的实体对象，但使用起来不那么“自然”。主要原因是：

- **基于接口的投影**需要创建的是查询结果的接口，接口中的方法是各个结果列属性的 `Getter` 方法，这样查询的结果就是这个接口的匿名实例或实例的集合，并非真正意义上的 `Java Bean`。
- **基于类的投影**创建的是一个实体类（`Java Bean`），但必须保证该类中含有查询结果列的构造方法，且还比须保证查询结果列与构造方法参数的顺序要一一对应，增加了后期维护的难度。而且该方式的 JPQL 语句必须使用 `new com.xxx.BlogDto(...)` 形式，比较奇怪，而且不能用于原生 SQL 的场景。

基于以上原因，Fenix 从 `v1.1.0` 版本开始新增了更加简单、自然的方式来返回自定义的实体对象（`Java Bean`）。下面依然通过 XML 和 Java 两种情况来做示例演示和说明。

## 🍭 二、XML 中的使用示例 :id=used-in-xml

首先，定义一个自定义的数据试图实体用户博客信息类（VO）`UserBlogInfo.java`，用来作为查询的返回结果，各属性请保证必须至少含有可公开访问的 `Getter` 和 `Setter` 方法：

```java
package com.blinkfox.fenix.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 用户博客信息的自定义业务实体类，用于测试 JPA 返回自定义实体的使用.
 *
 * @author blinkfox on 2019/8/9.
 */
@Getter
@Setter
public class UserBlogInfo {

    /**
     * 用户 ID.
     */
    private String userId;

    /**
     * 用户名称.
     */
    private String name;

    /**
     * 用户博客 ID.
     */
    private String blogId;

    /**
     * 博客标题.
     */
    private String title;

    /**
     * 博客原作者.
     */
    private String author;

    /**
     * 博客内容.
     */
    private String content;

}
```

在 `BlogRepository.java` 接口中定义查询的接口方法，接口返回的是我们刚才定义的 `UserBlogInfo` 分页信息，**并通过注解中的 `resultType` 属性来告知 Fenix 中你要返回的 Bean 类型**。

```java
/**
 * 使用 {@link QueryFenix} 注解来连表模糊查询自定义的用户博客实体分页信息.
 *
 * @param userId 用户ID
 * @param blog 博客实体信息
 * @return 用户博客信息集合
 */
@QueryFenix(resultType = UserBlogInfo.class)
Page<UserBlogInfo> queryUserBlogsWithFenixResultType(@Param("userId") String userId, @Param("blog") Blog blog, Pageable pageable);
```

然后，在 `BlogRepository.xml` 的 XML 文件中，书写 SQL 语句：

```xml
<!-- 根据用户ID、博客信息查询该用户发表的用户博客信息（用于测试返回自定义的实体信息）. -->
<fenix id="queryUserBlogsWithFenixResultType">
    SELECT
        u.id as userId,
        u.name as name,
        b.id as blogId,
        b.title as title,
        b.author as author,
        b.content as content
    FROM
        Blog as b,
        User as u
    WHERE
        u.id = b.userId
    <andEqual field="b.userId" value="userId"/>
    <andLike field="b.title" value="blog.title" match="blog.title != empty"/>
    <andLike field="b.content" value="blog.content" match="blog.content != empty"/>
</fenix>
```

**💡 注意事项**：

- 🔸 上面的代码关键就在 `QueryFenix` 注解中配置了 `resultType` 属性的值为我们定义的 Bean 的 Class，这样 Fenix 就能自动识别和处理了。也可以写在 XML 的 fenix 节点属性中，`v2.7.0` 版本以前就是这样写的，但考虑到代码重构可能造成类名或类路径修改，为了能更好的利用 Java 编译检查功能，从 `2.7.0` 版本开始支持在 `QueryFenix` 注解中标注，不过以前的方式仍然有效，新版本推荐写到注解中。
- 🔸 另一个要点是默认转换器使用的是 `FenixResultTransformer`，所有查询列**都必须使用 as**来返回一个“别名”，且**这个“别名”必须跟实体类的属性名一致，不区分大小写**，你也可以选择使用 Fenix 内置的其他转换器。
- 🔸 此种方式再运行时可能会与 `spring-boot-devtools.jar` 冲突，报 `No converter found capable of converting from type [xxx] to type [xxx]` 错误，建议不使用该 jar 包。

## 🍻 三、多种内置结果转换器 :id=more-result-transformer

经过大量用户的多次反馈，最终从 `v2.7.0` 版本开始，Fenix 开始增强自定义 Bean 转换的功能，并提供了如下多种自定义 Bean 的结果转换器：

- `FenixResultTransformer`：基于查询结果列 **`as` 别名与属性同名**的方式来转换为自定义 Bean 对象。（**默认，并兼容老版本**）
- `UnderscoreTransformer`：基于查询结果列**下划线转小驼峰**（`lowerCamelCase`）的方式来转换为自定义 Bean 对象。
- `PrefixUnderscoreTransformer`：基于查询结果列**下划线转小驼峰**（`lowerCamelCase`）并去除一些字段固有前缀（如：`c_`、`n_`、`dt_` 等）的方式来转换为自定义 Bean 对象。
- `ColumnAnnotationTransformer`：基于查询结果列与 VO 属性中 `@Column(name = "xxx")` 注解 `name` 相等的方式来转换为自定义 Bean 对象。

接下来，就简单演示几个结果转换器的使用示例。

### 🍉 1. UnderscoreTransformer 使用示例

首先，定义一个普通的 Java VO 类，请确保要填充转换的各种字段至少需要有公开的 `Getter` 和 `Setter` 方法。

```java
@Getter
@Setter
public class UnderscoreVo {

    private Long id;

    private String columnName;

    private Long columnLongName;

    private String columnThreeName;

    private String columnFourTestName;

    private Date columnCreateTime;

    private LocalDateTime columnLastUpdateTime;

}
```

然后，写原生的查询 SQL，查询结果列中包含下划线:

```xml
<!-- 查询并返回自定义的实体 Bean. -->
<fenix id="queryFenixResultType">
    SELECT
        u.id,
        u.column_name,
        column_long_name,
        column_three_name,
        u.column_four_test_name,
        u.column_create_time,
        u.column_last_update_time
    FROM t_underscore_table as u
    WHERE
        column_long_name > #{num}
</fenix>
```

最后，在 `@QueryFenix` 的 `resultTransformer` 属性中，使用 `UnderscoreTransformer.class` 即可。

```java
@QueryFenix(resultType = UnderscoreVo.class, resultTransformer = UnderscoreTransformer.class, nativeQuery = true)
List<UnderscoreVo> queryFenixResultType(@Param("num") long num);
```

### 🍇 2. UnderscoreTransformer 使用示例

首先，定义一个普通的 Java VO 类，请确保要填充转换的各种字段至少需要有公开的 `Getter` 和 `Setter` 方法。

```java
@Getter
@Setter
public class PrefixUnderscoreVo {

    private String id;

    private String name;

    private Integer integerColumn;

    private long longColumn;

    private LocalDateTime lastUpdateTime;

}
```

然后，写原生的查询 SQL，查询结果列中包含下划线:

```xml
<!-- 查询并返回自定义的实体 Bean. -->
<fenix id="queryPrefixUnderscoreVoWithXml">
    SELECT
        c_id,
        c_name,
        n_integer_column,
        n_long_column,
        dt_last_update_time
    FROM t_prefix_underscore
    WHERE
        n_integer_column >= #{num}
</fenix>
```

最后，在 `@QueryFenix` 的 `resultTransformer` 属性中，使用 `PrefixUnderscoreTransformer.class` 即可。

```java
@QueryFenix(resultType = PrefixUnderscoreVo.class,
        resultTransformer = PrefixUnderscoreTransformer.class, nativeQuery = true)
List<PrefixUnderscoreVo> queryPrefixUnderscoreVoWithXml(@Param("num") int num, Pageable pageable)
```

### 🍑 3. UnderscoreTransformer 使用示例

首先，定义一个普通的 Java VO 类，请确保要填充转换的各种字段至少需要有公开的 `Getter` 和 `Setter` 方法。

```java
@Getter
@Setter
public class ParentColumnVo {

    @Column(name = "id")
    private Long id;

    @Column(name = "column_last_update_time")
    private LocalDateTime lastUpdateTime;

}

@Getter
@Setter
// 测试有继承的情况.
public class UnderscoreColumnVo extends ParentColumnVo {

    @Column(name = "column_name")
    private String columnName;

    @Column(name = "column_long_name")
    private Long columnLongName;

    @Column(name = "column_four_test_name")
    private String columnFourTestName;

    /**
     * 这是故意不设置 @Column 注解的字段，测试不转换填充该字段的情况.
     */
    private Date createTime;

}
```

然后，写原生的查询 SQL，查询结果列与上面 VO 类的 `@Column` 注解中的 `name` 属性相匹配:

```xml
<!-- 查询并返回自定义的实体 Bean. -->
<fenix id="queryAtColumnVoList">
    SELECT
        id,
        column_name,
        column_long_name,
        column_four_test_name,
        column_last_update_time
    FROM t_underscore_table
    WHERE
    u.column_long_name > #{num}
</fenix>
```

最后，在 `@QueryFenix` 的 `resultTransformer` 属性中，使用 `ColumnAnnotationTransformer.class` 即可。

```java
@QueryFenix(resultType = UnderscoreColumnVo.class, resultTransformer = ColumnAnnotationTransformer.class,
        nativeQuery = true)
List<UnderscoreColumnVo> queryAtColumnVoList(@Param("num") long num)
```

## 🥣 四、自定义结果转换器

如果 Fenix 提供的几个内置结果转换器，无法满足你的需求，也允许你自定义自己的结果转换器。

你需要继承 Fenix 中的 `AbstractResultTransformer` 抽象类，并重写 `Object transformTuple(Object[] tuple, String[] aliases)` 方法。

在该方法中，参数 `tuple` 表示查询结果每一列的结果数值的数组，`aliases` 表示查询结果列的名称数组。结果 `Object` 就是你最终要转换后的结果对象，你可以参考 Fenix 中内置的几个转换器的实现来书写自定义的代码。或者，也可以视情况直接继承 Fenix 中提供的若干转换器来实现。

另外，`AbstractResultTransformer` 中有一个 `init` 方法，是本类每次实例化后会调用的方法，如果你需要缓存一些数据或做一些初始化操作，可以重写此方法来达到目的。Fenix 中所有转换器都重写了此方法，用来动态缓存被转换结果对象的各个属性，防止每次转换时都反射去获取，提高转换性能。

```java
public class MyResultTransformer extends AbstractResultTransformer {

    /**
     * 做一些初始化操作，每次创建此对象之后都会先调用此方法.
     */
    @Override
    public void init() {
        
    }

    /**
     * 用来将各个查询结果列的别名和值注入到 {@link super#resultClass} 的结果对象中的方法.
     *
     * @param tuple 查询结果列的结果数值的数组
     * @param aliases 查询结果列的名称数组
     * @return 转换后的结果对象
     */
    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {
        
    }

}
```

最终，转换器书写完成之后，你只需要在 `@QueryFenix` 的 `resultTransformer` 属性中使用你自己定义的转换器的 `Class` 即可。

## 💊 五、Java 中的使用示例 :id=used-in-java

在 Java 中的使用示例同 XML 中相似，只不过是将 SQL 写到了 Java 代码中了而已，且通过 `setResultTypeClass` 方法来设置返回的结果类型。

书写 SQL 的 Provider 类代码如下：

```java
public final class BlogSqlInfoProvider {

    /**
     * 使用 Java 拼接 SQL 的方式来拼接查询用户博客信息的 SQL 信息.
     *
     * @param userId 用户 ID
     * @param title 标题
     * @return SQL 信息
     */
    public SqlInfo queryUserBlogsWithFenixJava(@Param("userId") String userId, @Param("title") String title) {
        return Fenix.start()
                .select("u.id AS userId, u.name AS name, b.id AS blogId, b.title AS title, b.author AS author, "
                        + "b.content AS content")
                .from("Blog as b, User as u")
                .where("u.id = b.userId")
                .andEqual("b.userId", userId)
                .andLike("b.title", title, StringHelper.isNotBlank(title))
                .end();
    }

}
```

`BlogRepository.java` 接口中定义查询方法如下：

```java
/**
 * 使用 {@link QueryFenix} 注解和 Java 拼接 SQL 的方式来连表模糊查询并返回自定义的用户博客信息.
 *
 * @param userId 用户 ID
 * @param title 标题
 * @return 自定义的用户博客信息集合
 */
@QueryFenix(provider = BlogSqlInfoProvider.class, resultType = UserBlogInfo.class)
List<UserBlogInfo> queryUserBlogsWithFenixJava(@Param("userId") String userId, @Param("title") String title);
```
