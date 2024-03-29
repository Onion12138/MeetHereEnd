package com.ecnu.meethere.news.comment.controller;

import com.ecnu.meethere.common.result.CommonResult;
import com.ecnu.meethere.common.result.Result;
import com.ecnu.meethere.news.comment.param.NewsCommentPostParam;
import com.ecnu.meethere.news.comment.param.NewsCommentUpdateParam;
import com.ecnu.meethere.news.comment.service.NewsCommentService;
import com.ecnu.meethere.news.service.NewsService;
import com.ecnu.meethere.paging.PageParam;
import com.ecnu.meethere.session.UserSessionInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;

import static com.ecnu.meethere.utils.MockMvcTestUtils.jsonSerialize;
import static com.ecnu.meethere.utils.MockMvcTestUtils.parseMvcResult;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(NewsCommentController.class)
class NewsCommentControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private NewsCommentService newsCommentService;

    @Value("${user-session-info.name}")
    private String userSessionInfoName;

    private MockHttpSession session;

    @BeforeEach
    void before() {
        session = new MockHttpSession();
        session.setAttribute(userSessionInfoName, new UserSessionInfo(1L, "", "", true));
    }

    @Test
    void listComments() throws Exception {
        MvcResult result = mvc.perform(
                get("/news/comment/list")
                        .param("newsId", "1")
                        .param("pageNum", "1")
                        .session(session)
                        .param("pageSize", "1")
        ).andReturn();
        verify(newsCommentService).listComments(1L, new PageParam(1, 1));
        Result<Object> res = parseMvcResult(result, null);
        assertEquals(CommonResult.SUCCESS, res.getCode());
    }

    @Test
    void postComment() throws Exception {
        NewsCommentPostParam postParam = new NewsCommentPostParam(1L, "123");
        MvcResult result = mvc.perform(
                post("/news/comment/post")
                        .session(session)
                        .content(jsonSerialize(postParam))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andReturn();
        verify(newsCommentService).postComment(1L, postParam);

        Result<Object> res = parseMvcResult(result, null);
        assertEquals(CommonResult.SUCCESS, res.getCode());
    }

    @Test
    void updateComment() throws Exception {
        NewsCommentUpdateParam updateParam = new NewsCommentUpdateParam(1L, "123");
        MvcResult result = mvc.perform(
                post("/news/comment/update")
                        .session(session)
                        .content(jsonSerialize(updateParam))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andReturn();

        verify(newsCommentService).updateComment(1L, true, updateParam);


        Result<Object> res = parseMvcResult(result, null);
        assertEquals(CommonResult.SUCCESS, res.getCode());
    }

    @Test
    void deleteComment() throws Exception {
        MvcResult result = mvc.perform(
                post("/news/comment/delete")
                        .session(session)
                        .content(jsonSerialize(1L))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andReturn();
        verify(newsCommentService).deleteComment(1L, true, 1L);
        Result<Object> res = parseMvcResult(result, null);
        assertEquals(CommonResult.SUCCESS, res.getCode());
    }
}