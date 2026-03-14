package com.brainwave.core.audit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * 稽核僅輸出至 log；app.audit.sink=console 或未設定時使用。
 */
@Service
@Slf4j
@ConditionalOnProperty(name = "app.audit.sink", havingValue = "console", matchIfMissing = true)
public class ConsoleAuditLogService implements AuditLogService {

    @Override
    public void record(AuditRecord record) {
        log.info(
                "AUDIT action={} resource={} method={} path={} success={} durationMs={} requestId={} principal={}",
                record.action(),
                record.resource(),
                record.method(),
                record.path(),
                record.success(),
                record.durationMs(),
                record.requestId() != null ? record.requestId() : "",
                record.principal() != null ? record.principal() : ""
        );
    }
}
