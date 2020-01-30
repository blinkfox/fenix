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

## 自定义任意操作

## 获取 JPA 的 CriteriaBuilder 等对象

基于 `Specification` 的方式在构造动态查询条件的过程中，如果以上的诸多 API 方法仍然不满足你的需求，想通过原生的写法实现，Fenix 中也提供给你使用原生的方式。你可以在构建过程中获取到 Spring Data JPA 中的 `CriteriaBuilder`、`CriteriaQuery`、`From` 等对象实例，从而方便你来按你自己的需求写动态查询的代码。

```java
List<Book> books = bookRepository.findAll(builder -> {
        CriteriaBuilder criteriaBuilder = builder.getCriteriaBuilder();
        CriteriaQuery criteriaQuery = builder.getCriteriaQuery();
        From<?, ?> root = builder.getFrom();
        return builder.build();
});
```