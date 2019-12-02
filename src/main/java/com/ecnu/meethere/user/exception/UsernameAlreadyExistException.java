package com.ecnu.meethere.user.exception;

public class UsernameAlreadyExistException extends RuntimeException {
    public UsernameAlreadyExistException() {
        super(null, null, true, false);
    }
}
