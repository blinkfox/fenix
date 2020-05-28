package com.blinkfox.fenix.jpa;

import com.blinkfox.fenix.exception.FenixException;
import com.blinkfox.fenix.helper.ProxyHelper;
import javax.persistence.Query;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.ResultTransformer;

/**
 * 构建 {@link Query} 对象实例的自定义返回结果的构建器类.
 *
 * <p>注：构造的 Query 要区分是原生 SQL 还是 JPQL，
 * 通过设置 {@link ResultTransformer} 来达到自定义返回结果的目的.</p>
 *
 * @author blinkfox on 2019-10-09.
 * @see FenixJpaQuery
 * @see org.hibernate.query.Query
 * @see NativeQuery
 * @since v1.1.0
 */
final class QueryResultBuilder {

    /**
     * 查询的 {@code Query} 实例.
     */
    private final Query query;

    /**
     * 返回结果类型的字符串.
     */
    private final String resultType;

    /**
     * 基于返回结果类型字符串的构造方法.
     *
     * @param resultType 返回结果类型的字符串
     */
    QueryResultBuilder(Query query, String resultType) {
        this.query = query;
        this.resultType = resultType;
    }

    /**
     * 根据是否是原生 SQL 查询来构造自定义类型的查询结果 {@code Query} 实例.
     *
     * @param isNative 是否原生 SQL
     * @return 额外改造后的 {@code Query} 实例.
     */
    @SuppressWarnings({"deprecation", "rawtypes"})
    Query build(boolean isNative) {
        ResultTransformer resultTransformer = new FenixResultTransformer<>(this.getResultTypeClass());
        if (isNative) {
            // 获取该查询对应的 NativeQuery，设置转换类型.
            NativeQuery<?> nativeQuery = ProxyHelper.getTarget(this.query);
            nativeQuery.setResultTransformer(resultTransformer);
            return this.query;
        } else {
            org.hibernate.query.Query hibernateQuery = ProxyHelper.getTarget(this.query);
            hibernateQuery.setResultTransformer(resultTransformer);
            return this.query;
        }
    }

    /**
     * 获取返回类型字符串所对应的 {@code Class} 实例.
     *
     * @return {@code Class} 实例
     */
    private Class<?> getResultTypeClass() {
        try {
            return Class.forName(this.resultType);
        } catch (ClassNotFoundException e) {
            throw new FenixException("未找到【" + resultType + "】对应的 class，请检查！", e);
        }
    }

}
