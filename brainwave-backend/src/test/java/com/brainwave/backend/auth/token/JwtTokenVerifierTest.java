package com.brainwave.backend.auth.token;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.brainwave.backend.auth.dto.TokenPrincipal;
import com.brainwave.core.config.properties.AuthProperties;
import com.brainwave.core.exception.BusinessException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JwtTokenVerifierTest {

    private static final String SECRET_32 = "0123456789abcdef0123456789abcdef";

    private AuthProperties authProperties;
    private JwtTokenVerifier verifier;

    @BeforeEach
    void setUp() {
        authProperties = new AuthProperties();
        authProperties.getJwt().setSecret(SECRET_32);
        verifier = new JwtTokenVerifier(authProperties);
    }

    @Test
    void verify_validJwtWithSubScopeRole_shouldReturnPrincipal() {
        SecretKey key = Keys.hmacShaKeyFor(SECRET_32.getBytes(StandardCharsets.UTF_8));
        String token = Jwts.builder()
                .subject("7")
                .claim("scope", "ADMIN_USER")
                .claim("role", "ADMIN")
                .signWith(key)
                .compact();

        TokenPrincipal principal = verifier.verify(token);

        assertEquals("ADMIN_USER", principal.scope());
        assertEquals("7", principal.subject());
        assertEquals("ADMIN", principal.role());
    }

    @Test
    void verify_emptyToken_shouldThrow() {
        BusinessException ex = assertThrows(BusinessException.class, () -> verifier.verify(""));
        assertEquals("UNAUTHORIZED", ex.getErrorCode());
    }

    @Test
    void verify_shortSecret_shouldThrowConfigError() {
        authProperties.getJwt().setSecret("short");
        JwtTokenVerifier v = new JwtTokenVerifier(authProperties);

        BusinessException ex = assertThrows(BusinessException.class, () -> v.verify("any"));
        assertEquals("CONFIG_ERROR", ex.getErrorCode());
    }
}
