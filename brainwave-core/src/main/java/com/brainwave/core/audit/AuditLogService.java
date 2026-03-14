package com.brainwave.core.audit;

/**
 * 稽核紀錄輸出介面，可實作為 console log 或 DB 寫入。
 * 依 app.audit.sink 切換實作（console | db）。
 */
public interface AuditLogService {

    void record(AuditRecord record);
}
