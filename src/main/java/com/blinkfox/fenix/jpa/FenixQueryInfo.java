package com.blinkfox.fenix.jpa;

import com.blinkfox.fenix.bean.SqlInfo;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 * Fenix 查询的每个线程参数结果等信息的对象，用于隔离每个查询请求中的方法参数和结果等信息.
 *
 * @author blinkfox on 2020-07-03.
 * @since v2.3.3
 */
@Getter
@Setter
// TODO 是否需要手动 remove 待进一步验证.
public final class FenixQueryInfo {

    /**
     * 用作 {@code Fenix} 构建 SQL 信息的上下文参数.
     */
    private Map<String, Object> contextParams;

    /**
     * Fenix 构建出来的 SQL 信息.
     */
    private SqlInfo sqlInfo;

    /**
     * 用于拼接排序、分页等参数时的最终用于查询数据时的 JPQL 或者 SQL 语句.
     */
    private String querySql;

    /**
     * Fenix 每个查询请求的相关参数、SQL 信息等存放到 {@link ThreadLocal} 中.
     */
    private static final ThreadLocal<FenixQueryInfo> fenixThreadLocal = new ThreadLocal<>();

    /**
     * 获取 {@link FenixQueryInfo} 的实例.
     *
     * @return {@link FenixQueryInfo} 实例
     */
    public static FenixQueryInfo getInstance() {
        FenixQueryInfo fenixQueryInfo = fenixThreadLocal.get();
        if (fenixQueryInfo == null) {
            fenixQueryInfo = new FenixQueryInfo();
            fenixThreadLocal.set(fenixQueryInfo);
        }
        return fenixQueryInfo;
    }

}
