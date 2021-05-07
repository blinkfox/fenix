# 🌶️ SQL 语义化标签 :id=title

Fenix 的核心功能就在于将 SQL 单独写到 XML 文件中，为了增强写动态 SQL 的能力，引入了 `MVEL` 模版引擎，使得我们可以写出动态的 SQL。但是这样的 SQL 中就像 `MyBatis` 一样充斥着 `if/else` 或者 `foreach` 循环等等语句。不仅冗长、而且可读性也不好。

所以，在 Fenix 中将绝大多数常见的 SQL 片段进一步封装化，以 XML 的语义化标签的形式来生成 SQL 片段，为了杜绝大多数场景下 `if` 语句的使用，将 `if` 语句的条件本身进一步封装为一个 XML 标签的 `match` 属性，生成 SQL 时，判断 `match` 的属性内容（应该是一个 `MVEL` 表达式）的布尔结果值，如果为 `true` 就生成该条 SQL 片段，否则不生成。当然如果没有 `match` 属性，则表明是必然要生成此条 SQL 片段。

Fenix 中提供了大量常见场景下的 XML 标签供开发者使用，且这些标签大多数都配置有 `AND`、`OR`、`NOT` （即：与、或、非）等前缀，能够更大程度上精简动态 SQL。

这些动态的 Fenix XML SQL 片段标签，主要有如下几类：

- [equal](/xml/xml-tags?id=equal)
- [与 equal 类似的标签](/xml/xml-tags?id=equal-similar-tags)
- [like](/xml/xml-tags?id=like)
- [startswith](/xml/xml-tags?id=startswith)
- [endswith](/xml/xml-tags?id=endswith)
- [between](/xml/xml-tags?id=between)
- [in](/xml/xml-tags?id=in)
- [is null](/xml/xml-tags?id=is-null)
- [trimWhere](/xml/xml-tags?id=trimWhere)
- [text](/xml/xml-tags?id=text)
- [import](/xml/xml-tags?id=import)
- [choose](/xml/xml-tags?id=choose)
- [set](/xml/xml-tags?id=set)

## 🐈 一、equal :id=equal

### 🛀 1. 标签 :id=equal-tag

```xml
<equal field="" value="" name="" match="" />
<andEqual field="" value="" name="" match="" />
<orEqual field="" value="" name="" match="" />
```

### 🛌 2. 属性介绍 :id=equal-property

- **field**，表示对应数据库或实体的字段，也可以是数据库的表达式、函数等。**必填**属性。
- **name**，表示JPA 中生成的 JPQL 语句中的命名参数名称，`v2.3.0` 版本新增的属性。当不填写或者内容为空时，将默认根据 `value` 的值来生成命名参数名称。**非必填**属性。该字段通常用来解决 `value` 值比较复杂，为表达式时，生成的命名参数不对的问题。
- **value**，表示参数值，对应 `MVEL` 表达式，也可以是基础数据类型，如：数字、字符串等。**必填**属性。
- **match**，表示匹配条件。**非必填**属性，如果不填此属性，或者内容为空，则视为必然生成此条件 SQL（`JPQL`） 片段；否则解析出的匹配结果为 `true` 时才生成，匹配结果为 `false` 时不生成。

### 🔪 3. 使用示例 :id=equal-demo

```xml
<equal field="nickname" value="name" />
<!-- 要没有 match 属性将必然生成下面这条 SQL 片段和参数. -->
nickname = :name

<andEqual field="email" value="user.email" match="?email != empty" />
<!-- 如果 email 不等于空（不为 null 或空字符串）时，才生成下面这条 SQL 片段和参数 -->
AND email = :user_email

<andEqual field="email" name="myEmail" value="user.email" match="?email != empty" />
<!-- 如果 email 不等于空（不为 null 或空字符串）时，才生成下面这条 SQL 片段和参数，其中我们通过 name 属性，自定义了 SQL 中的命名参数为 myEmail. -->
<!-- 其中我们通过 name 属性，自定义了 SQL 中的命名参数为 myEmail. -->
AND email = :myEmail
```

## 🐒 二、与 equal 类似的标签 :id=equal-similar-tags

以下标签的属性及含义和 `<equal/>` 标签都是一样的，只不过 `XML` 的标签名称和生成的 SQL **操作符**有所不同而已。主要是**大于**、**小于**、**大于等于**和**小于等于**之类的标签逻辑。

- `notEqual`：不等于
- `andNotEqual`：带 `and` 前缀的不等于
- `orNotEqual`：带 `or` 前缀的不等于
- `greaterThan`：大于
- `andGreaterThan`：带 `and` 前缀的大于
- `orGreaterThan`：带 `or` 前缀的大于
- `lessThan`：小于
- `andLessThan`：带 `and` 前缀的小于
- `orLessThan`：带 `or` 前缀的小于
- `greaterThanEqual`：大于等于
- `andGreaterThanEqual`：带 `and` 前缀的大于等于
- `orGreaterThanEqual`：带 `or` 前缀的大于等于
- `lessThanEqual`：小于等于
- `andLessThanEqual`：带 `and` 前缀的小于等于
- `orLessThanEqual`：带 `or` 前缀的小于等于

## 🐶 三、like :id=like

### 🧭 1. 标签 :id=like-tag

```xml
<like field="" name="" value="" pattern="" match="" />
<andLike field="" name="" value="" pattern="" match="" />
<orLike field="" name="" value="" pattern="" match="" />

<!-- not like的 相关标签. -->
<notLike field="" name="" value="" pattern="" match="" />
<andNotLike field="" name="" value="" pattern="" match="" />
<orNotLike field="" name="" value="" pattern="" match="" />
```

### 🧱 2. 属性介绍 :id=like-property

- **field**，表示对应数据库或实体的字段，也可以是数据库的表达式、函数等。**必填**属性。
- **name**，表示JPA 中生成的 JPQL 语句中的命名参数名称，`v2.3.0` 版本新增的属性。当不填写或者内容为空时，将默认根据 `value` 的值来生成命名参数名称。**非必填**属性。该字段通常用来解决 `value` 值比较复杂，为表达式时，生成的命名参数不对的问题。
- **value**，表示参数值，对应 `MVEL` 表达式，也可以是基础数据类型，如：数字、字符串等。**条件必填**属性。`pattern` 和 `value` 只能存在一个，`value` 生成的 SQL 片段默认是两边模糊，即：`%%`。
- **pattern**，表示 `like` 匹配的模式，如：`abc%`、`_bc`等，只能是静态文本内容。**条件必填**属性。`pattern` 和 `value` 只能存在一个，`pattern` 用来指定自定义的匹配模式。
- **match**，表示匹配条件。**非必填**属性，如果不填此属性，或者内容为空，则视为必然生成此条件 SQL 片段；否则匹配结果为 `true` 时才生成，匹配结果为 `false` 时不生成。

### 💈 3. 使用示例 :id=like-demo

```xml
<andLike field="u.email" value="email" match="?email != empty" />
<!-- 如果 email 不等于空时，才生成下面的这条 SQL 片段，参数为: {email: '%ZhangSan%'}. -->
AND u.email LIKE :email

<notLike field="u.email" pattern="%@gmail.com" />
<!-- 匹配所有不是 gmail 的邮箱，将生成下面这这样的 SQL 片段. -->
u.email NOT LIKE '%@gmail.com'

<andNotLike field="u.email" name="myEmail" value="email" />
<!-- 生成下面这这样的 SQL 片段，其中我们通过 name 属性，自定义了 SQL 中的命名参数为 myEmail. -->
AND u.email NOT LIKE :myEmail
```

## 🦊 四、startsWith :id=starts-with

`startsWith` 是 `like` 标签的特殊形式，表示按前缀来做模糊匹配。

### 🛎️ 1. 标签 :id=starts-with-tag

```xml
<startsWith field="" name="" value="" match=""/>
<andStartsWith field="" name="" value="" match=""/>
<orStartsWith field="" name="" value="" match=""/>

<notStartsWith field="" name="" value="" match=""/>
<andNotStartsWith field="" name="" value="" match=""/>
<orNotStartsWith field="" name="" value="" match=""/>
```

### ⌛ 2. 属性介绍 :id=starts-with-property

- **field**，表示对应数据库或实体的字段，也可以是数据库的表达式、函数等。**必填**属性。
- **name**，表示JPA 中生成的 JPQL 语句中的命名参数名称，`v2.3.0` 版本新增的属性。当不填写或者内容为空时，将默认根据 `value` 的值来生成命名参数名称。**非必填**属性。该字段通常用来解决 `value` 值比较复杂，为表达式时，生成的命名参数不对的问题。
- **value**，表示参数值，对应 `MVEL` 表达式，也可以是基础数据类型，如：数字、字符串等。**必填**属性。生成的 SQL 片段是按前缀来匹配的，即：`xxx%`。
- **match**，表示匹配条件。**非必填**属性，如果不填此属性，或者内容为空，则视为必然生成此条件 SQL 片段；否则匹配结果为 `true` 时才生成，匹配结果为 `false` 时不生成。

### ⌚ 3. 使用示例 :id=starts-with-demo

```xml
<startsWith field="u.name" value="user.name" match="user.name != empty"/>
<!-- 如果用户的 name 不为空时，才生成下面的这条 SQL 片段，参数为: {user_name: 'ZhangSan%'}. -->
u.name LIKE :user_name

<andNotStartsWith field="u.name" value="user.name"/>
<!-- 将生成下面的这条 SQL 片段，参数为: {user_name: 'ZhangSan%'}. -->
AND u.name NOT LIKE :user_name

<andStartsWith field="u.name" name="myName" value="user.name"/>
<!-- 将生成下面的这条 SQL 片段，参数为: {myName: 'ZhangSan%'}，我们通过 name 属性，自定义了 SQL 中的命名参数为 myName. -->
AND u.name LIKE :myName
```

## 🐺 五、endsWith :id=ends-with

`endsWith` 也是 `like` 标签的特殊形式，同 `startsWith` 标签相反，表示按后缀来做模糊匹配。

### ⏰ 1. 标签 :id=ends-with-tag

```xml
<endsWith field="" name="" value="" match=""/>
<andEndsWith field="" name="" value="" match=""/>
<orEndsWith field="" name="" value="" match=""/>

<notEndsWith field="" name="" value="" match=""/>
<andNotEndsWith field="" name="" value="" match=""/>
<orNotEndsWith field="" name="" value="" match=""/>
```

### ⛱️ 2. 属性介绍 :id=ends-with-property

- **field**，表示对应数据库或实体的字段，也可以是数据库的表达式、函数等。**必填**属性。
- **name**，表示JPA 中生成的 JPQL 语句中的命名参数名称，`v2.3.0` 版本新增的属性。当不填写或者内容为空时，将默认根据 `value` 的值来生成命名参数名称。**非必填**属性。该字段通常用来解决 `value` 值比较复杂，为表达式时，生成的命名参数不对的问题。
- **value**，表示参数值，对应 `MVEL` 表达式，也可以是基础数据类型，如：数字、字符串等。**必填**属性。生成的 SQL 片段是按后缀来匹配的，即：`%xxx`。
- **match**，表示匹配条件。**非必填**属性，如果不填此属性，或者内容为空，则视为必然生成此条件 SQL 片段；否则匹配结果为 `true` 时才生成，匹配结果为 `false` 时不生成。

### 🌡️ 3. 使用示例 :id=ends-with-demo

```xml
<endsWith field="u.name" value="user.name" match="user.name != empty"/>
<!-- 如果用户的 name 不为空时，才生成下面的这条 SQL 片段，参数为: {user_name: '%ZhangSan'}. -->
u.name LIKE :user_name

<andNotEndsWith field="u.name" value="user.name"/>
<!-- 将生成下面的这条 SQL 片段，参数为: {user_name: '%ZhangSan'}. -->
AND u.name NOT LIKE :user_name

<andEndsWith field="u.name" name="myName" value="user.name"/>
<!-- 将生成下面的这条 SQL 片段，参数为: {user_name: '%ZhangSan'}，我们通过 name 属性，自定义了 SQL 中的命名参数为 myName. -->
AND u.name LIKE :myName
```

## 🐯 六、between :id=between

### 🧨 1. 标签 :id=between-tag

```xml
<between field="" startName="" start="" endName="" end="" match=""/>
<andBetween field="" startName="" start="" endName="" end="" match=""/>
<orBetween field="" startName="" start="" endName="" end="" match="" />
```

### 🎈 2. 属性介绍 :id=between-property

- **field**，表示对应数据库或实体的字段，可以是数据库的表达式、函数等。**必填**属性。
- **startName**，表示JPA 中生成的开始值的 JPQL 语句中的命名参数名称，`v2.3.0` 版本新增的属性。当不填写或者内容为空时，将默认根据 `start` 的值来生成命名参数名称。**非必填**属性。该字段通常用来解决 `start` 值比较复杂，为表达式时，生成的命名参数不对的问题。
- **start**，表示区间匹配条件的开始参数值，对应 `MVEL` 表达式，**条件必填**。
- **endName**，表示JPA 中生成的结束值的 JPQL 语句中的命名参数名称，`v2.3.0` 版本新增的属性。当不填写或者内容为空时，将默认根据 `end` 的值来生成命名参数名称。**非必填**属性。该字段通常用来解决 `end` 值比较复杂，为表达式时，生成的命名参数不对的问题。
- **end**，表示区间匹配条件的结束参数值，对应 `MVEL` 表达式，**条件必填**。
- **match**，表示匹配条件。**非必填**属性，如果不填此属性，或者内容为空，则视为必然生成此条件 SQL 片段；否则匹配结果为 `true` 时才生成，匹配结果为 `false` 时不生成。

!> **注意**：Fenix 中对 start 和 end 的空判断是检测是否是 `null`，而不是空字符串，`0`等情况。所以，如果 `start` 和 `end` 的某一个值为 `null` 时，区间查询将退化为大于等于（`>=`）或者小于等于（`<=`）的查询。

### 🎉 3. 使用示例 :id=between-demo

```xml
<andBetween field="u.age" start="startAge" end="endAge" match="(?startAge != null) || (?endAge != null)" />

<!-- 通过写 startName 和 endName 属性，自定义了 SQL 中的命名参数为. -->
<andBetween field="u.age" startName="startAge" start="Integer.parseInt(startAge)" endName="endAge" end="Integer.parseInt(endAge)" />
```

**解释**：

- 当 `start` 不为 `null`，`end` 为 `null`，则生成的 SQL 片段为：`AND u.age >= :startAge`；
- 当 `start` 为 `null`，`end` 不为 `null` ，则生成的 SQL 片段为：`AND u.age <= :endAge`；
- 当 `start` 不为 `null`，`end`不为`null`，则生成的 SQL 片段为：`AND u.age BETWEEN :startAge AND :endAge`；
- 当 `start` 为 `null`，`end`为`null`，则不生成SQL片段；

## 🦁 七、in :id=in

### 🎊 1. 标签 :id=in-tag

```xml
<in field="" name="" value="" match="" />
<andIn field="" name="" value="" match="" />
<orIn field="" name="" value="" match="" />

<!-- not in 相关的标签. -->
<notIn field="" name="" value="" match="" />
<andNotIn field="" name="" value="" match="" />
<orNotIn field="" name="" value="" match="" />
```

### 🎏 2. 属性介绍 :id=in-property

- **field**，表示对应数据库或实体的字段，可以是数据库的表达式、函数等。**必填**属性。
- **name**，表示JPA 中生成的 JPQL 语句中的命名参数名称，`v2.3.0` 版本新增的属性。当不填写或者内容为空时，将默认根据 `value` 的值来生成命名参数名称。**非必填**属性。该字段通常用来解决 `value` 值比较复杂，为表达式时，生成的命名参数不对的问题。
- **value**，表示参数的集合，值可以是数组，也可以是 `Collection` 集合，还可以是单个的值。**必填**属性。
- **match**，表示匹配条件。**非必填**属性，如果不填此属性，或者内容为空，则视为必然生成此条件 SQL 片段；否则匹配结果为 `true` 时才生成，匹配结果为 `false`时不生成。

### 🎀 3. 使用示例 :id=in-demo

```xml
<andIn field="u.sex" value="userMap.sexs" match="?userMap.sexs != empty"/>
<!-- 如果 userMap 中的 sexs 不等于空时，才生成下面这条 SQL 片段和参数. -->
AND u.sex in :userMap_sexs

<andIn field="u.sex" name="mySex" value="userMap.sexs"/>
<!-- 生成下面这条 SQL 片段和参数，通过 name 属性，自定义了 SQL 中的命名参数为 mySex. -->
AND u.sex in :mySex
```

## 🐱 八、is null :id=is-null

### 🎁 1. 标签 :id=is-null-tag

```xml
<!-- IS NULL 相关的标签. -->
<isNull field="" match=""/>
<andIsNull field="" match=""/>
<orIsNull field="" match=""/>

<!-- IS NOT NULL 相关的标签. -->
<isNotNull field="" match=""/>
<andIsNotNull field="" match=""/>
<orIsNotNull field="" match=""/>
```

### 🤿 2. 属性介绍 :id=is-null-property

- **field**，表示对应数据库或实体的字段，可以是数据库的表达式、函数等。**必填**属性。
- **match**，表示匹配条件。**非必填**属性，如果不填此属性，或者内容为空，则视为必然生成此条件 SQL 片段；否则匹配结果为 `true`时才生成，匹配结果为 `false`时不生成。

### 🪁 3. 使用示例 :id=is-null-demo

```xml
<andIsNull field="u.n_age" match="?id != empty"/>
<!-- 如果 id 不等于空时，才生成下面这条 SQL 片段和参数 -->
AND u.n_age IS NULL
```

## 🐎 九、trimWhere :id=trimWhere

`trimWhere` 标签属于 `v2.5.0` 版本新增的标签。主要用于在全动态 SQL 的场景中消除 `WHERE` 关键字后面的 `AND` 或者 `OR` 关键字，防止拼接出的动态 SQL 语法不对。

> **💡 注**：本 `trimWhere` 标签修改自之前版本中的 `where` 标签，不过 `where` 标签存在已知的 `bug`。因为 `where` 允许和其他的动态条件标签写在 XML 的同一层级，当动态标签都不满足条件时，会导致结果多一个 `WHERE` 关键字，导致最终生成的 JPQL 或 SQL 无法正确的运行。为了保持对以前 `where` 标签写法的兼容。所以，**在 `v2.5.0` 版本中新增了标签 `trimWhere` 标签。可以用于完全替代 `where` 标签，且写法上只允许包裹式的写法，并且以前的 `where` 标签将继续保留但不再推荐使用**。后续也将只会介绍 `trimWhere` 标签。

### 🔮 1. 标签 :id=where-tag

下面是 `trimWhere` 标签的使用方式。

```xml
<!-- 将动态条件包裹在 trimWhere 标签内部即可. -->
<trimWhere>
    <!-- 在 trimWhere 标签块中可以书写任何文本 SQL 或者 XML 语义化 SQL 标签. -->
</trimWhere>
```

### 🧿 2. 使用示例 :id=where-demo

```xml
<!-- 用于演示 trimWhere 标签的使用，假如 user.email 的值为空，那么生成的 SQL 结果为: -->
<!-- SELECT u FROM User WHERE u.id = :user_id AND u.name LIKE :user_name ORDER BY u.updateTime DESC -->
<fenix id="testWhere">
    SELECT u FROM @{entityName}
    <trimWhere>
        anD u.id = #{user.id}
        <andEqual field="u.email" value="user.email" match="user.email != empty"/>
        <andLike field="u.name" value="user.name" match="user.name != empty"/>
    </trimWhere>
    ORDER BY u.updateTime DESC
</fenix>

<!-- 用于演示 trimWhere 标签的使用，假如 user.email 和 birthday 的值都为空，那么生成的 SQL 结果为: -->
<!-- SELECT u FROM User -->
<fenix id="testWhere2">
    SELECT u FROM @{entityName}
    <trimWhere>
        <andEqual field="u.email" value="user.email" match="user.email != empty"/>
        <andLike field="u.birthday" value="user.birthday" match="user.birthday != empty"/>
    </trimWhere>
</fenix>
```

## 🐅 十、text :id=text

`text` 标签主要用于在标签内部自定义任何需要的文本和传递的参数，为 SQL 书写提供更多的灵活性。

### 🕹️ 1. 标签 :id=text-tag

```xml
<text value="" match="">
    <!-- 在 text 块中可以书写任何文本内容，value 值的类型必须是 Map 类型. -->
</text>
```

### 🧸 2. 属性介绍 :id=text-property

- **value**，表示 `text` 块中需要传递的 `Map` 型参数。**非必填**属性。`Map` 中的 `key` 必须是“死”字符串，用于和 `JPQL` 的命名参数相呼应，`value` 的值才可以被动态解析；
- **match**，表示匹配条件。**非必填**属性，如果不填此属性，或者内容为空，则视为必然生成此条件 SQL 片段；否则匹配结果为 `true`时才生成，匹配结果为 `false`时不生成。

### 🖼️ 3. 使用示例 :id=text-demo

```xml
<!-- value 值必须是 MVEL 表达式中的 Map 类型，其 key 应该与绑定参数 :userId 向同. -->
<text value="['userId': user.id]">
    u.id = :userId
</text>

<!-- 当用户名和邮箱都不为空（要对 '&&' 符号做转义）时，才生成下面的 JPQL 语句. -->
<text value="['userName': user.name, 'email': '%163.com']" match="user.name != empty &amp;&amp; email != empty">
    AND u.name = :userName AND u.email LIKE :email
</text>
```

## 🐴 十一、import :id=import

`import` 标签主要用于在 Fenix 标签中导入其它的 `<fenix></fenix>` 节点，便于 SQL 逻辑的进一步复用。

### 🧵 1. 标签 :id=import-tag

```xml
<import fenixId=""/>
<import fenixId="" match=""/>
<import fenixId="" value="" match=""/>
<import namespace="" fenixId="" value="" match=""/>
```

### 🧶 2. 属性介绍 :id=import-property

- **namespace**，表示需要引用导入的节点所在的 `XML` 文件的命名空间，**非必填**属性。如果如果不填此属性，则视为仅从本 `XML` 文件中查找和导入 `fenixId` 的节点。
- **fenixId**，表示要引用导入的 `<fenix></fenix>` 节点的 `id`，**必填**属性。
- **value**，表示需要传入到要引用的 `<fenix></fenix>` 节点中的上下文参数值，**非必填**属性。如果不填此属性，则会传递和使用最顶层的上下文参数。
- **match**，表示匹配条件。**非必填**属性，如果不填此属性，或者内容为空，则视为必然生成此条件 SQL 片段；否则匹配结果为 `true` 时才生成，匹配结果为 `false`时不生成。

### 🛍️ 3. 使用示例 :id=import-demo

```xml
<!-- 一些公共字段的 fenix 节点. -->
<fenix id="commonFields">
    u.id, u.name, u.email
</fenix>

<!-- 一些公共查询条件的 fenix 节点. -->
<fenix id="commonConditions">
    <isNotNull field="u.id" value="user.id"/>
    <andIn field="u.name" value="names" match="names != empty"/>
    <orEndsWith field="u.email" value="email" match=""/>
</fenix>

<!-- 可以使用 import 标签导入本 XML 文件中的其他 fenix 节点. -->
<fenix id="testImport">
    SELECT
    <import fenixId="commonFields"/>
    FROM @{entityName} AS u
    WHERE
    <import fenixId="commonConditions" match="user.name != empty &amp;&amp; email != empty"/>
</fenix>
```

下面是用于从另一个 `UserRepository.xml` 文件中导入（`import`）`<fenix></fenix>` 节点 `id`.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!-- UserRepository. -->
<fenixs namespace="com.blinkfox.fenix.repository.UserRepository">

    <fenix id="UserHeader">
        SELECT u FROM User AS u
        WHERE
    </fenix>

    <!-- 根据多个 ID 来查询用户信息. -->
    <fenix id="queryUserByIds">
        <import fenixId="UserHeader"/>
        <in field="u.id" value="userMap.ids"/>
    </fenix>

</fenixs>
```

```xml
<!-- 用于测试 import 标签导入而设计的 XML 节点. -->
<fenix id="commonSql">
    <andIn field="u.name" value="userMap.names" match="userMap.names != empty"/>
    <andEndsWith field="u.email" value="user.email" match="user.email != empty"/>
</fenix>

<!-- 从本 XML 文件和 UserRepository.xml 中 import 导入 sql 片段. -->
<fenix id="testImport2">
    <import namespace="com.blinkfox.fenix.repository.UserRepository" fenixId="queryUserByIds"/>
    <import fenixId="commonSql"/>
</fenix>
```

## 🐄 十二、choose :id=choose

`choose` 标签主要用于解决"较多的"多分支条件选择逻辑，对应的即是Java中 `if/else if/ ... /else if/else` 这种逻辑。

### 📿 1. 标签 :id=choose-tag

```xml
<choose when="" then=""
        when2="" then2=""
        when3="" then3=""
        ...
        else=""/>
```

### 📯 2. 属性介绍 :id=choose-property

- **when{x}**，表示匹配条件，可以写“无数”个，对应于Java中的 `if/else if` 条件。**必填**属性，如果不填此属性，表示 `false`，将直接进入 `else` 的表达式逻辑块中。
- **then{x}**，表示需要执行的逻辑，和 `when` 向对应，可以写“无数”个，内容是字符串或者是 `MVEL` 的字符串模版，**必填**属性。如果如果不填此属性，即使满足了对应的 `when` 条件，也不会做 SQL 的拼接操作。
- **else**，表示所有 `when` 条件都不满足时才执行的逻辑，内容是字符串或者 `MVEL` 的字符串模版，**非必填**属性。如果不填此属性，则表示什么都不做（这样就无任何意义了）。

### 🎙️ 3. 使用示例 :id=choose-demo

```xml
<!-- choose 标签的使用示例. -->
<fenix id="choose">
    UPDATE t_user SET u.c_sex =
    <choose when="user.sex == '0'" then="'female'"
            when2="user.sex == '1'" then2="'male'"
            else="unknown" />
    , u.c_status =
    <choose when="?state != empty" then="'yes'" else="'no'" />
    , u.c_age =
    <choose when="age > 60" then="'老年'"
            when2="age > 35" then2="'中年'"
            when3="age > 20" then3="'青年'"
            when4="age > 10" then4="'少年'"
            else="'幼年'" />
    WHERE u.c_id = '@{user.id}'
</fenix>
```

## 🐄 十三、set :id=set

`set` 标签主要用于动态生成 `update` 语句中的 SQL 片段。

> **💡 注**：从 `2.4.0` 版本之后，增加了 `saveOrUpdateByNotNullProperties` 的**增量更新非`null`字段**的方法。大多数情况下，你可以不使用 `set` 标签了。

### 📻 1. 标签 :id=set-tag

```xml
<set field="" value="" match=""
        field2="" value2=""
        field3="" value3="" match3=""
        field4="" value4="" match4=""/>
```

### 📱 2. 属性介绍 :id=set-property

- **field{x}**，表示对应数据库或实体的字段，可以写“无数”个。**必填**属性。
- **value{x}**，表示对应字段的值，可以写“无数”个，对应 `MVEL` 表达式，也可以是基础数据类型，如：数字、字符串等，与相同序号的 `field` 值相对应。**非必填**属性。不填写此值则是 `null` 值。
- **match{x}**，表示匹配条件，可以写“无数”个，与相同序号的 `field` 和 `value` 值相对应。**非必填**属性，如果不填此属性，或者内容为空，则视为必然生成此条件 SQL 片段；否则匹配结果为 `true` 时才生成，匹配结果为 `false`时不生成。

### ☎️ 3. 使用示例 :id=set-demo

```xml
<!-- 测试 set 相关标签的更新. -->
<fenix id="testSet">
    UPDATE User
    <set field="name" value="user.name" match1="user.name != empty"
            field2="email" value2="user.email"
            field3="age" value3="user.age" match3="user.?age != empty"
            field4="sex" value4="user.sex" match4="" />
    WHERE id = '@{user.id}'
</fenix>

<!-- 测试使用原生 SQL 来做 更新. -->
<fenix id="testNativeSet">
    UPDATE t_user
    <set field="c_name" value="user.name" match1="user.name != empty"
            field2="c_email" value2="user.email"
            field3="n_age" value3="user.age" match3="user.?age != empty"
            field4="c_sex" value4="user.sex" match4="" />
    WHERE c_id = '@{user.id}'
</fenix>
```
