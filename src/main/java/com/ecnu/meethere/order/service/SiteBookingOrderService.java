package com.ecnu.meethere.order.service;

import com.ecnu.meethere.common.idgenerator.IdGenerator;
import com.ecnu.meethere.order.dao.SiteBookingOrderDao;
import com.ecnu.meethere.order.dto.SiteBookingOrderDTO;
import com.ecnu.meethere.order.entity.SiteBookingOrderDO;
import com.ecnu.meethere.order.exception.WrongOrderStatusException;
import com.ecnu.meethere.order.manager.SiteBookingOrderManager;
import com.ecnu.meethere.order.param.SiteBookingOrderCreateParam;
import com.ecnu.meethere.order.param.SiteBookingOrderStatus;
import com.ecnu.meethere.paging.PageParam;
import com.ecnu.meethere.site.bookingtime.service.SiteBookingTimeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SiteBookingOrderService {
    @Autowired
    private SiteBookingOrderManager siteBookingOrderManager;

    @Autowired
    private SiteBookingOrderDao siteBookingOrderDao;

    @Autowired
    private SiteBookingTimeService siteBookingTimeService;

    @Autowired
    private IdGenerator idGenerator;

    public void createOrder(Long userId, SiteBookingOrderCreateParam createParam) {
        siteBookingOrderDao.create(convertToSiteBookingOrderDO(userId, createParam));
    }

    private SiteBookingOrderDO convertToSiteBookingOrderDO(Long userId,
                                                           SiteBookingOrderCreateParam createParam) {
        SiteBookingOrderDO siteBookingOrderDO = new SiteBookingOrderDO();
        BeanUtils.copyProperties(createParam, siteBookingOrderDO);
        siteBookingOrderDO.setId(idGenerator.nextId());
        siteBookingOrderDO.setUserId(userId);
        siteBookingOrderDO.setStatus(SiteBookingOrderStatus.AUDITING);
        return siteBookingOrderDO;
    }

    @Transactional
    public void auditOrder(Long orderId) {
        SiteBookingOrderDTO order = siteBookingOrderManager.getOrder(orderId);

        if (order.getStatus() != SiteBookingOrderStatus.AUDITING)
            throw new WrongOrderStatusException();

        if (!siteBookingTimeService.tryBooking(order.getSiteId(), order.getStartTime(),
                order.getEndTime())) {
            siteBookingOrderDao.updateStatus(orderId, SiteBookingOrderStatus.AUDITING,
                    SiteBookingOrderStatus.AUDIT_FAILED);
        } else {
            siteBookingOrderDao.updateStatus(orderId, SiteBookingOrderStatus.AUDITING,
                    SiteBookingOrderStatus.AUDITED);
        }

        siteBookingOrderManager.diableOrderCache(orderId);
    }

    public void cancelOrder(Long userId, boolean isAdministrator, Long orderId) {
        SiteBookingOrderDTO order = siteBookingOrderManager.getOrder(orderId);


        if (!isAdministrator && !order.getUserId().equals(userId))
            return;

        if (order.getStatus() != SiteBookingOrderStatus.AUDITING && order.getStatus() != SiteBookingOrderStatus.AUDITED)
            throw new WrongOrderStatusException();

        switch (order.getStatus()) {
            case SiteBookingOrderStatus.AUDITED:
                siteBookingTimeService.cancelBooked(order.getSiteId(), order.getStartTime());
                siteBookingOrderDao.updateStatus(orderId, SiteBookingOrderStatus.AUDITED,
                        SiteBookingOrderStatus.CANCELLED);
                break;
            case SiteBookingOrderStatus.AUDITING:
                siteBookingOrderDao.updateStatus(orderId, SiteBookingOrderStatus.AUDITING,
                        SiteBookingOrderStatus.CANCELLED);
        }

        siteBookingOrderManager.diableOrderCache(orderId);
    }


    public List<SiteBookingOrderDTO> listOrdersBySite(Long siteId, int status,
                                                      PageParam pageParam) {
        return siteBookingOrderManager.listOrdersBySite(siteId, status, pageParam);
    }

    public List<SiteBookingOrderDTO> listOrdersByUser(Long userId, int status,
                                                      PageParam pageParam) {
        return siteBookingOrderManager.listOrdersByUser(userId, status, pageParam);
    }

    public SiteBookingOrderDTO getOrder(Long orderId) {
        return siteBookingOrderManager.getOrder(orderId);
    }


}
