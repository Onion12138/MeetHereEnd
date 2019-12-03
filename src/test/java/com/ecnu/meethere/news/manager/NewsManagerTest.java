package com.ecnu.meethere.news.manager;

import com.ecnu.meethere.news.dao.NewsDao;
import com.ecnu.meethere.news.dto.NewsDTO;
import com.ecnu.meethere.news.dto.NewsDigestDTO;
import com.ecnu.meethere.paging.PageParam;
import com.ecnu.meethere.redis.config.RedisExpiresConfig;
import com.ecnu.meethere.redis.config.RedisUtilsConfig;
import com.ecnu.meethere.redis.core.RedisUtils;
import com.ecnu.meethere.utils.ReflectionTestUtils;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@DataRedisTest(
        includeFilters =
        @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = NewsManager.class
        )
)
@Import({
        RedisUtilsConfig.class,
        RedisExpiresConfig.class,
})
@ImportAutoConfiguration(RedisAutoConfiguration.class)
class NewsManagerTest {
    @Autowired
    private NewsManager newsManager;

    @MockBean
    private NewsDao newsDao;

    @Autowired
    private RedisUtils redisUtils;

    @AfterEach
    void flushAll(){
        redisUtils.flushAll();
    }

    @Test
    void getNews() {
        when(newsDao.getNews(-1L)).thenReturn(
                new NewsDTO().setId(-1L).setUserId(1L).setTitle("title").setImage("").setContent(
                        "content").setTimeCreate(LocalDateTime.now())
        );
        NewsDTO news = newsManager.getNews(-1L);
        assertTrue(ReflectionTestUtils.isAllFieldsNotNull(news));
        verify(newsDao, times(1)).getNews(anyLong());

        newsManager.getNews(-1L);
        verify(newsDao, times(1)).getNews(anyLong());//走缓存，不走数据库
        assertTrue(ReflectionTestUtils.isAllFieldsNotNull(news));
    }

    @Test
    void listNewsDigests() {
        when(newsDao.listNews(List.of(-1L))).thenReturn(
                List.of(new NewsDTO().setId(-1L).setUserId(1L).setTitle("title").setImage("").setContent(
                        "content").setTimeCreate(LocalDateTime.now())));
        when(newsDao.listNewsDigestsIds(any())).thenReturn(List.of(-1L));

        List<NewsDigestDTO> newsDigests = newsManager.listNewsDigests(new PageParam(1, 1));
        assertEquals(1, newsDigests.size());
        assertTrue(ReflectionTestUtils.isAllFieldsNotNull(newsDigests.get(0)));

        List<NewsDigestDTO> newsDigests1 = newsManager.listNewsDigests(new PageParam(1, 1));
        assertEquals(1, newsDigests1.size());
        assertTrue(ReflectionTestUtils.isAllFieldsNotNull(newsDigests1.get(0)));

        verify(newsDao, times(1)).listNewsDigestsIds(any());
    }

    @Test
    void listNewsDigestsByNewsIds() {
        when(newsDao.listNews(List.of(-1L, -2L))).thenReturn(
                List.of(
                        new NewsDTO().setId(-1L).setUserId(1L).setTitle("title").setImage("").setContent(
                                "content").setTimeCreate(LocalDateTime.now()),
                        new NewsDTO().setId(-2L).setUserId(1L).setTitle("title").setImage("").setContent(
                                "content").setTimeCreate(LocalDateTime.now())
                )
        );

        //缓存不命中
        assertArrayEquals(new Long[]{-1L, -2L},
                newsManager.listNewsDigestsByNewsIds(List.of(-1L, -2L)).stream().map(NewsDigestDTO::getId).toArray());

        verify(newsDao, times(1)).listNews(anyList());

        //缓存命中
        assertArrayEquals(new Long[]{-1L, -2L},
                newsManager.listNewsDigestsByNewsIds(List.of(-1L, -2L)).stream().map(NewsDigestDTO::getId).toArray());

        verify(newsDao, times(1)).listNews(anyList());

        //缓存命中
        assertArrayEquals(new Long[]{-1L},
                newsManager.listNewsDigestsByNewsIds(List.of(-1L)).stream().map(NewsDigestDTO::getId).toArray());

        verify(newsDao, times(1)).listNews(anyList());

        reset(newsDao);

        //缓存不命中

        when(newsDao.listNews(List.of(-3L))).thenReturn(
                List.of(
                        new NewsDTO().setId(-3L).setUserId(1L).setTitle("title").setImage("").setContent(
                                "content").setTimeCreate(LocalDateTime.now())
                )
        );

        //缓存命中

        assertArrayEquals(new Long[]{-1L, -2L, -3L},
                newsManager.listNewsDigestsByNewsIds(List.of(-1L, -2L, -3L)).stream().map(NewsDigestDTO::getId).toArray());

        verify(newsDao, times(1)).listNews(anyList());

        //缓存命中

        assertArrayEquals(new Long[]{-2L, -3L},
                newsManager.listNewsDigestsByNewsIds(List.of(-2L, -3L)).stream().map(NewsDigestDTO::getId).toArray());

        verify(newsDao, times(1)).listNews(anyList());


    }
}