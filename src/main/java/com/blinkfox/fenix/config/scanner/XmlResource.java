package com.blinkfox.fenix.config.scanner;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.dom4j.Document;

/**
 * XML 资源实体类.
 *
 * @author blinkfox on 2019-08-31.
 */
@Getter
@Setter
@Accessors(chain = true)
public class XmlResource {

    /**
     * XML 中的命名空间.
     */
    private String namespace;

    /**
     * XML 中的文件路径.
     */
    private String path;

    /**
     * XML 中的文档对象实例.
     */
    private Document document;

}
