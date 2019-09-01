package com.blinkfox.fenix.helper;

import com.blinkfox.fenix.config.FenixConfig;
import com.blinkfox.fenix.consts.Const;
import com.blinkfox.fenix.consts.XpathConst;
import com.blinkfox.fenix.exception.FieldEmptyException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.dom4j.Node;

/**
 * XML 文件和 XML 标签节点相关操作的工具类.
 *
 * @author blinkfox on 2019-08-04.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class XmlNodeHelper {

    /**
     * 根据 XML 文件的 namespace 及 Fenix 节点 id 值获取对应的第一个 dom4j 节点.
     *
     * @param namespace XML 文件对应命名空间
     * @param fenixId fenixId
     * @return dom4j的Node节点
     */
    public static Node getNodeBySpaceAndId(String namespace, String fenixId) {
        return FenixConfig.getFenixs().get(StringHelper.concat(namespace, Const.DOT, fenixId));
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
