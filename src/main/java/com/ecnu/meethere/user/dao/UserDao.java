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
    UserDTO getUserByUsername(String username);

    UserVO getUserVO(Long id);

    List<UserVO> listUserVOs(List<Long> id);

    int insertUser(UserDO userDO);

    boolean isUserExistById(Long id);
}
