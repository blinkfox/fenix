# 🍬 更多功能

Fenix 中除了上面介绍的一些功能之外，还有其他额外的辅助、简化开发的功能，以下作简要介绍。

## 🦋 一、返回自定义的实体对象

### 📡 1. 初衷

JPA 本身支持通过“[投影](https://docs.spring.io/spring-data/jpa/docs/2.2.0.RELEASE/reference/html/#projections)”的方式来返回自定义的实体对象，但使用起来不那么“自然”。主要原因是：

- **基于接口的投影**需要创建的是查询结果的接口，接口中的方法是各个结果列属性的 `Getter` 方法，这样查询的结果就是这个接口的匿名实例或实例的集合，并非真正意义上的 `Java Bean`。
- **基于类的投影**创建的是一个实体类（`Java Bean`），但必须保证该类中含有查询结果列的构造方法，且还比须保证查询结果列与构造方法参数的顺序要一一对应，增加了后期维护的难度。而且该方式的 JPQL 语句必须使用 `new com.xxx.BlogDto(...)` 形式，比较奇怪，而且不能用于原生 SQL 的场景。

基于以上原因，Fenix 从 `v1.1.0` 版本开始新增了更加简单、自然的方式来返回自定义的实体对象（`Java Bean`）。下面依然通过 XML 和 Java 两种情况来做示例演示和说明。

### 🩸 2. XML 中的使用示例

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

> **💡 注**：
> 1. 上面的代码关键之处，就在 fenix 节点中配置了 `resultType` 属性的值为我们定义的**实体类的全路径名** `com.blinkfox.fenix.vo.UserBlogInfo`。这样查询结果返回的时候就能自动识别并返回了。
> 2. 另一个要点是所有查询列**都必须使用 as**来返回一个“别名”，且**这个“别名”必须跟实体类的属性名一致，不区分大小写**。
> 3. 此种方式再运行时可能会与 `spring-boot-devtools.jar` 冲突，报 `No converter found capable of converting from type [xxx] to type [xxx]` 错误。建议不使用 `spring-boot-devtools.jar`。
> 4. 在 Fenix `v2.3.1` 及之前的版本，**在异步多线程情况下，可能出现 JDBC 连接无法释放的问题**，强烈建议你升级 Fenix 版本为 `v2.3.3` 及之后的版本。

### 💊 3. Java 中的使用示例

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

## 🐛 二、调试 (debug) 模式

从 `2.4.1` 版本开始，新增回了 debug 调试模式的功能，也就是可以在不重启服务的情况下，每一条对 XML SQL 的请求，都会实时从 XML 文件流中读取和解析 SQL 语句。

> **💡 注**：其实该功能在 `1.0.0` 版本中就有，只不过当时的 IDEA 在识别更新的文件时有些问题，导致该功能没有啥效果，所以就去掉了。现在又将此功能加了回来，更加方便开发人员快速、动态的调试 SQL 语句了。

### 📝 1. 开启 debug 模式

如果你是使用的 `fenix-spring-boot-starter`，那么只需要在你的 `application.yml` 或者 `application.properties` 文件中，将 `fenix.debug` 设置为 `true` 即可。

```yaml
fenix:
  # v2.4.1 版本新增，表示是否开启 debug 调试模式，默认 false。
  # 当开启之后，对 XML 中的 SQL 会进行实时文件流的读取和解析，不需要重启服务。切记仅在开发环境中开启此功能.
  debug: true
```

如果不是使用的 `fenix-spring-boot-starter`，那么需要在你构造的 `FenixConfig` 实例对象中设置 `setDebug` 为 `true` 即可。

### 🧬 2. IDEA 中开启热更新策略

当然，还需要你开发使用的 IDE 能在服务运行期间动态识别到文件资源的更新，不同的 IDE 工具支持可能有差别。

如果你使用的是 Intellij IDEA，那么可以通过 `Edit Configurations` 编辑应用运行时的更新策略。你可以将 `On 'update' action` 和 `On frame deactivation` 的值设置为：`Update classes and resources`。设置的结果效果图如下：

![IDEA 中的资源更新的设置](https://statics.sh1a.qingstor.com/2021/01/02/idea-update.png)

## 🦗 三、从 XML 中获取 SQL 信息

Fenix 中会自动从 `XML` 中获取到 SQL 信息。如果你想手动从 `XML` 中获取到 SQL 信息（`SqlInfo`），也可以使用 `Fenix.java` 提供的 `API` 来获取。

```java
// 通过传入完整的 fullFenixId（命名空间、'.'号和 Fenix 节点的 ID）和上下文参数，来简单快速的生成和获取 SqlInfo 信息.
Fenix.getXmlSqlInfo(String fullFenixId, Object context)

// 通过传入 Fenix XML 文件对应的命名空间、Fenix 节点的 ID 以及上下文参数对象，来生成和获取 SqlInfo 信息.
Fenix.getXmlSqlInfo(String namespace, String fenixId, Object context)
```

## 🐜 四、表达式、模版解析器

在 Fenix 中解析 XML 标签中的表达式或者模版是通过 `Mvel` 表达式语言来实现的，主要方法解析方法是封装在了`ParseHelper.java` 的工具类中，通过该类让开发人员自己测试表达式也是极为方便的。以下作简要介绍。

### 💉 1. 解析表达式

#### 主要方法

```java
// 解析出表达式的值，如果解析出错则不抛出异常，但会输出 error 级别的异常，返回 null.
Object parseExpress(String exp, Object paramObj);

// 解析出表达式的值，如果解析出错则抛出异常.
Object parseExpressWithException(String exp, Object paramObj);
```

#### 使用示例

```java
@Test
public void testParseWithMvel() {
    // 构造上下文参数
    Map<String, Object> context = new HashMap<String, Object>();
    context.put("foo", "Hello");
    context.put("bar", "World");

    // 解析得到 'HelloWorld' 字符串，断言为: true.
    String result = (String) ParseHelper.parseExpressWithException("foo + bar", context);
    assertEquals("HelloWorld", result);
}

@Test
public void testParseStr2() {
    Boolean result = (Boolean) ParseHelper.parseExpress("sex == 1", ParamWrapper.newInstance("sex", "1").toMap());

    // 断言为: true.
    assertEquals(true, result);
}
```

### 🩺 2. 解析模版

#### 主要方法

```java
// 解析出模板字符串中的值，如果解析出错则抛出异常.
String parseTemplate(String template, Object context)
```

#### 使用示例

```java
@Test
public void testParseTemplate2() {
    String result = ParseHelper.parseTemplate("@if{?foo != empty}@{foo} World!@end{}",
            ParamWrapper.newInstance("foo", "Hello").toMap());

    // 解析得到 'Hello World!' 字符串，断言为:true.
    assertEquals("Hello World!", result);
}
```

## 🐝 五、上下文参数包装类

Fenix 中提供了一个包装上下文参数为 `HashMap` 的包装器 `ParamWrapper` 工具类，其本质上就是对 `HashMap` 方法的一个**简单链式封装**。

> **💡 注**：提供该包装器类的主要目的是方便开发者封装较多的散参数或者多个 Java 对象为一个 `Map` 型的上下文参数。

### 🛏️ 1. ParamWrapper主要方法

- `newInstance()`，创建新的`ParamWrapper`实例。
- `newInstance(Map<String, Object> paramMap)`，传入已有的`Map`型对象，并创建新的`ParamWrapper`实例。
- `newInstance(String key, Object value)`，创建新的`ParamWrapper`实例，并创建一对key和value的键值对。
- `put(String key, Object value)`，向参数包装器中，`put`对应的key和value值。
- `toMap()`，返回填充了key、value后的Map对象。

### 🪑 2. 对比的示例

以前需要开发者自己封装Map：

```java
Map<String, Object> context = new HashMap<String, Object>();
context.put("sex", "1");
context.put("stuId", "123");
```

现在的使用方式：

```java
Map<String, Object> context = ParamWrapper.newInstance("sex", "1").put("stuId", "123").toMap());
```

前后对比来看，再仅仅只需要传入个别自定义参数时，能简化部分代码量和参数传递。

## 🐞 六、表达式的真假判断

Fenix 中关于表达式字符串的真假判断在 `com.blinkfox.fenix.helper.ParseHelper` 类中提供了静态方法。

**主要方法**：

```java
// 是否匹配，常用于标签中的 match 值的解析，即如果 match 不填写，或者内容为空，或者解析出为正确的值，都视为true.
ParseHelper.isMatch(String match, Object context)

// 是否不匹配，同 isMatch 相反，只有解析到的值是 false 时，才认为是 false.
ParseHelper.isNotMatch(String match, Object context)

// 是否为 true，只有当解析值确实为 true 时，才为 true.
ParseHelper.isTrue(String exp, Object context)
```
