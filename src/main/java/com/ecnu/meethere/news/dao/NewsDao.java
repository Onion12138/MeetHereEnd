package com.ecnu.meethere.news.dao;

import com.ecnu.meethere.news.dto.NewsDTO;
import com.ecnu.meethere.news.dto.NewsDigestDTO;
import com.ecnu.meethere.news.entity.NewsDO;
import com.ecnu.meethere.news.param.NewsUpdateParam;
import com.ecnu.meethere.paging.PageParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface NewsDao {
    int insert(NewsDO newsDO);

    int delete(Long id);

    List<Long> listIds(PageParam pageParam);

    List<NewsDTO> list(List<Long> newsIds);

    NewsDTO get(Long newsId);

    int update(NewsUpdateParam updateParam);
}