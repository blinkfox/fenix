package com.blinkfox.fenix.core.builder;

import com.blinkfox.fenix.bean.BuildSource;
import com.blinkfox.fenix.bean.SqlInfo;
import com.blinkfox.fenix.consts.Const;
import com.blinkfox.fenix.helper.StringHelper;

import java.util.Map;

/**
 * 构建拼接sql语句片段和参数的类.
 * @author blinkfox on 2017-03-31.
 */
public class SqlInfoBuilder {

    /**
     * {@link SqlInfo} 对象.
     */
    SqlInfo sqlInfo;

    /**
     * SQL 拼接器，{@link SqlInfo} 对象的属性.
     */
    private StringBuilder join;

    /**
     * JPQL 或者 SQL 语句对应的 Map 参数.
     */
    private Map<String, Object> params;

    /**
     * 上下文参数.
     */
    Object context;

    /**
     * 前缀.
     */
    private String prefix;

    /**
     * SQL 标记操作符.
     */
    private String symbol;

    /**
     * 私有构造方法.
     */
    SqlInfoBuilder() {
        super();
    }

    /**
     * 获取 SqlInfoBuilder 的实例，并初始化属性信息.
     *
     * @param source BuildSource实例
     * @return XmlSqlInfoBuilder实例
     */
    public static SqlInfoBuilder newInstace(BuildSource source) {
        SqlInfoBuilder builder = new SqlInfoBuilder();
        builder.init(source);
        return builder;
    }

    /**
     * 根据构建的资源参数初始化数据.
     *
     * @param source 构建sql的相关参数
     */
    void init(BuildSource source) {
        this.sqlInfo = source.getSqlInfo();
        this.join = sqlInfo.getJoin();
        this.params = sqlInfo.getParams();
        this.context = source.getParamMap();
        this.prefix = source.getPrefix();
        this.symbol = source.getSymbol();
    }

    /**
     * 构建普通查询需要的SqlInfo信息.
     * @param fieldText 数据库字段的文本
     * @param value 参数值
     * @param suffix 后缀，如：大于、等于、小于等
     * @return sqlInfo
     */
    public SqlInfo buildNormalSql(String fieldText, Object value, String suffix) {
        join.append(prefix).append(fieldText).append(suffix);
        params.add(value);
        return sqlInfo.setJoin(join).setParams(params);
    }

    /**
     * 构建like模糊查询需要的SqlInfo信息.
     * @param fieldText 数据库字段的文本
     * @param value 参数值
     * @return sqlInfo
     */
    public SqlInfo buildLikeSql(String fieldText, Object value) {
        // 由于默认配置的suffix的值只是" LIKE "和" NOT LIKE "两个关键字，生成的LIKE SQL片段需要加上" ? "占位符.
        this.suffix = StringHelper.isBlank(this.suffix) ? Const.LIKE_KEY : this.suffix;
        join.append(prefix).append(fieldText).append(suffix).append("? ");
        params.add("%" + value + "%");
        return sqlInfo.setJoin(join).setParams(params);
    }

    /**
     * 根据指定的模式`pattern`来构建like模糊查询需要的SqlInfo信息.
     * @param fieldText 数据库字段的文本
     * @param pattern like匹配的模式
     * @return sqlInfo
     */
    public SqlInfo buildLikePatternSql(String fieldText, String pattern) {
        this.suffix = StringHelper.isBlank(this.suffix) ? Const.LIKE_KEY : this.suffix;
        join.append(prefix).append(fieldText).append(this.suffix).append("'").append(pattern).append("' ");
        return sqlInfo.setJoin(join);
    }

    /**
     * 构建区间查询的SqlInfo信息.
     * @param fieldText 数据库字段文本
     * @param startValue 参数开始值
     * @param endValue 参数结束值
     * @return 返回SqlInfo信息
     */
    public SqlInfo buildBetweenSql(String fieldText, Object startValue, Object endValue) {
        /* 根据开始文本和结束文本判断执行是大于、小于还是区间的查询sql和参数的生成 */
        if (startValue != null && endValue == null) {
            join.append(prefix).append(fieldText).append(Const.GTE_SUFFIX);
            params.add(startValue);
        } else if (startValue == null && endValue != null) {
            join.append(prefix).append(fieldText).append(Const.LTE_SUFFIX);
            params.add(endValue);
        } else {
            join.append(prefix).append(fieldText).append(Const.BT_AND_SUFFIX);
            params.add(startValue);
            params.add(endValue);
        }

        return sqlInfo.setJoin(join).setParams(params);
    }

    /**
     * 构建" IN "范围查询的SqlInfo信息.
     * @param fieldText 数据库字段文本
     * @param values 对象数组的值
     * @return 返回SqlInfo信息
     */
    public SqlInfo buildInSql(String fieldText, Object[] values) {
        if (values == null || values.length == 0) {
            return sqlInfo;
        }

        // 遍历数组，并遍历添加in查询的替换符和参数
        this.symbol = StringHelper.isBlank(this.symbol) ? Const.IN_SUFFIX : this.symbol;
        join.append(prefix).append(fieldText).append(this.symbol).append("(");
        int len = values.length;
        for (int i = 0; i < len; i++) {
            if (i == len - 1) {
                join.append("?) ");
            } else {
                join.append("?, ");
            }
            params.add(values[i]);
        }

        return sqlInfo.setJoin(join).setParams(params);
    }

    /**
     * 构建" IS NULL "和" IS NOT NULL "需要的SqlInfo信息.
     * @param fieldText 数据库字段的文本
     * @return SqlInfo信息
     */
    public SqlInfo buildIsNullSql(String fieldText) {
        this.suffix = StringHelper.isBlank(this.suffix) ? Const.IS_NULL_SUFFIX : this.suffix;
        join.append(prefix).append(fieldText).append(this.suffix);
        return sqlInfo.setJoin(join);
    }

}