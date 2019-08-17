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

关于 param 的使用示例可以直接参考 [text 的使用示例](java/main-method?id=text-example) 即可。

## equal

#### 方法介绍

equal系列是用来拼接SQL中等值查询的系列方法，生成如：` u.email = ? `这样的等值查询且附带绑定参数的功能，其主要包含如下方法：

- equal(String field, Object value)
- equal(String field, Object value, boolean match)
- andEqual(String field, Object value)
- andEqual(String field, Object value, boolean match)
- orEqual(String field, Object value)
- orEqual(String field, Object value, boolean match)
- notEqual(String field, Object value)
- notEqual(String field, Object value, boolean match)
- andNotEqual(String field, Object value)
- andNotEqual(String field, Object value, boolean match)
- orNotEqual(String field, Object value)
- orNotEqual(String field, Object value, boolean match)

**方法解释**：

- equal、andEqual、orEqual，分别表示拼接等值查询SQL的前缀为`空字符串`,` AND `，` OR `。
- field，表示数据库字段
- value，表示Java中的变量或常量值
- match，表示是否生成该SQL片段，值为`true`时生成，否则不生成

#### 使用示例

```java
/**
 * 初始化.
 */
@BeforeClass
public static void init() {
    context = new HashMap<String, Object>();
    context.put("id", "3");
    context.put("name", "zhagnsan");
    context.put("myEmail", "zhagnsan@163.com");
    context.put("myAge", 25);
}

/**
 * equal相关方法测试.
 */
@Test
public void testEqual() {
    SqlInfo sqlInfo = ZealotKhala.start()
            .equal("u.id", context.get("id"), "4".equals(context.get("id")))
            .equal("u.nick_name", context.get("name"))
            .andEqual("u.true_age", context.get("myAge"))
            .andEqual("u.true_age", context.get("myAge"), context.get("myAge") != null)
            .andEqual("u.email", context.get("myAge"), context.get("myEmail") == null)
            .orEqual("u.email", context.get("myEmail"))
            .end();
    String sql = sqlInfo.getSql();
    Object[] arr = sqlInfo.getParamsArr();

    log.info("-- testEqual()方法生成的sql信息:\n" + sql + "\n-- 参数为:\n" + Arrays.toString(arr));
}
```

打印生成的SQL片段信息如下：

```sql
-- testEqual()方法生成的sql信息:
u.nick_name = ? AND u.true_age = ? AND u.true_age = ? OR u.email = ?
-- 参数为:
[zhagnsan, 25, 25, zhagnsan@163.com]
```

### 同equal类似的

同equal（等于）类似的系列还有不等于、大于、小于、大于等于、小于等于、模糊查询，各系列分别如下：

- `notEqual` 不等于
- `moreThan` 大于
- `lessThan` 小于
- `moreEqual` 大于等于
- `lessEqual` 小于等于
- `like` 模糊查询
- `likePattern` 根据自定义模式来匹配

!> 以上各系列的方法也同equal，这里就不再赘述了。

### between

#### 方法介绍

between系列是用来拼接SQL中区间查询的系列方法，生成如：` u.age BETWEEN ? AND ? `这样的区间查询功能，主要包含如下方法：

- between(String field, Object startValue, Object endValue)
- between(String field, Object startValue, Object endValue, boolean match)
- andBetween(String field, Object startValue, Object endValue)
- andBetween(String field, Object startValue, Object endValue, boolean match)
- orBetween(String field, Object startValue, Object endValue)
- orBetween(String field, Object startValue, Object endValue, boolean match)

**方法解释**：

- between、andBetween、orBetween，分别表示拼接区间查询SQL的前缀为`空字符串`,` AND `，` OR `
- field，表示数据库字段
- startValue，表示区间查询的开始值
- endValue，表示区间查询的结束值
- match，表示是否生成该SQL片段，值为`true`时生成，否则不生成

#### 使用示例

```java
/**
 * 初始化.
 */
@BeforeClass
public static void init() {
    context = new HashMap<String, Object>();
    context.put("startAge", 18);
    context.put("endAge", 26);
    context.put("startBirthday", null);
    context.put("endBirthday", "2010-05-28");
}

/**
 * between相关方法测试.
 */
@Test
public void testBetween() {
    SqlInfo sqlInfo = ZealotKhala.start()
            .between("u.age", startAge, endAge)
            .andBetween("u.age", startAge, endAge, startAge != null && endAge != null)
            .orBetween("u.age", startAge, endAge, startAge != null && endAge != null)
            .end();
    String sql = sqlInfo.getSql();
    Object[] arr = sqlInfo.getParamsArr();

    log.info("-- testEqual()方法生成的sql信息:\n" + sql + "\n-- 参数为:\n" + Arrays.toString(arr));
}
```

打印的SQL如下：

```sql
-- testBetween()方法生成的sql信息:
u.age BETWEEN ? AND ? AND u.age BETWEEN ? AND ? AND u.birthday <= ?
-- 参数为:
[18, 26, 18, 26, 2010-05-28]
```

!> **注意**：Zealot中会对start和end的值做null的空检测。区间查询中如果start为空，end不为空，则是大于等于查询；如果start为空，end不为空，则是小于等于查询；如果start、end均不为空，则是区间查询；两者会均为空则不生产此条sql。

### in

#### 方法介绍

in系列是用来拼接SQL中范围查询的系列方法，生成如：` u.sex in (?, ?) `这样的范围查询功能，主要包含如下方法：

- in(String field, Object[] values)
- in(String field, Object[] values, boolean match)
- andIn(String field, Object[] values)
- andIn(String field, Object[] values, boolean match)
- orIn(String field, Object[] values)
- orIn(String field, Object[] values, boolean match)
- notIn(String field, Object[] values)
- notIn(String field, Object[] values, boolean match)
- andNotIn(String field, Object[] values)
- andNotIn(String field, Object[] values, boolean match)
- orNotIn(String field, Object[] values)
- orNotIn(String field, Object[] values, boolean match)

**方法解释**：

- in、andIn、orIn，分别表示拼接范围查询SQL的前缀为`空字符串`,` AND `，` OR `。
- field，表示数据库字段
- values，表示范围查询需要的参数的数组
- match，表示是否生成该SQL片段，值为`true`时生成，否则不生成

#### 使用示例如下：

```java
/**
 * 初始化.
 */
@BeforeClass
public static void init() {
    context = new HashMap<String, Object>();
    context.put("sexs", new Integer[] {0, 1});
}

/**
 * in相关方法测试.
 */
@Test
public void testBetween() {
    Integer[] sexs = (Integer[]) context.get("sexs");

    SqlInfo sqlInfo = ZealotKhala.start()
            .in("u.sex", sexs)
            .andIn("u.sex", sexs, sexs != null)
            .orIn("u.sex", sexs)
            .end();
    String sql = sqlInfo.getSql();
    Object[] arr = sqlInfo.getParamsArr();

    log.info("-- testIn()方法生成的sql信息:\n" + sql + "\n-- 参数为:\n" + Arrays.toString(arr));
}
```

打印的SQL如下：

```sql
-- testIn()方法生成的sql信息:
u.sex in (?, ?) AND u.sex in (?, ?) OR u.sex in (?, ?)
-- 参数为:
[0, 1, 0, 1, 0, 1]
```

### isNull

#### 方法介绍

`isNull`系列是用来拼接SQL中判断字段为null值或不为null值情况的系列方法，生成如：` u.state IS NULL `这样SQL片段的功能，主要包含如下方法：

- isNull(String field)
- isNull(String field, boolean match)
- andIsNull(String field)
- andIsNull(String field, boolean match)
- orIsNull(String field)
- orIsNull(String field, boolean match)
- isNotNull(String field)
- isNotNull(String field, boolean match)
- andIsNotNull(String field)
- andIsNotNull(String field, boolean match)
- orIsNotNull(String field)
- orIsNotNull(String field, boolean match

**方法解释**：

- isNull、andIsNull、orIsNull，分别表示拼接null值判断SQL的前缀为`空字符串`,` AND `，` OR `。
- field，表示数据库字段
- match，表示是否生成该SQL片段，值为`true`时生成，否则不生成

#### 使用示例如下：

```java
/**
 * IS NULL相关方法测试.
 */
@Test
public void testIsNull() {
    long start = System.currentTimeMillis();

    SqlInfo sqlInfo = ZealotKhala.start()
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

    log.info("testIn()方法执行耗时:" + (System.currentTimeMillis() - start) + " ms");
    String sql = sqlInfo.getSql();
    Object[] arr = sqlInfo.getParamsArr();

    // 断言并输出sql信息
    assertEquals("a.name IS NULL b.email IS NULL a.name IS NULL AND a.name IS NULL AND b.email IS NULL "
            + "AND b.email IS NULL OR a.name IS NULL OR b.email IS NULL OR b.email IS NULL", sql);
    assertArrayEquals(new Object[]{} ,arr);
    log.info("-- testIsNull()方法生成的sql信息:\n" + sql + "\n-- 参数为:\n" + Arrays.toString(arr));
}
```

打印的SQL如下：

```sql
-- testIsNull()方法生成的sql信息:
a.name IS NULL b.email IS NULL a.name IS NULL AND a.name IS NULL AND b.email IS NULL AND b.email IS NULL OR a.name IS NULL OR b.email IS NULL OR b.email IS NULL
-- 参数为:
[]
```

### doAnything

> doAnything(ICustomAction action)

> doAnything(boolean match, ICustomAction action)

这两个方法主要用来方便你在链式拼接的过程中，来完成更多自定义、灵活的操作。`match`意义和上面类似，值为true时才执行，`ICustomAction`是你自定义操作的函数式接口，执行时调用`execute()`方法,使用示例如下：

```java
SqlInfo sqlInfo = ZealotKhala.start()
        .doAnything(true, new ICustomAction() {
            @Override
            public void execute(final StringBuilder join, final List<Object> params) {
                join.append("abc111");
                params.add(5);
                log.info("执行了自定义操作，可任意拼接字符串和有序参数...");
            }
        })
        .end();
```

如果是Java8的话，可以将以上代码转成Lambda表达式，代码如下：

```java
SqlInfo sqlInfo = ZealotKhala.start()
        .doAnything(true, (join, params) -> {
                join.append("abc111");
                params.add(5);
                log.info("执行了自定义操作，可任意插入、拼接字符串和有序参数...");
            }
        })
        .end();
```