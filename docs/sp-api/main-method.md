# API 方法

以下将介绍基于 `Specification` 的主要 API 方法。这些 API 都在 `FenixPredicateBuilder` 类中，用来链式生成 `Predicate` 的条件集合。

## 1. 比较匹配类型的方法

比较类型的方法是指等于、不等于、大于、大于等于、小于、小于等于等方法，使用方式也几乎相同。主要 API 如下：

### (1) 等于 (Equal)

> **注**：由于 `equals()` 方法是 Java Object 类自带的方法，为了将其与本方法区分开来，Fenix 中的等值匹配系列的方法取名为 `equal`。

```java
// 生成“与逻辑”的“等值匹配”的 Predicate 条件，如果没有 match 参数或者 match 值为 true 则生成该条件，否则不生成.
andEquals(String fieldName, Object value)
andEquals(String fieldName, Object value, boolean match)

// 生成“或逻辑”的“等值匹配”的 Predicate 条件，如果没有 match 参数或者 match 值为 true 则生成该条件，否则不生成.
orEquals(String fieldName, Object value)
orEquals(String fieldName, Object value, boolean match)

// 生成“与逻辑”的“不等值匹配”的 Predicate 条件，如果没有 match 参数或者 match 值为 true 则生成该条件，否则不生成.
andNotEquals(String fieldName, Object value)
andNotEquals(String fieldName, Object value, boolean match)

// 生成“或逻辑”的“不等值匹配”的 Predicate 条件，如果没有 match 参数或者 match 值为 true 则生成该条件，否则不生成.
orNotEquals(String fieldName, Object value)
orNotEquals(String fieldName, Object value, boolean match)
```

### (2) 大于 (GreaterThan)

```java
// 生成“与逻辑”的“大于匹配”的 Predicate 条件，如果没有 match 参数或者 match 值为 true 则生成该条件，否则不生成.
andGreaterThan(String fieldName, Object value)
andGreaterThan(String fieldName, Object value, boolean match)

// 生成“或逻辑”的“大于匹配”的 Predicate 条件，如果没有 match 参数或者 match 值为 true 则生成该条件，否则不生成.
orGreaterThan(String fieldName, Object value)
orGreaterThan(String fieldName, Object value, boolean match)
```

### (3) 大于等于 (GreaterThanEqual)

```java
// 生成“与逻辑”的“大于等于匹配”的 Predicate 条件，如果没有 match 参数或者 match 值为 true 则生成该条件，否则不生成.
andGreaterThanEqual(String fieldName, Object value)
andGreaterThanEqual(String fieldName, Object value, boolean match)

// 生成“或逻辑”的“大于等于匹配”的 Predicate 条件，如果没有 match 参数或者 match 值为 true 则生成该条件，否则不生成.
orGreaterThanEqual(String fieldName, Object value)
orGreaterThanEqual(String fieldName, Object value, boolean match)
```

### (4) 小于 (LessThan)

```java
// 生成“与逻辑”的“小于匹配”的 Predicate 条件，如果没有 match 参数或者 match 值为 true 则生成该条件，否则不生成.
andLessThan(String fieldName, Object value)
andLessThan(String fieldName, Object value, boolean match)

// 生成“或逻辑”的“小于匹配”的 Predicate 条件，如果没有 match 参数或者 match 值为 true 则生成该条件，否则不生成.
orLessThan(String fieldName, Object value)
orLessThan(String fieldName, Object value, boolean match)
```

### (5) 小于等于 (andLessThanEqual)

```java
// 生成“与逻辑”的“小于等于匹配”的 Predicate 条件，如果没有 match 参数或者 match 值为 true 则生成该条件，否则不生成.
andLessThanEqual(String fieldName, Object value)
andLessThanEqual(String fieldName, Object value, boolean match)

// 生成“或逻辑”的“小于等于匹配”的 Predicate 条件，如果没有 match 参数或者 match 值为 true 则生成该条件，否则不生成.
orLessThanEqual(String fieldName, Object value)
orLessThanEqual(String fieldName, Object value, boolean match)
```

### (6) 使用示例

```java
@Test
public void testOrGreaterThanEqual() {
    // 测试等于、大于等于混用的场景.
    int totalPage = (Integer) paramMap.get("totalPage");
    List<Book> books = bookRepository.findAll(FenixSpecification.of(builder ->
            builder.andEquals("id", paramMap.get("id"))
                    .orGreaterThanEqual("totalPage", totalPage)
                    .build()));
    Assert.assertEquals(6, books.size());

    // 测试等于、大于等于混用的场景，但大于等于条件不生成.
    String isbn = (String) paramMap.get("isbn");
    List<Book> books2 = bookRepository.findAll(FenixSpecification.of(builder ->
            builder.andEquals("isbn", isbn)
                    .orGreaterThanEqual("totalPage", totalPage, false)
                    .build()));
    Assert.assertEquals(1, books2.size());

    // 测试等于、大于等于混用的场景，但等于条件不生成.
    List<Book> books3 = bookRepository.findAll(FenixSpecification.of(builder ->
            builder.andEquals("isbn", isbn, false)
                    .orGreaterThanEqual("totalPage", totalPage, totalPage > 0)
                    .build()));
    Assert.assertEquals(5, books3.size());
    books3.forEach(book -> Assert.assertTrue(book.getTotalPage() >= PAGE));
}
```

## 2. 区间匹配的方法 (between)

区间匹配本质上也是比较匹配类型的特殊形式，API 参数上表现为匹配的边界值有两个（开始值和结束值），参数会多一个，且在某一个边界值为 null 时，会退化成大于等于或者小于等于的匹配条件。所以，这里单独拿出来作介绍说明。

### (1) API 方法

```java
// 生成“与逻辑”的“匹配区间”的 Predicate 条件，如果没有 match 参数或者 match 值为 true 则生成该条件，否则不生成.
andBetween(String fieldName, Object startValue, Object endValue)
andBetween(String fieldName, Object startValue, Object endValue, boolean match)

// 生成“或逻辑”的“匹配区间”的 Predicate 条件，如果没有 match 参数或者 match 值为 true 则生成该条件，否则不生成.
orBetween(String fieldName, Object startValue, Object endValue)
orBetween(String fieldName, Object startValue, Object endValue, boolean match)

// 生成“与逻辑”的“不匹配区间”的 Predicate 条件，如果没有 match 参数或者 match 值为 true 则生成该条件，否则不生成.
andNotBetween(String fieldName, Object startValue, Object endValue)
andNotBetween(String fieldName, Object startValue, Object endValue, boolean match)

// 生成“或逻辑”的“不匹配区间”的 Predicate 条件，如果没有 match 参数或者 match 值为 true 则生成该条件，否则不生成.
orNotBetween(String fieldName, Object startValue, Object endValue)
orNotBetween(String fieldName, Object startValue, Object endValue, boolean match)
```

### (2) 退化情况说明

- 当开始值或结束值均不为 `null` 时，会生成 `between ... and ...` 的区间条件，不发生退化；
- 当开始值不为 `null`，结束值为 `null` 时，会生成大于等于（`>=`）的条件，发生退化；
- 当开始值为 `null`，结束值不为 `null` 时，会生成小于等于（`<=`）的条件，发生退化；
- 当开始值或结束值均为 `null` 时，将直接抛出异常；

### (3) 使用示例

```java
@Test
public void testBetween() {
    // 测试区间匹配，开始值和结束值均不为 null 的情况.
    Integer minTotalPage = (Integer) paramMap.get("minTotalPage");
    Integer maxTotalPage = (Integer) paramMap.get("maxTotalPage");
    List<Book> books = bookRepository.findAll(FenixSpecification.of(builder ->
            builder.andBetween("totalPage", minTotalPage, maxTotalPage)
                    .build()));
    Assert.assertEquals(4, books.size());
    books.forEach(book ->
            Assert.assertTrue(book.getTotalPage() >= MIN_PAGE && book.getTotalPage() <= MAX_PAGE));

    // 测试区间匹配退化为大于等于匹配的情况，开始值不为 null，但结束值为 null 的情况.
    List<Book> books2 = bookRepository.findAll(FenixSpecification.of(builder ->
            builder.andBetween("totalPage", minTotalPage, null, minTotalPage != null)
                    .build()));
    Assert.assertEquals(8, books2.size());
    books2.forEach(book -> Assert.assertTrue(book.getTotalPage() >= MIN_PAGE));

    // 测试区间匹配退化为小于等于匹配的情况，开始值为 null，但结束值不为 null 的情况.
    List<Book> books3 = bookRepository.findAll(FenixSpecification.of(builder ->
            builder.andBetween("totalPage", null, maxTotalPage)
                    .build()));
    Assert.assertEquals(6, books3.size());
    books3.forEach(book -> Assert.assertTrue(book.getTotalPage() <= MAX_PAGE));

    // 测试 match 为 false 时，不生成区间查询的示例.
    List<Book> books4 = bookRepository.findAll(FenixSpecification.of(builder ->
            builder.andBetween("totalPage", minTotalPage, maxTotalPage, false)
                    .build()));
    Assert.assertEquals(10, books4.size());
}
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
