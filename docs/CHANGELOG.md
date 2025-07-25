# 🍹 版本更新记录 :id=title

### 🏡️ v3.1.0 新增多种主键生成策略的注解 🆕 (2025-07-23) :id=v340

- 新增多种主键生成策略的注解和修改各策略的实现接口，以适配 Hibernate 新的版本；

### 🏜️ v3.0.1 适配 SpringBoot 3.4.x 和 3.5.x 的版本 (2025-07-20) :id=v340

- 新增适配 SpringBoot 3.4.x 和 3.5.x 的版本；

### ⛵ v3.0.0 适配 SpringBoot 3.x 的版本 (2023-11-07) :id=v300

- 新增适配了 SpringBoot 3.x 的版本；

### 🏖️ v2.7.0 新增 ActiveRecord 模式和多种结果转换策略 (2022-03-31) :id=v270

- 新增了 ActiveRecord 模式，简单场景的“**增删改查**”或**动态查询**更加方便、优雅；
- 新增了多种可自定义的查询结果列转换为对象实体的转换策略，包括"**基于 as 别名转换**"（默认）、"**下换线转驼峰**"、"**去除前缀的下划线转驼峰**"和"**基于 Column 注解转换**"等；
- 新增了 `@QueryFenix` 注解中的 `resultType` 的结果实体类型属性，跟 XML 中的 `resultType` 同义，后续推荐使用注解标注结果类型，重构代码时，能更好的利用上的 Java 的静态编译检查。
- 新增了 `NanoId` 的主键生成策略和 Java API 调用方式；
- 新增和改进了部分文档中的内容，添加了 `IDEA` 插件地址和其他贡献者的列表；

### 🏔️ v2.6.1 修复启动 Banner 版本号不对的小问题 (2021-12-10) :id=v261

- 修复了 Fenix 启动 Banner 中版本号不对的小问题；

### 🎣 v2.6.0 支持 Spring Data JPA 2.6.0 版本 (2021-11-21) :id=v260

- 支持最新的 Spring Data JPA 版本(`v2.6.0`)，同时也能兼容之前的 Spring Data JPA 版本；

### 🎿 v2.5.0 新增了 trimWhere 标签 (2021-05-07) :id=v250

- 新增了 `trimWhere` 标签，用于完全替代之前的 `where` 标签，用于修复它的已知 [bug](https://github.com/blinkfox/fenix/issues/43)，以前的 `where` 标签将继续保留但不再推荐使用；

### ⛸️ v2.4.2 小 bug 修复版本 (2021-02-03) :id=v242

- 修复了使用 `Pageable.unpaged()` 时的异常；

### ⛳ v2.4.1 新增了 debug 模式和 bug 修复 (2021-01-02) :id=v241

- 新增了 `debug` 模式，开启之后，可以在不重启服务的情况下，实时读取和解析 XML 文件中的 SQL；
- 修复 `<where />` 标签中在混合使用了逻辑控制语法和 XML 标签语法时，去除 `AND` 前缀的一些 bug;
- 升级了 `mvel` 的依赖到最新小版本；

### 🥊 v2.4.0 增强 JPA 的增删改功能 (2020-12-09) :id=v240

- 新增了更快速高效的 JPA 批量“增删改”的支持；
- 新增了增量更新的功能，即更新时只更新实体类中属性值不为 `null` 的字段；
- 新增了三种主键 ID 生成策略类；
- 修改或升级了相关依赖包的版本，如：去除了 `dom4j` 的显示依赖，因为 JPA Hibernate 中已经传递依赖了它；

### 🏓 v2.3.6 小功能改进版本 (2020-08-27) :id=v236

- 新增了 `@EnableFenix` 注解中更多的配置信息，与 `@EnableJpaRepositories` 注解相对应；
- 修改了求 `COUNT` 的 SQL 不支持 `DISTINCT` 的问题；

### 🎳 v2.3.5 修复了在老版本 JPA 中某些情况下的 bug (2020-07-31) :id=v235

- 修复了在老版本 JPA 中，某些情况下出现 Javaassist 的 `ClassNotFoundException` 的问题；

### 🥏 v2.3.4 升级了相关依赖包的版本 (2020-07-26) :id=v234

- 升级了相关依赖包的版本，如：`dom4j`、`mvel` 等；

### 🏈 v2.3.3 修复多线程下同一个接口方法的线程安全问题（强烈推荐升级） (2020-07-03) :id=v233

- 修复了在多线程情况下执行同一个 `repository` 接口方法时，可能出现参数混淆的线程安全问题；

### 🏀 v2.3.2 修复某些情况下 JDBC 连接未释放的问题（强烈推荐升级） (2020-05-28) :id=v232

- 修复了在异步多线程情况下，返回自定义实体 Bean 类型时，JDBC 连接未释放的问题，老版本可以使用 `@Transactional` 注解解决；

### 🥎 v2.3.1 兼容支持最新 v2.3.0 版本的 Spring Data JPA (2020-05-18) :id=v231

- 支持最新的 Spring Data JPA 版本(`v2.3.0`)，同时也能兼容之前的 Spring Data JPA 版本；

### ⚽ v2.3.0 新增了基于 JPQL 方式时自定义命名参数名称的相关 API 和属性 (2020-05-09) :id=v230

- 新增了基于 JPQL 的 XML 方式中的多个 XML 标签中，可以自定义命名参数名称的 `name` 属性；
- 新增了基于 JPQL 的 Java API 方式中可以自定义命名参数名称的 `name` 参数相关的 API 方法；

### ⚾ v2.2.0 新增了基于 Specification 的动态注解和 Java 链式 API (2020-02-02) :id=v220

- 新增了基于 `Specification` 的动态条件注解来动态查询数据；
- 新增了基于 `Specification` 的 Java 链式 API 来动态查询数据；
- 新增了 `@EnableFenix` 注解来配置 JPA 可以使用 Fenix 的相关 API；

### 🏅 v2.1.0 新增 <where> 标签和对应的 Java API (2019-11-21) :id=v210

- 新增了 `<where>` 标签和动态 `where` 的 Java API，用来消除在全动态 SQL 中场景中，`WHERE` 关键字后的 `AND` 或者 `OR` 关键字；

### 🏆 v2.0.0 支持 Spring Boot 的 2.2.0.RELEASE 版本 (2019-10-10) :id=v200

- 支持 Spring Boot 和 Spring Data JPA 的 `2.2.0.RELEASE`及以上的版本，同时也兼容之前的版本；

### 🎖️ v1.1.1 求分页 count 小调整 (2019-10-10) :id=v111

- **修改**了默认求总记录数 `count(*)` 时的 SQL 为 `count(*) as count`，即增加了 `as` 列；

### 🎗️ v1.1.0 新增返回任意实体对象或集合 (2019-10-10) :id=v110

- **新增**了**返回任意实体对象**或集合的功能，相比使用投影的方式更为简单和自然；

### 🤹‍♂️ v1.0.1 bug 修复及小功能调整 (2019-09-01) :id=v101

- **新增**了可以使用 `spring.jpa.show-sql` 的配置来作为是否打印 SQL 信息的依据之一；
- **新增**了部分类的单元测试，提高了单元测试覆盖率；
- **重构**了 XML 文件的扫描读取机制；
- **修复**了 Spring Boot web 项目打成 `jar` 包时读取不到 Fenix `XML` 文件的问题；
- **删除**了 Fenix `debug` 模式的功能；

### 🎪 v1.0.0 第一个里程碑正式版 (2019-08-19) :id=v100

- **新增**了 `Fenix` JPA 扩展库的核心功能；
