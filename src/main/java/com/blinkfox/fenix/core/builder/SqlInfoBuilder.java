package com.blinkfox.fenix.core.builder;

import com.blinkfox.fenix.bean.BuildSource;
import com.blinkfox.fenix.bean.SqlInfo;
import com.blinkfox.fenix.consts.Const;
import com.blinkfox.fenix.helper.StringHelper;

/**
 * 构建拼接 JPQL 或者 SQL 语句片段和参数的构建器类.
 *
 * @author blinkfox on 2019-08-06.
 * @see XmlSqlInfoBuilder
 */
public class SqlInfoBuilder {

    /**
     * {@link SqlInfo} 对象.
     */
    private SqlInfo sqlInfo;

    /**
     * 上下文参数（一般是 Bean 或者 map）.
     */
    Object context;

    /**
     * 生成的 SQL 片段的前缀.
     */
    private String prefix;

    /**
     * SQL 标记操作符.
     */
    private String symbol;

    /**
     * 私有构造方法.
     *
     * @param source 构建资源参数
     */
    SqlInfoBuilder(BuildSource source) {
        this.sqlInfo = source.getSqlInfo();
        this.context = source.getContext();
        this.prefix = source.getPrefix();
        this.symbol = source.getSymbol();
    }

    /**
     * 构建常规 SQL 片段需要的 {@link SqlInfo} 信息.
     * <p>如：'u.id = :u_id'.</p>
     *
     * @param fieldText JPQL 或者 SQL 语句的字段的文本.
     * @param value 解析后的表达式的值
     * @param symbol SQL 标记或者操作符，如：" = "、" > "、" <= "等.
     */
    void buildNormalSql(String fieldText, Object value, String symbol) {
        String namedField = fieldText.replace(Const.DOT, Const.UNDERLINE);
        sqlInfo.getJoin().append(prefix).append(fieldText).append(symbol).append(Const.COLON).append(namedField);
        sqlInfo.getParams().put(namedField, value);
    }

    /**
     * 构建like模糊查询需要的SqlInfo信息.
     * @param fieldText 数据库字段的文本
     * @param value 参数值
     */
    public void buildLikeSql(String fieldText, Object value) {
        // 由于默认配置的suffix的值只是" LIKE "和" NOT LIKE "两个关键字，生成的LIKE SQL片段需要加上" ? "占位符.
//        this.suffix = StringHelper.isBlank(this.suffix) ? Const.LIKE_KEY : this.suffix;
//        join.append(prefix).append(fieldText).append(suffix).append("? ");
//        params.add("%" + value + "%");
    }

    /**
     * 根据指定的模式`pattern`来构建like模糊查询需要的SqlInfo信息.
     * @param fieldText 数据库字段的文本
     * @param pattern like匹配的模式
     * @return sqlInfo
     */
    public void buildLikePatternSql(String fieldText, String pattern) {
//        this.suffix = StringHelper.isBlank(this.suffix) ? Const.LIKE_KEY : this.suffix;
//        join.append(prefix).append(fieldText).append(this.suffix).append("'").append(pattern).append("' ");
//        return sqlInfo.setJoin(join);
    }

    /**
     * 构建区间查询的SqlInfo信息.
     * @param fieldText 数据库字段文本
     * @param startValue 参数开始值
     * @param endValue 参数结束值
     * @return 返回SqlInfo信息
     */
    public void buildBetweenSql(String fieldText, Object startValue, Object endValue) {
        /* 根据开始文本和结束文本判断执行是大于、小于还是区间的查询sql和参数的生成 */
//        if (startValue != null && endValue == null) {
//            join.append(prefix).append(fieldText).append(Const.GTE_SUFFIX);
//            params.add(startValue);
//        } else if (startValue == null && endValue != null) {
//            join.append(prefix).append(fieldText).append(Const.LTE_SUFFIX);
//            params.add(endValue);
//        } else {
//            join.append(prefix).append(fieldText).append(Const.BT_AND_SUFFIX);
//            params.add(startValue);
//            params.add(endValue);
//        }
    }

    /**
     * 构建" IN "范围查询的SqlInfo信息.
     * @param fieldText 数据库字段文本
     * @param values 对象数组的值
     * @return 返回SqlInfo信息
     */
    public void buildInSql(String fieldText, Object[] values) {
//        if (values == null || values.length == 0) {
//            return sqlInfo;
//        }
//
//        // 遍历数组，并遍历添加in查询的替换符和参数
//        this.symbol = StringHelper.isBlank(this.symbol) ? Const.IN_SUFFIX : this.symbol;
//        join.append(prefix).append(fieldText).append(this.symbol).append("(");
//        int len = values.length;
//        for (int i = 0; i < len; i++) {
//            if (i == len - 1) {
//                join.append("?) ");
//            } else {
//                join.append("?, ");
//            }
//            params.add(values[i]);
//        }
    }

    /**
     * 构建" IS NULL "和" IS NOT NULL "需要的SqlInfo信息.
     * @param fieldText 数据库字段的文本
     * @return SqlInfo信息
     */
    public void buildIsNullSql(String fieldText) {
//        this.suffix = StringHelper.isBlank(this.suffix) ? Const.IS_NULL_SUFFIX : this.suffix;
//        join.append(prefix).append(fieldText).append(this.suffix);
    }

}
