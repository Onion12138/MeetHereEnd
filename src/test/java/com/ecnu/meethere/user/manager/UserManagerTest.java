package com.ecnu.meethere.user.manager;

import com.ecnu.meethere.news.dto.NewsDTO;
import com.ecnu.meethere.news.manager.NewsManager;
import com.ecnu.meethere.redis.config.RedisExpiresConfig;
import com.ecnu.meethere.redis.config.RedisUtilsConfig;
import com.ecnu.meethere.redis.core.RedisUtils;
import com.ecnu.meethere.user.dao.UserDao;
import com.ecnu.meethere.user.vo.UserVO;
import com.ecnu.meethere.utils.ReflectionTestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@DataRedisTest(
        includeFilters =
        @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = UserManager.class
        )
)
@Import({
        RedisUtilsConfig.class,
        RedisExpiresConfig.class,
})
@ImportAutoConfiguration(RedisAutoConfiguration.class)
class UserManagerTest {
    @Autowired
    private UserManager userManager;

    @MockBean
    private UserDao userDao;

    @Autowired
    private RedisUtils redisUtils;

    @AfterEach
    void flushAll(){
        redisUtils.flushAll();
    }

    @Test
    void getUserVO() {
        when(userDao.getUserVO(-1L)).thenReturn(
                new UserVO().setId(-1L).setUsername("123").setAvatar("456")
        );
        UserVO user = userManager.getUserVO(-1L);
        assertTrue(ReflectionTestUtils.isAllFieldsNotNull(user));
        verify(userDao, times(1)).getUserVO(anyLong());

        userManager.getUserVO(-1L);
        verify(userDao, times(1)).getUserVO(anyLong());//走缓存，不走数据库
        assertTrue(ReflectionTestUtils.isAllFieldsNotNull(user));
    }

    @Test
    void listUserVOs() {
        when(userDao.listUserVOs(List.of(-1L))).thenReturn(
                List.of(new UserVO(-1L, "", ""))
        );

        //缓存不命中
        assertArrayEquals(new Long[]{-1L},
                userManager.listUserVOs(List.of(-1L)).stream().map(UserVO::getId).toArray(Long[]::new));

        verify(userDao, times(1)).listUserVOs(anyList());

        //缓存命中
        assertArrayEquals(new Long[]{-1L},
                userManager.listUserVOs(List.of(-1L)).stream().map(UserVO::getId).toArray(Long[]::new));

        verify(userDao, times(1)).listUserVOs(anyList());

        reset(userDao);

        when(userDao.listUserVOs(List.of(-2L))).thenReturn(
                List.of(new UserVO(-2L, "", ""))
        );

        //缓存不命中
        assertArrayEquals(new Long[]{-1L,-2L},
                userManager.listUserVOs(List.of(-1L,-2L)).stream().map(UserVO::getId).toArray(Long[]::new));

        verify(userDao, times(1)).listUserVOs(List.of(-2L));

        //缓存命中
        assertArrayEquals(new Long[]{-1L,-2L},
                userManager.listUserVOs(List.of(-1L,-2L)).stream().map(UserVO::getId).toArray(Long[]::new));

        verify(userDao, times(1)).listUserVOs(List.of(-2L));
    }
}