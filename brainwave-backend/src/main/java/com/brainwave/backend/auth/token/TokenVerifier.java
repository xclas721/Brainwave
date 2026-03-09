package com.brainwave.backend.auth.token;

import com.brainwave.backend.auth.dto.TokenPrincipal;

/**
 * Token 驗證抽象邊界。
 * 後續可替換成 JWT / Session / OAuth2 驗證實作。
 */
public interface TokenVerifier {

    TokenPrincipal verify(String rawToken);
}
