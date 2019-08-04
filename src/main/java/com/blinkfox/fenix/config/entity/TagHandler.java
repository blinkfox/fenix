package com.blinkfox.fenix.config.entity;

import com.blinkfox.fenix.consts.Const;
import com.blinkfox.fenix.core.IConditHandler;

import lombok.Getter;

/**
 * XML 标签对应的动态 SQL 生成的处理类.
 *
 * @author blinkfox on 2019-08-04.
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
     * 生成动态 SQL 片段的后缀，如: '>', '=', 'LIKE' 等.
     */
    @Getter
    private String symbol;

    /**
     * 仅标签对应处理类的构造方法.
     *
     * @param handlerCls 动态处理类的反射类型
     */
    public TagHandler(Class<? extends IConditHandler> handlerCls) {
        this.prefix = Const.ONE_SPACE;
        this.handlerCls = handlerCls;
    }

    /**
     * 含前缀、标签处理器的构造方法.
     *
     * @param prefix sql前缀
     * @param handlerCls 动态处理类的反射类型
     */
    public TagHandler(String prefix, Class<? extends IConditHandler> handlerCls) {
        this.prefix = prefix;
        this.handlerCls = handlerCls;
    }

    /**
     * 含标签处理器、后缀的构造方法.
     *
     * @param handlerCls 动态处理类的反射类型
     * @param symbol sql前后缀
     */
    public TagHandler(Class<? extends IConditHandler> handlerCls, String symbol) {
        this.prefix = Const.ONE_SPACE;
        this.handlerCls = handlerCls;
        this.symbol = symbol;
    }

    /**
     * 全构造方法.
     *
     * @param prefix sql前缀
     * @param symbol sql后缀
     * @param handlerCls 动态处理类的反射类型
     */
    public TagHandler(String prefix, Class<? extends IConditHandler> handlerCls, String symbol) {
        this.prefix = prefix;
        this.handlerCls = handlerCls;
        this.symbol = symbol;
    }

}