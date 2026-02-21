package com.brainwave.core.exception;

/**
 * 資源不存在例外
 */
public class ResourceNotFoundException extends BaseException {

    private static final long serialVersionUID = 1L;

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
