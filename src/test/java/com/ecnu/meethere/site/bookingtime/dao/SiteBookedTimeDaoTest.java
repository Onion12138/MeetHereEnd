package com.ecnu.meethere.site.bookingtime.dao;

import com.ecnu.meethere.site.bookingtime.entity.SiteBookedTimeDO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SiteBookedTimeDaoTest {
    @Autowired
    private SiteBookedTimeDao siteBookedTimeDao;

    @BeforeEach
    void insertTestData() {
        LocalDateTime dateTime = LocalDateTime.of(2010, 1, 1, 10, 10);
        siteBookedTimeDao.insert(new SiteBookedTimeDO(-1L, 1L, dateTime, dateTime.plusDays(1),
                dateTime, dateTime));
        siteBookedTimeDao.insert(new SiteBookedTimeDO(-2L, 1L, dateTime.plusDays(2),
                dateTime.plusDays(3), dateTime, dateTime));
    }

    @ParameterizedTest
    @MethodSource("hasConflictGen")
    void hasConflict(LocalDateTime start, LocalDateTime end, boolean wHasConflict) {
        assertEquals(wHasConflict, siteBookedTimeDao.hasConflict(1L, start, end).orElse(false));
    }

    static Stream<Arguments> hasConflictGen() {
        LocalDateTime dateTime = LocalDateTime.of(2010, 1, 1, 10, 10);
        return Stream.of(
                Arguments.of(dateTime.minusMinutes(30), dateTime, false),
                Arguments.of(dateTime.minusMinutes(30), dateTime.plusSeconds(30), true),
                Arguments.of(dateTime.plusDays(1), dateTime.plusDays(1).plusMinutes(30), false),
                Arguments.of(dateTime.plusDays(1).minusMinutes(30),
                        dateTime.plusDays(1).plusMinutes(30), true),
                Arguments.of(dateTime.plusMinutes(30), dateTime.plusMinutes(60), false),
                Arguments.of(dateTime.minusMinutes(60), dateTime.minusMinutes(30), false),
                Arguments.of(dateTime.plusDays(1).plusMinutes(30),
                        dateTime.plusDays(1).plusMinutes(60), false)
        );
    }

    @ParameterizedTest
    @MethodSource("listByStartTimeGen")
    void listByStartTime(LocalDateTime start, LocalDateTime end, int wSize) {
        assertEquals(wSize, siteBookedTimeDao.listByStartTime(1L, start, end).size());
    }

    static Stream<Arguments> listByStartTimeGen() {
        LocalDateTime dateTime = LocalDateTime.of(2010, 1, 1, 10, 10);
        return Stream.of(
                Arguments.of(dateTime.minusDays(1), dateTime, 1),
                Arguments.of(dateTime, dateTime, 1),
                Arguments.of(dateTime, dateTime.plusDays(1), 1),
                Arguments.of(dateTime, dateTime.plusDays(2), 2)
        );
    }

    @Test
    void deleteByStartTime() {
        LocalDateTime dateTime = LocalDateTime.of(2010, 1, 1, 10, 10);
        assertEquals(1, siteBookedTimeDao.deleteByStartTime(1L, dateTime));
    }
}