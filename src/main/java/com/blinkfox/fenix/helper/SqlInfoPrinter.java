package com.blinkfox.fenix.helper;

import com.blinkfox.fenix.bean.SqlInfo;

import com.blinkfox.fenix.consts.Const;
import lombok.extern.slf4j.Slf4j;

/**
 * 打印 {@link com.blinkfox.fenix.bean.SqlInfo} 实体信息的打印工具类.
 *
 * @author blinkfox on 2019-08-05.
 */
@Slf4j
public final class SqlInfoPrinter {

    private static final String PRINT_START = "------------------------------------------------------------ "
            + "Fenix 生成的 SQL 信息 ---------------------------------------------------------";

    private static final String PRINT_END = "---------------------------------------------------------------"
            + "----------------------------------------------------------------------------";

    /**
     * 换行符.
     */
    private static final String LINE_BREAK = "\n";

    /**
     * 拼接打印信息的 {@link StringBuilder} 实例.
     */
    private StringBuilder builder;

    /**
     * 默认构造方法.
     */
    public SqlInfoPrinter() {
        this.builder = new StringBuilder(LINE_BREAK).append(PRINT_START).append(LINE_BREAK);
    }

    /**
     * 使用 info 日志级别来打印 {@link SqlInfo} 对象中重要属性信息.
     *
     * <p>如果配置了要打印 SQL 信息，且日志级别是info级别,则打印SQL信息.</p>
     *
     * @param sqlInfo 要打印的 SqlInfo 对象
     */
    public void print(SqlInfo sqlInfo) {
        log.info(this.buildSqlAndParams(sqlInfo).builder.toString());
    }

    /**
     * 使用 info 日志级别来打印 {@link SqlInfo} 对象中重要属性信息，本方法还需要额外打印 XML 相关的信息.
     *
     * @param sqlInfo 要打印的 SqlInfo 对象
     * @param namespace XML 命名空间
     * @param fenixId XML 中的 fenixId
     */
    public void print(SqlInfo sqlInfo, String namespace, String fenixId) {
        log.info(this.buildXmlInfo(namespace, fenixId).buildSqlAndParams(sqlInfo).builder.toString());
    }

    /**
     * 构建 XML 相关的信息到 builder 中.
     */
    private SqlInfoPrinter buildXmlInfo(String namespace, String fenixId) {
        builder.append("-- Fenix xml: ")
                .append(namespace).append(Const.DOT).append(fenixId).append(LINE_BREAK);
        return this;
    }

    /**
     * 构建 SQL 语句和参数信息到 builder 中.
     */
    private SqlInfoPrinter buildSqlAndParams(SqlInfo sqlInfo) {
        builder.append("-------- SQL: ")
                .append(sqlInfo.getSql()).append(LINE_BREAK)
                .append("----- Params: ").append(sqlInfo.getParams().toString()).append(LINE_BREAK)
                .append(PRINT_END).append(LINE_BREAK);
        return this;
    }

}
