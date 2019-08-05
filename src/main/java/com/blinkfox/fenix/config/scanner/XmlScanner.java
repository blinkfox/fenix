package com.blinkfox.fenix.config.scanner;

import com.blinkfox.fenix.config.entity.XmlContext;
import com.blinkfox.fenix.consts.Const;
import com.blinkfox.fenix.helper.CollectionHelper;
import com.blinkfox.fenix.helper.StringHelper;
import com.blinkfox.fenix.helper.XmlNodeHelper;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

/**
 * Fenix 的 XML 文件包的扫描类，将扫描到的所有 Fenix XML 文件添加到 XmlContext 中，供后续配置使用.
 *
 * @author blinkfox on 2019-08-04.
 */
@Slf4j
public final class XmlScanner implements Scanner {

    /**
     * 存放所有扫描位置下的 XML 文件资源路径的 Set 集合.
     */
    private Set<String> xmlPaths;

    /**
     * 私有构造方法.
     */
    public XmlScanner() {
        this.xmlPaths = new HashSet<>();
    }

    /**
     * 扫描配置的 Fenix XML 包下所有的 XML 文件，并将这些 XML 文件的 fenix 节点读取存储到内存中.
     *
     * @param xmlLocations XML 文件所在的位置
     */
    @Override
    public void scan(String xmlLocations) {
        if (StringHelper.isBlank(xmlLocations)) {
            return;
        }

        // 对配置的 XML 路径按逗号分割的规则来解析，如果是 XML 文件则直接将该 XML 文件存放到 xmlPaths 的 Set 集合中，
        // 否则就代表是xml资源目录，并解析目录下所有的xml文件，将这些xml文件存放到xmlPaths的Set集合中，
        String[] xmlLocationArr = xmlLocations.split(Const.COMMA);
        for (String xmlLocation: xmlLocationArr) {
            if (StringHelper.isBlank(xmlLocation)) {
                continue;
            }

            String location = xmlLocation.trim();
            if (StringHelper.isXmlFile(location)) {
                xmlPaths.add(location);
            } else {
                this.scanXmlsByPackage(location.replace('.', '/'));
            }
        }

        // 从 xmlPaths 的 Set 集合中获取的如果是 Fenix 的 XML 文件的话，则将其添加到 XmlContext 上下文中.
        this.addZealotXmlInContext();
    }

    /**
     * 根据指定的一个包扫描其下所有的 XML 文件.
     *
     * @param xmlPackage XML 包
     */
    private void scanXmlsByPackage(String xmlPackage) {
        List<URL> urls = this.getDirUrls(xmlPackage);
        if (CollectionHelper.isEmpty(urls)) {
            return;
        }

        for (URL url: urls) {
            List<String> filePaths = this.getDirFilePaths(xmlPackage, url);
            if (CollectionHelper.isEmpty(filePaths)) {
                continue;
            }

            // 循环得到所有的xml文件，并将这些XML文件，添加到xmlPaths集合中.
            for (String filePath: filePaths) {
                if (StringHelper.isXmlFile(filePath)) {
                    xmlPaths.add(filePath);
                }
            }
        }

    }

    /**
     * 根据 XML 所在的包名，扫描得到其下所有的 URL.
     *
     * @param xmlPackage XML 包
     * @return url集合
     */
    private List<URL> getDirUrls(String xmlPackage) {
        try {
            return Collections.list(Thread.currentThread().getContextClassLoader().getResources(xmlPackage));
        } catch (IOException e) {
            log.error("【Fenix 错误警示】无法解析配置的 Fenix XML 路径下文件的 URL，将被忽略.该路径为:【" + xmlPackage + "】，请检查!", e);
            return Collections.emptyList();
        }
    }

    /**
     * 根据 XML 所在的包名，扫描得到其下所有的文件路径名的集合.
     *
     * @param xmlPackage XML 包
     * @param url url
     * @return 包中所有文件的集合
     */
    private List<String> getDirFilePaths(String xmlPackage, URL url) {
        try {
            return new FenixVfs().list(url, xmlPackage);
        } catch (Exception e) {
            // 此处忽略异常堆栈信息.
            log.error("【Fenix 错误警示】解析 Fenix XML 包存在问题，将被忽略！XML包为:【" + xmlPackage + "】，url:【" + url + "】.");
            return Collections.emptyList();
        }
    }

    /**
     * 从 xmlPaths 的 Set 集合中解析判断是否是 Fenix 的 XML 文件，如果是的话则将其添加到 XmlContext 上下文中，供后面解析.
     */
    private void addZealotXmlInContext() {
        for (String xmlPath: xmlPaths) {
            String namespace = XmlNodeHelper.getZealotXmlNameSpace(xmlPath);
            if (StringHelper.isNotBlank(namespace)) {
                XmlContext.getInstance().add(namespace, xmlPath);
            }
        }
    }

}
