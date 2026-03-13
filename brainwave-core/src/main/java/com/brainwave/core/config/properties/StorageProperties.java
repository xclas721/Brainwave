package com.brainwave.core.config.properties;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * 檔案儲存相關設定（app.storage.*）。
 * 目前先提供型別化骨架，後續由 StorageService 實作接入。
 */
@Component
@Validated
@ConfigurationProperties(prefix = "app.storage")
public class StorageProperties {

    @NotBlank
    private String provider = "local";
    @Valid
    private final Local local = new Local();
    @Valid
    private final S3 s3 = new S3();

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public Local getLocal() {
        return local;
    }

    public S3 getS3() {
        return s3;
    }

    public static class Local {

        @NotBlank
        private String basePath = "./uploads";
        private String publicBaseUrl = "";

        public String getBasePath() {
            return basePath;
        }

        public void setBasePath(String basePath) {
            this.basePath = basePath;
        }

        public String getPublicBaseUrl() {
            return publicBaseUrl;
        }

        public void setPublicBaseUrl(String publicBaseUrl) {
            this.publicBaseUrl = publicBaseUrl;
        }
    }

    public static class S3 {

        private String endpoint = "";
        private String region = "";
        private String bucket = "";

        public String getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getBucket() {
            return bucket;
        }

        public void setBucket(String bucket) {
            this.bucket = bucket;
        }
    }
}
