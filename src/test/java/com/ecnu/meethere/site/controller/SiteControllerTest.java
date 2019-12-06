package com.ecnu.meethere.site.controller;

import com.ecnu.meethere.common.result.CommonResult;
import com.ecnu.meethere.common.result.Result;
import com.ecnu.meethere.news.controller.NewsController;
import com.ecnu.meethere.news.service.NewsService;
import com.ecnu.meethere.paging.PageParam;
import com.ecnu.meethere.session.UserSessionInfo;
import com.ecnu.meethere.site.param.SiteCreateParam;
import com.ecnu.meethere.site.param.SiteUpdateParam;
import com.ecnu.meethere.site.service.SiteService;
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

import java.math.BigDecimal;

import static com.ecnu.meethere.utils.MockMvcTestUtils.jsonSerialize;
import static com.ecnu.meethere.utils.MockMvcTestUtils.parseMvcResult;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(SiteController.class)
class SiteControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private SiteService siteService;

    @Value("${user-session-info.name}")
    private String userSessionInfoName;

    private MockHttpSession session ;

    @BeforeEach
    void before() {
        session = new MockHttpSession();
        session.setAttribute(userSessionInfoName, new UserSessionInfo(1L, "", "", true));
    }

    @Test
    void getSite() throws Exception {
        MvcResult result = mvc.perform(
                get("/site/get")
                        .param("siteId", "1")
        ).andReturn();
        verify(siteService).getSite(1L);
        Result<Object> res = parseMvcResult(result, null);
        assertEquals(CommonResult.SUCCESS, res.getCode());
    }

    @Test
    void listSites() throws Exception {
        MvcResult result = mvc.perform(
                get("/site/list")
                        .param("pageNum", "1")
                        .param("pageSize", "1")
        ).andReturn();
        verify(siteService).listSites(new PageParam(1, 1));
        Result<Object> res = parseMvcResult(result, null);
        assertEquals(CommonResult.SUCCESS, res.getCode());
    }

    @Test
    void createSite() throws Exception {
        SiteCreateParam createParam = new SiteCreateParam("1", "1", "1", BigDecimal.ONE, "1");
        MvcResult result = mvc.perform(
                post("/site/create")
                        .content(jsonSerialize(createParam))
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andReturn();
        verify(siteService).createSite(createParam);
        Result<Object> res = parseMvcResult(result, null);
        assertEquals(CommonResult.SUCCESS, res.getCode());
    }

    @Test
    void deleteSite() throws Exception {
        MvcResult result = mvc.perform(
                post("/site/delete")
                        .content("1")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andReturn();
        verify(siteService).deleteSite(1L);
        Result<Object> res = parseMvcResult(result, null);
        assertEquals(CommonResult.SUCCESS, res.getCode());
    }

    @Test
    void updateNews() throws Exception {
        SiteUpdateParam updateParam = new SiteUpdateParam(1L, "1", "1", "1", BigDecimal.ONE, "1");
        MvcResult result = mvc.perform(
                post("/site/update")
                        .content(jsonSerialize(updateParam))
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andReturn();
        verify(siteService).updateSite(updateParam);
        Result<Object> res = parseMvcResult(result, null);
        assertEquals(CommonResult.SUCCESS, res.getCode());
    }
}