package com.blinkfox.fenix.core.concrete;

import com.blinkfox.fenix.bean.BuildSource;
import com.blinkfox.fenix.bean.SqlInfo;
import com.blinkfox.fenix.core.FenixHandler;
import com.blinkfox.fenix.core.FenixXmlBuilder;

/**
 * {@code <where></where>} 标签是可以包裹其他动态标签的标签，功能同 MyBatis 中的 {@code <where>} 标签相似，
 * 用于去掉 {@code WHERE} 关键字后的 {@code AND} 或者 {@code OR} 关键字的 {@link FenixHandler} 接口的实现类.
 *
 * <p>XML 标签示例如：</p>
 * <ul>
 *     <li>{@code <where>...</where>}</li>
 * </ul>
 * <p>注：{@code <where>} 标签只能用来去掉{@code WHERE} 关键字后的 {@code AND} 或者 {@code OR} 关键字.</p>
 *
 * @author blinkfox on 2019-08-07.
 * @see ImportHandler
 * @since v2.1.0
 */
public class WhereHandler implements FenixHandler {

    /**
     * 根据 {@link BuildSource} 的相关参数来动态构建出 {@code WHERE} 语句后的 SQL 或 JPQL 语句及参数信息.
     *
     * @param source 构建所需的 {@code BuildSource} 资源对象
     */
    @Override
    public void buildSqlInfo(BuildSource source) {
        // 进入此方法后，将 prependWhere 设置为 true,
        // 说明后续的动态条件的 SQL 语句中都要判断是否要前置添加 WHERE 关键字，并去除掉 WHERE 后面的 AND 或者 OR 关键字.
        // 在各个动态标签或文本标签中，如果处理了 WHERE 标签的情况之后，就再将 prependWhere 设置为 false 即可.
        SqlInfo sqlInfo = source.getSqlInfo();
        sqlInfo.setPrependWhere(true);
        FenixXmlBuilder.buildSqlInfo(source.getNamespace(), sqlInfo, source.getNode(), source.getContext());
    }

}
