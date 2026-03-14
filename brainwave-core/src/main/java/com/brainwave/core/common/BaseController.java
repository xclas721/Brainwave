package com.brainwave.core.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

/**
 * 基礎 Controller，提供通用回應方法。
 * 若有注入 {@link ResultMapper} 則使用其組裝回應，否則使用 {@link Result} 靜態方法（預設行為）。
 */
public abstract class BaseController {

    @Autowired(required = false)
    protected ResultMapper resultMapper;

    protected <T> ResponseEntity<Result<T>> success(T data) {
        if (resultMapper != null) {
            return ResponseEntity.ok(resultMapper.success(data));
        }
        return ResponseEntity.ok(Result.success(data));
    }

    protected <T> ResponseEntity<Result<T>> success(String message, T data) {
        if (resultMapper != null) {
            return ResponseEntity.ok(resultMapper.success(message, data));
        }
        return ResponseEntity.ok(Result.success(message, data));
    }

    /**
     * 分頁查詢成功回應（返回 Spring Data Page）
     */
    protected <T> ResponseEntity<Result<Page<T>>> success(Page<T> page) {
        if (resultMapper != null) {
            return ResponseEntity.ok(resultMapper.success(page));
        }
        return ResponseEntity.ok(Result.success(page));
    }

    /**
     * 分頁查詢成功回應（返回標準化 PageResponse）
     */
    protected <T> ResponseEntity<Result<PageResponse<T>>> successPage(Page<T> page) {
        Result<PageResponse<T>> body = resultMapper != null
                ? resultMapper.success(PageResponse.from(page))
                : Result.success(PageResponse.from(page));
        return ResponseEntity.ok(body);
    }

    protected <T> ResponseEntity<Result<T>> fail(String code, String message) {
        @SuppressWarnings("unchecked")
        Result<T> body = resultMapper != null
                ? (Result<T>) resultMapper.fail(code, message)
                : Result.fail(code, message);
        return ResponseEntity.badRequest().body(body);
    }

    protected <T> ResponseEntity<Result<T>> fail(String message) {
        return fail("FAIL", message);
    }
}
