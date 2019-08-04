package com.blinkfox.fenix.helper;

import java.io.Closeable;
import java.io.IOException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 输入输出相关的工具类.
 *
 * @author blinkfox on 2019-08-04.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class IoHelper {

    /**
     * “安静”的关闭资源.
     *
     * @param closeable reader实例
     */
    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException excepted) {
                // 忽略该异常，不打印异常堆栈信息.
                log.error("关闭 Reader 实例出错！", excepted.getMessage());
            }
        }
    }

}