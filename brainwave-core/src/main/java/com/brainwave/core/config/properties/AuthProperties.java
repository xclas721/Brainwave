package com.brainwave.core.config.properties;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * 認證相關設定（app.auth.*）。
 */
@Component
@Validated
@ConfigurationProperties(prefix = "app.auth")
public class AuthProperties {

    @Valid
    private final Demo demo = new Demo();
    @Valid
    private final Front front = new Front();
    @Valid
    private final Guard guard = new Guard();
    @Valid
    private final Jwt jwt = new Jwt();

    public Demo getDemo() {
        return demo;
    }

    public Front getFront() {
        return front;
    }

    public Guard getGuard() {
        return guard;
    }

    public Jwt getJwt() {
        return jwt;
    }

    public static class Jwt {

        /** HMAC 簽章用 secret，啟用 JWT profile 時必填；至少 256 bits 建議。 */
        private String secret = "";

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret != null ? secret : "";
        }
    }

    public static class Demo {

        @NotBlank
        private String username = "demo";
        @NotBlank
        private String password = "demo";

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class Front {

        @Valid
        private final Demo demo = new Demo();

        public Demo getDemo() {
            return demo;
        }
    }

    public static class Guard {

        private boolean enabled = true;

        /**
         * 需後台 scope（ADMIN_*）的路徑前綴；符合任一即要求後台登入。
         * 可於 application 覆寫，例如 app.auth.guard.admin-path-prefixes[0]=/api/users
         */
        private List<String> adminPathPrefixes = new ArrayList<>(List.of(
                "/api/users",
                "/api/members",
                "/api/system-configs"
        ));

        /**
         * 需前台 scope（FRONT_*）的路徑前綴；符合任一即要求前台登入。
         */
        private List<String> frontPathPrefixes = new ArrayList<>(List.of("/api/front/"));

        /**
         * 自 frontPathPrefixes 排除的前綴（例如登入入口不需驗證）。
         */
        private List<String> frontExcludePrefixes = new ArrayList<>(List.of("/api/front/auth"));

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public List<String> getAdminPathPrefixes() {
            return adminPathPrefixes;
        }

        public void setAdminPathPrefixes(List<String> adminPathPrefixes) {
            this.adminPathPrefixes = adminPathPrefixes != null ? adminPathPrefixes : new ArrayList<>();
        }

        public List<String> getFrontPathPrefixes() {
            return frontPathPrefixes;
        }

        public void setFrontPathPrefixes(List<String> frontPathPrefixes) {
            this.frontPathPrefixes = frontPathPrefixes != null ? frontPathPrefixes : new ArrayList<>();
        }

        public List<String> getFrontExcludePrefixes() {
            return frontExcludePrefixes;
        }

        public void setFrontExcludePrefixes(List<String> frontExcludePrefixes) {
            this.frontExcludePrefixes = frontExcludePrefixes != null ? frontExcludePrefixes : new ArrayList<>();
        }
    }
}
