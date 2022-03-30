package com.blinkfox.fenix.entity.ar;

import com.blinkfox.fenix.ar.repo.JpaModel;
import com.blinkfox.fenix.ar.spec.FenixSpecModel;
import com.blinkfox.fenix.repository.ar.ArFenixSpecRepository;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Proxy;

/**
 * 测试 ActiveRecord 模式下测试 FenixSpecModel 的接口.
 *
 * @author blinkfox on 2022-03-30.
 * @since 1.0.0
 */
@Getter
@Setter
@Accessors(chain = true)
@Entity
@Proxy(lazy = false)
@Table(name = "t_ar_fenix_spec")
public class ArFenixSpec implements
        JpaModel<ArFenixSpec, Long, ArFenixSpecRepository>,
        FenixSpecModel<ArFenixSpec, ArFenixSpecRepository> {

    @Id
    @Column(name = "n_id")
    private Long id;

    @Column(name = "c_name")
    private String name;

    @Column(name = "c_summary")
    private String summary;

    @Column(name = "n_status")
    private Integer status;

    @Column(name = "c_birthday")
    private String birthday;

}
