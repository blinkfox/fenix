package com.blinkfox.fenix.config;

import com.blinkfox.fenix.config.entity.NormalConfig;
import com.blinkfox.fenix.config.entity.XmlContext;
import com.blinkfox.fenix.config.scanner.TaggerScanner;
import com.blinkfox.fenix.config.scanner.XmlScanner;
import com.blinkfox.fenix.consts.Const;
import com.blinkfox.fenix.consts.XpathConst;
import com.blinkfox.fenix.exception.ConfigNotFoundException;
import com.blinkfox.fenix.exception.NodeNotFoundException;
import com.blinkfox.fenix.helper.ParseHelper;
import com.blinkfox.fenix.helper.StringHelper;
import com.blinkfox.fenix.helper.XmlNodeHelper;
import com.blinkfox.fenix.loader.BannerLoader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.dom4j.Document;
import org.dom4j.Node;

/**
 * Fenix 配置信息缓存管理器，用于加载 Fenix Config 配置信息到缓存中.
 *
 * @author blinkfox on 2019-08-04.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FenixConfigManager {
    
    /**
     * 初始化的单实例.
      */
    private static final FenixConfigManager confManager = new FenixConfigManager();

    /**
     * Fenix 的 XML 文件所在的位置，多个用逗号隔开,可以是目录也可以是具体的 XML 文件.
     */
    private String xmlLocations;

    /**
     * Fenix 的自定义 handler 处理器所在的位置，多个用逗号隔开,可以是目录也可以是具体的 java 或 class 文件路径.
     */
    private String handlerLocations;

    /**
     * 获取 ZealotConfigManager的唯一实例.
     * @return ZealotConfigManager唯一实例
     */
    public static FenixConfigManager getInstance() {
        return confManager;
    }

    /**
     * 获取配置的zealot的XML文件所在的位置字符串.
     * @return xmlLocations
     */
    public String getXmlLocations() {
        return xmlLocations;
    }

    /**
     * 获取配置的zealot的自定义handler处理器所在的位置字符串.
     * @return handlerLocations
     */
    public String getHandlerLocations() {
        return handlerLocations;
    }

    /**
     * 初始化加载Zealot的配置信息到缓存中.
     *
     * @param configClass 系统中Zealot的class路径
     * @param xmlLocations zealot的XML文件所在的位置，多个用逗号隔开
     * @param handlerLocations zealot的自定义handler处理器所在的位置，多个用逗号隔开
     */
    public void initLoad(String configClass, String xmlLocations, String handlerLocations) {
        // 设置配置的文件路径的值，并开始加载ZealotConfig配置信息
        this.xmlLocations = xmlLocations;
        this.handlerLocations = handlerLocations;
        this.initLoad(configClass);
    }

    /**
     * 初始化加载Zealot的配置信息到缓存中.
     * @param clazz 配置类
     * @param xmlLocations zealot的XML文件所在的位置，多个用逗号隔开
     * @param handlerLocations zealot的自定义handler处理器所在的位置，多个用逗号隔开
     */
    public void initLoad(Class<? extends AbstractFenixConfig> clazz, String xmlLocations,
            String handlerLocations) {
        this.initLoad(clazz.getName(), xmlLocations, handlerLocations);
    }

    /**
     * 初始化加载Zealot的配置信息到缓存中.
     *
     * @param zealotConfig 配置类实例
     * @param xmlLocations zealot的XML文件所在的位置，多个用逗号隔开
     * @param handlerLocations zealot的自定义handler处理器所在的位置，多个用逗号隔开
     */
    public void initLoad(AbstractFenixConfig zealotConfig, String xmlLocations, String handlerLocations) {
        this.xmlLocations = xmlLocations;
        this.handlerLocations = handlerLocations;
        this.initLoad(zealotConfig);
    }

    /**
     * 初始化加载Zealot的配置信息到缓存中.
     * @param clazz 配置类
     */
    public void initLoad(Class<? extends AbstractFenixConfig> clazz) {
        this.initLoad(clazz.getName());
    }

    /**
     * 初始化加载Zealot的配置信息到缓存中.
     * @param configClass 系统中Zealot的class路径
     */
    public void initLoad(String configClass) {
        // 加载ZealotConfig配置信息
        this.scanLocations(this.xmlLocations, this.handlerLocations);
        this.loadZealotConfig(configClass);
        cachingXmlAndEval();
    }

    /**
     * 初始化加载Zealot的配置信息到缓存中.
     *
     * @param zealotConfig 配置类实例
     */
    public void initLoad(AbstractFenixConfig zealotConfig) {
        this.scanLocations(this.xmlLocations, this.handlerLocations);
        this.loadZealotConfig(zealotConfig);
        this.cachingXmlAndEval();
    }

    /**
     * 扫描 xml和handler所在的文件位置.
     * @param xmlLocations zealot的XML文件所在的位置
     * @param handlerLocations zealot的自定义handler处理器所在的位置
     */
    private void scanLocations(String xmlLocations, String handlerLocations) {
        this.xmlLocations = StringHelper.isBlank(this.xmlLocations) ? "zealot" : this.xmlLocations;
        new XmlScanner().scan(xmlLocations);
        new TaggerScanner().scan(handlerLocations);
    }

    /**
     * 扫描 xml文件所在的文件位置 并识别配置加载到内存缓存中.
     * @param xmlLocations zealot的XML文件所在的位置
     * @return ZealotConfigManager的全局唯一实例
     */
    public FenixConfigManager initLoadXmlLocations(String xmlLocations) {
        this.xmlLocations = StringHelper.isBlank(xmlLocations) ? "zealot" : xmlLocations;
        new XmlScanner().scan(this.xmlLocations);
        this.cachingXmlAndEval();
        return this;
    }

    /**
     * 扫描 handler文件所在的文件位置 并识别配置加载到内存缓存中.
     * @param handlerLocations zealot的自定义handler处理器所在的位置
     * @return ZealotConfigManager的全局唯一实例
     */
    public FenixConfigManager initLoadHandlerLocations(String handlerLocations) {
        this.handlerLocations = handlerLocations;
        new TaggerScanner().scan(handlerLocations);
        return this;
    }

    /**
     * 清空zealot所有缓存的内容.
     * 包括xml命名空间路径缓存、xml节点缓存
     */
    public void clear() {
        XmlContext.INSTANCE.getXmlPathMap().clear();
        AbstractFenixConfig.getZealots().clear();
    }

    /**
     * 初始化zealotConfig的子类，并加载配置信息.
     * @param configClass 配置类的class路径
     */
    private void loadZealotConfig(String configClass) {
        if (configClass == null) {
            throw new ConfigNotFoundException("未获取到 FenixConfig 配置类信息");
        }

        log.info("Fenix 加载器开始加载，Fenix 配置类为:【" + configClass + "】.");
        Object temp;
        try {
            temp = Class.forName(configClass).newInstance();
        } catch (Exception e) {
            throw new ConfigNotFoundException("初始化 fenixConfig 实例失败，配置名称为:【" + configClass + "】.", e);
        }

        // 判断获取到的类是否是 AbstractFenixConfig 的子类，如果是，则加载 XML 和自定义标签.
        if (temp instanceof AbstractFenixConfig) {
            this.loadZealotConfig((AbstractFenixConfig) temp);
        }
    }

    /**
     * 加载初始化zealotConfig的子类信息，并执行初始化zealot配置信息到缓存中.
     * @param zealotConfig 配置类
     */
    private void loadZealotConfig(AbstractFenixConfig zealotConfig) {
        zealotConfig.configNormal(NormalConfig.getInstance());
        zealotConfig.configXml(XmlContext.INSTANCE);
        zealotConfig.configTagHandler();
        log.warn("Zealot的配置信息加载完成!");
    }

    /**
     * 缓存xml、打印bannner并做初次计算.
     */
    private void cachingXmlAndEval() {
        this.cachingXmlZealots();
        new BannerLoader().print(NormalConfig.getInstance().isPrintBanner());
        this.testFirstEvaluate();
    }

    /**
     * 将每个 Fenix XML 配置文件的 key 和文档缓存到 ConcurrentHashMap 内存缓存中.
     */
    private void cachingXmlZealots() {
        Map<String, String> xmlMaps = XmlContext.INSTANCE.getXmlPathMap();

        // 遍历所有的xml文档，将每个zealot节点缓存到ConcurrentHashMap内存缓存中
        for (Map.Entry<String, String> entry: xmlMaps.entrySet()) {
            String nameSpace = entry.getKey();
            String filePath = entry.getValue();

            // 根据文件路径获取对应的dom4j Document对象.
            Document doc = XmlNodeHelper.getDocument(filePath);
            if (doc == null) {
                throw new ConfigNotFoundException("注意：未找到配置文件中xml对应的dom4j Document文档,nameSpace为:" + nameSpace);
            }

            // 获取该文档下所有的 fenix 元素.
            List<Node> zealotNodes = doc.selectNodes(XpathConst.FENIX_TAG);
            for (Node zealotNode: zealotNodes) {
                String fenixId = XmlNodeHelper.getNodeText(zealotNode.selectSingleNode(XpathConst.ATTR_ID));
                if (StringHelper.isBlank(fenixId)) {
                    throw new NodeNotFoundException("该xml文件中有zealot节点的zealotId属性为空，请检查！文件为:" + filePath);
                }

                // fenix 节点缓存到 Map 中，key 是由 nameSpace 和 fenix id 组成，用 "." 号分隔，value 是 fenixNode
                AbstractFenixConfig.getZealots().put(StringHelper.concat(nameSpace, Const.DOT, fenixId), zealotNode);
            }
        }
    }

    /**
     * 测试第一次 MVEL 表达式的计算,会缓存 MVEL 相关准备工作，加快后续的 MVEL 执行.
     */
    private void testFirstEvaluate() {
        Map<String, Object> context = new HashMap<>(4);
        context.put("foo", "hello");
        ParseHelper.parseTemplate("@if{?foo != empty}Hello World!@end{}", context);
        ParseHelper.parseExpressWithException("foo != empty", context);
    }

}