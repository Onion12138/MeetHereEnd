package com.ecnu.meethere.news.comment.service;

import com.ecnu.meethere.common.idgenerator.IdGenerator;
import com.ecnu.meethere.common.utils.CollectionUtils;
import com.ecnu.meethere.news.comment.dao.NewsCommentDao;
import com.ecnu.meethere.news.comment.dto.NewsCommentDTO;
import com.ecnu.meethere.news.comment.entity.NewsCommentDO;
import com.ecnu.meethere.news.comment.manager.NewsCommentManager;
import com.ecnu.meethere.news.comment.param.NewsCommentPostParam;
import com.ecnu.meethere.news.comment.param.NewsCommentUpdateParam;
import com.ecnu.meethere.news.comment.vo.NewsCommentVO;
import com.ecnu.meethere.paging.PageParam;
import com.ecnu.meethere.user.service.UserService;
import com.ecnu.meethere.user.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NewsCommentService {
    @Autowired
    private NewsCommentManager newsCommentManager;

    @Autowired
    private NewsCommentDao newsCommentDao;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private UserService userService;

    public void postComment(Long userId, NewsCommentPostParam postParam) {
        newsCommentDao.insertComment(convertToNewsCommentDO(userId, postParam));
    }

    private NewsCommentDO convertToNewsCommentDO(Long userId, NewsCommentPostParam postParam) {
        NewsCommentDO newsCommentDO = new NewsCommentDO();
        BeanUtils.copyProperties(postParam, newsCommentDO);
        newsCommentDO.setId(idGenerator.nextId());
        newsCommentDO.setUserId(userId);
        return newsCommentDO;
    }

    public List<NewsCommentVO> listComments(Long newsId, PageParam pageParam) {
        List<NewsCommentDTO> newsCommentDTOs = newsCommentManager.listComments(newsId, pageParam);
        List<NewsCommentVO> newsCommentVOs =
                newsCommentDTOs.stream().map(this::convertToNewsCommentVO).collect(Collectors.toList());
        List<UserVO> userVOs =
                userService.listUserVOs(newsCommentDTOs.stream().map(NewsCommentDTO::getUserId).collect(Collectors.toList()));
        CollectionUtils.consume(newsCommentVOs, userVOs, NewsCommentVO::setCommenter);

        return newsCommentVOs;
    }

    private NewsCommentVO convertToNewsCommentVO(NewsCommentDTO newsCommentDTO) {
        NewsCommentVO vo = new NewsCommentVO();
        BeanUtils.copyProperties(newsCommentDTO, vo);
        return vo;
    }

    public void updateComment(Long userId, boolean isAdministrator,
                              NewsCommentUpdateParam updateParam) {
        if (isAdministrator) {
            newsCommentDao.updateComment(updateParam);
            return;
        }

        NewsCommentDTO comment = newsCommentManager.getComment(updateParam.getId());

        if (!userId.equals(comment.getUserId())) {
            return;
        }

        newsCommentDao.updateComment(updateParam);
    }

    public void deleteComment(Long userId, boolean isAdministrator, Long id) {
        if (isAdministrator) {
            newsCommentDao.deleteComment(id);
            return;
        }

        NewsCommentDTO comment = newsCommentManager.getComment(id);

        if (!userId.equals(comment.getUserId())) {
            return;
        }

        newsCommentDao.deleteComment(id);

    }

}
