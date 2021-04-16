package com.blinkfox.fenix.exception;

/**
 * Fenix XML 标签中当字段为空时抛出的异常.
 *
 * @author blinkfox on 2019-08-05.
 * @since v1.0.0
 */
public class FieldEmptyException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 附带日志消息参数的构造方法.
     *
     * @param msg 日志消息
     */
    public FieldEmptyException(String msg) {
        super(msg);
    }

}
