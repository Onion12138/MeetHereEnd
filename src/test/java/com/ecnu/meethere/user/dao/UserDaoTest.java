package com.ecnu.meethere.user.dao;

import com.ecnu.meethere.user.entity.UserDO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserDaoTest {
    @Autowired
    private UserDao userDao;

    @BeforeEach
    public void insertTestData() {
        userDao.insert(
                new UserDO()
                        .setId(-1L)
                        .setUsername("testusername")
                        .setPassword("testpassword")
        );
    }


    @Test
    void getUserByUsername() {
        assertNotNull(userDao.getByUsername("testusername"));

        assertNull(userDao.getByUsername("null"));
    }

    @Test
    void existById() {
        assertTrue(userDao.exist(-1L));

        assertFalse(userDao.exist(-2L));
    }
}