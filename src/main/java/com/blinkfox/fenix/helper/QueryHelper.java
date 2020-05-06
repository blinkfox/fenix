package com.blinkfox.fenix.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * QueryHelper.
 *
 * @author blinkfox on 2019-08-08.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class QueryHelper {

    private static final String IDENTIFIER = "[._[\\P{Z}&&\\P{Cc}&&\\P{Cf}&&\\P{P}]]+";

    private static final Pattern ALIAS_MATCH;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append("(?<=from)");
        sb.append("(?:\\s)+");
        sb.append(String.format("(%s)", IDENTIFIER));
        sb.append("(?:\\sas)*");
        sb.append("(?:\\s)+");
        sb.append("(?!(?:where|group by|order by))(\\w+)");
        ALIAS_MATCH = Pattern.compile(sb.toString(), Pattern.CASE_INSENSITIVE);
    }

    /**
     * 监测 JPQL 或者 SQL 中的别名.
     *
     * @param query JPQL 语句
     * @return JPQL 语句
     */
    public static String detectAlias(String query) {
        Matcher matcher = ALIAS_MATCH.matcher(query);
        return matcher.find() ? matcher.group(2) : null;
    }

}
