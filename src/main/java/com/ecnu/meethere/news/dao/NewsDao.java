package com.ecnu.meethere.news.dao;

import com.ecnu.meethere.news.dto.NewsDTO;
import com.ecnu.meethere.news.dto.NewsDigestDTO;
import com.ecnu.meethere.news.entity.NewsDO;
import com.ecnu.meethere.paging.PageParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NewsDao {
    int insertNews(NewsDO newsDO);

    List<Long> listNewsDigestsIds(PageParam pageParam);

    List<NewsDTO> listNews(List<Long> newsIds);

    NewsDTO getNews(Long newsId);
}