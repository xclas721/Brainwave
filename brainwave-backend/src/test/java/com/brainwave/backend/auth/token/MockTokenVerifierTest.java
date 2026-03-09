package com.brainwave.backend.auth.token;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.brainwave.backend.auth.dto.TokenPrincipal;
import com.brainwave.core.exception.BusinessException;
import org.junit.jupiter.api.Test;

class MockTokenVerifierTest {

    private final MockTokenVerifier verifier = new MockTokenVerifier();

    @Test
    void verify_demoToken_shouldReturnAdminDemoPrincipal() {
        TokenPrincipal principal = verifier.verify("demo-123");
        assertEquals("ADMIN_DEMO", principal.scope());
        assertEquals("demo", principal.subject());
    }

    @Test
    void verify_frontUserToken_shouldReturnFrontUserPrincipal() {
        TokenPrincipal principal = verifier.verify("front-user-99-uuid");
        assertEquals("FRONT_USER", principal.scope());
        assertEquals("99", principal.subject());
    }

    @Test
    void verify_invalidToken_shouldThrowBusinessException() {
        assertThrows(BusinessException.class, () -> verifier.verify("bad-token"));
    }
}
