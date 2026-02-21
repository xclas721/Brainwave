package com.brainwave.backend;

import com.brainwave.core.BrainwaveCoreConfiguration;
import com.brainwave.service.BrainwaveServiceConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * Brainwave Backend Application
 * 前端 API 服務，提供 RESTful API 給前端使用
 */
@Import({BrainwaveCoreConfiguration.class, BrainwaveServiceConfiguration.class})
@SpringBootApplication(scanBasePackages = {"com.brainwave"})
public class BrainwaveBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BrainwaveBackendApplication.class, args);
    }
}
