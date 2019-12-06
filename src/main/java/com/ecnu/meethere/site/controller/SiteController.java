package com.ecnu.meethere.site.controller;

import com.ecnu.meethere.common.result.CommonResult;
import com.ecnu.meethere.common.result.Result;
import com.ecnu.meethere.news.param.NewsUpdateParam;
import com.ecnu.meethere.paging.PageParam;
import com.ecnu.meethere.session.UserSessionInfo;
import com.ecnu.meethere.site.param.SiteCreateParam;
import com.ecnu.meethere.site.param.SiteUpdateParam;
import com.ecnu.meethere.site.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/site/")
public class SiteController {
    @Autowired
    private SiteService siteService;

    @GetMapping(value = "get")
    public Result<?> getSite(@RequestParam Long siteId) {
        return CommonResult.success().data(siteService.getSite(siteId));
    }

    @GetMapping(value = "list")
    public Result<?> listSites(@ModelAttribute @Valid PageParam pageParam) {
        return CommonResult.success().data(siteService.listSites(pageParam));
    }

    @PostMapping("create")
    public Result<?> createSite(
            @SessionAttribute UserSessionInfo userSessionInfo,
            @RequestBody @Valid SiteCreateParam createParam) {
        if(!userSessionInfo.getIsAdministrator())
            return CommonResult.accessDenied();
        siteService.createSite(createParam);
        return CommonResult.success();
    }

        @PostMapping(value = "delete")
    public Result<?> deleteNews(
            @SessionAttribute UserSessionInfo userSessionInfo,
            @RequestBody Long siteId) {
        if(!userSessionInfo.getIsAdministrator())
            return CommonResult.accessDenied();

        siteService.deleteSite(siteId);

        return CommonResult.success();
    }

    @PostMapping(value = "update")
    public Result<?> updateNews(
            @SessionAttribute UserSessionInfo userSessionInfo,
            @RequestBody @Valid SiteUpdateParam updateParam) {
        if(!userSessionInfo.getIsAdministrator())
            return CommonResult.accessDenied();

        siteService.updateSite(updateParam);

        return CommonResult.success();
    }
}
