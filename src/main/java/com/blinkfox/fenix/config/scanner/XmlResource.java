package com.blinkfox.fenix.config.scanner;

import java.net.URL;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.dom4j.Document;

/**
 * XML 资源实体类.
 *
 * @author blinkfox on 2019-08-31.
 * @since v1.0.0
 */
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class XmlResource {

    /**
     * XML 中的命名空间.
     */
    private String namespace;

    /**
     * XML 资源文件的 URL.
     *
     * @since v2.4.1
     */
    private URL url;

    /**
     * XML 中的文档对象实例.
     */
    private Document document;

    /**
     * 包含有命名空间 namespace 和 dom4j 文档 document 对象的构造方法.
     *
     * @param namespace 命名空间
     * @param document dom4j 文档
     * @author blinkfox on 2021-01-02.
     * @since v2.4.1
     */
    public XmlResource(String namespace, Document document) {
        this.namespace = namespace;
        this.document = document;
    }

}
