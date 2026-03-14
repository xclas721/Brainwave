-- 系統設定 Key-Value 表（見 SYSTEMCONFIG-GUIDE）
CREATE TABLE IF NOT EXISTS system_config (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    config_key VARCHAR(100) NOT NULL,
    config_value VARCHAR(1000) NOT NULL,
    config_type VARCHAR(50) NOT NULL,
    description VARCHAR(255) NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NULL,
    CONSTRAINT uk_system_config_key UNIQUE (config_key)
);
