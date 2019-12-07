package com.ecnu.meethere.user.manager;

import com.ecnu.meethere.common.annotation.Manager;
import com.ecnu.meethere.common.utils.CacheUtils;
import com.ecnu.meethere.redis.core.RedisExpires;
import com.ecnu.meethere.redis.core.RedisUtils;
import com.ecnu.meethere.user.dao.UserDao;
import com.ecnu.meethere.user.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Manager
public class UserManager {
    @Autowired
    private UserDao userDao;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    @Qualifier("userVORedisExpires")
    private RedisExpires redisExpires;

    public UserVO getUserVO(Long id) {
        String redisKey = getUserRedisKey(id);

        UserVO user = redisUtils.opsForValue().get(redisKey, UserVO.class);
        return CacheUtils.handleCache(
                user,
                id,
                userDao::get,
                Function.identity(),
                (i, u) -> redisUtils.opsForValue().set(redisKey, u)
        );
    }

    public List<UserVO> listUserVOs(List<Long> ids) {
        List<String> redisKeys =
                ids.stream().map(this::getUserRedisKey).collect(Collectors.toList());
        List<UserVO> caches = redisUtils.opsForValue().multiGet(redisKeys, UserVO.class);
        return CacheUtils.handleBatchCache(
                caches,
                ids,
                userDao::list,
                Function.identity(),
                (missCache, missData) -> redisUtils.opsForValue().multiSet(
                        missCache.stream().map(c->getUserRedisKey(c.getId())).collect(Collectors.toList()),
                        missCache,
                        redisExpires
                )
        );
    }

    private String getUserRedisKey(Long id) {
        return String.format("u:%d", id);
    }
}
