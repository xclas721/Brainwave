package com.brainwave.core.audit;

import java.time.Instant;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * 稽核寫入 DB（audit_log 表）；app.audit.sink=db 時使用。
 */
@Service
@ConditionalOnProperty(name = "app.audit.sink", havingValue = "db")
public class DbAuditLogService implements AuditLogService {

    private final AuditLogRepository repository;

    public DbAuditLogService(AuditLogRepository repository) {
        this.repository = repository;
    }

    @Override
    public void record(AuditRecord record) {
        AuditLogEntity entity = new AuditLogEntity();
        entity.setAction(record.action());
        entity.setResource(record.resource());
        entity.setMethod(record.method());
        entity.setPath(record.path());
        entity.setSuccess(record.success());
        entity.setDurationMs(record.durationMs());
        entity.setRequestId(record.requestId());
        entity.setPrincipal(record.principal());
        entity.setCreatedAt(Instant.now());
        repository.save(entity);
    }
}
