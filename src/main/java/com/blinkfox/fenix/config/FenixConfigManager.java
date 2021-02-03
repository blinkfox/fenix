package com.blinkfox.fenix.config;

import com.blinkfox.fenix.config.scanner.TaggerScanner;
import com.blinkfox.fenix.config.scanner.XmlResource;
import com.blinkfox.fenix.config.scanner.XmlScanner;
import com.blinkfox.fenix.consts.Const;
import com.blinkfox.fenix.consts.XpathConst;
import com.blinkfox.fenix.exception.FenixException;
import com.blinkfox.fenix.exception.NodeNotFoundException;
import com.blinkfox.fenix.helper.ParamWrapper;
import com.blinkfox.fenix.helper.ParseHelper;
import com.blinkfox.fenix.helper.StringHelper;
import com.blinkfox.fenix.helper.XmlNodeHelper;
import java.net.URL;
import java.util.Collection;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Node;

/**
 * Fenix 的配置信息管理器单例类，用于加载 Fenix 所需的各种配置信息到内存中.
 *
 * @author blinkfox on 2019-08-04.
 * @see FenixConfig
 * @see XmlResource
 * @since v1.0.0
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
            + "     \\/      \\/     \\/         \\/ v2.4.2\n";

    /**
     * Fenix 配置信息实例.
     */
    @Getter
    private FenixConfig fenixConfig;

    /**
     * 初始化的 {@link FenixConfigManager} 单实例.
     */
    private static final FenixConfigManager confManager = new FenixConfigManager();

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
     * @param fenixConfig {@link FenixConfig} 的子类配置类实例
     */
    public void initLoad(FenixConfig fenixConfig) {
        // 初始化设置 FenixConfig 实例和其中的一些属性.
        this.initFenixConfig(fenixConfig);

        // 初始化加载 Fenix XML 文件和自定义的标签处理器类.
        this.cachingFenixXmlResources(new XmlScanner().scan(this.fenixConfig.getXmlLocations()));
        new TaggerScanner().scan(this.fenixConfig.getHandlerLocations());

        // 初次测试表达式引擎是否能够正确工作和打印 banner 信息.
        this.testFirstEvaluate();
        this.printBanner();
    }

    /**
     * 初始化设置 {@link FenixConfig} 实例.
     *
     * @param fenixConfig {@link FenixConfig} 实例
     */
    private void initFenixConfig(FenixConfig fenixConfig) {
        if (fenixConfig == null) {
            throw new FenixException("【Fenix 异常】初始化加载的 FenixConfig 配置信息实例为空，请检查！");
        }

        // 扫描和缓存 Fenix XML 文件资源信息、扫描和配置自定义的 Fenix 标签处理器实例类.
        String xmlLocations = fenixConfig.getXmlLocations();
        fenixConfig.setXmlLocations(StringHelper.isBlank(xmlLocations) ? Const.DEFAULT_FENIX_XML_DIR : xmlLocations);
        this.fenixConfig = fenixConfig;
    }

    /**
     * 打印 Finix Banner.
     */
    private void printBanner() {
        if (this.fenixConfig.isPrintBanner()) {
            log.warn(BANNER_TEXT);
        }
        if (this.fenixConfig.isDebug()) {
            log.warn("【Fenix 提示】已开启 Fenix 的【debug】模式，仅建议你在开发环境中开启此模式.");
        }
        log.warn("【Fenix 提示】初始化加载 Fenix 配置信息完成.");
    }

    /**
     * 将每个 Fenix XML 配置文件的 key 和文档缓存到 ConcurrentHashMap 内存缓存中.
     *
     * @param xmlResourceMap XML 资源的 Map 集合
     */
    private void cachingFenixXmlResources(Map<String, XmlResource> xmlResourceMap) {
        if (log.isDebugEnabled()) {
            log.debug("【Fenix 提示】扫描到了这些 Fenix XML 文件：【{}】.", xmlResourceMap.keySet());
        }

        // 是否开启了 debug 模式.
        final boolean debug = this.fenixConfig.isDebug();
        Map<String, URL> xmlUrlMap = FenixConfig.getXmlUrlMap();

        // 遍历各个 XML 资源文件信息，将 fenixId 和 对应的 Node 节点缓存起来.
        Collection<XmlResource> xmlResources = xmlResourceMap.values();
        for (XmlResource xmlResource : xmlResources) {
            String namespace = xmlResource.getNamespace();
            for (Node fenixNode : xmlResource.getDocument().selectNodes(XpathConst.FENIX_TAG)) {
                String fenixId = XmlNodeHelper.getNodeText(fenixNode.selectSingleNode(XpathConst.ATTR_ID));
                if (StringHelper.isBlank(fenixId)) {
                    throw new NodeNotFoundException("【Fenix 异常提示】命名空间为【" + namespace + "】的 Fenix XML 文件中有"
                            + " fenix 节点的 id 属性为空，请检查！文件位置在【" + xmlResource.getUrl().getPath() + "】.");
                }

                // 判断 fenixId 是否有 '.' 号，如果有的话，就抛出异常提示.
                if (fenixId.contains(Const.DOT)) {
                    throw new FenixException("【Fenix 异常提示】命名空间为【" + namespace + "】的 XML 文件中，fenix 节点 id"
                            + "【" + fenixId + "】不能包含 '.' 号，请修正！文件位置在【" + xmlResource.getUrl().getPath() + "】.");
                }

                // 将 fenix 节点缓存到 Map 中，key 是由 namespace 和 fenixId 组成，用 "." 号分隔，value 是 fenixNode.
                FenixConfig.getFenixs().put(StringHelper.concat(namespace, Const.DOT, fenixId), fenixNode);

                // v2.4.1 版本新增，如果开启了 debug 模式，就建立 namespace 和 xml 路径的映射关系，便于实时读取 XML 文件中的内容.
                if (debug) {
                    xmlUrlMap.put(namespace, xmlResource.getUrl());
                }
            }
        }
    }

    /**
     * 清空 Fenix 所有内存缓存中的内容，包括 XML 命名空间路径缓存、XML节点缓存.
     */
    public void clear() {
        FenixConfig.getFenixs().clear();
        FenixConfig.getTagHandlerMap().clear();
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
