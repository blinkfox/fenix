# 🦋返回自定义的实体对象 :id=return-custom-entity

Fenix 中除了上面介绍的一些功能之外，还有其他额外的辅助、简化开发的功能，以下对返回自定义实体对象的功能作简要介绍。

## 📡 一、初衷 :id=intention

JPA 本身支持通过“[投影](https://docs.spring.io/spring-data/jpa/docs/2.2.0.RELEASE/reference/html/#projections)”的方式来返回自定义的实体对象，但使用起来不那么“自然”。主要原因是：

- **基于接口的投影**需要创建的是查询结果的接口，接口中的方法是各个结果列属性的 `Getter` 方法，这样查询的结果就是这个接口的匿名实例或实例的集合，并非真正意义上的 `Java Bean`。
- **基于类的投影**创建的是一个实体类（`Java Bean`），但必须保证该类中含有查询结果列的构造方法，且还比须保证查询结果列与构造方法参数的顺序要一一对应，增加了后期维护的难度。而且该方式的 JPQL 语句必须使用 `new com.xxx.BlogDto(...)` 形式，比较奇怪，而且不能用于原生 SQL 的场景。

基于以上原因，Fenix 从 `v1.1.0` 版本开始新增了更加简单、自然的方式来返回自定义的实体对象（`Java Bean`）。下面依然通过 XML 和 Java 两种情况来做示例演示和说明。

## 🩸 二、XML 中的使用示例 :id=used-in-xml

首先，定义一个自定义的数据传输实体用户博客信息类（DTO） `UserBlogInfo.java`，用来作为查询的返回结果，各属性请保证必须至少含有可公开访问的 `Setter` 方法：

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

在 `BlogRepository.java` 接口中定义查询的接口方法，接口返回的是我们刚才定义的 `UserBlogInfo` 分页信息：

```java
/**
 * 使用 {@link QueryFenix} 注解来连表模糊查询自定义的用户博客实体分页信息.
 *
 * @param userId 用户ID
 * @param blog 博客实体信息
 * @return 用户博客信息集合
 */
@QueryFenix("BlogRepository.queryUserBlogsWithFenixResultType")
Page<UserBlogInfo> queryUserBlogPageWithFenixResultType(@Param("userId") String userId, @Param("blog") Blog blog, Pageable pageable);
```

然后，在 `BlogRepository.xml` 的 XML 文件中，书写 SQL 语句，通过 `resultType` 来额外表明返回的结果为我们刚才自定义的实体类：

```xml
<!-- 根据用户ID、博客信息查询该用户发表的用户博客信息（用于测试返回自定义的实体信息）. -->
<fenix id="queryUserBlogsWithFenixResultType" resultType="com.blinkfox.fenix.vo.UserBlogInfo">
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

- 🔸 上面的代码关键就在 fenix 节点中配置了 `resultType` 属性的值为我们定义的**实体类的全路径名** `com.blinkfox.fenix.vo.UserBlogInfo`。这样查询结果返回的时候就能自动识别并返回了。
- 🔸 另一个要点是所有查询列**都必须使用 as**来返回一个“别名”，且**这个“别名”必须跟实体类的属性名一致，不区分大小写**。
- 🔸 此种方式再运行时可能会与 `spring-boot-devtools.jar` 冲突，报 `No converter found capable of converting from type [xxx] to type [xxx]` 错误。建议不使用 `spring-boot-devtools.jar`。
- 🔸 在 Fenix `v2.3.1` 及之前的版本，**在异步多线程情况下，可能出现 JDBC 连接无法释放的问题**，强烈建议你升级 Fenix 版本为 `v2.3.3` 及之后的版本。

## 💊 三、Java 中的使用示例 :id=used-in-java

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
                .end()
                .setResultTypeClass(UserBlogInfo.class);
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
@QueryFenix(provider = BlogSqlInfoProvider.class)
List<UserBlogInfo> queryUserBlogsWithFenixJava(@Param("userId") String userId, @Param("title") String title);
```
