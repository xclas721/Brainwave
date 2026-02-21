package com.brainwave.core;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Brainwave Core Configuration
 * 核心模組配置，供外部模組透過 @Import 方式引入使用
 * 註：ComponentScan 由主程式 scanBasePackages 統一處理；此處保留 EntityScan 與 JPA Repositories
 */
@Configuration
@EntityScan(basePackages = {"com.brainwave.service", "com.brainwave.core"})
@EnableJpaRepositories(basePackages = {"com.brainwave.service", "com.brainwave.core"})
public class BrainwaveCoreConfiguration {

}
