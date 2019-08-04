package com.blinkfox.fenix.config;

import static com.blinkfox.fenix.consts.Const.*;

import com.blinkfox.fenix.config.entity.NormalConfig;
import com.blinkfox.fenix.config.entity.TagHandler;
import com.blinkfox.fenix.config.entity.XmlContext;
import com.blinkfox.fenix.core.IConditHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.dom4j.Node;

/**
 * 抽象的 Fenix 主配置类.
 *
 * @author blinkfox on 2019-08-04.
 */
public class AbstractFenixConfig {

    /**
     * 所有 fenixs XML文档的缓存 map.
     * <p>key是资源的路径（将xml命名空间和zealotId用"@"符号分割），value是dom4j文档节点Node.</p>
     */
    private static final Map<String, Node> zealots = new ConcurrentHashMap<>();

    /** 初始化默认的一些标签和tagHandlers到HashMap集合中,key是标签字符串,value是TagHandler对象. */
    private static final Map<String, TagHandler> tagHandlerMap = new HashMap<>();
    
    /* 添加默认的标签和对应的handler处理器，主要是普通条件,like,between,in等 */
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
     * 获取全局的Zealots文档缓存数据.
     * @return 返回xml命名空间和dom4j文件的缓存map
     */
    public static Map<String, Node> getZealots() {
        return zealots;
    }

    /**
     * 获取全局的标签和对应处理器的tagHandlerMap对象.
     * @return tagHandlerMap 标签和对应处理器的Map
     */
    public static Map<String, TagHandler> getTagHandlerMap() {
        return tagHandlerMap;
    }

    /**
     * 添加自定义标签和其对应的Handler class.
     * @param tagName 标签名称
     * @param handlerCls 动态处理类的反射类型
     */
    protected static void add(String tagName, Class<? extends IConditHandler> handlerCls) {
        tagHandlerMap.put(tagName, new TagHandler(handlerCls));
    }

    /**
     * 添加自定义标签和其对应的Handler class.
     * @param tagName 标签名称
     * @param prefix 前缀
     * @param handlerCls 动态处理类的反射类型
     */
    protected static void add(String tagName, String prefix, Class<? extends IConditHandler> handlerCls) {
        tagHandlerMap.put(tagName, new TagHandler(prefix, handlerCls));
    }

    /**
     * 添加自定义标签和其对应的Handler class.
     * @param tagName 标签名称
     * @param handlerCls 动态处理类的反射类型
     * @param suffix 后缀
     */
    protected static void add(String tagName, Class<? extends IConditHandler> handlerCls, String suffix) {
        tagHandlerMap.put(tagName, new TagHandler(handlerCls, suffix));
    }

    /**
     * 添加自定义标签和其对应的Handler class.
     * @param tagName 标签名称
     * @param prefix 前缀
     * @param handlerCls 动态处理类的反射类型
     * @param suffix 后缀
     */
    protected static void add(String tagName, String prefix,
                              Class<? extends IConditHandler> handlerCls, String suffix) {
        tagHandlerMap.put(tagName, new TagHandler(prefix, handlerCls, suffix));
    }

    /**
     * 配置Zealot的普通配置信息(默认配置方法，开发者可覆盖此方法来做一些自定义配置).
     * @param normalConfig 普通配置实例
     */
    public void configNormal(NormalConfig normalConfig) {
        normalConfig.setDebug(false).setPrintBanner(true).setPrintSqlInfo(true);
    }

    /**
     * 配置xml文件的标识和资源路径.
     * @param ctx xmlContext对象
     */
    public void configXml(XmlContext ctx) {
        // 子类可覆盖此方法，来增加新的xml及命名空间的配置.
    }

    /**
     * 配置标签和其对应的处理类(默认了许多常用的标签，开发者可覆盖此方法来配置更多的自定义标签).
     */
    public void configTagHandler() {
        // 子类可覆盖此方法，来增加新的标签和处理器的配置.
    }

}