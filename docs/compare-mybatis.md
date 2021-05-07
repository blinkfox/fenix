# 🍎 与 MyBatis XML 的 SQL 写法比较 :id=title

## 🌴 假设业务查询场景 :id=business-scenario

下面将通过一个多条件查询**操作日志**的功能，来初步了解和比较 `MyBatis` 与 `Fenix` 在写“**多条件模糊分页**”查询时 SQL 写法的一些差异和各自的特点。

![查询页面](assets/images/search.png)

由于是查询的场景，上面的几个查询条件都是**非必填**的，字段含义解释如下：

- **操作名称**：数据库字段类型为 `String` 型，根据输入的名称来进行**模糊查询**（`LIKE`）；
- **操作类型**：数据库字段类型为 `int` 型，可以下拉选择多个选项来进行**范围查询**（`IN`）；
- **操作结果**：数据库字段类型为 `int` 型，只能下拉选择一个选项值来进行**等值查询**（`=`）；
- **操作时间**：数据库字段类型为 `datetime` 型，可以选择开始时间或者结束时间来进行**区间查询**（`BETWEEN ? AND ?`、`>=`、`<=`）；

## 🌼 使用 MyBatis XML 的 SQL 写法 :id=mybatis-xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.blinkfox.example.repository.mapper.OperationLogMapper">

    <!-- MyBatis 映射字段为 Bean 的 resultMap. -->
    <resultMap id="operationLogMap" type="com.blinkfox.example.repository.pojo.OperationLog">
        <id column="c_id" property="id"/>
        <result column="c_title" property="title"/>
        <result column="n_type" property="type"/>
        <result column="n_result" property="result"/>
        <result column="dt_create_time" property="createTime"/>
        <result column="c_description" property="description"/>
    </resultMap>

    <!-- MyBatis 动态查询操作日志的 SQL. -->
    <select id="queryOperationLogs" resultMap="operationLogMap">
        SELECT
            ol.c_id,
            ol.c_title,
            ol.n_type,
            ol.n_result,
            ol.dt_create_time,
            ol.c_description
        FROM
            t_operation_log AS ol
        <trim prefix="WHERE" suffix="" suffixOverrides="AND">
            <if test="log.result != null and log.result != 0">
                ol.n_result = #{log.result} AND
            </if>
            <if test="log.title != null and log.title != ''">
                ol.c_title like CONCAT('%', #{log.title}, '%') AND
            </if>
            <if test="log.typeList != null">
                ol.n_type in
                <foreach collection="log.typeList" index="index" item="item" open="(" separator="," close=")">
                    #{item}
                </foreach>
                AND
            </if>
            <if test="log.startTime != null and log.endTime != null">
                ol.dt_create_time BETWEEN #{log.startTime} AND #{log.endTime} AND
            </if>
            <if test="log.startTime != null and log.endTime == null">
                ol.dt_create_time &gt;= #{log.startTime} AND
            </if>
            <if test="log.startTime == null and log.endTime != null">
                ol.dt_create_time &lt;= #{log.endTime} AND
            </if>
        </trim>
    </select>

</mapper>
```

## 🌺 使用 Fenix XML 的 SQL 写法 :id=fenix-xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!-- 操作日志的 SQL 仓库. -->
<fenixs namespace="OperationLogRepository">

    <!-- 多条件模糊分页查询操作日志的示例 SQL. -->
    <fenix id="queryOperationLogs">
        SELECT
            ol.id,
            ol.title,
            ol.type,
            ol.result,
            ol.createTime,
            ol.description
        FROM
        OperationLog AS ol
        <trimWhere>
            <andLike field="ol.title" value="log.title" match="log.title != empty"/>
            <andIn field="ol.type" value="log.typeList" match="log.typeList != empty"/>
            <andEqual field="ol.result" value="log.result" match="log.result != empty"/>
            <andBetween field="ol.createTime" start="log.startTime" end="log.endTime"
                        match="(log.startTime != empty) || (log.endTime != empty)"/>
        </trimWhere>
    </fenix>

</fenixs>
```

## 🌸 比较总结 :id=summary

`MyBatis` 和 `Fenix` 的 SQL 有以下几个差异点：

- MyBatis 只能写原生 SQL，无法享受跨数据库时的兼容性；由于 Fenix 是基于 Spring Data JPA 的扩展，即可以写 `JPQL` 语句，也可以写原生 `SQL` 语句，上述示例中写的是 `JPQL` 语句，SQL 的字段表达上更简洁，也不需要再定义 `resultMap` 映射关系。
- MyBatis 书写动态 SQL 依赖只能使用 `if/else`、`foreach` 等分支选择、循环等操作，保证了灵活性，但是代码量和重复性较高，且 SQL 嵌套多层，视觉上比较混乱，可读写差；而 Fenix 也有 `if/else`、`foreach` 等分支循环操作，但内置了大量的更加简单、强大和语义化的 XML [SQL 标签](xml/xml-tags)，使用语义化的 SQL 标签，使得 SQL 的语义简单明了，没有多层嵌套，可读写更好，通过 `match` 属性的值来确定是否生成此条 SQL，来达到动态性。
- MyBatis 通过 `<trim />` 标签或者使用 `<where />` 标签来消除 `WHERE` 语句后的 `AND` 或者 `OR` 关键字，而 `Fenix` 也是直接将各个动态条件的 SQL 包裹在 `<trimWhere />` 标签中。
- MyBatis 的动态 SQL 解析引擎是 [OGNL](http://commons.apache.org/proper/commons-ognl/)，而 Fenix 的解析引擎是 [MVEL](http://mvel.documentnode.com/)，功能和性能上都更优一些。

> **💧 总结**：通过以上 MyBatis 和 Fenix 的各自 SQL 写法比较来看，`Fenix` 的 SQL 在**动态性**、**简洁性**和**SQL 语义化**等方面，都更加强大。
