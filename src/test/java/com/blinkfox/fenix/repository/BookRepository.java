package com.blinkfox.fenix.repository;

import com.blinkfox.fenix.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * 用来测试 Fenix 的动态 Specification 的 {@code Repository}.
 *
 * @author blinkfox on 2020-01-14.
 */
@Repository
public interface BookRepository extends JpaRepository<Book, String>, JpaSpecificationExecutor<Book> {

}
