package com.brainwave.core.common;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

/**
 * 基礎 Controller，提供通用回應方法
 */
public abstract class BaseController {

    protected <T> ResponseEntity<Result<T>> success(T data) {
        return ResponseEntity.ok(Result.success(data));
    }

    protected <T> ResponseEntity<Result<T>> success(String message, T data) {
        return ResponseEntity.ok(Result.success(message, data));
    }

    protected <T> ResponseEntity<Result<Page<T>>> success(Page<T> page) {
        return ResponseEntity.ok(Result.success(page));
    }

    protected <T> ResponseEntity<Result<T>> fail(String code, String message) {
        return ResponseEntity.badRequest().body(Result.fail(code, message));
    }

    protected <T> ResponseEntity<Result<T>> fail(String message) {
        return ResponseEntity.badRequest().body(Result.fail(message));
    }
}
