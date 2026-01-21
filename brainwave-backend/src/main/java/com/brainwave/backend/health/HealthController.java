package com.brainwave.backend.health;

import com.brainwave.core.common.BaseController;
import com.brainwave.core.common.Result;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 健康檢查 Controller
 */
@Slf4j
@RestController
@RequestMapping("/api/health")
public class HealthController extends BaseController {

    /**
     * 健康檢查端點
     */
    @GetMapping
    public ResponseEntity<Result<HealthResponse>> health() {
        log.info("收到健康檢查請求");
        HealthResponse response = HealthResponse.builder()
                .status("UP")
                .timestamp(Instant.now())
                .version("1.0.0")
                .build();
        log.info("健康檢查回應：status={}, version={}", response.getStatus(), response.getVersion());
        return success("系統運行正常", response);
    }

    /**
     * 健康檢查回應物件
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @lombok.Builder
    public static class HealthResponse {
        private String status;
        private Instant timestamp;
        private String version;
    }
}
