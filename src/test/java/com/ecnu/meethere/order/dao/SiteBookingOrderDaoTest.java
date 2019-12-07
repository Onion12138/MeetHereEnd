package com.ecnu.meethere.order.dao;

import com.ecnu.meethere.order.dto.SiteBookingOrderDTO;
import com.ecnu.meethere.order.entity.SiteBookingOrderDO;
import com.ecnu.meethere.order.param.SiteBookingOrderStatus;
import com.ecnu.meethere.paging.PageParam;
import com.ecnu.meethere.utils.ReflectionTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SiteBookingOrderDaoTest {
    @Autowired
    private SiteBookingOrderDao siteBookingOrderDao;

    @BeforeEach
    void insertTestData() {
        LocalDateTime now = LocalDateTime.now();
        siteBookingOrderDao.create(new SiteBookingOrderDO(-1L, -1L, -1L, "", "",
                BigDecimal.ONE, SiteBookingOrderStatus.AUDITING, now, now, now, now));
        siteBookingOrderDao.create(new SiteBookingOrderDO(-2L, -2L, -1L, "", "",
                BigDecimal.ONE, SiteBookingOrderStatus.AUDITING, now, now, now, now));
        siteBookingOrderDao.create(new SiteBookingOrderDO(-3L, -1L, -2L, "", "",
                BigDecimal.ONE, SiteBookingOrderStatus.AUDITING, now, now, now, now));
        siteBookingOrderDao.create(new SiteBookingOrderDO(-4L, -2L, -2L, "", "",
                BigDecimal.ONE, SiteBookingOrderStatus.AUDITING, now, now, now, now));

    }

    @ParameterizedTest
    @MethodSource("listOrderIdsBySiteIdGen")
    void listOrderIdsBySiteId(Long siteId, Integer status, PageParam pageParam, int wSize) {
        assertEquals(wSize,
                siteBookingOrderDao.listIdsBySite(siteId, status, pageParam).size());
    }

    static Stream<Arguments> listOrderIdsBySiteIdGen() {
        return Stream.of(
                Arguments.of(-1L, SiteBookingOrderStatus.AUDITING, new PageParam(1, 1), 1),
                Arguments.of(-1L, SiteBookingOrderStatus.AUDITING, new PageParam(1, 2), 2),
                Arguments.of(-1L, SiteBookingOrderStatus.AUDITING, new PageParam(1, 3), 2),
                Arguments.of(-2L, SiteBookingOrderStatus.AUDITING, new PageParam(2, 1), 1)
        );
    }

    @ParameterizedTest
    @MethodSource("listOrderIdsByUserIdGen")
    void listOrderIdsByUserId(Long userId, Integer status, PageParam pageParam, int wSize) {
        assertEquals(wSize,
                siteBookingOrderDao.listIdsByUser(userId, status, pageParam).size());

    }

    static Stream<Arguments> listOrderIdsByUserIdGen() {
        return Stream.of(
                Arguments.of(-1L, SiteBookingOrderStatus.AUDITING, new PageParam(1, 1), 1),
                Arguments.of(-1L, SiteBookingOrderStatus.AUDITING, new PageParam(1, 2), 2),
                Arguments.of(-1L, SiteBookingOrderStatus.AUDITING, new PageParam(1, 3), 2),
                Arguments.of(-2L, SiteBookingOrderStatus.AUDITING, new PageParam(2, 1), 1)
        );
    }

    @ParameterizedTest
    @MethodSource("listOrdersGen")
    void listOrders(List<Long> orderIds, int wSize) {
        List<SiteBookingOrderDTO> siteBookingOrderDTOs = siteBookingOrderDao.list(orderIds);
        assertEquals(wSize, siteBookingOrderDTOs.size());
        assertTrue(ReflectionTestUtils.isCollectionElementsAllFieldsNotNull(siteBookingOrderDTOs));
    }

    static Stream<Arguments> listOrdersGen() {
        return Stream.of(
                Arguments.of(null, 0),
                Arguments.of(List.of(), 0),
                Arguments.of(List.of(-1L), 1),
                Arguments.of(List.of(-1L, -2L, -3L), 3),
                Arguments.of(List.of(-1L, -2L, 0L), 2),
                Arguments.of(List.of(0L), 0)
        );
    }

    @Test
    void getOrder() {
        assertTrue(ReflectionTestUtils.isAllFieldsNotNull(siteBookingOrderDao.get(-1L)));
        assertNull(siteBookingOrderDao.get(0L));

    }
}