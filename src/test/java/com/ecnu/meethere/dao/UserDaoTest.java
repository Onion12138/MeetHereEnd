package com.ecnu.meethere.dao;

import com.ecnu.meethere.user.dao.UserDao;
import com.ecnu.meethere.user.entity.UserDO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserDaoTest {
    @Autowired
    private UserDao userDao;

    @BeforeEach
    public void insertTestData() {
        userDao.insertUser(
                new UserDO()
                        .setId(-1L)
                        .setUsername("testusername")
                        .setPassword("testpassword")
        );
    }


    @Test
    void getUserByUsername() {
        assertNotNull(userDao.getUserByUsername("testusername"));

        assertNull(userDao.getUserByUsername("null"));
    }

    @Test
    void existById() {
        assertTrue(userDao.isUserExistById(-1L));

        assertFalse(userDao.isUserExistById(-2L));
    }
}