package com.brainwave.core.config.properties;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * 稽核日誌相關設定（app.audit.*）。
 * 目前先提供型別化骨架，後續由 AOP/Log pipeline 接入。
 */
@Component
@Validated
@ConfigurationProperties(prefix = "app.audit")
public class AuditProperties {

    private boolean enabled = false;
    /** 輸出目標：console（僅 log）或 db（寫入 audit_log 表） */
    private String sink = "console";
    private boolean includeRequestBody = false;
    private boolean includeResponseBody = false;
    @Min(1)
    private int maxBodyLength = 2048;
    @NotEmpty
    private List<String> maskFields = new ArrayList<>(List.of("password", "token", "authorization"));

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getSink() {
        return sink;
    }

    public void setSink(String sink) {
        this.sink = sink != null ? sink : "console";
    }

    public boolean isIncludeRequestBody() {
        return includeRequestBody;
    }

    public void setIncludeRequestBody(boolean includeRequestBody) {
        this.includeRequestBody = includeRequestBody;
    }

    public boolean isIncludeResponseBody() {
        return includeResponseBody;
    }

    public void setIncludeResponseBody(boolean includeResponseBody) {
        this.includeResponseBody = includeResponseBody;
    }

    public int getMaxBodyLength() {
        return maxBodyLength;
    }

    public void setMaxBodyLength(int maxBodyLength) {
        this.maxBodyLength = maxBodyLength;
    }

    public List<String> getMaskFields() {
        return maskFields;
    }

    public void setMaskFields(List<String> maskFields) {
        this.maskFields = maskFields;
    }
}
