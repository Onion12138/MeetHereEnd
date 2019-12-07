package com.ecnu.meethere.common.exception;

public class ShouldNeverHappenException extends RuntimeException {
    public ShouldNeverHappenException() {
        super("Should never happen!", null, true, false);
    }
}
