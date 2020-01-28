package com.blinkfox.fenix.repository;

import com.blinkfox.fenix.entity.Book;
import com.blinkfox.fenix.specification.FenixJpaSpecificationExecutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 用来测试 Fenix 的动态 Specification 的 {@code Repository}.
 *
 * @author blinkfox on 2020-01-28.
 */
@Repository
public interface FenixBookRepository extends JpaRepository<Book, String>, FenixJpaSpecificationExecutor<Book> {

}
