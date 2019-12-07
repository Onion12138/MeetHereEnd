package com.ecnu.meethere.order.controller;

import com.ecnu.meethere.common.result.CommonResult;
import com.ecnu.meethere.common.result.Result;
import com.ecnu.meethere.order.param.SiteBookingOrderCreateParam;
import com.ecnu.meethere.order.param.SiteBookingOrderListBySiteParam;
import com.ecnu.meethere.order.param.SiteBookingOrderListByUserParam;
import com.ecnu.meethere.order.param.SiteBookingOrderStatus;
import com.ecnu.meethere.order.service.SiteBookingOrderService;
import com.ecnu.meethere.paging.PageParam;
import com.ecnu.meethere.session.UserSessionInfo;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/order/")
public class SiteBookingOrderController {
    @Autowired
    private SiteBookingOrderService service;

    @GetMapping(value = "get")
    public Result<?> getOrder(
            @SessionAttribute UserSessionInfo userSessionInfo,
            @RequestParam Long orderId) {
        return CommonResult.success().data(service.getOrder(orderId));
    }

    @GetMapping(value = "user/list")
    public Result<?> listOrdersByUserId(
            @SessionAttribute UserSessionInfo userSessionInfo,
            @RequestParam int status,
            @ModelAttribute PageParam pageParam) {
        return CommonResult.success().data(service.listOrdersByUser(userSessionInfo.getId(),
                status, pageParam));
    }

    @GetMapping(value = "site/list")
    public Result<?> listOrdersBySiteId(
            @SessionAttribute UserSessionInfo userSessionInfo,
            @RequestParam Long siteId,
            @RequestParam int status,
            @ModelAttribute PageParam pageParam) {
        if (!userSessionInfo.getIsAdministrator())
            return CommonResult.accessDenied();

        return CommonResult.success().data(service.listOrdersBySite(siteId,
                status, pageParam));
    }

    @PostMapping("create")
    public Result<?> createOrder(
            @SessionAttribute UserSessionInfo userSessionInfo,
            @RequestBody @Valid SiteBookingOrderCreateParam createParam
    ) {
        service.createOrder(userSessionInfo.getId(), createParam);
        return CommonResult.success();
    }

    @PostMapping("audit")
    public Result<?> auditOrder(
            @SessionAttribute UserSessionInfo userSessionInfo,
            @RequestBody Long orderId
    ) {
        if (!userSessionInfo.getIsAdministrator())
            return CommonResult.accessDenied();
        service.auditOrder(orderId);
        return CommonResult.success();
    }

    @PostMapping("cancel")
    public Result<?> cancelOrder(
            @SessionAttribute UserSessionInfo userSessionInfo,
            @RequestBody Long orderId
    ) {
        service.cancelOrder(userSessionInfo.getId(), userSessionInfo.getIsAdministrator(), orderId);
        return CommonResult.success();
    }

}
