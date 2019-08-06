package com.blinkfox.fenix.core.concrete;

import com.blinkfox.fenix.bean.BuildSource;
import com.blinkfox.fenix.consts.Const;
import com.blinkfox.fenix.consts.LikeTypeEnum;
import com.blinkfox.fenix.core.FenixHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于生成 'LIKE' 后缀匹配查询的动态 JPQL 或者 SQL 片段的 {@link FenixHandler} 接口的实现类，
 * 该类是 {@link LikeHandler} 的子类.
 *
 * <p>XML 标签示例如：</p>
 * <ul>
 *     <li>'<endsWith match="" field="" value="" />'</li>
 *     <li>'<andEndsWith match="" field="" value="" />'</li>
 *     <li>'<orEndsWith match="" field="" value="" />'</li>
 * </ul>
 * <p>注：</p>
 * <ul>
 *     <li>获取到 match 字段的值，如果为空或者为 true，就生成此 SQL 片段；</li>
 *     <li>生成的 SQL 片段默认是按后缀来匹配的，即：'%abc'.</li>
 * </ul>
 *
 * @author blinkfox on 2019-08-06.
 * @see LikeHandler
 * @see StartsWithHandler
 */
public class EndsWithHandler extends LikeHandler {

    /**
     * 用于后续生成 LIKE 后缀匹配 SQL 片段的额外参数 Map.
     */
    private static Map<String, Object> endsMap = new HashMap<>(2);

    static {
        endsMap.put(Const.TYPE, LikeTypeEnum.ENDS_WITH);
    }

    /**
     * 重写了 {@link LikeHandler#buildSqlInfo(BuildSource)} 中的方法，
     * 在 {@link BuildSource} 变量中设置一个 map 参数，用来标记是后缀匹配的情况，便于后续的获取、判断和处理.
     *
     * @param source {@link BuildSource} 构建资源参数
     */
    @Override
    public void buildSqlInfo(BuildSource source) {
        source.setOthers(endsMap);
        super.buildSqlInfo(source);
    }

}
