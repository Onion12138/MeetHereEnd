package com.ecnu.meethere.user.dao;

import com.ecnu.meethere.user.dto.UserDTO;
import com.ecnu.meethere.user.entity.UserDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao {
    UserDTO getUserByUsername(String username);

    int insertUser(UserDO userDO);

    boolean isUserExistById(Long id);
}
