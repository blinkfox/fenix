package com.blinkfox.fenix.repository.transformer;

import com.blinkfox.fenix.entity.transformer.UnderscoreEntity;
import com.blinkfox.fenix.jpa.QueryFenix;
import com.blinkfox.fenix.jpa.transformer.ColumnAnnotationTransformer;
import com.blinkfox.fenix.jpa.transformer.UnderscoreTransformer;
import com.blinkfox.fenix.vo.transformer.UnderscoreColumnVo;
import com.blinkfox.fenix.vo.transformer.UnderscoreVo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 用来测试下划线对应结果转换器的 Repository.
 *
 * @author blinkfox on 2022-03-26.
 * @since v2.7.0
 */
@Repository
public interface UnderscoreEntityRepository extends JpaRepository<UnderscoreEntity, Long> {

    /**
     * 使用 {@link QueryFenix} 注解来查询自定义的实体信息.
     *
     * @param num 数字
     * @return 用户博客信息集合
     */
    @QueryFenix(resultType = UnderscoreVo.class, resultTransformer = UnderscoreTransformer.class, nativeQuery = true)
    List<UnderscoreVo> queryFenixResultType(@Param("num") long num);

    /**
     * 使用 {@link QueryFenix} 注解来连表模糊查询自定义的用户博客实体信息.
     *
     * @param num 数字
     * @return 用户博客信息集合
     */
    @QueryFenix(resultType = UnderscoreColumnVo.class, resultTransformer = ColumnAnnotationTransformer.class,
            nativeQuery = true)
    List<UnderscoreColumnVo> queryAtColumnVoList(@Param("num") long num);

}
