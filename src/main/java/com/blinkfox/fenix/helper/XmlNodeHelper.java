package com.blinkfox.fenix.helper;

import com.blinkfox.fenix.config.entity.XmlContext;
import com.blinkfox.fenix.consts.XpathConst;
import com.blinkfox.fenix.exception.ConfigNotFoundException;
import com.blinkfox.fenix.exception.FieldEmptyException;
import com.blinkfox.fenix.exception.XmlParseException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

/**
 * XML 文件和 XML 标签节点相关操作的工具类.
 *
 * @author blinkfox on 2019-08-04.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class XmlNodeHelper {

    /**
     * Fenix XML 文件中的根节点名称.
     */
    private static final String ROOT_NAME = "fenixs";

    /**
     * 读取 XML 文件为 dom4j 的 Docment 文档对象.
     *
     * @param xmlPath 定位xml文件的路径，如：'com/blinkfox/test.xml'、`/fenixs/blog.xml`.
     * @return 返回dom4j文档
     */
    public static Document getDocument(String xmlPath) {
        try {
            return new SAXReader().read(Thread.currentThread().getContextClassLoader().getResourceAsStream(xmlPath));
        } catch (Exception e) {
            throw new XmlParseException("【Fenix 异常提示】读取或解析 XML 文件失败，读取到的 XML 路径是:【" + xmlPath + "】.", e);
        }
    }

    /**
     * 根据 XML 文件的 namespace 及 Fenix 节点 id 值获取对应的第一个 dom4j 节点.
     *
     * @param namespace XML 文件对应命名空间
     * @param fenixId fenixId
     * @return dom4j的Node节点
     */
    public static Node getNodeBySpaceAndId(String namespace, String fenixId) {
        Document doc = XmlNodeHelper.getDocument(XmlContext.getInstance().getXmlPathMap().get(namespace));
        return doc == null ? null : XmlNodeHelper.getFenixNodeById(doc, fenixId);
    }

    /**
     * 根据 XML 文件的路径判断该 XML 文件是否是 Fenix XML 文件，如果是则返回 namespace.
     * <p>注：这里简单判断是否有 '<fenixs></fenixs>' 根节点即可.</p>
     *
     * @param xmlPath XML 路径
     * @return 该 XML 文件的 Fenix 命名空间 namespace
     */
    public static String getFenixXmlNameSpace(String xmlPath) {
        Document doc;
        try {
            doc = new SAXReader().read(Thread.currentThread().getContextClassLoader().getResourceAsStream(xmlPath));
        } catch (Exception expected) {
            // 由于只是判断该文件是否能被正确解析，并不进行正式的解析，所有这里就不抛出和记录异常了.
            log.warn("【Fenix 警示】解析路径为:【" + xmlPath + "】的 XML 文件出错，请检查其正确性!");
            return null;
        }

        // 获取 XML 文件的根节点，如果根节点是 '<fenixs></fenixs>'，说明是 Fenix XML 文件
        // 然后获取其属性 namespace 的值，为空就抛出异常.
        Node root = doc.getRootElement();
        if (root != null && ROOT_NAME.equals(root.getName())) {
            String namespace = getNodeText(root.selectSingleNode(XpathConst.ATTR_NAMESPACE));
            if (StringHelper.isBlank(namespace)) {
                throw new ConfigNotFoundException("【Fenix 警示】Fenix XML 文件:【" + xmlPath + "】的根节点 namespace "
                        + "命名空间属性未配置，请配置，否则将被忽略!");
            }
            return namespace;
        }

        return null;
    }

    /**
     * 根据 XML 文件 Docment 中的 Fenix 节点 id 值获取对应的第一个节点.
     *
     * <p>使用 xPath 语法获取第一个符合条件的节点.</p>
     * @param doc docment文档
     * @param id fenix 节点的 id
     * @return dom4j 的 Node 节点
     */
    public static Node getFenixNodeById(Document doc, String id) {
        return doc.selectSingleNode("/fenixs/fenix[@id='" + id + "']");
    }

    /**
     * 获取 XML 节点的文本值，如果对象是空的，则转为空字符串.
     *
     * @param node dom4j 节点
     * @return 返回节点文本值
     */
    public static String getNodeText(Node node) {
        return node == null ? "" : node.getText();
    }

    /**
     * 获取节点文本的字符串值.
     *
     * @param node dom4j 节点
     * @param attrName 节点属性
     * @return 返回节点文本值
     */
    public static String getNodeAttrText(Node node, String attrName) {
        return XmlNodeHelper.getNodeText(node.selectSingleNode(attrName));
    }

    /**
     * 获取和检查节点文本，会检查节点是否为空，如果节点为空，则抛出异常.
     *
     * <p>注：该方法需要判断必填的参数是否为空，为空的话，需要抛出 {@link FieldEmptyException} 异常.</p>
     *
     * @param node dom4j 节点
     * @param nodeName 节点名称
     * @return 返回节点文本值
     */
    public static String getAndCheckNodeText(Node node, String nodeName) {
        String text = XmlNodeHelper.getNodeText(node.selectSingleNode(nodeName));
        if (StringHelper.isBlank(text)) {
            throw new FieldEmptyException("【Fenix 异常】【" + node.getName() + "】节点中填写的属性不存在或者属性内容是空的！");
        }
        return text;
    }

    /**
     * 检查和获取开始和结束文本的内容，返回一个数组.
     *
     * <p>会检查这两个节点是否为空，如果都为空，则抛出 {@link FieldEmptyException} 异常.</p>
     *
     * @param node dom4j 节点
     * @return 返回开始和结束文本的二元数组
     */
    public static String[] getRangeCheckNodeText(Node node) {
        String startText = XmlNodeHelper.getNodeText(node.selectSingleNode(XpathConst.ATTR_START));
        String endText = XmlNodeHelper.getNodeText(node.selectSingleNode(XpathConst.ATTR_ENT));
        if (StringHelper.isBlank(startText) && StringHelper.isBlank(endText)) {
            throw new FieldEmptyException("【Fenix 异常】【" + node.getName() + "】标签中填写的【start】和【end】字段值都是空的！");
        }
        return new String[] {startText, endText};
    }

}