package com.ecnu.meethere.common.controller.advice;

import com.ecnu.meethere.common.result.CommonResult;
import com.ecnu.meethere.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.ecnu.meethere")
@Slf4j
public class GlobalControllerAdvice {
    @ExceptionHandler(RuntimeException.class)
    public Object handle(RuntimeException ex) {
        log.error(ex.getMessage());
        return CommonResult.failed();
    }

    @ExceptionHandler(ServletRequestBindingException.class)
    private Result<?> handle(ServletRequestBindingException ex) {
        return CommonResult.unauthorized();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private Result<?> handle(MethodArgumentNotValidException ex) {
        return CommonResult.dataBindingFailed();
    }

}
