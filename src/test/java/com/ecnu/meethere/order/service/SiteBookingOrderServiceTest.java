package com.ecnu.meethere.order.service;

import com.ecnu.meethere.common.idgenerator.IdGenerator;
import com.ecnu.meethere.common.idgenerator.SnowflakeIdGenerator;
import com.ecnu.meethere.order.dao.SiteBookingOrderDao;
import com.ecnu.meethere.order.dto.SiteBookingOrderDTO;
import com.ecnu.meethere.order.entity.SiteBookingOrderDO;
import com.ecnu.meethere.order.manager.SiteBookingOrderManager;
import com.ecnu.meethere.order.param.SiteBookingOrderCreateParam;
import com.ecnu.meethere.order.param.SiteBookingOrderStatus;
import com.ecnu.meethere.paging.PageParam;
import com.ecnu.meethere.site.bookingtime.service.SiteBookingTimeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SiteBookingOrderServiceTest {
    @InjectMocks
    private SiteBookingOrderService service;

    @Mock
    private SiteBookingOrderManager manager;

    @Mock
    private SiteBookingOrderDao dao;

    @Mock
    private SiteBookingTimeService bookingTimeService;

    @Spy
    private IdGenerator idGenerator = new SnowflakeIdGenerator(0, 0);


    @Test
    void createOrder() {
        when(dao.create(any())).thenAnswer(invocation -> {
            SiteBookingOrderDO o = invocation.getArgument(0);
            assertNotNull(o.getId());
            assertNotNull(o.getUserId());
            assertNotNull(o.getSiteId());
            assertNotNull(o.getSiteName());
            assertNotNull(o.getSiteImage());
            assertNotNull(o.getRent());
            assertNotNull(o.getStartTime());
            assertNotNull(o.getEndTime());
            assertNotNull(o.getStatus());
            return 1;
        });
        LocalDateTime now = LocalDateTime.now();
        service.createOrder(-1L, new SiteBookingOrderCreateParam(1L, "1", "1", BigDecimal.ONE,
                now, now));
    }

    @Test
    void auditOrder() {
        when(manager.getOrder(anyLong())).thenReturn(new SiteBookingOrderDTO().setId(1L).setSiteId(1L).setStatus(SiteBookingOrderStatus.AUDITING));
        when(bookingTimeService.tryBooking(anyLong(), any(), any())).thenReturn(false);
        service.auditOrder(1L);
        verify(dao).updateStatus(1L, SiteBookingOrderStatus.AUDITING,
                SiteBookingOrderStatus.AUDIT_FAILED);
        reset(dao);

        when(bookingTimeService.tryBooking(anyLong(), any(), any())).thenReturn(true);
        service.auditOrder(1L);
        verify(dao).updateStatus(1L, SiteBookingOrderStatus.AUDITING,
                SiteBookingOrderStatus.AUDITED);
    }

    @Test
    void cancelOrder() {
        when(manager.getOrder(anyLong())).thenReturn(new SiteBookingOrderDTO().setUserId(1L).setId(1L).setStatus(SiteBookingOrderStatus.AUDITING));

        service.cancelOrder(2L, false, 1L);
        verify(dao, times(0)).updateStatus(anyLong(), anyInt(), anyInt());

        service.cancelOrder(2L, true, 1L);
        verify(dao, times(1)).updateStatus(1L, SiteBookingOrderStatus.AUDITING,
                SiteBookingOrderStatus.CANCELLED);

        service.cancelOrder(1L, false, 1L);
        verify(dao, times(2)).updateStatus(1L, SiteBookingOrderStatus.AUDITING,
                SiteBookingOrderStatus.CANCELLED);


        when(manager.getOrder(anyLong())).thenReturn(new SiteBookingOrderDTO().setUserId(1L).setId(1L).setSiteId(1L).setStatus(SiteBookingOrderStatus.AUDITED));

        service.cancelOrder(1L, false, 1L);
        verify(dao, times(1)).updateStatus(1L, SiteBookingOrderStatus.AUDITED,
                SiteBookingOrderStatus.CANCELLED);
        verify(bookingTimeService).cancelBooked(anyLong(), any());

    }

    @Test
    void listOrdersBySiteId() {
        service.listOrdersBySite(-1L, SiteBookingOrderStatus.AUDIT_FAILED, new PageParam());
        verify(manager).listOrdersBySite(anyLong(), anyInt(), any());
    }

    @Test
    void listOrdersByUserId() {
        service.listOrdersByUser(-1L, SiteBookingOrderStatus.AUDIT_FAILED, new PageParam());
        verify(manager).listOrdersByUser(anyLong(), anyInt(), any());
    }

    @Test
    void getOrder() {
        service.getOrder(-1L);
        verify(manager).getOrder(anyLong());

    }
}