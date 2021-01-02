# 🍆 逻辑控制语法 :id=title

前面的示例中使用到了 Fenix 的一些内置语义标签，其实 Fenix 一开始就是像 `Mybatis` 一样支持灵活的逻辑控制的，我没有像 `MyBatis` 一样选用 `ONGL` 来做逻辑控制，而是选用了更好的 [MVEL](http://mvel.documentnode.com/) 模版和表达式解析引擎来达到动态控制 SQL 的功能。

!> **💡 注意**：插值语法 `@{}`（或者 `${}`），不能实现绑定命名参数的方式来生成 `JPQL` 语句，某些情况下可能有 SQL 注入的风险，请视具体情况使用。如果要想使用 `JPQL` 绑定命名参数的特性，请使用 `#{}` 的插值语法。

## 🦅 一、使用示例 :id=example

Fenix 中的**语义化标签和流程控制语法是可以混合使用的**，请看下面的 Fenix SQL 书写方式，即可容易理解：

```xml
<!-- 根据 MVEL 模版语法和表达式来查询 SQL. -->
<fenix id="queryBlogsByTemplate">
    SELECT
       b
    FROM
       Blog AS b
    WHERE
    b.id in #{ids}
    <andLike field="b.author" value="blog.author"/>
    @if{?blog.title != empty}
       AND b.title LIKE '%@{blog.title}%'
    @end{}
</fenix>
```

当传入的博客信息的 `title` 为空时就会生成类似如下的 SQL 了:

```sql
--------------------------------- Fenix 生成的 SQL 信息 ----------------------------------
-- Fenix xml: fenix/BlogRepository.xml -> queryBlogsByTemplate
-------- SQL: SELECT b FROM Blog AS b WHERE b.id in :ids AND b.author LIKE :blog_author
----- Params: {blog_author=%ZhangSan%, ids=[1, 2, 3, 4, 5, 6]}
-----------------------------------------------------------------------------------------
```

!> **注**：`?blog.title != empty` 中的 `?` 是一种更安全的对象属性访问语法，如果你传递的参数是 `Map`，可能 `Map` 中没有这个属性，那么就会报错，如果加上 `?` 的话，可以直接判定为 `false`，而不是报错。

## 🦆 二、常用标签 :id=tags

Fenix 的流程控制语法使用的是 `MVEL` 模板引擎，所以，支持所有 `MVEL2.0` 及以上的模板标签，这也预示着 Fenix 动态 SQL 的强大特性。关于 `MVEL2.x` 的表达式语法和模板语法[请参考这里](http://mvel.documentnode.com/)。

### 🦢 1. @{} 表达式 :id=orb-tag

`@{}` 表达式是最基本的插值语法，当然 `${}` 语法也可以，但我更建议你使用官方的 `@{}` 语法。它包含对一个对字符串求值的值表达式，并附加到输出的模板中。例如：

```sql
-- 如果 user id 为 '5'，将生成为:
-- SELECT u FROM User AS u WHERE u.id = '5'.
SELECT u FROM User AS u WHERE u.id = '@{user.id}'
```

### 🦉 2. #{} 表达式 :id=hash-tag

`#{}` 表达式不是 `MVEL` 提供的语法，是 `Fenix` 增强的语法，主要用来渲染生成 `JPQL` 语句中的命名参数，可以防止 SQL 注入。例如：

```sql
-- 如果 user id 为 '5'，将生成为: 
-- SELECT u FROM User AS u WHERE u.id = :user_id. 其中 user_id 为 '5'
SELECT u FROM User AS u WHERE u.id = #{user.id}
```

### 🦩 3. @code{} 静默代码标签 :id=code-tag

静默代码标记允许您在模板中执行 `MVEL`表达式代码。它不返回值，并且不以任何方式影响模板的格式。

```java
@code{age = 23; name = 'John Doe'}
@{name} is @{age} years old
```

该模板将计算出：`John Doe is 23 years old`。

### 🐧 4. @if{} @else{} 分支选择标签 :id=if-else

`@if{}` 和 `@else{}` 标签在 `MVEL` 模板中提供了完全的 `if-then-else` 功能。 例如：

```java
@if{foo != bar}
   Foo not a bar!
@else{bar != cat}
   Bar is not a cat!
@else{}
   Foo may be a Bar or a Cat!
@end{}
```

`MVEL` 模板中的所有块必须用 `@end{}` 标签来终止，除非是 `if-then-else` 结构，其中 `@else{}` 标记表示前一个控制语句的终止。

### 🐥 5. @foreach{} 循环迭代 :id=foreach

`foreach`标签允许您在模板中迭代集合或数组。

!> 注意：`foreach` 的语法已经在 `MVEL` 模板 2.0 中改变，以使用 `foreach` 符号来标记 `MVEL` 语言本身的符号。

```java
@foreach{item : products}
 - @{item.serialNumber}
@end{}
```

**多重迭代**：

你也可以通过逗号（`,`）分割，一次迭代多个集合：

```java
@foreach{var1 : set1, var2 : set2}
  @{var1}-@{var2}
@end{}
```

**分隔符**：

您可以通过在 `@end{}` 标记中指定迭代器来自动向迭代添加文本分割符。

```java
@foreach{item : people}@{item.name}@end{', '}
```

会得到类似这样的结果：`John, Mary, Joseph`。

> **💡 总结**：以上只是几个你可能会用到的逻辑控制标签。当然，还有其他[更多的 `MVEL` 模版语法](http://mvel.documentnode.com/#mvel-2.0-templating-guide) 供你参考。
