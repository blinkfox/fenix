# 🚀 更高效的【批量增删改】 :id=more-efficient-cud

## 💐 一、JPA 中自带的优化配置 :id=jpa-builtin-optimization

在 Spring Data JPA 中批量保存或更新的 `saveAll` 方法性能较低。但是，Spring Data JPA（或者说 `Hibernate`）中也自带了关于批量操作的优化配置，通常默认没有开启，我建议你开启这些配置项。

以下是 JPA 中的批量操作优化的 `yaml` 配置，建议在你项目的 `yaml` 或者 `properties`设置这些配置项：

```yaml
spring:
  jpa:
    # 以下配置主要用于提高 jpa 批量操作的相关性能.
    properties:
      hibernate:
        order_inserts: true
        order_updates: true
        jdbc:
          batch_size: 500
          batch_versioned_data: true
```

## 🌻 二、Fenix 中【批量增删改】的增强 :id=fenix-cud-enhance

### 🍺 1. 如何使用 :id=how-to-use

Fenix 在 `2.4.0` 版本中提供了相比 `saveAll` 更高效的**批量新增**、**批量更新**和**批量删除**的方法。但是，Fenix 中并没有提供类似于 `saveAll` 这种能同时做到批量新增或者更新的方法，而是为了提高效率，对批量新增和批量更新的方法单独进行了实现和优化。这样即能保证对 JPA 的兼容性，又能提高各自处理场景的效率。需要开发人员可以根据自己的业务场景选择对应的方法。

要使用这些方法，只须要让你业务功能的 `Repository` 接口继承自 `FenixJpaRepository` 接口即可。由于 `FenixJpaRepository` 接口继承自 `JpaRepository` 接口，所以 `FenixJpaRepository` 接口的功能完全兼容你以前所使用的 `JpaRepository` 的接口。

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

### 🥤 2. 批量新增（saveBatch） :id=save-batch

**批量新增**有两个重载方法：

- `void saveBatch(Iterable<S> entities)`: 批量新增实体集合信息，该方法仅用于新增插入，不能用于有更新数据的场景。每次默认的批量大小为 `500`。
- `void saveBatch(Iterable<S> entities, int batchSize)`: 批量新增实体集合信息，该方法仅用于新增插入，不能用于有更新数据的场景。本方法可以手动设置批量大小的参数。

简单的使用示例如下：

```java
@Autowired
private SchoolRepository schoolRepository;

/**
 * 测试 saveBatch 方法.
 */
@Transactional(rollbackFor = RuntimeException.class)
public void testSaveBatch() {
    // 构造批量的数据.
    List<School> schools = this.buildSchools(1000);

    // 批量新增数据，保存到数据库中.
    schoolRepository.saveBatch(schools);
}
```

### 🍹 3. 批量更新（updateBatch） :id=update-batch

**批量更新**也有两个重载方法：

- `void updateBatch(Iterable<S> entities)`: 批量更新实体集合信息，该方法仅用于更新，不能用于含有新增数据的场景。每次默认的批量大小为 `500`。
- `void updateBatch(Iterable<S> entities, int batchSize)`: 批量更新实体集合信息，该方法仅用于更新，不能用于含有新增数据的场景。本方法可以手动设置批量大小的参数。

简单的使用示例如下：

```java
@Autowired
private SchoolRepository schoolRepository;

/**
 * 测试 updateBatch 方法.
 */
@Transactional(rollbackFor = RuntimeException.class)
public void testUpdateBatch() {
    // 构造批量的数据，你需要确保数据库中已经有这些数据了.
    List<School> schools = this.buildSchools(1000);

    // 批量更新数据，保存到数据库中.
    schoolRepository.updateBatch(schools);
}
```

### 🥂 4. 批量删除（deleteBatchByIds） :id=delete-batch

Spring Data JPA 中已经有相关批量删除的方法了，比如：

- `void deleteAll()`;
- `void deleteAll(Iterable<? extends T> var1)`;
- `void deleteAllInBatch()`
- `void deleteInBatch(Iterable<T> entities)`

但上面的几个方法要么是删除所有数据，要么就是必须根据实体的集合去删除。而实际场景中大多是根据 `ID` 的集合去做批量删除。所以，Fenix 里面做批量删除是根据 `ID` 的集合作为参数来做批量删除的。

**批量删除**有三个方法：

- `void deleteByIds(Iterable<ID> ids)`: 根据 ID 的集合数据删除这些数据，注意该方法仅是循环调用 JPA 中的 `deleteById(Object)` 方法而已。所以，性能相比下面的 `deleteBatchByIds(Iterable, int)` 而言较低。（不推荐）
- `void deleteBatchByIds(Iterable<ID> ids)`: 根据 ID 的集合批量删除这些数据，删除期间会批量转换为 `in` 条件来匹配删除。所以，性能相比上面的 `deleteByIds(Iterable)` 也更高，每次默认的批量大小为 `500`。（推荐）
- `void deleteBatchByIds(Iterable<ID> ids, int batchSize)`: 根据 ID 的集合批量删除这些数据，删除期间会批量转换为 `in` 条件来匹配删除。所以，性能相比上面的 `deleteByIds(Iterable)` 也更高。本方法可以手动设置批量大小的参数。（推荐）

简单的使用示例如下：

```java
@Autowired
private SchoolRepository schoolRepository;

/**
 * 测试 deleteBatchByIds 方法.
 */
@Transactional(rollbackFor = RuntimeException.class)
public void testdeleteBatchByIds() {
    // 构造批量的数据，你需要确保数据库中已经有这些数据了.
    List<String> ids = ...;

    // 批量删除数据.
    fenixSchoolRepository.deleteBatchByIds(ids);
}
```
