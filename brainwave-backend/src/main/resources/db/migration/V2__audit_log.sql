CREATE TABLE IF NOT EXISTS audit_log (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    action VARCHAR(100) NOT NULL,
    resource VARCHAR(100) NOT NULL,
    method VARCHAR(10) NOT NULL,
    path VARCHAR(500) NOT NULL,
    success BOOLEAN NOT NULL,
    duration_ms BIGINT NOT NULL,
    request_id VARCHAR(100) NULL,
    principal VARCHAR(500) NULL,
    created_at DATETIME(6) NOT NULL
);

CREATE INDEX idx_audit_log_created_at ON audit_log (created_at);
CREATE INDEX idx_audit_log_request_id ON audit_log (request_id);
