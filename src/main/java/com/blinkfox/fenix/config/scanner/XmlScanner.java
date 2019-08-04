package com.blinkfox.fenix.config.scanner;

import com.blinkfox.zealot.config.entity.XmlContext;
import com.blinkfox.zealot.consts.ZealotConst;
import com.blinkfox.zealot.helpers.CollectionHelper;
import com.blinkfox.zealot.helpers.StringHelper;
import com.blinkfox.zealot.helpers.XmlNodeHelper;
import com.blinkfox.zealot.log.Log;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * zealot的XML文件包的扫描类，将扫描到的所有zealot xml文件添加到XmlContext中，供后续配置使用.
 *
 * @author blinkfox on 2018/4/24.
 */
public final class XmlScanner implements Scanner {

    private static final Log log = Log.get(XmlScanner.class);

    /** 存放所有扫描位置下的XML文件资源路径Set集合. */
    private Set<String> xmlPaths;

    /**
     * 私有构造方法.
     */
    private XmlScanner() {
        this.xmlPaths = new HashSet<String>();
    }

    /**
     * 获取XmlScanner最新实例的唯一方法.
     * @return XmlScanner实例
     */
    public static XmlScanner newInstance() {
        return new XmlScanner();
    }

    /**
     * 扫描配置的zealot xml包下所有的xml文件，并将这些xml文件的zealot节点读取存储到内存中.
     *
     * @param xmlLocations xml所在的位置
     */
    @Override
    public void scan(String xmlLocations) {
        if (StringHelper.isBlank(xmlLocations)) {
            return;
        }

        // 对配置的xml路径按逗号分割的规则来解析，如果是XML文件则直接将该xml文件存放到xmlPaths的Set集合中，
        // 否则就代表是xml资源目录，并解析目录下所有的xml文件，将这些xml文件存放到xmlPaths的Set集合中，
        String[] xmlLocationArr = xmlLocations.split(ZealotConst.COMMA);
        for (String xmlLocation: xmlLocationArr) {
            if (StringHelper.isBlank(xmlLocation)) {
                continue;
            }

            String cleanXmlLocation = xmlLocation.trim();
            if (StringHelper.isXmlFile(cleanXmlLocation)) {
                xmlPaths.add(cleanXmlLocation);
            } else {
                this.scanXmlsByPackage(cleanXmlLocation.replace('.', '/'));
            }
        }

        // 从xmlPath的Set集合中获取的如果是zealot的xml文件的话，则将其添加到XmlContext上下文中.
        this.addZealotXmlInContext();
    }

    /**
     * 根据指定的一个包扫描其下所有的xml文件.
     * @param xmlPackage xml包
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
     * 根据xml所在的包名，扫描得到其下所有的URL.
     * @param xmlPackage xml包
     * @return url集合
     */
    private List<URL> getDirUrls(String xmlPackage) {
        try {
            return Collections.list(Thread.currentThread().getContextClassLoader().getResources(xmlPackage));
        } catch (IOException e) {
            log.error("无法解析配置的zealot xml路径下文件的URL，将被忽略.该路径为:" + xmlPackage + "，请检查!", e);
            return Collections.emptyList();
        }
    }

    /**
     * 根据xml所在的包名，扫描得到其下所有的文件路径名的集合.
     * @param xmlPackage xml包
     * @param url url
     * @return 包中所有文件的集合
     */
    private List<String> getDirFilePaths(String xmlPackage, URL url) {
        try {
            return DefaultVfs.newInstance().list(url, xmlPackage);
        } catch (Exception e) {
            // 此处忽略异常堆栈信息.
            log.error("解析zealot xml包存在些问题,将被忽略！xml包为:" + xmlPackage + ",url:" + url);
            return Collections.emptyList();
        }
    }

    /**
     * 从xmlPath的Set集合中解析判断是否是zealot的xml文件，如果是的话则将其添加到XmlContext上下文中，供后面解析.
     */
    private void addZealotXmlInContext() {
        for (String xmlPath: xmlPaths) {
            String nameSpace = XmlNodeHelper.getZealotXmlNameSpace(xmlPath);
            if (StringHelper.isNotBlank(nameSpace)) {
                XmlContext.INSTANCE.add(nameSpace, xmlPath);
            }
        }
    }

}