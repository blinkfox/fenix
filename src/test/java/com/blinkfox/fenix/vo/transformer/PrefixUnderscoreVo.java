package com.blinkfox.fenix.vo.transformer;

import java.time.LocalDateTime;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 数据库查询字段均为连字符的 VO 类.
 *
 * @author blinkfox on 2022-03-26.
 * @since v2.7.0
 */
@Getter
@Setter
@Accessors(chain = true)
public class PrefixUnderscoreVo {

    private String id;

    private String name;

    private String secondName;

    private Integer integerColumn;

    private long longColumn;

    private Date createTime;

    private LocalDateTime lastUpdateTime;

}
