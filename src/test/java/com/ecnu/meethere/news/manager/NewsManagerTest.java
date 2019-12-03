package com.ecnu.meethere.news.manager;

import com.ecnu.meethere.news.dao.NewsDao;
import com.ecnu.meethere.news.dto.NewsDTO;
import com.ecnu.meethere.news.entity.NewsDO;
import com.ecnu.meethere.redis.config.RedisExpiresConfig;
import com.ecnu.meethere.redis.config.RedisUtilsConfig;
import com.ecnu.meethere.utils.ReflectionTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.AutoConfigureMybatis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;

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

    @Test
    void getNews() {
        when(newsDao.getNews(-1L)).thenReturn(
                new NewsDTO().setId(-1L).setUserId(1L).setTitle("title").setImage("").setContent(
                        "content").setTimeCreate(LocalDateTime.now())
        );
        NewsDTO news = newsManager.getNews(-1L);
        assertTrue(ReflectionTestUtils.isAllFieldsNotNull(news));
        verify(newsDao,times(1)).getNews(anyLong());

        newsManager.getNews(-1L);
        verify(newsDao,times(1)).getNews(anyLong());//走缓存，不走数据库
        assertTrue(ReflectionTestUtils.isAllFieldsNotNull(news));
    }
}