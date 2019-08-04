package com.blinkfox.fenix.config.entity;

import com.blinkfox.fenix.helper.StringHelper;

import java.util.HashMap;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Fenix XML 文件对应的上下文配置的单例类.
 *
 * @author blinkfox on 2019-08-04.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class XmlContext {

    /**
     * 创建 {@link XmlContext} 的唯一实例.
     */
    private static final XmlContext xmlContext = new XmlContext();

    /**
     * 所有 Fenix XML 文档的缓存上下文的 map，其中 key 是文件命名空间标识，value 是 XML 文件所对应的资源路径.
     */
    @Getter
    private final Map<String, String> xmlPathMap = new HashMap<>();

    /**
     * 获取并返回 {@link XmlContext} 的唯一实例.
     *
     * @return XmlContext 实例
     */
    public static XmlContext getInstance() {
        return xmlContext;
    }

    /**
     * 添加 XML 命名空间和文件路径到 `xmlPathMap` 中.
     *
     * @param namespace XML 文件命名空间
     * @param filePath XML 文件资源路径
     */
    public void add(String namespace, String filePath) {
        if (StringHelper.isBlank(namespace) || StringHelper.isBlank(filePath)) {
            log.warn("【Fenix 警告】Fenix XML 文件中命名空间标识或者 XML 文件路径为空!");
            return;
        }

        // 将 XML 命名空间的标识和其对应的文件资源路径存放到 Map 中.
        xmlPathMap.put(namespace, filePath);
    }

}
