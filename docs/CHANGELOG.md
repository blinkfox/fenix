## v2.2.0 新增了基于 Specification 的动态注解诶和 Java 链式 API (2020-02-02)

- 新增了基于 `Specification` 的动态条件注解来动态查询数据；
- 新增了基于 `Specification` 的 Java 链式 API 来动态查询数据；
- 新增了 `@EnableFenix` 注解来配置 JPA 可以使用 Fenix 的相关 API；

## v2.1.0 新增 <where> 标签和对应的 Java API (2019-11-21)

- 新增了 `<where>` 标签和动态 `where` 的 Java API，用来消除在全动态 SQL 中场景中，`WHERE` 关键字后的 `AND` 或者 `OR` 关键字；

## v2.0.0 支持 Spring Boot 的 2.2.0.RELEASE 版本 (2019-10-10)

- 支持 Spring Boot 和 Spring Data JPA 的 `2.2.0.RELEASE`及以上的版本，同时也兼容之前的版本；

## v1.1.1 求分页 count 小调整 (2019-10-10)

- **修改**了默认求总记录数 `count(*)` 时的 SQL 为 `count(*) as count`，即增加了 `as` 列；

## v1.1.0 新增返回任意实体对象或集合 (2019-10-10)

- **新增**了**返回任意实体对象**或集合的功能，相比使用投影的方式更为简单和自然；

## v1.0.1 bug 修复及小功能调整 (2019-09-01)

- **新增**了可以使用 `spring.jpa.show-sql` 的配置来作为是否打印 SQL 信息的依据之一；
- **新增**了部分类的单元测试，提高了单元测试覆盖率；
- **重构**了 XML 文件的扫描读取机制；
- **修复**了 Spring Boot web 项目打成 `jar` 包时读取不到 Fenix `XML` 文件的问题；
- **删除**了 Fenix `debug` 模式的功能；

## v1.0.0 第一个里程碑正式版 (2019-08-19)

- **新增**了 `Fenix` JPA 扩展库的核心功能；
