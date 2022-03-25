package com.blinkfox.fenix.jpa;

import com.blinkfox.fenix.exception.FenixException;
import com.blinkfox.fenix.helper.StringHelper;
import java.lang.reflect.InvocationTargetException;
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
     * 根据是否是原生 SQL 查询来构造自定义类型的查询结果 {@code Query} 实例.
     *
     * @param query 查询对象
     * @param resultTypeClassStr Fenix XML 文件中配置的结果类型字符串.
     * @param queryFenix Fenix 的查询注解
     * @return 额外改造后的 {@code Query} 实例.
     */
    @SuppressWarnings("deprecation")
    static Query build(Query query, String resultTypeClassStr, QueryFenix queryFenix) {
        // 反射创建 ResultTransformer 的对象实例.
        Class<? extends AbstractResultTransformer> resultTransformerClass = queryFenix.resultTransformer();
        AbstractResultTransformer transformer;
        try {
            transformer = resultTransformerClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new FenixException(StringHelper.format("【Fenix 异常】通过反射创建【{}】类的对象实例异常，请检查该类的"
                    + "构造方法是否有 public 的无参构造方法，建议你参考【com.blinkfox.fenix.jpa.FenixResultTransformer】类来实现"
                    + "自己的 ResultTransformer 类。", resultTransformerClass.getName()), e);
        }

        // 设置结果类型的 class
        transformer.setResultClass(getResultTypeClass(resultTypeClassStr));

        if (queryFenix.nativeQuery()) {
            // 获取该查询对应的 NativeQuery，设置转换类型.
            query.unwrap(NativeQuery.class).setResultTransformer(transformer);
        } else {
            query.unwrap(org.hibernate.query.Query.class).setResultTransformer(transformer);
        }
        return query;
    }

    /**
     * 获取返回类型字符串所对应的 {@code Class} 实例.// TODO 待完成.
     *
     * @param resultTypeClassStr 结果类型
     * @return {@code Class} 实例
     */
    private static Class<?> getResultTypeClass(String resultTypeClassStr) {
        try {
            return Class.forName(resultTypeClassStr);
        } catch (ClassNotFoundException e) {
            throw new FenixException("【Fenix 异常】未找到【" + resultTypeClassStr + "】对应的 class，请检查！", e);
        }
    }

}
