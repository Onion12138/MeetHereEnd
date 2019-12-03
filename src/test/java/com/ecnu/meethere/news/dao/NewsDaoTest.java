package com.ecnu.meethere.news.dao;

import com.ecnu.meethere.news.dto.NewsDTO;
import com.ecnu.meethere.news.dto.NewsDigestDTO;
import com.ecnu.meethere.news.entity.NewsDO;
import com.ecnu.meethere.paging.PageParam;
import com.ecnu.meethere.utils.ReflectionTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static com.ecnu.meethere.utils.ReflectionTestUtils.isAllFieldsNotNull;
import static com.ecnu.meethere.utils.ReflectionTestUtils.isCollectionElementsAllFieldsNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class NewsDaoTest {

    @Autowired
    private NewsDao newsDao;

    @BeforeEach
    void insertTestData() {
        newsDao.insertNews(new NewsDO().setId(-1L).setUserId(1L).setTitle("title").setImage("").setContent(
                "content"));
        newsDao.insertNews(new NewsDO().setId(-2L).setUserId(1L).setTitle("title").setImage("").setContent(
                "content"));
        newsDao.insertNews(new NewsDO().setId(-3L).setUserId(1L).setTitle("title").setImage("").setContent(
                "content"));
        newsDao.insertNews(new NewsDO().setId(-4L).setUserId(1L).setTitle("title").setImage("").setContent(
                "content"));
        newsDao.insertNews(new NewsDO().setId(-5L).setUserId(1L).setTitle("title").setImage("").setContent(
                "content"));
    }

    @ParameterizedTest
    @MethodSource("listNewsDigestGen")
    void listNewsDigest(PageParam pageParam, int wSize) {
        List<NewsDigestDTO> newsDigests = newsDao.listNewsDigest(pageParam);
        assertEquals(wSize, newsDigests.size());
        assertTrue(isCollectionElementsAllFieldsNotNull(newsDigests));
    }

    static Stream<Arguments> listNewsDigestGen() {
        return Stream.of(
                of(new PageParam(1, 1), 1),
                of(new PageParam(1, 5), 5),
                of(new PageParam(1, 6), 5),
                of(new PageParam(3, 2), 1),
                of(new PageParam(2, 2), 2),
                of(new PageParam(4, 2), 0)
        );
    }

    @Test
    void getNews() {
        NewsDTO news1 = newsDao.getNews(-1L);
        assertTrue(isAllFieldsNotNull(news1));

        assertNull(newsDao.getNews(-5L));
    }
}