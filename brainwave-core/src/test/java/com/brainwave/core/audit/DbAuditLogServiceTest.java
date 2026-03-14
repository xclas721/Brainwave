package com.brainwave.core.audit;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DbAuditLogServiceTest {

    @Mock
    private AuditLogRepository repository;

    @Test
    void record_shouldSaveEntityWithMappedFields() {
        DbAuditLogService service = new DbAuditLogService(repository);
        AuditRecord record = new AuditRecord(
                "DELETE_USER", "USER", "DELETE", "/api/users/1",
                true, 20L, "req-xyz", "TokenPrincipal[subject=7]"
        );

        service.record(record);

        verify(repository).save(argThat(entity ->
                "DELETE_USER".equals(entity.getAction())
                        && "USER".equals(entity.getResource())
                        && "DELETE".equals(entity.getMethod())
                        && "/api/users/1".equals(entity.getPath())
                        && Boolean.TRUE.equals(entity.getSuccess())
                        && Long.valueOf(20L).equals(entity.getDurationMs())
                        && "req-xyz".equals(entity.getRequestId())
                        && "TokenPrincipal[subject=7]".equals(entity.getPrincipal())
                        && entity.getCreatedAt() != null
        ));
    }
}
