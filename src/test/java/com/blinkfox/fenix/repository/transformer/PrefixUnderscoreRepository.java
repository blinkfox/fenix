package com.blinkfox.fenix.repository.transformer;

import com.blinkfox.fenix.entity.transformer.PrefixUnderscore;
import com.blinkfox.fenix.jpa.QueryFenix;
import com.blinkfox.fenix.jpa.transformer.PrefixUnderscoreTransformer;
import com.blinkfox.fenix.provider.TransformerTestSqlInfoProvider;
import com.blinkfox.fenix.vo.transformer.PrefixUnderscoreVo;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 用来测试连字符对应结果转换器的 Repository.
 *
 * @author blinkfox on 2022-03-26.
 * @since v2.7.0
 */
@Repository
public interface PrefixUnderscoreRepository extends JpaRepository<PrefixUnderscore, Long> {

    /**
     * 使用 {@link QueryFenix} 注解来连表模糊查询自定义的用户博客实体信息.
     *
     * @param num 数字
     * @return 用户博客信息集合
     */
    @QueryFenix(provider = TransformerTestSqlInfoProvider.class, resultType = PrefixUnderscoreVo.class,
            resultTransformer = PrefixUnderscoreTransformer.class, nativeQuery = true)
    List<PrefixUnderscoreVo> queryPrefixUnderscoreVoResultType(@Param("num") int num);

    /**
     * 使用 {@link QueryFenix} 注解来连表模糊查询自定义的用户博客实体信息.
     *
     * @param num 数字
     * @return 用户博客信息集合
     */
    @QueryFenix(resultType = PrefixUnderscoreVo.class,
            resultTransformer = PrefixUnderscoreTransformer.class, nativeQuery = true)
    List<PrefixUnderscoreVo> queryPrefixUnderscoreVoWithXml(@Param("num") int num, Pageable pageable);

}
