package com.blinkfox.fenix.repository;

import com.alibaba.fastjson.JSON;
import com.blinkfox.fenix.FenixTestApplication;
import com.blinkfox.fenix.entity.Book;
import com.blinkfox.fenix.helper.StringHelper;
import com.blinkfox.fenix.specification.FenixSpecification;
import com.blinkfox.fenix.vo.BookParam;

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
     * 测试使用 {@code Specification} 的方式来根据名称模糊查询图书信息.
     */
    @Test
    public void testEquals() {
        BookParam bookParam = new BookParam().setIsbn("9787111641247");
        List<Book> books = bookRepository.findAll(FenixSpecification.of(bookParam));
        Assert.assertFalse(books.isEmpty());
        Assert.assertEquals(1, books.size());
        Assert.assertTrue(StringHelper.isNotBlank(books.get(0).getName()));
    }

    /**
     * 测试使用 {@code Specification} 的方式来根据名称模糊查询图书信息.
     */
    @Test
    public void testLike() {
        List<Book> books = bookRepository.findAll(FenixSpecification.of(new BookParam().setName("Java")));
        Assert.assertFalse(books.isEmpty());
        Assert.assertEquals(3, books.size());
        Assert.assertTrue(StringHelper.isNotBlank(books.get(0).getName()));
    }

    /**
     * 测试使用 {@code Specification} 的方式来根据名称模糊查询图书信息.
     */
    @Test
    public void testIn() {
        // 测试数组的情况.
        BookParam bookParam = new BookParam()
                .setId(Arrays.asList("1", "2", "3", "4", "5", "6", "7"));
        List<Book> books = bookRepository.findAll(FenixSpecification.of(bookParam));
        Assert.assertFalse(books.isEmpty());
        Assert.assertEquals(7, books.size());
        Assert.assertTrue(StringHelper.isNotBlank(books.get(0).getName()));
    }

}
