package com.blinkfox.fenix.jpa;

import com.blinkfox.fenix.bean.SqlInfo;
import com.blinkfox.fenix.consts.Const;
import com.blinkfox.fenix.core.Fenix;
import com.blinkfox.fenix.helper.ClassMethodInvoker;
import com.blinkfox.fenix.helper.QueryHelper;
import com.blinkfox.fenix.helper.StringHelper;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;
import lombok.Setter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.AbstractJpaQuery;
import org.springframework.data.jpa.repository.query.JpaParameters;
import org.springframework.data.jpa.repository.query.JpaParametersParameterAccessor;
import org.springframework.data.jpa.repository.query.JpaQueryMethod;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.data.repository.query.Parameter;
import org.springframework.data.repository.query.ParametersParameterAccessor;
import org.springframework.data.repository.query.ReturnedType;

/**
 * 继承了 {@code AbstractJpaQuery} 抽象类，
 * 并隐性实现了 {@code RepositoryQuery} 接口的 JPA 查询处理器类，
 * 该类主要用来处理标注了 {@link QueryFenix} 注解的 JPA 查询.
 *
 * <p>v2.0.0 修改记录：升级 Spring-data-jpa 的版本为 {@code 2.2.0.RELEASE} 之后，
 * 由于 {@code AbstractJpaQuery} 抽象类重写了 {@code doCreateQuery} 和 {@code doCreateCountQuery} 方法。
 * 所以，本 Fenix 库在 v2.0.0 也必须跟着重写这两个方法，才能正常使用. </p>
 *
 * @author blinkfox on 2019-08-04.
 * @since v1.0.0
 */
public class FenixJpaQuery extends AbstractJpaQuery {

    /**
     * 用来替换 'select ... from' 为 'select count(*) as count from ' 的正则表达式.
     */
    private static final String REGX_SELECT_FROM = "((?i)select)([\\s\\S]*?)((?i)from)";

    /**
     * 用来替换 'select ... from' 为 'select count(*) as count from ' 的求 count(*) 的常量.
     */
    private static final String SELECT_COUNT = "select count(*) as count from ";

    /**
     * 用来匹配 SQL 中的 DISTINCT 条件.
     */
    private static final String REGX_SELECT_FROM_DISTINCT =
            "((?i)select)([\\s\\S]*?)((?i)distinct)\\s+([\\s\\S]*?)((?i)from)";

    /**
     * 匹配 SQL 中别名的正则.
     */
    private static final String REGX_SQL_ALIAS = "\\s+((?i)as)\\s+[^,\\s]+";

    /**
     * select from 正则表达式 Pattern.
     */
    private static final Pattern SELECT_FROM_PATTERN = Pattern.compile(REGX_SELECT_FROM);

    /**
     * select distinct from 正则表达式 Pattern.
     */
    private static final Pattern SELECT_FROM_DISTINCT_PATTERN = Pattern.compile(REGX_SELECT_FROM_DISTINCT);

    /**
     * JPA 参数对象.
     */
    private JpaParameters jpaParams;

    /**
     * 标注了 {@code QueryFenix} 注解的注解实例.
     */
    @Setter
    private QueryFenix queryFenix;

    /**
     * 执行 {@code QueryFenix} 注解的执行的类 class.
     */
    @Setter
    private Class<?> queryClass;

    /**
     * Creates a new {@code AbstractJpaQuery} from the given {@code JpaQueryMethod}.
     *
     * @param method JpaQueryMethod
     * @param em EntityManager
     */
    FenixJpaQuery(JpaQueryMethod method, EntityManager em) {
        super(method, em);
    }

    /**
     * 基于 {@code JpaParametersParameterAccessor} 实例参数创建 {@code Query} 实例.
     *
     * @param accessor 访问器.
     * @return {@code Query} 实例
     * @since v2.0.0
     */
    @Override
    protected Query doCreateQuery(JpaParametersParameterAccessor accessor) {
        // 获取 QueryFenix 上下文参数，来从 XML 文件或 Java 中动态构建出 SQL 信息.
        return this.doCreateQuery(accessor.getValues());
    }

    /**
     * 原 {@code spring-data-jpa 2.1.x} 版本用来创建 {@link Query} 实例的方法.
     * 从 {@code spring-data-jpa 2.2.0} 版本开始抛弃了这个方法，我们这里目前仍然保留，便于兼容以前的 Fenix 版本.
     *
     * @param values must not be {@literal null}.
     * @return Query
     */
    protected Query doCreateQuery(Object[] values) {
        // 获取 QueryFenix 上下文参数，来从 XML 文件或 Java 中动态构建出 SQL 信息.
        JpaQueryMethod jpaMethod = super.getQueryMethod();
        this.jpaParams = jpaMethod.getParameters();

        // 构造和设置 FenixQueryInfo 所需的 SQL 和 参数信息.
        FenixQueryInfo fenixQueryInfo = FenixQueryInfo.getInstance();
        fenixQueryInfo.setContextParams(this.buildContextParams(values));
        SqlInfo sqlInfo = this.getSqlInfoByFenix();
        fenixQueryInfo.setSqlInfo(sqlInfo);
        fenixQueryInfo.setQuerySql(sqlInfo.getSql());

        // 判断是否有分页参数.如果有的话，就设置分页参数，并设置新的 querySql 的值.
        final Pageable pageable = this.buildPagableAndSortSql(values, fenixQueryInfo.getQuerySql());

        // 构建出 SQL 语句相关的 Query 实例，要区分是否是原生 SQL.
        Query query;
        EntityManager em = super.getEntityManager();
        String querySql = fenixQueryInfo.getQuerySql();
        if (queryFenix.nativeQuery()) {
            Class<?> type = this.getTypeToQueryFor(jpaMethod.getResultProcessor().withDynamicProjection(
                    new ParametersParameterAccessor(jpaMethod.getParameters(), values)).getReturnedType(), querySql);
            query = type == null ? em.createNativeQuery(querySql) : em.createNativeQuery(querySql, type);
        } else {
            query = em.createQuery(querySql);
        }

        // 循环设置命名绑定参数，且如果分页对象不为空，就设置分页参数.
        sqlInfo.getParams().forEach(query::setParameter);
        if (pageable != null && pageable.isPaged()) {
            query.setFirstResult((int) pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());
        }

        // 如果自定义设置的返回类型不为空，就做额外的返回结果处理.
        String resultType = sqlInfo.getResultType();
        if (StringHelper.isNotBlank(resultType)) {
            query = new QueryResultBuilder(query, resultType).build(queryFenix.nativeQuery());
        }

        // 如果分页参数为空，说明不需要再做分页查询，须要从 ThreadLocal 中移除当前线程中的 fenixQueryInfo 信息.
        if (pageable == null || pageable.isUnpaged()) {
            fenixQueryInfo.remove();
        }
        return query;
    }

    /**
     * 根据给定的参数访问对象创建一个 {@code Query} 对象，用于查询分页时的记录数.
     *
     * <p>这里要区分是否手动设置了 {@link QueryFenix#countQuery()} 和 {@link QueryFenix#countMethod()} 的值。</p>
     * <ul>
     *     <li>如果没有设置这个两个值，就默认使用先前的 SQL 来替换处理为查询条数的SQL；</li>
     *     <li>如果设置了这两个值，就依据优先级来构建新的 {@link SqlInfo} 对象，得到查询条数的 SQL；</li>
     * </ul>
     *
     * @param accessor JPA 参数访问对象
     * @return Query
     * @since v2.0.0
     */
    @Override
    protected Query doCreateCountQuery(JpaParametersParameterAccessor accessor) {
        return this.doCreateCountQuery(accessor.getValues());
    }

    /**
     * 根据给定的数组参数创建一个 {@link Query}，用于查询分页时的记录数.
     *
     * <p>这里要区分是否手动设置了 {@link QueryFenix#countQuery()} 和 {@link QueryFenix#countMethod()} 的值。</p>
     * <ul>
     *     <li>如果没有设置这个两个值，就默认使用先前的 SQL 来替换处理为查询条数的SQL；</li>
     *     <li>如果设置了这两个值，就依据优先级来构建新的 {@link SqlInfo} 对象，得到查询条数的 SQL；</li>
     * </ul>
     *
     * <p>注：原 {@code spring-data-jpa 2.1.x} 版本用来创建 {@link Query} 实例的方法.
     *     从 {@code spring-data-jpa 2.2.0} 版本开始抛弃了这个方法，我们这里目前仍然保留，便于兼容以前的 Fenix 版本.</p>
     *
     * @param values 参数数组
     * @return Query
     */
    protected Query doCreateCountQuery(Object[] values) {
        // 如果计数查询的 SQL 不为空（区分 Java和 Xml 两者方式），就重新构建 SqlInfo 信息，
        // 否则就替换查询字符串中的字段值为 'count(*) as count'.
        String countSql = this.getCountSql();

        // 创建 Query，并循环设置命名绑定参数，并返回 Query 实例.
        EntityManager em = getEntityManager();
        Query query = this.queryFenix.nativeQuery()
                ? em.createNativeQuery(countSql)
                : em.createQuery(countSql, Long.class);
        FenixQueryInfo.getInstance().getSqlInfo().getParams().forEach(query::setParameter);

        // 从 ThreadLocal 中移除当前线程中的 FenixQueryInfo 对象.
        FenixQueryInfo fenixQueryInfo = FenixQueryInfo.getLocalThreadInstance();
        if (fenixQueryInfo != null) {
            fenixQueryInfo.remove();
        }
        return query;
    }

    /**
     * 根据返回类型获取对应的 class.
     *
     * @param returnedType ReturnedType 实例
     * @param querySql 要执行的 SQL 字符串
     * @return class
     */
    private Class<?> getTypeToQueryFor(ReturnedType returnedType, String querySql) {
        Class<?> result = getQueryMethod().isQueryForEntity() ? returnedType.getDomainType() : null;

        // 如果 sql 中有构造器表达式或者投影，就直接返回该结果.
        if (QueryUtils.hasConstructorExpression(querySql)
                || QueryUtils.getProjection(querySql).equalsIgnoreCase(QueryHelper.detectAlias(querySql))) {
            return result;
        }

        return returnedType.isProjecting() && !getMetamodel().isJpaManaged(returnedType.getReturnedType())
                ? Tuple.class
                : result;
    }

    /**
     * 根据 JPA 参数和值的数组来分析构建出 JPQL 语句和参数的 Map 型上下文参数.
     *
     * @param values JPA 的有序数组参数
     * @return Map
     */
    private Map<String, Object> buildContextParams(Object[] values) {
        int len = this.jpaParams.getNumberOfParameters();
        Map<String, Object> context = new HashMap<>(len);
        for (int i = 0; i < len; i++) {
            Parameter parameter = this.jpaParams.getParameter(i);
            if (parameter.isSpecialParameter()) {
                continue;
            }

            // 获取参数
            Optional<String> nameOptional = parameter.getName();
            if (nameOptional.isPresent()) {
                context.put(nameOptional.get(), values[i]);
            }
        }
        return context;
    }

    /**
     * 根据 {@link QueryFenix} 注解来生成并获取 {@link SqlInfo} 信息.
     *
     * <p>并区分判断 Java 或者 XML 两种方式来构建 {@link SqlInfo} 信息，基于约定优于配置的方式来查找对应的生成 SqlInfo 的方式.</p>
     *
     * @return {@link SqlInfo} 对象实例
     */
    private SqlInfo getSqlInfoByFenix() {
        // 在 QueryFenix 注解中 provider 不为空的情况下，
        // 如果 method 不为空，将直接反射调用该 provider 下的 method 方法；
        // 如果 method 为空，但 fullFenixId 不为空，则视为使用 XML 的方式来生成和构建 SqlInfo 信息.
        // 否则，两者皆为空时，则默认视为 provider 中存在与本查询方法相同方法名，直接使用该查询的方法名来进行执行.
        Class<?> provider = queryFenix.provider();
        String method = queryFenix.method();
        String fenixId = queryFenix.value();
        Map<String, Object> contextParams = FenixQueryInfo.getInstance().getContextParams();
        if (provider != Void.class) {
            if (StringHelper.isNotBlank(method)) {
                return ClassMethodInvoker.invoke(provider, method, contextParams);
            } else if (StringHelper.isNotBlank(fenixId)) {
                return this.getXmlSqlInfo(fenixId, contextParams);
            } else {
                return ClassMethodInvoker.invoke(provider, getQueryMethod().getName(), contextParams);
            }
        }

        // 如果 QueryFenix 注解中 value 不为空，即表明 fenixId 不为空，则说明是使用 XML 的方式来拼接 SQL 的.
        // 否则将执行类的全路径名和方法名来分别对应 XML 中的 namespace 和 fenixId 来对应进行查找，生成 SqlInfo 信息.
        return StringHelper.isNotBlank(fenixId)
                ? this.getXmlSqlInfo(fenixId, contextParams)
                : Fenix.getXmlSqlInfo(queryClass.getName(), getQueryMethod().getName(), contextParams);
    }

    /**
     * 根据 fenixId 来构建 SqlInfo 信息.
     *
     * <p>区分该 fenixId 是否有 '.' 号，如果有就分割 namespace 和 fenixId，
     * 否则就用查询方法所在的 class 全路径名来作为 namespace.</p>
     *
     * @param fenixId fenix XML 中的 id，可能包含 namespace.
     * @param contextParams 上下文参数.
     * @return {@link SqlInfo} 信息.
     */
    private SqlInfo getXmlSqlInfo(String fenixId, Map<String, Object> contextParams) {
        if (fenixId.contains(Const.DOT)) {
            int i = fenixId.lastIndexOf(Const.DOT);
            return Fenix.getXmlSqlInfo(fenixId.substring(0, i), fenixId.substring(i + 1), contextParams);
        } else {
            return Fenix.getXmlSqlInfo(queryClass.getName(), fenixId, contextParams);
        }
    }

    /**
     * 继续构建 Spring Data JPA 分页和排序参数的SQL.
     *
     * @param values 参数数组
     * @param querySql 执行的 SQL 字符串
     * @return 分页参数信息
     */
    private Pageable buildPagableAndSortSql(Object[] values, String querySql) {
        Pageable pageable = null;
        FenixQueryInfo fenixQueryInfo = FenixQueryInfo.getInstance();
        if (this.jpaParams.hasPageableParameter()) {
            pageable = (Pageable) (values[this.jpaParams.getPageableIndex()]);
            if (pageable != null) {
                fenixQueryInfo.setQuerySql(
                        QueryUtils.applySorting(querySql, pageable.getSort(), QueryHelper.detectAlias(querySql)));
            }
        }

        // 判断是否有排序参数，如果有，就追加排序相关的参数.
        if (this.jpaParams.hasSortParameter()) {
            fenixQueryInfo.setQuerySql(QueryUtils.applySorting(querySql,
                    new ParametersParameterAccessor(this.jpaParams, values).getSort(),
                    QueryHelper.detectAlias(querySql)));
        }
        return pageable;
    }

    /**
     * 获取总记录数查询的 JPQL 或者 SQL 语句.
     *
     * @return 总记录数 SQL 语句
     */
    private String getCountSql() {
        // 在 QueryFenix 注解中 provider 不为空的情况下，优先查询 countMethod，其次是 countQuery，得到新的 sqlInfo.
        // 如果两者都没有，则默认将之前的查询 SQL 结果替换修改成求 count(*) 的 SQL.
        Class<?> provider = queryFenix.provider();
        String xmlCountQuery = queryFenix.countQuery();
        String countMethod = queryFenix.countMethod();
        FenixQueryInfo fenixQueryInfo = FenixQueryInfo.getInstance();
        Map<String, Object> contextParams = fenixQueryInfo.getContextParams();
        if (provider != Void.class) {
            if (StringHelper.isNotBlank(countMethod)) {
                fenixQueryInfo.setSqlInfo(ClassMethodInvoker.invoke(provider, countMethod, contextParams));
                return fenixQueryInfo.getSqlInfo().getSql();
            }
            if (StringHelper.isNotBlank(xmlCountQuery)) {
                fenixQueryInfo.setSqlInfo(this.getXmlSqlInfo(xmlCountQuery, contextParams));
                return fenixQueryInfo.getSqlInfo().getSql();
            } else {
                return getCountSqlByQueryInfo(fenixQueryInfo);
            }
        }

        // 接下来则是查询 countQuery，得到新的 sqlInfo.
        // 如果没有 countQuery，则默认将之前的查询 SQL 结果替换修改成求 count(*) 的 SQL.
        if (StringHelper.isNotBlank(xmlCountQuery)) {
            fenixQueryInfo.setSqlInfo(this.getXmlSqlInfo(xmlCountQuery, contextParams));
            return fenixQueryInfo.getSqlInfo().getSql();
        }
        return getCountSqlByQueryInfo(fenixQueryInfo);
    }

    /**
     * 通过QueryInfo获取CountSql.
     *
     * @param fenixQueryInfo {@link FenixQueryInfo}
     * @return countSql
     */
    private String getCountSqlByQueryInfo(FenixQueryInfo fenixQueryInfo) {
        boolean enableDistinct = queryFenix.enableDistinct();
        String infoSql = fenixQueryInfo.getSqlInfo().getSql();
        Matcher matcher = SELECT_FROM_PATTERN.matcher(infoSql);
        String countSql = matcher.replaceFirst(SELECT_COUNT);
        if (!enableDistinct) {
            return countSql;
        }
        String selectPrefix = matcher.group();
        matcher = SELECT_FROM_DISTINCT_PATTERN.matcher(selectPrefix);
        if (!matcher.find()) {
            return countSql;
        }
        String distinctColumn = matcher.group(4).replaceAll(REGX_SQL_ALIAS, "");
        return countSql.replaceFirst("count\\(\\*\\)", String.format("count(distinct %s)", distinctColumn));
    }

}
