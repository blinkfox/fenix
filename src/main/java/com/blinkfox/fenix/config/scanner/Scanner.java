package com.blinkfox.fenix.config.scanner;

/**
 * Fenix 的扫描应用系统中各 XML、注解等文件的接口.
 *
 * @author blinkfox on 2019-08-04.
 * @see TaggerScanner
 * @see XmlScanner
 */
public interface Scanner {

    /**
     * 扫描指定路径下的相关文件(可以是目录，也可以是具体的文件)，并配置存储起来.
     *
     * @param locations 文件位置路径，可以是多个，用逗号隔开
     */
    void scan(String locations);

}