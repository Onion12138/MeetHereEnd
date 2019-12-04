package com.ecnu.meethere.user.controller;

import com.ecnu.meethere.common.result.CommonResult;
import com.ecnu.meethere.common.result.Result;
import com.ecnu.meethere.session.UserSessionInfo;
import com.ecnu.meethere.user.param.LoginParam;
import com.ecnu.meethere.user.param.RegisterParam;
import com.ecnu.meethere.user.result.UserResult;
import com.ecnu.meethere.user.service.UserService;
import com.ecnu.meethere.user.utils.UsernamePasswordValidUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;


    @PostMapping(value = "login")
    public Result<?> login(@Valid @RequestBody LoginParam loginParam,
                           BindingResult bindingResult,
                           @Value("${user-session-info.name}") String userSessionInfoName,
                           HttpServletRequest request) {
        if (request.getSession(false) != null)
            return CommonResult.failed();
        if (!UsernamePasswordValidUtils.isValidUsername(loginParam.getUsername()) || !UsernamePasswordValidUtils.isValidPassword(loginParam.getPassword()))
            return UserResult.incorrectUsernameOrPassword();

        UserSessionInfo userSessionInfo = userService.login(loginParam);

        HttpSession session = request.getSession(true);
        session.setAttribute(userSessionInfoName, userSessionInfo);

        return CommonResult.success().data(userSessionInfo);
    }

    @PostMapping("register")
    public Result<?> register(@Valid @RequestBody RegisterParam registerParam,
                              BindingResult bindingResult) {
        if (!UsernamePasswordValidUtils.isValidUsername(registerParam.getUsername()) || !UsernamePasswordValidUtils.isValidPassword(registerParam.getPassword()))
            return UserResult.incorrectUsernameOrPassword();
        userService.register(registerParam);
        return CommonResult.success();
    }
}
