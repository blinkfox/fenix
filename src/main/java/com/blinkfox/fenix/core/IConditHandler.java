package com.blinkfox.fenix.core;

import com.blinkfox.fenix.bean.BuildSource;
import com.blinkfox.fenix.bean.SqlInfo;

/**
 * 构建动态的 JPQL 或者 SQL 片段及参数的 {@link SqlInfo} 信息接口.
 *
 * @author blinkfox on 2019-08-04.
 */
public interface IConditHandler {

    /**
     * 根据构建资源的相关参数来构建 {@link SqlInfo} 信息.
     *
     * @param source 构建所需的 {@link BuildSource} 资源对象
     * @return 返回 {@link SqlInfo} 对象
     */
    SqlInfo buildSqlInfo(BuildSource source);

}
