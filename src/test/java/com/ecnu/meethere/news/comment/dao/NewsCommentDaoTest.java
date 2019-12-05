package com.ecnu.meethere.news.comment.dao;

import com.ecnu.meethere.news.comment.dto.NewsCommentDTO;
import com.ecnu.meethere.news.comment.entity.NewsCommentDO;
import com.ecnu.meethere.news.comment.param.NewsCommentUpdateParam;
import com.ecnu.meethere.paging.PageParam;
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
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class NewsCommentDaoTest {
    @Autowired
    private NewsCommentDao newsCommentDao;

    @BeforeEach
    void insertTestData() throws InterruptedException {
        LocalDateTime now = LocalDateTime.now();
        newsCommentDao.insertComment(new NewsCommentDO(-1L, 1L, 1L, "", now, now));
        newsCommentDao.insertComment(new NewsCommentDO(-2L, 1L, 1L, "", now, now));
        newsCommentDao.insertComment(new NewsCommentDO(-3L, 1L, 1L, "", now, now));
    }

    @ParameterizedTest
    @MethodSource("listCommentIdsByPageGen")
    void listCommentIdsByPage(PageParam pageParam, int wSize) {
        List<Long> commentIds = newsCommentDao.listCommentIdsByPage(1L, pageParam);
        assertEquals(wSize, commentIds.size());
    }

    static Stream<Arguments> listCommentIdsByPageGen() {
        return Stream.of(
                of(new PageParam(1, 1), 1),
                of(new PageParam(1, 2), 2),
                of(new PageParam(2, 1), 1),
                of(new PageParam(3, 1), 1),
                of(new PageParam(4, 1), 0)
        );
    }

    @ParameterizedTest
    @MethodSource("listCommentIdsGen")
    void listCommentIds(List<Long> commentIds, int wSize) {
        List<NewsCommentDTO> comments = newsCommentDao.listCommentsByIds(commentIds);
        assertEquals(wSize, comments.size());
    }

    static Stream<Arguments> listCommentIdsGen() {
        return Stream.of(
                of(null, 0),
                of(List.of(), 0),
                of(List.of(-1L), 1),
                of(List.of(-1L, -2L, -3L), 3),
                of(List.of(-1L, -2L, -4L), 2),
                of(List.of(-4L, -5L, -6L), 0)
        );
    }

    @Test
    void deleteComment() {
        assertEquals(1, newsCommentDao.deleteComment(-1L));
        assertEquals(0, newsCommentDao.deleteComment(0L));
    }

    @Test
    void updateComment() {
        assertEquals(1, newsCommentDao.updateComment(new NewsCommentUpdateParam(-1L, "")));
        assertEquals(1, newsCommentDao.updateComment(new NewsCommentUpdateParam(-1L, null)));
        assertEquals(0, newsCommentDao.updateComment(new NewsCommentUpdateParam(0L, null)));
    }

    @Test
    void getComment() {
        assertNotNull(newsCommentDao.getComment(-1L));
        assertNull(newsCommentDao.getComment(0L));
    }
}