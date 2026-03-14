package com.brainwave.backend.auth.guard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.brainwave.backend.auth.dto.TokenPrincipal;
import com.brainwave.backend.auth.facade.AuthFacade;
import com.brainwave.core.config.properties.AuthProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class AuthGuardInterceptorTest {

    @Mock
    private AuthFacade authFacade;

    private AuthProperties authProperties;
    private AuthGuardInterceptor interceptor;

    @BeforeEach
    void setUp() {
        authProperties = new AuthProperties();
        authProperties.getGuard().setEnabled(true);
        interceptor = new AuthGuardInterceptor(authFacade, authProperties);
    }

    @Test
    void preHandle_publicPath_shouldPassWithoutVerification() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/health");
        boolean allowed = interceptor.preHandle(request, new MockHttpServletResponse(), new Object());
        assertTrue(allowed);
        verifyNoInteractions(authFacade);
    }

    @Test
    void preHandle_protectedPathWithoutAuthHeader_shouldReturnUnauthorized() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/users");

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> interceptor.preHandle(request, new MockHttpServletResponse(), new Object())
        );

        assertEquals(401, ex.getStatusCode().value());
    }

    @Test
    void preHandle_protectedAdminPathWithFrontToken_shouldReturnForbidden() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/users");
        request.addHeader("Authorization", "Bearer front-user-9-xyz");
        when(authFacade.verifyToken("front-user-9-xyz")).thenReturn(new TokenPrincipal("FRONT_USER", "9"));

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> interceptor.preHandle(request, new MockHttpServletResponse(), new Object())
        );

        assertEquals(403, ex.getStatusCode().value());
    }

    @Test
    void preHandle_protectedAdminPathWithAdminToken_shouldPassAndStorePrincipal() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/users");
        request.addHeader("Authorization", "Bearer user-7-xyz");
        TokenPrincipal principal = new TokenPrincipal("ADMIN_USER", "7");
        when(authFacade.verifyToken("user-7-xyz")).thenReturn(principal);

        boolean allowed = interceptor.preHandle(request, new MockHttpServletResponse(), new Object());

        assertTrue(allowed);
        verify(authFacade).verifyToken("user-7-xyz");
        assertEquals(principal, request.getAttribute(AuthGuardInterceptor.ATTR_AUTH_PRINCIPAL));
    }

    @Test
    void preHandle_frontPathWithFrontToken_shouldPass() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/front/profile");
        request.addHeader("Authorization", "Bearer front-user-5-xyz");
        TokenPrincipal principal = new TokenPrincipal("FRONT_USER", "5");
        when(authFacade.verifyToken("front-user-5-xyz")).thenReturn(principal);

        boolean allowed = interceptor.preHandle(request, new MockHttpServletResponse(), new Object());

        assertTrue(allowed);
    }

    @Test
    void preHandle_customAdminPathFromConfig_shouldRequireAdmin() {
        authProperties.getGuard().setAdminPathPrefixes(java.util.List.of("/api/orders", "/api/users"));
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/orders");
        request.addHeader("Authorization", "Bearer user-1-xyz");
        TokenPrincipal principal = new TokenPrincipal("ADMIN_USER", "1");
        when(authFacade.verifyToken("user-1-xyz")).thenReturn(principal);

        boolean allowed = interceptor.preHandle(request, new MockHttpServletResponse(), new Object());

        assertTrue(allowed);
        assertEquals(principal, request.getAttribute(AuthGuardInterceptor.ATTR_AUTH_PRINCIPAL));
    }

    @Test
    void preHandle_frontExcludePath_shouldPassWithoutVerification() {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/front/auth/login");
        boolean allowed = interceptor.preHandle(request, new MockHttpServletResponse(), new Object());
        assertTrue(allowed);
        verifyNoInteractions(authFacade);
    }

    @Test
    void preHandle_guardDisabled_shouldPassWithoutVerification() {
        authProperties.getGuard().setEnabled(false);
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/users");
        boolean allowed = interceptor.preHandle(request, new MockHttpServletResponse(), new Object());
        assertTrue(allowed);
        verifyNoInteractions(authFacade);
    }

    @Test
    void preHandle_adminPathWithViewerRole_shouldPass() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/system-configs");
        request.addHeader("Authorization", "Bearer viewer-1-xyz");
        TokenPrincipal principal = new TokenPrincipal("ADMIN_VIEWER", "1", "VIEWER");
        when(authFacade.verifyToken("viewer-1-xyz")).thenReturn(principal);

        boolean allowed = interceptor.preHandle(request, new MockHttpServletResponse(), new Object());

        assertTrue(allowed);
        assertEquals(principal, request.getAttribute(AuthGuardInterceptor.ATTR_AUTH_PRINCIPAL));
    }

    @Test
    void preHandle_bearerWithEmptyToken_shouldReturnUnauthorized() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/users");
        request.addHeader("Authorization", "Bearer ");

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> interceptor.preHandle(request, new MockHttpServletResponse(), new Object())
        );
        assertEquals(401, ex.getStatusCode().value());
        verifyNoInteractions(authFacade);
    }
}
