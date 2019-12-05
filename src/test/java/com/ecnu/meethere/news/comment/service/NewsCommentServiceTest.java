package com.ecnu.meethere.news.comment.service;

import com.ecnu.meethere.common.idgenerator.IdGenerator;
import com.ecnu.meethere.common.idgenerator.SnowflakeIdGenerator;
import com.ecnu.meethere.common.utils.CollectionUtils;
import com.ecnu.meethere.news.comment.dao.NewsCommentDao;
import com.ecnu.meethere.news.comment.dto.NewsCommentDTO;
import com.ecnu.meethere.news.comment.entity.NewsCommentDO;
import com.ecnu.meethere.news.comment.manager.NewsCommentManager;
import com.ecnu.meethere.news.comment.param.NewsCommentPostParam;
import com.ecnu.meethere.news.comment.param.NewsCommentUpdateParam;
import com.ecnu.meethere.news.comment.vo.NewsCommentVO;
import com.ecnu.meethere.news.dao.NewsDao;
import com.ecnu.meethere.paging.PageParam;
import com.ecnu.meethere.user.service.UserService;
import com.ecnu.meethere.user.vo.UserVO;
import com.ecnu.meethere.utils.ReflectionTestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NewsCommentServiceTest {
    @InjectMocks
    private NewsCommentService newsCommentService;

    @Mock(lenient = true)
    private NewsCommentManager newsCommentManager;

    @Mock
    private NewsCommentDao newsCommentDao;

    @Mock
    private UserService userService;

    @Spy
    private IdGenerator idGenerator = new SnowflakeIdGenerator(0, 0);

    @Test
    void postComment() {
        when(newsCommentDao.insertComment(any())).thenAnswer(invocation -> {
            NewsCommentDO newsCommentDO = invocation.getArgument(0);
            assertNotNull(newsCommentDO.getId());
            assertNotNull(newsCommentDO.getNewsId());
            assertNotNull(newsCommentDO.getContent());
            assertNotNull(newsCommentDO.getUserId());
            return 1;
        });

        newsCommentService.postComment(-1L, new NewsCommentPostParam(1L, ""));
    }

    @Test
    void listComments() {
        when(newsCommentManager.listComments(anyLong(), any()))
                .thenReturn(List.of(
                        new NewsCommentDTO(-1L, -1L, "", LocalDateTime.now()),
                        new NewsCommentDTO(-2L, -2L, "", LocalDateTime.now())
                ));

        when(userService.listUserVOs(List.of(-1L, -2L)))
                .thenReturn(List.of(
                        new UserVO(-1L, "", ""),
                        new UserVO(-2L, "", "")
                ));

        List<NewsCommentVO> newsCommentVOs = newsCommentService.listComments(-1L, new PageParam(1
                , 1));
        assertTrue(ReflectionTestUtils.isCollectionElementsAllFieldsNotNull(newsCommentVOs));
    }

    @ParameterizedTest
    @MethodSource("updateCommentGen")
    void updateComment(Long userId, boolean isAdministrator,
                       NewsCommentUpdateParam updateParam, int wTimes) {
        when(newsCommentManager.getComment(anyLong())).thenReturn(new NewsCommentDTO().setId(-1L).setUserId(-1L));
        newsCommentService.updateComment(userId, isAdministrator, updateParam);
        verify(newsCommentDao, times(wTimes)).updateComment(any());
    }

    static Stream<Arguments> updateCommentGen() {
        return Stream.of(
                of(-1L, false, new NewsCommentUpdateParam().setId(-1L), 1),
                of(-2L, true, new NewsCommentUpdateParam().setId(-1L), 1),
                of(-2L, false, new NewsCommentUpdateParam().setId(-1L), 0)
        );
    }

    @ParameterizedTest
    @MethodSource("deleteCommentGen")
    void deleteComment(Long userId, boolean isAdministrator,
                       Long id, int wTimes) {
        when(newsCommentManager.getComment(anyLong())).thenReturn(new NewsCommentDTO().setId(-1L).setUserId(-1L));
        newsCommentService.deleteComment(userId, isAdministrator, id);
        verify(newsCommentDao, times(wTimes)).deleteComment(any());
    }

    static Stream<Arguments> deleteCommentGen() {
        return Stream.of(
                of(-1L, false, -1L, 1),
                of(-2L, true, -1L, 1),
                of(-2L, false, -1L, 0)
        );
    }
}