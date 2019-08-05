package com.blinkfox.fenix.helper;

import com.blinkfox.fenix.bean.SqlInfo;
import com.blinkfox.fenix.config.entity.NormalConfig;
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
     * 打印SqlInfo的日志信息.
     * @param nameSpace XML命名空间
     * @param zealotId XML中的zealotId
     * @param sqlInfo 要打印的SqlInfo对象
     * @param hasXml 是否包含xml的打印信息
     */
    public void printZealotSqlInfo(SqlInfo sqlInfo, boolean hasXml, String nameSpace, String zealotId) {
        // 如果可以配置的打印SQL信息，且日志级别是info级别,则打印SQL信息.
        if (NormalConfig.getInstance().isPrintSqlInfo()) {
            StringBuilder sb = new StringBuilder(LINE_BREAK);
            sb.append(PRINT_START).append(LINE_BREAK);

            // 如果是xml版本的SQL，则打印xml的相关信息.
            if (hasXml) {
                sb.append("--zealot xml: ").append(XmlContext.getInstance().getXmlPathMap().get(nameSpace))
                        .append(" -> ").append(zealotId).append(LINE_BREAK);
            }

            sb.append("-------- SQL: ").append(sqlInfo.getSql()).append(LINE_BREAK)
                    .append("----- Params: ").append(sqlInfo.getParams().toString()).append(LINE_BREAK)
                    .append(PRINT_END).append(LINE_BREAK);
            log.info(sb.toString());
        }
    }

}