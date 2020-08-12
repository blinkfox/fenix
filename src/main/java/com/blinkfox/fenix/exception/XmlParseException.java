package com.blinkfox.fenix.exception;

/**
 * XML 解析出错时需要抛出的异常.
 *
 * @author blinkfox on 2019-08-04.
 * @since v1.0.0
 */
public class XmlParseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 附带日志消息参数的构造方法.
     *
     * @param msg 日志消息
     * @param t Throwable对象
     */
    public XmlParseException(String msg, Throwable t) {
        super(msg, t);
    }

}
