package com.blinkfox.fenix.repository;

import com.blinkfox.fenix.FenixTestApplication;
import com.blinkfox.fenix.entity.Book;
import com.blinkfox.fenix.helper.StringHelper;
import com.blinkfox.fenix.specification.util.JpaQueryUtils;
import com.blinkfox.fenix.vo.BookParam;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.jpa.repository.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * BlogRepository Test.
 *
 * @author blinkfox on 2019-08-04.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = FenixTestApplication.class)
public class BookRepositoryTest {

    @Resource
    private BookRepository bookRepository;

    /**
     * 测试使用原生的 {@link Query} 注解来模糊查询博客信息.
     */
    @Ignore
    @Test
    public void queryBooksByName() {
        BookParam bookParam = new BookParam().setName("Java");
        List<Book> books = bookRepository.findAll(JpaQueryUtils.getQuerySpecification(bookParam));
        Assert.assertFalse(books.isEmpty());
        Assert.assertTrue(StringHelper.isNotBlank(books.get(0).getName()));
    }

}
