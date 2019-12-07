package com.ecnu.meethere.news.dao;

import com.ecnu.meethere.common.utils.CollectionUtils;
import com.ecnu.meethere.news.dto.NewsDTO;
import com.ecnu.meethere.news.entity.NewsDO;
import com.ecnu.meethere.news.param.NewsUpdateParam;
import com.ecnu.meethere.paging.PageParam;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

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
        newsDao.insert(new NewsDO().setId(-1L).setUserId(1L).setTitle("title").setImage("").setContent(
                "content"));
        newsDao.insert(new NewsDO().setId(-2L).setUserId(1L).setTitle("title").setImage("").setContent(
                "content"));
        newsDao.insert(new NewsDO().setId(-3L).setUserId(1L).setTitle("title").setImage("").setContent(
                "content"));
        newsDao.insert(new NewsDO().setId(-4L).setUserId(1L).setTitle("title").setImage("").setContent(
                "content"));
        newsDao.insert(new NewsDO().setId(-5L).setUserId(1L).setTitle("title").setImage("").setContent(
                "content"));
    }

    @ParameterizedTest
    @MethodSource("listNewsDigestsGen")
    void listNewsDigests(PageParam pageParam, int wSize) {
        List<Long> newsDigests = newsDao.listIds(pageParam);
        assertEquals(wSize, newsDigests.size());
    }

    static Stream<Arguments> listNewsDigestsGen() {
        return Stream.of(
                of(new PageParam(1, 1), 1),
                of(new PageParam(1, 5), 5),
                of(new PageParam(1, 6), 5),
                of(new PageParam(3, 2), 1),
                of(new PageParam(2, 2), 2),
                of(new PageParam(4, 2), 0)
        );
    }

    @ParameterizedTest
    @MethodSource("listNewsGen")
    void listNews(List<Long> newsIds, int wSize) {
        List<NewsDTO> newsDigests = newsDao.list(newsIds);
        assertEquals(wSize, newsDigests.size());
        assertTrue(isCollectionElementsAllFieldsNotNull(newsDigests));
        if (newsIds != null) {
            //顺序id
            assertTrue(CollectionUtils.test(newsIds, newsDigests,
                    (id, newsDigest) -> id.equals(newsDigest.getId())));
        }
    }

    static Stream<Arguments> listNewsGen() {
        return Stream.of(
                of(null, 0),
                of(List.of(), 0),
                of(List.of(-1L), 1),
                of(List.of(-1L, -2L), 2),
                of(List.of(-1L, -2L, -3L, -4L, -5L), 5)
        );
    }

    @Test
    void getNews() {
        NewsDTO news1 = newsDao.get(-1L);
        assertTrue(isAllFieldsNotNull(news1));

        assertNull(newsDao.get(-7L));
    }

    @Test
    void deleteNews() {
        assertEquals(1, newsDao.delete(-1L));
    }

    @Test
    void updateNews() {
        assertEquals(1,
                newsDao.update(new NewsUpdateParam(-1L, null, null, null)));


        assertEquals(1,
                newsDao.update(new NewsUpdateParam(-1L, "你好", null, null)));

        assertEquals(1,
                newsDao.update(new NewsUpdateParam(-1L, "我好", "12", "23")));
    }
}