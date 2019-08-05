package com.blinkfox.fenix.exception;

/**
 * 当判断的值不是集合或者数组时需要抛出的异常.
 *
 * @author blinkfox on 2019-08-04.
 */
public class NotCollectionOrArrayException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 附带日志消息参数的构造方法.
     *
     * @param msg 日志消息
     */
    public NotCollectionOrArrayException(String msg) {
        super(msg);
    }

}
