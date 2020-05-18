package com.blinkfox.fenix.exception;

/**
 * 无法解析表达式时需要抛出的运行时异常.
 *
 * @author blinkfox on 2019-08-04.
 * @since v1.0.0
 */
public class ParseExpressionException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 附带日志消息和异常信息组成的构造方法.
     *
     * @param msg 日志消息
     * @param t Throwable对象
     */
    public ParseExpressionException(String msg, Throwable t) {
        super(msg, t);
    }

}
