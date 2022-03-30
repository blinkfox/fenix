# 🐝 其他功能 :id=others

## 🦗 一、从 XML 中获取 SQL 信息 :id=get-xml-sql

Fenix 中会自动从 `XML` 中获取到 SQL 信息。如果你想手动从 `XML` 中获取到 SQL 信息（`SqlInfo`），也可以使用 `Fenix.java` 提供的 `API` 来获取。

```java
// 通过传入完整的 fullFenixId（命名空间、'.'号和 Fenix 节点的 ID）和上下文参数，来简单快速的生成和获取 SqlInfo 信息.
Fenix.getXmlSqlInfo(String fullFenixId, Object context)

// 通过传入 Fenix XML 文件对应的命名空间、Fenix 节点的 ID 以及上下文参数对象，来生成和获取 SqlInfo 信息.
Fenix.getXmlSqlInfo(String namespace, String fenixId, Object context)
```

## 🐜 二、表达式、模版解析器 :id=express-template-parser

在 Fenix 中解析 XML 标签中的表达式或者模版是通过 `Mvel` 表达式语言来实现的，主要方法解析方法是封装在了`ParseHelper.java` 的工具类中，通过该类让开发人员自己测试表达式也是极为方便的。以下作简要介绍。

### 💉 1. 解析表达式 :id=parse-express

#### 主要方法

```java
// 解析出表达式的值，如果解析出错则不抛出异常，但会输出 error 级别的异常，返回 null.
Object parseExpress(String exp, Object paramObj);

// 解析出表达式的值，如果解析出错则抛出异常.
Object parseExpressWithException(String exp, Object paramObj);
```

#### 使用示例

```java
@Test
public void testParseWithMvel() {
    // 构造上下文参数
    Map<String, Object> context = new HashMap<String, Object>();
    context.put("foo", "Hello");
    context.put("bar", "World");

    // 解析得到 'HelloWorld' 字符串，断言为: true.
    String result = (String) ParseHelper.parseExpressWithException("foo + bar", context);
    assertEquals("HelloWorld", result);
}

@Test
public void testParseStr2() {
    Boolean result = (Boolean) ParseHelper.parseExpress("sex == 1", ParamWrapper.newInstance("sex", "1").toMap());

    // 断言为: true.
    assertEquals(true, result);
}
```

### 🐴 2. 解析模版 :id=parse-template

#### 主要方法

```java
// 解析出模板字符串中的值，如果解析出错则抛出异常.
String parseTemplate(String template, Object context)
```

#### 使用示例

```java
@Test
public void testParseTemplate2() {
    String result = ParseHelper.parseTemplate("@if{?foo != empty}@{foo} World!@end{}",
            ParamWrapper.newInstance("foo", "Hello").toMap());

    // 解析得到 'Hello World!' 字符串，断言为:true.
    assertEquals("Hello World!", result);
}
```

## 🦗 三、上下文参数包装类 :id=param-wrapper

Fenix 中提供了一个包装上下文参数为 `HashMap` 的包装器 `ParamWrapper` 工具类，其本质上就是对 `HashMap` 方法的一个**简单链式封装**。

> **💡 注**：提供该包装器类的主要目的是方便开发者封装较多的散参数或者多个 Java 对象为一个 `Map` 型的上下文参数。

### 🛏️ 1. ParamWrapper 主要方法 :id=main-methods

- `newInstance()`，创建新的`ParamWrapper`实例。
- `newInstance(Map<String, Object> paramMap)`，传入已有的`Map`型对象，并创建新的`ParamWrapper`实例。
- `newInstance(String key, Object value)`，创建新的`ParamWrapper`实例，并创建一对key和value的键值对。
- `put(String key, Object value)`，向参数包装器中，`put`对应的key和value值。
- `toMap()`，返回填充了key、value后的Map对象。

### 🦊 2. 对比的示例 :id=compare-demo

以前需要开发者自己封装Map：

```java
Map<String, Object> context = new HashMap<String, Object>();
context.put("sex", "1");
context.put("stuId", "123");
```

现在的使用方式：

```java
Map<String, Object> context = ParamWrapper.newInstance("sex", "1").put("stuId", "123").toMap());
```

前后对比来看，再仅仅只需要传入个别自定义参数时，能简化部分代码量和参数传递。

## 🐞 四、表达式的真假判断 :id=true-or-false

Fenix 中关于表达式字符串的真假判断在 `com.blinkfox.fenix.helper.ParseHelper` 类中提供了静态方法。

**📌 主要方法**：

```java
// 是否匹配，常用于标签中的 match 值的解析，即如果 match 不填写，或者内容为空，或者解析出为正确的值，都视为true.
ParseHelper.isMatch(String match, Object context)

// 是否不匹配，同 isMatch 相反，只有解析到的值是 false 时，才认为是 false.
ParseHelper.isNotMatch(String match, Object context)

// 是否为 true，只有当解析值确实为 true 时，才为 true.
ParseHelper.isTrue(String exp, Object context)
```
