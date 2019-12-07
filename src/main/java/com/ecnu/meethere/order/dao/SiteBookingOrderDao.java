package com.ecnu.meethere.order.dao;

import com.ecnu.meethere.order.dto.SiteBookingOrderDTO;
import com.ecnu.meethere.order.entity.SiteBookingOrderDO;
import com.ecnu.meethere.paging.PageParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SiteBookingOrderDao {
    int create(SiteBookingOrderDO siteBookingOrderDO);

    List<Long> listIdsBySite(@Param("siteId") Long siteId, @Param("status") Integer status,
                             @Param("pageParam") PageParam pageParam);

    List<Long> listIdsByUser(@Param("userId") Long userId,
                             @Param("status") Integer status,
                             @Param("pageParam") PageParam pageParam);

    List<SiteBookingOrderDTO> list(List<Long> orderIds);

    SiteBookingOrderDTO get(Long orderId);

    int updateStatus(@Param("id")Long id, @Param("oldStatus")int oldStatus , @Param(
            "newStatus") int newStatus);
}
