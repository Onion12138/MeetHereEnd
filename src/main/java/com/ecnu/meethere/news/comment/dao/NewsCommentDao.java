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
    int insertComment(NewsCommentDO newsCommentDO);

    List<Long> listCommentIdsByPage(@Param("newsId") Long newsId,
                                    @Param("pageParam") PageParam pageParam);

    List<NewsCommentDTO> listCommentsByIds(List<Long> commentIds);

    int deleteComment(Long id);

    int updateComment(NewsCommentUpdateParam updateParam);

    NewsCommentDTO getComment(Long id);
}