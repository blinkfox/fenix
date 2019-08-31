package com.blinkfox.fenix.config.scanner;

import com.blinkfox.fenix.consts.Const;
import com.blinkfox.fenix.consts.XpathConst;
import com.blinkfox.fenix.exception.ConfigNotFoundException;
import com.blinkfox.fenix.exception.FenixException;
import com.blinkfox.fenix.helper.StringHelper;
import com.blinkfox.fenix.helper.XmlNodeHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;

/**
 * 用于扫描指定路径下 Fenix XML 文件资源的扫描器类.
 *
 * @author blinkfox on 2019-08-31.
 */
@Slf4j
public class XmlScanner {

    /**
     * 用于查找某个目录及目录下所有 XML 文件的模式常量.
     */
    private static final String DIR_XML_PATTERN = "**/*.xml";

    /**
     * 扫描指定路径下的相关文件(可以是目录，也可以是具体的文件)，并配置存储起来.
     *
     * @param xmlLocations 文件位置路径，可以是多个，用逗号隔开
     */
    public Map<String, XmlResource> scan(String xmlLocations) {
        Map<String, XmlResource> xmlResourceMap = new HashMap<>();
        if (StringHelper.isBlank(xmlLocations)) {
            return xmlResourceMap;
        }

        // 对配置的 XML 路径按逗号分割的规则来解析.
        String[] xmlLocationArr = xmlLocations.split(Const.COMMA);
        if (log.isDebugEnabled()) {
            log.debug("【Fenix 提示】将扫描这些位置的 Fenix XML 文件：【{}】", Arrays.asList(xmlLocationArr));
        }

        for (String xmlLocation: xmlLocationArr) {
            if (StringHelper.isBlank(xmlLocation)) {
                continue;
            }

            // 将该 XML 位置去除两边空白. 如果是 XML 文件则直接查找该 XML 文件，否则替换掉 '.' 号为 '/' 号，就代表资源目录.
            // 然后解析该目录下所有的 XML 文件，将这些 Fenix XML 文件解析出来，然后构建出 XmlResource 的集合，
            String location = xmlLocation.trim();
            if (StringHelper.isXmlFile(location)) {
                this.buildXmlResourcesByLocation(xmlResourceMap, location);
            } else {
                location = location.replace(Const.DOT, Const.SLASH);
                location = location.endsWith(Const.SLASH) ? location : location + Const.SLASH;
                this.buildXmlResourcesByLocation(xmlResourceMap, location + DIR_XML_PATTERN);
            }
        }
        return xmlResourceMap;
    }

    /**
     * 根据指定的一个包扫描其下所有的 XML 文件.
     *
     * @param xmlResourceMap XML 资源文件 Map
     * @param location XML 位置，可以是一个包，也可以是一个具体的文件路径
     */
    private void buildXmlResourcesByLocation(Map<String, XmlResource> xmlResourceMap, String location) {
        // 根据位置获取对应的 XML 资源实例.
        Resource[] resources = this.getResourcesByLocation(location);

        try {
            for (Resource resource: resources) {
                String path = resource.getURL().getPath();
                if (xmlResourceMap.containsKey(path)) {
                    log.debug("【Fenix 提示】已经扫描过了【" + path + "】文件，将跳过该 XML 文件的初始化加载.");
                    continue;
                }

                // 获取该资源文件中的 Fenix XML 文件的 Document 对象，并存入到 Map 中.
                try (InputStream in = resource.getInputStream()) {
                    XmlResource xmlResource = this.getFenixXmlResource(in, path);
                    if (xmlResource != null) {
                        xmlResourceMap.put(path, xmlResource);
                    }
                }
            }
        } catch (IOException e) {
            throw new FenixException("【Fenix 异常】初始化读取【" + location + "】下的 Fenix XML 文件出错，请检查！", e);
        }
    }


    /**
     * 根据资源文件位置的匹配规则查找到其下对应的 Fenix XML 文件资源的数组.
     *
     * @param location 资源文件位置的匹配规则字符串
     * @return XML 文件资源数组
     */
    private Resource[] getResourcesByLocation(String location) {
        try {
            return ResourcePatternUtils.getResourcePatternResolver(
                    new PathMatchingResourcePatternResolver()).getResources(location);
        } catch (IOException expected) {
            log.warn("【Fenix 警示】未查找到匹配规则【" + location + "】下的 Fenix XML 文件.", expected.getMessage());
            return new Resource[0];
        }
    }

    /**
     * 根据输入流获取 Fenix 的 XML 文件资源信息，并封装到 XmlResource 对象中.
     *
     * @param in 资源文件输入流
     * @param path 文件路径
     * @return Fenix XML 资源
     */
    private XmlResource getFenixXmlResource(InputStream in, String path) {
        Document doc;
        try {
            doc = new SAXReader().read(in);
        } catch (Exception expected) {
            // 由于只是判断该文件是否能被正确解析，所有这里就不抛出异常堆栈信息了.
            log.info("【Fenix 提示】解析路径为:【" + path + "】的 Fenix XML 文件异常，将忽略此文件!");
            return null;
        }

        // 获取 XML 文件的根节点，如果根节点是 '<fenixs></fenixs>'，说明是 Fenix XML 文件
        // 然后获取其属性 namespace 的值，如果命名空间为空，就抛出异常.
        Node root = doc.getRootElement();
        if (root != null && XpathConst.FENIX_ROOT_NAME.equals(root.getName())) {
            String namespace = XmlNodeHelper.getNodeText(root.selectSingleNode(XpathConst.ATTR_NAMESPACE));
            if (StringHelper.isBlank(namespace)) {
                throw new ConfigNotFoundException("【Fenix 警示】Fenix XML 文件 " + path + " 的根节点 namespace "
                        + "命名空间属性未配置，请配置!");
            }
            return new XmlResource().setNamespace(namespace).setPath(path).setDocument(doc);
        }

        return null;
    }

}
