package com.brainwave.backend.config;

import com.brainwave.core.common.Result;
import com.brainwave.core.common.ResultMapper;
import java.time.Instant;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * 預設的 Result 組裝實作，行為與 {@link Result#success(Object)} / {@link Result#fail(String, String)} 一致。
 * 可替換為自訂 Bean 以產出企業規格（例如加上 correlationId）。
 */
@Primary
@Component
public class DefaultResultMapper implements ResultMapper {

    @Override
    public <T> Result<T> success(T data) {
        return Result.<T>builder()
                .success(true)
                .code("SUCCESS")
                .message("操作成功")
                .data(data)
                .timestamp(Instant.now())
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
                .build();
    }

    @Override
    public Result<Void> fail(String code, String message) {
        return Result.<Void>builder()
                .success(false)
                .code(code)
                .message(message)
                .timestamp(Instant.now())
                .build();
    }
}
