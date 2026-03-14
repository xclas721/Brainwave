package com.brainwave.backend;

import com.brainwave.core.BrainwaveCoreConfiguration;
import com.brainwave.service.BrainwaveServiceConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

/**
 * Brainwave Backend Application
 * 前端 API 服務，提供 RESTful API 給前端使用
 */
@Import({BrainwaveCoreConfiguration.class, BrainwaveServiceConfiguration.class})
@SpringBootApplication(scanBasePackages = {"com.brainwave"})
public class BrainwaveBackendApplication {

    private static final Logger log = LoggerFactory.getLogger(BrainwaveBackendApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(BrainwaveBackendApplication.class, args);
    }

    @Bean
    ApplicationRunner startupSuccessLog(Environment env) {
        return args -> {
            String baseUrl = env.getProperty("app.startup.api-base-url", "").trim();
            if (!baseUrl.isEmpty()) {
                log.info("後端啟動成功，API 根路徑: {}/api", baseUrl.replaceAll("/+$", ""));
            } else {
                String port = env.getProperty("server.port", "8080");
                log.info("後端啟動成功，port: {}（設 APP_STARTUP_API_BASE_URL 可顯示完整 API 網址）", port);
            }
        };
    }
}
