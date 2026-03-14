package com.brainwave.core.common;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 統一回應格式。
 * correlationId 為可選，由 ResultMapper 在有 CorrelationIdFilter 時從 MDC 帶入，供前端/客戶端串接追蹤。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

    private boolean success;
    private String code;
    private String message;
    private T data;
    private Instant timestamp;
    /** 可選；由 DefaultResultMapper 從 MDC requestId 帶入，與 X-Request-ID 一致 */
    private String correlationId;

    public static <T> Result<T> success(T data) {
        return Result.<T>builder()
                .success(true)
                .code("SUCCESS")
                .message("操作成功")
                .data(data)
                .timestamp(Instant.now())
                .build();
    }

    public static <T> Result<T> success(String message, T data) {
        return Result.<T>builder()
                .success(true)
                .code("SUCCESS")
                .message(message)
                .data(data)
                .timestamp(Instant.now())
                .build();
    }

    public static <T> Result<T> fail(String code, String message) {
        return Result.<T>builder()
                .success(false)
                .code(code)
                .message(message)
                .timestamp(Instant.now())
                .build();
    }

    public static <T> Result<T> fail(String message) {
        return fail("FAIL", message);
    }
}
