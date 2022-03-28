package com.blinkfox.fenix.entity.ar;

import com.blinkfox.fenix.ar.FenixModel;
import com.blinkfox.fenix.enums.StatusEnum;
import java.time.LocalDateTime;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

/**
 * 用来测试 Active Record 的实体类.
 *
 * @author blinkfox on 2022-03-28.
 * @since 2.7.0
 */
@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "t_ar_table")
public class ArEntity extends FenixModel<ArEntity, String> {

    /**
     * ID.
     */
    @Id
    @Column(name = "c_id")
    @GeneratedValue(generator = "nanoId")
    @GenericGenerator(name = "nanoId", strategy = "com.blinkfox.fenix.id.NanoIdGenerator")
    private String id;

    /**
     * 标题.
     */
    @Column(name = "c_title")
    private String title;

    /**
     * 描述.
     */
    @Column(name = "c_desc")
    private String desc;

    /**
     * 年龄.
     */
    @Column(name = "n_age")
    private Integer age;

    /**
     * 状态.
     */
    @Column(name = "c_status")
    private StatusEnum status;

    /**
     * 创建时间.
     */
    @Column(name = "dt_create_time")
    private Date createTime;

    /**
     * 最后更新时间.
     */
    @Column(name = "dt_update_time")
    private LocalDateTime lastUpdateTime;

}
