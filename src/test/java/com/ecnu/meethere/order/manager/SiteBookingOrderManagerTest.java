package com.ecnu.meethere.order.manager;

import com.ecnu.meethere.order.dao.SiteBookingOrderDao;
import com.ecnu.meethere.order.dto.SiteBookingOrderDTO;
import com.ecnu.meethere.order.param.SiteBookingOrderStatus;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@DataRedisTest(
        includeFilters =
        @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = SiteBookingOrderManager.class
        )
)
@Import({
        RedisUtilsConfig.class,
        RedisExpiresConfig.class,
})
@ImportAutoConfiguration(RedisAutoConfiguration.class)
class SiteBookingOrderManagerTest {

    @Autowired
    private SiteBookingOrderManager orderManager;

    @MockBean
    private SiteBookingOrderDao orderDao;

    @Autowired
    private RedisUtils redisUtils;

    @AfterEach
    void flushAll() {
        redisUtils.flushAll();
    }

    @Test
    void listOrdersByUserId() {
        LocalDateTime now = LocalDateTime.now();
        when(orderDao.list(List.of(-1L))).thenReturn(
                List.of(
                        new SiteBookingOrderDTO(-1L, 1L, 1L,"t", "", BigDecimal.ONE,
                                SiteBookingOrderStatus.AUDITING, now, now, now, now)
                ));
        when(orderDao.listIdsByUser(anyLong(), anyInt(), any())).thenReturn(List.of(-1L));

        //缓存不命中
        List<SiteBookingOrderDTO> orders = orderManager.listOrdersByUser(-1L, 0, new PageParam(1, 1));
        assertEquals(1, orders.size());
        assertTrue(ReflectionTestUtils.isAllFieldsNotNull(orders.get(0)));

        //缓存命中
        List<SiteBookingOrderDTO> orders1 = orderManager.listOrdersByUser(-1L, 0, new PageParam(1, 1));
        assertEquals(1, orders1.size());
        assertTrue(ReflectionTestUtils.isAllFieldsNotNull(orders1.get(0)));

        verify(orderDao, times(1)).listIdsByUser(anyLong(), anyInt(), any());

    }

    @Test
    void listOrdersBySiteId() {
        LocalDateTime now = LocalDateTime.now();
        when(orderDao.list(List.of(-1L))).thenReturn(
                List.of(
                        new SiteBookingOrderDTO(-1L, 1L,1L, "t", "", BigDecimal.ONE,
                                SiteBookingOrderStatus.AUDITING, now, now, now, now)
                ));
        when(orderDao.listIdsBySite(anyLong(), anyInt(), any())).thenReturn(List.of(-1L));

        //缓存不命中
        List<SiteBookingOrderDTO> orders = orderManager.listOrdersBySite(-1L, 0, new PageParam(1, 1));
        assertEquals(1, orders.size());
        assertTrue(ReflectionTestUtils.isAllFieldsNotNull(orders.get(0)));

        //缓存命中
        List<SiteBookingOrderDTO> orders1 = orderManager.listOrdersBySite(-1L, 0, new PageParam(1, 1));
        assertEquals(1, orders1.size());
        assertTrue(ReflectionTestUtils.isAllFieldsNotNull(orders1.get(0)));

        verify(orderDao, times(1)).listIdsBySite(anyLong(), anyInt(), any());

    }

    @Test
    void getOrder() {
        LocalDateTime now = LocalDateTime.now();
        when(orderDao.get(-1L)).thenReturn(
                new SiteBookingOrderDTO(-1L, 1L,1L, "t", "", BigDecimal.ONE,
                        SiteBookingOrderStatus.AUDITING, now, now, now, now)
        );
        SiteBookingOrderDTO order = orderManager.getOrder(-1L);
        assertTrue(ReflectionTestUtils.isAllFieldsNotNull(order));
        verify(orderDao, times(1)).get(anyLong());

        orderManager.getOrder(-1L);
        verify(orderDao, times(1)).get(anyLong());//走缓存，不走数据库
        assertTrue(ReflectionTestUtils.isAllFieldsNotNull(order));
    }
}