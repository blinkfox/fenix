## 无参静态方法

!> **注意**：这里的**无参静态方法**是指拼接 SQL 时仅仅拼接文本字符串，不会添加 SQL 的命名参数，且无动态判断是否生成该段 SQL 片段的能力。作用是和待拼接的字符串自动拼接在一起，省去了 `SQL 关键字` 的书写，目的是用来提高SQL的可读性。

### 主要方法

SQL中的关键字很多，`Fenix` 封装了大多数常用的关键字作为连接 SQL 字符串的方法，如上面总体示例所列出的 `select()`、`from()`、`select()` 等，在流式拼接的过程中，使得 SQL 的可读性和连贯性大大提高了。下面列出了大多数常用的关键字方法，来**用于拼接字符串文本，但不能传递 SQL 参数**。

- insertInto(String text)
- values(String text)
- deleteFrom(String text)
- update(String text)
- select(String text)
- from(String text)
- and(String text)
- or(String text)
- as(String text)
- set(String text)
- innerJoin(String text)
- leftJoin(String text)
- rightJoin(String text)
- fullJoin(String text)
- on(String text)
- orderBy(String text)
- groupBy(String text)
- having(String text)
- limit(String text)
- offset(String text)
- asc()
- desc()
- union()
- unionAll()

> **以上方法主要作用**：用方法名所代表的关键字后追加空格，再拼接上 `text` 文本参数，其方法名称已经体现了具体用途和使用场景，这里不在赘述。

### 使用示例

!> **注**：下面的示例仅是为了演示相关 `API` 的使用，具体 SQL 运行时的正确性，你不用特别在意，实际业务场景中不会这样写。

```java
/**
 * 一个综合测试 SELECT 的一些拼接方式.
 */
@Test
public void testSelect() {
    SqlInfo sqlInfo = Fenix.start()
            .select("u.id, u.nickName, u.email")
            .from("user").as("u")
            .innerJoin("Corp as c").on("u.corpId = c.id")
            .leftJoin("Dept").as("d").on("u.deptId = d.id")
            .rightJoin("Office").as("o").on("u.officeId = o.id")
            .fullJoin("UserDetail").as("ud").on("u.detailId = ud.id")
            .where("u.id = :u_id", "u_id","3")
            .and("u.nickName like '%zhang%'")
            .text(true, "AND u.email = :u_email", "u_email", "san@163.com")
            .doAny(true, (join, params) -> {
                join.append(" hi");
                params.put("hi", 5);
                log.info("执行了自定义操作，你可以任意拼接 JPQL 或者 SQL 字符串和命名参数...");
            })
            .groupBy("u.id").having("u.id")
            .orderBy("u.id").desc().text(", u.nickName", "u_nick_name", "zhang").asc()
            .unionAll()
            .select("u.id, u.nickName, u.email")
            .from("user2")
            .limit("5")
            .offset("3")
            .end();
    String sql = sqlInfo.getSql();

    assertEquals("SELECT u.id, u.nickName, u.email FROM user AS u INNER JOIN Corp as c ON u.corpId = c.id "
            + "LEFT JOIN Dept AS d ON u.deptId = d.id RIGHT JOIN Office AS o ON u.officeId = o.id "
            + "FULL JOIN UserDetail AS ud ON u.detailId = ud.id WHERE u.id = :u_id AND u.nickName like '%zhang%' "
            + "AND u.email = :u_email hi GROUP BY u.id HAVING u.id ORDER BY u.id DESC , u.nickName ASC "
            + "UNION ALL SELECT u.id, u.nickName, u.email FROM user2 LIMIT 5 OFFSET 3", sql);
    assertEquals(4, sqlInfo.getParams().size());
}
```

## text

`text()` 系列的方法作用同 `XML` 中的 [text 标签](xml/xml-tags?id=text)比较类似，是用来任意传递拼接 SQL 字符串和参数的，主要目的是为了提高 SQL 拼接的灵活性。

### 主要方法

下面是 `text()` 系列的重载方法：

```java
// 仅追加 SQL 字符串的方法.
text(String text)

// 追加 SQL 字符串和命名参数的方法，其中 key 为命名参数名称，value 为参数值.
text(String text, String key, Object value)

// 在 SQL 后追加任何 SQL 字符串，后可追加 Map 型参数，Map 中的 key 为命名参数名称，value 为参数值.
text(String text, Map<String, Object> paramMap)

// 在 SQL 后追加任何 SQL 字符串，并设置该 SQL 字符串的命名参数，如果 match 为 true 时，才生成此 SQL 文本和参数.
text(boolean match, String text, String key, Object value)

// 在 SQL 后追加任何 SQL 字符串，后可追加 Map 型参数，如果 match 为 true 时，才生成此 SQL 文本和参数.
text(boolean match, String text, Map<String, Object> paramMap)
```

### 使用示例 :id=text-example

!> **注**：下面的示例仅是为了集中演示 `text` 的使用，具体 SQL 运行时的正确性，你不用特别在意。

```java
/**
 * 测试 text 任何文本和参数的相关方法测试.
 */
@Test
public void testText() {
    SqlInfo sqlInfo = Fenix.start()
            .text("select u.id, u.nickName from User as u where ")
            .text("u.id = :id ", "id", 5)
            .text("and u.nickName like :nickName ", "nickName", "lisi")
            .text("and u.sex in :sex ").param("sex", new Integer[]{2, 3, 4})
            .text("and u.city in :city ", "city", context.get("citys"))
            .end();

    assertEquals("select u.id, u.nickName from User as u where u.id = :id and u.nickName like :nickName "
            + "and u.sex in :sex and u.city in :city", sqlInfo.getSql());
    assertEquals(4, sqlInfo.getParams().size());
}
```

## param 和 params

`param()` 和 `params()` 方法的作用是为了任意传递 SQL 参数的，目的也是为了提高 SQL 拼接过程中 SQL 参数的灵活性。

### 主要方法

```java
// 在 SQL 的参数集合中添加命名参数，其中 key 是 JPQL 中的命名参数名称，value 是该参数对应的值.
param(String key, Object value)

// 在 SQL 的参数集合后添加 Map 集合中的命名参数，Map 中的 key 是 JPQL 中的命名参数名称，value 是该参数对应的值.
params(Map<String, Object> paramMap)
```

### 使用示例

关于 `param` 的使用示例可以直接参考 [text 的使用示例](java/main-method?id=text-example) 即可。

## equal

### 方法介绍

`equal` 系列是用来拼接 SQL 中等值查询的系列方法，生成如：`u.email = :email` 这样的等值查询且附带绑定参数的功能，其主要包含如下方法：

```java
// 生成等值查询的 SQL 片段
equal(String field, Object value)
equal(String field, Object value, boolean match)

// 生成带 " AND " 前缀等值查询的 SQL 片段.
andEqual(String field, Object value)
andEqual(String field, Object value, boolean match)

// 生成带 " OR " 前缀等值查询的 SQL 片段.
orEqual(String field, Object value)
orEqual(String field, Object value, boolean match)
```

**方法解释**：

- `equal`、`andEqual`、`orEqual`，分别表示拼接等值查询 SQL 的前缀为 `空字符串`、`AND`、`OR`；
- `field`，表示数据库字段或实体属性；
- `value`，表示 Java 中的变量或常量值；
- `match`，表示是否生成该 SQL 片段，值为 `true` 时生成，否则不生成；

## 与 equal 类似的方法

同 `equal`（等于）类似的系列方法还有**不等于**、**大于**、**小于**、**大于等于**、**小于等于**、**模糊查询**，各系列分别如下：

- `notEqual`：不等于
- `greaterThan`：大于
- `lessThan`：小于
- `greaterThanEqual`：大于等于
- `lessThanEqual`：小于等于
- `like`：模糊匹配
- `notLike`：不按模糊匹配
- `startsWith`：按前缀匹配
- `notStartsWith`：不按前缀匹配
- `endsWith`：按后缀匹配
- `notEndsWith`：不按后缀匹配
- `likePattern`：根据自定义模式来匹配

!> 以上各系列的方法和参数也同 `equal`，这里就不再赘述了。

## between

### 方法介绍

`between` 系列方法是用来拼接 SQL 中区间查询的系列方法，生成如：`u.age BETWEEN :u_age_start AND :u_age_end`这样的区间查询功能，主要包含如下方法：

```java
// 生成 BETWEEN 区间查询的 SQL 片段，当某一个值为 null 时，会是大于等于或小于等于的情形.
- between(String field, Object startValue, Object endValue)
- between(String field, Object startValue, Object endValue, boolean match)

// 生成带 " AND " 前缀的 BETWEEN 区间查询的 SQL 片段，当某一个值为 null 时，会是大于等于或小于等于的情形.
- andBetween(String field, Object startValue, Object endValue)
- andBetween(String field, Object startValue, Object endValue, boolean match)

// 生成带 " OR " 前缀的 BETWEEN 区间查询的 SQL 片段，当某一个值为 null 时，会是大于等于或小于等于的情形.
- orBetween(String field, Object startValue, Object endValue)
- orBetween(String field, Object startValue, Object endValue, boolean match)
```

**方法解释**：

- `between`、`andBetween`、`orBetween`，分别表示拼接区间查询SQL的前缀为 `空字符串`、`AND`、`OR`；
- `field`，表示数据库字段或实体属性；
- `startValue`，表示区间查询的开始值；
- `endValue`，表示区间查询的结束值；
- `match`，表示是否生成该SQL片段，值为`true`时生成，否则不生成；

### 使用示例

!> **注**：下面的示例仅是为了集中演示 `between` 的使用，具体 SQL 运行时的正确性，你不用特别在意。

```java
Integer startAge = (Integer) context.get("startAge");
Integer endAge = (Integer) context.get("endAge");
String startBirthday = (String) context.get("startBirthday");
String endBirthday = (String) context.get("endBirthday");

SqlInfo sqlInfo = Fenix.start()
        .between("u.age", startAge, endAge)
        .between("u.age", startAge, endAge, startAge == null && endAge == null)
        .between("u.birthday", startBirthday, endBirthday)
        .between("u.birthday", startBirthday, endBirthday, startBirthday != null)
        .andBetween("u.age", startAge, endAge)
        .andBetween("u.age", startAge, endAge, startAge != null && endAge != null)
        .andBetween("u.birthday", startBirthday, endBirthday)
        .andBetween("u.birthday", startBirthday, endBirthday, startBirthday != null)
        .orBetween("u.age", startAge, endAge)
        .orBetween("u.age", startAge, endAge, startAge != null && endAge != null)
        .orBetween("u.birthday", startBirthday, endBirthday)
        .orBetween("u.birthday", startBirthday, endBirthday, startBirthday != null)
        .end();

assertEquals("u.age BETWEEN :u_age_start AND :u_age_end u.birthday <= :u_birthday_end AND u.age "
        + "BETWEEN :u_age_start AND :u_age_end AND u.age BETWEEN :u_age_start AND :u_age_end AND "
        + "u.birthday <= :u_birthday_end OR u.age BETWEEN :u_age_start AND :u_age_end OR u.age "
        + "BETWEEN :u_age_start AND :u_age_end OR u.birthday <= :u_birthday_end", sqlInfo.getSql());
assertEquals(3, sqlInfo.getParams().size());
```

!> **注意**：Fenix 中会对 `start` 和 `end` 的值做 `null` 的空检测。区间查询中如果 `start` 为空，`end` 不为空，则会退化为大于等于查询；如果 `start` 为空，`end` 不为空，则会退化为小于等于查询；如果 `start`、`end` 均不为空，则是区间查询；两者会均为空则不生产此条 SQL。

## in

### 方法介绍

`in` 系列的方法是用来拼接 `SQL` 中范围查询的系列方法，生成如：`u.sex in :u_sex` 这样的范围查询功能，主要包含如下方法：

```java
// 生成 IN 范围查询的 SQL 片段.
in(String field, Object[] values)
in(String field, Collection<?> values)
in(String field, Object[] values, boolean match)
in(String field, Collection<?> values, boolean match)

// 生成带 " AND " 前缀的 IN 范围查询的 SQL 片段.
andIn(String field, Object[] values)
andIn(String field, Collection<?> values)
andIn(String field, Object[] values, boolean match)
andIn(String field, Collection<?> values, boolean match)

// 生成带 " OR " 前缀的 IN 范围查询的 SQL 片段.
orIn(String field, Object[] values)
orIn(String field, Collection<?> values)
orIn(String field, Object[] values, boolean match)
orIn(String field, Collection<?> values, boolean match)

// 生成 " NOT IN " 范围查询的 SQL 片段.
notIn(String field, Object[] values)
notIn(String field, Collection<?> values)
notIn(String field, Object[] values, boolean match)
notIn(String field, Collection<?> values, boolean match)

// 生成带 " AND " 前缀的 " NOT IN " 范围查询的 SQL 片段.
andNotIn(String field, Object[] values)
andNotIn(String field, Collection<?> values)
andNotIn(String field, Object[] values, boolean match)
andNotIn(String field, Collection<?> values, boolean match)

// 生成带 " OR " 前缀的 " NOT IN " 范围查询的 SQL 片段.
orNotIn(String field, Object[] values)
orNotIn(String field, Collection<?> values)
orNotIn(String field, Object[] values, boolean match)
orNotIn(String field, Collection<?> values, boolean match)
```

**方法解释**：

- `in`、`andIn`、`orIn`，分别表示拼接范围查询SQL的前缀为 `空字符串`、`AND`、`OR`；
- `field`，表示数据库字段或实体属性；
- `values`，表示范围查询需要的参数的数组或集合；
- `match`，表示是否生成该SQL片段，值为`true`时生成，否则不生成；

### 使用示例

!> **注**：下面的示例仅是为了集中演示 `in` 的使用，具体 SQL 运行时的正确性，你不用特别在意。

```java
Integer[] sexs = (Integer[]) context.get("sexs");
List<String> citys = (List<String>) context.get("citys");

SqlInfo sqlInfo = Fenix.start()
        .in("u.sex", sexs)
        .in("u.city", citys)
        .in("u.sex", sexs, sexs != null)
        .in("u.city", citys, citys == null)
        .andIn("u.sex", sexs)
        .andIn("u.city", citys)
        .andIn("u.sex", sexs, sexs != null)
        .andIn("u.city", citys, citys == null)
        .orIn("u.sex", sexs)
        .orIn("u.city", citys)
        .orIn("u.sex", sexs, sexs != null)
        .orIn("u.city", citys, citys == null)
        .end();

assertEquals("u.sex IN :u_sex u.city IN :u_city u.sex IN :u_sex AND u.sex IN :u_sex "
        + "AND u.city IN :u_city AND u.sex IN :u_sex OR u.sex IN :u_sex OR u.city IN :u_city "
        + "OR u.sex IN :u_sex", sqlInfo.getSql());
assertEquals(2, sqlInfo.getParams().size());
```

## isNull

### 方法介绍

`isNull` 系列的方法是用来拼接 SQL 中判断字段为 `null` 值或不为 `null` 值情况的系列方法，生成如：`u.state IS NULL` 这样 SQL 片段的功能，主要包含如下方法：

```java
// 生成 " IS NULL " 的 SQL 片段.
isNull(String field)
isNull(String field, boolean match)

// 生成带 " AND " 前缀 " IS NULL " 的 SQL 片段.
andIsNull(String field)
andIsNull(String field, boolean match)

// 生成带 " OR " 前缀 " IS NULL " 的 SQL 片段.
orIsNull(String field)
orIsNull(String field, boolean match)

// 生成 " IS NOT NULL " 的 SQL 片段.
isNotNull(String field)
isNotNull(String field, boolean match)

// 生成带 " AND " 前缀 " IS NOT NULL " 的 SQL 片段.
andIsNotNull(String field)
andIsNotNull(String field, boolean match)

// 生成带 " OR " 前缀 " IS NOT NULL " 的 SQL 片段.
orIsNotNull(String field)
orIsNotNull(String field, boolean match
```

**方法解释**：

- `isNull`、`andIsNull`、`orIsNull`，分别表示拼接 `null` 值判断 SQL 的前缀为 `空字符串`、`AND`、`OR`。
- `field`，表示数据库字段或实体属性；
- `match`，表示是否生成该 SQL 片段，值为 `true` 时生成，否则不生成；

### 使用示例

!> **注**：下面的示例仅是为了集中演示 `isNull` 的使用，具体 SQL 运行时的正确性，你不用特别在意。

```java
/**
 * IS NULL 相关方法的测试.
 */
@Test
public void testIsNull() {
    SqlInfo sqlInfo = Fenix.start()
            .isNull("a.name")
            .isNull("b.email")
            .isNull("a.name", true)
            .isNull("b.email", false)
            .andIsNull("a.name")
            .andIsNull("b.email")
            .andIsNull("a.name", false)
            .andIsNull("b.email", true)
            .orIsNull("a.name")
            .orIsNull("b.email")
            .orIsNull("a.name", false)
            .orIsNull("b.email", true)
            .end();

    assertEquals("a.name IS NULL b.email IS NULL a.name IS NULL AND a.name IS NULL AND b.email IS NULL "
            + "AND b.email IS NULL OR a.name IS NULL OR b.email IS NULL OR b.email IS NULL", sqlInfo.getSql());
    assertTrue(sqlInfo.getParams().isEmpty());
}
```

## doAny

`doAny` 的两个方法主要用来方便你在链式拼接的过程中，来完成更多自定义、灵活的操作。`match` 意义和上面类似，值为 `true` 时才执行，`FenixAction` 是你自定义操作的函数式接口，执行时调用 `execute()` 方法，Java 8 及之后可以使用 `Lambda` 表达式来简化代码。

```java
// 基于 FenixAction 的函数式接口来执行任意的自定义操作.
doAny(FenixAction action)

// 当匹配 match 条件为 true 时，才执行自定义的任意 FenixAction 操作.
doAny(boolean match, FenixAction action)
```

### 使用示例

下面是 `doAny` 的执行示例，供你参考。

```java
SqlInfo sqlInfo = Fenix.start()
        .doAny(true, (join, params) -> {
            join.append(" AND 1 = 1");
            params.put("abc", 5);
            log.info("执行了自定义操作，你可以任意拼接 SQL 字符串和命名参数.");
        })
        .doAny(false, (join, params) -> log.info("match 为 false，将不会执行该自定义操作."))
        .end();
```

## where

`where` 方法有几个重载方法，其中 `where(Consumer<Fenix> consumer)` 方法同 XML 中的 `<where></where>` 标签是用来处理动态 SQL 中的 `WHERE` 关键之后的 `AND` 或者 `OR` 关键字的情况。

```java
// 拼接并带上 'WHERE' 关键字的 SQL 字符串.
where()

// 拼接并带上 'WHERE' 关键字及之后的字符串.
where(String text)

// 拼接并带上 'WHERE' 关键字的字符串和动态参数.
where(String text, Map<String, Object> paramMap)

// 拼接并带上 'WHERE' 关键字的字符串和动态参数.
where(String text, String key, Object value)

// 通过 Lambda 继续拼接 SQL，并动态处理 WHERE 关键字后的 AND 或者 OR 关键字.
where(Consumer<Fenix> consumer)

// 通过 Lambda 继续拼接 SQL，并动态处理 WHERE 关键字后的 AND 或者 OR 关键字.
// 该方法等价于 XML 中的 <where></where> 标签
where(Consumer<Fenix> consumer)

// 使用该方法会动态处理 WHERE 关键字后的 AND 或者 OR 关键字，同 where(Consumer<Fenix> consumer) 方法类似.
// 该方法等价于 XML 中的 <where /> 标签
whereDynamic()
```

### 使用示例

下面是动态 where（`whereDynamic()` 和 `where(Consumer<Fenix> consumer)`）的使用示例，供你参考。

```java
SqlInfo sqlInfo = Fenix.start()
        .select("u")
        .from("User")
        .whereDynamic()
        .andEqual("u.id", context.get("id"), context.get("id_a") != null)
        .andLike("u.nickName", context.get("name"), context.get("name") != null)
        .andLike("u.email", context.get("email"), context.get("email") != null)
        .andIn("u.sex", (Object[]) context.get("sexs"), context.get("sexs") != null)
        .orderBy("u.updateTime").desc()
        .end();
```

```java
SqlInfo sqlInfo = Fenix.start()
        .select("u")
        .from("User")
        .where(fenix ->
                fenix.andEqual("u.id", context.get("id"), context.get("id_a") != null)
                     .andLike("u.nickName", context.get("name"), context.get("name") != null)
                     .andLike("u.email", context.get("email"), context.get("email") != null))
        .andIn("u.sex", (Object[]) context.get("sexs"), context.get("sexs") != null)
        .orderBy("u.updateTime").desc()
        .end();
```

## 综合性示例

下面是一个综合性的示例，来演示通过 Fenix 的链式 API 来拼接动态 SQL 的使用。

```java
/**
 * 综合测试使用 Fenix 书写的 SQL.
 */
@Test
public void testSql() {
    String userName = "zhang";
    String startBirthday = "1990-03-25";
    String endBirthday = "2010-08-28";
    Integer[] sexs = new Integer[]{0, 1};

    SqlInfo sqlInfo = Fenix.start()
            .select("u.id, u.name, u.email, d.birthday, d.address")
            .from("User AS u")
            .leftJoin("UserDetail AS d").on("u.id = d.userId")
            .where("u.id != ''")
            .andLike("u.name", userName, StringHelper.isNotBlank(userName))
            .doAny(sexs.length > 0, (join, params) -> {
                join.append(" AND 1 = 1");
                params.put("abc", 5);
                log.info("执行了自定义操作，你可以任意拼接 SQL 字符串和命名参数.");
            })
            .doAny(false, (join, params) -> log.info("match 为 false，将不会执行该自定义操作."))
            .andGreaterThan("u.age", 21)
            .andLessThan("u.age", 13)
            .andGreaterThanEqual("d.birthday", startBirthday)
            .andLessThanEqual("d.birthday", endBirthday)
            .andBetween("d.birthday", startBirthday, endBirthday)
            .andIn("u.sex", sexs)
            .andIsNotNull("u.state")
            .orderBy("d.birthday").desc()
            .end();

    assertEquals("SELECT u.id, u.name, u.email, d.birthday, d.address FROM User AS u "
            + "LEFT JOIN UserDetail AS d ON u.id = d.userId WHERE u.id != '' AND u.name LIKE :u_name "
            + "AND 1 = 1 AND u.age > :u_age AND u.age < :u_age AND d.birthday >= :d_birthday "
            + "AND d.birthday <= :d_birthday AND d.birthday BETWEEN :d_birthday_start AND :d_birthday_end "
            + "AND u.sex IN :u_sex AND u.state IS NOT NULL ORDER BY d.birthday DESC", sqlInfo.getSql());
    assertEquals(7, sqlInfo.getParams().size());
}
```
