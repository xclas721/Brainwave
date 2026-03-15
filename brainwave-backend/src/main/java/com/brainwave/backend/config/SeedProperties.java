package com.brainwave.backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 啟動時是否自動建立示範帳號（demo 管理員與 demo 會員）的開關。
 * 設為 true 時，若資料庫尚無該帳號則建立；設為 false 則不執行。
 */
@ConfigurationProperties(prefix = "app.seed")
public class SeedProperties {

    /** 是否在啟動時自動建立 demo 管理員與 demo 會員（預設 true）。 */
    private boolean demoAccountsEnabled = true;

    public boolean isDemoAccountsEnabled() {
        return demoAccountsEnabled;
    }

    public void setDemoAccountsEnabled(boolean demoAccountsEnabled) {
        this.demoAccountsEnabled = demoAccountsEnabled;
    }
}
