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
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;

import lombok.Setter;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.AbstractJpaQuery;
import org.springframework.data.jpa.repository.query.JpaParameters;
import org.springframework.data.jpa.repository.query.JpaQueryMethod;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.data.repository.query.Parameter;
import org.springframework.data.repository.query.ParametersParameterAccessor;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.data.repository.query.ReturnedType;

/**
 * 继承了 {@link AbstractJpaQuery} 抽象类，
 * 并隐性实现了 {@link RepositoryQuery} 接口的 JPA 查询处理器类，
 * 该类主要用来处理标注了 {@link QueryFenix} 注解的 JPA 查询.
 *
 * @author blinkfox on 2019-08-04.
 */
public class FenixJpaQuery extends AbstractJpaQuery {

    /**
     * 用来替换 'select ... from' 为 'select count(*) from ' 的正则表达式.
     */
    private static final String REGX_SELECT_FROM = "((?i)select)([\\s\\S]*?)((?i)from)";

    /**
     * 用来替换 'select ... from' 为 'select count(*) from ' 的求 count(*) 的常量.
     */
    private static final String SELECT_COUNT = "select count(*) from ";

    /**
     * JPA 参数对象.
     */
    private JpaParameters jpaParams;

    /**
     * 标注了 {@link QueryFenix} 注解的注解实例.
     */
    @Setter
    private QueryFenix queryFenix;

    /**
     * 执行 {@link QueryFenix} 注解的执行的类 class.
     */
    @Setter
    private Class<?> queryClass;

    /**
     * 用作 {@link Fenix} 构建 SQL 信息的上下文参数.
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
     * Creates a new {@link AbstractJpaQuery} from the given {@link JpaQueryMethod}.
     *
     * @param method JpaQueryMethod
     * @param em EntityManager
     */
    FenixJpaQuery(JpaQueryMethod method, EntityManager em) {
        super(method, em);
    }

    /**
     * Creates a {@link Query} instance for the given values.
     *
     * @param values must not be {@literal null}.
     * @return Query
     */
    @Override
    protected Query doCreateQuery(Object[] values) {
        // 获取 QueryFenix 上下文参数，来从 XML 文件或 Java 中动态构建出 SQL 信息.
        this.jpaParams = getQueryMethod().getParameters();
        this.contextParams = this.buildContextParams(values);
        this.getSqlInfoByFenix();
        this.querySql = this.sqlInfo.getSql();

        // 判断是否有分页参数.如果有的话，就设置分页参数.
        final Pageable pageable = this.buildPagableAndSortSql(values);

        // 构建出 SQL 查询和相关的参数，区分是否是原生 SQL 的查询.
        Query query;
        EntityManager em = super.getEntityManager();
        if (queryFenix.nativeQuery()) {
            Class<?> type = this.getTypeToQueryFor(getQueryMethod().getResultProcessor().withDynamicProjection(
                    new ParametersParameterAccessor(getQueryMethod().getParameters(), values)).getReturnedType());
            query = type == null ? em.createNativeQuery(this.querySql) : em.createNativeQuery(this.querySql, type);
        } else {
            query = em.createQuery(this.querySql);
        }

        // 如果自定义设置的返回类型不为空，就做额外的返回结果处理.
        String resultType = this.sqlInfo.getResultType();
        if (StringHelper.isNotBlank(resultType)) {
            query = new QueryResultBuilder(query, resultType).build(queryFenix.nativeQuery());
        }

        // 循环设置命名绑定参数，且如果分页对象不为空，就设置分页参数.
        this.sqlInfo.getParams().forEach(query::setParameter);
        if (pageable != null) {
            query.setFirstResult((int) pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());
        }
        return query;
    }

    /**
     * 根据返回类型获取对应的 class.
     *
     * @param returnedType ReturnedType 实例
     * @return class
     */
    private Class<?> getTypeToQueryFor(ReturnedType returnedType) {
        Class<?> result = getQueryMethod().isQueryForEntity() ? returnedType.getDomainType() : null;

        // 如果 sql 中有构造器表达式或者投影，就直接返回该结果.
        if (QueryUtils.hasConstructorExpression(this.querySql)
                || QueryUtils.getProjection(this.querySql).equalsIgnoreCase(QueryHelper.detectAlias(this.querySql))) {
            return result;
        }

        return returnedType.isProjecting() && !getMetamodel().isJpaManaged(returnedType.getReturnedType())
                ? Tuple.class
                : result;
    }

    /**
     * 根据 JPA 参数和值的数组来分析构建出 JPQL 语句和参数的 Map 型上下文参数.
     *
     * @param values JPA 参数值
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
            Optional<String> nameOptional =  parameter.getName();
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
     */
    private void getSqlInfoByFenix() {
        // 在 QueryFenix 注解中 provider 不为空的情况下，
        // 如果 method 不为空，将直接反射调用该 provider 下的 method 方法；
        // 如果 method 为空，但 fullFenixId 不为空，则视为使用 XML 的方式来生成和构建 SqlInfo 信息.
        // 否则，两者皆为空时，则默认视为 provider 中存在与本查询方法相同方法名，直接使用该查询的方法名来进行执行.
        Class<?> provider = queryFenix.provider();
        String method = queryFenix.method();
        String fenixId = queryFenix.value();
        if (provider != Void.class) {
            if (StringHelper.isNotBlank(method)) {
                this.sqlInfo = ClassMethodInvoker.invoke(provider, method, this.contextParams);
            } else if (StringHelper.isNotBlank(fenixId)) {
                this.getXmlSqlInfo(fenixId);
            } else {
                this.sqlInfo = ClassMethodInvoker.invoke(provider, getQueryMethod().getName(), this.contextParams);
            }
            return;
        }

        // 如果 QueryFenix 注解中 value 不为空，即表明 fenixId 不为空，则说明是使用 XML 的方式来拼接 SQL 的.
        // 否则将执行类的全路径名和方法名来分别对应 XML 中的 namespace 和 fenixId 来对应进行查找，生成 SqlInfo 信息.
        if (StringHelper.isNotBlank(fenixId)) {
            this.getXmlSqlInfo(fenixId);
        } else {
            this.sqlInfo = Fenix.getXmlSqlInfo(queryClass.getName(), getQueryMethod().getName(), this.contextParams);
        }
    }

    /**
     * 根据 fenixId 来构建 SqlInfo 信息.
     *
     * <p>区分该 fenixId 是否有 '.' 号，如果有就分割 namespace 和 fenixId，
     *      否则就用查询方法所在的 class 全路径名来作为 namespace.</p>
     *
     * @param fenixId fenix XML 中的 id，可能包含 namespace.
     */
    private void getXmlSqlInfo(String fenixId) {
        if (fenixId.contains(Const.DOT)) {
            int i = fenixId.lastIndexOf(Const.DOT);
            this.sqlInfo = Fenix.getXmlSqlInfo(fenixId.substring(0, i), fenixId.substring(i + 1), this.contextParams);
        } else {
            this.sqlInfo = Fenix.getXmlSqlInfo(queryClass.getName(), fenixId, this.contextParams);
        }
    }

    /**
     * 继续构建 Spring Data JPA 分页和排序参数的SQL.
     *
     * @param values 参数数组
     */
    private Pageable buildPagableAndSortSql(Object[] values) {
        Pageable pageable = null;
        if (this.jpaParams.hasPageableParameter()) {
            pageable = (Pageable) (values[this.jpaParams.getPageableIndex()]);
            if (pageable != null) {
                this.querySql = QueryUtils.applySorting(this.querySql, pageable.getSort(),
                        QueryHelper.detectAlias(this.querySql));
            }
        }

        // 判断是否有排序参数，如果有，就追加排序相关的参数.
        if (this.jpaParams.hasSortParameter()) {
            this.querySql = QueryUtils.applySorting(this.querySql,
                    new ParametersParameterAccessor(this.jpaParams, values).getSort(),
                    QueryHelper.detectAlias(this.querySql));
        }
        return pageable;
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
     * @param values 参数数组
     * @return Query
     */
    @Override
    protected Query doCreateCountQuery(Object[] values) {
        // 如果计数查询的 SQL 不为空（区分 Java和 Xml 两者方式），就重新构建 SqlInfo 信息，
        // 否则就替换查询字符串中的字段值为 'count(*)'.
        String countSql = this.getCountSql();

        // 创建 Query，并循环设置命名绑定参数.
        EntityManager em = getEntityManager();
        Query query = this.queryFenix.nativeQuery()
                ? em.createNativeQuery(countSql)
                : em.createQuery(countSql, Long.class);
        this.sqlInfo.getParams().forEach(query::setParameter);

        return query;
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
        if (provider != Void.class) {
            if (StringHelper.isNotBlank(countMethod)) {
                this.sqlInfo = ClassMethodInvoker.invoke(provider, countMethod, this.contextParams);
                return this.sqlInfo.getSql();
            }
            if (StringHelper.isNotBlank(xmlCountQuery)) {
                this.getXmlSqlInfo(xmlCountQuery);
                return this.sqlInfo.getSql();
            } else {
                return this.sqlInfo.getSql().replaceFirst(REGX_SELECT_FROM, SELECT_COUNT);
            }
        }

        // 接下来则是查询 countQuery，得到新的 sqlInfo.
        // 如果没有 countQuery，则默认将之前的查询 SQL 结果替换修改成求 count(*) 的 SQL.
        if (StringHelper.isNotBlank(xmlCountQuery)) {
            this.getXmlSqlInfo(xmlCountQuery);
            return this.sqlInfo.getSql();
        }
        return this.sqlInfo.getSql().replaceFirst(REGX_SELECT_FROM, SELECT_COUNT);
    }

}
