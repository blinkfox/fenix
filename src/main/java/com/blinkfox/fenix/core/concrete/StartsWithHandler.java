package com.blinkfox.fenix.core.concrete;

import com.blinkfox.fenix.bean.BuildSource;
import com.blinkfox.fenix.consts.Const;
import com.blinkfox.fenix.consts.LikeTypeEnum;
import com.blinkfox.fenix.core.FenixHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于生成 'LIKE' 前缀匹配查询的动态 JPQL 或者 SQL 片段的 {@link FenixHandler} 接口的实现类，
 * 是 {@link LikeHandler} 的特殊情况.
 *
 * <p>XML 标签示例如：</p>
 * <ul>
 *     <li>'<startsWith match="" field="" value="" />'</li>
 *     <li>'<andStartsWith match="" field="" value="" />'</li>
 *     <li>'<orStartsWith match="" field="" value="" />'</li>
 * </ul>
 * <p>注：</p>
 * <ul>
 *     <li>获取到 match 字段的值，如果为空或者为 true，就生成此 SQL 片段；</li>
 *     <li>生成的 SQL 片段默认是按前缀来匹配的，即：'abc%'.</li>
 * </ul>
 *
 * @author blinkfox on 2019-08-06.
 * @see LikeHandler
 */
public class StartsWithHandler extends LikeHandler {

    private static Map<String, Object> startMap = new HashMap<>(2);

    static {
        startMap.put(Const.TYPE, LikeTypeEnum.STARTS_WITH);
    }

    /**
     * 重写了 {@link LikeHandler#buildSqlInfo(BuildSource)} 中的方法，
     * 在 source 变量中设置一个 map 参数，用来标记是前缀匹配的情况，便于后续判断处理.
     *
     * @param source 构建所需的资源对象
     */
    @Override
    public void buildSqlInfo(BuildSource source) {
        source.setOthers(startMap);
        super.buildSqlInfo(source);
    }

}
