package com.brainwave.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JPA Auditing 設定，啟用 @CreatedDate 和 @LastModifiedDate
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
}
