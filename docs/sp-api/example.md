# 🍕 总体示例 :id=title

在 Fenix 中书写动态查询，也可以使用 Spring Data JPA 中提供的 `Specification` 方式，Fenix 中对此进行了动态查询能力的链式封装。

下面直接通过示例来说明如何使用。

## 📒 一、继承 FenixJpaSpecificationExecutor 接口 :id=extends-executor

在你的 `BlogRepository` 接口中，继承 `FenixJpaSpecificationExecutor` 接口，该接口实质上是继承自 `JpaSpecificationExecutor` 接口，但提供了更多的**默认接口方法**，且在 API 使用上，可以不用写 `FenixSpecification.of()` 的中间层，更为简单直接。

```java
public interface BlogRepository extends JpaRepository<Blog, String>, FenixJpaSpecificationExecutor<Blog> {

}
```

## 🏷️ 二、Service 中直接调用 :id=service-invoke

基于 `Specification` 的方式，不需要定义额外的查询方法，也不需要写 `JPQL` (或 SQL) 语句，简单直接。

接下来在你的 `service` 方法中，直接调用 `findOne`、`findAll`、`count` 等方法即可。

```java
// params 是 Map 型的参数，供查询时判断或传参使用.
Object[] ids = (Object[]) params.get("ids");
List<Blog> blogs = blogRepository.findAll(builder ->
        builder.andIn("id", ids, ids != null && ids.length > 0)
                .andLike("title", params.get("title"), params.get("title") != null)
                .andLike("author", params.get("author"))
                .andBetween("createTime", params.get("startTime"), params.get("endTime"))
        .build());
```

> **💡 注**：`andIn`、`andLike`、`andBetween` 等方法均提供了 `boolean` 型的 `match` 匹配参数，用来表示是否生成此条件，从而达到动态的能力。当然 Fenix 中还提供了更多的动态查询条件的 API，且囊括了 `and`、`or`、`andNot`、`orNot` 等绝大多数情形。

以上就是基于 `Specification` 通过 Java 链式 `API` 来动态查询的简单使用示例。
