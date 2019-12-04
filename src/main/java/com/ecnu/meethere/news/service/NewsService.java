package com.ecnu.meethere.news.service;

import com.ecnu.meethere.common.idgenerator.IdGenerator;
import com.ecnu.meethere.common.utils.CollectionUtils;
import com.ecnu.meethere.news.dao.NewsDao;
import com.ecnu.meethere.news.dto.NewsDTO;
import com.ecnu.meethere.news.dto.NewsDigestDTO;
import com.ecnu.meethere.news.entity.NewsDO;
import com.ecnu.meethere.news.manager.NewsManager;
import com.ecnu.meethere.news.param.NewsPublishParam;
import com.ecnu.meethere.news.param.NewsUpdateParam;
import com.ecnu.meethere.news.vo.NewsDigestVO;
import com.ecnu.meethere.news.vo.NewsVO;
import com.ecnu.meethere.paging.PageParam;
import com.ecnu.meethere.user.service.UserService;
import com.ecnu.meethere.user.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NewsService {
    @Autowired
    private NewsManager newsManager;

    @Autowired
    private UserService userService;

    @Autowired
    private NewsDao newsDao;

    @Autowired
    private IdGenerator idGenerator;

    public void publishNews(Long userId, NewsPublishParam newsPublishParam) {
        newsDao.insertNews(convertToNewsDO(userId, newsPublishParam));
    }

    public void deleteNews(Long id) {
        newsDao.deleteNews(id);
    }

    private NewsDO convertToNewsDO(Long userId, NewsPublishParam newsPublishParam) {
        NewsDO newsDO = new NewsDO();
        BeanUtils.copyProperties(newsPublishParam, newsDO);
        newsDO.setId(idGenerator.nextId());
        newsDO.setUserId(userId);
        return newsDO;
    }

    public NewsVO getNews(Long id) {
        return convertToNewsVO(newsManager.getNews(id));
    }

    private NewsVO convertToNewsVO(NewsDTO newsDTO) {
        NewsVO newsVO = new NewsVO();
        BeanUtils.copyProperties(newsDTO, newsVO);
        newsVO.setWriter(userService.getUserVO(newsDTO.getUserId()));
        return newsVO;
    }

    private NewsDigestVO convertToNewsDigestVO(NewsDigestDTO newsDigestDTO) {
        NewsDigestVO newsVO = new NewsDigestVO();
        BeanUtils.copyProperties(newsDigestDTO, newsVO);
        return newsVO;
    }

    public List<NewsDigestVO> listNewsDigests(PageParam pageParam) {
        List<NewsDigestDTO> digestDTOs = newsManager.listNewsDigests(pageParam);
        List<NewsDigestVO> digestVOs = digestDTOs.stream()
                .map(this::convertToNewsDigestVO).collect(Collectors.toList());
        List<UserVO> userVOs =
                userService.listUserVOs(digestDTOs.stream().map(NewsDigestDTO::getUserId).collect(Collectors.toList()));
        CollectionUtils.consume(digestVOs, userVOs, NewsDigestVO::setWriter);
        return digestVOs;
    }

    public void updateNews(NewsUpdateParam updateParam) {
        newsDao.updateNews(updateParam);
    }
}