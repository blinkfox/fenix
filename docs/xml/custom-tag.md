# 🍄 自定义标签 :id=title

语义化标签生成的动态 SQL 片段和参数大大简化了复杂、动态 SQL 的代码量，但是项目开发的过程中往往还有更多复杂的逻辑来生成某些 SQL，甚至那些逻辑还要被多处使用到，默认的一些标签可能不能够满足开发的需求，那么自定义自己的动态逻辑的SQL 语义化标签来实现就显得很重要了。

所谓自定义标签和处理器就是根据自己的需求来设计自定义的标签名称、匹配条件、参数或者数据库字段等，再通过开发者自定义的处理器来控制生成 SQL 的逻辑，这样就可以达到生成我们需要的动态逻辑的 SQL，这样的标签重大的意义在于 SQL 语义明确，且能够最大化简化 SQL 的书写和功能的复用。

## 🌄 一、假设业务需求 :id=business-demand

假设有一个全国的业务管理系统，有这样的业务场景和需求，登录的用户只能查询到自己**管辖区域**的业务数据，而系统中几乎每个查询页面都要做这样的逻辑控制，具体的需求和查询逻辑如下：

- 如果登录人的级别是**区县级**用户，那么他只能查询到区县级的数据；
- 如果登录人的级别是**地市级**用户，那么他能查询到他所管辖的地市下的所有区县级的数据；
- 如果登录人的级别是**省级**用户，那么他能查询到他所管辖的省份下的所有数据；
- 如果登录人的级别是**全国中央级**的用户，那么他能查询到所有数据；

## 🏙️ 二、解决方法 :id=solution

针对这样的需求，使用 Fenix 也有几种方案可以做到，

1. 第一种，使用模板引擎中的逻辑控制语法（[@if{} @else{}](xml/logic-control?id=if-else)），在每个查询语句中都复制粘贴这条 SQL。或者更好点儿的办法，是将其做成一个单独的 `fenix` XML 节点，每个用到的地方都用 [import](xml/xml-tags?id=import) 标签来导入即可，能最大限度的防止代码重复。
2. 第二种，使用 [<choose />](xml/xml-tags?id=choose) 标签来做逻辑控制，将其做成一个单独的 `fenix` XML 节点，每个用到的地方都用 [import](xml/xml-tags?id=import) 标签来导入即可，能最大限度的防止代码重复。

以上两种方案，都可行，也都能防止 SQL 代码重复。但我认为还不是最好的。我们可以将以上逻辑做成一个语义化的 XML SQL 标签，只需要传递待查询的字段 `field` 和 `userId` 参数，然后通过我们书写一些 Java 的逻辑代码来根据 userId 的情况，生成不同的 SQL 片段和参数即可。

Fenix 也为开发者扩展自己的 XML SQL 语义化标签提供了支持。

## 🌅 三、设计 XML 语义化标签 :id=design-xml-tag

根据前面的需求，我们需要传入一个用户 ID（`userId`）的参数和具体业务表的”地区“字段，每个业务表的地区字段可能名称不一样，所以这里也设计为参数。

那么按地区查询的数据权限的 `XML` 语义化标签可以设计成下面这样，当然 `XML` 标签的名称你可以根据自己的需要来命名。

```xml
<regionAuth field="" userId=""/>

<!-- 前面附带 AND 前缀的标签. -->
<andRegionAuth field="" userId=""/>
```

## 🌁 四、创建 XML 标签的处理器 :id=xml-handler

当你定义了自己的标签和属性之后，就需要创建一个能够识别、读取和按需要的逻辑拼接生成出 SQL 片段和参数的标签处理器。

首先，创建一个实现了 `FenixHandler` 接口的 `RegionAuthHandler.java` 类，然后重写 `buildSqlInfo` 方法。

主要处理的代码和详细的注释如下：

```java
package com.blinkfox.fenix.example.handler;

import com.blinkfox.fenix.bean.BuildSource;
import com.blinkfox.fenix.config.annotation.Tagger;
import com.blinkfox.fenix.consts.Const;
import com.blinkfox.fenix.consts.SymbolConst;
import com.blinkfox.fenix.core.FenixHandler;
import com.blinkfox.fenix.helper.ParseHelper;
import com.blinkfox.fenix.helper.XmlNodeHelper;

import java.util.Map;

import org.dom4j.Node;

/**
 * 实现了 {@link FenixHandler} 接口的自定义地区权限控制的标签处理器.
 *
 * @author blinkfox on 2019-08-17.
 */
@Tagger(value = "regionAuth")
@Tagger(value = "andRegionAuth", prefix = " AND ")
public class RegionAuthHandler implements FenixHandler {

    /**
     * 用于 JPQL 绑定变量的命名参数常量.
     */
    private static final String REGION_ANME = "region";

    /**
     * 根据 {@link BuildSource} 的相关参数来追加构建出对应 XML 标签的 JPQL 语句及参数信息.
     *
     * @param source 构建 SQL 片段和参数所需的 {@link BuildSource} 资源对象
     */
    @Override
    public void buildSqlInfo(BuildSource source) {
        // 从 source 参数中获取到 XML Node 节点、拼接 SQL 的 StringBuilder 对象和 params 参数.
        Node node = source.getNode();
        StringBuilder join = source.getSqlInfo().getJoin();
        Map<String, Object> params = source.getSqlInfo().getParams();

        // 获取 field 和 userId 属性的文本值，该方法会检测属性值是否为空，如果为空，会抛出异常.
        String fieldText = XmlNodeHelper.getAndCheckNodeText(node, "attribute::field");
        String userIdText = XmlNodeHelper.getAndCheckNodeText(node, "attribute::userId");

        // 根据上下文参数，解析出 userId 的真实值，我这里假设 userId 为 String 型，你可以根据实际情况转换成其他类型.
        String userId = (String) ParseHelper.parseExpressWithException(userIdText, source.getContext());

        // 假定从用户缓存中获取到该用户信息，从而得到用户地区级别和所在的地区编码.
        User user = UserCache.get(userId);
        int level = user.getLevel();
        String region = user.getRegion();

        // 如果是中央级（level == 0）的用户，由于是要查看全部数据，所以不用生成查询条件的 SQL 片段，直接返回即可.
        if (level == 0) {
            return;
        }

        // 如果该用户是省级（level == 1）用户，则生成 x.region LIKE :region，
        // 参数值为地区编码的前 2 位数的前缀匹配，因为前 2 位相同说明是同一个省份的.
        if (user.getLevel() == 1) {
            join.append(source.getPrefix()).append(fieldText)
                    .append(SymbolConst.LIKE).append(Const.COLON).append(REGION_ANME);
            params.put(REGION_ANME, region.substring(0, 2) + "%");
        } else if (user.getLevel() == 2) {
            // 如果该用户是地市级（level == 2）用户，则生成 x.region LIKE :region，
            // 参数值为地区编码的前 4 位数的前缀匹配，因为前 4 位相同说明是同一个地市的.
            join.append(source.getPrefix()).append(fieldText)
                    .append(SymbolConst.LIKE).append(Const.COLON).append(REGION_ANME);
            params.put(REGION_ANME, region.substring(0, 4) + "%");
        } else if (user.getLevel() == 3) {
            // 如果该用户是区县级（level == 3）用户，则直接生成等值查询即可。x.region = :region.
            join.append(source.getPrefix()).append(fieldText)
                    .append(SymbolConst.EQUAL).append(Const.COLON).append(REGION_ANME);
            params.put(REGION_ANME, region);
        }
    }

}
```

## 🌃 五、配置标签和处理器的映射关系 :id=tag-handler-mapping

上面的 `RegionAuthHandler` 类中其实已经使用 `@Tagger` 注解配置了标签和处理器之间的映射关系了。`@Tagger` 是一个重复注解，可以在类中配置多个。

```java
@Tagger(value = "regionAuth")
@Tagger(value = "andRegionAuth", prefix = " AND ")
```

然后，你还需要告诉 Fenix 扫描哪些包下 `Handler` 注解，这样才能将注解和处理器类之间的映射扫描加载到内存中。这样以后的其他自定义标签处理器也都可以存放到这个包下。

所以，你需要在 `application.yml` 文件中，配置扫描你自定义的标签处理器的包路径或者 Java 文件的路径。

```yaml
# Fenix 的几个配置项、默认值及详细说明.
fenix:
  # 扫描你自定义的 XML 标签处理器的位置，默认为空，可以是包路径，也可以是 Java 或 class 文件的全路径名.
  # 可以使用 yaml 集合的形式配置多个值，properties 文件则用英文逗号隔开.
  handler-locations:
    - com.blinkfox.fenix.example.handler
```

**`@Tagger` 注解介绍**：

- `value()` 元素是 XML 的标签名称。
- `prefix()` 元素是前缀，如：`AND`、`OR` 等，默认值是空字符串，你也可以设置为其他值。
- `symbol()` 元素是操作符，我们的标签中没用这个值，你也可以把他当任何的参数来传，这样 `BuildSource` 参数中就能获取到这个值，你就可以任意拼接参数了。

## 🌉 六、使用示例 :id=demo

由于我是模拟的业务场景，就不再真实的去创建表、初始化数据，并执行 SQL 了，以下就列出使用场景的示例，供你参考即可：

```xml
SELECT
    x
FROM
    XxxEntity AS x
WHERE
<regionAuth field="x.region" userId="searchMap.userId"/>
<!-- 下面是其他的查询条件. -->
<andLike field="x.name" value="searchMap.xxxName" match="searchMap.?xxxName != empty"/>
...
```

使用 `AND` 的情况：

```xml
SELECT
    x
FROM
    XxxEntity AS x
WHERE
<like field="x.name" value="searchMap.xxxName" match="searchMap.?xxxName != empty"/>
<andRegionAuth field="x.region" userId="searchMap.userId"/>
<!-- 下面是其他的查询条件. -->
...
```

**💡 注意事项**：

- 🔹 上面的 `XxxEntity` 是假想中的业务实体类，
- 🔹 `x.region` 是该实体类中对应的地区字段值，这里假设为 `6` 位数的地区编码，前两位数表示省份，前四位数表示地市，全体 `6` 位数表示具体的区县编码。所以，`6` 位数都相同说明是一个区县，前 `4` 位数相同说明是在一个地市，前两位数相同说明是在一个省份。
- 🔹 `searchMap` 假设为是传递过来的 `Map` 型参数，当然视具体情况，你也可以传递为 `Bean` 或者单个散参数。这里只是示例。
