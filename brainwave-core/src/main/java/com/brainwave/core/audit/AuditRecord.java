package com.brainwave.core.audit;

/**
 * 單筆稽核紀錄的承載，供 AuditLogService 寫入 console 或 DB。
 */
public record AuditRecord(
        String action,
        String resource,
        String method,
        String path,
        boolean success,
        long durationMs,
        String requestId,
        String principal
) {}
