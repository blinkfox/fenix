package com.blinkfox.fenix.loader;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 加载 zealot banner 加载器类.
 * @author blinkfox on 2017/4/21.
 */
@Slf4j
@NoArgsConstructor
public class BannerLoader {
    
    /** fenix 的 banner 文本. */
    private static final String BANNER_TEXT = "\n"
            + "__________               .__           __\n"
            + "\\____    / ____  _____   |  |    _____/  |_\n"
            + "  /     /_/ __ \\ \\__  \\  |  |   /  _ \\   __\\\n"
            + " /     /_\\  ___/  / __ \\_|  |__(  <_> )  |\n"
            + "/_______ \\\\___  >(____  /|____/ \\____/|__|\n"
            + "        \\/    \\/      \\/\n";

    /**
     * 是否打印Banner信息，如果参数为true,则打印否则不打印.
     *
     * @param isPrint 是否打印
     */
    public void print(boolean isPrint) {
        if (isPrint) {
            log.warn(BANNER_TEXT);
        }
    }

}