package com.brainwave.core.config;

import java.util.List;
import java.util.stream.Collectors;
import com.brainwave.core.config.properties.CorsProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * CORS 跨域設定
 * 允許前端應用程式存取後端 API
 */
@Configuration
@RequiredArgsConstructor
public class CorsConfig {

    private final CorsProperties corsProperties;

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // 使用白名單來源，避免 wildcard + credentials 造成風險
        List<String> originList = corsProperties.getAllowedOrigins().stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
        config.setAllowedOrigins(originList);

        // 允許的 HTTP 方法
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");
        config.addAllowedMethod("OPTIONS");

        // 允許的請求標頭（含目前前端實際送出的自定義 header）
        config.setAllowedHeaders(List.of(
                "Authorization",
                "Content-Type",
                "Accept",
                "Accept-Language",
                "Replay-key",
                "UserTimeZone",
                "X-XSRF-TOKEN"
        ));
        config.setExposedHeaders(List.of("Authorization"));

        // 是否允許攜帶憑證（Cookie / Authorization）
        config.setAllowCredentials(corsProperties.isAllowCredentials());

        // 預檢請求的緩存時間（秒）
        config.setMaxAge(3600L);

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
