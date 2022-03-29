# 🍓 @QueryFenix 注解 :id=title

## 🚀 一、注解元数据介绍 :id=metadata

- **`value()`**: 完整的 `fenix id` 标记，该值由 XML 文件的命名空间（`namespace`）、点（`.`）号和 Fenix XML 标签中的 fenix id 组成。如果 `namespace` 的值写成对应 `repository` 接口的全路径名，则该值可以不用写 `namespace`和`.`号，且如果 fenix id 与对应的查询方法名一样，那么 `value()` 值就可以为空。
- **`countQuery()`**: 表示查询分页查询情况下查询总记录数时需要执行的 SQL。该值仅分页查询时用到，值的规则同上面的 `value()` 一样，如果 `namespace` 的值写成对应 `repository` 接口的全路径名，则该值可以不用写 `namespace`和`.`号。
- **`nativeQuery()`**: 表示原生 SQL 查询，默认为 `false`。
- **`provider()`**: 表示通过 Java 来拼接 SQL 语句的提供类的 class。
- **`method()`**: 表示通过 Java 来拼接 SQL 语句的提供类的方法。
- **`countMethod()`**: 表示通过 Java 来拼接分页查询时查询总记录数 SQL 语句的提供类的方法。
- **`enableDistinct()`**: 表示是否启用 distinct 检测。
- **`resultType()`**: 表示自定义查询结果的 Class 类型。该属性和 Fenix XML 文件中的 `resultType` 同义，后续推荐使用本属性来设置结果类型的 class，这样能更好的利用 Java 类的静态编译检查和代码重构后发现一些类路径修改所造成的隐患问题。
- **`resultTransformer()`**: 表示自定义查询结果的转换器的 Class 类型。为了保持向前兼容，默认的转换器仍然使用的是之前版本使用的 `FenixResultTransformer` 类的 Class，你也可以选择下划线或注解等方式的结果转换器。

## 🚠 二、@QueryFenix 注解使用简化 :id=simplified

之前的示例中 `@QueryFenix("BlogRepository.queryMyBlogs")` 注解的内容分别代表 XML 文件对应的命名空间 `namespace` 和 XML 标签的 `id` 属性值。如果你将 XML 文件中的 `namespace` 写成 `BlogRepository.java` 的全路径名 `com.blinkfox.fenix.example.repository.BlogRepository`，那么 `@QueryFenix` 注解就可以再简化一些，只写对应的 `fenixId` 即可。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!-- 将命名空间 namespace 写成对应 BlogRepository 接口的全路径名. -->
<fenixs namespace="com.blinkfox.fenix.example.repository.BlogRepository">
    <!-- ... -->
</fenixs>
```

```java
// QueryFenix 注解只需要写 fenix id 即可.
@QueryFenix("queryMyBlogs")
Page<Blog> queryMyBlogs(@Param("ids") List<String> ids, @Param("blog") Blog blog, Pageable pageable);
```

从上面可以看出，查询的方法名 `queryMyBlogs` 和 XML 标签的 `id` 属性值也是一样的，那么在注解中就可以不用写 `id` 名了，

所以，查询的接口方法可以进一步简化为不需要元数据内容的 `@QueryFenix` 注解：

```java
@QueryFenix
Page<Blog> queryMyBlogs(@Param("ids") List<String> ids, @Param("blog") Blog blog, Pageable pageable);
```

## 🛰️ 三、自动的分页总记录数查询及自定义 :id=auto-paging

上面的分页查询，我们没有设置自定义的查询总记录数的语句，依然可以正常分页。是因为 Fenix 帮你将上面 `SELECT` 语句块的查询结果换成了 `count(*)`，来查询总记录数。由于 JPA 中的分页和排序参数是单独设置的，所以，查询总记录数的 JPQL 语句中也不会有 `Order By` 这样的片段。

当然，同原生的 `@Query` 注解一样你也可以在 `@QueryFenix` 注解中，通过 `countQuery()` 值来设置自定义的查询总记录数的 SQL。不过，`@QueryFenix` 注解中 `countQuery()` 的值应该是 Fenix XML 标签的 `id` 值。

通过前面将 `namespace` 设置成了全路径名之后，那么自定义总记录数的查询示例如下：

```xml
<!-- 这是自定义的查询博客总记录数的方法，这里使用另一种模板语法和插值语法来作演示和查询. -->
<fenix id="queryMyBlogsCount">
    SELECT
        count(*)
    FROM
        Blog AS b
    WHERE
        b.id in #{ids}
    AND b.author LIKE '%${blog.author}%'
</fenix>
```

接口方法的注解代码如下：

```java
// 这个时候，countQuery 的值必须跟你自定义的查询总记录数的 fenix id 值一样.
@QueryFenix(countQuery = "queryMyBlogsCount")
Page<Blog> queryMyBlogs(@Param("ids") List<String> ids, @Param("blog") Blog blog, Pageable pageable);
```

## 🚁 四、nativeQuery 原生 SQL :id=native-query

同原生的 `@Query` 注解一样，你也可以在 `@QueryFenix` 注解中，通过将 `nativeQuery()` 的值来设置为 `true`，来表示你的 JPQL 语句是将使用原生 SQL 查询。

## 🪐 五、使用 Java 代码拼接 SQL :id=java-sql

关于如何用 Java 代码来拼接动态 SQL，请参看[后续篇章](java/example)。
