# 🥗 内置的条件注解 :id=title

Fenix 中内置了大量的(约 `44` 个)条件注解（[点击这里查看](https://github.com/blinkfox/fenix/tree/develop/src/main/java/com/blinkfox/fenix/specification/annotation)），这些注解均作用在 Java Bean 的属性上。且这些注解的内部处理机制绝大多数都与**基于 Specification 的 Java 链式 API 方式**中的方法相对应，你可以相互对比使用和参考。

## 🐳 一、比较匹配类型的注解 :id=compare-annotations

比较类型的注解是指等于、不等于、大于、大于等于、小于、小于等于等注解，使用方式也几乎相同，且大多都囊括了 `and`、`or`、`andNot`、`orNot` 等情况。

**以下的若干个注解，都仅有一个 `value()` 方法，表示数据库对应的实体类的属性字段名称（`fieldName`），如果不填写，则默认使用被标注的 Java Bean 的属性名作为 `fieldName`**。

**而被标注的属性值，可以是任何可比较的类型，如：`String`、`Date`、`Integer` 等等。这些类型本质上都实现了 Java 中的 `Comparable` 接口**。

### 🔨 1. 等于 (@Equals) :id=equals

- `@Equals`: 表示使用标注的字段名称和属性值，生成“与逻辑”的“等值匹配”的 `Predicate` 条件；
- `@OrEquals`: 表示使用标注的字段名称和属性值，生成“或逻辑”的“等值匹配”的 `Predicate` 条件；
- `@NotEquals`: 表示使用标注的字段名称和属性值，生成“与逻辑”的“等值不匹配”的 `Predicate` 条件；
- `@OrNotEquals`: 表示使用标注的字段名称和属性值，生成“或逻辑”的“等值不匹配”的 `Predicate` 条件；

### 🪓 2. 大于 (@GreaterThan) :id=greater-than

- `@GreaterThan`: 表示使用标注的字段名称和属性值，生成“与逻辑”的“大于匹配”的 `Predicate` 条件；
- `@OrGreaterThan`: 表示使用标注的字段名称和属性值，生成“或逻辑”的“大于匹配”的 `Predicate` 条件；

### ⛏️ 3. 大于等于 (@GreaterThanEqual) :id=greater-than-equal

- `@GreaterThanEqual`: 表示使用标注的字段名称和属性值，生成“与逻辑”的“大于等于匹配”的 `Predicate` 条件；
- `@OrGreaterThanEqual`: 表示使用标注的字段名称和属性值，生成“或逻辑”的“大于等于匹配”的 `Predicate` 条件；

### 🛠️ 4. 小于 (@LessThan) :id=less-than

- `@LessThan`: 表示使用标注的字段名称和属性值，生成“与逻辑”的“小于匹配”的 `Predicate` 条件；
- `@OrLessThan`: 表示使用标注的字段名称和属性值，生成“或逻辑”的“小于匹配”的 `Predicate` 条件；

### 🗡️ 5. 小于等于 (@LessThanEqual) :id=less-than-equal

- `@LessThanEqual`: 表示使用标注的字段名称和属性值，生成“与逻辑”的“小于等于匹配”的 `Predicate` 条件；
- `@OrLessThanEqual`: 表示使用标注的字段名称和属性值，生成“或逻辑”的“小于等于匹配”的 `Predicate` 条件；

### ⚔️ 6. 使用示例 :id=demo

```java
/**
 * 图书的 ISBN 编号.
 * 注解中没有填写 `value()` 值，将默认使用字符串 `isbn` 作为数据库关联的实体字段名称.
 */
@Equals
private String isbn;

/**
 * 图书总页数.
 * 注解中填写了 `value()` 的值为 `totalPage`，将使用 `totalPage` 作为数据库关联的实体字段名称.
 */
@OrGreaterThan("totalPage")
private Integer bookTotalPage;
```

## 🐬 二、区间匹配的注解 (@Between) :id=between

区间匹配本质上也是比较匹配类型的特殊形式，不同点在于标注注解的属性类型至少需要两个边界值（开始值和结束值）来表达，且在某一个边界值为 null 时，会退化成大于等于或者小于等于的匹配条件。所以，这里单独拿出来作介绍说明。

### 🔫 1. 注解介绍 :id=between-annotation

- `@Between`: 表示使用标注的字段名称和属性值，生成“与逻辑”的“等值匹配”的 `Predicate` 条件；
- `@OrBetween`: 表示使用标注的字段名称和属性值，生成“或逻辑”的“等值匹配”的 `Predicate` 条件；
- `@NotBetween`: 表示使用标注的字段名称和属性值，生成“与逻辑”的“等值不匹配”的 `Predicate` 条件；
- `@OrNotBetween`: 表示使用标注的字段名称和属性值，生成“或逻辑”的“等值不匹配”的 `Predicate` 条件；

**上面的四个个注解，也都仅有一个 `value()` 方法，表示数据库对应的实体类的属性字段名称（`fieldName`），如果不填写，则默认使用被标注的 Java Bean 的属性名作为 `fieldName`**。

**而被标注的属性值的类型，必须是任何可比较的类型，如：`String`、`Date`、`Integer` 等等。这些类型本质上都实现了 Java 中的 `Comparable` 接口**。为了表达开始值和结束值，值的类型只能是 `BetweenValue<T extends Comparable<T>>`、数组和 List 有序集合三种类型中的某一种才行。如果是数组或者集合，则必须保证第一个值表示的是开始值，第二个值表示的是结束值，其中某一个值可以为 `null`。而 `BetweenValue` 是 Fenxi 中提供的一个二元组类型，用来封装开始值和结束值的类型，**建议使用 `BetweenValue` 作为区间查询的属性值类型**。

### 🛡️ 2. 退化情况说明 :id=between-introduction

- 🔹 当开始值或结束值均不为 `null` 时，会生成 `between ... and ...` 的区间条件，不发生退化；
- 🔹 当开始值不为 `null`，结束值为 `null` 时，会生成大于等于（`>=`）的条件，发生退化；
- 🔹 当开始值为 `null`，结束值不为 `null` 时，会生成小于等于（`<=`）的条件，发生退化；
- 🔹 当开始值或结束值均为 `null` 时，将直接抛出异常；

### 🔧 3. 使用示例 :id=between-demo

```java
/**
 * 用于总页数区间查询的数组.
 * 注解中填写了 `value()` 的值为 `totalPage`，将使用 `totalPage` 作为数据库关联的实体字段名称.
 */
@Between("totalPage")
private BetweenValue<Integer> totalPageValue;

/**
 * 用于总页数区间查询的数组.
 * 注解中填写了 `value()` 的值为 `totalPage`，将使用 `totalPage` 作为数据库关联的实体字段名称.
 */
@Between("totalPage")
private Integer[] totalPageArr;

/**
 * 用于总页数区间查询的数组.
 * 注解中填写了 `value()` 的值为 `totalPage`，将使用 `totalPage` 作为数据库关联的实体字段名称.
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

## 🐠 三、模糊匹配的注解 :id=like-annotations

Fenix 中的模糊匹配包含四种，分别是：

- 🔹 前后模糊匹配的 `Like`
- 🔹 前缀模糊匹配 `StartsWith`
- 🔹 后缀模糊匹配 `EndsWith`
- 🔹 以及 SQL 语法通用的任意自定义模式匹配的 `LikePattern`

**以下的若干个注解，都仅有一个 `value()` 方法，表示数据库对应的实体类的属性字段名称（`fieldName`），如果不填写，则默认使用被标注的 Java Bean 的属性名作为 `fieldName`。而被标注的属性值的，通常是字符串的类型**。

### 🔩 1. 前后模糊匹配的注解 (@Like) :id=like

- 🔹 `@Like`: 表示使用标注的字段名称和属性值，生成“与逻辑”的“前后模糊匹配”的 `Predicate` 条件；
- 🔹 `@OrLike`: 表示使用标注的字段名称和属性值，生成“或逻辑”的“前后模糊匹配”的 `Predicate` 条件；
- 🔹 `@NotLike`: 表示使用标注的字段名称和属性值，生成“与逻辑”的“前后模糊不匹配”的 `Predicate` 条件；
- 🔹 `@OrNotLike`: 表示使用标注的字段名称和属性值，生成“或逻辑”的“前后模糊不匹配”的 `Predicate` 条件；

### ⚙️ 2. 前缀模糊匹配的注解 (@StartsWith) :id=starts-with

- 🔹 `@StartsWith`: 表示使用标注的字段名称和属性值，生成“与逻辑”的“前缀模糊匹配”的 `Predicate` 条件；
- 🔹 `@OrStartsWith`: 表示使用标注的字段名称和属性值，生成“或逻辑”的“前缀模糊匹配”的 `Predicate` 条件；
- 🔹 `@NotStartsWith`: 表示使用标注的字段名称和属性值，生成“与逻辑”的“前缀模糊不匹配”的 `Predicate` 条件；
- 🔹 `@OrNotStartsWith`: 表示使用标注的字段名称和属性值，生成“或逻辑”的“前缀模糊不匹配”的 `Predicate` 条件；

### ⚖️ 3. 后缀模糊匹配的注解 (@EndsWith) :id=ends-with

- 🔹 `@EndsWith`: 表示使用标注的字段名称和属性值，生成“与逻辑”的“后缀模糊匹配”的 `Predicate` 条件；
- 🔹 `@OrEndsWith`: 表示使用标注的字段名称和属性值，生成“或逻辑”的“后缀模糊匹配”的 `Predicate` 条件；
- 🔹 `@NotEndsWith`: 表示使用标注的字段名称和属性值，生成“与逻辑”的“后缀模糊不匹配”的 `Predicate` 条件；
- 🔹 `@OrNotEndsWith`: 表示使用标注的字段名称和属性值，生成“或逻辑”的“后缀模糊不匹配”的 `Predicate` 条件；

### 🔗 4. 自定义模式匹配的注解 (@LikePattern) :id=like-pattern

- 🔹 `@EndsWith`: 表示使用标注的字段名称和属性值，生成“与逻辑”的“自定义模式匹配”的 `Predicate` 条件；
- 🔹 `@OrEndsWith`: 表示使用标注的字段名称和属性值，生成“或逻辑”的“自定义模式匹配”的 `Predicate` 条件；
- 🔹 `@NotEndsWith`: 表示使用标注的字段名称和属性值，生成“与逻辑”的“自定义模式不匹配”的 `Predicate` 条件；
- 🔹 `@OrNotEndsWith`: 表示使用标注的字段名称和属性值，生成“或逻辑”的“自定义模式不匹配”的 `Predicate` 条件；

### ⛓️ 5. 使用示例 :id=like-demo

```java
/**
 * 根据图书标题前后模糊查询.
 * 注解中没有填写 `value()` 值，将默认使用字符串 `name` 作为数据库关联的实体字段名称.
 */
@Like
private String name;

/**
 * 使用或语句按前缀不匹配图书标题，即查询标题中不以“什么”开头的图书.
 * 注解中填写了 `value()` 的值为 `name`，将使用 `name` 作为数据库关联的实体字段名称.
 */
@OrNotStartsWith("name")
private String orNotStartsWithName;
```

## 🐡 四、范围匹配的注解 (@IN) :id=in

范围匹配是指生成 `IN` 范围查条件。

### 🧲 1. 注解介绍 :id=in-annotations

- 🔹 `@In`: 表示使用标注的字段名称和属性值，生成“与逻辑”的“等值匹配”的 `Predicate` 条件；
- 🔹 `@OrIn`: 表示使用标注的字段名称和属性值，生成“或逻辑”的“等值匹配”的 `Predicate` 条件；
- 🔹 `@NotIn`: 表示使用标注的字段名称和属性值，生成“与逻辑”的“等值不匹配”的 `Predicate` 条件；
- 🔹 `@OrNotIn`: 表示使用标注的字段名称和属性值，生成“或逻辑”的“等值不匹配”的 `Predicate` 条件；

**上面的四个个注解与其他注解不同，有两个方法，一个是 `value()` 方法，表示数据库对应的实体类的属性字段名称（`fieldName`），如果不填写，则默认使用被标注的 Java Bean 的属性名作为 `fieldName`，另一个是 `allowNull()` 方法，表示的是后允许匹配属性值集合或数组中的 `null` 值，默认为 `false`。**。

**被标注的属性值的类型可以是数组，也可以是 `Collection` 集合**。

### 🧰 2. 使用示例 :id=in-demo

```java
/**
 * 用于 IN 范围查询的 ID 集合.
 * 注解中没有填写 `value()` 值，将默认使用属性名的字符串 `id` 作为数据库关联的实体字段名称.
 */
@In
private List<String> id;

/**
 * 用于或逻辑的 IN 范围查询的 ID 集合.
 * 注解中填写了 `value()` 的值为 `id`，将使用 `id` 作为数据库关联的实体字段名称.
 */
@OrIn("id")
private String[] orIds;
```

## 🦈 五、NULL 匹配的注解 (@IsNull) :id=is-null

NULL 匹配是指 SQL 中的字段 `IS NULL` 或者 `IS NOT NULL`。

### 🧬 1. 注解介绍 :id=is-null-annotations

- 🔹 `@IsNull`: 表示使用标注的字段名称，生成“与逻辑”的“IS NULL 匹配”的 `Predicate` 条件；
- 🔹 `@OrIsNull`: 表示使用标注的字段名称，生成“或逻辑”的“IS NULL 匹配”的 `Predicate` 条件；
- 🔹 `@IsNotNull`: 表示使用标注的字段名称，生成“与逻辑”的“IS NOT NULL 匹配”的 `Predicate` 条件；
- 🔹 `@OrIsNotNull`: 表示使用标注的字段名称，生成“或逻辑”的“IS NOT NULL 匹配”的 `Predicate` 条件；

### 🧪 2. 使用示例 :id=is-null-demo

```java
/**
 * 判断 others 字段是否是 NULL.
 * 在使用时请设置属性值为数据库对应该实体属性值，即 .setOthers("others")。
 */
@IsNull
private String others;
```
