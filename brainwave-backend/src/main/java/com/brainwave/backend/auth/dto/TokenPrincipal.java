package com.brainwave.backend.auth.dto;

/**
 * 驗證後的最小 Token 主體資訊。
 */
public record TokenPrincipal(String scope, String subject, String role) {

    public TokenPrincipal(String scope, String subject) {
        this(scope, subject, defaultRoleFromScope(scope));
    }

    private static String defaultRoleFromScope(String scope) {
        if (scope == null) return null;
        if (scope.startsWith("ADMIN_")) return "ADMIN";
        if (scope.startsWith("FRONT_")) return "MEMBER";
        return null;
    }
}
