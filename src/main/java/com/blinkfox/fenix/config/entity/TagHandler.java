package com.blinkfox.fenix.config.entity;

import com.blinkfox.fenix.consts.Const;
import com.blinkfox.fenix.core.FenixHandler;
import com.blinkfox.fenix.core.FenixHandlerFactory;
import lombok.Getter;

/**
 * XML 标签对应的动态 SQL 片段生成的处理类.
 *
 * @author blinkfox on 2019-08-04.
 * @see FenixHandler
 */
public class TagHandler {

    /**
     * 生成 SQL 片段的前缀，如: 'and', 'or' 等.
     */
    @Getter
    private String prefix;

    /**
     * 生成动态 SQL 片段的处理实现反射类型，如: {@link com.blinkfox.fenix.core.concrete.LikeHandler}.
     */
    @Getter
    private Class<? extends FenixHandler> handlerCls;

    /**
     * 生成动态 SQL 片段的处理实现类的 {@link FenixHandlerFactory} 工厂类.
     *
     * <p>注：handlerFactory 和 handlerCls 只能一个有值.</p>
     */
    @Getter
    private FenixHandlerFactory handlerFactory;

    /**
     * 生成动态 SQL 片段的 SQL 操作符，如: '>', '=', 'LIKE' 等.
     */
    @Getter
    private String symbol;

    /**
     * 仅标签对应的标签处理器 class 的构造方法.
     *
     * @param handlerCls 标签处理器的 class
     */
    public TagHandler(Class<? extends FenixHandler> handlerCls) {
        this.prefix = Const.SPACE;
        this.handlerCls = handlerCls;
    }

    /**
     * 仅标签对应的标签处理器工厂 {@link FenixHandlerFactory} 的构造方法.
     *
     * @param handlerFactory 标签处理器工厂
     */
    public TagHandler(FenixHandlerFactory handlerFactory) {
        this.prefix = Const.SPACE;
        this.handlerFactory = handlerFactory;
    }

    /**
     * 含 SQL 片段前缀和标签处理器 class 的构造方法.
     *
     * @param prefix SQL 前缀
     * @param handlerCls 标签处理器的 class
     */
    public TagHandler(String prefix, Class<? extends FenixHandler> handlerCls) {
        this.prefix = prefix;
        this.handlerCls = handlerCls;
    }

    /**
     * 含 SQL 片段前缀和标签处理器工厂 {@link FenixHandlerFactory} 的构造方法.
     *
     * @param prefix SQL 前缀
     * @param handlerFactory 标签处理器工厂
     */
    public TagHandler(String prefix, FenixHandlerFactory handlerFactory) {
        this.prefix = prefix;
        this.handlerFactory = handlerFactory;
    }

    /**
     * 含标签处理器、SQL 操作符的构造方法.
     *
     * @param handlerCls 标签处理器的 class
     * @param symbol SQL 操作符
     */
    public TagHandler(Class<? extends FenixHandler> handlerCls, String symbol) {
        this.prefix = Const.SPACE;
        this.handlerCls = handlerCls;
        this.symbol = symbol;
    }

    /**
     * 含标签处理器、SQL 操作符的构造方法.
     *
     * @param handlerFactory 标签处理器工厂
     * @param symbol SQL 操作符
     */
    public TagHandler(FenixHandlerFactory handlerFactory, String symbol) {
        this.prefix = Const.SPACE;
        this.handlerFactory = handlerFactory;
        this.symbol = symbol;
    }

    /**
     * 全构造方法.
     *
     * @param prefix SQL 片段前缀
     * @param symbol SQL 操作符
     * @param handlerCls 标签处理器的 class
     */
    public TagHandler(String prefix, Class<? extends FenixHandler> handlerCls, String symbol) {
        this.prefix = prefix;
        this.handlerCls = handlerCls;
        this.symbol = symbol;
    }

    /**
     * 含前缀、标签处理器、SQL 操作符的构造方法.
     *
     * @param prefix SQL 片段前缀
     * @param symbol SQL 操作符
     * @param handlerFactory 标签处理器工厂
     */
    public TagHandler(String prefix, FenixHandlerFactory handlerFactory, String symbol) {
        this.prefix = prefix;
        this.handlerFactory = handlerFactory;
        this.symbol = symbol;
    }

}
