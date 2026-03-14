package com.brainwave.core.audit;

import org.junit.jupiter.api.Test;

class ConsoleAuditLogServiceTest {

    @Test
    void record_shouldNotThrow() {
        ConsoleAuditLogService service = new ConsoleAuditLogService();
        AuditRecord record = new AuditRecord(
                "CREATE_USER", "USER", "POST", "/api/users",
                true, 10L, "req-1", "principal"
        );
        service.record(record);
    }

    @Test
    void record_withNullRequestIdAndPrincipal_shouldNotThrow() {
        ConsoleAuditLogService service = new ConsoleAuditLogService();
        AuditRecord record = new AuditRecord(
                "UPDATE_CONFIG", "CONFIG", "PUT", "/api/config",
                false, 5L, null, null
        );
        service.record(record);
    }
}
