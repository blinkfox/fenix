package com.blinkfox.fenix.exception;

/**
 * Fenix XML 标签节点未找到时抛出的异常.
 *
 * @author blinkfox on 2019-08-04.
 */
public class NodeNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 附带日志消息参数的构造方法.
     *
     * @param msg 日志消息
     */
    public NodeNotFoundException(String msg) {
        super(msg);
    }

}
