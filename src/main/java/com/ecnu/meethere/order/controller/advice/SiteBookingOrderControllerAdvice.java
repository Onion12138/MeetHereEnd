package com.ecnu.meethere.order.controller.advice;

import com.ecnu.meethere.order.controller.SiteBookingOrderController;
import com.ecnu.meethere.order.exception.WrongOrderStatusException;
import com.ecnu.meethere.order.result.OrderResult;
import com.ecnu.meethere.user.exception.IncorrectUsernameOrPasswordException;
import com.ecnu.meethere.user.exception.UsernameAlreadyExistException;
import com.ecnu.meethere.user.exception.UsernameNotExistsException;
import com.ecnu.meethere.user.result.UserResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = SiteBookingOrderController.class)
@Slf4j
@Order(1)
public class SiteBookingOrderControllerAdvice {

    @ExceptionHandler(WrongOrderStatusException.class)
    public Object handle(WrongOrderStatusException ex) {
        return OrderResult.wrongOrderStatus();
    }
}