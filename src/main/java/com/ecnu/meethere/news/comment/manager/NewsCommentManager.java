package com.ecnu.meethere.news.comment.manager;

import com.ecnu.meethere.common.annotation.Manager;
import com.ecnu.meethere.common.utils.CacheUtils;
import com.ecnu.meethere.common.utils.Tuple;
import com.ecnu.meethere.news.comment.dao.NewsCommentDao;
import com.ecnu.meethere.news.comment.dto.NewsCommentDTO;
import com.ecnu.meethere.paging.PageParam;
import com.ecnu.meethere.redis.codec.protobuf.LongListWrapper;
import com.ecnu.meethere.redis.core.RedisExpires;
import com.ecnu.meethere.redis.core.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Manager
public class NewsCommentManager {
    @Autowired
    private NewsCommentDao newsCommentDao;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    @Qualifier("newsCommentPageRedisExpires")
    private RedisExpires newsCommentPageRedisExpires;

    @Autowired
    @Qualifier("newsCommentRedisExpires")
    private RedisExpires newsCommentRedisExpires;

    public List<NewsCommentDTO> listComments(Long newsId, PageParam pageParam) {
        String redisKey = getNewsCommentPageRedisKey(newsId, pageParam);

        List<Long> cache = Optional.ofNullable(redisUtils.opsForValue().get(redisKey,
                LongListWrapper.class)).orElse(new LongListWrapper()).getList();
        return listCommentsByIds(CacheUtils.handleCache(
                cache,
                new Tuple<>(newsId, pageParam),
                tuple -> newsCommentDao.listIds(tuple.getFirst(), tuple.getSecond()),
                Function.identity(),
                (tuple, c) -> redisUtils.opsForValue().set(redisKey, new LongListWrapper(c),
                        newsCommentPageRedisExpires)
        ));
    }

    public NewsCommentDTO getComment(Long id) {
        String redisKey = getCommentRedisKey(id);
        NewsCommentDTO cache = redisUtils.opsForValue().get(redisKey,
                NewsCommentDTO.class);
        return CacheUtils.handleCache(
                cache,
                id,
                newsCommentDao::get,
                Function.identity(),
                (i, n) -> redisUtils.opsForValue().set(redisKey, n)
        );
    }

    List<NewsCommentDTO> listCommentsByIds(List<Long> commentIds) {
        List<String> redisKeys =
                commentIds.stream().map(this::getCommentRedisKey).collect(Collectors.toList());
        List<NewsCommentDTO> cache = redisUtils.opsForValue().multiGet(redisKeys,
                NewsCommentDTO.class);
        return CacheUtils.handleBatchCache(
                cache,
                commentIds,
                newsCommentDao::list,
                Function.identity(),
                (c, d) -> {
                    List<String> missCacheRedisKeys =
                            c.stream().map(n -> getCommentRedisKey(n.getId())).collect(Collectors.toList());
                    redisUtils.opsForValue().multiSet(missCacheRedisKeys, c, newsCommentRedisExpires);
                }
        );
    }

    private String getCommentRedisKey(Long commentId) {
        return String.format("n:c:%d", commentId);
    }

    private String getNewsCommentPageRedisKey(Long newsId, PageParam pageParam) {
        return String.format("n:%d:c:p:%d", newsId, pageParam.getPageNum());
    }
}
