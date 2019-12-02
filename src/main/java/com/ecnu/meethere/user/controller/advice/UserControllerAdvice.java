package com.ecnu.meethere.user.controller.advice;

import com.ecnu.meethere.common.result.CommonResult;
import com.ecnu.meethere.user.controller.UserController;
import com.ecnu.meethere.user.exception.IncorrectUsernameOrPasswordException;
import com.ecnu.meethere.user.exception.UsernameAlreadyExistException;
import com.ecnu.meethere.user.exception.UsernameNotExistsException;
import com.ecnu.meethere.user.result.UserResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = UserController.class)
@Slf4j
public class UserControllerAdvice {
    @ExceptionHandler(UsernameAlreadyExistException.class)
    public Object handle(UsernameAlreadyExistException ex) {
        return UserResult.usernameAlreadyExists();
    }

    @ExceptionHandler(IncorrectUsernameOrPasswordException.class)
    public Object handle(IncorrectUsernameOrPasswordException ex) {
        return UserResult.incorrectUsernameOrPassword();
    }

    @ExceptionHandler(UsernameNotExistsException.class)
    public Object handle(UsernameNotExistsException ex) {
        return UserResult.usernameNotExists();
    }

    @ExceptionHandler(RuntimeException.class)
    public Object handle(RuntimeException ex) {
        log.error(ex.getMessage());
        return CommonResult.failed();
    }
}