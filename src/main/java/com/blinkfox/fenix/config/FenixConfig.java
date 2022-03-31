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
import com.blinkfox.fenix.core.concrete.TrimWhereHandler;
import com.blinkfox.fenix.core.concrete.WhereHandler;
import com.blinkfox.fenix.specification.handler.AbstractPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.BetweenPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.EndsWithPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.EqualsPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.GreaterThanEqualPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.GreaterThanPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.InPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.IsNotNullPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.IsNullPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.JoinPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.LessThanEqualPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.LessThanPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.LikeInPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.LikeOrLikePredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.LikePatternPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.LikePredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.NotBetweenPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.NotEndsWithPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.NotEqualsPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.NotInPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.NotLikePatternPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.NotLikePredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.NotStartsWithPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.OrBetweenPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.OrEndsWithPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.OrEqualsPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.OrGreaterThanEqualPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.OrGreaterThanPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.OrInPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.OrIsNotNullPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.OrIsNullPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.OrLessThanEqualPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.OrLessThanPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.OrLikeOrLikePredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.OrLikePatternPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.OrLikePredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.OrNotBetweenPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.OrNotEndsWithPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.OrNotEqualsPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.OrNotInPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.OrNotLikePatternPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.OrNotLikePredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.OrNotStartsWithPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.OrStartsWithPredicateHandler;
import com.blinkfox.fenix.specification.handler.impl.StartsWithPredicateHandler;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import lombok.Getter;
import org.dom4j.Node;

/**
 * Fenix 的默认主配置类.
 *
 * @author blinkfox on 2019-08-04.
 * @see FenixConfigManager
 * @see FenixHandlerFactory
 * @see TagHandler
 * @since v1.0.0
 */
@Getter
public class FenixConfig {

    /**
     * 是否开启 debug 模式.
     *
     * @since v2.4.1
     */
    private boolean debug;

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
     * 采用带前缀的下划线转自定义 Bean 时需要移除的前缀集合的字符串，多个前缀字符串用逗号隔开.
     *
     * @since v2.7.0
     */
    private String underscoreTransformerPrefix;

    /**
     * 所有 Fenix XML 文档的缓存 map.
     *
     * <p>该 Map 的 key 是资源的路径（将 XML 命名空间和 fenixId 用"."号分割），value 是 dom4j 的文档节点 Node.</p>
     */
    @Getter
    private static final Map<String, Node> fenixs = new HashMap<>();

    /**
     * 所有 Fenix XML 命名空间 namespace 和 XML 文件的 URL 映射关系的 Map.
     *
     * <p>该 Map 的 key 是 XML 文件命名空间 namespace 的值，value 是 XML 文件的 URL.</p>
     *
     * @since v2.4.1
     */
    @Getter
    private static final Map<String, Set<URL>> xmlUrlMap = new HashMap<>();

    /**
     * 初始化默认的一些标签和 TagHandler 实例到 HashMap 集合中，key 是标签字符串,value 是 TagHandler 实例.
     */
    @Getter
    private static final Map<String, TagHandler> tagHandlerMap = new HashMap<>(128);

    /**
     * 初始化默认的注解 {@code Class} 和对应处理器实例到 HashMap 集合中，key 是注解 {@code Class}，value 是注解对应的处理器实例.
     */
    @Getter
    private static final Map<Class<?>, AbstractPredicateHandler> specificationHandlerMap = new HashMap<>(64);

    static {
        initDefaultTagHandler();
        initDefaultSpecificationHandlers();
    }

    /**
     * 初始化添加默认的标签和对应的 TagHandler 处理器，如：普通条件, 'like', 'between', 'in' 等.
     */
    private static void initDefaultTagHandler() {
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
        add("where", WhereHandler::new);
        add("trimWhere", TrimWhereHandler::new);
    }

    /**
     * 初始化添加默认的注解 {@code Class} 和对应处理器实例到 HashMap 集合中等.
     */
    private static void initDefaultSpecificationHandlers() {
        add(new EqualsPredicateHandler());
        add(new GreaterThanEqualPredicateHandler());
        add(new GreaterThanPredicateHandler());
        add(new InPredicateHandler());
        add(new IsNotNullPredicateHandler());
        add(new IsNullPredicateHandler());
        add(new JoinPredicateHandler());
        add(new LessThanEqualPredicateHandler());
        add(new LessThanPredicateHandler());
        add(new BetweenPredicateHandler());
        add(new NotBetweenPredicateHandler());
        add(new LikeInPredicateHandler());
        add(new LikeOrLikePredicateHandler());
        add(new LikePredicateHandler());
        add(new NotLikePredicateHandler());
        add(new StartsWithPredicateHandler());
        add(new NotStartsWithPredicateHandler());
        add(new EndsWithPredicateHandler());
        add(new NotEndsWithPredicateHandler());
        add(new LikePatternPredicateHandler());
        add(new NotLikePatternPredicateHandler());
        add(new NotEqualsPredicateHandler());
        add(new NotInPredicateHandler());
        add(new OrEqualsPredicateHandler());
        add(new OrGreaterThanEqualPredicateHandler());
        add(new OrGreaterThanPredicateHandler());
        add(new OrInPredicateHandler());
        add(new OrIsNotNullPredicateHandler());
        add(new OrIsNullPredicateHandler());
        add(new OrLessThanEqualPredicateHandler());
        add(new OrLessThanPredicateHandler());
        add(new OrBetweenPredicateHandler());
        add(new OrNotBetweenPredicateHandler());
        add(new OrLikeOrLikePredicateHandler());
        add(new OrLikePredicateHandler());
        add(new OrNotLikePredicateHandler());
        add(new OrNotEqualsPredicateHandler());
        add(new OrNotInPredicateHandler());
        add(new OrStartsWithPredicateHandler());
        add(new OrNotStartsWithPredicateHandler());
        add(new OrEndsWithPredicateHandler());
        add(new OrNotEndsWithPredicateHandler());
        add(new OrLikePatternPredicateHandler());
        add(new OrNotLikePatternPredicateHandler());
    }

    /**
     * 设置是否开启 debug 模式.
     *
     * <p>开启之后 XML 的 SQL 方式，将直接从 XML 中文件中实时读取和解析资源，不会走缓存，建议仅开发环境开启此功能.</p>
     *
     * @param debug 是否开启 debug 模式
     * @return {@link FenixConfig} 实例自身
     * @author blinkfox on 2021-01-01.
     * @since v2.4.1
     */
    public FenixConfig setDebug(boolean debug) {
        this.debug = debug;
        return this;
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
     * 多个用逗号隔开，可以是目录也可以是具体的 java 或 class 文件路径.
     *
     * @param handlerLocations handler 的包路径位置，如：'com.blinkfox.handler'.
     * @return {@link FenixConfig} 实例自身
     */
    public FenixConfig setHandlerLocations(String handlerLocations) {
        this.handlerLocations = handlerLocations;
        return this;
    }

    /**
     * 设置自定义的 {@link com.blinkfox.fenix.config.entity.TagHandler} 处理器实现的所在位置，
     * 多个用逗号隔开，可以是目录也可以是具体的 java 或 class 文件路径.
     *
     * @param underscoreTransformerPrefix 自定义的前缀字符串，多个用逗号隔开.
     * @return {@link FenixConfig} 实例自身
     * @since v2.7.0
     */
    public FenixConfig setUnderscoreTransformerPrefix(String underscoreTransformerPrefix) {
        this.underscoreTransformerPrefix = underscoreTransformerPrefix;
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

    /**
     * 将注解的 {@code class} 作为 key，其对应的 {@link AbstractPredicateHandler} 处理器实例作为 value 存入到 Map 中.
     *
     * @param handlerSupplier {@link AbstractPredicateHandler} 处理器提供者
     */
    public static void add(Supplier<AbstractPredicateHandler> handlerSupplier) {
        AbstractPredicateHandler handler = handlerSupplier.get();
        specificationHandlerMap.put(handler.getAnnotation(), handler);
    }

    /**
     * 将注解的 {@code class} 作为 key，其对应的 {@link AbstractPredicateHandler} 处理器实例作为 value 存入到 Map 中.
     *
     * @param handler {@link AbstractPredicateHandler} 处理器实例
     */
    public static void add(AbstractPredicateHandler handler) {
        specificationHandlerMap.put(handler.getAnnotation(), handler);
    }

}
