package com.ecnu.meethere.user.service;

import com.ecnu.meethere.common.idgenerator.IdGenerator;
import com.ecnu.meethere.common.idgenerator.SnowflakeIdGenerator;
import com.ecnu.meethere.user.dao.UserDao;
import com.ecnu.meethere.user.dto.UserDTO;
import com.ecnu.meethere.user.exception.IncorrectUsernameOrPasswordException;
import com.ecnu.meethere.user.exception.UsernameAlreadyExistException;
import com.ecnu.meethere.user.exception.UsernameNotExistsException;
import com.ecnu.meethere.user.manager.UserManager;
import com.ecnu.meethere.user.param.LoginParam;
import com.ecnu.meethere.user.param.RegisterParam;
import com.ecnu.meethere.user.vo.UserVO;
import com.ecnu.meethere.utils.ReflectionTestUtils;
import com.mysql.cj.log.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;

import static org.junit.jupiter.params.provider.Arguments.*;

import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserManager userManager;

    @Mock
    private UserDao userDao;

    @Spy
    private IdGenerator idGenerator = new SnowflakeIdGenerator(0, 0);

    void before() {
        when(userDao.getUserByUsername(anyString()))
                .thenAnswer(invocation -> {
                    switch (((String) invocation.getArgument(0))) {
                        case "testu":
                            return new UserDTO(1L, "testu", "testp", "avatar", false);
                        default:
                            return null;
                    }
                });
    }

    @ParameterizedTest
    @MethodSource("loginGen")
    void login(LoginParam loginParam, Class<? extends Throwable> wEx) {
        before();
        if (wEx != null)
            assertThrows(wEx, () -> userService.login(loginParam));
        else
            assertTrue(ReflectionTestUtils.isAllFieldsNotNull(userService.login(loginParam)));
    }

    static Stream<Arguments> loginGen() {
        return Stream.of(
                of(new LoginParam("testu", "null"), IncorrectUsernameOrPasswordException.class),
                of(new LoginParam("null", "null"), UsernameNotExistsException.class),
                of(new LoginParam("testu", "testp"), null)
        );
    }

    @ParameterizedTest
    @MethodSource("registerGen")
    void register(RegisterParam registerParam, Class<? extends Throwable> wEx) {
        before();
        Executable target = () -> userService.register(registerParam);
        if (wEx != null)
            assertThrows(wEx, target);
        else
            assertDoesNotThrow(target);
    }

    static Stream<Arguments> registerGen() {
        return Stream.of(
                of(new RegisterParam("testu", "testp"), UsernameAlreadyExistException.class),
                of(new RegisterParam("null", "null"), null)
        );
    }

    @Test
    void getUserVO() {
        when(userManager.getUserVO(anyLong())).thenReturn(new UserVO().setId(-1L));

        assertEquals(-1L, userService.getUserVO(-1L).getId());
    }
}