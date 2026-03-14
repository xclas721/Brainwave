package com.brainwave.backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 啟動 log 用設定（例如 Zeabur 部署時可設 APP_STARTUP_API_BASE_URL 顯示實際 API 網址）。
 */
@ConfigurationProperties(prefix = "app.startup")
public class AppStartupProperties {

    /** 選填；設定的話啟動 log 會印出「API 根路徑: {api-base-url}/api」。 */
    private String apiBaseUrl = "";

    public String getApiBaseUrl() {
        return apiBaseUrl;
    }

    public void setApiBaseUrl(String apiBaseUrl) {
        this.apiBaseUrl = apiBaseUrl != null ? apiBaseUrl.trim() : "";
    }
}
