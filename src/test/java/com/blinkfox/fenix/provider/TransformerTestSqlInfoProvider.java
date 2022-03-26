package com.blinkfox.fenix.provider;

import com.blinkfox.fenix.bean.SqlInfo;
import com.blinkfox.fenix.core.Fenix;
import org.springframework.data.repository.query.Param;

/**
 * 使用 Java 代码来拼接用户动态 SQL 的 Provider 类.
 *
 * @author blinkfox on 2022-03-26.
 * @since v2.7.0
 */
public final class TransformerTestSqlInfoProvider {

    /**
     * 使用 Java 拼接 SQL 的方式来生成的 SQL 信息.
     *
     * @param num 数字
     * @return SQL 信息
     */
    public SqlInfo queryPrefixUnderscoreVoResultType(@Param("num") int num) {
        return Fenix.start()
                .select("c_id, c_name, c_second_name, n_integer_column, n_long_column, "
                        + "d_create_time, dt_last_update_time")
                .from("t_prefix_underscore")
                .where()
                .lessThan("n_integer_column", num)
                .end();
    }

}
