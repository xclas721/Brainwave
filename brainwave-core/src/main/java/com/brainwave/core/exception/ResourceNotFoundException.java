package com.brainwave.core.exception;

/**
 * 資源不存在例外
 */
public class ResourceNotFoundException extends BaseException {

    public ResourceNotFoundException(String errorCode) {
        super(errorCode);
    }

    public ResourceNotFoundException(String errorCode, String message) {
        super(errorCode, message);
    }

    public ResourceNotFoundException(String errorCode, String message, Object... args) {
        super(errorCode, message, args);
    }
}
