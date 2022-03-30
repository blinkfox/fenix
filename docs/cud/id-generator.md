# 🆔 更多的主键 ID 生成策略 :id=title

## 📖 一、简介 :id=introduction

Fenix 从 `2.4.0` 版本开始新增了三种主键 `ID` 的生产策略类供你选择和使用，同时也支持你通过 Java API 去调用生成 `ID`：

- **❄️ 雪花算法 ID** (`Long` 长整型)
- **☃️ 36 进制雪花算法 ID** (`String` 字符串型)
- **☃️ 62 进制雪花算法 ID** (`String` 字符串型)
- **✒️ 21 位 NanoId** (`String` 字符串型)
- **🌟 62 进制 UUID** (`String` 字符串型)

> **建议**：各个 ID 的推荐优先级：**雪花算法** > **`NanoId`** > **`UUID`**。

## ❄️ 二、雪花算法的 ID 生成策略 :id=snowflake

雪花算法 (`snowflake`) 已经是现在使用比较广泛的 ID 生成算法，其避免了 `UUID` 的冗长无序的缺点，生成的 ID 是**整体有序**的**长整型**数据，Fenix 中也默认做了集成和支持。

雪花算法生成的 ID 示例：`2458424618421248`。

在 JPA 中要使用自定义的 `ID` 生成策略，需要在你的实体类的 ID 字段中标注 `@GeneratedValue` 和 `@GenericGenerator` 两个注解，并保证 `@GeneratedValue` 注解中的 `generator` 属性和 `@GenericGenerator` 注解中的 `name` 属性值相同。且  `@GenericGenerator` 注解中的 `strategy` 属性值为：`com.blinkfox.fenix.id.SnowflakeIdGenerator`。

使用方式示例如下：

```java
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Entity
@Table(name = "t_my_entity")
public class MyEntity {

    /**
     * 使用 Fenix 中的雪花算法 ID 生成策略.
     */
    @Id
    @Column(name = "c_id")
    @GeneratedValue(generator = "snowflake")
    @GenericGenerator(name = "snowflake", strategy = "com.blinkfox.fenix.id.SnowflakeIdGenerator")
    private Long id;

    /**
     * 名称.
     */
    @Column(name = "c_name")
    private String name;

    // 下面省略了其它字段.

}
```

## ☃️ 三、36 或 62 进制雪花算法的 ID 生成策略 :id=snowflake-base62

如果你的 ID 不是长整型（`Long`）的，是字符串类型（`String`）的，为了能缩短雪花算法 ID 字符串的长度，可以将原来长度为 `16` 位的雪花算法 ID 的转换为 `62` 进制，能大幅度缩短 `ID` 的长度为 `9` 位，且依然能保证**唯一性**和**整体有序性**。

62 进制雪花算法生成的字符串型 ID 示例：`BG5skT7pI`。

`@GenericGenerator` 注解中的 `strategy` 属性值为：`com.blinkfox.fenix.id.Snowflake62RadixIdGenerator`。

使用方式示例如下：

```java
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Entity
@Table(name = "t_my_entity")
public class MyEntity {

    /**
     * 使用 Fenix 中的雪花算法 ID 生成策略.
     */
    @Id
    @Column(name = "c_id")
    @GeneratedValue(generator = "snowflake62Radix")
    @GenericGenerator(name = "snowflake62Radix", strategy = "com.blinkfox.fenix.id.Snowflake62RadixIdGenerator")
    private String id;

    /**
     * 名称.
     */
    @Column(name = "c_name")
    private String name;

    // 下面省略了其它字段.

}
```

而 `36`进制通常是 `10` 位的小写字母，唯一且有序，你也可以视情况选择此策略。示例代码如下：

```java
@Id
@Column(name = "c_id")
@GeneratedValue(generator = "snowflake36RadixId")
@GenericGenerator(name = "snowflake36RadixId", strategy = "com.blinkfox.fenix.id.Snowflake36RadixIdGenerator")
private String id;
```

## ✒️ 四、21 位 NanoId 生成策略 :id=nano-id

相比于 UUID，`NanoId` 大小只有 `108` 个字节，生成的字符串更短，并且生成速度更快。所以，你也可以选择使用 `NanoId` 的生成器 `com.blinkfox.fenix.id.NanoIdGenerator`。

`NanoId` 的字符串示例为：`IaoyHI51Rx-dUIzz-MQUq`。

```java
@Entity
@Table(name = "t_my_entity")
public class MyEntity {

    @Id
    @Column(name = "c_id")
    @GeneratedValue(generator = "nanoId")
    @GenericGenerator(name = "nanoId", strategy = "com.blinkfox.fenix.id.NanoIdGenerator")
    private String id;

}
```

## 🌟 五、62 进制 UUID 生成策略 :id=uuid-base62

鉴于 `UUID` 本质上是 `16` 进制的字符串，字符串长度为 `32` 位，依然可以通过进制转换，将其转换为 `62` 进制，能大幅度缩短 `UUID` 的字符串长度为 `19` 位，且依然能保证**唯一性**和**无序性**。

假如原 16 进制的 UUID 为：`73b037d12c894a8ebe673fb6b1caecac`，那么转换后的 62 进制 `UUID` 的字符串示例为：`FXOedrCvouduYPlYgul`。

`@GenericGenerator` 注解中的 `strategy` 属性值为：`com.blinkfox.fenix.id.Uuid62RadixIdGenerator`。

使用方式示例如下：

```java
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Entity
@Table(name = "t_my_entity")
public class MyEntity {

    @Id
    @Column(name = "c_id")
    @GeneratedValue(generator = "uuid62Radix")
    @GenericGenerator(name = "uuid62Radix", strategy = "com.blinkfox.fenix.id.Uuid62RadixIdGenerator")
    private String id;

    /**
     * 名称.
     */
    @Column(name = "c_name")
    private String name;

    // 下面省略了其它字段.

}
```

## ☕ 五、通过 Java API 获取 ID :id=java-api

在 Fenix 中，你也可以通过 Java API 调用生成雪花算法的 ID 或 `UUID`。

以下是 Java API 生成雪花算法 ID 的 API 方法：

```java
// 获取 10 进制长整型的雪花算法 ID（仅由数字组成）.
long id = IdWorker.getSnowflakeId();

// 获取 36 进制字符串型的雪花算法 ID（由数字 + 26 位小写字母组成）.
String id2 = IdWorker.getSnowflake36RadixId();

// 获取 62 进制字符串型的雪花算法 ID（由数字 + 26 位小写字母 + 26 位大写字母组成）.
String id3 = IdWorker.getSnowflake62RadixId();
```

以下是通过 Java API 生成 `NanoId` 的方法：

```java
// 生成默认字符串长度为 21 位的 NanoId，示例：`y5-Gvn2-LSn9p3HN6RuJi`.
String nanoId = IdWorker.getNanoId();

// 生成指定长度的 NanoId，例如：15，示例：'cz5KYPncsTsszP8'.
String nanoId2 = IdWorker.getNanoId(15);
```

以下是通过 Java API 生成 `UUID` 和 62 进制 `UUID` 的方法：

```java
// 使用 IdWorker 来静态方法调用获取通常的 UUID，示例：'73b037d12c894a8ebe673fb6b1caecac'.
String uuid = IdWorker.getUuid();

// 使用 IdWorker 来静态方法调用获取 62 进制的简短 UUID，示例：'FXOedrCvouduYPlYgul'.
String uuid2 = IdWorker.get62RadixUuid();
```
