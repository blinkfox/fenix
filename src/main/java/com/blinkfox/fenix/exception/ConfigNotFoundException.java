package com.blinkfox.fenix.exception;

/**
 * Fenix 配置未找到时抛出的异常.
 *
 * @author blinkfox on 2019-08-04.
 */
public class ConfigNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 附带日志消息组成的构造方法.
     *
     * @param msg 日志消息
     */
    public ConfigNotFoundException(String msg) {
        super(msg);
    }

}
