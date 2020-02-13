package com.blinkfox.fenix.repository;

import com.alibaba.fastjson.JSON;
import com.blinkfox.fenix.FenixTestApplication;
import com.blinkfox.fenix.entity.Book;
import com.blinkfox.fenix.helper.StringHelper;
import com.blinkfox.fenix.specification.FenixSpecification;
import com.blinkfox.fenix.vo.param.BookParam;

import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;

import lombok.Setter;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.FileCopyUtils;

/**
 * BlogRepository Test.
 *
 * @author blinkfox on 2019-08-04.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = FenixTestApplication.class)
public class BookRepositoryTest {

    /**
     * 是否加载过的标识.
     */
    @Setter
    private static volatile Boolean isLoad = false;

    @Value("/data/book.json")
    private Resource bookResource;

    @Autowired
    private BookRepository bookRepository;

    /**
     * 初始化 Fenix 配置信息，并从资源文件的中读取数据，初始化保存起来，便于后续读取或操作.
     */
    @PostConstruct
    public void init() throws IOException {
        if (!isLoad) {
            bookRepository.saveAll(
                    JSON.parseArray(new String(FileCopyUtils.copyToByteArray(bookResource.getFile())), Book.class));
            setIsLoad(true);
            List<Book> books = bookRepository.findAll();
            Assert.assertEquals(10, books.size());
        }
    }

    /**
     * 测试使用 {@code Specification} 的方式来根据名称模糊查询图书信息.
     */
    @Test
    public void findBooksByName() {
        BookParam bookParam = new BookParam().setName("Java");
        List<Book> books = bookRepository.findAll(FenixSpecification.ofBean(bookParam));
        Assert.assertFalse(books.isEmpty());
        Assert.assertEquals(3, books.size());
        Assert.assertTrue(StringHelper.isNotBlank(books.get(0).getName()));
    }

    /**
     * 测试使用 {@code Specification} 的方式来根据名称模糊分页查询图书信息.
     */
    @Test
    public void findBookPagingByName() {
        BookParam bookParam = new BookParam().setName("Java");
        Page<Book> bookPage = bookRepository.findAll(FenixSpecification.ofBean(bookParam),
                PageRequest.of(0, 2, Sort.by(Sort.Order.desc("updateTime"))));
        List<Book> books = bookPage.getContent();
        Assert.assertFalse(books.isEmpty());
        Assert.assertTrue(StringHelper.isNotBlank(books.get(0).getName()));
    }

}
