package com.blinkfox.fenix.jpa;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.query.AbstractJpaQuery;
import org.springframework.data.jpa.repository.query.JpaParameters;
import org.springframework.data.jpa.repository.query.JpaQueryMethod;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.data.repository.query.Parameter;
import org.springframework.data.repository.query.ParameterAccessor;
import org.springframework.data.repository.query.ParametersParameterAccessor;
import org.springframework.data.repository.query.RepositoryQuery;

/**
 * 继承了 {@link AbstractJpaQuery} 抽象类，
 * 并隐性实现了 {@link RepositoryQuery} 接口的 JPA 查询处理器类，
 * 该类主要用来处理标注了 {@link QueryFenix} 注解的 JPA 查询.
 *
 * @author blinkfox on 2019-08-04.
 */
@Slf4j
public class FenixJpaQuery extends AbstractJpaQuery {

    /**
     * 标注了 {@link QueryFenix} 注解的注解实例.
     */
    private QueryFenix queryFenix;

    /**
     * Creates a new {@link AbstractJpaQuery} from the given {@link JpaQueryMethod}.
     *
     * @param method JpaQueryMethod
     * @param em EntityManager
     */
    FenixJpaQuery(JpaQueryMethod method, EntityManager em, QueryFenix queryFenix) {
        super(method, em);
        this.queryFenix = queryFenix;
    }

    /**
     * Creates a {@link Query} instance for the given values.
     *
     * @param values must not be {@literal null}.
     * @return Query
     */
    @Override
    protected Query doCreateQuery(Object[] values) {
        // 分析出 SQL 查询的参数
        Map<String, Object> paramMap = new LinkedHashMap<>();
        JpaParameters parameters = getQueryMethod().getParameters();
        for (int i = 0; i < parameters.getNumberOfParameters(); i++) {
            Parameter parameter = parameters.getParameter(i);
            if (parameter.isSpecialParameter()) {
                continue;
            }

            log.info("isExplicitlyNamed: 【{}】", parameter.isExplicitlyNamed());
            paramMap.put(parameter.getName().get(), values[i]);
            log.info("param name:【{}】, type:【{}】，index:【{}】，placeholder:【{}】", parameter.getName(),
                    parameter.getType(), parameter.getIndex(), parameter.getPlaceholder());
        }

//        String namespace = myQuery.namespace();
//        String zealotId = myQuery.zealotId();
//        log.info("namespace:【{}】, zealotId:【{}】", namespace, zealotId);
//
//        SqlInfo sqlInfo = Zealot.getSqlInfo(namespace, zealotId, paramMap);
//        String sql = sqlInfo.getSql();
//        Map<String, Object> sqlParamMap = sqlInfo.getParamMap();
//        log.info("zealot 解析后的新 sql 字符串: 【{}】, 参数：【{}】", sql, sqlParamMap);
        Map<String, Object> sqlParamMap = null;
        String sql = "";

        // 判断是否有分页参数.如果有的话，就设置分页参数.
        Pageable pageable = null;
        if (parameters.hasPageableParameter()) {
            pageable = (Pageable) (values[parameters.getPageableIndex()]);
            if (pageable != null) {
                Sort sort = pageable.getSort();
                if (sort != null) {
                    sql = QueryUtils.applySorting(sql, sort, QueryUtils.detectAlias(sql));
                }
            }
        }

        // 判断是否有排序参数，如果有，就追加排序相关的参数.
        if (parameters.hasSortParameter()) {
            ParameterAccessor accessor = new ParametersParameterAccessor(parameters, values);
            sql = QueryUtils.applySorting(sql, accessor.getSort(), QueryUtils.detectAlias(sql));
        }

        EntityManager em = super.getEntityManager();
        Query query = em.createQuery(sql);
        for (Map.Entry<String, Object> entry : sqlParamMap.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        // 设置分页参数.
        if (pageable != null) {
            query.setFirstResult((int) pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());
        }

        return query;
    }

    /**
     * Creates a TypedQuery for counting using the given values.
     *
     * @param values must not be {@literal null}.
     * @return Query
     */
    @Override
    protected Query doCreateCountQuery(Object[] values) {
        return null;
    }

}
