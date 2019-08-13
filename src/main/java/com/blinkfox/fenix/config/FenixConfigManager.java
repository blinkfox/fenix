package com.blinkfox.fenix.config;

import com.blinkfox.fenix.config.entity.NormalConfig;
import com.blinkfox.fenix.config.entity.XmlContext;
import com.blinkfox.fenix.config.scanner.TaggerScanner;
import com.blinkfox.fenix.config.scanner.XmlScanner;
import com.blinkfox.fenix.consts.Const;
import com.blinkfox.fenix.consts.XpathConst;
import com.blinkfox.fenix.exception.ConfigNotFoundException;
import com.blinkfox.fenix.exception.FenixException;
import com.blinkfox.fenix.exception.NodeNotFoundException;
import com.blinkfox.fenix.helper.ParamWrapper;
import com.blinkfox.fenix.helper.ParseHelper;
import com.blinkfox.fenix.helper.StringHelper;
import com.blinkfox.fenix.helper.XmlNodeHelper;

import java.util.List;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.dom4j.Document;
import org.dom4j.Node;

/**
 * Fenix 的配置信息管理器单例类，用于加载 Fenix 所需的各种配置信息到内存中.
 *
 * @author blinkfox on 2019-08-04.
 * @see FenixConfig
 * @see NormalConfig
 * @see XmlContext
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FenixConfigManager {

    /**
     * Fenix 的 banner 文本.
     */
    private static final String BANNER_TEXT = "\n"
            + "___________           .__        \n"
            + "\\_   _____/___   ____ |__|__  ___\n"
            + " |    __)/ __ \\ /    \\|  \\  \\/  /\n"
            + " |     \\\\  ___/|   |  \\  |>    < \n"
            + " \\___  / \\___  >___|  /__/__/\\_ \\\n"
            + "     \\/      \\/     \\/         \\/\n";

    /**
     * fenix 目录名的常量.
     */
    private static final String FENIX_DIR_NAME = "fenix";
    
    /**
     * 初始化的 {@link FenixConfigManager} 单实例.
     */
    private static final FenixConfigManager confManager = new FenixConfigManager();

    /**
     * Fenix 的 XML 文件所在的位置，多个用逗号隔开，可以是目录也可以是具体的 XML 文件.
     */
    @Getter
    private String xmlLocations;

    /**
     * Fenix 自定义的 {@link com.blinkfox.fenix.config.entity.TagHandler} 处理器实现的所在位置，
     * 多个用逗号隔开，可以是目录也可以是具体的 java 或 class 文件路径.
     */
    @Getter
    private String handlerLocations;

    /**
     * 获取 {@link FenixConfigManager} 的唯一实例.
     *
     * @return FenixConfigManager 唯一实例
     */
    public static FenixConfigManager getInstance() {
        return confManager;
    }

    /**
     * 初始化加载默认的 Fenix 的配置信息到内存中.
     */
    public void initLoad() {
        this.initLoad(new FenixConfig());
    }

    /**
     * 初始化加载 Fenix 的配置信息到内存中.
     *
     * @param configClass 系统中 {@link FenixConfig} 的子类的 class 路径
     * @param xmlLocations Fenix 的 XML 文件所在的位置，多个用逗号隔开
     * @param handlerLocations Fenix 的自定义 {@link com.blinkfox.fenix.config.entity.TagHandler}
     *          处理器所在的位置，多个用逗号隔开
     */
    public void initLoad(String configClass, String xmlLocations, String handlerLocations) {
        this.xmlLocations = xmlLocations;
        this.handlerLocations = handlerLocations;
        this.initLoad(configClass);
    }

    /**
     * 初始化加载 Fenix 的配置信息到内存中.
     *
     * @param clazz {@link FenixConfig} 子类的配置类
     * @param xmlLocations Fenix 的 XML 文件所在的位置，多个用逗号隔开
     * @param handlerLocations Fenix 的自定义 {@link com.blinkfox.fenix.config.entity.TagHandler}
     *          处理器所在的位置，多个用逗号隔开
     */
    public void initLoad(Class<? extends FenixConfig> clazz, String xmlLocations, String handlerLocations) {
        this.initLoad(clazz.getName(), xmlLocations, handlerLocations);
    }

    /**
     * 初始化加载 Fenix 的配置信息到内存中.
     *
     * @param fenixConfig {@link FenixConfig} 的子类的配置类实例
     * @param xmlLocations Fenix 的 XML 文件所在的位置，多个用逗号隔开
     * @param handlerLocations Fenix 的自定义 {@link com.blinkfox.fenix.config.entity.TagHandler}
     *          处理器所在的位置，多个用逗号隔开
     */
    public void initLoad(FenixConfig fenixConfig, String xmlLocations, String handlerLocations) {
        this.xmlLocations = xmlLocations;
        this.handlerLocations = handlerLocations;
        this.initLoad(fenixConfig);
    }

    /**
     * 初始化加载 Fenix 的配置信息到内存中.
     *
     * @param clazz {@link FenixConfig} 的子类配置类
     */
    public void initLoad(Class<? extends FenixConfig> clazz) {
        this.initLoad(clazz.getName());
    }

    /**
     * 初始化加载 Fenix 的配置信息到内存中.
     *
     * @param configClass 系统中 Fenix 的 class 路径
     */
    public void initLoad(String configClass) {
        this.scanLocations(this.xmlLocations, this.handlerLocations);
        this.loadFenixConfig(configClass);
        cachingXmlAndEval();
    }

    /**
     * 初始化加载 Fenix 的配置信息到内存中.
     *
     * @param fenixConfig {@link FenixConfig} 的子类配置类实例
     */
    public void initLoad(FenixConfig fenixConfig) {
        this.scanLocations(this.xmlLocations, this.handlerLocations);
        this.loadFenixConfig(fenixConfig);
        this.cachingXmlAndEval();
    }

    /**
     * 扫描 Fenix XML 和 {@link com.blinkfox.fenix.config.entity.TagHandler} 所在的文件位置.
     *
     * @param xmlLocations Fenix 的 XML 文件所在的位置
     * @param handlerLocations Fenix 的自定义 handler 处理器所在的位置
     */
    private void scanLocations(String xmlLocations, String handlerLocations) {
        this.xmlLocations = StringHelper.isBlank(xmlLocations) ? FENIX_DIR_NAME : this.xmlLocations;
        new XmlScanner().scan(this.xmlLocations);
        new TaggerScanner().scan(handlerLocations);
    }

    /**
     * 扫描 XML 文件所在的位置 并识别配置加载到内存中.
     *
     * @param xmlLocations Fenix 的 XML 文件所在的位置
     * @return {@link FenixConfigManager} 的全局唯一实例
     */
    public FenixConfigManager initLoadXmlLocations(String xmlLocations) {
        this.xmlLocations = StringHelper.isBlank(xmlLocations) ? FENIX_DIR_NAME : xmlLocations;
        new XmlScanner().scan(this.xmlLocations);
        this.cachingXmlAndEval();
        return this;
    }

    /**
     * 扫描 {@link com.blinkfox.fenix.config.entity.TagHandler} 文件所在的位置，并识别配置加载到内存中.
     *
     * @param handlerLocations Fenix 的自定义 handler 处理器所在的位置
     * @return {@link FenixConfigManager} 的全局唯一实例
     */
    public FenixConfigManager initLoadHandlerLocations(String handlerLocations) {
        this.handlerLocations = handlerLocations;
        new TaggerScanner().scan(this.handlerLocations);
        return this;
    }

    /**
     * 清空 Fenix 所有内存缓存中的内容，包括 XML 命名空间路径缓存、XML节点缓存.
     */
    public void clear() {
        XmlContext.getInstance().getXmlPathMap().clear();
        FenixConfig.getFenixs().clear();
    }

    /**
     * 初始化创建 {@link FenixConfig} 的子类实例，并加载配置信息.
     *
     * @param configClass 配置类的 class 文件全路径
     */
    private void loadFenixConfig(String configClass) {
        if (StringHelper.isBlank(configClass)) {
            throw new ConfigNotFoundException("未获取到 FenixConfig 的配置类信息！");
        }

        // 创建 configClass 的实例，并判断该实例是否是 FenixDefaultConfig 的子类，
        // 如果是，则加载配置信息，否则就抛出异常.
        log.info("【Fenix 提示】开始加载 Fenix 配置信息，配置类为:【" + configClass + "】.");
        try {
            Object config = Class.forName(configClass).newInstance();
            if (config instanceof FenixConfig) {
                this.loadFenixConfig((FenixConfig) config);
            }
        } catch (Exception e) {
            throw new ConfigNotFoundException("【Fenix 错误提示】初始化 fenixConfig 实例失败，配置名称为:【" + configClass + "】.", e);
        }
    }

    /**
     * 初始化加载 {@link FenixConfig} 的子类信息，并执行初始化 Fenix 配置信息到内存缓存中.
     *
     * @param fenixConfig 配置类
     */
    private void loadFenixConfig(FenixConfig fenixConfig) {
        fenixConfig.configNormal(NormalConfig.getInstance());
        fenixConfig.configXml(XmlContext.getInstance());
        fenixConfig.configTagHandler();
        log.warn("【Fenix 提示】加载 Fenix 的配置信息完成.");
    }

    /**
     * 缓存 XML、打印 banner 并做初次计算.
     */
    private void cachingXmlAndEval() {
        this.cachingXmlZealots();
        this.printBanner(NormalConfig.getInstance().isPrintBanner());
        this.testFirstEvaluate();
    }

    /**
     * 是否打印 Finix Banner.
     *
     * @param isPrint 是否打印
     */
    private void printBanner(boolean isPrint) {
        if (isPrint) {
            log.warn(BANNER_TEXT);
        }
    }

    /**
     * 将每个 Fenix XML 配置文件的 key 和文档缓存到 ConcurrentHashMap 内存缓存中.
     */
    private void cachingXmlZealots() {
        Map<String, String> xmlMaps = XmlContext.getInstance().getXmlPathMap();

        // 遍历所有的 XML 文档，将每个 fenix 节点缓存到 ConcurrentHashMap 内存缓存中.
        for (Map.Entry<String, String> entry: xmlMaps.entrySet()) {
            String namespace = entry.getKey();
            String filePath = entry.getValue();

            // 根据文件路径获取对应的 dom4j Document 对象.
            Document doc = XmlNodeHelper.getDocument(filePath);
            if (doc == null) {
                throw new ConfigNotFoundException("【Fenix 异常提示】未找到配置文件中 XML 对应的 dom4j Document 文档，"
                        + "namespace 为:【" + namespace + "】.");
            }

            // 获取该文档下所有的 fenix XML 元素.
            List<Node> fenixNodes = doc.selectNodes(XpathConst.FENIX_TAG);
            for (Node fenixNode: fenixNodes) {
                String fenixId = XmlNodeHelper.getNodeText(fenixNode.selectSingleNode(XpathConst.ATTR_ID));
                if (StringHelper.isBlank(fenixId)) {
                    throw new NodeNotFoundException("【Fenix 异常提示】该【" + filePath + "】的 XML 文件中有"
                            + " fenix 节点的 id 属性为空，请检查！");
                }

                // 判断 fenixId 是否有 '.' 号，如果有的话，就抛出异常提示.
                if (fenixId.contains(Const.DOT)) {
                    throw new FenixException("【Fenix 异常提示】该【" + filePath + "】的 XML 文件中"
                            + " fenix 节点 id【" + fenixId + "】不能包含 '.' 号，请修正！");
                }

                // 将 fenix 节点缓存到 Map 中，key 是由 namespace 和 fenixId 组成，用 "." 号分隔，value 是 fenixNode.
                FenixConfig.getFenixs().put(StringHelper.concat(namespace, Const.DOT, fenixId), fenixNode);
            }
        }
    }

    /**
     * 测试第一次 MVEL 表达式的计算，会缓存 MVEL 相关准备工作，从而加快后续的 MVEL 执行.
     */
    private void testFirstEvaluate() {
        Map<String, Object> context = ParamWrapper.newInstance("foo", "hello").toMap();
        ParseHelper.parseTemplate("@if{?foo != empty}Hello World!@end{}", context);
        ParseHelper.parseExpressWithException("foo != empty", context);
    }

}
