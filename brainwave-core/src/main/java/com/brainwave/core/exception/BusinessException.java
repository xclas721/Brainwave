package com.brainwave.core.exception;

/**
 * 業務邏輯例外
 */
public class BusinessException extends BaseException {

    public BusinessException(String errorCode) {
        super(errorCode);
    }

    public BusinessException(String errorCode, String message) {
        super(errorCode, message);
    }

    public BusinessException(String errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    public BusinessException(String errorCode, String message, Object... args) {
        super(errorCode, message, args);
    }
}
