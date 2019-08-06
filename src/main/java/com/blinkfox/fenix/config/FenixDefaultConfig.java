package com.blinkfox.fenix.config;

import com.blinkfox.fenix.config.entity.NormalConfig;
import com.blinkfox.fenix.config.entity.TagHandler;
import com.blinkfox.fenix.config.entity.XmlContext;
import com.blinkfox.fenix.core.FenixHandler;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

import org.dom4j.Node;

/**
 * Fenix 的默认主配置类.
 *
 * @author blinkfox on 2019-08-04.
 * @see TagHandler
 */
public class FenixDefaultConfig {

    /**
     * 所有 Fenix XML 文档的缓存 map.
     *
     * <p>该 Map 的 key 是资源的路径（将 XML 命名空间和 fenixId 用"."号分割），value 是 dom4j 的文档节点 Node.</p>
     */
    @Getter
    private static final Map<String, Node> fenixs = new HashMap<>();

    /**
     * 初始化默认的一些标签和 TagHandler 实例到 HashMap 集合中，key 是标签字符串,value 是 TagHandler 实例.
     */
    @Getter
    private static final Map<String, TagHandler> tagHandlerMap = new HashMap<>();
    
    /* 添加默认的标签和对应的 TagHandler 处理器，如：普通条件, 'like', 'between', 'in' 等. */
    static {
        // 等于相关标签：equal、andEqual、orEqual
//        add(EQUAL, NormalHandler.class, EQUAL_SUFFIX);
//        add(AND_EQUAL, AND_PREFIX, NormalHandler.class, EQUAL_SUFFIX);
//        add(OR_EQUAL, OR_PREFIX, NormalHandler.class, EQUAL_SUFFIX);
//
//        // 不等于相关标签：notEqual、andNotEqual、orNotEqual
//        add(NOT_EQUAL, NormalHandler.class, NOT_EQUAL_SUFFIX);
//        add(AND_NOT_EQUAL, AND_PREFIX, NormalHandler.class, NOT_EQUAL_SUFFIX);
//        add(OR_NOT_EQUAL, OR_PREFIX, NormalHandler.class, NOT_EQUAL_SUFFIX);
//
//        // 大于相关标签：greaterThan、andGreaterThan、orGreaterThan
//        add(MORE, NormalHandler.class, GT_SUFFIX);
//        add(AND_MORE, AND_PREFIX, NormalHandler.class, GT_SUFFIX);
//        add(OR_MORE, OR_PREFIX, NormalHandler.class, GT_SUFFIX);
//
//        // 小于相关标签：lessThan、andGreater、orGreater
//        add(LESS, NormalHandler.class, LT_SUFFIX);
//        add(AND_LESS, AND_PREFIX, NormalHandler.class, LT_SUFFIX);
//        add(OR_LESS, OR_PREFIX, NormalHandler.class, LT_SUFFIX);
//
//        // 大于等于相关标签：greaterEqual、andGreaterEqual、orGreaterEqual
//        add(MORE_EQUAL, NormalHandler.class, GTE_SUFFIX);
//        add(AND_MORE_EQUAL, AND_PREFIX, NormalHandler.class, GTE_SUFFIX);
//        add(OR_MORE_EQUAL, OR_PREFIX, NormalHandler.class, GTE_SUFFIX);
//
//        // 小于等于相关标签：lessEqual、andLessEqual、orLessEqual
//        add(LESS_EQUAL, NormalHandler.class, LTE_SUFFIX);
//        add(AND_LESS_EQUAL, AND_PREFIX, NormalHandler.class, LTE_SUFFIX);
//        add(OR_LESS_EQUAL, OR_PREFIX, NormalHandler.class, LTE_SUFFIX);
//
//        // like相关标签：like、andLike、orLike
//        add(LIKE, LikeHandler.class, LIKE_KEY);
//        add(AND_LIKE, AND_PREFIX, LikeHandler.class, LIKE_KEY);
//        add(OR_LIKE, OR_PREFIX, LikeHandler.class, LIKE_KEY);
//
//        // not like相关标签：notLike、andNotLike、orNotLike
//        add(NOT_LIKE, LikeHandler.class, NOT_LIKE_KEY);
//        add(AND_NOT_LIKE, AND_PREFIX, LikeHandler.class, NOT_LIKE_KEY);
//        add(OR_NOT_LIKE, OR_PREFIX, LikeHandler.class, NOT_LIKE_KEY);
//
//        // between相关标签：between、andBetween、orBetween
//        add(BETWEEN, BetweenHandler.class);
//        add(AND_BETWEEN, AND_PREFIX, BetweenHandler.class);
//        add(OR_BETWEEN, OR_PREFIX, BetweenHandler.class);
//
//        // "IN"相关标签：in、andIn、orIn
//        add(IN, InHandler.class, IN_SUFFIX);
//        add(AND_IN, AND_PREFIX, InHandler.class, IN_SUFFIX);
//        add(OR_IN, OR_PREFIX, InHandler.class, IN_SUFFIX);
//        // "NOT IN"相关标签：notIn、andNotIn、orNotIn
//        add(NOT_IN, InHandler.class, NOT_IN_SUFFIX);
//        add(AND_NOT_IN, AND_PREFIX, InHandler.class, NOT_IN_SUFFIX);
//        add(OR_NOT_IN, OR_PREFIX, InHandler.class, NOT_IN_SUFFIX);
//
//        // "IS NULL"相关标签：isNull、andIsNull、orIsNull
//        add(IS_NULL, IsNullHandler.class, IS_NULL_SUFFIX);
//        add(AND_IS_NULL, AND_PREFIX, IsNullHandler.class, IS_NULL_SUFFIX);
//        add(OR_IS_NULL, OR_PREFIX, IsNullHandler.class, IS_NULL_SUFFIX);
//        // "IS NOT NULL"相关标签：isNotNull、andIsNotNull、orIsNotNull
//        add(IS_NOT_NULL, IsNullHandler.class, IS_NOT_NULL_SUFFIX);
//        add(AND_IS_NOT_NULL, AND_PREFIX, IsNullHandler.class, IS_NOT_NULL_SUFFIX);
//        add(OR_IS_NOT_NULL, OR_PREFIX, IsNullHandler.class, IS_NOT_NULL_SUFFIX);
//
//        // 其他标签：text、include、case
//        add(TEXT, TextHandler.class);
//        add(IMPORT, ImportHandler.class);
//        add(CHOOSE, ChooseHandler.class);
    }

    /**
     * 配置 Fenix 的普通配置信息(默认配置方法，开发者可覆盖此方法来做一些自定义配置).
     *
     * @param normalConfig 常规配置实例
     */
    public void configNormal(NormalConfig normalConfig) {
        normalConfig.setDebug(false).setPrintBanner(true).setPrintSqlInfo(true);
    }

    /**
     * 配置 XML 文件的标识和资源路径.
     *
     * @param ctx xmlContext 实例
     */
    public void configXml(XmlContext ctx) {
        // 子类可重写此方法，来增加新的 XML 及命名空间的配置.
    }

    /**
     * 配置标签和其对应的处理类(默认了许多常用的标签，开发者可覆盖此方法来配置更多的自定义标签).
     */
    public void configTagHandler() {
        // 子类可重写此方法，来增加新的 SQL 标签和该标签对应的处理器.
    }

    /**
     * 添加自定义标签和该 SQL 片段对应的 {@link TagHandler} 处理器实现的 class.
     *
     * @param tagName 标签名称
     * @param handlerCls 标签处理器类的 Class
     */
    protected static void add(String tagName, Class<? extends FenixHandler> handlerCls) {
        tagHandlerMap.put(tagName, new TagHandler(handlerCls));
    }

    /**
     * 添加自定义标签、SQL 片段前缀和该 SQL 片段对应的 {@link TagHandler} 处理器实现的 class.
     *
     * @param tagName 标签名称
     * @param prefix SQL 片段前缀
     * @param handlerCls 标签处理器类的 Class
     */
    protected static void add(String tagName, String prefix, Class<? extends FenixHandler> handlerCls) {
        tagHandlerMap.put(tagName, new TagHandler(prefix, handlerCls));
    }

    /**
     * 添加自定义标签、SQL 操作符和该 SQL 片段对应的 {@link TagHandler} 处理器实现的 class.
     *
     * @param tagName 标签名称
     * @param handlerCls 标签处理器类的 Class
     * @param symbol SQL 操作符
     */
    protected static void add(String tagName, Class<? extends FenixHandler> handlerCls, String symbol) {
        tagHandlerMap.put(tagName, new TagHandler(handlerCls, symbol));
    }

    /**
     * 添加自定义标签、SQL 片段前缀、SQL 操作符和该 SQL 片段对应的 {@link TagHandler} 处理器实现的 class.
     *
     * @param tagName 标签名称
     * @param prefix 前缀
     * @param handlerCls 标签处理器类的 Class
     * @param symbol SQL 操作符
     */
    protected static void add(String tagName, String prefix,
                              Class<? extends FenixHandler> handlerCls, String symbol) {
        tagHandlerMap.put(tagName, new TagHandler(prefix, handlerCls, symbol));
    }

}
