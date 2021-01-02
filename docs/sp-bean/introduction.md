# 🥣 使用介绍 :id=title

在 Fenix 中基于 `Specification` 方式书写动态查询，也可以使用普通的 Java Bean，以及将 Bean 中的属性标注上对应匹配条件的注解，就可以将 Java Bean 作为参数传递来完成动态查询了。

## 🦖 一、总体示例 :id=example

下面直接通过示例来说明如何使用。

### 🔑 1. 继承 FenixJpaSpecificationExecutor 接口 :id=extends-executor

同基于 `Specification` API 的方式一样，Java Bean 的注解方式也需要在你的 `BlogRepository` 接口中，继承 `FenixJpaSpecificationExecutor` 接口，该接口实质上是继承自 `JpaSpecificationExecutor` 接口，但提供了更多的**默认接口方法**，且在 API 使用上，可以不用写 `FenixSpecification.of()` 的中间层，更为简单直接。

```java
public interface BlogRepository extends FenixJpaSpecificationExecutor<Blog> {

}
```

### 🔐 2. 定义动态查询的 Java Bean 类和查询条件的注解 :id=annotation

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

### 🗝️ 3. 调用方法和测试 :id=invoke-and-run

基于 `Specification` 的方式，不需要定义额外的查询方法，也不需要写 `JPQL` (或 SQL) 语句，简单直接。

接下来在你的 `service` 方法或单元测试中，直接调用 `findOne`、`findAll`、`count` 等方法即可。

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

以上就是基于 `Specification` 通过 Java Bean 的条件注解来动态查询的简单使用示例。

## 🦕 二、自定义属性注解条件的生成匹配逻辑 :id=custom-match

匹配 Java Bean 中的属性是否生成对应的条件，默认是通过属性的值是否为 `空` 来识别的。

`空`的判断逻辑有以下几种：

- 🔹 普通 Java 对象是 `null`；
- 🔹 字符串是否是“大空字符串”，即 `isBlank` 的逻辑；
- 🔹 数组或集合是否是 `null` 或内容为空；

**某个对象如果符合以上逻辑，就表明是 `空` 的，就说明该属性标注的注解字段不会生成对应的查询条件，只有当该属性的值不为 `空` 时才生成。注意：如果属性是原始类型，如：`int`、`long` 等，由于他们没有 `null`，永远不会认为是 `空` 值，所以，建议你的 Java Bean 的对象中都使用包装类型 `Integer`、`Long` 等语义更为明确的类型，因为 `null` 和 `0` 值在特殊场合的含义不尽相同。**。

由于 Fenix 中的默认识别机制是对 `空` 的识别，那么如何才能自定义自己场景的注解条件生成识别逻辑呢？

要自定义生成匹配逻辑，也很简单，只需要在你的 Java Bean 类中**增加一个与属性名相同的、返回值为 boolean 类型的无参方法**即可。当该方法运行时返回结果为 `true` 时，表明就生成该属性注解对应的条件，否则不生成。

Java Bean 的自定义注解条件生成逻辑示例，见下面的 `id()` 方法，表明 `id` 值不等于空和不等于 `1` 时才生成 `id` 的等值匹配逻辑：

```java
@Getter
@Setter
@Accessors(chain = true)
public class BookMatch {

    /**
     * ID.
     */
    @Equals
    private String id;

    /**
     * ISBN.
     */
    @Equals
    private String isbn;

    /**
     * 区间查询页数的对象.
     * 该值的类型可以是 {@link BetweenValue} 类型，也可以是数组或 {@link java.util.List} 类型，
     * 但需要保证数组或集合包含开始值或结束值，两个值不能同时为 {@code null}.
     */
    @OrBetween("totalPage")
    private BetweenValue<Integer> totalPageValue;

    /**
     * 判断 id 属性是否匹配，结果为真就生成这个匹配条件，否则不生成.
     * 这里假定 ID 不为空，且 ID 不等于 1 时才能生成查询条件.
     *
     * @return 布尔值，返回 true 时，表明生成 id 的 @Equals 注解条件，否则不生成.
     */
    public boolean id() {
        return id != null && !id.equals("1");
    }

}
```
