# 自定义条件注解

如果 Fenix 中内置的注解不满足你的场景，你可以自定义注解和对应注解的处理器来达到自己的目的。

## 1. 定义你自己的注解

假设你想定义一个我自己的等值条件的注解 `@MyEquals`，来达到自己的相等条件的处理的能力。注解的代码的定义如下：

```java
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyEquals {

    /**
     * 注解的实体字段属性名称，默认为空或空字符串时将使用属性名称.
     *
     * @return 字符串值
     */
    String value() default "";

}
```

## 2. 创建你注解的处理器

然后，需要创建一个你注解的处理器类 `MyEqualsPredicateHandler.java`，该类须要继承 `AbstractPredicateHandler` 抽象类。然后实现其中的 `getAnnotation()` 和 `buildPredicate()` 方法即可。

代码示例如下：

```java
public class MyEqualsPredicateHandler extends AbstractPredicateHandler {

    /**
     * 返回你自定义的注解.
     */
    @Override
    public Class<MyEquals> getAnnotation() {
        return MyEquals.class;
    }

    /**
     * 构造相等条件逻辑的 {@link Predicate} 条件的方法.
     *
     * @param criteriaBuilder {@link CriteriaBuilder} 实例
     * @param from {@link From} 实例
     * @param fieldName 实体类的属性名
     * @param value 对应属性的值
     * @param annotation 前字段使用的注解
     * @param <Z> 范型 Z
     * @param <X> 范型 X
     * @return {@link Predicate} 实例
     */
    @Override
    public <Z, X> Predicate buildPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value, Annotation annotation) {
        // 此处的处理逻辑代码，你就可以自由发挥了.
        return criteriaBuilder.and(criteriaBuilder.equal(from.get(fieldName), value));
    }

}
```

## 3. 将处理器类加入到初始化配置中

最后一步，就是将上述 `MyEqualsPredicateHandler` 的处理器类添加到 Fenix 配置中，使得的系统初始化时将该处理器的实例和注解的映射信息加载到内存中，方便后续使用。

### (1) Spring Boot 项目的自定义注解处理器配置

如果你是 Spring Boot 项目，那么只需要在 `fenix.predicate-handlers` 属性中添加该处理器的全路径名即可，示例如下：

```yaml
# Fenix 的几个配置项、默认值及详细说明，通常情况下你不需要填写这些配置信息.
fenix:
  # v2.2.0 版本新增的配置项，表示自定义的继承自 AbstractPredicateHandler 的子类的全路径名
  # 可以配置多个值，通常情况下，你也不需要配置这个值.
  predicate-handlers:
    - com.xxx.yyy.handler.MyEqualsPredicateHandler
    - com.xxx.yyy.handler.MyOtherPredicateHandler
```

### (2) 非 Spring Boot 项目的自定义注解处理器配置

如果你的项目不是 Spring Boot 项目，那么在你的初始化代码中，通过 `FenixConfig.add(handler);` 代码来添加处理器的实例即可。示例代码如下：

```java
// 初始化加载 Fenix 的配置信息.
FenixConfigManager.getInstance().initLoad();

// 添加自定义的处理器的对象实例到 FenixConfig 中，可以添加任意多个处理器实例.
FenixConfig.add(new MyEqualsPredicateHandler());
FenixConfig.add(new MyOtherPredicateHandler());
```

最后，就可以使用 `@MyEquals` 注解了。
