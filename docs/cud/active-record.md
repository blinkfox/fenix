# ✈️ ActiveRecord 模式 :id=active-record-mode

在简单应用中，领域模型和数据库结构几乎是一致的，对应每个数据库表都有一个领域类。在这种情况下，可以让每个对象负责数据库的存取过程，这也就是 `Active Record`（活动记录）。

通常 Java 中的 ActiveRecord 模式都采用了“**继承**”的方式来操作实体对象，但考虑到 Java 只有单继承的因素。而实际工程中大多数实体类都可能继承了公共的基础实体类。所以，在 Fenix 中，实体类是通过组合**实现各个 Model 接口**来做“增删改查”的，灵活性更高。

## 🍣 一、选择 Model 接口 :id=choose-model

在集成了 Fenix 的 JPA 中，根据继承关系存在 `CrudRepository`、`PagingAndSortingRepository`、`JpaRepository` 和 `FenixJpaRepository`（Fenix 提供的）四个 Repository 接口。

另外，根据继承关系还存在 `JpaSpecificationExecutor` 和 `FenixJpaSpecificationExecutor` 两个 SpecificationExecutor 的接口。

在 Fenix 的 ActiveRecord 模式中，本质上仍然需要这几个 `Repository` 或者 `JpaSpecificationExecutor` 接口，并基于此继承关系分别提供了四个 Repository `Model` 接口、两个 Specification `Model` 接口，你须要按需或者组合使用对应的接口。对应关系如下：

- CrudRepository -> `CrudModel`
- PagingAndSortingRepository -> `PagingAndSortingModel`
- JpaRepository -> `JpaModel`
- FenixJpaRepository -> `FenixJpaModel`
- JpaSpecificationExecutor -> `SpecModel`
- FenixJpaSpecificationExecutor -> `FenixSpecModel`

## 🍡 二、实现 Repository 相关的接口 :id=impl-repository

### 🧀 1. 如何实现 :id=repo-impl

首先，在实体类中实现 Fenix 提供的 `JpaModel` 接口（或者其他 Repository Model 接口），在该接口中，你需要传递三个泛型参数，按顺序分别是**当前实体类**、**本实体类的 ID 类型**和**本实体类对应的 `Repository` 接口**。

```java
public class Blog implements JpaModel<Blog, String, BlogRepository> {

    @Id
    @Column(name = "c_id")
    private String id;

    ...

}
```

下面是实体类所对应的 `Repository` 接口，该接口继承了 `JpaRepository` 接口，它与 `JpaModel` 是关联对应的。

```java
@Repository
public interface BlogRepository extends JpaRepository<Blog, String> {

}
```

### 🍕 2. 增删改查 :id=do-curd

#### 🍨 (1). 插入或更新数据

```java
// 直接通过 Blog 对象保存或更新数据.
new Blog()
    .setTitle(TITLE)
    ...
    .save(); 

// 保存新数据或者更新属性不为 null 的字段（本方法是 Fenix 提供，需实现 FenixJpaModel）
blog.saveOrUpdateByNotNullProperties();
```

#### 🍩 (2). 删除数据

```java
// 根据已有实体删除.
blog.delete();

// 根据 ID 删除实体.
new Blog().setId().deleteById();
```

#### 🎂 (3). 根据 ID 查询数据

```java
// 根据 ID 查询实体数据.
new Blog().setId().findById();

// 根据 ID 查询实体是否存在.
blog.existsById();
```

#### 🍰 (4). 调用任意 Repository 中的方法

通过调用实体类中的 `getRepository()` 方法拿到对应实体的 `Repository` 对象，就可以调用 JPA 或你自己定义的任意方法。

```java
blog.getRepository().findXxx();
```

## 🥩 三、实现 SpecificationExecutor 相关的接口 :id=impl-spec-executor

### 🍖 1. 如何实现 :id=spec-impl

SpecificationExecutor 相关的接口可以和 Repository 相关的接口组合使用。所以，在上面的实体类中可以额外再实现 Fenix 提供的 `FenixSpecModel` 接口，在该接口中，你需要传递两个泛型参数，按顺序分别是**当前实体类**和**本实体类对应的 `Repository` 接口**。

```java
public class Blog implements JpaModel<Blog, String, BlogRepository>,
        FenixSpecModel<Blog, BlogRepository> {

    @Id
    @Column(name = "c_id")
    private String id;

    ...

}
```

下面是实体类所对应的 `Repository` 接口，该接口还额外继承了 `FenixJpaSpecificationExecutor` 接口，它与 `FenixSpecModel` 是关联对应的。

```java
@Repository
public interface BlogRepository extends JpaRepository<Blog, String>,
        FenixJpaSpecificationExecutor<Blog> {

}
```

### 🥓 2. 查询示例 :id=spec-query-demo

当你的实体类中实现了 `FenixSpecModel`，该实体类中将获得各种 Fenix 提供的动态查询方法，以下仅展示多条件模糊分页查询的示例。

```java
// 模拟构造一些前端传递过来的查询条件和分页条件.
String name = "hello";
String startDate = "2022-03-02";
String endDate = "2022-03-07";
List<Integer> statusList = Arrays.asList(0, 1, 2, 3);
Pageable pageable = PageRequest.of(0, 3, Sort.by(Sort.Order.desc("birthday")));

// 使用 Fenix 的条件 Lambda 进行模糊查询，还可以在每个查询条件后最后追加动态生成条件.
Page<Blog> blogsPage = new Blog().findAll(builder -> builder
        .andIn("status", statusList)
        .andLike("name", name, StringUtils.isNotBlank(name))
        .andBetween("publishDate", startDate, endDate)
        .build(), pageable);
```

### 🍕 3. 通过 Bean 的查询示例 :id=spec-query-demo-of-bean

Fenix 中的 ActiveRecord 模式，也同样支持 Specification 的 Bean 注解查询方式。

```java
@Getter
@Setter
@Accessors(chain = true)
public class BlogSearchParam {

    /**
     * 根据 ID 等值查询的条件，如果该值不为 null 就进行等值查询.
     */
    @Equals
    private Long id;

    /**
     * 根据名称模糊查询的条件，如果该值不为 null 就进行模糊查询.
     */
    @Like
    private String name;

    /**
     * 根据状态范围查询的条件，如果该值不为空就进行 in 范围查询.
     */
    @In("status")
    private List<Integer> states;

    /**
     * 根据出版日期区间查询的条件，如果该值不为空就进行  Between and 查询，
     * 或者会根据边界值是否为空，自动退化为大于等于或者小于等于的查询.
     */
    @Between
    private BetweenValue<String> publishDate;

}
```

```java
// 模拟构造一些前端传递过来的查询条件和分页条件.
BlogSearchParam blogSearch = new BlogSearchParam()
        .setName("hello")
        .setStates(Arrays.asList(0, 1, 2, 3))
        .setPublishDate(BetweenValue.of(startDate, endDate));
Pageable pageable = PageRequest.of(0, 3, Sort.by(Sort.Order.desc("publishDate")));

// 使用 Fenix 的具有条件注解的对象进行模糊查询,更加简单.
Page<Blog> blogsPage = new Blog().findAllOfBean(blogSearch, pageable);
```
