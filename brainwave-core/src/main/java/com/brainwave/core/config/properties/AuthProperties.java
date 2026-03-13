package com.brainwave.core.config.properties;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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

    public Demo getDemo() {
        return demo;
    }

    public Front getFront() {
        return front;
    }

    public Guard getGuard() {
        return guard;
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

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }
}
