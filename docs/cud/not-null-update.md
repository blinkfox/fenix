# 🚁 非【null】属性的增量更新 :id=title

## 🍀 一、初衷 :id=original-intention

在实际业务场景中，更新数据时，从前端得到的对象实体信息数据的参数中，部分信息是不为空，而我们业务上也只想更新这些不为空的字段到数据库中，而不是将空的数据也一起覆盖保存到数据库中，否则就会造成部分字段数据丢失的问题。

Fenix 在 `2.4.0` 版本中增加了这种常见业务操作的功能处理。

## 🍁 二、如何使用 :id=how-to-use

首先，你须要让你业务功能的 `Repository` 接口继承自 `FenixJpaRepository` 接口。由于 `FenixJpaRepository` 接口继承自 `JpaRepository` 接口，所以 `FenixJpaRepository` 接口的功能完全兼容你以前所使用的 `JpaRepository` 的接口。

使用示例如下：

```java
/**
 * 学校（School）相关操作的 Repository 接口.
 *
 * @author blinkfox on 2020-12-05.
 * @since v2.4.0
 */
@Repository
public interface SchoolRepository extends FenixJpaRepository<School, String> {

}
```

### 🍂 三、方法介绍 :id=method-introduction

**非 `null` 属性的增量更新**有两个方法：

- 🔹 `S saveOrUpdateByNotNullProperties(S entity)`: 新增实体或更新实体类中非 `null` 属性的字段值。本方法保存每条数据时会先查询判断是否存在，再进行插入或者更新。
- 🔹 `void saveOrUpdateAllByNotNullProperties(Iterable<S> entities)`: 新增或更新所有实体类中非 `null` 属性的字段值，本方法仅仅是上面方法的 `for` 循环版本，提供了循环处理的功能。

**💡 注意事项**：

- 🔸 上面两个方法的主要判断处理逻辑为：如果传入的实体主键 `ID` 为空，说明是新增的情况，就插入一条新的数据；如果实体的主键 `ID` 不为空，会先判断是否存在该 `ID` 的数据，如果不存在，也会新增插入一条数据；否则说明是更新的情况，会仅更新保存实体类属性中不为 `null` 值的属性字段到数据库中；
- 🔸 由于实际场景中难以区分空和 `null` 的关系，为了统一处理以及不和一些业务场景产生冲突，Fenix 中约定仅更新字段值不为 `null` 的值，如果实体对象中的属性值是空字符串`""`或者 `0` 等等，那么也认为是不为 `null` 的，视为业务上就是要保存空字符串值或者 `0`。所以，Fenix 中也会将该空字符串的值更新到数据库中。所以，**建议你的实体类中所有属性的类型都是对象包装类型，不要用 Java 中的 8 种原生基础类型**。

简单的使用示例如下：

```java
@Autowired
private SchoolRepository schoolRepository;

/**
 * 测试 saveBatch 方法.
 */
@Transactional(rollbackFor = RuntimeException.class)
public void testSaveBatch() {
    // 构造要更新的数据，这里表示只更新 id 为 '123' 的数据的 name 值为 '北京大学'.
    List<School> schools = new School()
            .setId("123")
            .setName("北京大学");

    // 增量更新数据.
    fenixSchoolRepository.saveOrUpdateByNotNullProperties(school);
}
```
