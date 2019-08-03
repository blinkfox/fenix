package com.blinkfox.fenix.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 扩展自 JpaRepository 的 Repository 接口，便于自定义扩展更多的实用功能.
 *
 * @author blinkfox on 2019-08-04.
 */
@NoRepositoryBean
public interface FenixJpaRepository<T, ID> extends JpaRepository<T, ID> {

}
