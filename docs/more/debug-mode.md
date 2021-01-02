# 🐛 调试 (debug) 模式 :id=debug-mode

Fenix 从 `2.4.1` 版本开始，新增回了 debug 调试模式的功能，也就是可以在不重启服务的情况下，每一条对 XML SQL 的请求，都会实时从 XML 文件流中读取和解析 SQL 语句。

> **💡 注**：其实该功能在 `1.0.0` 版本中就有，只不过当时的 IDEA 在识别更新的文件时有些问题，导致该功能没有啥效果，所以就去掉了。现在又将此功能加了回来，更加方便开发人员快速、动态的调试 SQL 语句了。

## 📝 一、开启 debug 模式 :id=enable-debug

如果你是使用的 `fenix-spring-boot-starter` 组件，那么只需要在你的 `application.yml` 或者 `application.properties` 文件中，将 `fenix.debug` 设置为 `true` 即可。

```yaml
fenix:
  # v2.4.1 版本新增，表示是否开启 debug 调试模式，默认 false。
  # 当开启之后，对 XML 中的 SQL 会进行实时文件流的读取和解析，不需要重启服务。切记仅在开发环境中开启此功能.
  debug: true
```

如果不是使用的 `fenix-spring-boot-starter` 组件，那么需要在你构造的 `FenixConfig` 实例对象中设置 `setDebug` 为 `true` 即可。

## 🧬 二、设置 IDEA 中的热更新策略 :id=idea-settings

当然，还需要你开发使用的 IDE 能在服务运行期间动态识别到文件资源的更新，不同的 IDE 工具支持可能有差别。

如果你使用的是 Intellij IDEA，那么可以通过 `Edit Configurations` 编辑应用运行时的更新策略。你可以将 `On 'update' action` 和 `On frame deactivation` 的值设置为：`Update classes and resources`。设置的结果效果图如下：

![IDEA 中的资源更新的设置](https://statics.sh1a.qingstor.com/2021/01/02/idea-update.png)
