package com.blinkfox.fenix.config.entity;

import com.blinkfox.fenix.consts.Const;
import com.blinkfox.fenix.core.IConditHandler;

import lombok.Getter;

/**
 * XML 标签对应的动态 SQL 片段生成的处理类.
 *
 * @author blinkfox on 2019-08-04.
 * @see IConditHandler
 */
public class TagHandler {

    /**
     * 生成 SQL 片段的前缀，如: 'and', 'or' 等.
     * */
    @Getter
    private String prefix;

    /**
     * 生成动态 SQL 片段的处理实现反射类型，如: 'EqualHandler'.
     */
    @Getter
    private Class<? extends IConditHandler> handlerCls;

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
    public TagHandler(Class<? extends IConditHandler> handlerCls) {
        this.prefix = Const.ONE_SPACE;
        this.handlerCls = handlerCls;
    }

    /**
     * 含 SQL 片段前缀和标签处理器 class 的构造方法.
     *
     * @param prefix SQL 前缀
     * @param handlerCls 标签处理器的 class
     */
    public TagHandler(String prefix, Class<? extends IConditHandler> handlerCls) {
        this.prefix = prefix;
        this.handlerCls = handlerCls;
    }

    /**
     * 含标签处理器、SQL 操作符的构造方法.
     *
     * @param handlerCls 标签处理器的 class
     * @param symbol SQL 操作符
     */
    public TagHandler(Class<? extends IConditHandler> handlerCls, String symbol) {
        this.prefix = Const.ONE_SPACE;
        this.handlerCls = handlerCls;
        this.symbol = symbol;
    }

    /**
     * 全构造方法.
     *
     * @param prefix SQL 片段前缀
     * @param symbol SQL 操作符
     * @param handlerCls 标签处理器的 class
     */
    public TagHandler(String prefix, Class<? extends IConditHandler> handlerCls, String symbol) {
        this.prefix = prefix;
        this.handlerCls = handlerCls;
        this.symbol = symbol;
    }

}
