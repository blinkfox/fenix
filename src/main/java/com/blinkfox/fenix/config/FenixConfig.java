package com.blinkfox.fenix.config;

import static com.blinkfox.fenix.consts.SymbolConst.*;

import com.blinkfox.fenix.config.entity.NormalConfig;
import com.blinkfox.fenix.config.entity.TagHandler;
import com.blinkfox.fenix.config.entity.XmlContext;
import com.blinkfox.fenix.core.FenixHandler;
import com.blinkfox.fenix.core.FenixHandlerFactory;
import com.blinkfox.fenix.core.concrete.BetweenHandler;
import com.blinkfox.fenix.core.concrete.ChooseHandler;
import com.blinkfox.fenix.core.concrete.EndsWithHandler;
import com.blinkfox.fenix.core.concrete.ImportHandler;
import com.blinkfox.fenix.core.concrete.InHandler;
import com.blinkfox.fenix.core.concrete.IsNullHandler;
import com.blinkfox.fenix.core.concrete.LikeHandler;
import com.blinkfox.fenix.core.concrete.NormalHandler;
import com.blinkfox.fenix.core.concrete.SetHandler;
import com.blinkfox.fenix.core.concrete.StartsWithHandler;
import com.blinkfox.fenix.core.concrete.TextHandler;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

import org.dom4j.Node;

/**
 * Fenix 的默认主配置类.
 *
 * @author blinkfox on 2019-08-04.
 * @see FenixConfigManager
 * @see FenixHandlerFactory
 * @see TagHandler
 */
public class FenixConfig {

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
    private static final Map<String, TagHandler> tagHandlerMap = new HashMap<>(128);
    
    /* ------- 添加默认的标签和对应的 TagHandler 处理器，如：普通条件, 'like', 'between', 'in' 等. ------- */

    static {
        // “等于”的相关标签：equal、andEqual、orEqual.
        add("equal", NormalHandler::new, EQUAL);
        add("andEqual", AND, NormalHandler::new, EQUAL);
        add("orEqual", OR, NormalHandler::new, EQUAL);

        // “不等于”的相关标签：notEqual、andNotEqual、orNotEqual.
        add("notEqual", NormalHandler::new, NOT_EQUAL);
        add("andNotEqual", AND, NormalHandler::new, NOT_EQUAL);
        add("orNotEqual", OR, NormalHandler::new, NOT_EQUAL);

        // “大于”的相关标签：greaterThan、andGreaterThan、orGreaterThan.
        add("greaterThan", NormalHandler::new, GT);
        add("andGreaterThan", AND, NormalHandler::new, GT);
        add("orGreaterThan", OR, NormalHandler::new, GT);

        // “小于”的相关标签：lessThan、andLessThan、orLessThan.
        add("lessThan", NormalHandler::new, LT);
        add("andLessThan", AND, NormalHandler::new, LT);
        add("orLessThan", OR, NormalHandler::new, LT);

        // “大于等于”的相关标签：greaterThanEqual、andGreaterThanEqual、orGreaterThanEqual.
        add("greaterThanEqual", NormalHandler::new, GTE);
        add("andGreaterThanEqual", AND, NormalHandler::new, GTE);
        add("orGreaterThanEqual", OR, NormalHandler::new, GTE);

        // “小于等于”的相关标签：lessThanEqual、andLessThanEqual、orLessThanEqual.
        add("lessThanEqual", NormalHandler::new, LTE);
        add("andLessThanEqual", AND, NormalHandler::new, LTE);
        add("orLessThanEqual", OR, NormalHandler::new, LTE);

        // "LIKE" 的相关标签：like、andLike、orLike.
        add("like", LikeHandler::new, LIKE);
        add("andLike", AND, LikeHandler::new, LIKE);
        add("orLike", OR, LikeHandler::new, LIKE);

        // "NOT LIKE" 的相关标签：notLike、andNotLike、orNotLike.
        add("notLike", LikeHandler::new, NOT_LIKE);
        add("andNotLike", AND, LikeHandler::new, NOT_LIKE);
        add("orNotLike", OR, LikeHandler::new, NOT_LIKE);

        // "LIKE 前缀匹配" 的相关标签：startsWith、andStartsWith、orStartsWith.
        add("startsWith", StartsWithHandler::new, LIKE);
        add("andStartsWith", AND, StartsWithHandler::new, LIKE);
        add("orStartsWith", OR, StartsWithHandler::new, LIKE);

        // "NOT LIKE 前缀匹配" 的相关标签：notStartsWith、andNotStartsWith、orNotStartsWith.
        add("notStartsWith", StartsWithHandler::new, NOT_LIKE);
        add("andNotStartsWith", AND, StartsWithHandler::new, NOT_LIKE);
        add("orNotStartsWith", OR, StartsWithHandler::new, NOT_LIKE);

        // "LIKE 后缀匹配" 的相关标签：endsWith、andEndsWith、orEndsWith.
        add("endsWith", EndsWithHandler::new, LIKE);
        add("andEndsWith", AND, EndsWithHandler::new, LIKE);
        add("orEndsWith", OR, EndsWithHandler::new, LIKE);

        // "NOT LIKE 后缀匹配" 的相关标签：notEndsWith、andNotEndsWith、orNotEndsWith.
        add("notEndsWith", EndsWithHandler::new, NOT_LIKE);
        add("andNotEndsWith", AND, EndsWithHandler::new, NOT_LIKE);
        add("orNotEndsWith", OR, EndsWithHandler::new, NOT_LIKE);

        // "BETWEEN" 的相关标签：between、andBetween、orBetween.
        add("between", BetweenHandler::new);
        add("andBetween", AND, BetweenHandler::new);
        add("orBetween", OR, BetweenHandler::new);

        // "IN" 的相关标签：in、andIn、orIn.
        add("in", InHandler::new, IN);
        add("andIn", AND, InHandler::new, IN);
        add("orIn", OR, InHandler::new, IN);

        // "NOT IN" 的相关标签：notIn、andNotIn、orNotIn.
        add("notIn", InHandler::new, NOT_IN);
        add("andNotIn", AND, InHandler::new, NOT_IN);
        add("orNotIn", OR, InHandler::new, NOT_IN);

        // "IS NULL" 的相关标签：isNull、andIsNull、orIsNull.
        add("isNull", IsNullHandler::new, IS_NULL);
        add("andIsNull", AND, IsNullHandler::new, IS_NULL);
        add("orIsNull", OR, IsNullHandler::new, IS_NULL);

        // "IS NOT NULL" 的相关标签：isNotNull、andIsNotNull、orIsNotNull.
        add("isNotNull", IsNullHandler::new, IS_NOT_NULL);
        add("andIsNotNull", AND, IsNullHandler::new, IS_NOT_NULL);
        add("orIsNotNull", OR, IsNullHandler::new, IS_NOT_NULL);

        // 其他标签：text、import、choose.
        add("text", TextHandler::new);
        add("import", ImportHandler::new);
        add("choose", ChooseHandler::new);
        add("set", SetHandler::new);
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
     * 添加自定义标签和该 SQL 片段对应的 {@link FenixHandlerFactory} 处理器工厂实例.
     *
     * @param tagName 标签名称
     * @param handlerFactory 标签处理器工厂
     */
    protected static void add(String tagName, FenixHandlerFactory handlerFactory) {
        tagHandlerMap.put(tagName, new TagHandler(handlerFactory));
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
     * 添加自定义标签、SQL 片段前缀和该 SQL 片段对应的 {@link FenixHandlerFactory} 处理器工厂实现的实例.
     *
     * @param tagName 标签名称
     * @param prefix SQL 片段前缀
     * @param handlerFactory 标签处理器工厂
     */
    protected static void add(String tagName, String prefix, FenixHandlerFactory handlerFactory) {
        tagHandlerMap.put(tagName, new TagHandler(prefix, handlerFactory));
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
     * 添加自定义标签、SQL 操作符和该 SQL 片段对应的 {@link FenixHandlerFactory} 处理器工厂实现类的实例.
     *
     * @param tagName 标签名称
     * @param handlerFactory 标签处理器工厂
     * @param symbol SQL 操作符
     */
    protected static void add(String tagName, FenixHandlerFactory handlerFactory, String symbol) {
        tagHandlerMap.put(tagName, new TagHandler(handlerFactory, symbol));
    }

    /**
     * 添加自定义标签、SQL 片段前缀、SQL 操作符和该 SQL 片段对应的 {@link TagHandler} 处理器实现的 class.
     *
     * @param tagName 标签名称
     * @param prefix 前缀
     * @param handlerCls 标签处理器类的 Class
     * @param symbol SQL 操作符
     */
    protected static void add(String tagName, String prefix, Class<? extends FenixHandler> handlerCls, String symbol) {
        tagHandlerMap.put(tagName, new TagHandler(prefix, handlerCls, symbol));
    }

    /**
     * 添加自定义标签、SQL 片段前缀、SQL 操作符和该 SQL 片段对应的 {@link FenixHandlerFactory} 处理器工厂实现类的实例.
     *
     * @param tagName 标签名称
     * @param prefix 前缀
     * @param handlerFactory 标签处理器工厂
     * @param symbol SQL 操作符
     */
    protected static void add(String tagName, String prefix, FenixHandlerFactory handlerFactory, String symbol) {
        tagHandlerMap.put(tagName, new TagHandler(prefix, handlerFactory, symbol));
    }

}
