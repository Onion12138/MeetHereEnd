package com.ecnu.meethere.order.controller;

import com.ecnu.meethere.common.result.CommonResult;
import com.ecnu.meethere.common.result.Result;
import com.ecnu.meethere.news.controller.NewsController;
import com.ecnu.meethere.news.service.NewsService;
import com.ecnu.meethere.order.exception.WrongOrderStatusException;
import com.ecnu.meethere.order.param.SiteBookingOrderCreateParam;
import com.ecnu.meethere.order.result.OrderResult;
import com.ecnu.meethere.order.service.SiteBookingOrderService;
import com.ecnu.meethere.paging.PageParam;
import com.ecnu.meethere.session.UserSessionInfo;
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
import java.time.LocalDateTime;

import static com.ecnu.meethere.utils.MockMvcTestUtils.jsonSerialize;
import static com.ecnu.meethere.utils.MockMvcTestUtils.parseMvcResult;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(SiteBookingOrderController.class)
class SiteBookingOrderControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private SiteBookingOrderService service;

    @Value("${user-session-info.name}")
    private String userSessionInfoName;

    private MockHttpSession session;

    @BeforeEach
    void before() {
        session = new MockHttpSession();
        session.setAttribute(userSessionInfoName, new UserSessionInfo(1L, "", "", true));
    }


    @Test
    void getOrder() throws Exception {
        MvcResult result = mvc.perform(
                get("/order/get")
                        .session(session)
                        .param("orderId", "1")
        ).andReturn();
        verify(service).getOrder(1L);
        Result<Object> res = parseMvcResult(result, null);
        assertEquals(CommonResult.SUCCESS, res.getCode());
    }

    @Test
    void listOrdersByUserId() throws Exception {
        MvcResult result = mvc.perform(
                get("/order/user/list")
                        .param("status", "0")
                        .param("pageNum", "1")
                        .session(session)
                        .param("pageSize", "1")
        ).andReturn();
        verify(service).listOrdersByUser(1L, 0, new PageParam(1, 1));
        Result<Object> res = parseMvcResult(result, null);
        assertEquals(CommonResult.SUCCESS, res.getCode());
    }

    @Test
    void listOrdersBySiteId() throws Exception {
        MvcResult result = mvc.perform(
                get("/order/site/list")
                        .param("siteId", "1")
                        .param("status", "0")
                        .param("pageNum", "1")
                        .session(session)
                        .param("pageSize", "1")
        ).andReturn();
        Result<Object> res = parseMvcResult(result, null);
        assertEquals(CommonResult.SUCCESS, res.getCode());
        verify(service).listOrdersBySite(1L, 0, new PageParam(1, 1));
    }

    @Test
    void createOrder() throws Exception {
        SiteBookingOrderCreateParam createParam = new SiteBookingOrderCreateParam(1L, "1", "1",
                BigDecimal.ONE, LocalDateTime.now(), LocalDateTime.now());
        MvcResult result = mvc.perform(
                post("/order/create")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonSerialize(createParam))
        ).andReturn();

        Result<Object> res = parseMvcResult(result, null);

        assertEquals(CommonResult.SUCCESS, res.getCode());
        verify(service).createOrder(1L, createParam);
    }

    @Test
    void auditOrder() throws Exception {
        MvcResult result = mvc.perform(
                post("/order/audit")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content("1")
        ).andReturn();

        Result<Object> res = parseMvcResult(result, null);

        assertEquals(CommonResult.SUCCESS, res.getCode());
        verify(service).auditOrder(1L);
    }

    @Test
    void cancelOrder() throws Exception {
        MvcResult result = mvc.perform(
                post("/order/cancel")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content("1")
        ).andReturn();

        Result<Object> res = parseMvcResult(result, null);

        assertEquals(CommonResult.SUCCESS, res.getCode());
        verify(service).cancelOrder(anyLong(), anyBoolean(), anyLong());

        doThrow(WrongOrderStatusException.class).when(service).cancelOrder(anyLong(), anyBoolean(),
                anyLong());
        res = parseMvcResult(mvc.perform(
                post("/order/cancel")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content("1")
        ).andReturn(),null);
        assertEquals(OrderResult.WRONG_ORDER_STATUS, res.getCode());

    }
}