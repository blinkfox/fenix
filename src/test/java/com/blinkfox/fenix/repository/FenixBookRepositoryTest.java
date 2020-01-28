package com.blinkfox.fenix.repository;

import com.alibaba.fastjson.JSON;
import com.blinkfox.fenix.FenixTestApplication;
import com.blinkfox.fenix.entity.Book;
import com.blinkfox.fenix.specification.handler.bean.BetweenValue;
import com.blinkfox.fenix.vo.param.BookSearch;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.PostConstruct;

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
 * 基于 {@link BookRepository} 用来测试 {@link org.springframework.data.jpa.domain.Specification} 和 {} 功能的单元测试类.
 *
 * @author blinkfox on 2020-01-21.
 * @since v2.2.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = FenixTestApplication.class)
public class FenixBookRepositoryTest {

    private static final String ISBN = "9787111641247";

    private static final String ID_2 = "2";

    private static final int PAGE = 540;

    private static final int MIN_PAGE = 300;

    private static final int MAX_PAGE = 600;

    private static final String NAME = "Java";

    private static final String ENDS_NAME = "(algorithm)";

    private static Map<String, Object> paramMap;

    @Value("/data/book.json")
    private Resource bookResource;

    @Autowired
    private FenixBookRepository bookRepository;

    /**
     * 从资源文件的中读取数据，初始化保存起来，便于后续读取或操作.
     */
    @PostConstruct
    public void init() throws IOException {
        // 从 JSON 文件中初始化插入 10 条图书信息，用作测试动态查询时使用.
        bookRepository.saveAll(JSON.parseArray(
                new String(FileCopyUtils.copyToByteArray(bookResource.getFile())), Book.class));

        // 初始化一些上下文参数信息.
        paramMap = new HashMap<>();
        paramMap.put("isbn", ISBN);
        paramMap.put("id", ID_2);
        paramMap.put("totalPage", PAGE);
        paramMap.put("minTotalPage", MIN_PAGE);
        paramMap.put("maxTotalPage", MAX_PAGE);
        paramMap.put("name", NAME);
        paramMap.put("startsName", NAME);
        paramMap.put("endsName", ENDS_NAME);
        paramMap.put("patternName", "_ava%");
        paramMap.put("idList", Arrays.asList("1", "2", "3", "4", "5", "6", "7"));
        paramMap.put("ids", new String[]{"1", "2", "3", "4", "5", "6", "7"});
    }

    /**
     * 测试使用 {@code Specification} 的方式来查询图书信息.
     */
    @Test
    public void testFindOne() {
        Optional<Book> bookOptional = bookRepository.findOne(builder ->
                builder.andEquals("id", paramMap.get("id")).build());
        Assert.assertTrue(bookOptional.isPresent());
        Assert.assertEquals(ID_2, bookOptional.get().getId());
    }

    /**
     * 测试使用 {@code Specification} 的方式来查询图书信息.
     */
    @Test
    public void testFindOneOfBean() {
        // 模拟构造的图书查询的参数实体对象.
        BookSearch bookSearch = new BookSearch().setId(ID_2);

        // 查询并断言.
        Optional<Book> bookOptional = bookRepository.findOneOfBean(bookSearch);
        Assert.assertTrue(bookOptional.isPresent());
        Assert.assertEquals(ID_2, bookOptional.get().getId());
    }

    /**
     * 测试使用 {@code Specification} 的方式来查询图书信息.
     */
    @Test
    public void testFindAll() {
        List<Book> books = bookRepository.findAll(builder ->
                builder.andEquals("isbn", paramMap.get("isbn"))
                        .orBetween("totalPage", paramMap.get("minTotalPage"), paramMap.get("maxTotalPage"))
                .build());
        Assert.assertEquals(4, books.size());
    }

    /**
     * 测试使用 {@code Specification} 的方式来查询图书信息.
     */
    @Test
    public void testFindAllOfBean() {
        // 模拟构造的图书查询的参数实体对象.
        BookSearch bookSearch = new BookSearch()
                .setIsbn(ISBN)
                .setTotalPageValue(BetweenValue.of(MIN_PAGE, MAX_PAGE));

        List<Book> books = bookRepository.findAllOfBean(bookSearch);
        Assert.assertEquals(4, books.size());
    }

    /**
     * 测试使用 {@code Specification} 的方式来查询图书信息.
     */
    @Test
    public void testFindAllWithPage() {
        Page<Book> bookPage = bookRepository.findAll(builder ->
                builder.andEquals("isbn", paramMap.get("isbn"))
                        .orBetween("totalPage", paramMap.get("minTotalPage"), paramMap.get("maxTotalPage"))
                        .build(), PageRequest.of(1, 2, Sort.by(Sort.Order.desc("totalPage"))));

        Assert.assertEquals(4, bookPage.getTotalElements());
        List<Book> books = bookPage.getContent();
        Assert.assertEquals(2, books.size());
        Assert.assertTrue(books.get(0).getTotalPage() > books.get(1).getTotalPage());
    }

    /**
     * 测试使用 {@code Specification} 的方式来查询图书信息.
     */
    @Test
    public void testFindAllOfBeanWithPage() {
        // 模拟构造的图书查询的参数实体对象.
        BookSearch bookSearch = new BookSearch()
                .setIsbn(ISBN)
                .setTotalPageValue(BetweenValue.of(MIN_PAGE, MAX_PAGE));

        Page<Book> bookPage = bookRepository.findAllOfBean(bookSearch,
                PageRequest.of(1, 2, Sort.by(Sort.Order.desc("totalPage"))));

        Assert.assertEquals(4, bookPage.getTotalElements());
        List<Book> books = bookPage.getContent();
        Assert.assertEquals(2, books.size());
        Assert.assertTrue(books.get(0).getTotalPage() > books.get(1).getTotalPage());
    }

    /**
     * 测试使用 {@code Specification} 的方式来查询图书信息.
     */
    @Test
    public void testFindAllWithSort() {
        List<Book> books = bookRepository.findAll(builder ->
                builder.andEquals("isbn", paramMap.get("isbn"))
                        .orBetween("totalPage", paramMap.get("minTotalPage"), paramMap.get("maxTotalPage"))
                        .build(), Sort.by(Sort.Order.desc("totalPage")));

        Assert.assertEquals(4, books.size());
        Assert.assertTrue(books.get(0).getTotalPage() > books.get(1).getTotalPage());
    }

    /**
     * 测试使用 {@code Specification} 的方式来查询图书信息.
     */
    @Test
    public void testFindAllOfBeanWithSort() {
        // 模拟构造的图书查询的参数实体对象.
        BookSearch bookSearch = new BookSearch()
                .setIsbn(ISBN)
                .setTotalPageValue(BetweenValue.of(MIN_PAGE, MAX_PAGE));

        List<Book> books = bookRepository.findAllOfBean(bookSearch, Sort.by(Sort.Order.desc("totalPage")));
        Assert.assertEquals(4, books.size());
        Assert.assertTrue(books.get(0).getTotalPage() > books.get(1).getTotalPage());
    }

    /**
     * 测试使用 {@code Specification} 的方式来查询图书信息.
     */
    @Test
    public void testCount() {
        long count = bookRepository.count(builder ->
                builder.andEquals("isbn", paramMap.get("isbn"))
                        .orBetween("totalPage", paramMap.get("minTotalPage"), paramMap.get("maxTotalPage"))
                        .build());
        Assert.assertEquals(4, count);
    }

    /**
     * 测试使用 {@code Specification} 的方式来查询图书信息.
     */
    @Test
    public void testCountOfBean() {
        // 模拟构造的图书查询的参数实体对象.
        BookSearch bookSearch = new BookSearch()
                .setIsbn(ISBN)
                .setTotalPageValue(BetweenValue.of(MIN_PAGE, MAX_PAGE));

        long count = bookRepository.countOfBean(bookSearch);
        Assert.assertEquals(4, count);
    }

}
