package com.ecnu.meethere.user.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.*;


class UsernamePasswordValidUtilsTest {

    @ParameterizedTest
    @MethodSource("isValidUsernameGen")
    void isValidUsername(String username, boolean valid) {
        assertEquals(valid, UsernamePasswordValidUtils.isValidUsername(username));
    }

    static Stream<Arguments> isValidUsernameGen() {
        return Stream.of(
                of("1234567890123456", true),
                of("12345678901234567", false),
                of("一二三四五六七八", true),
                of("一二三四五六七八九", false),
                of("", false),
                of("一二三四五六1234", true),
                of("一二三四五六12345", false),
                of("🍂", false),
                of(null, false)
        );
    }

    @ParameterizedTest
    @MethodSource("isValidPasswordGen")
    void isValidPassword(String username, boolean valid){
        assertEquals(valid, UsernamePasswordValidUtils.isValidPassword(username));
    }

    static Stream<Arguments> isValidPasswordGen(){
        return Stream.of(
                of("1234567890123456", true),
                of("12345678901234567", false),
                of("1234567", false),
                of("1234567 8", false),
                of("一二三四五六七八九", false),
                of(null, false),
                of("🍂", false)
        );
    }
}