package com.ecnu.meethere.site.dao;

import com.ecnu.meethere.paging.PageParam;
import com.ecnu.meethere.site.dto.SiteDTO;
import com.ecnu.meethere.site.entity.SiteDO;
import com.ecnu.meethere.site.param.SiteUpdateParam;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SiteDao {
    int insertSite(SiteDO siteDO);

    int updateSite(SiteUpdateParam updateParam);

    SiteDTO getSite(Long id);

    List<Long> listSiteIds(PageParam pageParam);

    List<SiteDTO> listSites(List<Long> ids);

    int deleteSite(Long id);
}
