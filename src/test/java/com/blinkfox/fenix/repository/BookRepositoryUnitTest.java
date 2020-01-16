package com.blinkfox.fenix.repository;

import com.alibaba.fastjson.JSON;
import com.blinkfox.fenix.FenixTestApplication;
import com.blinkfox.fenix.entity.Book;
import com.blinkfox.fenix.specification.FenixSpecification;
import com.blinkfox.fenix.vo.param.BookParam;
import com.blinkfox.fenix.vo.param.BookParam2;
import com.blinkfox.fenix.vo.param.BookParam3;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import lombok.Setter;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.FileCopyUtils;

/**
 * 基于 {@link BookRepository} 用来测试 {@link org.springframework.data.jpa.domain.Specification} 功能的单元测试类.
 *
 * @author blinkfox on 2020-01-15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = FenixTestApplication.class)
public class BookRepositoryUnitTest {

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
        }
    }

    /**
     * 测试使用 {@code Specification} 的方式来等值查询图书信息.
     */
    @Test
    public void testEquals() {
        BookParam bookParam = new BookParam().setIsbn("9787111641247");
        List<Book> books = bookRepository.findAll(FenixSpecification.of(bookParam));
        Assert.assertEquals(1, books.size());
    }

    /**
     * 测试使用 {@code Specification} 的方式来大于查询图书信息.
     */
    @Test
    public void testGreaterThan() {
        BookParam bookParam = new BookParam().setTotalPage(500);
        List<Book> books = bookRepository.findAll(FenixSpecification.of(bookParam));
        Assert.assertEquals(5, books.size());
    }

    /**
     * 测试使用 {@code Specification} 的方式来大于等于查询图书信息.
     */
    @Test
    public void testGreaterThanEqual() {
        List<Book> books = bookRepository.findAll(FenixSpecification.of(new BookParam2().setTotalPage(880)));
        Assert.assertEquals(2, books.size());
    }

    /**
     * 测试使用 {@code Specification} 的方式来小于查询图书信息.
     */
    @Test
    public void testLessThan() {
        List<Book> books = bookRepository.findAll(FenixSpecification.of(
                new BookParam().setPublishAt("2014-11-01")));
        Assert.assertEquals(4, books.size());
    }

    /**
     * 测试使用 {@code Specification} 的方式来小于查询图书信息.
     */
    @Test
    public void testLessThanEqual() {
        List<Book> books = bookRepository.findAll(FenixSpecification.of(
                new BookParam2().setPublishAt("2014-11-01")));
        Assert.assertEquals(5, books.size());
    }

    /**
     * 测试使用 {@code Specification} 的方式来模糊查询图书信息.
     */
    @Test
    public void testLike() {
        List<Book> books = bookRepository.findAll(FenixSpecification.of(new BookParam().setName("Java")));
        Assert.assertEquals(3, books.size());
    }

    /**
     * 测试使用 {@code Specification} 的方式来模糊查询图书信息.
     */
    @Test
    public void testLikeIn() {
        List<Book> books = bookRepository.findAll(FenixSpecification.of(
                new BookParam().setAuthors(Arrays.asList("袁国忠", "陈"))));
        Assert.assertEquals(4, books.size());
    }

    /**
     * 测试使用 {@code Specification} 的方式来模糊查询图书信息.
     */
    @Test
    public void testLikeOrLike() {
        List<Book> books = bookRepository.findAll(FenixSpecification.of(
                new BookParam3().setNameOrAuthor(Arrays.asList("Java", "袁国忠"))));
        Assert.assertEquals(6, books.size());
    }

    /**
     * 测试使用 {@code Specification} 的方式来模糊查询图书信息.
     */
    @Test
    public void testOrLikeOrLike() {
        List<Book> books = bookRepository.findAll(FenixSpecification.of(
                new BookParam3().setOrNameOrAuthor(new String[]{"Java", "袁国忠"})));
        Assert.assertEquals(6, books.size());
    }

    /**
     * 测试使用 {@code Specification} 的方式来范围查询图书信息.
     */
    @Test
    public void testIn() {
        // 测试数组的情况.
        BookParam bookParam = new BookParam()
                .setId(Arrays.asList("1", "2", "3", "4", "5", "6", "7"));
        List<Book> books = bookRepository.findAll(FenixSpecification.of(bookParam));
        Assert.assertEquals(7, books.size());
    }

    /**
     * 测试使用 {@code Specification} 的方式来范围查询图书信息.
     */
    @Test
    public void testIsNull() {
        List<Book> books = bookRepository.findAll(FenixSpecification.of(new BookParam().setOthers("others")));
        Assert.assertEquals(7, books.size());
    }

    /**
     * 测试使用 {@code Specification} 的方式来范围查询图书信息.
     */
    @Test
    public void testIsNotNull() {
        List<Book> books = bookRepository.findAll(FenixSpecification.of(new BookParam2().setOthers("others")));
        Assert.assertEquals(3, books.size());
    }

}
