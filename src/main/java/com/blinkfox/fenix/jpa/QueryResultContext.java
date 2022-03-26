package com.blinkfox.fenix.jpa;

import com.blinkfox.fenix.exception.FenixException;
import com.blinkfox.fenix.helper.StringHelper;
import com.blinkfox.fenix.jpa.transformer.ColumnAnnotationTransformer;
import com.blinkfox.fenix.jpa.transformer.PrefixUnderscoreTransformer;
import com.blinkfox.fenix.jpa.transformer.UnderscoreTransformer;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import javax.persistence.Query;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class QueryResultContext {

    /**
     * 这是 Fenix 内置的结果转换器 Map 集合，其中 key 是转换器的 class 全路径名，value 是转换器的 Supplier 表达式，便于动态创建对象.
     */
    private static final Map<String, Supplier<AbstractResultTransformer>> buildInTransformerMap = new HashMap<>(8);

    static {
        buildInTransformerMap.put(FenixResultTransformer.class.getName(), FenixResultTransformer::new);
        buildInTransformerMap.put(UnderscoreTransformer.class.getName(), UnderscoreTransformer::new);
        buildInTransformerMap.put(PrefixUnderscoreTransformer.class.getName(), PrefixUnderscoreTransformer::new);
        buildInTransformerMap.put(ColumnAnnotationTransformer.class.getName(), ColumnAnnotationTransformer::new);
    }

    /**
     * 根据是否是原生 SQL 查询来构造自定义类型的查询结果的 {@code Query} 实例.
     *
     * @param query 查询对象
     * @param resultTypeClassStr Fenix XML 文件中配置的结果类型字符串.
     * @param queryFenix Fenix 的查询注解
     * @return 额外改造后的 {@code Query} 实例.
     */
    @SuppressWarnings("deprecation")
    static Query buildTransformer(Query query, String resultTypeClassStr, QueryFenix queryFenix) {
        // 创建 ResultTransformer 的对象实例，然后设置结果类型的 class，并进行初始化设置.
        AbstractResultTransformer transformer = newTransformerInstance(queryFenix);
        transformer.setResultClass(getResultTypeClass(queryFenix.resultType(), resultTypeClassStr));
        transformer.init();

        // 根据是否原生 SQL 来包装并设置查询结果转换器对象.
        if (queryFenix.nativeQuery()) {
            // 获取该查询对应的 NativeQuery，设置转换类型.
            query.unwrap(NativeQuery.class).setResultTransformer(transformer);
        } else {
            query.unwrap(org.hibernate.query.Query.class).setResultTransformer(transformer);
        }
        return query;
    }

    /**
     * 创建查询结果转换器的对象实例.
     *
     * <p>如果检测出是 Fenix 内置的转换器就通过 new 来创建对象，提高性能；否则，才通过反射创建 ResultTransformer 对象.</p>
     *
     * @param queryFenix {@link QueryFenix} 的对象实例
     * @return 结果转换器对象实例
     */
    private static AbstractResultTransformer newTransformerInstance(QueryFenix queryFenix) {
        Class<? extends AbstractResultTransformer> transformer = queryFenix.resultTransformer();
        Supplier<AbstractResultTransformer> transformerSupplier = buildInTransformerMap.get(transformer.getName());
        if (transformerSupplier != null) {
            return transformerSupplier.get();
        }

        try {
            return transformer.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException
                | InvocationTargetException | NoSuchMethodException e) {
            throw new FenixException(StringHelper.format("【Fenix 异常】通过反射创建【{}】类的对象实例异常，请检查该类的"
                    + "构造方法是否有 public 的无参构造方法，建议你参考【com.blinkfox.fenix.jpa.FenixResultTransformer】类来实现"
                    + "自己的 ResultTransformer 类。", transformer.getName()), e);
        }
    }

    /**
     * 根据多个结果类型优先选取或得到一个可用的结果类型，如果 class 为空，就返回类型字符串所对应的 {@code Class} 实例.
     *
     * @param resultTypeClass 结果类型的 Class 对象
     * @param resultTypeClassStr 结果类型
     * @return {@code Class} 实例
     */
    private static Class<?> getResultTypeClass(Class<?> resultTypeClass, String resultTypeClassStr) {
        if (resultTypeClass != null && resultTypeClass != Void.class) {
            return resultTypeClass;
        }

        try {
            return Class.forName(resultTypeClassStr);
        } catch (ClassNotFoundException e) {
            throw new FenixException("【Fenix 异常】未找到【" + resultTypeClassStr + "】对应的 class，请检查！", e);
        }
    }

}
