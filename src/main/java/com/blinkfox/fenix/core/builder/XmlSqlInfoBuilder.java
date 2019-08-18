package com.blinkfox.fenix.core.builder;

import com.blinkfox.fenix.bean.BuildSource;
import com.blinkfox.fenix.bean.SqlInfo;
import com.blinkfox.fenix.exception.FenixException;
import com.blinkfox.fenix.helper.ParseHelper;
import com.blinkfox.fenix.helper.StringHelper;

import java.util.Arrays;
import java.util.Map;

/**
 * 构建使用 XML 拼接 JPQL 或者 SQL 片段的构建器.
 *
 * @author blinkfox on 2019-08-06.
 */
public final class XmlSqlInfoBuilder extends SqlInfoBuilder {

    /**
     * 构造方法.
     *
     * @param source 构建资源
     */
    public XmlSqlInfoBuilder(BuildSource source) {
        super(source);
    }

    /**
     * 通过计算 XML 中 value 属性的值来追加构建常规 SQL 片段需要的 {@link SqlInfo} 信息.
     *
     * @param fieldText 字段文本值
     * @param valueText 参数值
     */
    public void buildNormalSql(String fieldText, String valueText) {
        super.buildNormalSql(fieldText, valueText, ParseHelper.parseExpressWithException(valueText, context));
    }

    /**
     * 追加构建 'LIKE' 模糊查询的 {@link SqlInfo} 信息.
     *
     * @param fieldText   字段文本值
     * @param valueText   参数值
     * @param patternText 模式字符串文本
     */
    public void buildLikeSql(String fieldText, String valueText, String patternText) {
        if (StringHelper.isNotBlank(valueText) && StringHelper.isBlank(patternText)) {
            super.buildLikeSql(fieldText, valueText, ParseHelper.parseExpressWithException(valueText, context));
        } else if (StringHelper.isBlank(valueText) && StringHelper.isNotBlank(patternText)) {
            super.buildLikePatternSql(fieldText, patternText);
        } else {
            throw new FenixException("【Fenix 异常】<like /> 相关的标签中，【value】属性和【pattern】属性不能同时为空或者同时不为空！");
        }
    }

    /**
     * 追加构建 'BETWEEN ? AND ?'、'大于等于'、'小于等于' 的区间查询的 {@link SqlInfo} 信息.
     *
     * @param fieldText 字段文本值
     * @param startText 开始文本
     * @param endText   结束文本
     */
    public void buildBetweenSql(String fieldText, String startText, String endText) {
        super.buildBetweenSql(fieldText, startText, ParseHelper.parseExpress(startText, context),
                endText, ParseHelper.parseExpress(endText, context));
    }

    /**
     * 追加构建 'IN' 的范围查询的 {@link SqlInfo} 信息.
     *
     * <p>获取 value 的值，判断是否为空，若为空，则不做处理.</p>
     *
     * @param fieldText 字段文本值
     * @param valueText IN 所要查找的范围文本值
     */
    public void buildInSql(String fieldText, String valueText) {
        Object obj = ParseHelper.parseExpressWithException(valueText, context);
        if (obj != null) {
            super.buildInSql(fieldText, valueText, obj);
        }
    }

    /**
     * 追加构建 'TEXT' 的文本标签中参数的 {@link SqlInfo} 信息.
     *
     * <p>注：value 的类型必须是 Map 类型的，否则将抛出 {@link FenixException} 异常；
     *      且 Map 中的 key 必须是“死”字符串，value 的值才可以被动态解析.</p>
     *
     * @param valueText value 文本值
     */
    @SuppressWarnings("unchecked")
    public void buildTextSqlParams(String valueText) {
        // 获取 value 值为空，则直接退出本方法.
        Object obj;
        if (StringHelper.isBlank(valueText)
                || (obj = ParseHelper.parseExpressWithException(valueText, context)) == null) {
            return;
        }

        // 如果这个对象实例是 Map 类型的，才能进行后续设置参数的操作，否则抛出异常！
        if (!(obj instanceof Map)) {
            throw new FenixException("【Fenix 异常提示】<text /> 标签中 value 值的类型不是 Map 类型，请检查！");
        }

        // Map 的 key 是 JPQL 的命名参数，是死字符串，value 是 JPQL 中对应的参数值，会被动态解析.
        Map<String, Object> params = super.sqlInfo.getParams();
        for (Map.Entry<String, Object> entry : ((Map<String, Object>) obj).entrySet()) {
            // 如果 value 是数组，就需要转换成集合，否则 JPA 的执行会报错.
            Object value = entry.getValue();
            params.put(entry.getKey(),
                    value != null && value.getClass().isArray() ? Arrays.asList((Object[]) value) : value);
        }
    }

}
