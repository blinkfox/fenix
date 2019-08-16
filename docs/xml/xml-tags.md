Fenix 的核心功能就在于将 SQL 单独写到 XML 文件中，为了增强写动态 SQL 的能力，引入了 `MVEL` 模版引擎，使得我们可以写出动态的 SQL。但是这样的 SQL 中就像 `MyBatis` 一样充斥着 `if/else` 或者 `foreach` 循环等等语句。不仅冗长、而且可读性也不好。

所以，在 Fenix 中将绝大多数常见的 SQL 片段进一步封装化，以 XML 的语义化标签的形式来生成 SQL 片段，为了杜绝大多数场景下 `if` 语句的使用，将 `if` 语句的条件本身进一步封装为一个 XML 标签的 `match` 属性，生成 SQL 时，判断 `match` 的属性内容（应该是一个 `MVEL` 表达式）的布尔结果值，如果为 `true` 就生成该条 SQL 片段，否则不生成。当然如果没有 `match` 属性，则表明是必然要生成此条 SQL 片段。

Fenix 中提供了大量常见场景下的 XML 标签供开发者使用，且这些标签大多数都配置有 `AND`、`OR`、`NOT` （即：与、或、非）等前缀，能够更大程度上精简动态 SQL。

这些动态的 Fenix XML SQL 片段标签，主要有如下几类：

- [equal](/xml/xml-tags?id=equal)
- [与 equal 类似的标签](/xml/xml-tags?id=equal-similar-tags)
- [like](/xml/xml-tags?id=like)
- [between](/xml/xml-tags?id=between)
- [in](/xml/xml-tags?id=in)
- [is null](/xml/xml-tags?id=is-null)
- [text](/xml/xml-tags?id=text)
- [import](/xml/xml-tags?id=import)
- [choose](/xml/xml-tags?id=choose)
- [set](/xml/xml-tags?id=set)

## equal

### 标签

```xml
<equal field="" value="" match=""/>
<andEqual field="" value="" match=""/>
<orEqual field="" value="" match=""/>
```

### 属性介绍

- **field**，表示对应数据库或实体的字段，也可以是数据库的表达式、函数等。必填属性。
- **value**，表示参数值，对应 `MVEL` 表达式，也可以是基础数据类型，如：数字、字符串等。必要填属性。
- **match**，表示匹配条件。非必填属性，如果不填此属性，或者内容为空，则视为必然生成此条件 SQL（`JPQL`） 片段；否则解析出的匹配结果为 `true` 时才生成，匹配结果为 `false` 时不生成。

### 使用示例

```xml
<equal field="nickname" value="name"/>
<!-- 要没有 match 属性将必然生成下面这条 SQL 片段和参数. -->
nickname = :name


<andEqual field="email" value="user.email" match="?email != empty"/>
<!-- 如果 email 不等于空（不为 null 或空字符串）时，才生成下面这条 SQL 片段和参数 -->
AND email = :user_email
```

## 与 equal 类似的标签 :id=equal-similar-tags

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

## like

### 标签

```xml
<like field="" value="" pattern="" match=""/>
<andLike field="" value="" pattern="" match=""/>
<orLike field="" value="" pattern="" match=""/>

<!-- not like的 相关标签. -->
<notLike field="" value="" pattern="" match=""/>
<andNotLike field="" value="" pattern="" match=""/>
<orNotLike field="" value="" pattern="" match=""/>
```

### 属性介绍

- **field**，表示对应数据库或实体的字段，也可以是数据库的表达式、函数等。必填属性。
- **value**，表示参数值，对应 `MVEL` 表达式，也可以是基础数据类型，如：数字、字符串等。条件必填属性。`pattern` 和 `value` 只能存在一个，`value` 生成的 SQL 片段默认是两边模糊，即：`%%`。
- **pattern**，表示 `like` 匹配的模式，如：`abc%`、`_bc`等，只能是静态文本内容。条件必填属性。`pattern` 和 `value` 只能存在一个，`pattern` 用来指定自定义的匹配模式。
- **match**，表示匹配条件。非必填属性，如果不填此属性，或者内容为空，则视为必然生成此条件 SQL 片段；否则匹配结果为 `true` 时才生成，匹配结果为 `false` 时不生成。

### 使用示例

```xml
<andLike field="u.email" value="email" match="?email != empty"/>
<!-- 如果 email 不等于空时，才生成下面的这条 SQL 片段和参数. -->
AND u.email LIKE :email

<notLike field="u.email" pattern="%@gmail.com"/ >
<!-- 匹配所有不是 gmail 的邮箱，将生成下面这这样的 SQL 片段. -->
u.email NOT LIKE '%@gmail.com'
```

## between

### 标签

```xml
<between field="" start="" end="" match=""/>
<andBetween field="" start="" end="" match=""/>
<orBetween field="" start="" end="" match="" />
```

### 属性介绍

- **field**，表示对应数据库或实体的字段，可以是数据库的表达式、函数等。必填属性。
- **start**，表示区间匹配条件的开始参数值，对应 `MVEL` 表达式，条件必填。
- **end**，表示区间匹配条件的结束参数值，对应 `MVEL` 表达式，条件必填。
- **match**，表示匹配条件。非必填属性，如果不填此属性，则视为必然生成此条件 SQL 片段；否则匹配结果为 `true` 时才生成，匹配结果为 `false` 时不生成。

!> **注意**：Fenix 中对 start 和 end 的空判断是检测是否是 `null`，而不是空字符串，`0`等情况。所以，如果 `start` 和 `end` 的某一个值为 `null` 时，区间查询将退化为大于等于（`>=`）或者小于等于（`<=`）的查询。

### 使用示例

```xml
<andBetween field="u.age" start="startAge" end="endAge" match="(?startAge != null) || (?endAge != null)"/>
```

**解释**：

- 当 `start` 不为 `null`，`end` 为 `null`，则生成的 SQL 片段为：`AND u.age >= :startAge`；
- 当 `start` 为 `null`，`end` 不为 `null` ，则生成的 SQL 片段为：`AND u.age <= :endAge`；
- 当 `start` 不为 `null`，`end`不为`null`，则生成的 SQL 片段为：`AND u.age BETWEEN :startAge AND :endAge`；
- 当 `start` 为 `null`，`end`为`null`，则不生成SQL片段；

## in

### 标签

```xml
<in field="" value="" match=""/>
<andIn field="" value="" match=""/>
<orIn field="" value="" match=""/>

<!-- not in 相关的标签. -->
<notIn field="" value="" match=""/>
<andNotIn field="" value="" match=""/>
<orNotIn field="" value="" match=""/>
```

### 属性介绍

- **field**，表示对应数据库或实体的字段，可以是数据库的表达式、函数等。必填属性。
- **value**，表示参数的集合，值可以是数组，也可以是 `Collection` 集合，还可以是单个的值。必填属性。
- **match**，表示匹配条件。非必填属性，如果不填此属性，则视为必然生成此条件 SQL 片段；否则匹配结果为 `true` 时才生成，匹配结果为 `false`时不生成。

### 使用生成示例

```xml
<andIn field="u.sex" value="userMap.sexs" match="?userMap.sexs != empty"/>
<!-- 如果 userMap 中的 sexs 不等于空时，才生成下面这条 SQL 片段和参数. -->
AND u.sex in :userMap_sexs
```

## is null

### 标签

```xml
<isNull match="" field="" />
<andIsNull match="" field=""  />
<orIsNull match="" field="" />

<!-- IS NOT NULL 相关的标签. -->
<isNotNull match="" field="" />
<andIsNotNull match="" field="" />
<orIsNotNull match="" field="" />
```

### 属性介绍

- **match**，表示匹配条件。非必要（填）属性，如果不写（填）此属性，则视为必然生成此条件SQL片段；否则匹配结果为true时才生成，匹配结果为false时，不生成。
- **field**，表示对应数据库的字段，可以是数据库的表达式、函数等。必要（填）属性。

### 使用生成示例

```markup
<andIsNull match="?id != empty" field="s.n_age" />

SQL片段的生成结果：AND s.n_age IS NULL

解释：如果 id 不等于空时，才生成此条SQL片段和参数
```

## text

text标签主要用于在标签内部自定义需要的文本和需要传递的各种参数，为SQL书写提供灵活性。

### 标签

```xml
<text match="" value="">
    ...
</text>
```

### 属性介绍

- **match**，同上。
- **value**，表示参数的集合，值可以是数组，也可以是Collection集合，还可以是单个的值。必填

### 使用生成示例

```xml
<text match="" value="{name1, name2, email}">
    and name in (?, ?)
    and email = ?
</text>
```

```markup
SQL片段的生成结果：and name in (?, ?) and email = ?

解释：如果match为true、不填写或无match标签时，才生成此条SQL片段和自定义传递的参数，参数就是通过`name1`、`name2`和`email`组合成的数组或集合，或者直接传递集合或数组（此处组合而成的数组，如果是集合就把'{'换成'['即可）。
```

## import

import标签主要用于在zealot标签中导入其它公共的zealot节点，便于程序代码逻辑的复用。

### 标签

```xml
<import zealotid="" />
<import match="" zealotid="" />
<import match="" namespace="" zealotid="" value="" />
```

### 属性介绍

- **match**，表示匹配条件。非必要（填）属性，如果不写（填）此属性，则视为必然生成此条件SQL片段；否则匹配结果为true时才生成，匹配结果为false时，不生成。
- **namespace**，表示需要引用导入的节点所在的xml文件的命名空间，非必填属性。如果如果不写（填）此属性，则视为仅在本xml文件中查找对应的zealotId的节点。
- **zealotid**，表示要引用导入的zealot节点的ID，必填属性。
- **value**，表示需要传入到要引用的zealot节点中的上下文参数值，非必填属性。如果不写（填）此属性，则传递最顶层的上下文参数。

### 使用生成示例

```xml
<zealot id="commonStuCondition">
    <andMoreEqual match="?age > 0" field="s.n_age" value="age" />
    <andBetween match="(?startBirthday != null) || (?endBirthday != null)" field="s.d_birthday" start="startBirthday" end="endBirthday" />
</zealot>

<zealot id="queryStudents">
    ...
    <import zealotid="commonStuCondition" />
    ...
</zealot>
```

```markup
SQL片段的生成结果：AND s.n_age >= ? AND s.d_birthday BETWEEN ? AND ?
```

## choose

choose标签主要用于解决"无数的"多分支条件选择逻辑，对应的即是Java中`if/else if/ ... /else if/else`这种逻辑。

### 标签

```xml
<choose when="" then="" when2="" then2="" ... whenx="" thenx="" else="" />
```

### 属性介绍

- **when**，表示匹配条件，可以写无数个，对应于Java中的`if/else if`条件。必要（填）属性，如果不写（填）此属性，表示false，直接进入`else`的逻辑块中。
- **then**，表示需要执行的逻辑，和`when`向对应，可以写无数个，内容是字符串或者zealot的字符串模版，必要（填）属性。如果如果不写（填）此属性，即使满足了对应的`when`条件，也不会做SQL的拼接操作。
- **else**，表示所有when条件都不满足时才执行的逻辑，内容是字符串或者zealot的字符串模版，非必填属性。如果不写（填）此属性，则表示什么都不做（这样就无任何意义了）。

### 使用生成示例

```xml
<zealot id="queryByChoose">
    UPDATE t_student SET s.c_sex =
    <choose when="?sex == 0" then="'female'" when2="?sex == 1" then2="'male'" else="unknown" />
    , s.c_status =
    <choose when="?state" then="'yes'" else="'no'" />
    , s.c_age =
    <choose when="age > 60" then="'老年'" when2="age > 40" then2="'中年'" when3="age > 20" then3="'青年'" when4="age > 10" then4="'少年'" else="'幼年'" />
    WHERE s.c_id = '@{stuId}'
</zealot>
```

```markup
SQL片段的生成结果：UPDATE t_student SET s.c_sex = 'male' , s.c_status = 'no' , s.c_age = '幼年' WHERE s.c_id = '123'
```
