package com.ecnu.meethere.news.controller;

import com.ecnu.meethere.common.result.CommonResult;
import com.ecnu.meethere.common.result.Result;
import com.ecnu.meethere.news.param.NewsPublishParam;
import com.ecnu.meethere.news.param.NewsUpdateParam;
import com.ecnu.meethere.news.service.NewsService;
import com.ecnu.meethere.session.UserSessionInfo;
import com.ecnu.meethere.user.controller.UserController;
import com.ecnu.meethere.user.service.UserService;
import org.aspectj.lang.annotation.Aspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;

import static com.ecnu.meethere.utils.MockMvcTestUtils.jsonSerialize;
import static com.ecnu.meethere.utils.MockMvcTestUtils.parseMvcResult;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(NewsController.class)
class NewsControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private NewsService newsService;

    @Value("${user-session-info.name}")
    private String userSessionInfoName;
    
    @Test
    void getNews() throws Exception {
        MvcResult result = mvc.perform(
                get("/news/get")
                        .param("newsId", "1")
        ).andReturn();
        Result<Object> res = parseMvcResult(result, null);
        assertEquals(CommonResult.SUCCESS, res.getCode());
    }

    @Test
    void listNewsDigests() throws Exception {
        MvcResult result = mvc.perform(
                get("/news/digest/list")
                        .param("pageNum", "1")
                        .param("pageSize", "1")
        ).andReturn();
        Result<Object> res = parseMvcResult(result, null);
        assertEquals(CommonResult.SUCCESS, res.getCode());

    }

    @Test
    void publishNews() throws Exception {
        NewsPublishParam publishParam = new NewsPublishParam()
                .setTitle("1").setContent("1").setImage("1");
        // miss Session Info
        MvcResult result = mvc.perform(
                post("/news/publish")
                        .content(jsonSerialize(publishParam))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andReturn();

        assertEquals(CommonResult.UNAUTHORIZED, parseMvcResult(result, null).getCode());

        MockHttpSession session = new MockHttpSession();
        UserSessionInfo sessionInfo = new UserSessionInfo();
        sessionInfo.setId(1L).setIsAdministrator(false);
        session.setAttribute(userSessionInfoName, sessionInfo);

        //不是管理员
        result = mvc.perform(
                post("/news/publish")
                        .content(jsonSerialize(publishParam))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .session(session)
        ).andReturn();

        assertEquals(CommonResult.ACCESS_DENIED, parseMvcResult(result, null).getCode());

        //管理员
        sessionInfo.setIsAdministrator(true);

        result = mvc.perform(
                post("/news/publish")
                        .content(jsonSerialize(publishParam))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .session(session)
        ).andReturn();

        assertEquals(CommonResult.SUCCESS, parseMvcResult(result, null).getCode());

    }

    @Test
    void deleteNews() throws Exception {


        MockHttpSession session = new MockHttpSession();
        UserSessionInfo sessionInfo = new UserSessionInfo();
        sessionInfo.setId(1L).setIsAdministrator(false);
        session.setAttribute(userSessionInfoName, sessionInfo);

        //不是管理员
        MvcResult result = mvc.perform(
                post("/news/delete")
                        .content("-1")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .session(session)
        ).andReturn();

        assertEquals(CommonResult.ACCESS_DENIED, parseMvcResult(result, null).getCode());

        //管理员
        sessionInfo.setIsAdministrator(true);

        result = mvc.perform(
                post("/news/delete")
                        .content("-1")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .session(session)
        ).andReturn();

        assertEquals(CommonResult.SUCCESS, parseMvcResult(result, null).getCode());
    }

    @Test
    void updateNews() throws Exception {
        NewsUpdateParam updateParam = new NewsUpdateParam(1L, "1", "1", "1");


        MockHttpSession session = new MockHttpSession();
        UserSessionInfo sessionInfo = new UserSessionInfo();
        sessionInfo.setId(1L).setIsAdministrator(false);
        session.setAttribute(userSessionInfoName, sessionInfo);

        //不是管理员
        MvcResult result = mvc.perform(
                post("/news/update")
                        .content(jsonSerialize(updateParam))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .session(session)
        ).andReturn();

        assertEquals(CommonResult.ACCESS_DENIED, parseMvcResult(result, null).getCode());

        //管理员
        sessionInfo.setIsAdministrator(true);

        result = mvc.perform(
                post("/news/update")
                        .content(jsonSerialize(updateParam))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .session(session)
        ).andReturn();

        assertEquals(CommonResult.SUCCESS, parseMvcResult(result, null).getCode());
    }
}