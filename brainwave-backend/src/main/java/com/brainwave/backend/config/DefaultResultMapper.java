package com.brainwave.backend.config;

import com.brainwave.core.common.Result;
import com.brainwave.core.common.ResultMapper;
import java.time.Instant;
import org.slf4j.MDC;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * 預設的 Result 組裝實作，行為與 {@link Result#success(Object)} / {@link Result#fail(String, String)} 一致。
 * 會從 MDC 帶入 requestId 作為 correlationId（與 CorrelationIdFilter 銜接）。
 */
@Primary
@Component
public class DefaultResultMapper implements ResultMapper {

    private static final String MDC_REQUEST_ID = "requestId";

    @Override
    public <T> Result<T> success(T data) {
        return Result.<T>builder()
                .success(true)
                .code("SUCCESS")
                .message("操作成功")
                .data(data)
                .timestamp(Instant.now())
                .correlationId(MDC.get(MDC_REQUEST_ID))
                .build();
    }

    @Override
    public <T> Result<T> success(String message, T data) {
        return Result.<T>builder()
                .success(true)
                .code("SUCCESS")
                .message(message)
                .data(data)
                .timestamp(Instant.now())
                .correlationId(MDC.get(MDC_REQUEST_ID))
                .build();
    }

    @Override
    public Result<Void> fail(String code, String message) {
        return Result.<Void>builder()
                .success(false)
                .code(code)
                .message(message)
                .timestamp(Instant.now())
                .correlationId(MDC.get(MDC_REQUEST_ID))
                .build();
    }
}
