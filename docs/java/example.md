# 🍗 总体示例 :id=title

在 Fenix 中书写动态 SQL，也可以使用 Java 链式 `API` 代码来完成，使用方式和 `MyBatis` 的方式总体相似，但是 Fenix 的 Java 动态 SQL 的 `API` 更为强大，且大多数方法同 `XML` 中 SQL 语义化标签相对应。

下面直接通过示例来说明如何使用。

## 🎠 一、创建提供 SqlInfo 的方法 :id=provider-method

创建一个 `BlogSqlProvider` 类，在类中创建一个返回 `SqlInfo` 示例的方法，这里作为示例我取名为 `queryBlogsWithJava`，方法参数最好与 `BlogRepository` 接口中的查询方法保持一致，并用 `@Param` 注解来描述，方法的参数顺序可以调换，参数可以变少，但不应该增多（因为多余的参数值，也会是 `null`）。

```java
import com.blinkfox.fenix.bean.SqlInfo;
import com.blinkfox.fenix.core.Fenix;
import com.blinkfox.fenix.example.entity.Blog;
import com.blinkfox.fenix.helper.CollectionHelper;
import com.blinkfox.fenix.helper.StringHelper;

import java.util.Date;

import org.springframework.data.repository.query.Param;

/**
 * 博客 {@link SqlInfo} 信息的提供类.
 *
 * @author blinkfox on 2019-08-17.
 */
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

## 🎠 二、创建查询方法 :id=query-method

在 `BlogRepository.java` 接口中添加动态查询博客信息的方法，我这里也取名为 `queryBlogsWithJava`。注意，这里也使用到了 `@QueryFenix` 注解，但是注解元素用得是 `provider` 和 `method`。

```java
/**
 * 使用 {@link QueryFenix} 注解和 Java API 来拼接 SQL 的方式来查询博客信息.
 *
 * @param blog 博客信息实体
 * @param startTime 开始时间
 * @param endTime 结束时间
 * @param blogIds 博客 ID 集合
 * @return 用户信息集合
 */
@QueryFenix(provider = BlogSqlProvider.class, method = "queryBlogsWithJava")
List<Blog> queryBlogsWithJava(@Param("blog") Blog blog, @Param("startTime") Date startTime,
        @Param("endTime") Date endTime, @Param("blogIds") String[] blogIds);
```

**💡 `@QueryFenix` 注解的再解释**：

- `provider`：表示 `SqlInfo` 提供类的 `class`，如果使用 `Java` 方式的话，**必填**元素。示例中它的值就是我们前面创建的 `BlogSqlProvider.class`。
- `method`：表示 `SqlInfo` 提供类的方法名，值为方法名的字符串。**非必填**元素。如果不填写这个值，就默认视为提供的方法名就是 `repository` 接口的查询方法名。示例中它的值就是我们前面创建的 `queryBlogsWithJava` 方法。
- `countMethod()`：表示通过 Java 来拼接分页查询时查询总记录数 SQL 语句的提供类的方法名。

## 🎢 三、创建并执行单元测试 :id=unit-test

在 `BlogRepositoryTest` 单元测试类中，新增 `queryBlogsWithJava` 测试方法，代码如下：

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

执行该单元测试，断言通过。在测试运行过程中，将打印出 Fenix 运行过程中生成的 `JPQL` 信息：

```sql
------------------------------------------------------------ Fenix 生成的 SQL 信息 ---------------------------------------------------------
-------- SQL: SELECT b FROM Blog AS b WHERE b.id IN :b_id AND b.author LIKE :b_author AND b.createTime BETWEEN :b_createTime_start AND :b_createTime_end
----- Params: {b_createTime_end=Tue Oct 08 00:00:00 CST 2019, b_id=[1, 2, 3, 4, 5, 6, 7, 8], b_author=%ZhangSan%, b_createTime_start=Mon Apr 08 00:00:00 CST 2019}
-------------------------------------------------------------------------------------------------------------------------------------------
```

以上就是通过 Java 链式 `API` 来构造动态 SQL 的用法。
