package com.blinkfox.fenix.config;

import com.blinkfox.fenix.config.entity.TagHandler;
import com.blinkfox.fenix.consts.Const;
import com.blinkfox.fenix.consts.SymbolConst;
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
@Getter
public class FenixConfig {

    /**
     * Fenix 加载完成后是否打印启动的 banner，默认打印.
     */
    private boolean printBanner = true;

    /**
     * 是否打印 SqlInfo 信息.
     */
    private boolean printSqlInfo;

    /**
     * Fenix 的 XML 文件所在的位置，多个用逗号隔开，可以是目录也可以是具体的 XML 文件
     * ，默认是 'fenix' 目录.
     */
    protected String xmlLocations = Const.DEFAULT_FENIX_XML_DIR;

    /**
     * Fenix 自定义的 {@link com.blinkfox.fenix.config.entity.TagHandler} 处理器实现的所在位置，
     * 多个用逗号隔开，可以是目录也可以是具体的 java 或 class 文件路径.
     */
    protected String handlerLocations;

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
    
    static {
        /* ------- 添加默认的标签和对应的 TagHandler 处理器，如：普通条件, 'like', 'between', 'in' 等. ------- */

        // “等于”的相关标签：equal、andEqual、orEqual.
        add("equal", NormalHandler::new, SymbolConst.EQUAL);
        add("andEqual", SymbolConst.AND, NormalHandler::new, SymbolConst.EQUAL);
        add("orEqual", SymbolConst.OR, NormalHandler::new, SymbolConst.EQUAL);

        // “不等于”的相关标签：notEqual、andNotEqual、orNotEqual.
        add("notEqual", NormalHandler::new, SymbolConst.NOT_EQUAL);
        add("andNotEqual", SymbolConst.AND, NormalHandler::new, SymbolConst.NOT_EQUAL);
        add("orNotEqual", SymbolConst.OR, NormalHandler::new, SymbolConst.NOT_EQUAL);

        // “大于”的相关标签：greaterThan、andGreaterThan、orGreaterThan.
        add("greaterThan", NormalHandler::new, SymbolConst.GT);
        add("andGreaterThan", SymbolConst.AND, NormalHandler::new, SymbolConst.GT);
        add("orGreaterThan", SymbolConst.OR, NormalHandler::new, SymbolConst.GT);

        // “小于”的相关标签：lessThan、andLessThan、orLessThan.
        add("lessThan", NormalHandler::new, SymbolConst.LT);
        add("andLessThan", SymbolConst.AND, NormalHandler::new, SymbolConst.LT);
        add("orLessThan", SymbolConst.OR, NormalHandler::new, SymbolConst.LT);

        // “大于等于”的相关标签：greaterThanEqual、andGreaterThanEqual、orGreaterThanEqual.
        add("greaterThanEqual", NormalHandler::new, SymbolConst.GTE);
        add("andGreaterThanEqual", SymbolConst.AND, NormalHandler::new, SymbolConst.GTE);
        add("orGreaterThanEqual", SymbolConst.OR, NormalHandler::new, SymbolConst.GTE);

        // “小于等于”的相关标签：lessThanEqual、andLessThanEqual、orLessThanEqual.
        add("lessThanEqual", NormalHandler::new, SymbolConst.LTE);
        add("andLessThanEqual", SymbolConst.AND, NormalHandler::new, SymbolConst.LTE);
        add("orLessThanEqual", SymbolConst.OR, NormalHandler::new, SymbolConst.LTE);

        // "LIKE" 的相关标签：like、andLike、orLike.
        add("like", LikeHandler::new, SymbolConst.LIKE);
        add("andLike", SymbolConst.AND, LikeHandler::new, SymbolConst.LIKE);
        add("orLike", SymbolConst.OR, LikeHandler::new, SymbolConst.LIKE);

        // "NOT LIKE" 的相关标签：notLike、andNotLike、orNotLike.
        add("notLike", LikeHandler::new, SymbolConst.NOT_LIKE);
        add("andNotLike", SymbolConst.AND, LikeHandler::new, SymbolConst.NOT_LIKE);
        add("orNotLike", SymbolConst.OR, LikeHandler::new, SymbolConst.NOT_LIKE);

        // "LIKE 前缀匹配" 的相关标签：startsWith、andStartsWith、orStartsWith.
        add("startsWith", StartsWithHandler::new, SymbolConst.LIKE);
        add("andStartsWith", SymbolConst.AND, StartsWithHandler::new, SymbolConst.LIKE);
        add("orStartsWith", SymbolConst.OR, StartsWithHandler::new, SymbolConst.LIKE);

        // "NOT LIKE 前缀匹配" 的相关标签：notStartsWith、andNotStartsWith、orNotStartsWith.
        add("notStartsWith", StartsWithHandler::new, SymbolConst.NOT_LIKE);
        add("andNotStartsWith", SymbolConst.AND, StartsWithHandler::new, SymbolConst.NOT_LIKE);
        add("orNotStartsWith", SymbolConst.OR, StartsWithHandler::new, SymbolConst.NOT_LIKE);

        // "LIKE 后缀匹配" 的相关标签：endsWith、andEndsWith、orEndsWith.
        add("endsWith", EndsWithHandler::new, SymbolConst.LIKE);
        add("andEndsWith", SymbolConst.AND, EndsWithHandler::new, SymbolConst.LIKE);
        add("orEndsWith", SymbolConst.OR, EndsWithHandler::new, SymbolConst.LIKE);

        // "NOT LIKE 后缀匹配" 的相关标签：notEndsWith、andNotEndsWith、orNotEndsWith.
        add("notEndsWith", EndsWithHandler::new, SymbolConst.NOT_LIKE);
        add("andNotEndsWith", SymbolConst.AND, EndsWithHandler::new, SymbolConst.NOT_LIKE);
        add("orNotEndsWith", SymbolConst.OR, EndsWithHandler::new, SymbolConst.NOT_LIKE);

        // "BETWEEN" 的相关标签：between、andBetween、orBetween.
        add("between", BetweenHandler::new);
        add("andBetween", SymbolConst.AND, BetweenHandler::new);
        add("orBetween", SymbolConst.OR, BetweenHandler::new);

        // "IN" 的相关标签：in、andIn、orIn.
        add("in", InHandler::new, SymbolConst.IN);
        add("andIn", SymbolConst.AND, InHandler::new, SymbolConst.IN);
        add("orIn", SymbolConst.OR, InHandler::new, SymbolConst.IN);

        // "NOT IN" 的相关标签：notIn、andNotIn、orNotIn.
        add("notIn", InHandler::new, SymbolConst.NOT_IN);
        add("andNotIn", SymbolConst.AND, InHandler::new, SymbolConst.NOT_IN);
        add("orNotIn", SymbolConst.OR, InHandler::new, SymbolConst.NOT_IN);

        // "IS NULL" 的相关标签：isNull、andIsNull、orIsNull.
        add("isNull", IsNullHandler::new, SymbolConst.IS_NULL);
        add("andIsNull", SymbolConst.AND, IsNullHandler::new, SymbolConst.IS_NULL);
        add("orIsNull", SymbolConst.OR, IsNullHandler::new, SymbolConst.IS_NULL);

        // "IS NOT NULL" 的相关标签：isNotNull、andIsNotNull、orIsNotNull.
        add("isNotNull", IsNullHandler::new, SymbolConst.IS_NOT_NULL);
        add("andIsNotNull", SymbolConst.AND, IsNullHandler::new, SymbolConst.IS_NOT_NULL);
        add("orIsNotNull", SymbolConst.OR, IsNullHandler::new, SymbolConst.IS_NOT_NULL);

        // 其他标签：text、import、choose.
        add("text", TextHandler::new);
        add("import", ImportHandler::new);
        add("choose", ChooseHandler::new);
        add("set", SetHandler::new);
    }

    /**
     * 设置是否打印 Fenix 的 Banner 信息.
     *
     * @param enabled 是否开启打印 banner 的标识
     * @return {@link FenixConfig} 实例自身
     */
    public FenixConfig setPrintBanner(boolean enabled) {
        this.printBanner = enabled;
        return this;
    }

    /**
     * 设置是否打印 {@link com.blinkfox.fenix.bean.SqlInfo} 中的 SQL 语句和参数信息.
     *
     * @param enabled 是否开启 SQL 和参数打印的标识
     * @return {@link FenixConfig} 实例自身
     */
    public FenixConfig setPrintSqlInfo(boolean enabled) {
        this.printSqlInfo = enabled;
        return this;
    }

    /**
     * 设置 Fenix XML 文件的所在位置，多个用逗号隔开，可以是资源目录也可以是具体的 XML 资源文件.
     *
     * @param xmlLocations XML 位置
     * @return {@link FenixConfig} 实例自身
     */
    public FenixConfig setXmlLocations(String xmlLocations) {
        this.xmlLocations = xmlLocations;
        return this;
    }

    /**
     * 设置自定义的 {@link com.blinkfox.fenix.config.entity.TagHandler} 处理器实现的所在位置，
     *     多个用逗号隔开，可以是目录也可以是具体的 java 或 class 文件路径.
     *
     * @param handlerLocations handler 的包路径位置，如：'com.blinkfox.handler'.
     * @return {@link FenixConfig} 实例自身
     */
    public FenixConfig setHandlerLocations(String handlerLocations) {
        this.handlerLocations = handlerLocations;
        return this;
    }

    /**
     * 添加自定义标签和该 SQL 片段对应的 {@link TagHandler} 处理器实现的 class.
     *
     * @param tagName 标签名称
     * @param handlerCls 标签处理器类的 Class
     */
    public static void add(String tagName, Class<? extends FenixHandler> handlerCls) {
        tagHandlerMap.put(tagName, new TagHandler(handlerCls));
    }

    /**
     * 添加自定义标签和该 SQL 片段对应的 {@link FenixHandlerFactory} 处理器工厂实例.
     *
     * @param tagName 标签名称
     * @param handlerFactory 标签处理器工厂
     */
    public static void add(String tagName, FenixHandlerFactory handlerFactory) {
        tagHandlerMap.put(tagName, new TagHandler(handlerFactory));
    }

    /**
     * 添加自定义标签、SQL 片段前缀和该 SQL 片段对应的 {@link TagHandler} 处理器实现的 class.
     *
     * @param tagName 标签名称
     * @param prefix SQL 片段前缀
     * @param handlerCls 标签处理器类的 Class
     */
    public static void add(String tagName, String prefix, Class<? extends FenixHandler> handlerCls) {
        tagHandlerMap.put(tagName, new TagHandler(prefix, handlerCls));
    }

    /**
     * 添加自定义标签、SQL 片段前缀和该 SQL 片段对应的 {@link FenixHandlerFactory} 处理器工厂实现的实例.
     *
     * @param tagName 标签名称
     * @param prefix SQL 片段前缀
     * @param handlerFactory 标签处理器工厂
     */
    public static void add(String tagName, String prefix, FenixHandlerFactory handlerFactory) {
        tagHandlerMap.put(tagName, new TagHandler(prefix, handlerFactory));
    }

    /**
     * 添加自定义标签、SQL 操作符和该 SQL 片段对应的 {@link TagHandler} 处理器实现的 class.
     *
     * @param tagName 标签名称
     * @param handlerCls 标签处理器类的 Class
     * @param symbol SQL 操作符
     */
    public static void add(String tagName, Class<? extends FenixHandler> handlerCls, String symbol) {
        tagHandlerMap.put(tagName, new TagHandler(handlerCls, symbol));
    }

    /**
     * 添加自定义标签、SQL 操作符和该 SQL 片段对应的 {@link FenixHandlerFactory} 处理器工厂实现类的实例.
     *
     * @param tagName 标签名称
     * @param handlerFactory 标签处理器工厂
     * @param symbol SQL 操作符
     */
    public static void add(String tagName, FenixHandlerFactory handlerFactory, String symbol) {
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
    public static void add(String tagName, String prefix, Class<? extends FenixHandler> handlerCls, String symbol) {
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
    public static void add(String tagName, String prefix, FenixHandlerFactory handlerFactory, String symbol) {
        tagHandlerMap.put(tagName, new TagHandler(prefix, handlerFactory, symbol));
    }

}
