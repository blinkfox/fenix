package com.blinkfox.fenix.helper;

import com.blinkfox.fenix.bean.SqlInfo;
import com.blinkfox.fenix.config.entity.XmlContext;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 打印 {@link com.blinkfox.fenix.bean.SqlInfo} 实体信息的打印工具类.
 *
 * @author blinkfox on 2019-08-05.
 */
@Slf4j
@NoArgsConstructor
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
     * 使用 info 日志级别来打印 {@link SqlInfo} 对象中重要属性信息.
     *
     * @param sqlInfo 要打印的 SqlInfo 对象
     * @param hasXml 是否包含 xml 的打印信息
     * @param namespace XML 命名空间
     * @param fenixId XML 中的 fenixId
     */
    public void print(SqlInfo sqlInfo, boolean hasXml, String namespace, String fenixId) {
        // 如果可以配置的打印SQL信息，且日志级别是info级别,则打印SQL信息.
        StringBuilder sb = new StringBuilder(LINE_BREAK).append(PRINT_START).append(LINE_BREAK);

        // 如果是 XML 版本的SQL，则打印 XML 的相关信息.
        if (hasXml) {
            sb.append("-- Fenix xml: ")
                    .append(XmlContext.getInstance().getXmlPathMap().get(namespace))
                    .append(" -> ").append(fenixId).append(LINE_BREAK);
        }

        // 打印 SQL 的语句和参数.
        sb.append("-------- SQL: ")
                .append(sqlInfo.getSql()).append(LINE_BREAK)
                .append("----- Params: ").append(sqlInfo.getParams().toString()).append(LINE_BREAK)
                .append(PRINT_END).append(LINE_BREAK);
        log.info(sb.toString());
    }

}
