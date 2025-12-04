package com.blinkfox.fenix.entity.ar;

import com.blinkfox.fenix.ar.repo.JpaModel;
import com.blinkfox.fenix.id.NanoId;
import com.blinkfox.fenix.repository.ar.ArJpaRepository;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 用来测试 Active Record 的实体类，本实体类继承自 {@link JpaModel}.
 *
 * @author blinkfox on 2022-03-29.
 * @since 2.7.0
 */
@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "t_ar_jpa")
public class ArJpa implements JpaModel<ArJpa, String, ArJpaRepository> {

    /**
     * ID.
     */
    @Id
    @Column(name = "c_id")
    @NanoId
    private String id;

    /**
     * 名称.
     */
    @Column(name = "c_name")
    private String name;

}
