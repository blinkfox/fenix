# Fenix

> [Fenix](https://github.com/blinkfox/fenix)（菲尼克斯）是一个为了解决复杂动态 SQL (`JPQL`) 而生的 `Spring Data JPA` 扩展库，目的是辅助开发者更方便快捷的书写复杂、动态且易于维护的 SQL，支持 `XML`、Java 链式 `API` 和动态条件注解等四种方式来书写动态 SQL。

## 特性

- 简单、轻量级、无副作用的集成和使用，jar 包仅 `176 KB`；
- 作为 JPA 的扩展和增强，兼容 Spring Data JPA 原有功能和各种特性；
- 提供了 `XML`、Java 链式 `API` 和动态条件注解等四种方式来书写动态 SQL；
- `XML` 的方式功能强大，让 SQL 和 Java 代码解耦，易于维护；
- 可以采用 Java 链式 `API` 来书写动态 SQL；
- 可以采用动态条件注解和Java 链式 `API` 来书写出动态的 `Specification`。
- 具有动态性、极致的可复用性的优点；
- SQL 执行结果可返回任意自定义的实体对象，比使用 JPA 自身的投影方式更加简单和自然；
- 具有可扩展性，如：可自定义 `XML` 语义标签和对应的标签处理器来生成自定义逻辑的 SQL 片段和参数；

## 初衷

随着 [Spring Data JPA](https://spring.io/projects/spring-data-jpa) 越来越流行，极大的方便了数据的“增删改”和简单查询的场景，但是在复杂、动态查询方面就显得有些“糟糕”了，相比 `MyBatis` 的 `XML` 动态 SQL 而言，缺少了一定优雅和可维护性，而使用原生的 `Specification` 又显得过于“臃肿”。

所有，为了能使开发人员能像在 `MyBatis` 中那样在 `XML` 中书写 `JPQL` 语句，Fenix 中引入了 [MVEL](http://mvel.documentnode.com/) 表达式和模板引擎的语法来书写和渲染 XML 中的动态 SQL。通俗的说，就是支持使用表达式、`if/else`、`foreach` 等来达到跟 MyBatis 类似的动态 SQL 能力。但是，仅靠这些“灵活”的动态能力，仍然会书写出大量相似或重复的 SQL。

因此，为了更加极致的解决 SQL 片段“**相似或重复**”的问题，Fenix 中引入了 SQL 片段的“**语义化标签**”，将大多数常见的 SQL 片段做成 `XML` 标签，通过传递的字段动态的参数值就可以生成对应的 SQL 片段和命名参数。语言化的 XML 标签可以在各个需要的地方复用，也可以自定义自己的 XML SQL 标签。

同时，Fenix 还提供了 Java 链式 `API` 书写动态 SQL 和动态 `Specification` 的方式，使 SQL 可读性和紧凑性更好。还可以使用动态条件注解将查询条件的实体 Bean 中标记对应的查询条件注解，而获得动态的查询能力。如果要书写静态或动态的中、长 SQL，则推荐使用 `XML` 方式，便于集中阅读、调试和维护 SQL。

> **注**：本 `Fenix` 扩展库开发的核心思想来源于我几年前写的动态 SQL 拼接库 [Zealot](https://github.com/blinkfox/zealot)。如果你熟悉星际争霸的话，大概能理解其中的关系。

## 开源许可证

本 `Fenix` 的 Spring Data JPA 扩展库遵守 [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0) 许可证。
