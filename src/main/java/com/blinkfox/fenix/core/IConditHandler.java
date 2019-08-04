package com.blinkfox.fenix.core;

import com.blinkfox.fenix.bean.BuildSource;
import com.blinkfox.fenix.bean.SqlInfo;

/**
 * 构建动态的 JPQL 或者 SQL 片段和参数的抽象接口.
 *
 * @author blinkfox on 2019-08-04.
 */
public interface IConditHandler {

    /**
     * 构建 SqlInfo 信息.
     *
     * @param source 构建所需的资源对象
     * @return 返回 SqlInfo 对象
     */
    SqlInfo buildSqlInfo(BuildSource source);

}
