package com.blinkfox.fenix.entity.ar;

import com.blinkfox.fenix.ar.repo.JpaModel;
import com.blinkfox.fenix.repository.ar.ArPageSortRepository;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Proxy;

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
@Proxy(lazy = false)
@Table(name = "t_ar_page_sort")
public class ArPageSort implements JpaModel<ArPageSort, String, ArPageSortRepository> {

    /**
     * ID.
     */
    @Id
    @Column(name = "c_id")
    @GeneratedValue(generator = "nanoId")
    @GenericGenerator(name = "nanoId", strategy = "com.blinkfox.fenix.id.NanoIdGenerator")
    private String id;

    /**
     * 名称.
     */
    @Column(name = "c_name")
    private String name;

}
