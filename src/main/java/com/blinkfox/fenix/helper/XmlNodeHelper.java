package com.blinkfox.fenix.helper;

import com.blinkfox.fenix.config.entity.XmlContext;
import com.blinkfox.fenix.consts.XpathConst;
import com.blinkfox.fenix.exception.FieldEmptyException;
import com.blinkfox.fenix.exception.XmlParseException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

/**
 * XML 和 XML 节点相关操作的工具类.
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
            throw new XmlParseException("读取或解析 XML 文件失败，读取到的 XML 路径是:【" + xmlPath + "】.", e);
        }
    }

    /**
     * 根据xml文件的nameSpace及zealot节点id值获取对应的第一个dom4j节点.
     *
     * @param namespace XML 文件对应命名空间
     * @param zealotId ZealotId
     * @return dom4j的Node节点
     */
    public static Node getNodeBySpaceAndId(String namespace, String zealotId) {
        Document doc = XmlNodeHelper.getDocument(XmlContext.getInstance().getXmlPathMap().get(namespace));
        return doc == null ? null : XmlNodeHelper.getZealotNodeById(doc, zealotId);
    }

    /**
     * 根据 XML 文件的路径判断该 XML 文件是否是 Fenix XML 文件，如果是则返回nameSpace.
     * <p>注：这里简单判断是否有 'fenixs' 根节点即可.</p>
     *
     * @param xmlPath XML 路径
     * @return 该 XML 文件的 Fenix 命名空间 namespace
     */
    public static String getZealotXmlNameSpace(String xmlPath) {
        Document doc;
        try {
            doc = new SAXReader().read(Thread.currentThread().getContextClassLoader().getResourceAsStream(xmlPath));
        } catch (Exception expected) {
            // 由于只是判断该文件是否能被正确解析，并不进行正式的解析，所有这里就不抛出和记录异常了.
            log.warn("解析路径为:【" + xmlPath + "】的 XML 文件出错，请检查其正确性!");
            return null;
        }

        // 获取 XML 文件的根节点，判断其根节点是否为 '<fenixs></fenixs>'，如果是则获取其属性 namespace 的值.
        Node root = doc.getRootElement();
        if (root != null && ROOT_NAME.equals(root.getName())) {
            String nameSpace = getNodeText(root.selectSingleNode(XpathConst.ATTR_NAMESPACE));
            if (StringHelper.isBlank(nameSpace)) {
                log.warn("Fenix XML 文件:【" + xmlPath + "】的根节点 namespace 命名空间属性未配置，请配置，否则将被忽略!");
                return null;
            }
            return nameSpace;
        }

        return null;
    }

    /**
     * 根据 XML 文件 Docment 中的 fenix 节点 id 值获取对应的第一个节点.
     *
     * <p>使用 xPath 语法获取第一个符合条件的节点.</p>
     * @param doc docment文档
     * @param id fenix 节点的 id
     * @return dom4j 的 Node 节点
     */
    public static Node getZealotNodeById(Document doc, String id) {
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
     * 获取节点文本.
     *
     * @param node dom4j 节点
     * @param attrName 节点属性
     * @return 返回节点文本值
     */
    public static String getNodeAttrText(Node node, String attrName) {
        return XmlNodeHelper.getNodeText(node.selectSingleNode(attrName));
    }

    /**
     * 检查和获取节点文本，会检查节点是否为空，如果节点为空，则抛出异常.
     *
     * @param node dom4j 节点
     * @param nodeName 节点名称
     * @return 返回节点文本值
     */
    public static String getAndCheckNodeText(Node node, String nodeName) {
        // 判断必填的参数是否为空.
        String fieldText = XmlNodeHelper.getNodeText(node.selectSingleNode(nodeName));
        if (StringHelper.isBlank(fieldText)) {
            throw new FieldEmptyException("填写的字段值是空的");
        }
        return fieldText;
    }

    /**
     * 检查和获取开始和结束文本的内容，返回一个数组，会检查两个节点是否为空，如果都为空，则抛出异常.
     *
     * @param node dom4j 节点
     * @return 返回开始和结束文本的二元数组
     */
    public static String[] getBothCheckNodeText(Node node) {
        String startText = XmlNodeHelper.getNodeText(node.selectSingleNode(XpathConst.ATTR_START));
        String endText = XmlNodeHelper.getNodeText(node.selectSingleNode(XpathConst.ATTR_ENT));
        if (StringHelper.isBlank(startText) && StringHelper.isBlank(endText)) {
            throw new FieldEmptyException("填写的开始和结束字段值是空的！");
        }
        return new String[] {startText, endText};
    }

}