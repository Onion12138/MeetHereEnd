package com.ecnu.meethere.news.controller;

import com.ecnu.meethere.common.result.CommonResult;
import com.ecnu.meethere.common.result.Result;
import com.ecnu.meethere.news.param.NewsPublishParam;
import com.ecnu.meethere.news.param.NewsUpdateParam;
import com.ecnu.meethere.news.service.NewsService;
import com.ecnu.meethere.paging.PageParam;
import com.ecnu.meethere.session.UserSessionInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/news/")
public class NewsController {
    @Autowired
    private NewsService newsService;


    @GetMapping(value = "get")
    public Result<?> getNews(@RequestParam Long newsId) {
        return CommonResult.success().data(newsService.getNews(newsId));
    }

    @GetMapping(value = "digest/list")
    public Result<?> listNewsDigests(@ModelAttribute @Valid PageParam pageParam) {
        return CommonResult.success().data(newsService.listNewsDigests(pageParam));
    }

    @PostMapping(value = "publish")
    public Result<?> publishNews(
            @SessionAttribute UserSessionInfo userSessionInfo,
            @RequestBody @Valid NewsPublishParam publishParam) {
        if(!userSessionInfo.getIsAdministrator())
            return CommonResult.accessDenied();

        newsService.publishNews(userSessionInfo.getId(), publishParam);
        return CommonResult.success();
    }

    @PostMapping(value = "delete")
    public Result<?> deleteNews(
            @SessionAttribute UserSessionInfo userSessionInfo,
            @RequestBody Long newsId) {
        if(!userSessionInfo.getIsAdministrator())
            return CommonResult.accessDenied();

        newsService.deleteNews(newsId);
        return CommonResult.success();
    }

    @PostMapping(value = "update")
    public Result<?> updateNews(
            @SessionAttribute UserSessionInfo userSessionInfo,
            @RequestBody @Valid NewsUpdateParam updateParam) {
        if(!userSessionInfo.getIsAdministrator())
            return CommonResult.accessDenied();

        newsService.updateNews(updateParam);
        return CommonResult.success();
    }
}
