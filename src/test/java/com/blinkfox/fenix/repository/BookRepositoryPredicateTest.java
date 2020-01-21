package com.blinkfox.fenix.repository;

import com.alibaba.fastjson.JSON;
import com.blinkfox.fenix.FenixTestApplication;
import com.blinkfox.fenix.entity.Book;
import com.blinkfox.fenix.specification.FenixSpecification;

import java.io.IOException;
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

    private static final String DATE = "2014-11-01";

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
    }

    /**
     * 测试使用 {@code Specification} 的方式来等值查询图书信息.
     */
    @Test
    public void testEquals() {
        List<Book> books = bookRepository.findAll(FenixSpecification.of(builder ->
                builder.equal("isbn", paramMap.get("isbn"))
                .build()));
        Assert.assertEquals(1, books.size());
    }

}
