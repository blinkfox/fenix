package com.blinkfox.fenix.provider;

import com.blinkfox.fenix.bean.SqlInfo;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 这是用来测试异常调用情况下的类.
 *
 * @author blinkfox on 2019-09-01.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SqlExceptionProvider {

    /**
     * 构建 SQL.
     *
     * @return SqlInfo
     */
    public SqlInfo build() {
        return new SqlInfo();
    }

}
