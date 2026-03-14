package com.brainwave.backend.auth.token;

import com.brainwave.backend.auth.dto.TokenPrincipal;
import com.brainwave.core.exception.BusinessException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 現階段最小可用 mock token 驗證器。
 * 規則僅用於開發階段；啟用 profile=jwt 時改由 JwtTokenVerifier 接手。
 */
@Component
@org.springframework.context.annotation.Profile("!jwt")
public class MockTokenVerifier implements TokenVerifier {

    @Override
    public TokenPrincipal verify(String rawToken) {
        if (!StringUtils.hasText(rawToken)) {
            throw new BusinessException("UNAUTHORIZED", "Token 不可為空");
        }

        if (rawToken.startsWith("demo-")) {
            return new TokenPrincipal("ADMIN_DEMO", "demo", "ADMIN");
        }
        if (rawToken.startsWith("front-demo-")) {
            return new TokenPrincipal("FRONT_DEMO", "demo", "MEMBER");
        }

        if (rawToken.startsWith("user-")) {
            String[] parts = rawToken.split("-", 3);
            if (parts.length >= 2 && StringUtils.hasText(parts[1])) {
                return new TokenPrincipal("ADMIN_USER", parts[1], "ADMIN");
            }
        }
        if (rawToken.startsWith("front-user-")) {
            String[] parts = rawToken.split("-", 4);
            if (parts.length >= 3 && StringUtils.hasText(parts[2])) {
                return new TokenPrincipal("FRONT_USER", parts[2], "MEMBER");
            }
        }

        throw new BusinessException("UNAUTHORIZED", "Token 格式無效");
    }
}
