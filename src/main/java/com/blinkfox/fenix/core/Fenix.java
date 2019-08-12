package com.blinkfox.fenix.core;

import com.blinkfox.fenix.bean.BuildSource;
import com.blinkfox.fenix.bean.SqlInfo;
import com.blinkfox.fenix.consts.Const;
import com.blinkfox.fenix.consts.SqlKeyConst;
import com.blinkfox.fenix.consts.SymbolConst;
import com.blinkfox.fenix.core.builder.JavaSqlInfoBuilder;
import com.blinkfox.fenix.helper.CollectionHelper;
import com.blinkfox.fenix.helper.SqlInfoPrinter;
import com.blinkfox.fenix.helper.StringHelper;

import java.util.Collection;

/**
 * 使用 XML 或者 Java 链式写法来拼接 JPQL 或者 SQL 语句和对应命名参数的 {@link com.blinkfox.fenix.bean.SqlInfo} 信息的核心 API 类.
 *
 * @author blinkfox on 2019-08-11.
 * @see FenixXmlBuilder
 */
public final class Fenix {

    /**
     * 封装了 {@link SqlInfo}、应用中提供的上下文参数、前缀等信息.
     *
     * <p>注：由于这里是纯Java拼接,所以就没有xml的Node节点信息，初始为为null.</p>
     */
    private BuildSource source;

    /**
     * 用于构建 {@link SqlInfo} 对象中的 JPQL 片段和参数的构建器.
     */
    private JavaSqlInfoBuilder sqlInfoBuilder;

    /**
     * 私有构造方法，构造时就初始化 {@link BuildSource} 相应的参数信息.
     */
    private Fenix() {
        this.source = new BuildSource(new SqlInfo());
        this.sqlInfoBuilder = new JavaSqlInfoBuilder(source);
    }

    /**
     * 开始的方法，初始化 {@link Fenix} 实例.
     *
     * @return {@link Fenix} 实例
     */
    public static Fenix start() {
        return new Fenix();
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
     * 通过传入 fullFenixId（命名空间和 Fenix 节点的 ID）和上下文参数，
     * 来简单快速的生成和获取 {@link SqlInfo} 信息(有参的SQL).
     *
     * @param fullFenixId XML 命名空间 'namespace' + '.' + 'fenixId' 的值，如: "student.queryStudentById".
     * @param context 上下文参数（一般是 Bean 或者 map）
     * @return 返回 {@link SqlInfo} 对象
     */
    public static SqlInfo getXmlSqlInfo(String fullFenixId, Object context) {
        return FenixXmlBuilder.getXmlSqlInfo(fullFenixId, context);
    }

    /**
     * 通过传入 Fenix XML 文件对应的命名空间、Fenix 节点的 ID 以及 Map 型参数对象，
     * 来生成和获取 {@link SqlInfo} 信息(有参的SQL).
     *
     * @param namespace XML 命名空间
     * @param fenixId XML 中的 fenixId
     * @param context 上下文参数（一般是 Bean 或者 map）
     * @return 返回 {@link SqlInfo} 对象
     */
    public static SqlInfo getXmlSqlInfo(String namespace, String fenixId, Object context) {
        return FenixXmlBuilder.getXmlSqlInfo(namespace, fenixId, context);
    }

    /**
     * 连接字符串.
     *
     * @param sqlKey SQL 关键字
     * @param params 其他若干字符串参数
     */
    private Fenix concat(String sqlKey, String... params) {
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
     * @return {@link Fenix} 实例
     */
    public Fenix insertInto(String text) {
        return this.concat(SqlKeyConst.INSERT_INTO, text);
    }

    /**
     * 拼接并带上 'VALUES' 关键字的字符串.
     *
     * @param text 文本
     * @return {@link Fenix} 实例
     */
    public Fenix values(String text) {
        return this.concat(SqlKeyConst.VALUES, text);
    }

    /**
     * 拼接并带上 'DELETE FROM' 关键字的字符串.
     *
     * @param text 文本
     * @return {@link Fenix} 实例
     */
    public Fenix deleteFrom(String text) {
        return this.concat(SqlKeyConst.DELETE_FROM, text);
    }

    /**
     * 拼接并带上 'UPDATE' 关键字的字符串.
     *
     * @param text 文本
     * @return {@link Fenix} 实例
     */
    public Fenix update(String text) {
        return this.concat(SqlKeyConst.UPDATE, text);
    }

    /**
     * 拼接并带上 'SELECT' 关键字的字符串.
     *
     * @param text 文本
     * @return {@link Fenix} 实例
     */
    public Fenix select(String text) {
        return this.concat(SqlKeyConst.SELECT, text);
    }

    /**
     * 拼接并带上 'FROM' 关键字的字符串.
     *
     * @param text 文本
     * @return {@link Fenix} 实例
     */
    public Fenix from(String text) {
        return this.concat(SqlKeyConst.FROM, text);
    }

    /**
     * 拼接并带上 'WHERE' 关键字的字符串和动态参数.
     *
     * @param text 文本
     * @param value 不定个数的参数值
     * @return {@link Fenix} 实例
     */
    public Fenix where(String text, Object... value) {
        this.concat(SqlKeyConst.WHERE, text);
        return this.param(value);
    }

    /**
     * 拼接并带上 'AND' 关键字的字符串.
     *
     * @param text 文本
     * @return {@link Fenix} 实例
     */
    public Fenix and(String text) {
        return this.concat(SqlKeyConst.AND, text);
    }

    /**
     * 拼接并带上 'OR' 关键字的字符串.
     *
     * @param text 文本
     * @return {@link Fenix} 实例
     */
    public Fenix or(String text) {
        return this.concat(SqlKeyConst.OR, text);
    }

    /**
     * 拼接并带上 'AS' 关键字的字符串.
     *
     * @param text 文本
     * @return {@link Fenix} 实例
     */
    public Fenix as(String text) {
        return this.concat(SqlKeyConst.AS, text);
    }

    /**
     * 拼接并带上 'AS' 关键字的字符串.
     *
     * @param text 文本
     * @return {@link Fenix} 实例
     */
    public Fenix set(String text) {
        return this.concat(SqlKeyConst.SET, text);
    }

    /**
     * 拼接并带上 'INNER JOIN' 关键字的字符串.
     *
     * @param text 文本
     * @return {@link Fenix} 实例
     */
    public Fenix innerJoin(String text) {
        return this.concat(SqlKeyConst.INNER_JOIN, text);
    }

    /**
     * 拼接并带上 'LEFT JOIN' 关键字的字符串.
     *
     * @param text 文本
     * @return {@link Fenix} 实例
     */
    public Fenix leftJoin(String text) {
        return this.concat(SqlKeyConst.LEFT_JOIN, text);
    }

    /**
     * 拼接并带上 'RIGHT JOIN' 关键字的字符串.
     *
     * @param text 文本
     * @return {@link Fenix} 实例
     */
    public Fenix rightJoin(String text) {
        return this.concat(SqlKeyConst.RIGHT_JOIN, text);
    }

    /**
     * 拼接并带上 'FULL JOIN' 关键字的字符串.
     *
     * @param text 文本
     * @return {@link Fenix} 实例
     */
    public Fenix fullJoin(String text) {
        return this.concat(SqlKeyConst.FULL_JOIN, text);
    }

    /**
     * 拼接并带上 'ON' 关键字的字符串.
     *
     * @param text 文本
     * @return {@link Fenix} 实例
     */
    public Fenix on(String text) {
        return this.concat(SqlKeyConst.ON, text);
    }

    /**
     * 拼接并带上 'ORDER BY' 关键字的字符串.
     *
     * @param text 文本
     * @return {@link Fenix} 实例
     */
    public Fenix orderBy(String text) {
        return this.concat(SqlKeyConst.ORDER_BY, text);
    }

    /**
     * 拼接并带上 'GROUP BY' 关键字的字符串.
     *
     * @param text 文本
     * @return {@link Fenix} 实例
     */
    public Fenix groupBy(String text) {
        return this.concat(SqlKeyConst.GROUP_BY, text);
    }

    /**
     * 拼接并带上 'HAVING' 关键字的字符串.
     *
     * @param text 文本
     * @return {@link Fenix} 实例
     */
    public Fenix having(String text) {
        return this.concat(SqlKeyConst.HAVING, text);
    }

    /**
     * 拼接并带上 'LIMIT' 关键字的字符串.
     *
     * @param text 文本
     * @return {@link Fenix} 实例
     */
    public Fenix limit(String text) {
        return this.concat(SqlKeyConst.LIMIT, text);
    }

    /**
     * 拼接并带上 'OFFSET' 关键字的字符串.
     *
     * @param text 文本
     * @return {@link Fenix} 实例
     */
    public Fenix offset(String text) {
        return this.concat(SqlKeyConst.OFFSET, text);
    }

    /**
     * 拼接并带上 'ASC' 关键字的字符串.
     *
     * @return {@link Fenix} 实例
     */
    public Fenix asc() {
        return this.concat(SqlKeyConst.ASC);
    }

    /**
     * 拼接并带上 'DESC' 关键字的字符串.
     *
     * @return {@link Fenix} 实例
     */
    public Fenix desc() {
        return this.concat(SqlKeyConst.DESC);
    }

    /**
     * 拼接并带上 'UNION' 关键字的字符串.
     *
     * @return {@link Fenix} 实例
     */
    public Fenix union() {
        return this.concat(SqlKeyConst.UNION);
    }

    /**
     * 拼接并带上 'UNION ALL' 关键字的字符串.
     *
     * @return {@link Fenix} 实例
     */
    public Fenix unionAll() {
        return this.concat(SqlKeyConst.UNION_ALL);
    }

    /**
     * 在 SQL 后追加任何文本字符串，后可追加自定义可变参数.
     *
     * @param text 文本
     * @param values 可变参数数组
     * @return {@link Fenix} 实例
     */
    public Fenix text(String text, Object... values) {
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
     * @return {@link Fenix} 实例
     */
    public Fenix text(boolean match, String text, Object... values) {
        return match ? text(text, values) : this;
    }

    /**
     * 在 SQL 的参数集合后追加任何的数组.
     *
     * @param value 值
     * @param objType 对象类型那
     * @return {@link Fenix} 实例
     */
    private Fenix appendParams(Object value, int objType) {
        Object[] values = CollectionHelper.toArray(value, objType);
        if (CollectionHelper.isNotEmpty(values)) {
            // TODO 之类待修改和完善.
            //Collections.addAll(this.source.getXmlSqlInfo().getParams(), values);
        }
        return this;
    }

    /**
     * 在 SQL 的参数集合后追加不定对象个数的数组.
     *
     * @param values 不定个数的值，也是数组
     * @return {@link Fenix} 实例
     */
    public Fenix param(Object... values) {
        return this.appendParams(values, Const.OBJTYPE_ARRAY);
    }

    /**
     * 在 SQL 的参数集合后追加任何的一个集合.
     *
     * @param values 不定个数的值
     * @return {@link Fenix} 实例
     */
    public Fenix param(Collection<?> values) {
        return this.appendParams(values, Const.OBJTYPE_COLLECTION);
    }

    /**
     * 基于 {@link FenixAction} 的函数式接口来执行任意的自定义操作.
     *
     * @param action {@link FenixAction} 的接口实现类实例
     * @return {@link Fenix} 实例
     */
    public Fenix doAny(FenixAction action) {
        SqlInfo sqlInfo = this.source.getSqlInfo();
        action.execute(sqlInfo.getJoin(), sqlInfo.getParams());
        return this;
    }

    /**
     * 当匹配 match 条件为 true 时，才执行自定义的任意 {@link FenixAction} 操作.
     *
     * @param match 匹配条件
     * @param action {@link FenixAction} 的接口实现类实例
     * @return {@link Fenix} 实例
     */
    public Fenix doAny(boolean match, FenixAction action) {
        return match ? this.doAny(action) : this;
    }

    /**
     * 执行生成等值查询 JPQL 或 SQL 片段的方法.
     *
     * @param prefix 前缀
     * @param field 数据库字段
     * @param value 值
     * @param symbol 关键操作符
     * @param match 是否匹配
     * @return {@link Fenix} 实例的当前实例
     */
    private Fenix doNormal(String prefix, String field, Object value, String symbol, boolean match) {
        if (match) {
            //this.source.setPrefix(prefix);
            //this.sqlInfoBuilder.buildNormalSql(field, value, symbol);
            // TODO 有待修改和完善.
            //.buildNormalSql(field, value, symbol);
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
     * @return {@link Fenix} 实例的当前实例
     */
    private Fenix doLike(String prefix, String field, Object value, boolean match, boolean positive) {
        if (match) {
            // TODO 这里还有问题.
            this.source.setPrefix(prefix).setSymbol(positive ? SymbolConst.LIKE : SymbolConst.NOT_LIKE);
            //this.sqlInfoBuilder.buildLikeSql(field, value);
            this.source.resetPrefix();
        }
        return this;
    }

    /**
     * 执行根据传入的模式来生成 LIKE 匹配的 SQL 片段的方法.
     *
     * @param prefix 前缀
     * @param field 数据库字段
     * @param pattern 值
     * @param match 是否匹配
     * @param positive true 则表示是 LIKE，否则是 NOT LIKE
     * @return {@link Fenix} 实例的当前实例
     */
    private Fenix doLikePattern(String prefix, String field, String pattern, boolean match, boolean positive) {
        if (match) {
            this.source.setPrefix(prefix).setSymbol(positive ? SymbolConst.LIKE : SymbolConst.NOT_LIKE);
            this.sqlInfoBuilder.buildLikePatternSql(field, pattern);
            this.source.resetPrefix();
        }
        return this;
    }

    /**
     * 执行生成 'BETWEEN ... AND' 区间查询的 SQL 片段的方法.
     *
     * @param prefix 前缀
     * @param field 数据库字段
     * @param startValue 值
     * @param endValue 值
     * @param match 是否匹配
     * @return {@link Fenix} 实例的当前实例
     */
    private Fenix doBetween(String prefix, String field, Object startValue, Object endValue, boolean match) {
        if (match) {
            this.source.setPrefix(prefix);
            // this.sqlInfoBuilder.buildBetweenSql(field, startValue, endValue);
            this.source.resetPrefix();
        }
        return this;
    }

    /**
     * 执行生成 IN 范围查询的 SQL 片段的方法,如果是集合或数组，则执行生成，否则会包装成集合.
     *
     * @param prefix 前缀
     * @param field 数据库字段
     * @param value 数组的值
     * @param match 是否匹配
     * @param objType 对象类型，取自ZealotConst.java中以OBJTYPE开头的类型
     * @param positive true 则表示是 IN，否则是 NOT IN
     * @return {@link Fenix} 实例的当前实例
     */
    @SuppressWarnings("unchecked")
    private Fenix doInByType(String prefix, String field, Object value,
                             boolean match, int objType, boolean positive) {
        if (match) {
            // 赋予 source 对象 IN 或者 NOT IN 的 SQL 片段的前缀和操作符.
            this.source.setPrefix(prefix).setSymbol(positive ? SymbolConst.IN : SymbolConst.NOT_IN);
            switch (objType) {
                // 如果类型是数组.
                case Const.OBJTYPE_ARRAY:
                    // TODO 待修改
                    // this.sqlInfoBuilder.buildInSql(field, (Object[]) value);
                    break;
                // 如果类型是Java集合.
                case Const.OBJTYPE_COLLECTION:
                    this.sqlInfoBuilder.buildInSqlByCollection(field, (Collection<Object>) value);
                    break;
                default:
                    // 这里要包装成集合.
                    // throw new NotCollectionOrArrayException("in查询的值不是有效的集合或数组!");
            }
            this.source.resetPrefix();
        }
        return this;
    }

    /**
     * 执行生成 IN 范围查询 SQL 片段的方法.
     *
     * @param prefix 前缀
     * @param field 数据库字段
     * @param values 数组的值
     * @param match 是否匹配
     * @param positive true 则表示是 IN，否则是 NOT IN
     * @return {@link Fenix} 实例的当前实例
     */
    private Fenix doIn(String prefix, String field, Object[] values, boolean match, boolean positive) {
        return this.doInByType(prefix, field, values, match, Const.OBJTYPE_ARRAY, positive);
    }

    /**
     * 执行生成 IN 范围查询 SQL 片段的方法.
     *
     * @param prefix 前缀
     * @param field 数据库字段
     * @param values 集合的值
     * @param match 是否匹配
     * @param positive true 则表示是 IN，否则是 NOT IN
     * @return {@link Fenix} 实例的当前实例
     */
    private Fenix doIn(String prefix, String field, Collection<?> values, boolean match, boolean positive) {
        return this.doInByType(prefix, field, values, match, Const.OBJTYPE_COLLECTION, positive);
    }

    /**
     * 执行生成 " IS NULL " 的 SQL 片段的方法.
     *
     * @param prefix 前缀
     * @param field 数据库字段
     * @param match 是否匹配
     * @param positive true则表示是 IS NULL，否则是IS NOT NULL
     * @return {@link Fenix} 实例的当前实例
     */
    private Fenix doIsNull(String prefix, String field, boolean match, boolean positive) {
        if (match) {
            // 判断是"IS NULL"还是"IS NOT NULL"来设置source实例.
            this.source = this.source.setPrefix(prefix)
                    .setSymbol(positive ? SymbolConst.IS_NULL : SymbolConst.IS_NOT_NULL);
            this.sqlInfoBuilder.buildIsNullSql(field);
            this.source.resetPrefix();
        }
        return this;
    }

    /**
     * 生成等值查询的 SQL 片段.
     *
     * @param field 数据库字段
     * @param value 值
     * @return {@link Fenix} 实例
     */
    public Fenix equal(String field, Object value) {
        return this.doNormal(SqlKeyConst.SPACE, field, value, SymbolConst.EQUAL,true);
    }

    /**
     * 生成等值查询的 SQL 片段,如果 match 为 true 时则生成该条 SQL 片段，否则不生成.
     *
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return {@link Fenix} 实例
     */
    public Fenix equal(String field, Object value, boolean match) {
        return this.doNormal(SqlKeyConst.SPACE, field, value, SymbolConst.EQUAL, match);
    }

    /**
     * 生成带 " AND " 前缀等值查询的 SQL 片段.
     *
     * @param field 数据库字段
     * @param value 值
     * @return {@link Fenix} 实例
     */
    public Fenix andEqual(String field, Object value) {
        return this.doNormal(SymbolConst.AND, field, value, SymbolConst.EQUAL, true);
    }

    /**
     * 生成带 " AND " 前缀等值查询的 SQL 片段,如果 match 为 true 时则生成该条 SQL 片段，否则不生成.
     *
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return {@link Fenix} 实例
     */
    public Fenix andEqual(String field, Object value, boolean match) {
        return this.doNormal(SymbolConst.AND, field, value, SymbolConst.EQUAL, match);
    }

    /**
     * 生成带 " OR " 前缀等值查询的 SQL 片段.
     *
     * @param field 数据库字段
     * @param value 值
     * @return {@link Fenix} 实例
     */
    public Fenix orEqual(String field, Object value) {
        return this.doNormal(SymbolConst.OR, field, value, SymbolConst.EQUAL, true);
    }

    /**
     * 生成带 " OR " 前缀等值查询的 SQL 片段,如果 match 为 true 时则生成该条 SQL 片段，否则不生成.
     *
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return {@link Fenix} 实例
     */
    public Fenix orEqual(String field, Object value, boolean match) {
        return this.doNormal(SymbolConst.OR, field, value, SymbolConst.EQUAL, match);
    }

    /**
     * 生成不等查询的 SQL 片段.
     *
     * @param field 数据库字段
     * @param value 值
     * @return {@link Fenix} 实例
     */
    public Fenix notEqual(String field, Object value) {
        return this.doNormal(SqlKeyConst.SPACE, field, value, SymbolConst.NOT_EQUAL,true);
    }

    /**
     * 生成不等查询的 SQL 片段,如果 match 为 true 时则生成该条 SQL 片段，否则不生成.
     *
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return {@link Fenix} 实例
     */
    public Fenix notEqual(String field, Object value, boolean match) {
        return this.doNormal(SqlKeyConst.SPACE, field, value, SymbolConst.NOT_EQUAL, match);
    }

    /**
     * 生成带 " AND " 前缀不等查询的 SQL 片段.
     *
     * @param field 数据库字段
     * @param value 值
     * @return {@link Fenix} 实例
     */
    public Fenix andNotEqual(String field, Object value) {
        return this.doNormal(SymbolConst.AND, field, value, SymbolConst.NOT_EQUAL, true);
    }

    /**
     * 生成带 " AND " 前缀不等查询的 SQL 片段,如果 match 为 true 时则生成该条 SQL 片段，否则不生成.
     *
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return {@link Fenix} 实例
     */
    public Fenix andNotEqual(String field, Object value, boolean match) {
        return this.doNormal(SymbolConst.AND, field, value, SymbolConst.NOT_EQUAL, match);
    }

    /**
     * 生成带 " OR " 前缀不等查询的 SQL 片段.
     *
     * @param field 数据库字段
     * @param value 值
     * @return {@link Fenix} 实例
     */
    public Fenix orNotEqual(String field, Object value) {
        return this.doNormal(SymbolConst.OR, field, value, SymbolConst.NOT_EQUAL, true);
    }

    /**
     * 生成带 " OR " 前缀不等查询的 SQL 片段,如果 match 为 true 时则生成该条 SQL 片段，否则不生成.
     *
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return {@link Fenix} 实例
     */
    public Fenix orNotEqual(String field, Object value, boolean match) {
        return this.doNormal(SymbolConst.OR, field, value, SymbolConst.NOT_EQUAL, match);
    }

    /**
     * 生成大于查询的 SQL 片段.
     *
     * @param field 数据库字段
     * @param value 值
     * @return {@link Fenix} 实例
     */
    public Fenix greaterThan(String field, Object value) {
        return this.doNormal(SqlKeyConst.SPACE, field, value, SymbolConst.GT, true);
    }

    /**
     * 生成大于查询的 SQL 片段,如果 match 为 true 时则生成该条 SQL 片段，否则不生成.
     *
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return {@link Fenix} 实例
     */
    public Fenix greaterThan(String field, Object value, boolean match) {
        return this.doNormal(SqlKeyConst.SPACE, field, value, SymbolConst.GT, match);
    }

    /**
     * 生成带 " AND " 前缀大于查询的 SQL 片段.
     *
     * @param field 数据库字段
     * @param value 值
     * @return {@link Fenix} 实例
     */
    public Fenix andGreaterThan(String field, Object value) {
        return this.doNormal(SymbolConst.AND, field, value, SymbolConst.GT, true);
    }

    /**
     * 生成带 " AND " 前缀大于查询的 SQL 片段,如果 match 为 true 时则生成该条 SQL 片段，否则不生成.
     * 
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return {@link Fenix} 实例
     */
    public Fenix andGreaterThan(String field, Object value, boolean match) {
        return this.doNormal(SymbolConst.AND, field, value, SymbolConst.GT, match);
    }

    /**
     * 生成带 " OR " 前缀大于查询的 SQL 片段.
     *
     * @param field 数据库字段
     * @param value 值
     * @return {@link Fenix} 实例
     */
    public Fenix orGreaterThan(String field, Object value) {
        return this.doNormal(SymbolConst.OR, field, value, SymbolConst.GT, true);
    }

    /**
     * 生成带 " OR " 前缀大于查询的 SQL 片段,如果 match 为 true 时则生成该条 SQL 片段，否则不生成.
     *
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return {@link Fenix} 实例
     */
    public Fenix orGreaterThan(String field, Object value, boolean match) {
        return this.doNormal(SymbolConst.OR, field, value, SymbolConst.GT, match);
    }

    /**
     * 生成小于查询的 SQL 片段.
     *
     * @param field 数据库字段
     * @param value 值
     * @return {@link Fenix} 实例
     */
    public Fenix lessThan(String field, Object value) {
        return this.doNormal(SqlKeyConst.SPACE, field, value, SymbolConst.LT, true);
    }

    /**
     * 生成小于查询的 SQL 片段,如果 match 为 true 时则生成该条 SQL 片段，否则不生成.
     *
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return {@link Fenix} 实例
     */
    public Fenix lessThan(String field, Object value, boolean match) {
        return this.doNormal(SqlKeyConst.SPACE, field, value, SymbolConst.LT, match);
    }

    /**
     * 生成带 " AND " 前缀小于查询的 SQL 片段.
     *
     * @param field 数据库字段
     * @param value 值
     * @return {@link Fenix} 实例
     */
    public Fenix andLessThan(String field, Object value) {
        return this.doNormal(SymbolConst.AND, field, value, SymbolConst.LT, true);
    }

    /**
     * 生成带 " AND " 前缀小于查询的 SQL 片段,如果 match 为 true 时则生成该条 SQL 片段，否则不生成.
     *
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return {@link Fenix} 实例
     */
    public Fenix andLessThan(String field, Object value, boolean match) {
        return this.doNormal(SymbolConst.AND, field, value, SymbolConst.LT, match);
    }

    /**
     * 生成带 " OR " 前缀小于查询的 SQL 片段.
     *
     * @param field 数据库字段
     * @param value 值
     * @return {@link Fenix} 实例
     */
    public Fenix orLessThan(String field, Object value) {
        return this.doNormal(SymbolConst.OR, field, value, SymbolConst.LT, true);
    }

    /**
     * 生成带 " OR " 前缀小于查询的 SQL 片段,如果 match 为 true 时则生成该条 SQL 片段，否则不生成.
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return {@link Fenix} 实例
     */
    public Fenix orLessThan(String field, Object value, boolean match) {
        return this.doNormal(SymbolConst.OR, field, value, SymbolConst.LT, match);
    }

    /**
     * 生成大于等于查询的 SQL 片段.
     *
     * @param field 数据库字段
     * @param value 值
     * @return {@link Fenix} 实例
     */
    public Fenix greaterThanEqual(String field, Object value) {
        return this.doNormal(SqlKeyConst.SPACE, field, value, SymbolConst.GTE, true);
    }

    /**
     * 生成大于等于查询的 SQL 片段,如果 match 为 true 时则生成该条 SQL 片段，否则不生成.
     *
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return {@link Fenix} 实例
     */
    public Fenix greaterThanEqual(String field, Object value, boolean match) {
        return this.doNormal(SqlKeyConst.SPACE, field, value, SymbolConst.GTE, match);
    }

    /**
     * 生成带 " AND " 前缀大于等于查询的 SQL 片段.
     *
     * @param field 数据库字段
     * @param value 值
     * @return {@link Fenix} 实例
     */
    public Fenix andGreaterThanEqual(String field, Object value) {
        return this.doNormal(SymbolConst.AND, field, value, SymbolConst.GTE, true);
    }

    /**
     * 生成带 " AND " 前缀大于等于查询的 SQL 片段,如果 match 为 true 时则生成该条 SQL 片段，否则不生成.
     *
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return {@link Fenix} 实例
     */
    public Fenix andGreaterThanEqual(String field, Object value, boolean match) {
        return this.doNormal(SymbolConst.AND, field, value, SymbolConst.GTE, match);
    }

    /**
     * 生成带 " OR " 前缀大于等于查询的 SQL 片段.
     *
     * @param field 数据库字段
     * @param value 值
     * @return {@link Fenix} 实例
     */
    public Fenix orGreaterThanEqual(String field, Object value) {
        return this.doNormal(SymbolConst.OR, field, value, SymbolConst.GTE, true);
    }

    /**
     * 生成带 " OR " 前缀大于等于查询的 SQL 片段,如果 match 为 true 时则生成该条 SQL 片段，否则不生成.
     * 
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return {@link Fenix} 实例
     */
    public Fenix orGreaterThanEqual(String field, Object value, boolean match) {
        return this.doNormal(SymbolConst.OR, field, value, SymbolConst.GTE, match);
    }

    /**
     * 生成小于等于查询的 SQL 片段.
     *
     * @param field 数据库字段
     * @param value 值
     * @return {@link Fenix} 实例
     */
    public Fenix lessThanEqual(String field, Object value) {
        return this.doNormal(SqlKeyConst.SPACE, field, value, SymbolConst.LTE, true);
    }

    /**
     * 生成小于等于查询的 SQL 片段,如果 match 为 true 时则生成该条 SQL 片段，否则不生成.
     *
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return {@link Fenix} 实例
     */
    public Fenix lessThanEqual(String field, Object value, boolean match) {
        return this.doNormal(SqlKeyConst.SPACE, field, value, SymbolConst.LTE, match);
    }

    /**
     * 生成带 " AND " 前缀小于等于查询的 SQL 片段.
     *
     * @param field 数据库字段
     * @param value 值
     * @return {@link Fenix} 实例
     */
    public Fenix andLessThanEqual(String field, Object value) {
        return this.doNormal(SymbolConst.AND, field, value, SymbolConst.LTE, true);
    }

    /**
     * 生成带 " AND " 前缀小于等于查询的 SQL 片段,如果 match 为 true 时则生成该条 SQL 片段，否则不生成.
     *
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return {@link Fenix} 实例
     */
    public Fenix andLessThanEqual(String field, Object value, boolean match) {
        return this.doNormal(SymbolConst.AND, field, value, SymbolConst.LTE, match);
    }

    /**
     * 生成带 " OR " 前缀小于等于查询的 SQL 片段.
     *
     * @param field 数据库字段
     * @param value 值
     * @return {@link Fenix} 实例
     */
    public Fenix orLessThanEqual(String field, Object value) {
        return this.doNormal(SymbolConst.OR, field, value, SymbolConst.LTE, true);
    }

    /**
     * 生成带 " OR " 前缀小于等于查询的 SQL 片段,如果 match 为 true 时则生成该条 SQL 片段，否则不生成.
     *
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return {@link Fenix} 实例
     */
    public Fenix orLessThanEqual(String field, Object value, boolean match) {
        return this.doNormal(SymbolConst.OR, field, value, SymbolConst.LTE, match);
    }

    /**
     * 生成 LIKE 模糊查询的 SQL 片段.
     * @param field 数据库字段
     * @param value 值
     * @return {@link Fenix} 实例
     */
    public Fenix like(String field, Object value) {
        return this.doLike(SqlKeyConst.SPACE, field, value, true, true);
    }

    /**
     * 生成 LIKE 模糊查询的 SQL 片段,如果 match 为 true 时则生成该条 SQL 片段，否则不生成.
     *
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return {@link Fenix} 实例
     */
    public Fenix like(String field, Object value, boolean match) {
        return this.doLike(SqlKeyConst.SPACE, field, value, match, true);
    }

    /**
     * 生成带 " AND " 前缀的 LIKE 模糊查询的 SQL 片段.
     * 
     * @param field 数据库字段
     * @param value 值
     * @return {@link Fenix} 实例
     */
    public Fenix andLike(String field, Object value) {
        return this.doLike(SymbolConst.AND, field, value, true, true);
    }

    /**
     * 生成带 " AND " 前缀的 LIKE 模糊查询的 SQL 片段,如果 match 为 true 时则生成该条 SQL 片段，否则不生成.
     *
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return {@link Fenix} 实例
     */
    public Fenix andLike(String field, Object value, boolean match) {
        return this.doLike(SymbolConst.AND, field, value, match, true);
    }

    /**
     * 生成带 " OR " 前缀的 LIKE 模糊查询的 SQL 片段.
     *
     * @param field 数据库字段
     * @param value 值
     * @return {@link Fenix} 实例
     */
    public Fenix orLike(String field, Object value) {
        return this.doLike(SymbolConst.OR, field, value, true, true);
    }

    /**
     * 生成带 " OR " 前缀的 LIKE 模糊查询的 SQL 片段,如果 match 为 true 时则生成该条 SQL 片段，否则不生成.
     *
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return {@link Fenix} 实例
     */
    public Fenix orLike(String field, Object value, boolean match) {
        return this.doLike(SymbolConst.OR, field, value, match, true);
    }

    /**
     * 生成 " NOT LIKE " 模糊查询的 SQL 片段.
     *
     * <p>示例：传入 {"b.title", "Spring"} 两个参数，生成的SQL片段为：" b.title NOT LIKE :b_title ",
     *      SQL参数为:{b_title: "%Spring%"}.</p>
     *
     * @param field 数据库字段
     * @param value 值
     * @return {@link Fenix} 实例
     */
    public Fenix notLike(String field, Object value) {
        return this.doLike(SqlKeyConst.SPACE, field, value, true, false);
    }

    /**
     * 生成 " NOT LIKE " 模糊查询的 SQL 片段,如果 match 为 true 时则生成该条 SQL 片段，否则不生成.
     *
     * <p>示例：传入 {"b.title", "Spring", true} 三个参数，生成的SQL片段为：" b.title NOT LIKE :b_title ",
     *      SQL参数为:{b_title: "%Spring%"}.</p>
     *
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return {@link Fenix} 实例
     */
    public Fenix notLike(String field, Object value, boolean match) {
        return this.doLike(SqlKeyConst.SPACE, field, value, match, false);
    }

    /**
     * 生成带 " AND " 前缀的 " NOT LIKE " 模糊查询的 SQL 片段.
     *
     * <p>示例：传入 {"b.title", "Spring"} 两个参数，生成的SQL片段为：" AND b.title NOT LIKE :b_title ",
     *      SQL参数为:{b_title: "%Spring%"}.</p>
     *
     * @param field 数据库字段
     * @param value 值
     * @return {@link Fenix} 实例
     */
    public Fenix andNotLike(String field, Object value) {
        return this.doLike(SymbolConst.AND, field, value, true, false);
    }

    /**
     * 生成带 " AND " 前缀的 " NOT LIKE " 模糊查询的 SQL 片段,如果 match 为 true 时则生成该条 SQL 片段，否则不生成.
     *
     * <p>示例：传入 {"b.title", "Spring", true} 三个参数，生成的SQL片段为：" AND b.title NOT LIKE :b_title ",
     *      SQL参数为:{b_title: "%Spring%"}.</p>
     *
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return {@link Fenix} 实例
     */
    public Fenix andNotLike(String field, Object value, boolean match) {
        return this.doLike(SymbolConst.AND, field, value, match, false);
    }

    /**
     * 生成带 " OR " 前缀的" NOT LIKE "模糊查询的 SQL 片段.
     *
     * <p>示例：传入 {"b.title", "Spring"} 两个参数，生成的SQL片段为：" OR b.title NOT LIKE :b_title ",
     *      SQL参数为:{b_title: "%Spring%"}.</p>
     *
     * @param field 数据库字段
     * @param value 值
     * @return {@link Fenix} 实例
     */
    public Fenix orNotLike(String field, Object value) {
        return this.doLike(SymbolConst.OR, field, value, true, false);
    }

    /**
     * 生成带 " OR " 前缀的" NOT LIKE "模糊查询的 SQL 片段,如果 match 为 true 时则生成该条 SQL 片段，否则不生成.
     * <p>示例：传入 {"b.title", "Spring", true} 三个参数，生成的SQL片段为：" OR b.title NOT LIKE :b_title ",
     *      SQL参数为:{b_title: "%Spring%"}.</p>
     *
     * @param field 数据库字段
     * @param value 值
     * @param match 是否匹配
     * @return {@link Fenix} 实例
     */
    public Fenix orNotLike(String field, Object value, boolean match) {
        return this.doLike(SymbolConst.OR, field, value, match, false);
    }

    /**
     * 根据指定的模式字符串生成 LIKE 模糊查询的 SQL 片段.
     *
     * <p>示例：传入 {"b.title", "Java%"} 两个参数，生成的SQL片段为：" b.title LIKE 'Java%' "</p>
     *
     * @param field 数据库字段
     * @param pattern 模式字符串
     * @return {@link Fenix} 实例
     */
    public Fenix likePattern(String field, String pattern) {
        return this.doLikePattern(SqlKeyConst.SPACE, field, pattern, true, true);
    }

    /**
     * 根据指定的模式字符串生成 LIKE 模糊查询的 SQL 片段,如果 match 为 true 时则生成该条 SQL 片段，否则不生成.
     *
     * <p>示例：传入 {"b.title", "Java%", true} 三个参数，生成的SQL片段为：" b.title LIKE 'Java%' "</p>
     * <p>示例：传入 {"b.title", "Java%", false} 三个参数，生成的SQL片段为空字符串.</p>
     *
     * @param field 数据库字段
     * @param pattern 模式字符串
     * @param match 是否匹配
     * @return {@link Fenix} 实例
     */
    public Fenix likePattern(String field, String pattern, boolean match) {
        return this.doLikePattern(SqlKeyConst.SPACE, field, pattern, match, true);
    }

    /**
     * 根据指定的模式字符串生成带 " AND " 前缀的 LIKE 模糊查询的 SQL 片段.
     *
     * <p>示例：传入 {"b.title", "Java%"} 两个参数，生成的SQL片段为：" AND b.title LIKE 'Java%' "</p>
     *
     * @param field 数据库字段
     * @param pattern 模式字符串
     * @return {@link Fenix} 实例
     */
    public Fenix andLikePattern(String field, String pattern) {
        return this.doLikePattern(SymbolConst.AND, field, pattern, true, true);
    }

    /**
     * 根据指定的模式字符串生成带 " AND " 前缀的 LIKE 模糊查询的 SQL 片段,如果 match 为 true 时则生成该条 SQL 片段，否则不生成.
     *
     * <p>示例：传入 {"b.title", "Java%", true} 三个参数，生成的SQL片段为：" AND b.title LIKE 'Java%' "</p>
     * <p>示例：传入 {"b.title", "Java%", false} 三个参数，生成的SQL片段为空字符串.</p>
     *
     * @param field 数据库字段
     * @param pattern 模式字符串
     * @param match 是否匹配
     * @return {@link Fenix} 实例
     */
    public Fenix andLikePattern(String field, String pattern, boolean match) {
        return this.doLikePattern(SymbolConst.AND, field, pattern, match, true);
    }

    /**
     * 根据指定的模式字符串生成带 " OR " 前缀的 LIKE 模糊查询的 SQL 片段.
     *
     * <p>示例：传入 {"b.title", "Java%"} 两个参数，生成的SQL片段为：" OR b.title LIKE 'Java%' "</p>
     *
     * @param field 数据库字段
     * @param pattern 模式字符串
     * @return {@link Fenix} 实例
     */
    public Fenix orLikePattern(String field, String pattern) {
        return this.doLikePattern(SymbolConst.OR, field, pattern, true, true);
    }

    /**
     * 根据指定的模式字符串生成带 " OR " 前缀的 LIKE 模糊查询的 SQL 片段,如果 match 为 true 时则生成该条 SQL 片段，否则不生成.
     *
     * <p>示例：传入 {"b.title", "Java%", true} 三个参数，生成的SQL片段为：" OR b.title LIKE 'Java%' "</p>
     * <p>示例：传入 {"b.title", "Java%", false} 三个参数，生成的SQL片段为空字符串.</p>
     *
     * @param field 数据库字段
     * @param pattern 模式字符串
     * @param match 是否匹配
     * @return {@link Fenix} 实例
     */
    public Fenix orLikePattern(String field, String pattern, boolean match) {
        return this.doLikePattern(SymbolConst.OR, field, pattern, match, true);
    }

    /**
     * 根据指定的模式字符串生成 " NOT LIKE " 模糊查询的 SQL 片段.
     *
     * <p>示例：传入 {"b.title", "Java%"} 两个参数，生成的SQL片段为：" b.title NOT LIKE 'Java%' "</p>
     *
     * @param field 数据库字段
     * @param pattern 模式字符串
     * @return {@link Fenix} 实例
     */
    public Fenix notLikePattern(String field, String pattern) {
        return this.doLikePattern(SqlKeyConst.SPACE, field, pattern, true, false);
    }

    /**
     * 根据指定的模式字符串生成 " NOT LIKE " 模糊查询的 SQL 片段,如果 match 为 true 时则生成该条 SQL 片段，否则不生成.
     *
     * <p>示例：传入 {"b.title", "Java%", true} 三个参数，生成的SQL片段为：" b.title NOT LIKE 'Java%' "</p>
     * <p>示例：传入 {"b.title", "Java%", false} 三个参数，生成的SQL片段为空字符串.</p>
     *
     * @param field 数据库字段
     * @param pattern 模式字符串
     * @param match 是否匹配
     * @return {@link Fenix} 实例
     */
    public Fenix notLikePattern(String field, String pattern, boolean match) {
        return this.doLikePattern(SqlKeyConst.SPACE, field, pattern, match, false);
    }

    /**
     * 根据指定的模式字符串生成带 " AND " 前缀的 " NOT LIKE " 模糊查询的 SQL 片段.
     *
     * <p>示例：传入 {"b.title", "Java%"} 两个参数，生成的SQL片段为：" AND b.title NOT LIKE 'Java%' "</p>
     *
     * @param field 数据库字段
     * @param pattern 模式字符串
     * @return {@link Fenix} 实例
     */
    public Fenix andNotLikePattern(String field, String pattern) {
        return this.doLikePattern(SymbolConst.AND, field, pattern, true, false);
    }

    /**
     * 根据指定的模式字符串生成带 " AND " 前缀的 " NOT LIKE " 模糊查询的 SQL 片段,如果 match 为 true 时则生成该条 SQL 片段，否则不生成.
     *
     * <p>示例：传入 {"b.title", "Java%", true} 三个参数，生成的SQL片段为：" AND b.title NOT LIKE 'Java%' "</p>
     * <p>示例：传入 {"b.title", "Java%", false} 三个参数，生成的SQL片段为空字符串.</p>
     *
     * @param field 数据库字段
     * @param pattern 模式字符串
     * @param match 是否匹配
     * @return {@link Fenix} 实例
     */
    public Fenix andNotLikePattern(String field, String pattern, boolean match) {
        return this.doLikePattern(SymbolConst.AND, field, pattern, match, false);
    }

    /**
     * 根据指定的模式字符串生成带 " OR " 前缀的 " NOT LIKE " 模糊查询的 SQL 片段.
     *
     * <p>示例：传入 {"b.title", "Java%"} 两个参数，生成的SQL片段为：" OR b.title NOT LIKE 'Java%' "</p>
     *
     * @param field 数据库字段
     * @param pattern 模式字符串
     * @return {@link Fenix} 实例
     */
    public Fenix orNotLikePattern(String field, String pattern) {
        return this.doLikePattern(SymbolConst.OR, field, pattern, true, false);
    }

    /**
     * 根据指定的模式字符串生成带 " OR " 前缀的 " NOT LIKE " 模糊查询的 SQL 片段,如果 match 为 true 时则生成该条 SQL 片段，否则不生成.
     *
     * <p>示例：传入 {"b.title", "Java%", true} 三个参数，生成的SQL片段为：" OR b.title NOT LIKE 'Java%' "</p>
     * <p>示例：传入 {"b.title", "Java%", false} 三个参数，生成的SQL片段为空字符串.</p>
     *
     * @param field 数据库字段
     * @param pattern 模式字符串
     * @param match 是否匹配
     * @return {@link Fenix} 实例
     */
    public Fenix orNotLikePattern(String field, String pattern, boolean match) {
        return this.doLikePattern(SymbolConst.OR, field, pattern, match, false);
    }

    /**
     * 生成 BETWEEN 区间查询的 SQL 片段(当某一个值为 null 时，会是大于等于或小于等于的情形).
     *
     * @param field 数据库字段
     * @param startValue 开始值
     * @param endValue 结束值
     * @return {@link Fenix} 实例
     */
    public Fenix between(String field, Object startValue, Object endValue) {
        return this.doBetween(SqlKeyConst.SPACE, field, startValue, endValue, true);
    }

    /**
     * 生成 BETWEEN 区间查询的 SQL 片段,如果 match 为 true 时则生成该条 SQL 片段，否则不生成(当某一个值为 null 时，会是大于等于或小于等于的情形).
     *
     * @param field 数据库字段
     * @param startValue 开始值
     * @param endValue 结束值
     * @param match 是否匹配
     * @return {@link Fenix} 实例
     */
    public Fenix between(String field, Object startValue, Object endValue, boolean match) {
        return this.doBetween(SqlKeyConst.SPACE, field, startValue, endValue, match);
    }

    /**
     * 生成带 " AND " 前缀的 BETWEEN 区间查询的 SQL 片段(当某一个值为 null 时，会是大于等于或小于等于的情形).
     *
     * @param field 数据库字段
     * @param startValue 开始值
     * @param endValue 结束值
     * @return {@link Fenix} 实例
     */
    public Fenix andBetween(String field, Object startValue, Object endValue) {
        return this.doBetween(SymbolConst.AND, field, startValue, endValue, true);
    }

    /**
     * 生成带 " AND " 前缀的 BETWEEN 区间查询的 SQL 片段,如果 match 为 true 时则生成该条SQL片段，
     * 否则不生成(当某一个值为 null 时，会是大于等于或小于等于的情形).
     *
     * @param field 数据库字段
     * @param startValue 开始值
     * @param endValue 结束值
     * @param match 是否匹配
     * @return {@link Fenix} 实例
     */
    public Fenix andBetween(String field, Object startValue, Object endValue, boolean match) {
        return this.doBetween(SymbolConst.AND, field, startValue, endValue, match);
    }

    /**
     * 生成带 " OR " 前缀的 BETWEEN 区间查询的 SQL 片段(当某一个值为 null 时，会是大于等于或小于等于的情形).
     *
     * @param field 数据库字段
     * @param startValue 开始值
     * @param endValue 结束值
     * @return {@link Fenix} 实例
     */
    public Fenix orBetween(String field, Object startValue, Object endValue) {
        return this.doBetween(SymbolConst.OR, field, startValue, endValue, true);
    }

    /**
     * 生成带 " OR " 前缀的 BETWEEN 区间查询的 SQL 片段,如果 match 为 true 时则生成该条SQL片段，
     * 否则不生成(当某一个值为 null 时，会是大于等于或小于等于的情形).
     *
     * @param field 数据库字段
     * @param startValue 开始值
     * @param endValue 结束值
     * @param match 是否匹配
     * @return {@link Fenix} 实例
     */
    public Fenix orBetween(String field, Object startValue, Object endValue, boolean match) {
        return this.doBetween(SymbolConst.OR, field, startValue, endValue, match);
    }

    /**
     * 生成 IN 范围查询的 SQL 片段.
     * 
     * @param field 数据库字段
     * @param values 数组的值
     * @return {@link Fenix} 实例
     */
    public Fenix in(String field, Object[] values) {
        return this.doIn(SqlKeyConst.SPACE, field, values, true, true);
    }

    /**
     * 生成 IN 范围查询的 SQL 片段,如果 match 为 true 时则生成该条 SQL 片段，否则不生成.
     *
     * @param field 数据库字段
     * @param values 数组的值
     * @param match 是否匹配
     * @return {@link Fenix} 实例
     */
    public Fenix in(String field, Object[] values, boolean match) {
        return this.doIn(SqlKeyConst.SPACE, field, values, match, true);
    }

    /**
     * 生成 IN 范围查询的 SQL 片段.
     *
     * @param field 数据库字段
     * @param values 集合的值
     * @return {@link Fenix} 实例
     */
    public Fenix in(String field, Collection<?> values) {
        return this.doIn(SqlKeyConst.SPACE, field, values, true, true);
    }

    /**
     * 生成 IN 范围查询的 SQL 片段,如果 match 为 true 时则生成该条 SQL 片段，否则不生成.
     * 
     * @param field 数据库字段
     * @param values 集合的值
     * @param match 是否匹配
     * @return {@link Fenix} 实例
     */
    public Fenix in(String field, Collection<?> values, boolean match) {
        return this.doIn(SqlKeyConst.SPACE, field, values, match, true);
    }

    /**
     * 生成带 " AND " 前缀的 IN 范围查询的 SQL 片段.
     * 
     * @param field 数据库字段
     * @param values 数组的值
     * @return {@link Fenix} 实例
     */
    public Fenix andIn(String field, Object[] values) {
        return this.doIn(SymbolConst.AND, field, values, true, true);
    }

    /**
     * 生成带 " AND " 前缀的 IN 范围查询的 SQL 片段,如果 match 为 true 时则生成该条 SQL 片段，否则不生成.
     *
     * @param field 数据库字段
     * @param values 数组的值
     * @param match 是否匹配
     * @return {@link Fenix} 实例
     */
    public Fenix andIn(String field, Object[] values, boolean match) {
        return this.doIn(SymbolConst.AND, field, values, match, true);
    }

    /**
     * 生成带 " AND " 前缀的 IN 范围查询的 SQL 片段.
     *
     * @param field 数据库字段
     * @param values 集合的值
     * @return {@link Fenix} 实例
     */
    public Fenix andIn(String field, Collection<?> values) {
        return this.doIn(SymbolConst.AND, field, values, true, true);
    }

    /**
     * 生成带 " AND " 前缀的 IN 范围查询的 SQL 片段,如果 match 为 true 时则生成该条 SQL 片段，否则不生成.
     *
     * @param field 数据库字段
     * @param values 集合的值
     * @param match 是否匹配
     * @return {@link Fenix} 实例
     */
    public Fenix andIn(String field, Collection<?> values, boolean match) {
        return this.doIn(SymbolConst.AND, field, values, match, true);
    }

    /**
     * 生成带 " OR " 前缀的 IN 范围查询的 SQL 片段.
     * @param field 数据库字段
     * @param values 数组的值
     * @return {@link Fenix} 实例
     */
    public Fenix orIn(String field, Object[] values) {
        return this.doIn(SymbolConst.OR, field, values, true, true);
    }

    /**
     * 生成带 " OR " 前缀的 IN 范围查询的 SQL 片段,如果 match 为 true 时则生成该条 SQL 片段，否则不生成.
     *
     * @param field 数据库字段
     * @param values 数组的值
     * @param match 是否匹配
     * @return {@link Fenix} 实例
     */
    public Fenix orIn(String field, Object[] values, boolean match) {
        return this.doIn(SymbolConst.OR, field, values, match, true);
    }

    /**
     * 生成带 " OR " 前缀的 IN 范围查询的 SQL 片段.
     *
     * @param field 数据库字段
     * @param values 集合的值
     * @return {@link Fenix} 实例
     */
    public Fenix orIn(String field, Collection<?> values) {
        return this.doIn(SymbolConst.OR, field, values, true, true);
    }

    /**
     * 生成带 " OR " 前缀的 IN 范围查询的 SQL 片段,如果 match 为 true 时则生成该条 SQL 片段，否则不生成.
     *
     * @param field 数据库字段
     * @param values 集合的值
     * @param match 是否匹配
     * @return {@link Fenix} 实例
     */
    public Fenix orIn(String field, Collection<?> values, boolean match) {
        return this.doIn(SymbolConst.OR, field, values, match, true);
    }

    /**
     * 生成 " NOT IN " 范围查询的 SQL 片段.
     * 
     * @param field 数据库字段
     * @param values 数组的值
     * @return {@link Fenix} 实例
     */
    public Fenix notIn(String field, Object[] values) {
        return this.doIn(SqlKeyConst.SPACE, field, values, true, false);
    }

    /**
     * 生成 " NOT IN " 范围查询的 SQL 片段,如果 match 为 true 时则生成该条 SQL 片段，否则不生成.
     *
     * @param field 数据库字段
     * @param values 数组的值
     * @param match 是否匹配
     * @return {@link Fenix} 实例
     */
    public Fenix notIn(String field, Object[] values, boolean match) {
        return this.doIn(SqlKeyConst.SPACE, field, values, match, false);
    }

    /**
     * 生成 " NOT IN " 范围查询的 SQL 片段.
     *
     * @param field 数据库字段
     * @param values 集合的值
     * @return {@link Fenix} 实例
     */
    public Fenix notIn(String field, Collection<?> values) {
        return this.doIn(SqlKeyConst.SPACE, field, values, true, false);
    }

    /**
     * 生成 " NOT IN " 范围查询的 SQL 片段,如果 match 为 true 时则生成该条 SQL 片段，否则不生成.
     *
     * @param field 数据库字段
     * @param values 集合的值
     * @param match 是否匹配
     * @return {@link Fenix} 实例
     */
    public Fenix notIn(String field, Collection<?> values, boolean match) {
        return this.doIn(SqlKeyConst.SPACE, field, values, match, false);
    }

    /**
     * 生成带 " AND " 前缀的 " NOT IN " 范围查询的 SQL 片段.
     *
     * @param field 数据库字段
     * @param values 数组的值
     * @return {@link Fenix} 实例
     */
    public Fenix andNotIn(String field, Object[] values) {
        return this.doIn(SymbolConst.AND, field, values, true, false);
    }

    /**
     * 生成带 " AND " 前缀的 " NOT IN " 范围查询的 SQL 片段,如果 match 为 true 时则生成该条 SQL 片段，否则不生成.
     *
     * @param field 数据库字段
     * @param values 数组的值
     * @param match 是否匹配
     * @return {@link Fenix} 实例
     */
    public Fenix andNotIn(String field, Object[] values, boolean match) {
        return this.doIn(SymbolConst.AND, field, values, match, false);
    }

    /**
     * 生成带 " AND " 前缀的 " NOT IN " 范围查询的 SQL 片段.
     *
     * @param field 数据库字段
     * @param values 集合的值
     * @return {@link Fenix} 实例
     */
    public Fenix andNotIn(String field, Collection<?> values) {
        return this.doIn(SymbolConst.AND, field, values, true, false);
    }

    /**
     * 生成带 " AND " 前缀的 " NOT IN " 范围查询的 SQL 片段,如果 match 为 true 时则生成该条 SQL 片段，否则不生成.
     *
     * @param field 数据库字段
     * @param values 集合的值
     * @param match 是否匹配
     * @return {@link Fenix} 实例
     */
    public Fenix andNotIn(String field, Collection<?> values, boolean match) {
        return this.doIn(SymbolConst.AND, field, values, match, false);
    }

    /**
     * 生成带 " OR " 前缀的 " NOT IN " 范围查询的 SQL 片段.
     *
     * @param field 数据库字段
     * @param values 数组的值
     * @return {@link Fenix} 实例
     */
    public Fenix orNotIn(String field, Object[] values) {
        return this.doIn(SymbolConst.OR, field, values, true, false);
    }

    /**
     * 生成带 " OR " 前缀的 " NOT IN " 范围查询的 SQL 片段,如果 match 为 true 时则生成该条 SQL 片段，否则不生成.
     *
     * @param field 数据库字段
     * @param values 数组的值
     * @param match 是否匹配
     * @return {@link Fenix} 实例
     */
    public Fenix orNotIn(String field, Object[] values, boolean match) {
        return this.doIn(SymbolConst.OR, field, values, match, false);
    }

    /**
     * 生成带 " OR " 前缀的 " NOT IN " 范围查询的 SQL 片段.
     *
     * @param field 数据库字段
     * @param values 集合的值
     * @return {@link Fenix} 实例
     */
    public Fenix orNotIn(String field, Collection<?> values) {
        return this.doIn(SymbolConst.OR, field, values, true, false);
    }

    /**
     * 生成带 " OR " 前缀的 " NOT IN " 范围查询的 SQL 片段,如果 match 为 true 时则生成该条 SQL 片段，否则不生成.
     *
     * @param field 数据库字段
     * @param values 集合的值
     * @param match 是否匹配
     * @return {@link Fenix} 实例
     */
    public Fenix orNotIn(String field, Collection<?> values, boolean match) {
        return this.doIn(SymbolConst.OR, field, values, match, false);
    }

    /**
     * 生成 " IS NULL " 的 SQL 片段.
     *
     * <p>示例：传入 {"a.name"} 参数，生成的 SQL 片段为：" a.name IS NULL "</p>
     *
     * @param field 数据库字段
     * @return {@link Fenix} 实例
     */
    public Fenix isNull(String field) {
        return this.doIsNull(SqlKeyConst.SPACE, field, true,true);
    }

    /**
     * 生成 " IS NULL " 的 SQL 片段,如果 match 为 true 时则生成该条 SQL 片段，否则不生成.
     *
     * <p>示例：传入 {"a.name", true} 两个参数，生成的SQL片段为：" a.name IS NULL "</p>
     *
     * @param field 数据库字段
     * @param match 是否匹配
     * @return {@link Fenix} 实例
     */
    public Fenix isNull(String field, boolean match) {
        return this.doIsNull(SqlKeyConst.SPACE, field, match,true);
    }

    /**
     * 生成带 " AND " 前缀 " IS NULL " 的 SQL 片段.
     *
     * <p>示例：传入 {"a.name"} 参数，生成的SQL片段为：" AND a.name IS NULL "</p>
     *
     * @param field 数据库字段
     * @return {@link Fenix} 实例
     */
    public Fenix andIsNull(String field) {
        return this.doIsNull(SymbolConst.AND, field, true,true);
    }

    /**
     * 生成带 " AND " 前缀 " IS NULL " 的 SQL 片段,如果 match 为 true 时则生成该条 SQL 片段，否则不生成.
     *
     * <p>示例：传入 {"a.name", true} 两个参数，生成的SQL片段为：" AND a.name IS NULL "</p>
     *
     * @param field 数据库字段
     * @param match 是否匹配
     * @return {@link Fenix} 实例
     */
    public Fenix andIsNull(String field, boolean match) {
        return this.doIsNull(SymbolConst.AND, field, match,true);
    }

    /**
     * 生成带 " OR " 前缀 " IS NULL " 的 SQL 片段.
     *
     * <p>示例：传入 {"a.name"} 参数，生成的SQL片段为：" OR a.name IS NULL "</p>
     *
     * @param field 数据库字段
     * @return {@link Fenix} 实例
     */
    public Fenix orIsNull(String field) {
        return this.doIsNull(SymbolConst.OR, field, true,true);
    }

    /**
     * 生成带 " OR " 前缀" IS NULL "的 SQL 片段,如果 match 为 true 时则生成该条 SQL 片段，否则不生成.
     *
     * <p>示例：传入 {"a.name", true} 两个参数，生成的SQL片段为：" OR a.name IS NULL "</p>
     *
     * @param field 数据库字段
     * @param match 是否匹配
     * @return {@link Fenix} 实例
     */
    public Fenix orIsNull(String field, boolean match) {
        return this.doIsNull(SymbolConst.OR, field, match,true);
    }

    /**
     * 生成 " IS NOT NULL " 的 SQL 片段.
     *
     * <p>示例：传入 {"a.name"} 参数，生成的SQL片段为：" a.name IS NOT NULL "</p>
     *
     * @param field 数据库字段
     * @return {@link Fenix} 实例
     */
    public Fenix isNotNull(String field) {
        return this.doIsNull(SqlKeyConst.SPACE, field, true,false);
    }

    /**
     * 生成 " IS NOT NULL " 的 SQL 片段,如果 match 为 true 时则生成该条 SQL 片段，否则不生成.
     *
     * <p>示例：传入 {"a.name", true} 两个参数，生成的SQL片段为：" a.name IS NOT NULL "</p>
     *
     * @param field 数据库字段
     * @param match 是否匹配
     * @return {@link Fenix} 实例
     */
    public Fenix isNotNull(String field, boolean match) {
        return this.doIsNull(SqlKeyConst.SPACE, field, match,false);
    }

    /**
     * 生成带 " AND " 前缀 " IS NOT NULL " 的 SQL 片段.
     *
     * <p>示例：传入 {"a.name"} 参数，生成的SQL片段为：" AND a.name IS NOT NULL "</p>
     *
     * @param field 数据库字段
     * @return {@link Fenix} 实例
     */
    public Fenix andIsNotNull(String field) {
        return this.doIsNull(SymbolConst.AND, field, true,false);
    }

    /**
     * 生成带 " AND " 前缀 " IS NOT NULL " 的 SQL 片段,如果 match 为 true 时则生成该条 SQL 片段，否则不生成.
     *
     * <p>示例：传入 {"a.name", true} 两个参数，生成的SQL片段为：" AND a.name IS NOT NULL "</p>
     *
     * @param field 数据库字段
     * @param match 是否匹配
     * @return {@link Fenix} 实例
     */
    public Fenix andIsNotNull(String field, boolean match) {
        return this.doIsNull(SymbolConst.AND, field, match,false);
    }

    /**
     * 生成带 " OR " 前缀 " IS NOT NULL " 的 SQL 片段.
     *
     * <p>示例：传入 {"a.name"} 参数，生成的SQL片段为：" OR a.name IS NOT NULL "</p>
     *
     * @param field 数据库字段
     * @return {@link Fenix} 实例
     */
    public Fenix orIsNotNull(String field) {
        return this.doIsNull(SymbolConst.OR, field, true,false);
    }

    /**
     * 生成带 " OR " 前缀 " IS NOT NULL " 的 SQL 片段,如果 match 为 true 时则生成该条 SQL 片段，否则不生成.
     *
     * <p>示例：传入 {"a.name", true} 两个参数，生成的SQL片段为：" OR a.name IS NOT NULL "</p>
     *
     * @param field 数据库字段
     * @param match 是否匹配
     * @return {@link Fenix} 实例
     */
    public Fenix orIsNotNull(String field, boolean match) {
        return this.doIsNull(SymbolConst.OR, field, match,false);
    }

}
