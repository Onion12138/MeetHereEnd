package com.ecnu.meethere.news.comment.controller;

import com.ecnu.meethere.common.result.CommonResult;
import com.ecnu.meethere.common.result.Result;
import com.ecnu.meethere.news.comment.param.NewsCommentPostParam;
import com.ecnu.meethere.news.comment.param.NewsCommentUpdateParam;
import com.ecnu.meethere.news.comment.service.NewsCommentService;
import com.ecnu.meethere.paging.PageParam;
import com.ecnu.meethere.session.UserSessionInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/news/comment/")
public class NewsCommentController {
    @Autowired
    private NewsCommentService newsCommentService;

    @GetMapping("list")
    public Result<?> listComments(@RequestParam("newsId") Long newsId,
                                  @ModelAttribute @Valid PageParam pageParam) {
        return CommonResult.success().data(newsCommentService.listComments(newsId, pageParam));
    }

    @PostMapping("post")
    public Result<?> postComment(@SessionAttribute UserSessionInfo userSessionInfo,
                                 @RequestBody @Valid NewsCommentPostParam postParam
    ) {
        newsCommentService.postComment(userSessionInfo.getId(), postParam);
        return CommonResult.success();
    }

    @PostMapping("update")
    public Result<?> updateComment(@SessionAttribute UserSessionInfo userSessionInfo,
                                   @RequestBody @Valid NewsCommentUpdateParam updateParam
    ) {
        newsCommentService.updateComment(userSessionInfo.getId(),
                userSessionInfo.getIsAdministrator(), updateParam);
        return CommonResult.success();
    }

    @PostMapping("delete")
    public Result<?> deleteComment(@SessionAttribute UserSessionInfo userSessionInfo,
                                   @RequestBody Long commentId) {
        newsCommentService.deleteComment(userSessionInfo.getId(),
                userSessionInfo.getIsAdministrator(), commentId);
        return CommonResult.success();
    }
}
