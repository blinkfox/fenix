package com.blinkfox.fenix.repository;

import com.alibaba.fastjson.JSON;
import com.blinkfox.fenix.FenixTestApplication;
import com.blinkfox.fenix.entity.Book;
import com.blinkfox.fenix.exception.BuildSpecificationException;
import com.blinkfox.fenix.specification.FenixSpecification;
import com.blinkfox.fenix.specification.handler.bean.BetweenValue;
import com.blinkfox.fenix.vo.param.BookParam;
import com.blinkfox.fenix.vo.param.BookParam2;
import com.blinkfox.fenix.vo.param.BookParam3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    private static final String ISBN = "9787111641247";

    private static final String DATE = "2014-11-01";

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
        List<Book> books = bookRepository.findAll(FenixSpecification.ofBean(new BookParam().setIsbn(ISBN)));
        Assert.assertEquals(1, books.size());

        List<Book> books2 = bookRepository.findAll(FenixSpecification.ofBean(new BookParam().setOrIsbn(ISBN)));
        Assert.assertEquals(1, books2.size());
    }

    /**
     * 测试使用 {@code Specification} 的方式来等值查询图书信息.
     */
    @Test
    public void testNotEquals() {
        List<Book> books = bookRepository.findAll(FenixSpecification.ofBean(new BookParam2().setIsbn(ISBN)));
        Assert.assertEquals(9, books.size());

        List<Book> books2 = bookRepository.findAll(FenixSpecification.ofBean(new BookParam2().setOrIsbn(ISBN)));
        Assert.assertEquals(9, books2.size());
    }

    /**
     * 测试使用 {@code Specification} 的方式来大于查询图书信息.
     */
    @Test
    public void testGreaterThan() {
        List<Book> books = bookRepository.findAll(FenixSpecification.ofBean(new BookParam().setTotalPage(500)));
        Assert.assertEquals(5, books.size());

        List<Book> books2 = bookRepository.findAll(FenixSpecification.ofBean(new BookParam().setOrTotalPage(500)));
        Assert.assertEquals(5, books2.size());
    }

    /**
     * 测试使用 {@code Specification} 的方式来大于等于查询图书信息.
     */
    @Test
    public void testGreaterThanEqual() {
        List<Book> books = bookRepository.findAll(FenixSpecification.ofBean(new BookParam2().setTotalPage(880)));
        Assert.assertEquals(2, books.size());

        List<Book> books2 = bookRepository.findAll(FenixSpecification.ofBean(new BookParam2().setOrTotalPage(880)));
        Assert.assertEquals(2, books2.size());
    }

    /**
     * 测试使用 {@code Specification} 的方式来小于查询图书信息.
     */
    @Test
    public void testLessThan() {
        List<Book> books = bookRepository.findAll(FenixSpecification.ofBean(new BookParam().setPublishAt(DATE)));
        Assert.assertEquals(4, books.size());

        List<Book> books2 = bookRepository.findAll(FenixSpecification.ofBean(new BookParam().setOrPublishAt(DATE)));
        Assert.assertEquals(4, books2.size());
    }

    /**
     * 测试使用 {@code Specification} 的方式来小于查询图书信息.
     */
    @Test
    public void testLessThanEqual() {
        List<Book> books = bookRepository.findAll(FenixSpecification.ofBean(new BookParam2().setPublishAt(DATE)));
        Assert.assertEquals(5, books.size());

        List<Book> books2 = bookRepository.findAll(FenixSpecification.ofBean(new BookParam2().setOrPublishAt(DATE)));
        Assert.assertEquals(5, books2.size());
    }

    /**
     * 测试使用 {@code Specification} 的方式来大于等于查询图书信息.
     */
    @Test
    public void testBetween() {
        List<Book> books = bookRepository.findAll(
                FenixSpecification.ofBean(new BookParam2().setTotalPageArr(new Integer[]{300, 600})));
        Assert.assertEquals(4, books.size());
        books.forEach(book -> Assert.assertTrue(book.getTotalPage() >= 300 && book.getTotalPage() <= 600));

        List<Book> books2 = bookRepository.findAll(
                FenixSpecification.ofBean(new BookParam2().setTotalPageArr(new Integer[]{300, null})));
        Assert.assertEquals(8, books2.size());
        books2.forEach(book -> Assert.assertTrue(book.getTotalPage() >= 300));

        List<Book> books3 = bookRepository.findAll(
                FenixSpecification.ofBean(new BookParam2().setTotalPageArr(new Integer[]{null, 600})));
        Assert.assertEquals(6, books3.size());
        books3.forEach(book -> Assert.assertTrue(book.getTotalPage() <= 600));

        List<Book> books4 = bookRepository.findAll(
                FenixSpecification.ofBean(new BookParam2().setTotalPages(Arrays.asList(300, 600))));
        Assert.assertEquals(4, books4.size());
        books4.forEach(book -> Assert.assertTrue(book.getTotalPage() >= 300 && book.getTotalPage() <= 600));

        List<Book> books5 = bookRepository.findAll(
                FenixSpecification.ofBean(new BookParam2().setTotalPages(Arrays.asList(300, null))));
        Assert.assertEquals(8, books5.size());
        books5.forEach(book -> Assert.assertTrue(book.getTotalPage() >= 300));

        List<Book> books6 = bookRepository.findAll(
                FenixSpecification.ofBean(new BookParam2().setTotalPages(Arrays.asList(null, 600))));
        Assert.assertEquals(6, books6.size());
        books6.forEach(book -> Assert.assertTrue(book.getTotalPage() <= 600));
    }

    /**
     * 测试使用 {@code Specification} 的方式来大于等于查询图书信息.
     */
    @Test
    public void testBetweenWithBetweenValue() {
        List<Book> books = bookRepository.findAll(
                FenixSpecification.ofBean(new BookParam2().setTotalPageValue(BetweenValue.of(300, 600))));
        Assert.assertEquals(4, books.size());
        books.forEach(book -> Assert.assertTrue(book.getTotalPage() >= 300 && book.getTotalPage() <= 600));

        List<Book> books2 = bookRepository.findAll(
                FenixSpecification.ofBean(new BookParam2().setTotalPageValue(BetweenValue.ofStart(300))));
        Assert.assertEquals(8, books2.size());
        books2.forEach(book -> Assert.assertTrue(book.getTotalPage() >= 300));

        List<Book> books3 = bookRepository.findAll(
                FenixSpecification.ofBean(new BookParam2().setTotalPageValue(BetweenValue.ofEnd(600))));
        Assert.assertEquals(6, books3.size());
        books3.forEach(book -> Assert.assertTrue(book.getTotalPage() <= 600));
    }

    /**
     * 测试使用 {@code Specification} 的方式来大于等于查询图书信息.
     */
    @Test
    public void testNotBetween() {
        List<Book> books = bookRepository.findAll(
                FenixSpecification.ofBean(new BookParam2().setNotTotalPageArr(new Integer[]{300, 600})));
        Assert.assertEquals(6, books.size());
        books.forEach(book -> Assert.assertFalse(book.getTotalPage() >= 300 && book.getTotalPage() <= 600));

        List<Book> books2 = bookRepository.findAll(
                FenixSpecification.ofBean(new BookParam2().setNotTotalPageArr(new Integer[]{300, null})));
        Assert.assertEquals(2, books2.size());
        books2.forEach(book -> Assert.assertFalse(book.getTotalPage() >= 300));

        List<Book> books3 = bookRepository.findAll(
                FenixSpecification.ofBean(new BookParam2().setNotTotalPageArr(new Integer[]{null, 600})));
        Assert.assertEquals(4, books3.size());
        books3.forEach(book -> Assert.assertFalse(book.getTotalPage() <= 600));

        List<Book> books4 = bookRepository.findAll(
                FenixSpecification.ofBean(new BookParam2().setNotTotalPages(Arrays.asList(300, 600))));
        Assert.assertEquals(6, books4.size());
        books4.forEach(book -> Assert.assertFalse(book.getTotalPage() >= 300 && book.getTotalPage() <= 600));

        List<Book> books5 = bookRepository.findAll(
                FenixSpecification.ofBean(new BookParam2().setNotTotalPages(Arrays.asList(300, null))));
        Assert.assertEquals(2, books5.size());
        books5.forEach(book -> Assert.assertFalse(book.getTotalPage() >= 300));

        List<Book> books6 = bookRepository.findAll(
                FenixSpecification.ofBean(new BookParam2().setNotTotalPages(Arrays.asList(null, 600))));
        Assert.assertEquals(4, books6.size());
        books6.forEach(book -> Assert.assertFalse(book.getTotalPage() <= 600));
    }

    /**
     * 测试使用 {@code Specification} 的方式来模糊查询图书信息.
     */
    @Test
    public void testLike() {
        List<Book> books = bookRepository.findAll(FenixSpecification.ofBean(new BookParam().setName("Java")));
        Assert.assertEquals(3, books.size());

        List<Book> books2 = bookRepository.findAll(FenixSpecification.ofBean(new BookParam().setOrName("Java")));
        Assert.assertEquals(3, books2.size());
    }

    /**
     * 测试使用 {@code Specification} 的方式来按前缀模糊查询图书信息.
     */
    @Test
    public void testStartsWith() {
        List<Book> books = bookRepository.findAll(
                FenixSpecification.ofBean(new BookParam().setStartsWithName("Java")));
        Assert.assertEquals(2, books.size());
        books.forEach(book -> Assert.assertTrue(book.getName().startsWith("Java")));

        List<Book> books2 = bookRepository.findAll(
                FenixSpecification.ofBean(new BookParam().setOrStartsWithName("Java")));
        Assert.assertEquals(2, books2.size());
        books2.forEach(book -> Assert.assertTrue(book.getName().startsWith("Java")));
    }

    /**
     * 测试使用 {@code Specification} 的方式来按前缀模糊查询图书信息.
     */
    @Test
    public void testNotStartsWith() {
        List<Book> books = bookRepository.findAll(
                FenixSpecification.ofBean(new BookParam().setNotStartsWithName("Java")));
        Assert.assertEquals(8, books.size());
        books.forEach(book -> Assert.assertFalse(book.getName().startsWith("Java")));

        List<Book> books2 = bookRepository.findAll(
                FenixSpecification.ofBean(new BookParam().setOrNotStartsWithName("Java")));
        Assert.assertEquals(8, books2.size());
        books2.forEach(book -> Assert.assertFalse(book.getName().startsWith("Java")));
    }

    /**
     * 测试使用 {@code Specification} 的方式来按后缀模糊查询图书信息.
     */
    @Test
    public void testEndsWith() {
        List<Book> books = bookRepository.findAll(
                FenixSpecification.ofBean(new BookParam().setEndsWithName("(algorithm)")));
        Assert.assertEquals(3, books.size());
        books.forEach(book -> Assert.assertTrue(book.getName().endsWith("(algorithm)")));

        List<Book> books2 = bookRepository.findAll(
                FenixSpecification.ofBean(new BookParam().setOrEndsWithName("(algorithm)")));
        Assert.assertEquals(3, books2.size());
        books2.forEach(book -> Assert.assertTrue(book.getName().endsWith("(algorithm)")));
    }

    /**
     * 测试使用 {@code Specification} 的方式来按后缀模糊查询图书信息.
     */
    @Test
    public void testNotEndsWith() {
        List<Book> books = bookRepository.findAll(
                FenixSpecification.ofBean(new BookParam().setNotEndsWithName("(algorithm)")));
        Assert.assertEquals(7, books.size());
        books.forEach(book -> Assert.assertFalse(book.getName().endsWith("(algorithm)")));

        List<Book> books2 = bookRepository.findAll(
                FenixSpecification.ofBean(new BookParam().setOrNotEndsWithName("(algorithm)")));
        Assert.assertEquals(7, books2.size());
        books2.forEach(book -> Assert.assertFalse(book.getName().endsWith("(algorithm)")));
    }

    /**
     * 测试使用 {@code Specification} 的方式来按指定模式匹配查询图书信息.
     */
    @Test
    public void testLikePattern() {
        List<Book> books = bookRepository.findAll(
                FenixSpecification.ofBean(new BookParam().setPatternName("_ava%")));
        Assert.assertEquals(2, books.size());

        List<Book> books2 = bookRepository.findAll(
                FenixSpecification.ofBean(new BookParam().setOrPatternName("_ava%")));
        Assert.assertEquals(2, books2.size());
    }

    /**
     * 测试使用 {@code Specification} 的方式来按指定模式不匹配查询图书信息.
     */
    @Test
    public void testNotLikePattern() {
        List<Book> books = bookRepository.findAll(
                FenixSpecification.ofBean(new BookParam().setNotPatternName("_ava%")));
        Assert.assertEquals(8, books.size());

        List<Book> books2 = bookRepository.findAll(
                FenixSpecification.ofBean(new BookParam().setOrNotPatternName("_ava%")));
        Assert.assertEquals(8, books2.size());
    }

    /**
     * 测试使用 {@code Specification} 的方式来模糊查询图书信息.
     */
    @Test
    public void testLikeIn() {
        List<Book> books = bookRepository.findAll(FenixSpecification.ofBean(
                new BookParam().setAuthors(Arrays.asList("yuanguozhong", "chen"))));
        Assert.assertEquals(4, books.size());
    }

    /**
     * 测试使用 {@code Specification} 的方式来模糊查询图书信息.
     */
    @Test
    public void testLikeOrLike() {
        List<Book> books = bookRepository.findAll(FenixSpecification.ofBean(
                new BookParam3().setNameOrAuthor(Arrays.asList("Java", "yuanguozhong"))));
        Assert.assertEquals(6, books.size());
    }

    /**
     * 测试使用 {@code Specification} 的方式来模糊查询图书信息.
     */
    @Test
    public void testOrLikeOrLike() {
        List<Book> books = bookRepository.findAll(FenixSpecification.ofBean(
                new BookParam3().setOrNameOrAuthor(new String[]{"Java", "yuanguozhong"})));
        Assert.assertEquals(6, books.size());
    }

    /**
     * 测试使用 {@code Specification} 的方式来模糊查询图书信息.
     */
    @Test(expected = BuildSpecificationException.class)
    public void testLikeOrLikeWithException() {
        Set<String> set = new HashSet<>(2);
        set.add("Java");
        bookRepository.findAll(FenixSpecification.ofBean(new BookParam3().setNameOrAuthorSet(set)));
    }

    /**
     * 测试使用 {@code Specification} 的方式来模糊查询图书信息.
     */
    @Test(expected = BuildSpecificationException.class)
    public void testLikeOrLikeWithException2() {
        bookRepository.findAll(FenixSpecification.ofBean(
                new BookParam3().setNameOrAuthor(Collections.singletonList("Java"))));
    }

    /**
     * 测试使用 {@code Specification} 的方式来模糊查询图书信息.
     */
    @Test(expected = BuildSpecificationException.class)
    public void testLikeOrLikeWithException3() {
        Set<String> set = new HashSet<>(2);
        set.add("Java");
        bookRepository.findAll(FenixSpecification.ofBean(new BookParam3().setOrNameOrAuthorSet(set)));
    }

    /**
     * 测试使用 {@code Specification} 的方式来模糊查询图书信息.
     */
    @Test(expected = BuildSpecificationException.class)
    public void testLikeOrLikeWithException4() {
        bookRepository.findAll(FenixSpecification.ofBean(
                new BookParam3().setOrNameOrAuthor(new String[]{"Java"})));
    }

    /**
     * 测试使用 {@code Specification} 的方式来范围查询图书信息.
     */
    @Test
    public void testIn() {
        BookParam bookParam = new BookParam()
                .setId(Arrays.asList("1", "2", "3", "4", "5", "6", "7"));
        List<Book> books = bookRepository.findAll(FenixSpecification.ofBean(bookParam));
        Assert.assertEquals(7, books.size());

        BookParam orbookParam = new BookParam()
                .setOrId(Arrays.asList("1", "2", "3", "4", "5", "6", "7"));
        List<Book> books2 = bookRepository.findAll(FenixSpecification.ofBean(orbookParam));
        Assert.assertEquals(7, books2.size());
    }

    /**
     * 测试使用 {@code Specification} 的方式来范围查询图书信息.
     */
    @Test
    public void testNotIn() {
        BookParam2 bookParam = new BookParam2()
                .setId(new String[]{"1", "2", "3", "4", "5", "6", "7"});
        List<Book> books = bookRepository.findAll(FenixSpecification.ofBean(bookParam));
        Assert.assertEquals(3, books.size());

        BookParam2 orbookParam = new BookParam2()
                .setOrIds(new String[]{"1", "2", "3", "4", "5", "6", "7"});
        List<Book> books2 = bookRepository.findAll(FenixSpecification.ofBean(orbookParam));
        Assert.assertEquals(3, books2.size());
    }

    /**
     * 测试使用 {@code Specification} 的方式来范围查询图书信息.
     */
    @Test
    public void testInWithEmpty() {
        List<Book> books = bookRepository.findAll(FenixSpecification.ofBean(new BookParam().setId(new ArrayList<>())));
        Assert.assertEquals(10, books.size());

        List<Book> books2 = bookRepository.findAll(
                FenixSpecification.ofBean(new BookParam2().setId(new String[]{})));
        Assert.assertEquals(10, books2.size());

        List<Book> books3 = bookRepository.findAll(
                FenixSpecification.ofBean(new BookParam().setOrId(new ArrayList<>())));
        Assert.assertEquals(10, books3.size());
    }

    /**
     * 测试使用 {@code Specification} 的方式来范围查询图书信息.
     */
    @Test
    public void testInWithOne() {
        List<Book> books = bookRepository.findAll(FenixSpecification.ofBean(new BookParam().setSingleId("3")));
        Assert.assertEquals(1, books.size());

        List<Book> books2 = bookRepository.findAll(FenixSpecification.ofBean(new BookParam2().setSingleId("2")));
        Assert.assertEquals(9, books2.size());

        List<Book> books3 = bookRepository.findAll(FenixSpecification.ofBean(new BookParam().setOrSingleId("3")));
        Assert.assertEquals(1, books3.size());

        List<Book> books4 = bookRepository.findAll(FenixSpecification.ofBean(new BookParam2().setOrSingleId("1")));
        Assert.assertEquals(9, books4.size());
    }

    /**
     * 测试使用 {@code Specification} 的方式来范围查询图书信息.
     */
    @Test
    public void testIsNull() {
        List<Book> books = bookRepository.findAll(FenixSpecification.ofBean(new BookParam().setOthers("others")));
        Assert.assertEquals(7, books.size());

        List<Book> books2 = bookRepository.findAll(FenixSpecification.ofBean(new BookParam().setOrOthers("others")));
        Assert.assertEquals(7, books2.size());
    }

    /**
     * 测试使用 {@code Specification} 的方式来范围查询图书信息.
     */
    @Test
    public void testIsNotNull() {
        List<Book> books = bookRepository.findAll(FenixSpecification.ofBean(new BookParam2().setOthers("others")));
        Assert.assertEquals(3, books.size());

        List<Book> books2 = bookRepository.findAll(FenixSpecification.ofBean(new BookParam2().setOrOthers("others")));
        Assert.assertEquals(3, books2.size());
    }

}
