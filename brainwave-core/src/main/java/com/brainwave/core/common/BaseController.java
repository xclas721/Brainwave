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

    /**
     * 分頁查詢成功回應（返回 Spring Data Page）
     *
     * @param page Spring Data Page 物件
     * @param <T> 資料類型
     * @return ResponseEntity
     */
    protected <T> ResponseEntity<Result<Page<T>>> success(Page<T> page) {
        return ResponseEntity.ok(Result.success(page));
    }

    /**
     * 分頁查詢成功回應（返回標準化 PageResponse）
     *
     * @param page Spring Data Page 物件
     * @param <T> 資料類型
     * @return ResponseEntity
     */
    protected <T> ResponseEntity<Result<PageResponse<T>>> successPage(Page<T> page) {
        return ResponseEntity.ok(Result.success(PageResponse.from(page)));
    }

    protected <T> ResponseEntity<Result<T>> fail(String code, String message) {
        return ResponseEntity.badRequest().body(Result.fail(code, message));
    }

    protected <T> ResponseEntity<Result<T>> fail(String message) {
        return ResponseEntity.badRequest().body(Result.fail(message));
    }
}
