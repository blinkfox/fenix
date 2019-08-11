package com.blinkfox.fenix.core;

import com.blinkfox.fenix.bean.BuildSource;
import com.blinkfox.fenix.bean.SqlInfo;
import com.blinkfox.fenix.consts.Const;
import com.blinkfox.fenix.consts.SqlKeyConst;
import com.blinkfox.fenix.consts.SymbolConst;
import com.blinkfox.fenix.core.builder.SqlInfoBuilder;
import com.blinkfox.fenix.helper.CollectionHelper;
import com.blinkfox.fenix.helper.SqlInfoPrinter;
import com.blinkfox.fenix.helper.StringHelper;

import java.util.Collection;

/**
 * 使用 Java 链式写法来拼接 JPQL 或者 SQL 语句和对应命名参数的 {@link com.blinkfox.fenix.bean.SqlInfo} 信息的类.
 *
 * @author blinkfox on 2019-08-11.
 */
public final class FenixKhala {

    /**
     * 封装了 {@link SqlInfo}、应用中提供的上下文参数、前缀等信息.
     *
     * <p>注：由于这里是纯Java拼接,所以就没有xml的Node节点信息，初始为为null.</p>
     */
    private BuildSource source;

    /**
     * 私有构造方法，构造时就初始化 {@link BuildSource} 相应的参数信息.
     */
    private FenixKhala() {
        this.source = new BuildSource(new SqlInfo());
    }

    /**
     * 开始的方法，初始化 {@link FenixKhala} 实例.
     *
     * @return {@link FenixKhala} 实例
     */
    public static FenixKhala start() {
        return new FenixKhala();
    }

    /**
     * 结束JPQL 或者 SQL 的拼接流程，并生成最终的 {@link SqlInfo} 信息.
     *
     * @return sqlInfo
     */
    public SqlInfo end() {
        SqlInfo sqlInfo = this.source.getSqlInfo();
        sqlInfo.setSql(StringHelper.replaceBlank(sqlInfo.getJoin().toString()));
        new SqlInfoPrinter().print(sqlInfo);
        return sqlInfo;
    }

    /**
     * 连接字符串.
     *
     * @param sqlKey SQL 关键字
     * @param params 其他若干字符串参数
     */
    private FenixKhala concat(String sqlKey, String... params) {
        this.source.getSqlInfo().getJoin().append(SqlKeyConst.SPACE).append(sqlKey).append(SqlKeyConst.SPACE);
        if (params != null && params.length > 0) {
            for (String param: params) {
                this.source.getSqlInfo().getJoin().append(param).append(SqlKeyConst.SPACE);
            }
        }
        return this;
    }

    /**
     * 拼接并带上 'INSERT INTO' 关键字的字符串.
     * 
     * @param text 文本
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala insertInto(String text) {
        return this.concat(SqlKeyConst.INSERT_INTO, text);
    }

    /**
     * 拼接并带上 'VALUES' 关键字的字符串.
     *
     * @param text 文本
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala values(String text) {
        return this.concat(SqlKeyConst.VALUES, text);
    }

    /**
     * 拼接并带上 'DELETE FROM' 关键字的字符串.
     *
     * @param text 文本
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala deleteFrom(String text) {
        return this.concat(SqlKeyConst.DELETE_FROM, text);
    }

    /**
     * 拼接并带上 'UPDATE' 关键字的字符串.
     *
     * @param text 文本
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala update(String text) {
        return this.concat(SqlKeyConst.UPDATE, text);
    }

    /**
     * 拼接并带上 'SELECT' 关键字的字符串.
     *
     * @param text 文本
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala select(String text) {
        return this.concat(SqlKeyConst.SELECT, text);
    }

    /**
     * 拼接并带上 'FROM' 关键字的字符串.
     *
     * @param text 文本
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala from(String text) {
        return this.concat(SqlKeyConst.FROM, text);
    }

    /**
     * 拼接并带上 'WHERE' 关键字的字符串和动态参数.
     *
     * @param text 文本
     * @param value 不定个数的参数值
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala where(String text, Object... value) {
        this.concat(SqlKeyConst.WHERE, text);
        return this.param(value);
    }

    /**
     * 拼接并带上 'AND' 关键字的字符串.
     *
     * @param text 文本
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala and(String text) {
        return this.concat(SqlKeyConst.AND, text);
    }

    /**
     * 拼接并带上 'OR' 关键字的字符串.
     *
     * @param text 文本
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala or(String text) {
        return this.concat(SqlKeyConst.OR, text);
    }

    /**
     * 拼接并带上 'AS' 关键字的字符串.
     *
     * @param text 文本
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala as(String text) {
        return this.concat(SqlKeyConst.AS, text);
    }

    /**
     * 拼接并带上 'AS' 关键字的字符串.
     *
     * @param text 文本
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala set(String text) {
        return this.concat(SqlKeyConst.SET, text);
    }

    /**
     * 拼接并带上 'INNER JOIN' 关键字的字符串.
     *
     * @param text 文本
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala innerJoin(String text) {
        return this.concat(SqlKeyConst.INNER_JOIN, text);
    }

    /**
     * 拼接并带上 'LEFT JOIN' 关键字的字符串.
     *
     * @param text 文本
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala leftJoin(String text) {
        return this.concat(SqlKeyConst.LEFT_JOIN, text);
    }

    /**
     * 拼接并带上 'RIGHT JOIN' 关键字的字符串.
     *
     * @param text 文本
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala rightJoin(String text) {
        return this.concat(SqlKeyConst.RIGHT_JOIN, text);
    }

    /**
     * 拼接并带上 'FULL JOIN' 关键字的字符串.
     *
     * @param text 文本
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala fullJoin(String text) {
        return this.concat(SqlKeyConst.FULL_JOIN, text);
    }

    /**
     * 拼接并带上 'ON' 关键字的字符串.
     *
     * @param text 文本
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala on(String text) {
        return this.concat(SqlKeyConst.ON, text);
    }

    /**
     * 拼接并带上 'ORDER BY' 关键字的字符串.
     *
     * @param text 文本
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala orderBy(String text) {
        return this.concat(SqlKeyConst.ORDER_BY, text);
    }

    /**
     * 拼接并带上 'GROUP BY' 关键字的字符串.
     *
     * @param text 文本
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala groupBy(String text) {
        return this.concat(SqlKeyConst.GROUP_BY, text);
    }

    /**
     * 拼接并带上 'HAVING' 关键字的字符串.
     *
     * @param text 文本
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala having(String text) {
        return this.concat(SqlKeyConst.HAVING, text);
    }

    /**
     * 拼接并带上 'LIMIT' 关键字的字符串.
     *
     * @param text 文本
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala limit(String text) {
        return this.concat(SqlKeyConst.LIMIT, text);
    }

    /**
     * 拼接并带上 'OFFSET' 关键字的字符串.
     *
     * @param text 文本
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala offset(String text) {
        return this.concat(SqlKeyConst.OFFSET, text);
    }

    /**
     * 拼接并带上 'ASC' 关键字的字符串.
     *
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala asc() {
        return this.concat(SqlKeyConst.ASC);
    }

    /**
     * 拼接并带上 'DESC' 关键字的字符串.
     *
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala desc() {
        return this.concat(SqlKeyConst.DESC);
    }

    /**
     * 拼接并带上 'UNION' 关键字的字符串.
     *
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala union() {
        return this.concat(SqlKeyConst.UNION);
    }

    /**
     * 拼接并带上 'UNION ALL' 关键字的字符串.
     *
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala unionAll() {
        return this.concat(SqlKeyConst.UNION_ALL);
    }

    /**
     * 在 SQL 后追加任何文本字符串，后可追加自定义可变参数.
     *
     * @param text 文本
     * @param values 可变参数数组
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala text(String text, Object... values) {
        this.source.getSqlInfo().getJoin().append(text);
        this.appendParams(values, Const.OBJTYPE_ARRAY);
        return this;
    }

    /**
     * 在 SQL 后追加任何文本字符串，后可追加自定义可变参数，如果 match 为 true 时，才生成此 SQL 文本和参数.
     *
     * @param match 匹配条件
     * @param text 文本
     * @param values 可变参数数组
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala text(boolean match, String text, Object... values) {
        return match ? text(text, values) : this;
    }

    /**
     * 在 SQL 的参数集合后追加任何的数组.
     *
     * @param value 值
     * @param objType 对象类型那
     * @return {@link FenixKhala} 实例
     */
    private FenixKhala appendParams(Object value, int objType) {
        Object[] values = CollectionHelper.toArray(value, objType);
        if (CollectionHelper.isNotEmpty(values)) {
            // TODO 之类待修改和完善.
            //Collections.addAll(this.source.getSqlInfo().getParams(), values);
        }
        return this;
    }

    /**
     * 在 SQL 的参数集合后追加不定对象个数的数组.
     *
     * @param values 不定个数的值，也是数组
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala param(Object... values) {
        return this.appendParams(values, Const.OBJTYPE_ARRAY);
    }

    /**
     * 在 SQL 的参数集合后追加任何的一个集合.
     *
     * @param values 不定个数的值
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala param(Collection<?> values) {
        return this.appendParams(values, Const.OBJTYPE_COLLECTION);
    }

    /**
     * 基于 {@link FenixAction} 的函数式接口来执行任意的自定义操作.
     *
     * @param action {@link FenixAction} 的接口实现类实例
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala doAnything(FenixAction action) {
        SqlInfo sqlInfo = this.source.getSqlInfo();
        action.execute(sqlInfo.getJoin(), sqlInfo.getParams());
        return this;
    }

    /**
     * 当匹配 match 条件为 true 时，才执行自定义的任意操作.
     *
     * @param match 匹配条件
     * @param action {@link FenixAction} 的接口实现类实例
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala doAny(boolean match, FenixAction action) {
        return match ? this.doAnything(action) : this;
    }

    /**
     * 执行生成等值查询 JPQL 或 SQL 片段的方法.
     *
     * @param prefix 前缀
     * @param field 数据库字段
     * @param value 值
     * @param symbol 关键操作符
     * @param match 是否匹配
     * @return {@link FenixKhala} 实例的当前实例
     */
    private FenixKhala doNormal(String prefix, String field, Object value, String symbol, boolean match) {
        if (match) {
            new SqlInfoBuilder(this.source.setPrefix(prefix)).buildNormalSql(field, value, symbol);
            this.source.resetPrefix();
        }
        return this;
    }

    /**
     * 执行生成 LIKE 模糊查询的 SQL 片段的方法.
     *
     * @param prefix 前缀
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @param positive true则表示是like，否则是not like
     * @return {@link FenixKhala} 实例的当前实例
     */
    private FenixKhala doLike(String prefix, String field, Object value, boolean match, boolean positive) {
        if (match) {
            // TODO 这里还有问题.
            new SqlInfoBuilder(this.source.setPrefix(prefix)
                    .setSymbol(positive ? SymbolConst.LIKE : SymbolConst.NOT_LIKE))
                    //.buildLikeSql(field, value);
                    ;
            this.source.resetPrefix();
        }
        return this;
    }

    /**
     * 执行根据传入模式来生成like匹配SQL片段的方法.
     * @param prefix 前缀
     * @param field 数据库字段
     * @param pattern 值
     * @param match 是否匹配
     * @param positive true则表示是like，否则是not like
     * @return {@link FenixKhala} 实例的当前实例
     */
    private FenixKhala doLikePattern(String prefix, String field, String pattern, boolean match, boolean positive) {
        if (match) {
            new SqlInfoBuilder(this.source.setPrefix(prefix)
                    .setSymbol(positive ? SymbolConst.LIKE : SymbolConst.NOT_LIKE))
                    .buildLikePatternSql(field, pattern);
            this.source.resetPrefix();
        }
        return this;
    }

    /**
     * 执行生成like模糊查询SQL片段的方法.
     * @param prefix 前缀
     * @param field 数据库字段
     * @param startValue 值
     * @param endValue 值
     * @param match 是否匹配
     * @return {@link FenixKhala} 实例的当前实例
     */
    private FenixKhala doBetween(String prefix, String field, Object startValue, Object endValue, boolean match) {
        if (match) {
            new SqlInfoBuilder(this.source.setPrefix(prefix))
                    //.buildBetweenSql(field, startValue, endValue)
                    ;
            this.source.resetPrefix();
        }
        return this;
    }

    /**
     * 执行生成in范围查询SQL片段的方法,如果是集合或数组，则执行生成，否则抛出异常.
     * @param prefix 前缀
     * @param field 数据库字段
     * @param value 数组的值
     * @param match 是否匹配
     * @param objType 对象类型，取自ZealotConst.java中以OBJTYPE开头的类型
     * @param positive true则表示是in，否则是not in
     * @return {@link FenixKhala} 实例的当前实例
     */
    @SuppressWarnings("unchecked")
    private FenixKhala doInByType(String prefix, String field, Object value,
                                  boolean match, int objType, boolean positive) {
        if (match) {
            // 赋予source对象in SQL片段的前缀和后缀操作符.
            this.source.setPrefix(prefix).setSuffix(positive ? ZealotConst.IN_SUFFIX : ZealotConst.NOT_IN_SUFFIX);
            // 根据对象类型调用对应的生成in查询的sql片段方法,否则抛出类型不符合的异常
            switch (objType) {
                // 如果类型是数组.
                case ZealotConst.OBJTYPE_ARRAY:
                    SqlInfoBuilder.newInstace(source).buildInSql(field, (Object[]) value);
                    break;
                // 如果类型是Java集合.
                case ZealotConst.OBJTYPE_COLLECTION:
                    JavaSqlInfoBuilder.newInstace(source).buildInSqlByCollection(field, (Collection<Object>) value);
                    break;
                default:
                    throw new NotCollectionOrArrayException("in查询的值不是有效的集合或数组!");
            }
            this.source.resetPrefix();
        }
        return this;
    }

    /**
     * 执行生成in范围查询SQL片段的方法.
     * @param prefix 前缀
     * @param field 数据库字段
     * @param values 数组的值
     * @param match 是否匹配
     * @param positive true则表示是in，否则是not in
     * @return {@link FenixKhala} 实例的当前实例
     */
    private FenixKhala doIn(String prefix, String field, Object[] values, boolean match, boolean positive) {
        return this.doInByType(prefix, field, values, match, ZealotConst.OBJTYPE_ARRAY, positive);
    }

    /**
     * 执行生成in范围查询SQL片段的方法.
     * @param prefix 前缀
     * @param field 数据库字段
     * @param values 集合的值
     * @param match 是否匹配
     * @param positive true则表示是in，否则是not in
     * @return {@link FenixKhala} 实例的当前实例
     */
    private FenixKhala doIn(String prefix, String field, Collection<?> values, boolean match, boolean positive) {
        return this.doInByType(prefix, field, values, match, ZealotConst.OBJTYPE_COLLECTION, positive);
    }

    /**
     * 执行生成" IS NULL "SQL片段的方法.
     * @param prefix 前缀
     * @param field 数据库字段
     * @param match 是否匹配
     * @param positive true则表示是 IS NULL，否则是IS NOT NULL
     * @return {@link FenixKhala} 实例的当前实例
     */
    private FenixKhala doIsNull(String prefix, String field, boolean match, boolean positive) {
        if (match) {
            // 判断是"IS NULL"还是"IS NOT NULL"来设置source实例.
            this.source = this.source.setPrefix(prefix)
                    .setSuffix(positive ? ZealotConst.IS_NULL_SUFFIX : ZealotConst.IS_NOT_NULL_SUFFIX);
            SqlInfoBuilder.newInstace(this.source).buildIsNullSql(field);
            this.source.resetPrefix();
        }
        return this;
    }

    /**
     * 生成等值查询的SQL片段.
     * @param field 数据库字段
     * @param value 值
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala equal(String field, Object value) {
        return this.doNormal(ZealotConst.ONE_SPACE, field, value, ZealotConst.EQUAL_SUFFIX,true);
    }

    /**
     * 生成等值查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala equal(String field, Object value, boolean match) {
        return this.doNormal(ZealotConst.ONE_SPACE, field, value, ZealotConst.EQUAL_SUFFIX, match);
    }

    /**
     * 生成带" AND "前缀等值查询的SQL片段.
     * @param field 数据库字段
     * @param value 值
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala andEqual(String field, Object value) {
        return this.doNormal(ZealotConst.AND_PREFIX, field, value, ZealotConst.EQUAL_SUFFIX, true);
    }

    /**
     * 生成带" AND "前缀等值查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala andEqual(String field, Object value, boolean match) {
        return this.doNormal(ZealotConst.AND_PREFIX, field, value, ZealotConst.EQUAL_SUFFIX, match);
    }

    /**
     * 生成带" OR "前缀等值查询的SQL片段.
     * @param field 数据库字段
     * @param value 值
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala orEqual(String field, Object value) {
        return this.doNormal(ZealotConst.OR_PREFIX, field, value, ZealotConst.EQUAL_SUFFIX, true);
    }

    /**
     * 生成带" OR "前缀等值查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala orEqual(String field, Object value, boolean match) {
        return this.doNormal(ZealotConst.OR_PREFIX, field, value, ZealotConst.EQUAL_SUFFIX, match);
    }

    /**
     * 生成不等查询的SQL片段.
     * @param field 数据库字段
     * @param value 值
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala notEqual(String field, Object value) {
        return this.doNormal(ZealotConst.ONE_SPACE, field, value, ZealotConst.NOT_EQUAL_SUFFIX,true);
    }

    /**
     * 生成不等查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala notEqual(String field, Object value, boolean match) {
        return this.doNormal(ZealotConst.ONE_SPACE, field, value, ZealotConst.NOT_EQUAL_SUFFIX, match);
    }

    /**
     * 生成带" AND "前缀不等查询的SQL片段.
     * @param field 数据库字段
     * @param value 值
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala andNotEqual(String field, Object value) {
        return this.doNormal(ZealotConst.AND_PREFIX, field, value, ZealotConst.NOT_EQUAL_SUFFIX, true);
    }

    /**
     * 生成带" AND "前缀不等查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala andNotEqual(String field, Object value, boolean match) {
        return this.doNormal(ZealotConst.AND_PREFIX, field, value, ZealotConst.NOT_EQUAL_SUFFIX, match);
    }

    /**
     * 生成带" OR "前缀不等查询的SQL片段.
     * @param field 数据库字段
     * @param value 值
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala orNotEqual(String field, Object value) {
        return this.doNormal(ZealotConst.OR_PREFIX, field, value, ZealotConst.NOT_EQUAL_SUFFIX, true);
    }

    /**
     * 生成带" OR "前缀不等查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala orNotEqual(String field, Object value, boolean match) {
        return this.doNormal(ZealotConst.OR_PREFIX, field, value, ZealotConst.NOT_EQUAL_SUFFIX, match);
    }

    /**
     * 生成大于查询的SQL片段.
     * @param field 数据库字段
     * @param value 值
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala moreThan(String field, Object value) {
        return this.doNormal(ZealotConst.ONE_SPACE, field, value, ZealotConst.GT_SUFFIX, true);
    }

    /**
     * 生成大于查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala moreThan(String field, Object value, boolean match) {
        return this.doNormal(ZealotConst.ONE_SPACE, field, value, ZealotConst.GT_SUFFIX, match);
    }

    /**
     * 生成带" AND "前缀大于查询的SQL片段.
     * @param field 数据库字段
     * @param value 值
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala andMoreThan(String field, Object value) {
        return this.doNormal(ZealotConst.AND_PREFIX, field, value, ZealotConst.GT_SUFFIX, true);
    }

    /**
     * 生成带" AND "前缀大于查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala andMoreThan(String field, Object value, boolean match) {
        return this.doNormal(ZealotConst.AND_PREFIX, field, value, ZealotConst.GT_SUFFIX, match);
    }

    /**
     * 生成带" OR "前缀大于查询的SQL片段.
     * @param field 数据库字段
     * @param value 值
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala orMoreThan(String field, Object value) {
        return this.doNormal(ZealotConst.OR_PREFIX, field, value, ZealotConst.GT_SUFFIX, true);
    }

    /**
     * 生成带" OR "前缀大于查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala orMoreThan(String field, Object value, boolean match) {
        return this.doNormal(ZealotConst.OR_PREFIX, field, value, ZealotConst.GT_SUFFIX, match);
    }

    /**
     * 生成小于查询的SQL片段.
     * @param field 数据库字段
     * @param value 值
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala lessThan(String field, Object value) {
        return this.doNormal(ZealotConst.ONE_SPACE, field, value, ZealotConst.LT_SUFFIX, true);
    }

    /**
     * 生成小于查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala lessThan(String field, Object value, boolean match) {
        return this.doNormal(ZealotConst.ONE_SPACE, field, value, ZealotConst.LT_SUFFIX, match);
    }

    /**
     * 生成带" AND "前缀小于查询的SQL片段.
     * @param field 数据库字段
     * @param value 值
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala andLessThan(String field, Object value) {
        return this.doNormal(ZealotConst.AND_PREFIX, field, value, ZealotConst.LT_SUFFIX, true);
    }

    /**
     * 生成带" AND "前缀小于查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala andLessThan(String field, Object value, boolean match) {
        return this.doNormal(ZealotConst.AND_PREFIX, field, value, ZealotConst.LT_SUFFIX, match);
    }

    /**
     * 生成带" OR "前缀小于查询的SQL片段.
     * @param field 数据库字段
     * @param value 值
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala orLessThan(String field, Object value) {
        return this.doNormal(ZealotConst.OR_PREFIX, field, value, ZealotConst.LT_SUFFIX, true);
    }

    /**
     * 生成带" OR "前缀小于查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala orLessThan(String field, Object value, boolean match) {
        return this.doNormal(ZealotConst.OR_PREFIX, field, value, ZealotConst.LT_SUFFIX, match);
    }

    /**
     * 生成大于等于查询的SQL片段.
     * @param field 数据库字段
     * @param value 值
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala moreEqual(String field, Object value) {
        return this.doNormal(ZealotConst.ONE_SPACE, field, value, ZealotConst.GTE_SUFFIX, true);
    }

    /**
     * 生成大于等于查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala moreEqual(String field, Object value, boolean match) {
        return this.doNormal(ZealotConst.ONE_SPACE, field, value, ZealotConst.GTE_SUFFIX, match);
    }

    /**
     * 生成带" AND "前缀大于等于查询的SQL片段.
     * @param field 数据库字段
     * @param value 值
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala andMoreEqual(String field, Object value) {
        return this.doNormal(ZealotConst.AND_PREFIX, field, value, ZealotConst.GTE_SUFFIX, true);
    }

    /**
     * 生成带" AND "前缀大于等于查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala andMoreEqual(String field, Object value, boolean match) {
        return this.doNormal(ZealotConst.AND_PREFIX, field, value, ZealotConst.GTE_SUFFIX, match);
    }

    /**
     * 生成带" OR "前缀大于等于查询的SQL片段.
     * @param field 数据库字段
     * @param value 值
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala orMoreEqual(String field, Object value) {
        return this.doNormal(ZealotConst.OR_PREFIX, field, value, ZealotConst.GTE_SUFFIX, true);
    }

    /**
     * 生成带" OR "前缀大于等于查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala orMoreEqual(String field, Object value, boolean match) {
        return this.doNormal(ZealotConst.OR_PREFIX, field, value, ZealotConst.GTE_SUFFIX, match);
    }

    /**
     * 生成小于等于查询的SQL片段.
     * @param field 数据库字段
     * @param value 值
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala lessEqual(String field, Object value) {
        return this.doNormal(ZealotConst.ONE_SPACE, field, value, ZealotConst.LTE_SUFFIX, true);
    }

    /**
     * 生成小于等于查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala lessEqual(String field, Object value, boolean match) {
        return this.doNormal(ZealotConst.ONE_SPACE, field, value, ZealotConst.LTE_SUFFIX, match);
    }

    /**
     * 生成带" AND "前缀小于等于查询的SQL片段.
     * @param field 数据库字段
     * @param value 值
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala andLessEqual(String field, Object value) {
        return this.doNormal(ZealotConst.AND_PREFIX, field, value, ZealotConst.LTE_SUFFIX, true);
    }

    /**
     * 生成带" AND "前缀小于等于查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala andLessEqual(String field, Object value, boolean match) {
        return this.doNormal(ZealotConst.AND_PREFIX, field, value, ZealotConst.LTE_SUFFIX, match);
    }

    /**
     * 生成带" OR "前缀小于等于查询的SQL片段.
     * @param field 数据库字段
     * @param value 值
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala orLessEqual(String field, Object value) {
        return this.doNormal(ZealotConst.OR_PREFIX, field, value, ZealotConst.LTE_SUFFIX, true);
    }

    /**
     * 生成带" OR "前缀小于等于查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala orLessEqual(String field, Object value, boolean match) {
        return this.doNormal(ZealotConst.OR_PREFIX, field, value, ZealotConst.LTE_SUFFIX, match);
    }

    /**
     * 生成like模糊查询的SQL片段.
     * @param field 数据库字段
     * @param value 值
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala like(String field, Object value) {
        return this.doLike(ZealotConst.ONE_SPACE, field, value, true, true);
    }

    /**
     * 生成like模糊查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala like(String field, Object value, boolean match) {
        return this.doLike(ZealotConst.ONE_SPACE, field, value, match, true);
    }

    /**
     * 生成带" AND "前缀的like模糊查询的SQL片段.
     * @param field 数据库字段
     * @param value 值
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala andLike(String field, Object value) {
        return this.doLike(ZealotConst.AND_PREFIX, field, value, true, true);
    }

    /**
     * 生成带" AND "前缀的like模糊查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala andLike(String field, Object value, boolean match) {
        return this.doLike(ZealotConst.AND_PREFIX, field, value, match, true);
    }

    /**
     * 生成带" OR "前缀的like模糊查询的SQL片段.
     * @param field 数据库字段
     * @param value 值
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala orLike(String field, Object value) {
        return this.doLike(ZealotConst.OR_PREFIX, field, value, true, true);
    }

    /**
     * 生成带" OR "前缀的like模糊查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala orLike(String field, Object value, boolean match) {
        return this.doLike(ZealotConst.OR_PREFIX, field, value, match, true);
    }

    /**
     * 生成" NOT LIKE "模糊查询的SQL片段.
     * <p>示例：传入 {"b.title", "Spring"} 两个参数，生成的SQL片段为：" b.title NOT LIKE ? ", SQL参数为:{"%Spring%"}</p>
     *
     * @param field 数据库字段
     * @param value 值
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala notLike(String field, Object value) {
        return this.doLike(ZealotConst.ONE_SPACE, field, value, true, false);
    }

    /**
     * 生成" NOT LIKE "模糊查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * <p>示例：传入 {"b.title", "Spring", true} 三个参数，生成的SQL片段为：" b.title NOT LIKE ? ", SQL参数为:{"%Spring%"}</p>
     *
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala notLike(String field, Object value, boolean match) {
        return this.doLike(ZealotConst.ONE_SPACE, field, value, match, false);
    }

    /**
     * 生成带" AND "前缀的" NOT LIKE "模糊查询的SQL片段.
     * <p>示例：传入 {"b.title", "Spring"} 两个参数，生成的SQL片段为：" AND b.title NOT LIKE ? ", SQL参数为:{"%Spring%"}</p>
     *
     * @param field 数据库字段
     * @param value 值
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala andNotLike(String field, Object value) {
        return this.doLike(ZealotConst.AND_PREFIX, field, value, true, false);
    }

    /**
     * 生成带" AND "前缀的" NOT LIKE "模糊查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * <p>示例：传入 {"b.title", "Spring", true} 三个参数，生成的SQL片段为：" AND b.title NOT LIKE ? ", SQL参数为:{"%Spring%"}</p>
     *
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala andNotLike(String field, Object value, boolean match) {
        return this.doLike(ZealotConst.AND_PREFIX, field, value, match, false);
    }

    /**
     * 生成带" OR "前缀的" NOT LIKE "模糊查询的SQL片段.
     * <p>示例：传入 {"b.title", "Spring"} 两个参数，生成的SQL片段为：" OR b.title NOT LIKE ? ", SQL参数为:{"%Spring%"}</p>
     *
     * @param field 数据库字段
     * @param value 值
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala orNotLike(String field, Object value) {
        return this.doLike(ZealotConst.OR_PREFIX, field, value, true, false);
    }

    /**
     * 生成带" OR "前缀的" NOT LIKE "模糊查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * <p>示例：传入 {"b.title", "Spring", true} 三个参数，生成的SQL片段为：" OR b.title NOT LIKE ? ", SQL参数为:{"%Spring%"}</p>
     *
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala orNotLike(String field, Object value, boolean match) {
        return this.doLike(ZealotConst.OR_PREFIX, field, value, match, false);
    }

    /**
     * 根据指定的模式字符串生成like模糊查询的SQL片段.
     * <p>示例：传入 {"b.title", "Java%"} 两个参数，生成的SQL片段为：" b.title LIKE 'Java%' "</p>
     *
     * @param field 数据库字段
     * @param pattern 模式字符串
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala likePattern(String field, String pattern) {
        return this.doLikePattern(ZealotConst.ONE_SPACE, field, pattern, true, true);
    }

    /**
     * 根据指定的模式字符串生成like模糊查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * <p>示例：传入 {"b.title", "Java%", true} 三个参数，生成的SQL片段为：" b.title LIKE 'Java%' "</p>
     * <p>示例：传入 {"b.title", "Java%", false} 三个参数，生成的SQL片段为空字符串.</p>
     *
     * @param field 数据库字段
     * @param pattern 模式字符串
     * @param match 是否匹配
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala likePattern(String field, String pattern, boolean match) {
        return this.doLikePattern(ZealotConst.ONE_SPACE, field, pattern, match, true);
    }

    /**
     * 根据指定的模式字符串生成带" AND "前缀的like模糊查询的SQL片段.
     * <p>示例：传入 {"b.title", "Java%"} 两个参数，生成的SQL片段为：" AND b.title LIKE 'Java%' "</p>
     *
     * @param field 数据库字段
     * @param pattern 模式字符串
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala andLikePattern(String field, String pattern) {
        return this.doLikePattern(ZealotConst.AND_PREFIX, field, pattern, true, true);
    }

    /**
     * 根据指定的模式字符串生成带" AND "前缀的like模糊查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * <p>示例：传入 {"b.title", "Java%", true} 三个参数，生成的SQL片段为：" AND b.title LIKE 'Java%' "</p>
     * <p>示例：传入 {"b.title", "Java%", false} 三个参数，生成的SQL片段为空字符串.</p>
     *
     * @param field 数据库字段
     * @param pattern 模式字符串
     * @param match 是否匹配
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala andLikePattern(String field, String pattern, boolean match) {
        return this.doLikePattern(ZealotConst.AND_PREFIX, field, pattern, match, true);
    }

    /**
     * 根据指定的模式字符串生成带" OR "前缀的like模糊查询的SQL片段.
     * <p>示例：传入 {"b.title", "Java%"} 两个参数，生成的SQL片段为：" OR b.title LIKE 'Java%' "</p>
     *
     * @param field 数据库字段
     * @param pattern 模式字符串
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala orLikePattern(String field, String pattern) {
        return this.doLikePattern(ZealotConst.OR_PREFIX, field, pattern, true, true);
    }

    /**
     * 根据指定的模式字符串生成带" OR "前缀的like模糊查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * <p>示例：传入 {"b.title", "Java%", true} 三个参数，生成的SQL片段为：" OR b.title LIKE 'Java%' "</p>
     * <p>示例：传入 {"b.title", "Java%", false} 三个参数，生成的SQL片段为空字符串.</p>
     *
     * @param field 数据库字段
     * @param pattern 模式字符串
     * @param match 是否匹配
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala orLikePattern(String field, String pattern, boolean match) {
        return this.doLikePattern(ZealotConst.OR_PREFIX, field, pattern, match, true);
    }

    /**
     * 根据指定的模式字符串生成" NOT LIKE "模糊查询的SQL片段.
     * <p>示例：传入 {"b.title", "Java%"} 两个参数，生成的SQL片段为：" b.title NOT LIKE 'Java%' "</p>
     *
     * @param field 数据库字段
     * @param pattern 模式字符串
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala notLikePattern(String field, String pattern) {
        return this.doLikePattern(ZealotConst.ONE_SPACE, field, pattern, true, false);
    }

    /**
     * 根据指定的模式字符串生成" NOT LIKE "模糊查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * <p>示例：传入 {"b.title", "Java%", true} 三个参数，生成的SQL片段为：" b.title NOT LIKE 'Java%' "</p>
     * <p>示例：传入 {"b.title", "Java%", false} 三个参数，生成的SQL片段为空字符串.</p>
     *
     * @param field 数据库字段
     * @param pattern 模式字符串
     * @param match 是否匹配
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala notLikePattern(String field, String pattern, boolean match) {
        return this.doLikePattern(ZealotConst.ONE_SPACE, field, pattern, match, false);
    }

    /**
     * 根据指定的模式字符串生成带" AND "前缀的" NOT LIKE "模糊查询的SQL片段.
     * <p>示例：传入 {"b.title", "Java%"} 两个参数，生成的SQL片段为：" AND b.title NOT LIKE 'Java%' "</p>
     *
     * @param field 数据库字段
     * @param pattern 模式字符串
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala andNotLikePattern(String field, String pattern) {
        return this.doLikePattern(ZealotConst.AND_PREFIX, field, pattern, true, false);
    }

    /**
     * 根据指定的模式字符串生成带" AND "前缀的" NOT LIKE "模糊查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * <p>示例：传入 {"b.title", "Java%", true} 三个参数，生成的SQL片段为：" AND b.title NOT LIKE 'Java%' "</p>
     * <p>示例：传入 {"b.title", "Java%", false} 三个参数，生成的SQL片段为空字符串.</p>
     *
     * @param field 数据库字段
     * @param pattern 模式字符串
     * @param match 是否匹配
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala andNotLikePattern(String field, String pattern, boolean match) {
        return this.doLikePattern(ZealotConst.AND_PREFIX, field, pattern, match, false);
    }

    /**
     * 根据指定的模式字符串生成带" OR "前缀的" NOT LIKE "模糊查询的SQL片段.
     * <p>示例：传入 {"b.title", "Java%"} 两个参数，生成的SQL片段为：" OR b.title NOT LIKE 'Java%' "</p>
     *
     * @param field 数据库字段
     * @param pattern 模式字符串
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala orNotLikePattern(String field, String pattern) {
        return this.doLikePattern(ZealotConst.OR_PREFIX, field, pattern, true, false);
    }

    /**
     * 根据指定的模式字符串生成带" OR "前缀的" NOT LIKE "模糊查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * <p>示例：传入 {"b.title", "Java%", true} 三个参数，生成的SQL片段为：" OR b.title NOT LIKE 'Java%' "</p>
     * <p>示例：传入 {"b.title", "Java%", false} 三个参数，生成的SQL片段为空字符串.</p>
     *
     * @param field 数据库字段
     * @param pattern 模式字符串
     * @param match 是否匹配
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala orNotLikePattern(String field, String pattern, boolean match) {
        return this.doLikePattern(ZealotConst.OR_PREFIX, field, pattern, match, false);
    }

    /**
     * 生成between区间查询的SQL片段(当某一个值为null时，会是大于等于或小于等于的情形).
     * @param field 数据库字段
     * @param startValue 开始值
     * @param endValue 结束值
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala between(String field, Object startValue, Object endValue) {
        return this.doBetween(ZealotConst.ONE_SPACE, field, startValue, endValue, true);
    }

    /**
     * 生成between区间查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成(当某一个值为null时，会是大于等于或小于等于的情形).
     * @param field 数据库字段
     * @param startValue 开始值
     * @param endValue 结束值
     * @param match 是否匹配
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala between(String field, Object startValue, Object endValue, boolean match) {
        return this.doBetween(ZealotConst.ONE_SPACE, field, startValue, endValue, match);
    }

    /**
     * 生成带" AND "前缀的between区间查询的SQL片段(当某一个值为null时，会是大于等于或小于等于的情形).
     * @param field 数据库字段
     * @param startValue 开始值
     * @param endValue 结束值
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala andBetween(String field, Object startValue, Object endValue) {
        return this.doBetween(ZealotConst.AND_PREFIX, field, startValue, endValue, true);
    }

    /**
     * 生成带" AND "前缀的between区间查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成(当某一个值为null时，会是大于等于或小于等于的情形).
     * @param field 数据库字段
     * @param startValue 开始值
     * @param endValue 结束值
     * @param match 是否匹配
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala andBetween(String field, Object startValue, Object endValue, boolean match) {
        return this.doBetween(ZealotConst.AND_PREFIX, field, startValue, endValue, match);
    }

    /**
     * 生成带" OR "前缀的between区间查询的SQL片段(当某一个值为null时，会是大于等于或小于等于的情形).
     * @param field 数据库字段
     * @param startValue 开始值
     * @param endValue 结束值
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala orBetween(String field, Object startValue, Object endValue) {
        return this.doBetween(ZealotConst.OR_PREFIX, field, startValue, endValue, true);
    }

    /**
     * 生成带" OR "前缀的between区间查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成(当某一个值为null时，会是大于等于或小于等于的情形).
     * @param field 数据库字段
     * @param startValue 开始值
     * @param endValue 结束值
     * @param match 是否匹配
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala orBetween(String field, Object startValue, Object endValue, boolean match) {
        return this.doBetween(ZealotConst.OR_PREFIX, field, startValue, endValue, match);
    }

    /**
     * 生成in范围查询的SQL片段.
     * @param field 数据库字段
     * @param values 数组的值
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala in(String field, Object[] values) {
        return this.doIn(ZealotConst.ONE_SPACE, field, values, true, true);
    }

    /**
     * 生成in范围查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param values 数组的值
     * @param match 是否匹配
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala in(String field, Object[] values, boolean match) {
        return this.doIn(ZealotConst.ONE_SPACE, field, values, match, true);
    }

    /**
     * 生成in范围查询的SQL片段.
     * @param field 数据库字段
     * @param values 集合的值
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala in(String field, Collection<?> values) {
        return this.doIn(ZealotConst.ONE_SPACE, field, values, true, true);
    }

    /**
     * 生成in范围查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param values 集合的值
     * @param match 是否匹配
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala in(String field, Collection<?> values, boolean match) {
        return this.doIn(ZealotConst.ONE_SPACE, field, values, match, true);
    }

    /**
     * 生成带" AND "前缀的in范围查询的SQL片段.
     * @param field 数据库字段
     * @param values 数组的值
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala andIn(String field, Object[] values) {
        return this.doIn(ZealotConst.AND_PREFIX, field, values, true, true);
    }

    /**
     * 生成带" AND "前缀的in范围查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param values 数组的值
     * @param match 是否匹配
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala andIn(String field, Object[] values, boolean match) {
        return this.doIn(ZealotConst.AND_PREFIX, field, values, match, true);
    }

    /**
     * 生成带" AND "前缀的in范围查询的SQL片段.
     * @param field 数据库字段
     * @param values 集合的值
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala andIn(String field, Collection<?> values) {
        return this.doIn(ZealotConst.AND_PREFIX, field, values, true, true);
    }

    /**
     * 生成带" AND "前缀的in范围查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param values 集合的值
     * @param match 是否匹配
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala andIn(String field, Collection<?> values, boolean match) {
        return this.doIn(ZealotConst.AND_PREFIX, field, values, match, true);
    }

    /**
     * 生成带" OR "前缀的in范围查询的SQL片段.
     * @param field 数据库字段
     * @param values 数组的值
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala orIn(String field, Object[] values) {
        return this.doIn(ZealotConst.OR_PREFIX, field, values, true, true);
    }

    /**
     * 生成带" OR "前缀的in范围查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param values 数组的值
     * @param match 是否匹配
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala orIn(String field, Object[] values, boolean match) {
        return this.doIn(ZealotConst.OR_PREFIX, field, values, match, true);
    }

    /**
     * 生成带" OR "前缀的in范围查询的SQL片段.
     * @param field 数据库字段
     * @param values 集合的值
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala orIn(String field, Collection<?> values) {
        return this.doIn(ZealotConst.OR_PREFIX, field, values, true, true);
    }

    /**
     * 生成带" OR "前缀的in范围查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param values 集合的值
     * @param match 是否匹配
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala orIn(String field, Collection<?> values, boolean match) {
        return this.doIn(ZealotConst.OR_PREFIX, field, values, match, true);
    }

    /**
     * 生成" NOT IN "范围查询的SQL片段.
     * @param field 数据库字段
     * @param values 数组的值
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala notIn(String field, Object[] values) {
        return this.doIn(ZealotConst.ONE_SPACE, field, values, true, false);
    }

    /**
     * 生成" NOT IN "范围查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param values 数组的值
     * @param match 是否匹配
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala notIn(String field, Object[] values, boolean match) {
        return this.doIn(ZealotConst.ONE_SPACE, field, values, match, false);
    }

    /**
     * 生成" NOT IN "范围查询的SQL片段.
     * @param field 数据库字段
     * @param values 集合的值
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala notIn(String field, Collection<?> values) {
        return this.doIn(ZealotConst.ONE_SPACE, field, values, true, false);
    }

    /**
     * 生成" NOT IN "范围查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param values 集合的值
     * @param match 是否匹配
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala notIn(String field, Collection<?> values, boolean match) {
        return this.doIn(ZealotConst.ONE_SPACE, field, values, match, false);
    }

    /**
     * 生成带" AND "前缀的" NOT IN "范围查询的SQL片段.
     * @param field 数据库字段
     * @param values 数组的值
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala andNotIn(String field, Object[] values) {
        return this.doIn(ZealotConst.AND_PREFIX, field, values, true, false);
    }

    /**
     * 生成带" AND "前缀的" NOT IN "范围查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param values 数组的值
     * @param match 是否匹配
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala andNotIn(String field, Object[] values, boolean match) {
        return this.doIn(ZealotConst.AND_PREFIX, field, values, match, false);
    }

    /**
     * 生成带" AND "前缀的" NOT IN "范围查询的SQL片段.
     * @param field 数据库字段
     * @param values 集合的值
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala andNotIn(String field, Collection<?> values) {
        return this.doIn(ZealotConst.AND_PREFIX, field, values, true, false);
    }

    /**
     * 生成带" AND "前缀的" NOT IN "范围查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param values 集合的值
     * @param match 是否匹配
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala andNotIn(String field, Collection<?> values, boolean match) {
        return this.doIn(ZealotConst.AND_PREFIX, field, values, match, false);
    }

    /**
     * 生成带" OR "前缀的" NOT IN "范围查询的SQL片段.
     * @param field 数据库字段
     * @param values 数组的值
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala orNotIn(String field, Object[] values) {
        return this.doIn(ZealotConst.OR_PREFIX, field, values, true, false);
    }

    /**
     * 生成带" OR "前缀的" NOT IN "范围查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param values 数组的值
     * @param match 是否匹配
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala orNotIn(String field, Object[] values, boolean match) {
        return this.doIn(ZealotConst.OR_PREFIX, field, values, match, false);
    }

    /**
     * 生成带" OR "前缀的" NOT IN "范围查询的SQL片段.
     * @param field 数据库字段
     * @param values 集合的值
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala orNotIn(String field, Collection<?> values) {
        return this.doIn(ZealotConst.OR_PREFIX, field, values, true, false);
    }

    /**
     * 生成带" OR "前缀的" NOT IN "范围查询的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * @param field 数据库字段
     * @param values 集合的值
     * @param match 是否匹配
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala orNotIn(String field, Collection<?> values, boolean match) {
        return this.doIn(ZealotConst.OR_PREFIX, field, values, match, false);
    }

    /**
     * 生成" IS NULL "的SQL片段.
     * <p>示例：传入 {"a.name"} 参数，生成的SQL片段为：" a.name IS NULL "</p>
     *
     * @param field 数据库字段
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala isNull(String field) {
        return this.doIsNull(ZealotConst.ONE_SPACE, field, true,true);
    }

    /**
     * 生成" IS NULL "的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * <p>示例：传入 {"a.name", true} 两个参数，生成的SQL片段为：" a.name IS NULL "</p>
     *
     * @param field 数据库字段
     * @param match 是否匹配
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala isNull(String field, boolean match) {
        return this.doIsNull(ZealotConst.ONE_SPACE, field, match,true);
    }

    /**
     * 生成带" AND "前缀" IS NULL "的SQL片段.
     * <p>示例：传入 {"a.name"} 参数，生成的SQL片段为：" AND a.name IS NULL "</p>
     *
     * @param field 数据库字段
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala andIsNull(String field) {
        return this.doIsNull(ZealotConst.AND_PREFIX, field, true,true);
    }

    /**
     * 生成带" AND "前缀" IS NULL "的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * <p>示例：传入 {"a.name", true} 两个参数，生成的SQL片段为：" AND a.name IS NULL "</p>
     *
     * @param field 数据库字段
     * @param match 是否匹配
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala andIsNull(String field, boolean match) {
        return this.doIsNull(ZealotConst.AND_PREFIX, field, match,true);
    }

    /**
     * 生成带" OR "前缀" IS NULL "的SQL片段.
     * <p>示例：传入 {"a.name"} 参数，生成的SQL片段为：" OR a.name IS NULL "</p>
     *
     * @param field 数据库字段
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala orIsNull(String field) {
        return this.doIsNull(ZealotConst.OR_PREFIX, field, true,true);
    }

    /**
     * 生成带" OR "前缀" IS NULL "的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * <p>示例：传入 {"a.name", true} 两个参数，生成的SQL片段为：" OR a.name IS NULL "</p>
     *
     * @param field 数据库字段
     * @param match 是否匹配
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala orIsNull(String field, boolean match) {
        return this.doIsNull(ZealotConst.OR_PREFIX, field, match,true);
    }

    /**
     * 生成" IS NOT NULL "的SQL片段.
     * <p>示例：传入 {"a.name"} 参数，生成的SQL片段为：" a.name IS NOT NULL "</p>
     *
     * @param field 数据库字段
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala isNotNull(String field) {
        return this.doIsNull(ZealotConst.ONE_SPACE, field, true,false);
    }

    /**
     * 生成" IS NOT NULL "的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * <p>示例：传入 {"a.name", true} 两个参数，生成的SQL片段为：" a.name IS NOT NULL "</p>
     *
     * @param field 数据库字段
     * @param match 是否匹配
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala isNotNull(String field, boolean match) {
        return this.doIsNull(ZealotConst.ONE_SPACE, field, match,false);
    }

    /**
     * 生成带" AND "前缀" IS NOT NULL "的SQL片段.
     * <p>示例：传入 {"a.name"} 参数，生成的SQL片段为：" AND a.name IS NOT NULL "</p>
     *
     * @param field 数据库字段
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala andIsNotNull(String field) {
        return this.doIsNull(ZealotConst.AND_PREFIX, field, true,false);
    }

    /**
     * 生成带" AND "前缀" IS NOT NULL "的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * <p>示例：传入 {"a.name", true} 两个参数，生成的SQL片段为：" AND a.name IS NOT NULL "</p>
     *
     * @param field 数据库字段
     * @param match 是否匹配
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala andIsNotNull(String field, boolean match) {
        return this.doIsNull(ZealotConst.AND_PREFIX, field, match,false);
    }

    /**
     * 生成带" OR "前缀" IS NOT NULL "的SQL片段.
     * <p>示例：传入 {"a.name"} 参数，生成的SQL片段为：" OR a.name IS NOT NULL "</p>
     *
     * @param field 数据库字段
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala orIsNotNull(String field) {
        return this.doIsNull(ZealotConst.OR_PREFIX, field, true,false);
    }

    /**
     * 生成带" OR "前缀" IS NOT NULL "的SQL片段,如果match为true时则生成该条SQL片段，否则不生成.
     * <p>示例：传入 {"a.name", true} 两个参数，生成的SQL片段为：" OR a.name IS NOT NULL "</p>
     *
     * @param field 数据库字段
     * @param match 是否匹配
     * @return {@link FenixKhala} 实例
     */
    public FenixKhala orIsNotNull(String field, boolean match) {
        return this.doIsNull(ZealotConst.OR_PREFIX, field, match,false);
    }

}