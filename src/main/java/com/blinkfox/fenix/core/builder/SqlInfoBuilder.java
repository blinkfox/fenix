package com.blinkfox.fenix.core.builder;

import com.blinkfox.fenix.bean.BuildSource;
import com.blinkfox.fenix.bean.SqlInfo;
import com.blinkfox.fenix.consts.Const;
import com.blinkfox.fenix.consts.LikeTypeEnum;
import com.blinkfox.fenix.consts.SymbolConst;
import com.blinkfox.fenix.helper.StringHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
    protected SqlInfo sqlInfo;

    /**
     * 上下文参数（一般是 Bean 或者 map）.
     */
    protected Object context;

    /**
     * 生成的 SQL 片段的前缀.
     */
    protected String prefix;

    /**
     * SQL 标记操作符.
     */
    protected String symbol;

    /**
     * 其它数据.
     *
     * <p>注：通常情况下这个值是 NULL，如果某些情况下，你需要传递额外的参数值，可以通过这个属性来传递，
     *      是为了方便传递或处理数据而设计的.</p>
     */
    protected Map<String, Object> others;

    /**
     * 私有构造方法.
     *
     * @param source 构建资源参数
     */
    public SqlInfoBuilder(BuildSource source) {
        this.sqlInfo = source.getSqlInfo();
        this.context = source.getContext();
        this.prefix = source.getPrefix();
        this.symbol = source.getSymbol();
        this.others = source.getOthers();
    }

    /**
     * 为了生成 JPQL 语法中 " :text " 这种冒号式的命名参数，需要将 "." 点号替换为 "_"，Spring DATA JPA 才支持.
     *
     * <p>如：JPQL 语句片段 " b.title = :blog.title "，会将 "blog.title" 替换为 "blog_title".</p>
     *
     * @param text 待替换的文本
     * @return 替换后的文本
     */
    private String fixDot(String text) {
        return text.contains(Const.DOT) ? text.replace(Const.DOT, Const.UNDERLINE) : text;
    }

    /**
     * 追加构建常规 SQL 片段的 {@link SqlInfo} 信息.
     * <p>如：'u.id = :id'.</p>
     *
     * @param fieldText JPQL 或者 SQL 语句的字段的文本.
     * @param valueText 待解析 value 的文本值
     * @param value 解析后的表达式的值
     */
    public void buildNormalSql(String fieldText, String valueText, Object value) {
        String namedText = this.fixDot(valueText);
        sqlInfo.getJoin().append(this.prefix).append(fieldText)
                .append(this.symbol).append(Const.COLON).append(namedText);
        sqlInfo.getParams().put(namedText, value);
    }

    /**
     * 追加构建 LIKE 模糊查询 SQL 片段的 {@link SqlInfo} 信息.
     * <p>如：'u.id LIKE :id'.</p>
     *
     * @param fieldText 数据库字段的文本
     * @param valueText 待解析 value 的文本值
     * @param value 参数值
     */
    public void buildLikeSql(String fieldText, String valueText, Object value) {
        String namedText = this.fixDot(valueText);
        sqlInfo.getJoin().append(this.prefix).append(fieldText)
                .append(StringHelper.isBlank(this.symbol) ? SymbolConst.LIKE : this.symbol)
                .append(Const.COLON).append(namedText);

        // 如果 others 参数为空，说明是前后模糊的情况.
        if (this.others == null || this.others.size() == 0) {
            sqlInfo.getParams().put(namedText, "%" + value + "%");
            return;
        }

        // 如果 others 参数不为空，获取对应的类型设置参数.
        LikeTypeEnum likeTypeEnum = (LikeTypeEnum) this.others.get(Const.TYPE);
        if (likeTypeEnum == LikeTypeEnum.STARTS_WITH) {
            sqlInfo.getParams().put(namedText, value + "%");
        } else if (likeTypeEnum == LikeTypeEnum.ENDS_WITH) {
            sqlInfo.getParams().put(namedText, "%" + value);
        }
    }

    /**
     * 根据指定的模式 `pattern` 来追加构建 LIKE 模糊查询 SQL 片段的 {@link SqlInfo} 信息.
     *
     * @param fieldText 数据库字段的文本
     * @param pattern LIKE 匹配的模式
     */
    public void buildLikePatternSql(String fieldText, String pattern) {
        sqlInfo.getJoin().append(prefix).append(fieldText)
                .append(StringHelper.isBlank(this.symbol) ? SymbolConst.LIKE : this.symbol)
                .append(Const.QUOTE).append(pattern).append(Const.QUOTE);
    }

    /**
     * 追加构建区间查询 SQL 片段的 {@link SqlInfo} 信息.
     *
     * <p>根据开始文本和结束文本是否为空来判断执行是大于、小于还是区间的查询 JPQL 和参数的生成.</p>
     *
     * @param fieldText 字段文本
     * @param startText 开始文本
     * @param startValue 解析的开始值
     * @param endText 结束文本
     * @param endValue 解析的结束值
     */
    public void buildBetweenSql(String fieldText, String startText, Object startValue,
            String endText, Object endValue) {
        // 开始值不为空，结束值为空时，转为"大于"的情况.
        if (startValue != null && endValue == null) {
            String startNamed = this.fixDot(startText);
            sqlInfo.getJoin().append(prefix).append(fieldText).append(SymbolConst.GTE)
                    .append(Const.COLON).append(startNamed);
            sqlInfo.getParams().put(startNamed, startValue);
        } else if (startValue == null && endValue != null) {
            // 开始值为空，结束值不为空时，转为"小于"的情况.
            String endNamed = this.fixDot(endText);
            sqlInfo.getJoin().append(prefix).append(fieldText).append(SymbolConst.LTE)
                    .append(Const.COLON).append(endNamed);
            sqlInfo.getParams().put(endNamed, endValue);
        } else {
            // 开始值不为空，结束值也不为空时，转为"BETWEEN ? AND ?"的情况.
            String startNamed = this.fixDot(startText);
            String endNamed = this.fixDot(endText);
            sqlInfo.getJoin().append(prefix).append(fieldText)
                    .append(SymbolConst.BETWEEN).append(Const.COLON).append(startNamed)
                    .append(SymbolConst.AND).append(Const.COLON).append(endNamed);
            Map<String, Object> params = sqlInfo.getParams();
            params.put(startNamed, startValue);
            params.put(endNamed, endValue);
        }
    }

    /**
     * 追加构建 'IN' 范围查询 SQL 片段的 {@link SqlInfo} 信息.
     *
     * @param fieldText 字段文本
     * @param valueText IN 属性的值文本
     * @param obj IN 查询范围的值，如果不是集合或数组，就将单个的值包装数组
     */
    public void buildInSql(String fieldText, String valueText, Object obj) {
        String endNamed = this.fixDot(valueText);
        sqlInfo.getJoin().append(prefix).append(fieldText).append(this.symbol)
                .append(Const.COLON).append(endNamed);

        // 封装 IN 查询的参数，如果解析到的值是一个数组，需要转换成 List 集合，不然 JPA 执行会报错，如果只有单个元素就包装成 List 集合.
        if (obj instanceof Collection) {
            sqlInfo.getParams().put(endNamed, obj);
        } else if (obj.getClass().isArray()) {
            sqlInfo.getParams().put(endNamed, Arrays.asList((Object[]) obj));
        } else {
            // 如果只有一个元素就创建一个 List 集合.
            List<Object> lists = new ArrayList<>(2);
            lists.add(obj);
            sqlInfo.getParams().put(endNamed, lists);
        }
    }

    /**
     * 追加构建 ' IS NULL ' 和 ' IS NOT NULL ' SQL 片段的 {@link SqlInfo} 信息.
     *
     * @param fieldText 字段文本
     */
    public void buildIsNullSql(String fieldText) {
        sqlInfo.getJoin().append(prefix).append(fieldText).append(this.symbol);
    }

}
