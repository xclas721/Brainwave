package com.brainwave.backend.auth.dto;

/**
 * 驗證後的最小 Token 主體資訊。
 */
public record TokenPrincipal(String scope, String subject) {
}
