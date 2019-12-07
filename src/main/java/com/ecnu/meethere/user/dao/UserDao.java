package com.ecnu.meethere.user.dao;

import com.ecnu.meethere.user.dto.UserDTO;
import com.ecnu.meethere.user.entity.UserDO;
import com.ecnu.meethere.user.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserDao {
    UserDTO getByUsername(String username);

    UserVO get(Long id);

    List<UserVO> list(List<Long> id);

    int insert(UserDO userDO);

    boolean exist(Long id);
}
