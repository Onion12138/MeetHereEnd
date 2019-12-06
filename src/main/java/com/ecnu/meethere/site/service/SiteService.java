package com.ecnu.meethere.site.service;

import com.ecnu.meethere.common.idgenerator.IdGenerator;
import com.ecnu.meethere.paging.PageParam;
import com.ecnu.meethere.site.dao.SiteDao;
import com.ecnu.meethere.site.dto.SiteDTO;
import com.ecnu.meethere.site.entity.SiteDO;
import com.ecnu.meethere.site.manager.SiteManager;
import com.ecnu.meethere.site.param.SiteCreateParam;
import com.ecnu.meethere.site.param.SiteUpdateParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SiteService {
    @Autowired
    private SiteManager siteManager;

    @Autowired
    private SiteDao siteDao;

    @Autowired
    private IdGenerator idGenerator;

    public List<SiteDTO> listSites(PageParam pageParam) {
        return siteManager.listSites(pageParam);
    }

    public SiteDTO getSite(Long id) {
        return siteManager.getSite(id);
    }

    public void createSite(SiteCreateParam createParam) {
        siteDao.insertSite(convertToSiteDO(createParam));
    }

    private SiteDO convertToSiteDO(SiteCreateParam createParam) {
        SiteDO siteDO = new SiteDO();
        BeanUtils.copyProperties(createParam, siteDO);
        siteDO.setId(idGenerator.nextId());
        return siteDO;
    }

    public void updateSite(SiteUpdateParam updateParam) {
        siteDao.updateSite(updateParam);
    }

    public void deleteSite(Long id) {
        siteDao.deleteSite(id);
    }
}