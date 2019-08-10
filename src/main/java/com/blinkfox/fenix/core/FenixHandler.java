package com.blinkfox.fenix.core;

import com.blinkfox.fenix.bean.BuildSource;

/**
 * 根据构建资源来动态追加构建 JPQL 或者 SQL 片段及参数信息的处理器接口.
 *
 * @author blinkfox on 2019-08-04.
 */
@FunctionalInterface
public interface FenixHandler {

    /**
     * 根据 {@link BuildSource} 的相关参数来追加构建出对应 XML 标签的 JPQL 语句及参数信息.
     *
     * @param source 构建所需的 {@link BuildSource} 资源对象
     */
    void buildSqlInfo(BuildSource source);

}
