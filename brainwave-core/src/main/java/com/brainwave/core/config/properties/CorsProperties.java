package com.brainwave.core.config.properties;

import jakarta.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * CORS 相關設定（app.cors.*）。
 */
@Component
@Validated
@ConfigurationProperties(prefix = "app.cors")
public class CorsProperties {

    @NotEmpty
    private List<String> allowedOrigins = new ArrayList<>(List.of(
            "http://localhost:3000",
            "http://localhost:5173"
    ));

    private boolean allowCredentials = true;

    public List<String> getAllowedOrigins() {
        return allowedOrigins;
    }

    public void setAllowedOrigins(List<String> allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    public boolean isAllowCredentials() {
        return allowCredentials;
    }

    public void setAllowCredentials(boolean allowCredentials) {
        this.allowCredentials = allowCredentials;
    }
}
