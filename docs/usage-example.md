# 🍉 四种方式的使用示例 :id=title

以下的四种方式的示例均以之前的博客信息数据作为示例，你可以根据自己的场景或喜欢的方式来选择动态查询的方式。

## ✈️ 第一种：基于 JPQL (或 SQL) 的 XML 方式 :id=xml-sql

在 `BlogRepository` 中的查询方法使用 `QueryFenix` 注解，用来分页查询博客信息数据：

```java
public interface BlogRepository extends JpaRepository<Blog, String> {

    /**
     * 使用 {@link QueryFenix} 注解来演示根据博客信息Bean(可以是其它Bean 或者 Map)来多条件模糊分页查询博客信息.
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

    <!-- 这是一条完整的 Fenix 查询语句块，必须填写 fenix 标签的 id 属性，建议与方法名相同，这样注解中不用再单独设置了. -->
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

## 🚢 第二种：基于 JPQL (或 SQL) 的 Java API 方式 :id=java-sql

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

## ⛵ 第三种：基于 Specification 的 Java API 方式 :id=specification-java-api

基于 `Specification` 的方式，只须要 `BlogRepository` 接口继承 `FenixJpaSpecificationExecutor` 接口即可，当然也可以继承原生的 `JpaSpecificationExecutor` 接口亦可，但更建议直接继承 `FenixJpaSpecificationExecutor` 接口，该接口也继承自 `JpaSpecificationExecutor` 接口，但提供了更多的默认接口方法，且在 API 使用上，可以不用写 `FenixSpecification.of()` 的中间层，更为简单直接。

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

## 🪂 第四种：基于 Specification 的 Java Bean 注解方式 :id=specification-java-bean

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
