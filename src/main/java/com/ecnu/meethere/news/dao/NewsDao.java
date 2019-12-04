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
    int insertNews(NewsDO newsDO);

    int deleteNews(Long id);

    List<Long> listNewsDigestsIds(PageParam pageParam);

    List<NewsDTO> listNews(List<Long> newsIds);

    NewsDTO getNews(Long newsId);

    int updateNews(NewsUpdateParam updateParam);
}