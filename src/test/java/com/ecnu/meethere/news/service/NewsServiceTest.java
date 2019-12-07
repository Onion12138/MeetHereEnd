package com.ecnu.meethere.news.service;

import com.ecnu.meethere.common.idgenerator.IdGenerator;
import com.ecnu.meethere.common.idgenerator.SnowflakeIdGenerator;
import com.ecnu.meethere.news.dao.NewsDao;
import com.ecnu.meethere.news.dto.NewsDTO;
import com.ecnu.meethere.news.dto.NewsDigestDTO;
import com.ecnu.meethere.news.manager.NewsManager;
import com.ecnu.meethere.news.param.NewsPublishParam;
import com.ecnu.meethere.news.vo.NewsDigestVO;
import com.ecnu.meethere.paging.PageParam;
import com.ecnu.meethere.user.service.UserService;
import com.ecnu.meethere.user.vo.UserVO;
import com.ecnu.meethere.utils.ReflectionTestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NewsServiceTest {
    @InjectMocks
    private NewsService newsService;

    @Mock
    private NewsManager newsManager;

    @Mock
    private UserService userService;

    @Mock
    private NewsDao newsDao;

    @Spy
    private IdGenerator idGenerator = new SnowflakeIdGenerator(0, 0);

    @Test
    void publishNews() {
        newsService.publishNews(-1L,
                new NewsPublishParam().setTitle("title").setImage("image").setContent("content"));
        verify(newsDao, times(1)).insert(any());
    }

    @Test
    void deleteNews() {
        newsService.deleteNews(-1L);
        verify(newsDao, times(1)).delete(anyLong());

    }

    @Test
    void getNews() {
        when(userService.getUserVO(anyLong())).thenReturn(new UserVO());
        when(newsManager.getNews(-1L)).thenReturn(
                new NewsDTO(-1L, -1L, "", "", "", LocalDateTime.now())
        );

        assertTrue(ReflectionTestUtils.isAllFieldsNotNull(newsService.getNews(-1L)));
        verify(newsManager, times(1)).getNews(anyLong());
    }

    @Test
    void listNewsDigests() {
        when(newsManager.listNewsDigests(any())).thenReturn(List.of(
                new NewsDigestDTO(-1L, 1L, "", "", LocalDateTime.now()),
                new NewsDigestDTO(-1L, 2L, "", "", LocalDateTime.now())));
        when(userService.listUserVOs(List.of(1L, 2L))).thenReturn(List.of(
                new UserVO(1L, "1", "1"),
                new UserVO(2L, "2", "2")
        ));

        List<NewsDigestVO> newsDigestVOs = newsService.listNewsDigests(new PageParam());
        assertTrue(ReflectionTestUtils.isCollectionElementsAllFieldsNotNull(newsDigestVOs));
    }

    @Test
    void updateNews() {
        newsService.updateNews(any());
    }
}