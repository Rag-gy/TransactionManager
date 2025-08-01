package com.learning.tracker.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class BusinessException extends RuntimeException{
    private final String errorCode;

    public BusinessException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public BusinessException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
}
