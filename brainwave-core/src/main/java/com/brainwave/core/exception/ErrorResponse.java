package com.brainwave.core.exception;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 錯誤回應 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private String errorCode;
    private String message;
    private Instant timestamp;
    private String path;

    public static ErrorResponse of(String errorCode, String message, String path) {
        return ErrorResponse.builder()
                .errorCode(errorCode)
                .message(message)
                .timestamp(Instant.now())
                .path(path)
                .build();
    }
}
