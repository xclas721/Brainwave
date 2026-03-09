package com.brainwave.backend.auth.guard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.brainwave.backend.auth.dto.TokenPrincipal;
import com.brainwave.backend.auth.facade.AuthFacade;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class AuthGuardInterceptorTest {

    @Mock
    private AuthFacade authFacade;

    private AuthGuardInterceptor interceptor;

    @BeforeEach
    void setUp() {
        interceptor = new AuthGuardInterceptor(authFacade);
        ReflectionTestUtils.setField(interceptor, "enabled", true);
    }

    @Test
    void preHandle_publicPath_shouldPassWithoutVerification() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/health");
        boolean allowed = interceptor.preHandle(request, (HttpServletResponse) null, new Object());
        assertTrue(allowed);
        verifyNoInteractions(authFacade);
    }

    @Test
    void preHandle_protectedPathWithoutAuthHeader_shouldReturnUnauthorized() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/users");

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> interceptor.preHandle(request, (HttpServletResponse) null, new Object())
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
                () -> interceptor.preHandle(request, (HttpServletResponse) null, new Object())
        );

        assertEquals(403, ex.getStatusCode().value());
    }

    @Test
    void preHandle_protectedAdminPathWithAdminToken_shouldPassAndStorePrincipal() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/users");
        request.addHeader("Authorization", "Bearer user-7-xyz");
        TokenPrincipal principal = new TokenPrincipal("ADMIN_USER", "7");
        when(authFacade.verifyToken("user-7-xyz")).thenReturn(principal);

        boolean allowed = interceptor.preHandle(request, (HttpServletResponse) null, new Object());

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

        boolean allowed = interceptor.preHandle(request, (HttpServletResponse) null, new Object());

        assertTrue(allowed);
    }
}
