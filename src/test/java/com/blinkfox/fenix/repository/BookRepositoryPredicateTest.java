package com.blinkfox.fenix.repository;

import com.alibaba.fastjson.JSON;
import com.blinkfox.fenix.FenixTestApplication;
import com.blinkfox.fenix.entity.Book;
import com.blinkfox.fenix.helper.CollectionHelper;
import com.blinkfox.fenix.helper.StringHelper;
import com.blinkfox.fenix.specification.FenixSpecification;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
 * 基于 {@link BookRepository} 用来测试 {@link org.springframework.data.jpa.domain.Specification} 和 {} 功能的单元测试类.
 *
 * @author blinkfox on 2020-01-21.
 * @since v2.2.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = FenixTestApplication.class)
public class BookRepositoryPredicateTest {

    private static final String ISBN = "9787111641247";

    private static final String ID_2 = "2";

    private static final int PAGE = 540;

    private static final String NAME = "Java";

    private static final String ENDS_NAME = "(algorithm)";

    private static Map<String, Object> paramMap;

    @Value("/data/book.json")
    private Resource bookResource;

    @Autowired
    private BookRepository bookRepository;

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
        paramMap.put("name", NAME);
        paramMap.put("startsName", NAME);
        paramMap.put("endsName", ENDS_NAME);
        paramMap.put("patternName", "_ava%");
        paramMap.put("idList", Arrays.asList("1", "2", "3", "4", "5", "6", "7"));
        paramMap.put("ids", new String[]{"1", "2", "3", "4", "5", "6", "7"});
    }

    /**
     * 测试使用 {@code Specification} 的方式来等值查询图书信息.
     */
    @Test
    public void testEmpty() {
        List<Book> books = bookRepository.findAll(FenixSpecification.of(builder -> {
            Assert.assertNotNull(builder.getCriteriaBuilder());
            Assert.assertNotNull(builder.getCriteriaQuery());
            Assert.assertNotNull(builder.getFrom());
            return builder.build();
        }));
        Assert.assertEquals(10, books.size());
    }

    /**
     * 测试使用 {@code Specification} 的方式来等值查询图书信息.
     */
    @Test
    public void testEquals() {
        List<Book> books = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andEquals("isbn", paramMap.get("isbn"))
                .build()));
        Assert.assertEquals(1, books.size());

        String isbn = (String) paramMap.get("isbn");
        List<Book> books2 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andEquals("isbn", isbn, StringHelper.isNotBlank(isbn))
                        .build()));
        Assert.assertEquals(1, books2.size());

        String isbn2 = (String) paramMap.get("isbn2");
        List<Book> books3 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andEquals("isbn", isbn2, StringHelper.isNotBlank(isbn2))
                        .build()));
        Assert.assertEquals(10, books3.size());
    }

    /**
     * 测试使用 {@code Specification} 的方式来等值查询图书信息.
     */
    @Test
    public void testOrEquals() {
        List<Book> books = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andEquals("isbn", paramMap.get("isbn"))
                        .orEquals("id", paramMap.get("id"))
                        .build()));
        Assert.assertEquals(2, books.size());

        String isbn = (String) paramMap.get("isbn");
        List<Book> books2 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andEquals("isbn", isbn)
                        .orEquals("id", paramMap.get("id"), false)
                        .build()));
        Assert.assertEquals(1, books2.size());
        Assert.assertEquals(ISBN, books2.get(0).getIsbn());

        String id = (String) paramMap.get("id");
        List<Book> books3 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andEquals("isbn", isbn, false)
                        .orEquals("id", id, StringHelper.isNotBlank(id))
                        .build()));
        Assert.assertEquals(1, books3.size());
        Assert.assertEquals(ID_2, books3.get(0).getId());
    }

    /**
     * 测试使用 {@code Specification} 的方式来等值查询图书信息.
     */
    @Test
    public void testNotEquals() {
        List<Book> books = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andNotEquals("isbn", paramMap.get("isbn"))
                        .build()));
        Assert.assertEquals(9, books.size());

        String isbn = (String) paramMap.get("isbn");
        List<Book> books2 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andNotEquals("isbn", isbn, StringHelper.isNotBlank(isbn))
                        .build()));
        Assert.assertEquals(9, books2.size());

        String isbn2 = (String) paramMap.get("isbn2");
        List<Book> books3 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andNotEquals("isbn", isbn2, StringHelper.isNotBlank(isbn2))
                        .build()));
        Assert.assertEquals(10, books3.size());
    }

    /**
     * 测试使用 {@code Specification} 的方式来等值查询图书信息.
     */
    @Test
    public void testOrNotEquals() {
        List<Book> books = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.orNotEquals("isbn", paramMap.get("isbn"))
                        .build()));
        Assert.assertEquals(9, books.size());

        String isbn = (String) paramMap.get("isbn");
        List<Book> books2 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.orNotEquals("isbn", isbn, false)
                        .build()));
        Assert.assertEquals(10, books2.size());

        List<Book> books3 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.orNotEquals("isbn", isbn, StringHelper.isNotBlank(isbn))
                        .build()));
        Assert.assertEquals(9, books3.size());
    }

    /**
     * 测试使用 {@code Specification} 的方式来大于查询图书信息.
     */
    @Test
    public void testGreaterThan() {
        List<Book> books = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andGreaterThan("totalPage", paramMap.get("totalPage"))
                        .build()));
        Assert.assertEquals(4, books.size());
        books.forEach(book -> Assert.assertTrue(book.getTotalPage() > PAGE));

        int totalPage = (Integer) paramMap.get("totalPage");
        List<Book> books2 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andGreaterThan("totalPage", totalPage, totalPage > 0)
                        .build()));
        Assert.assertEquals(4, books2.size());
        books2.forEach(book -> Assert.assertTrue(book.getTotalPage() > PAGE));

        List<Book> books3 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andGreaterThan("totalPage", totalPage, false)
                        .build()));
        Assert.assertEquals(10, books3.size());
    }

    /**
     * 测试使用 {@code Specification} 的方式来大于查询图书信息.
     */
    @Test
    public void testOrGreaterThan() {
        int totalPage = (Integer) paramMap.get("totalPage");
        List<Book> books = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andEquals("isbn", paramMap.get("isbn"))
                        .orGreaterThan("totalPage", totalPage)
                        .build()));
        Assert.assertEquals(5, books.size());

        String isbn = (String) paramMap.get("isbn");
        List<Book> books2 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andEquals("isbn", isbn)
                        .orGreaterThan("totalPage", totalPage, false)
                        .build()));
        Assert.assertEquals(1, books2.size());

        List<Book> books3 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andEquals("isbn", isbn, false)
                        .orGreaterThan("totalPage", totalPage, totalPage > 0)
                        .build()));
        Assert.assertEquals(4, books3.size());
        books3.forEach(book -> Assert.assertTrue(book.getTotalPage() > PAGE));
    }

    /**
     * 测试使用 {@code Specification} 的方式来大于等于查询图书信息.
     */
    @Test
    public void testGreaterThanEqual() {
        List<Book> books = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andGreaterThanEqual("totalPage", paramMap.get("totalPage"))
                        .build()));
        Assert.assertEquals(5, books.size());
        books.forEach(book -> Assert.assertTrue(book.getTotalPage() >= PAGE));

        int totalPage = (Integer) paramMap.get("totalPage");
        List<Book> books2 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andGreaterThanEqual("totalPage", totalPage, totalPage > 0)
                        .build()));
        Assert.assertEquals(5, books2.size());
        books2.forEach(book -> Assert.assertTrue(book.getTotalPage() >= PAGE));

        List<Book> books3 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andGreaterThanEqual("totalPage", totalPage, false)
                        .build()));
        Assert.assertEquals(10, books3.size());
    }

    /**
     * 测试使用 {@code Specification} 的方式来大于等于查询图书信息.
     */
    @Test
    public void testOrGreaterThanEqual() {
        int totalPage = (Integer) paramMap.get("totalPage");
        List<Book> books = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andEquals("id", paramMap.get("id"))
                        .orGreaterThanEqual("totalPage", totalPage)
                        .build()));
        Assert.assertEquals(6, books.size());

        String isbn = (String) paramMap.get("isbn");
        List<Book> books2 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andEquals("isbn", isbn)
                        .orGreaterThanEqual("totalPage", totalPage, false)
                        .build()));
        Assert.assertEquals(1, books2.size());

        List<Book> books3 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andEquals("isbn", isbn, false)
                        .orGreaterThanEqual("totalPage", totalPage, totalPage > 0)
                        .build()));
        Assert.assertEquals(5, books3.size());
        books3.forEach(book -> Assert.assertTrue(book.getTotalPage() >= PAGE));
    }

    /**
     * 测试使用 {@code Specification} 的方式来小于查询图书信息.
     */
    @Test
    public void testLessThan() {
        int totalPage = (Integer) paramMap.get("totalPage");
        List<Book> books = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andLessThan("totalPage", totalPage)
                        .build()));
        Assert.assertEquals(5, books.size());
        books.forEach(book -> Assert.assertTrue(book.getTotalPage() < PAGE));

        List<Book> books2 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andLessThan("totalPage", totalPage, totalPage > 0)
                        .build()));
        Assert.assertEquals(5, books2.size());
        books2.forEach(book -> Assert.assertTrue(book.getTotalPage() < PAGE));

        List<Book> books3 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andLessThan("totalPage", totalPage, false)
                        .build()));
        Assert.assertEquals(10, books3.size());
    }

    /**
     * 测试使用 {@code Specification} 的方式来小于查询图书信息.
     */
    @Test
    public void testOrLessThan() {
        int totalPage = (Integer) paramMap.get("totalPage");
        List<Book> books = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andEquals("isbn", paramMap.get("isbn"))
                        .orLessThan("totalPage", totalPage)
                        .build()));
        Assert.assertEquals(6, books.size());

        String isbn = (String) paramMap.get("isbn");
        List<Book> books2 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andEquals("isbn", isbn)
                        .orLessThan("totalPage", totalPage, false)
                        .build()));
        Assert.assertEquals(1, books2.size());

        List<Book> books3 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andEquals("isbn", isbn, false)
                        .orLessThan("totalPage", totalPage, totalPage > 0)
                        .build()));
        Assert.assertEquals(5, books3.size());
        books3.forEach(book -> Assert.assertTrue(book.getTotalPage() < PAGE));
    }

    /**
     * 测试使用 {@code Specification} 的方式来小于等于查询图书信息.
     */
    @Test
    public void testLessThanEqual() {
        List<Book> books = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andLessThanEqual("totalPage", paramMap.get("totalPage"))
                        .build()));
        Assert.assertEquals(6, books.size());
        books.forEach(book -> Assert.assertTrue(book.getTotalPage() <= PAGE));

        int totalPage = (Integer) paramMap.get("totalPage");
        List<Book> books2 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andLessThanEqual("totalPage", totalPage, totalPage > 0)
                        .build()));
        Assert.assertEquals(6, books2.size());
        books2.forEach(book -> Assert.assertTrue(book.getTotalPage() <= PAGE));

        List<Book> books3 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andLessThanEqual("totalPage", totalPage, false)
                        .build()));
        Assert.assertEquals(10, books3.size());
    }

    /**
     * 测试使用 {@code Specification} 的方式来小于等于查询图书信息.
     */
    @Test
    public void testOrLessThanEqual() {
        int totalPage = (Integer) paramMap.get("totalPage");
        List<Book> books = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andEquals("id", paramMap.get("id"))
                        .orLessThanEqual("totalPage", totalPage)
                        .build()));
        Assert.assertEquals(6, books.size());

        String isbn = (String) paramMap.get("isbn");
        List<Book> books2 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andEquals("isbn", isbn)
                        .orLessThanEqual("totalPage", totalPage, false)
                        .build()));
        Assert.assertEquals(1, books2.size());

        List<Book> books3 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andEquals("isbn", isbn, false)
                        .orLessThanEqual("totalPage", totalPage, totalPage > 0)
                        .build()));
        Assert.assertEquals(6, books3.size());
        books3.forEach(book -> Assert.assertTrue(book.getTotalPage() <= PAGE));
    }

    /**
     * 测试使用 {@code Specification} 的方式来模糊查询图书信息.
     */
    @Test
    public void testLike() {
        String name = (String) paramMap.get("name");
        List<Book> books = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andLike("name", name)
                        .build()));
        Assert.assertEquals(3, books.size());
        books.forEach(book -> Assert.assertTrue(book.getName().contains(NAME)));

        List<Book> books2 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andLike("name", name, StringHelper.isNotBlank(name))
                        .build()));
        Assert.assertEquals(3, books2.size());
        books2.forEach(book -> Assert.assertTrue(book.getName().contains(NAME)));

        List<Book> books3 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andLike("name", name, false)
                        .build()));
        Assert.assertEquals(10, books3.size());
    }

    /**
     * 测试使用 {@code Specification} 的方式来模糊查询图书信息.
     */
    @Test
    public void testOrLike() {
        String name = (String) paramMap.get("name");
        List<Book> books = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andEquals("id", ID_2)
                        .orLike("name", name)
                        .build()));
        Assert.assertEquals(4, books.size());

        List<Book> books2 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.orLike("name", name, StringHelper.isNotBlank(name))
                        .build()));
        Assert.assertEquals(3, books2.size());
        books2.forEach(book -> Assert.assertTrue(book.getName().contains(NAME)));

        List<Book> books3 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andEquals("id", ID_2)
                        .orLike("name", name, false)
                        .build()));
        Assert.assertEquals(1, books3.size());
        Assert.assertEquals(ID_2, books3.get(0).getId());
    }

    /**
     * 测试使用 {@code Specification} 的方式来模糊不匹配查询图书信息.
     */
    @Test
    public void testNotLike() {
        String name = (String) paramMap.get("name");
        List<Book> books = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andNotLike("name", name)
                        .build()));
        Assert.assertEquals(7, books.size());
        books.forEach(book -> Assert.assertFalse(book.getName().contains(NAME)));

        List<Book> books2 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andNotLike("name", name, StringHelper.isNotBlank(name))
                        .build()));
        Assert.assertEquals(7, books2.size());
        books2.forEach(book -> Assert.assertFalse(book.getName().contains(NAME)));

        List<Book> books3 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andNotLike("name", name, false)
                        .build()));
        Assert.assertEquals(10, books3.size());
    }

    /**
     * 测试使用 {@code Specification} 的方式来模糊不匹配查询图书信息.
     */
    @Test
    public void testOrNotLike() {
        String name = (String) paramMap.get("name");
        List<Book> books = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andEquals("id", ID_2)
                        .orNotLike("name", name)
                        .build()));
        Assert.assertEquals(7, books.size());
        books.forEach(book -> Assert.assertFalse(book.getName().contains(NAME)));

        List<Book> books2 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.orNotLike("name", name, StringHelper.isNotBlank(name))
                        .build()));
        Assert.assertEquals(7, books2.size());
        books2.forEach(book -> Assert.assertFalse(book.getName().contains(NAME)));

        List<Book> books3 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andEquals("id", ID_2)
                        .orNotLike("name", name, false)
                        .build()));
        Assert.assertEquals(1, books3.size());
        Assert.assertEquals(ID_2, books3.get(0).getId());
    }

    /**
     * 测试使用 {@code Specification} 的方式来按前缀模糊匹配查询图书信息.
     */
    @Test
    public void testStartsWith() {
        String startsName = (String) paramMap.get("startsName");
        List<Book> books = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andStartsWith("name", startsName)
                        .build()));
        Assert.assertEquals(2, books.size());
        books.forEach(book -> Assert.assertTrue(book.getName().contains(NAME)));

        List<Book> books2 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andStartsWith("name", startsName, StringHelper.isNotBlank(startsName))
                        .build()));
        Assert.assertEquals(2, books2.size());
        books2.forEach(book -> Assert.assertTrue(book.getName().contains(NAME)));

        List<Book> books3 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andStartsWith("name", startsName, false)
                        .build()));
        Assert.assertEquals(10, books3.size());
    }

    /**
     * 测试使用 {@code Specification} 的方式来按前缀模糊匹配查询图书信息.
     */
    @Test
    public void testOrStartsWith() {
        String startsName = (String) paramMap.get("startsName");
        List<Book> books = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andEquals("id", paramMap.get("id"))
                        .orStartsWith("name", startsName)
                        .build()));
        Assert.assertEquals(3, books.size());

        List<Book> books2 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andEquals("id", paramMap.get("id"))
                        .orStartsWith("name", startsName, StringHelper.isNotBlank(startsName))
                        .build()));
        Assert.assertEquals(3, books2.size());

        List<Book> books3 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andEquals("id", paramMap.get("id"))
                        .orStartsWith("name", startsName, false)
                        .build()));
        Assert.assertEquals(1, books3.size());
        Assert.assertEquals(ID_2, books3.get(0).getId());
    }

    /**
     * 测试使用 {@code Specification} 的方式来按后缀模糊匹配查询图书信息.
     */
    @Test
    public void testEndsWith() {
        String endsName = (String) paramMap.get("endsName");
        List<Book> books = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andEndsWith("name", endsName)
                        .build()));
        Assert.assertEquals(3, books.size());
        books.forEach(book -> Assert.assertTrue(book.getName().contains(ENDS_NAME)));

        List<Book> books2 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andEndsWith("name", endsName, StringHelper.isNotBlank(endsName))
                        .build()));
        Assert.assertEquals(3, books2.size());
        books2.forEach(book -> Assert.assertTrue(book.getName().contains(ENDS_NAME)));

        List<Book> books3 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andEndsWith("name", endsName, false)
                        .build()));
        Assert.assertEquals(10, books3.size());
    }

    /**
     * 测试使用 {@code Specification} 的方式来按后缀模糊匹配查询图书信息.
     */
    @Test
    public void testOrEndsWith() {
        String endsName = (String) paramMap.get("endsName");
        List<Book> books = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andEquals("id", paramMap.get("id"))
                        .orEndsWith("name", endsName)
                        .build()));
        Assert.assertEquals(4, books.size());

        List<Book> books2 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andEquals("id", paramMap.get("id"))
                        .orEndsWith("name", endsName, StringHelper.isNotBlank(endsName))
                        .build()));
        Assert.assertEquals(4, books2.size());

        List<Book> books3 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andEquals("id", paramMap.get("id"))
                        .orEndsWith("name", endsName, false)
                        .build()));
        Assert.assertEquals(1, books3.size());
        Assert.assertEquals(ID_2, books3.get(0).getId());
    }

    /**
     * 测试使用 {@code Specification} 的方式来模糊查询图书信息.
     */
    @Test
    public void testLikePattern() {
        String patternName = (String) paramMap.get("patternName");
        List<Book> books = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andLikePattern("name", patternName)
                        .build()));
        Assert.assertEquals(2, books.size());
        books.forEach(book -> Assert.assertTrue(book.getName().startsWith(NAME)));

        List<Book> books2 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andLikePattern("name", patternName, StringHelper.isNotBlank(patternName))
                        .build()));
        Assert.assertEquals(2, books2.size());
        books2.forEach(book -> Assert.assertTrue(book.getName().startsWith(NAME)));

        List<Book> books3 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andLikePattern("name", patternName, false)
                        .build()));
        Assert.assertEquals(10, books3.size());
    }

    /**
     * 测试使用 {@code Specification} 的方式来模糊查询图书信息.
     */
    @Test
    public void testOrLikePattern() {
        String patternName = (String) paramMap.get("patternName");
        List<Book> books = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andEquals("id", paramMap.get("id"))
                        .orLikePattern("name", patternName)
                        .build()));
        Assert.assertEquals(3, books.size());

        List<Book> books2 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andEquals("id", paramMap.get("id"))
                        .orLikePattern("name", patternName, StringHelper.isNotBlank(patternName))
                        .build()));
        Assert.assertEquals(3, books2.size());

        List<Book> books3 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andEquals("id", paramMap.get("id"))
                        .orLikePattern("name", patternName, false)
                        .build()));
        Assert.assertEquals(1, books3.size());
        Assert.assertEquals(ID_2, books3.get(0).getId());
    }

    /**
     * 测试使用 {@code Specification} 的方式来模糊查询图书信息.
     */
    @Test
    public void testNotLikePattern() {
        String patternName = (String) paramMap.get("patternName");
        List<Book> books = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andNotLikePattern("name", patternName)
                        .build()));
        Assert.assertEquals(8, books.size());
        books.forEach(book -> Assert.assertFalse(book.getName().startsWith(NAME)));

        List<Book> books2 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andNotLikePattern("name", patternName, StringHelper.isNotBlank(patternName))
                        .build()));
        Assert.assertEquals(8, books2.size());
        books2.forEach(book -> Assert.assertFalse(book.getName().startsWith(NAME)));

        List<Book> books3 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andNotLikePattern("name", patternName, false)
                        .build()));
        Assert.assertEquals(10, books3.size());
    }

    /**
     * 测试使用 {@code Specification} 的方式来模糊查询图书信息.
     */
    @Test
    public void testOrNotLikePattern() {
        String patternName = (String) paramMap.get("patternName");
        List<Book> books = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andEquals("id", paramMap.get("id"))
                        .orNotLikePattern("name", patternName)
                        .build()));
        Assert.assertEquals(8, books.size());

        List<Book> books2 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andEquals("id", paramMap.get("id"))
                        .orNotLikePattern("name", patternName, StringHelper.isNotBlank(patternName))
                        .build()));
        Assert.assertEquals(8, books2.size());

        List<Book> books3 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andEquals("id", paramMap.get("id"))
                        .orNotLikePattern("name", patternName, false)
                        .build()));
        Assert.assertEquals(1, books3.size());
        Assert.assertEquals(ID_2, books3.get(0).getId());
    }

    /**
     * 测试使用 {@code Specification} 的方式来范围查询图书信息.
     */
    @Test
    public void testIn() {
        @SuppressWarnings("unchecked")
        List<String> idList = (List<String>) paramMap.get("idList");
        List<Book> books = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andIn("id", idList)
                        .build()));
        Assert.assertEquals(7, books.size());
        books.forEach(book -> Assert.assertTrue(Integer.valueOf(book.getId()) <= 8));

        List<Book> books2 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andIn("id", idList, CollectionHelper.isNotEmpty(idList))
                        .build()));
        Assert.assertEquals(7, books2.size());
        books2.forEach(book -> Assert.assertTrue(Integer.valueOf(book.getId()) <= 8));

        List<Book> books3 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andIn("id", idList, false)
                        .build()));
        Assert.assertEquals(10, books3.size());

        String[] ids = (String[]) paramMap.get("ids");
        List<Book> books4 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andIn("id", ids)
                        .build()));
        Assert.assertEquals(7, books4.size());
        books4.forEach(book -> Assert.assertTrue(Integer.valueOf(book.getId()) <= 8));

        List<Book> books5 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andIn("id", ids, CollectionHelper.isNotEmpty(ids))
                        .build()));
        Assert.assertEquals(7, books5.size());
        books5.forEach(book -> Assert.assertTrue(Integer.valueOf(book.getId()) <= 8));

        List<Book> books6 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andIn("id", ids, false)
                        .build()));
        Assert.assertEquals(10, books6.size());
    }

    /**
     * 测试使用 {@code Specification} 的方式来范围查询图书信息.
     */
    @Test
    public void testOrIn() {
        @SuppressWarnings("unchecked")
        List<String> idList = (List<String>) paramMap.get("idList");
        List<Book> books = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.orIn("id", idList)
                        .build()));
        Assert.assertEquals(7, books.size());
        books.forEach(book -> Assert.assertTrue(Integer.valueOf(book.getId()) <= 8));

        List<Book> books2 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.orIn("id", idList, CollectionHelper.isNotEmpty(idList))
                        .build()));
        Assert.assertEquals(7, books2.size());
        books2.forEach(book -> Assert.assertTrue(Integer.valueOf(book.getId()) <= 8));

        List<Book> books3 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.orIn("id", idList, false)
                        .build()));
        Assert.assertEquals(10, books3.size());

        String[] ids = (String[]) paramMap.get("ids");
        List<Book> books4 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.orIn("id", ids)
                        .build()));
        Assert.assertEquals(7, books4.size());
        books4.forEach(book -> Assert.assertTrue(Integer.valueOf(book.getId()) <= 8));

        List<Book> books5 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.orIn("id", ids, CollectionHelper.isNotEmpty(ids))
                        .build()));
        Assert.assertEquals(7, books5.size());
        books5.forEach(book -> Assert.assertTrue(Integer.valueOf(book.getId()) <= 8));

        List<Book> books6 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.orIn("id", ids, false)
                        .build()));
        Assert.assertEquals(10, books6.size());
    }

    /**
     * 测试使用 {@code Specification} 的方式来范围不匹配查询图书信息.
     */
    @Test
    public void testNotIn() {
        @SuppressWarnings("unchecked")
        List<String> idList = (List<String>) paramMap.get("idList");
        List<Book> books = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andNotIn("id", idList)
                        .build()));
        Assert.assertEquals(3, books.size());
        books.forEach(book -> Assert.assertTrue(Integer.valueOf(book.getId()) > 7));

        List<Book> books2 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andNotIn("id", idList, CollectionHelper.isNotEmpty(idList))
                        .build()));
        Assert.assertEquals(3, books2.size());
        books2.forEach(book -> Assert.assertTrue(Integer.valueOf(book.getId()) > 7));

        List<Book> books3 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andNotIn("id", idList, false)
                        .build()));
        Assert.assertEquals(10, books3.size());

        String[] ids = (String[]) paramMap.get("ids");
        List<Book> books4 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andNotIn("id", ids)
                        .build()));
        Assert.assertEquals(3, books4.size());
        books4.forEach(book -> Assert.assertTrue(Integer.valueOf(book.getId()) > 7));

        List<Book> books5 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andNotIn("id", ids, CollectionHelper.isNotEmpty(ids))
                        .build()));
        Assert.assertEquals(3, books5.size());
        books5.forEach(book -> Assert.assertTrue(Integer.valueOf(book.getId()) > 7));

        List<Book> books6 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andNotIn("id", ids, false)
                        .build()));
        Assert.assertEquals(10, books6.size());
    }

    /**
     * 测试使用 {@code Specification} 的方式来范围不匹配查询图书信息.
     */
    @Test
    public void testOrNotIn() {
        @SuppressWarnings("unchecked")
        List<String> idList = (List<String>) paramMap.get("idList");
        List<Book> books = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.orNotIn("id", idList)
                        .build()));
        Assert.assertEquals(3, books.size());
        books.forEach(book -> Assert.assertTrue(Integer.valueOf(book.getId()) > 7));

        List<Book> books2 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.orNotIn("id", idList, CollectionHelper.isNotEmpty(idList))
                        .build()));
        Assert.assertEquals(3, books2.size());
        books2.forEach(book -> Assert.assertTrue(Integer.valueOf(book.getId()) > 7));

        List<Book> books3 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.orNotIn("id", idList, false)
                        .build()));
        Assert.assertEquals(10, books3.size());

        String[] ids = (String[]) paramMap.get("ids");
        List<Book> books4 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.orNotIn("id", ids)
                        .build()));
        Assert.assertEquals(3, books4.size());
        books4.forEach(book -> Assert.assertTrue(Integer.valueOf(book.getId()) > 7));

        List<Book> books5 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.orNotIn("id", ids, CollectionHelper.isNotEmpty(ids))
                        .build()));
        Assert.assertEquals(3, books5.size());
        books5.forEach(book -> Assert.assertTrue(Integer.valueOf(book.getId()) > 7));

        List<Book> books6 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.orNotIn("id", ids, false)
                        .build()));
        Assert.assertEquals(10, books6.size());
    }

    /**
     * 测试使用 {@code Specification} 的方式来判断字段数据是否为空的图书信息.
     */
    @Test
    public void testIsNull() {
        List<Book> books = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andIsNull("others")
                        .build()));
        Assert.assertEquals(7, books.size());
        books.forEach(book -> Assert.assertNull(book.getOthers()));

        List<Book> books2 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andIsNull("others", true)
                        .build()));
        Assert.assertEquals(7, books2.size());
        books2.forEach(book -> Assert.assertNull(book.getOthers()));

        List<Book> books3 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andIsNull("others", false)
                        .build()));
        Assert.assertEquals(10, books3.size());
    }

    /**
     * 测试使用 {@code Specification} 的方式来判断字段数据是否为空的图书信息.
     */
    @Test
    public void testOrIsNull() {
        List<Book> books = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.orIsNull("others")
                        .build()));
        Assert.assertEquals(7, books.size());
        books.forEach(book -> Assert.assertNull(book.getOthers()));

        List<Book> books2 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.orIsNull("others", true)
                        .build()));
        Assert.assertEquals(7, books2.size());
        books2.forEach(book -> Assert.assertNull(book.getOthers()));

        List<Book> books3 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.orIsNull("others", false)
                        .build()));
        Assert.assertEquals(10, books3.size());
    }

    /**
     * 测试使用 {@code Specification} 的方式来判断字段数据是否不为空的图书信息.
     */
    @Test
    public void testIsNotNull() {
        List<Book> books = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andIsNotNull("others")
                        .build()));
        Assert.assertEquals(3, books.size());
        books.forEach(book -> Assert.assertNotNull(book.getOthers()));

        List<Book> books2 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andIsNotNull("others", true)
                        .build()));
        Assert.assertEquals(3, books2.size());
        books2.forEach(book -> Assert.assertNotNull(book.getOthers()));

        List<Book> books3 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.andIsNotNull("others", false)
                        .build()));
        Assert.assertEquals(10, books3.size());
    }

    /**
     * 测试使用 {@code Specification} 的方式来判断字段数据是否不为空的图书信息.
     */
    @Test
    public void testOrIsNotNull() {
        List<Book> books = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.orIsNotNull("others")
                        .build()));
        Assert.assertEquals(3, books.size());
        books.forEach(book -> Assert.assertNotNull(book.getOthers()));

        List<Book> books2 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.orIsNotNull("others", true)
                        .build()));
        Assert.assertEquals(3, books2.size());
        books2.forEach(book -> Assert.assertNotNull(book.getOthers()));

        List<Book> books3 = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.orIsNotNull("others", false)
                        .build()));
        Assert.assertEquals(10, books3.size());
    }

}
