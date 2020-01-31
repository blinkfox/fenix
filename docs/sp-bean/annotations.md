# 内置的条件注解

Fenix 中内置了大量的(约 `44` 个)条件注解（[点击这里查看](https://github.com/blinkfox/fenix/tree/develop/src/main/java/com/blinkfox/fenix/specification/annotation)），这些注解均作用在 Java Bean 的属性上。且这些注解的内部处理机制绝大多数都与**基于 Specification 的 Java 链式 API 方式**中的方法相对应，你可以相互对比使用和参考。

## 1. 比较匹配类型的注解

比较类型的注解是指等于、不等于、大于、大于等于、小于、小于等于等注解，使用方式也几乎相同，且大多都囊括了 `and`、`or`、`andNot`、`orNot` 等情况。

**以下的若干个注解，都仅有一个 `value()` 方法，表示数据库对应的实体类的属性字段名称（`fieldName`），如果不填写，则默认使用被标注的 Java Bean 的属性名作为 `fieldName`**。

**而被标注的属性值的，可以是任何可比较的类型，如：`String`、`Date`、`Integer` 等等。这些类型本质上都实现了 Java 中的 `Comparable` 接口**。

### (1) 等于 (Equal)

- `@Equals`: 表示使用标注的字段名称和属性值，生成“与逻辑”的“等值匹配”的 `Predicate` 条件；
- `@OrEquals`: 表示使用标注的字段名称和属性值，生成“或逻辑”的“等值匹配”的 `Predicate` 条件；
- `@NotEquals`: 表示使用标注的字段名称和属性值，生成“与逻辑”的“等值不匹配”的 `Predicate` 条件；
- `@OrNotEquals`: 表示使用标注的字段名称和属性值，生成“或逻辑”的“等值不匹配”的 `Predicate` 条件；

### (2) 大于 (GreaterThan)

- `@GreaterThan`: 表示使用标注的字段名称和属性值，生成“与逻辑”的“大于匹配”的 `Predicate` 条件；
- `@OrGreaterThan`: 表示使用标注的字段名称和属性值，生成“或逻辑”的“大于匹配”的 `Predicate` 条件；

### (3) 大于等于 (GreaterThanEqual)

- `@GreaterThanEqual`: 表示使用标注的字段名称和属性值，生成“与逻辑”的“大于等于匹配”的 `Predicate` 条件；
- `@OrGreaterThanEqual`: 表示使用标注的字段名称和属性值，生成“或逻辑”的“大于等于匹配”的 `Predicate` 条件；

### (4) 小于 (LessThan)

- `@LessThan`: 表示使用标注的字段名称和属性值，生成“与逻辑”的“小于匹配”的 `Predicate` 条件；
- `@OrLessThan`: 表示使用标注的字段名称和属性值，生成“或逻辑”的“小于匹配”的 `Predicate` 条件；

### (5) 小于等于 (LessThanEqual)

- `@LessThanEqual`: 表示使用标注的字段名称和属性值，生成“与逻辑”的“小于等于匹配”的 `Predicate` 条件；
- `@OrLessThanEqual`: 表示使用标注的字段名称和属性值，生成“或逻辑”的“小于等于匹配”的 `Predicate` 条件；

### (6) 使用示例

```java
/**
 * 图书的 ISBN 编号. 注解中没有填写 `value()` 值，将默认使用 `isbn` 作为数据库关联的实体字段名称.
 */
@Equals
private String isbn;

/**
 * 图书总页数. 注解中填写了 `value()` 的值为 `totalPage`，将使用 `totalPage` 作为数据库关联的实体字段名称.
 */
@OrGreaterThan("totalPage")
private Integer bookTotalPage;
```

## 2. 区间匹配的方法 (between)

区间匹配本质上也是比较匹配类型的特殊形式，不同点在于标注注解的属性类型至少需要两个边界值（开始值和结束值）来表达，且在某一个边界值为 null 时，会退化成大于等于或者小于等于的匹配条件。所以，这里单独拿出来作介绍说明。

### (1) 区间注解

- `@Between`: 表示使用标注的字段名称和属性值，生成“与逻辑”的“等值匹配”的 `Predicate` 条件；
- `@OrBetween`: 表示使用标注的字段名称和属性值，生成“或逻辑”的“等值匹配”的 `Predicate` 条件；
- `@NotBetween`: 表示使用标注的字段名称和属性值，生成“与逻辑”的“等值不匹配”的 `Predicate` 条件；
- `@OrNotBetween`: 表示使用标注的字段名称和属性值，生成“或逻辑”的“等值不匹配”的 `Predicate` 条件；

**上面的四个个注解，也都仅有一个 `value()` 方法，表示数据库对应的实体类的属性字段名称（`fieldName`），如果不填写，则默认使用被标注的 Java Bean 的属性名作为 `fieldName`**。

**而被标注的属性值的类型，必须是任何可比较的类型，如：`String`、`Date`、`Integer` 等等。这些类型本质上都实现了 Java 中的 `Comparable` 接口**。为了表达开始值和结束值，值的类型只能是 `BetweenValue<T extends Comparable<T>>`、数组和 List 有序集合三种类型中的某一种才行。如果是数组或者集合，则必须保证第一个值表示的是开始值，第二个值表示的是结束值，其中某一个值可以为 `null`。而 `BetweenValue` 是 Fenxi 中提供的一个二元组类型，用来封装开始值和结束值的类型，**建议使用 `BetweenValue` 作为区间查询的属性值类型**。

### (2) 退化情况说明

- 当开始值或结束值均不为 `null` 时，会生成 `between ... and ...` 的区间条件，不发生退化；
- 当开始值不为 `null`，结束值为 `null` 时，会生成大于等于（`>=`）的条件，发生退化；
- 当开始值为 `null`，结束值不为 `null` 时，会生成小于等于（`<=`）的条件，发生退化；
- 当开始值或结束值均为 `null` 时，将直接抛出异常；

### (3) 使用示例

```java
/**
 * 用于总页数区间查询的数组.
 */
@Between("totalPage")
private BetweenValue<Integer> totalPageValue;

/**
 * 用于总页数区间查询的数组.
 */
@Between("totalPage")
private Integer[] totalPageArr;

/**
 * 用于总页数区间查询的数组.
 */
@OrNotBetween("totalPage")
private List<Integer> notTotalPages;
```

`BetweenValue` 的对象实例构造方式如下：

```java
// 定义了开始值和结束值的实例.
BetweenValue.of(300,600)

// 只定义了开始值的实例.
BetweenValue.ofStart(300)

// 只定义了结束值的实例.
BetweenValue.ofEnd(600)
```

## 3. 模糊匹配的方法 (LIKE)

Fenix 中的模糊匹配包含四种，分别是：

- 前后模糊匹配的 `Like`
- 前缀模糊匹配 `StartsWith`
- 后缀模糊匹配 `EndsWith`
- 以及 SQL 语法通用的任意自定义模式匹配的 `LikePattern`

### (1) 前后模糊匹配的 (Like)

```java
// 生成“与逻辑”的“前后模糊匹配”时的 Predicate 条件，如果没有 match 参数或者 match 值为 true 则生成该条件，否则不生成.
andLike(String fieldName, Object value)
andLike(String fieldName, Object value, boolean match)

// 生成“或逻辑”的“前后模糊匹配”时的 Predicate 条件，如果没有 match 参数或者 match 值为 true 则生成该条件，否则不生成.
orLike(String fieldName, Object value)
orLike(String fieldName, Object value, boolean match)

// 生成“与逻辑”的“前后模糊不匹配”时的 Predicate 条件，如果没有 match 参数或者 match 值为 true 则生成该条件，否则不生成.
andNotLike(String fieldName, Object value)
andNotLike(String fieldName, Object value, boolean match)

// 生成“或逻辑”的“前后模糊不匹配”时的 Predicate 条件，如果没有 match 参数或者 match 值为 true 则生成该条件，否则不生成.
orNotLike(String fieldName, Object value)
orNotLike(String fieldName, Object value, boolean match)
```

### (2) 前缀模糊匹配 (StartsWith)

```java
// 生成“与逻辑”的“前缀模糊匹配”时的 Predicate 条件，如果没有 match 参数或者 match 值为 true 则生成该条件，否则不生成.
andStartsWith(String fieldName, Object value)
andStartsWith(String fieldName, Object value, boolean match)

// 生成“或逻辑”的“前缀模糊匹配”时的 Predicate 条件，如果没有 match 参数或者 match 值为 true 则生成该条件，否则不生成.
orStartsWith(String fieldName, Object value)
orStartsWith(String fieldName, Object value, boolean match)

// 生成“与逻辑”的“前缀模糊不匹配”时的 Predicate 条件，如果没有 match 参数或者 match 值为 true 则生成该条件，否则不生成.
andNotStartsWith(String fieldName, Object value)
andNotStartsWith(String fieldName, Object value, boolean match)

// 生成“或逻辑”的“前缀模糊不匹配”时的 Predicate 条件，如果没有 match 参数或者 match 值为 true 则生成该条件，否则不生成.
orNotStartsWith(String fieldName, Object value)
orNotStartsWith(String fieldName, Object value, boolean match)
```

### (3) 后缀模糊匹配 (EndsWith)

```java
// 生成“与逻辑”的“后缀模糊匹配”时的 Predicate 条件，如果没有 match 参数或者 match 值为 true 则生成该条件，否则不生成.
andEndsWith(String fieldName, Object value)
andEndsWith(String fieldName, Object value, boolean match)

// 生成“或逻辑”的“后缀模糊匹配”时的 Predicate 条件，如果没有 match 参数或者 match 值为 true 则生成该条件，否则不生成.
orEndsWith(String fieldName, Object value)
orEndsWith(String fieldName, Object value, boolean match)

// 生成“与逻辑”的“后缀模糊不匹配”时的 Predicate 条件，如果没有 match 参数或者 match 值为 true 则生成该条件，否则不生成.
andNotEndsWith(String fieldName, Object value)
andNotEndsWith(String fieldName, Object value, boolean match)

// 生成“或逻辑”的“后缀模糊不匹配”时的 Predicate 条件，如果没有 match 参数或者 match 值为 true 则生成该条件，否则不生成.
orNotEndsWith(String fieldName, Object value)
orNotEndsWith(String fieldName, Object value, boolean match)
```

### (4) 自定义模式匹配 (LikePattern)

```java
// 生成“与逻辑”的“自定义模式匹配”时的 Predicate 条件，如果没有 match 参数或者 match 值为 true 则生成该条件，否则不生成.
andLikePattern(String fieldName, String pattern)
andLikePattern(String fieldName, String pattern, boolean match)

// 生成“或逻辑”的“自定义模式匹配”时的 Predicate 条件，如果没有 match 参数或者 match 值为 true 则生成该条件，否则不生成.
orLikePattern(String fieldName, String pattern)
orLikePattern(String fieldName, String pattern, boolean match)

// 生成“与逻辑”的“自定义模式不匹配”时的 Predicate 条件，如果没有 match 参数或者 match 值为 true 则生成该条件，否则不生成.
andNotLikePattern(String fieldName, String pattern)
andNotLikePattern(String fieldName, String pattern, boolean match)

// 生成“或逻辑”的“自定义模式不匹配”时的 Predicate 条件，如果没有 match 参数或者 match 值为 true 则生成该条件，否则不生成.
orNotLikePattern(String fieldName, String pattern)
orNotLikePattern(String fieldName, String pattern, boolean match)
```

### (5) 使用示例

```java
@Test
public void testOrLike() {
    // 测试生成 LIKE 查询条件的测试用例
    String name = (String) paramMap.get("name");
    List<Book> books = bookRepository.findAll(builder ->
            builder.orLike("name", name, StringHelper.isNotBlank(name)).build());
    Assert.assertEquals(3, books.size());
    books.forEach(book -> Assert.assertTrue(book.getName().contains(NAME)));

    // 测试不生成 LIKE 查询条件的测试用例
    List<Book> books2 = bookRepository.findAll(builder ->
            builder.andEquals("id", ID_2)
                    .orLike("name", name, false)
                    .build());
    Assert.assertEquals(1, books2.size());
    Assert.assertEquals(ID_2, books2.get(0).getId());
}
```

## 4. 范围匹配的方法 (IN)

范围匹配是指生成 `IN` 范围查条件。

### (1) API 方法

```java
// 生成“与逻辑”的“范围匹配”时的 Predicate 条件，如果没有 match 参数或者 match 值为 true 则生成该条件，否则不生成.
andIn(String fieldName, Collection<?> value)
andIn(String fieldName, Collection<?> value, boolean match)
andIn(String fieldName, Object[] value)
andIn(String fieldName, Object[] value, boolean match)

// 生成“或逻辑”的“范围匹配”时的 Predicate 条件，如果没有 match 参数或者 match 值为 true 则生成该条件，否则不生成.
orIn(String fieldName, Collection<?> value)
orIn(String fieldName, Collection<?> value, boolean match)
orIn(String fieldName, Object[] value)
orIn(String fieldName, Object[] value, boolean match)

// 生成“与逻辑”的“范围不匹配”时的 Predicate 条件，如果没有 match 参数或者 match 值为 true 则生成该条件，否则不生成.
andNotIn(String fieldName, Collection<?> value)
andNotIn(String fieldName, Collection<?> value, boolean match)
andNotIn(String fieldName, Object[] value)
andNotIn(String fieldName, Object[] value, boolean match)

// 生成“或逻辑”的“范围不匹配”时的 Predicate 条件，如果没有 match 参数或者 match 值为 true 则生成该条件，否则不生成.
orNotIn(String fieldName, Collection<?> value)
orNotIn(String fieldName, Collection<?> value, boolean match)
orNotIn(String fieldName, Object[] value)
orNotIn(String fieldName, Object[] value, boolean match)
```

### (2) 使用示例

```java
@Test
public void testIn() {
    List<String> idList = (List<String>) paramMap.get("idList");
    List<Book> books2 = bookRepository.findAll(builder ->
            builder.andIn("id", idList, CollectionHelper.isNotEmpty(idList)).build());

    // 断言查询结果的条数是否正确.
    Assert.assertEquals(7, books2.size());
}
```

## 5. NULL 匹配 (IS NULL)

### (1) API 方法

NULL 匹配是指 SQL 中的字段 `IS NULL` 或者 `IS NOT NULL`。主要 API 方法如下：

```java
// 生成“与逻辑”的“IS NULL”的 Predicate 条件，如果没有 match 参数或者 match 值为 true 则生成该条件，否则不生成.
andIsNull(String fieldName)
andIsNull(String fieldName, boolean match)

// 生成“或逻辑”的“IS NULL”的 Predicate 条件，如果没有 match 参数或者 match 值为 true 则生成该条件，否则不生成.
orIsNull(String fieldName)
orIsNull(String fieldName, boolean match)

// 生成“与逻辑”的“IS NOT NULL”的 Predicate 条件，如果没有 match 参数或者 match 值为 true 则生成该条件，否则不生成.
andIsNotNull(String fieldName)
andIsNotNull(String fieldName, boolean match)

// 生成“或逻辑”的“IS NOT NULL”的 Predicate 条件，如果没有 match 参数或者 match 值为 true 则生成该条件，否则不生成.
orIsNotNull(String fieldName)
orIsNotNull(String fieldName, boolean match)
```

### (2) 使用示例

```java
@Test
public void testIsNull() {
    List<Book> books = bookRepository.findAll(builder -> builder.andIsNull("others").build());

    // 断言查询结果的条数是否正确.
    Assert.assertEquals(7, books.size());
    books.forEach(book -> Assert.assertNull(book.getOthers()));
}
```

## 6. 自定义任意操作

Fenix 中仍然提供了让你自定义动态操作的 `doAny` 方法，该方法中需要传递 `AbstractPredicateHandler` 的子类对象，也可以传递 `PredicateHandler` 接口的实现类实例。

- 通常情况下，推荐直接使用 `PredicateHandler` 的匿名实现类的方式来完成，这样就可以简单的通过 `Lambda` 表达式来完成操作。
- 如果你的自定义操作，也想用于 Java Bean 条件注解的情况，那么建议你继承 `AbstractPredicateHandler` 抽象类即可。

### (1) API 方法

```java
// 通过传递 AbstractPredicateHandler 的子类对象来完成任意自定义操作，在具体实现类中写相关的动态条件拼接逻辑.
doAny(String fieldName, Object value, AbstractPredicateHandler handler)
doAny(String fieldName, Object value, AbstractPredicateHandler handler, boolean match)

// 通过传递 PredicateHandler 的实现类类对象来完成任意自定义操作，可以通过 Lambda 表达式来完成自定义的动态条件拼接逻辑.
doAny(String fieldName, Object value, PredicateHandler handler)
doAny(String fieldName, Object value, PredicateHandler handler, boolean match)
```

### (2) 示例

以下通过一个简单的 `PredicateHandler` 的 Lambda 表达式来完成字符串第二、三、四个字符分别是 `ava` 的匹配代码。当然，其实你可以直接通过 `andLikePattern` 方法达到目的，但这里我只是做一个演示的示例供你参考。

```java
/**
 * 测试使用 {@code Specification} 的方式来查询图书信息.
 */
@Test
public void testFindAllWithDoAny() {
    // 模拟动态查询，使用 doAny 来完成图书名称的第二、三、四个字符分别是 `ava` 的匹配代码.
    List<Book> books = bookRepository.findAll(builder ->
            builder.andEquals("isbn", paramMap.get("isbn"))
                    .orBetween("totalPage", paramMap.get("minTotalPage"), paramMap.get("maxTotalPage"))
                    .doAny("name", null,
                           (cb, from, fieldName, value) -> cb.or(cb.like(from.get(fieldName), "_ava%")), true)
                    .build());

    // 断言查询结果的正确性.
    Assert.assertEquals(6, books.size());
}
```

## 7. 获取 JPA 的 CriteriaBuilder 等对象

基于 `Specification` 的方式在构造动态查询条件的过程中，如果以上的诸多 API 方法仍然不满足你的需求，想通过原生的写法实现，Fenix 中也提供给你使用原生的方式。你可以在构建过程中获取到 Spring Data JPA 中的 `CriteriaBuilder`、`CriteriaQuery`、`From` 等对象实例，从而方便你来按你自己的需求写动态查询的代码。

```java
List<Book> books = bookRepository.findAll(builder -> {
        CriteriaBuilder criteriaBuilder = builder.getCriteriaBuilder();
        CriteriaQuery criteriaQuery = builder.getCriteriaQuery();
        From<?, ?> root = builder.getFrom();
        return builder.build();
});
```
