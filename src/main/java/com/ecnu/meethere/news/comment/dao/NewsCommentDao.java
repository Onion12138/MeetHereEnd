package com.ecnu.meethere.news.comment.dao;

import com.ecnu.meethere.news.comment.dto.NewsCommentDTO;
import com.ecnu.meethere.news.comment.entity.NewsCommentDO;
import com.ecnu.meethere.news.comment.param.NewsCommentUpdateParam;
import com.ecnu.meethere.paging.PageParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface NewsCommentDao {
    int insert(NewsCommentDO newsCommentDO);

    List<Long> listIds(@Param("newsId") Long newsId,
                       @Param("pageParam") PageParam pageParam);

    List<NewsCommentDTO> list(List<Long> commentIds);

    int delete(Long id);

    int update(NewsCommentUpdateParam updateParam);

    NewsCommentDTO get(Long id);
}