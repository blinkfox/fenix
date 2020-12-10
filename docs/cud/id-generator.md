# ✈️ 更多的主键 ID 生成策略

## 🆔 一、简介

Fenix 从 `2.4.0` 版本开始新增了三种主键 `ID` 的生产策略类供你选择和使用，同时也支持你通过 Java API 去调用生成 `ID`：

- **❄️ 雪花算法 ID** (`Long` 长整型)
- **☃️ 62 进制雪花算法 ID** (`String` 字符串型)
- **🌟 62 进制 UUID** (`String` 字符串型)

## ❄️ 二、雪花算法的 ID 生成策略

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

## ☃️ 三、62 进制雪花算法的 ID 生成策略

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

在 Fenix 中，你也可以通过 Java API 调用生成 `UUID` 和 62 进制的 `UUID`，API 示例如下：

```java
// 使用 IdWorker 来静态方法调用获取通常的 UUID，示例：'73b037d12c894a8ebe673fb6b1caecac'.
String uuid = IdWorker.getUuid();

// 使用 IdWorker 来静态方法调用获取 62 进制的简短 UUID，示例：'FXOedrCvouduYPlYgul'.
String uuid2 = IdWorker.get62RadixUuid();
```

## 🌟 四、62 进制 UUID 生成策略

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

    /**
     * 使用 Fenix 中的雪花算法 ID 生成策略.
     */
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

## ☕ 五、通过 Java API 获取 ID

在 Fenix 中，你也可以通过 Java API 调用生成雪花算法的 ID 或 `UUID`。

以下是生成雪花算法 ID 的 API 方法：

```java
private static final IdWorker idWorker = new IdWorker();

// 获取 10 进制长整型的雪花算法 ID（仅由数字组成）.
long id = idWorker.getId();

// 获取 36 进制字符串型的雪花算法 ID（由数字 + 26 位小写字母组成）.
String id2 = idWorker.get36RadixId();

// 获取 62 进制字符串型的雪花算法 ID（由数字 + 26 位小写字母 + 26 位大写字母组成）.
String id3 = idWorker.get62RadixId();
```

以下是通过 Java 静态方法去生成通常的 `UUID` 和 62 进制 `UUID` 的方法：

```java
// 使用 IdWorker 来静态方法调用获取通常的 UUID，示例：'73b037d12c894a8ebe673fb6b1caecac'.
String uuid = IdWorker.getUuid();

// 使用 IdWorker 来静态方法调用获取 62 进制的简短 UUID，示例：'FXOedrCvouduYPlYgul'.
String uuid2 = IdWorker.get62RadixUuid();
```
