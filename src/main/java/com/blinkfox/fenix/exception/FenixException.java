package com.blinkfox.fenix.exception;

import com.blinkfox.fenix.helper.StringHelper;

/**
 * 自定义的 Fenix 运行时抛出的异常.
 *
 * @author blinkfox on 2019-08-04.
 * @since v1.0.0
 */
public class FenixException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 附带日志消息参数的构造方法.
     *
     * @param msg 日志消息
     */
    public FenixException(String msg) {
        super(msg);
    }

    /**
     * 附带日志消息参数的构造方法.
     *
     * @param msg 日志消息
     * @param t Throwable对象
     */
    public FenixException(String msg, Throwable t) {
        super(msg, t);
    }

    /**
     * 附带日志消息参数的构造方法.
     *
     * @param t Throwable对象
     * @param msg 日志消息
     * @param args 任意不定参数
     * @since v2.7.0
     */
    public FenixException(Throwable t, String msg, Object... args) {
        super(StringHelper.format(msg, args), t);
    }

}
