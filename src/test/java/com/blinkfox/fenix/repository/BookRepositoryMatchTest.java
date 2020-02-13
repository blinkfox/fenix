package com.blinkfox.fenix.repository;

import com.alibaba.fastjson.JSON;
import com.blinkfox.fenix.FenixTestApplication;
import com.blinkfox.fenix.entity.Book;
import com.blinkfox.fenix.specification.handler.bean.BetweenValue;
import com.blinkfox.fenix.vo.param.BookMatch;

import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;

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
 * 基于 {@link BookRepository} 用来测试注解与匹配方法共同使用时的单元测试类.
 *
 * @author blinkfox on 2020-01-15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = FenixTestApplication.class)
public class BookRepositoryMatchTest {

    private static final String ID_1 = "1";

    private static final String ID_2 = "2";

    private static final int MIN_PAGE = 300;

    private static final int MAX_PAGE = 600;

    @Value("/data/book.json")
    private Resource bookResource;

    @Autowired
    private FenixBookRepository bookRepository;

    /**
     * 初始化 Fenix 配置信息，并从资源文件的中读取数据，初始化保存起来，便于后续读取或操作.
     */
    @PostConstruct
    public void init() throws IOException {
        bookRepository.saveAll(
                JSON.parseArray(new String(FileCopyUtils.copyToByteArray(bookResource.getFile())), Book.class));
    }

    /**
     * 测试使用 {@code Specification} 的方式来等值查询图书信息.
     */
    @Test
    public void testMatchWithId1() {
        // 测试匹配不到 id() 方法的情况.
        List<Book> books = bookRepository.findAllOfBean(
                new BookMatch().setId(ID_1));
        Assert.assertEquals(10, books.size());
    }

    /**
     * 测试使用 {@code Specification} 的方式来等值查询图书信息.
     */
    @Test
    public void testMatchWithId2() {
        // 测试匹配到 id() 方法的情况.
        List<Book> books = bookRepository.findAllOfBean(
                new BookMatch().setId(ID_2));
        Assert.assertEquals(1, books.size());
        Assert.assertEquals(ID_2, books.get(0).getId());
    }

    /**
     * 测试使用 {@code Specification} 的方式来等值查询图书信息.
     */
    @Test
    public void testMatch() {
        // 测试匹配到 id() 方法的情况.
        List<Book> books = bookRepository.findAllOfBean(
                new BookMatch().setId(ID_2).setTotalPageValue(BetweenValue.of(MIN_PAGE, MAX_PAGE)));
        Assert.assertEquals(4, books.size());
    }

}
