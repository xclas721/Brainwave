package com.brainwave.core.exception;

import lombok.Getter;

/**
 * 基礎例外類別，所有自訂例外應繼承此類
 */
@Getter
public class BaseException extends RuntimeException {

    private final String errorCode;
    private final Object[] args;

    public BaseException(String errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
        this.args = null;
    }

    public BaseException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.args = null;
    }

    public BaseException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.args = null;
    }

    public BaseException(String errorCode, String message, Object... args) {
        super(message);
        this.errorCode = errorCode;
        this.args = args;
    }

    public BaseException(String errorCode, String message, Throwable cause, Object... args) {
        super(message, cause);
        this.errorCode = errorCode;
        this.args = args;
    }
}
