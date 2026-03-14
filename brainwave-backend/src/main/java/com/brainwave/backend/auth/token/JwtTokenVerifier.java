package com.brainwave.backend.auth.token;

import com.brainwave.backend.auth.dto.TokenPrincipal;
import com.brainwave.core.config.properties.AuthProperties;
import com.brainwave.core.exception.BusinessException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * JWT 驗證實作，依 profile=jwt 啟用。
 * Claim 約定：sub → subject；scope、role 為自訂 claim，若無則依預設推斷。
 */
@Component
@Primary
@org.springframework.context.annotation.Profile("jwt")
public class JwtTokenVerifier implements TokenVerifier {

    private static final int MIN_SECRET_LENGTH = 32;

    private final AuthProperties authProperties;

    public JwtTokenVerifier(AuthProperties authProperties) {
        this.authProperties = authProperties;
    }

    @Override
    public TokenPrincipal verify(String rawToken) {
        if (!StringUtils.hasText(rawToken)) {
            throw new BusinessException("UNAUTHORIZED", "Token 不可為空");
        }

        String secret = authProperties.getJwt().getSecret();
        if (!StringUtils.hasText(secret) || secret.getBytes(StandardCharsets.UTF_8).length < MIN_SECRET_LENGTH) {
            throw new BusinessException("CONFIG_ERROR", "app.auth.jwt.secret 未設定或長度不足（HS256 至少 32 字元）");
        }

        try {
            SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
            Claims payload = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(rawToken.trim())
                    .getPayload();

            String subject = payload.getSubject();
            if (!StringUtils.hasText(subject)) {
                throw new BusinessException("UNAUTHORIZED", "JWT 缺少 sub");
            }

            String scope = payload.get("scope", String.class);
            String role = payload.get("role", String.class);
            if (!StringUtils.hasText(scope)) {
                scope = StringUtils.hasText(role) && "MEMBER".equals(role)
                        ? "FRONT_USER"
                        : "ADMIN_USER";
            }
            if (!StringUtils.hasText(role)) {
                role = scope.startsWith("ADMIN_") ? "ADMIN" : (scope.startsWith("FRONT_") ? "MEMBER" : null);
            }

            return new TokenPrincipal(scope, subject, role);
        } catch (JwtException e) {
            throw new BusinessException("UNAUTHORIZED", "Token 無效或已過期: " + e.getMessage());
        }
    }
}
