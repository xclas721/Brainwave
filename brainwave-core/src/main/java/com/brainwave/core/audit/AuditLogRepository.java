package com.brainwave.core.audit;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 稽核紀錄儲存。
 */
public interface AuditLogRepository extends JpaRepository<AuditLogEntity, Long> {
}
