package com.ecnu.meethere.news.comment.manager;

import com.ecnu.meethere.news.comment.dao.NewsCommentDao;
import com.ecnu.meethere.news.comment.dto.NewsCommentDTO;
import com.ecnu.meethere.paging.PageParam;
import com.ecnu.meethere.redis.config.RedisExpiresConfig;
import com.ecnu.meethere.redis.config.RedisUtilsConfig;
import com.ecnu.meethere.redis.core.RedisUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@DataRedisTest(
        includeFilters =
        @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = NewsCommentManager.class
        )
)
@Import({
        RedisUtilsConfig.class,
        RedisExpiresConfig.class,
})
@ImportAutoConfiguration(RedisAutoConfiguration.class)
class NewsCommentManagerTest {
    @Autowired
    private NewsCommentManager newsCommentManager;

    @MockBean
    private NewsCommentDao newsCommentDao;

    @Autowired
    private RedisUtils redisUtils;

    @AfterEach
    void flushAll() {
        redisUtils.flushAll();
    }

    @Test
    void listComments() {
        when(newsCommentDao.listCommentIdsByPage(anyLong(), any())).thenReturn(List.of(-1L, -2L));
        when(newsCommentDao.listCommentsByIds(List.of(-1L, -2L))).thenReturn(List.of(
                new NewsCommentDTO(-1L, 1L, "", null),
                new NewsCommentDTO(-2L, 1L, "", null)
        ));

        //不命中
        List<NewsCommentDTO> newsComments = newsCommentManager.listComments(1L,
                new PageParam(1, 2));

        assertEquals(2, newsComments.size());

        //命中
        List<NewsCommentDTO> newsComments1 = newsCommentManager.listComments(1L,
                new PageParam(1, 2));

        assertEquals(2, newsComments1.size());
        verify(newsCommentDao, times(1)).listCommentsByIds(List.of(-1L, -2L));

    }

    @Test
    void listCommentsByIds() {
        when(newsCommentDao.listCommentsByIds(List.of(-1L, -2L))).thenReturn(List.of(
                new NewsCommentDTO(-1L, 1L, "", null),
                new NewsCommentDTO(-2L, 1L, "", null)
        ));

        //缓存不命中
        assertArrayEquals(new Long[]{-1L, -2L},
                newsCommentManager.listCommentsByIds(List.of(-1L, -2L)).stream().map(NewsCommentDTO::getId).toArray());

        //缓存命中
        assertArrayEquals(new Long[]{-1L, -2L},
                newsCommentManager.listCommentsByIds(List.of(-1L, -2L)).stream().map(NewsCommentDTO::getId).toArray());

        verify(newsCommentDao, times(1)).listCommentsByIds(List.of(-1L, -2L));

        reset(newsCommentDao);
        when(newsCommentDao.listCommentsByIds(List.of(-3L))).thenReturn(List.of(
                new NewsCommentDTO(-3L, 1L, "", null)
        ));

        //缓存不命中
        assertArrayEquals(new Long[]{-1L, -2L, -3L},
                newsCommentManager.listCommentsByIds(List.of(-1L, -2L, -3L)).stream().map(NewsCommentDTO::getId).toArray());

        //缓存命中
        assertArrayEquals(new Long[]{-1L, -2L, -3L},
                newsCommentManager.listCommentsByIds(List.of(-1L, -2L, -3L)).stream().map(NewsCommentDTO::getId).toArray());

        verify(newsCommentDao, times(1)).listCommentsByIds(List.of(-3L));


    }

    @Test
    void getComment() {
        when(newsCommentDao.getComment(-1L)).thenReturn((
                new NewsCommentDTO(-1L, 1L, "", null)
        ));

        assertEquals(-1L, newsCommentManager.getComment(-1L).getId());

        assertEquals(-1L, newsCommentManager.getComment(-1L).getId());

        verify(newsCommentDao, times(1)).getComment(-1L);

    }
}