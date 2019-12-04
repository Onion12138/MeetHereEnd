package com.ecnu.meethere.user.service;

import com.ecnu.meethere.common.idgenerator.IdGenerator;
import com.ecnu.meethere.session.UserSessionInfo;
import com.ecnu.meethere.user.dao.UserDao;
import com.ecnu.meethere.user.dto.UserDTO;
import com.ecnu.meethere.user.entity.UserDO;
import com.ecnu.meethere.user.exception.IncorrectUsernameOrPasswordException;
import com.ecnu.meethere.user.exception.UsernameAlreadyExistException;
import com.ecnu.meethere.user.exception.UsernameNotExistsException;
import com.ecnu.meethere.user.manager.UserManager;
import com.ecnu.meethere.user.param.LoginParam;
import com.ecnu.meethere.user.param.RegisterParam;
import com.ecnu.meethere.user.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private UserManager userManager;

    @Autowired
    private IdGenerator idGenerator;

    public UserSessionInfo login(LoginParam loginParam) {
        String username = loginParam.getUsername();
        String password = loginParam.getPassword();

        UserDTO userDTO = userDao.getUserByUsername(username);
        if (userDTO == null)
            throw new UsernameNotExistsException();

        if (!password.equals(userDTO.getPassword()))
            throw new IncorrectUsernameOrPasswordException();

        return convertToUserSessionInfo(userDTO);
    }

    private UserSessionInfo convertToUserSessionInfo(UserDTO userDTO) {
        UserSessionInfo userSessionInfo = new UserSessionInfo();
        BeanUtils.copyProperties(userDTO, userSessionInfo);
        return userSessionInfo;
    }

    public void register(RegisterParam registerParam) {
        String username = registerParam.getUsername();
        String password = registerParam.getPassword();

        if (userDao.getUserByUsername(username) != null)
            throw new UsernameAlreadyExistException();

        UserDO userDO = new UserDO()
                .setId(idGenerator.nextId())
                .setUsername(username)
                .setPassword(password);

        userDao.insertUser(userDO);
    }

    public UserVO getUserVO(Long userId) {
        return userManager.getUserVO(userId);
    }

    public List<UserVO> listUserVOs(List<Long>ids) {
        return userManager.listUserVOs(ids);
    }
}
