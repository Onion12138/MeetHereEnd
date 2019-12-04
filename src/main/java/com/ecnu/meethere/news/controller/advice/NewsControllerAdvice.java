package com.ecnu.meethere.news.controller.advice;

import com.ecnu.meethere.common.result.CommonResult;
import com.ecnu.meethere.common.result.Result;
import com.ecnu.meethere.news.controller.NewsController;
import com.ecnu.meethere.user.controller.UserController;
import com.ecnu.meethere.user.exception.IncorrectUsernameOrPasswordException;
import com.ecnu.meethere.user.exception.UsernameAlreadyExistException;
import com.ecnu.meethere.user.exception.UsernameNotExistsException;
import com.ecnu.meethere.user.result.UserResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = NewsController.class)
@Slf4j
public class NewsControllerAdvice {

    @ExceptionHandler(RuntimeException.class)
    public Object handle(RuntimeException ex) {
        log.error(ex.getMessage());
        return CommonResult.failed();
    }

    @Value("${userSessionInfo.name}")
    private String userSessionInfoName;

    @ExceptionHandler(ServletRequestBindingException.class)
    private Result<?> handle(ServletRequestBindingException ex) {
        return CommonResult.unauthorized();
    }

}