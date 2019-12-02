package com.ecnu.meethere.user.exception;

public class IncorrectUsernameOrPasswordException extends RuntimeException {
    public IncorrectUsernameOrPasswordException() {
        super(null, null, true, false);
    }
}
