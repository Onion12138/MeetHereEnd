package com.ecnu.meethere.user.controller;

import com.ecnu.meethere.common.aspect.BindingResultAspect;
import com.ecnu.meethere.common.result.CommonResult;
import com.ecnu.meethere.common.result.Result;
import com.ecnu.meethere.user.exception.IncorrectUsernameOrPasswordException;
import com.ecnu.meethere.user.exception.UsernameAlreadyExistException;
import com.ecnu.meethere.user.exception.UsernameNotExistsException;
import com.ecnu.meethere.user.param.LoginParam;
import com.ecnu.meethere.user.param.RegisterParam;
import com.ecnu.meethere.user.result.UserResult;
import com.ecnu.meethere.user.service.UserService;
import com.ecnu.meethere.utils.MockMvcTestUtils;
import org.aspectj.lang.annotation.Aspect;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.stream.Stream;

import static com.ecnu.meethere.utils.MockMvcTestUtils.jsonSerialize;
import static com.ecnu.meethere.utils.MockMvcTestUtils.parseMvcResult;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(
        value = UserController.class,
        includeFilters = @ComponentScan.Filter(Aspect.class)
)
@ImportAutoConfiguration(AopAutoConfiguration.class)
class UserControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @ParameterizedTest
    @MethodSource("loginGen")
    void login(LoginParam loginParam, Class<? extends Throwable> throwOnService, int code) throws Exception {
        if(throwOnService != null)
            when(userService.login(any())).thenThrow(throwOnService);
        MvcResult result = mvc.perform(
                post("/user/login")
                        .content(jsonSerialize(loginParam))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andReturn();
        Result<Object> res = parseMvcResult(result, null);
        assertEquals(code, res.getCode());
    }

    static Stream<Arguments> loginGen() {
        return Stream.of(
                //切面
                of(
                        new LoginParam(null, null),
                        null ,
                        CommonResult.DATA_BINDING_FAILED),
                of(
                        new LoginParam("123456789", "123"),
                        null,
                        CommonResult.DATA_BINDING_FAILED),
                //valid utils
                of(
                        new LoginParam("123456789", "一二三四五六七八"),
                        null,
                        UserResult.INCORRECT_USERNAME_OR_PASSWORD),
                of(
                        new LoginParam("123", "一二三四五六七八"),
                        null,
                        UserResult.INCORRECT_USERNAME_OR_PASSWORD),
                //throw exception
                of(
                        new LoginParam("123456789", "123456789"),
                        IncorrectUsernameOrPasswordException.class,
                        UserResult.INCORRECT_USERNAME_OR_PASSWORD),
                of(
                        new LoginParam("123456789", "123456789"),
                        UsernameNotExistsException.class,
                        UserResult.USERNAME_NOT_EXISTS),
                of(
                        new LoginParam("123456789", "123456789"),
                        RuntimeException.class,
                        CommonResult.FAILED),
                of(
                        new LoginParam("123456789", "123456789"),
                        null,
                        CommonResult.SUCCESS)
        );
    }
    
    @ParameterizedTest
    @MethodSource("registerGen")
    void register(RegisterParam registerParam, Class<? extends Throwable> throwOnService, int code) throws Exception {
        if(throwOnService != null)
            doThrow(throwOnService).when(userService).register(any());
        MvcResult result = mvc.perform(
                post("/user/register")
                        .content(jsonSerialize(registerParam))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andReturn();
        Result<Object> res = parseMvcResult(result, null);
        assertEquals(code, res.getCode());
    }
    
    static Stream<Arguments> registerGen(){
        return Stream.of(
                //valid utils
                of(
                        new RegisterParam("123456789", "一二三四五六七八"),
                        null,
                        UserResult.INCORRECT_USERNAME_OR_PASSWORD),
                of(
                        new RegisterParam("123", "一二三四五六七八"),
                        null,
                        UserResult.INCORRECT_USERNAME_OR_PASSWORD),
                //throw exception
                of(
                        new RegisterParam("123456789", "123456789"),
                        UsernameAlreadyExistException.class,
                        UserResult.USERNAME_ALREADY_EXISTS)
        );
    }
}