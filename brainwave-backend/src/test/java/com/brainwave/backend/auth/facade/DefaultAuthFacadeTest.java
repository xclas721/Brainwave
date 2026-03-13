package com.brainwave.backend.auth.facade;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.brainwave.backend.auth.dto.AuthLoginResult;
import com.brainwave.backend.auth.dto.TokenPrincipal;
import com.brainwave.backend.auth.token.TokenVerifier;
import com.brainwave.core.config.properties.AuthProperties;
import com.brainwave.service.member.dto.MemberDto;
import com.brainwave.service.member.service.MemberService;
import com.brainwave.service.user.dto.UserDto;
import com.brainwave.service.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DefaultAuthFacadeTest {

    @Mock
    private UserService userService;

    @Mock
    private MemberService memberService;

    @Mock
    private TokenVerifier tokenVerifier;

    private DefaultAuthFacade authFacade;

    @BeforeEach
    void setUp() {
        AuthProperties authProperties = new AuthProperties();
        authProperties.getDemo().setUsername("demo");
        authProperties.getDemo().setPassword("demo");
        authProperties.getFront().getDemo().setUsername("demo");
        authProperties.getFront().getDemo().setPassword("demo");
        authFacade = new DefaultAuthFacade(userService, memberService, tokenVerifier, authProperties);
    }

    @Test
    void loginAdmin_withDemoCredential_shouldReturnDemoToken() {
        AuthLoginResult result = authFacade.loginAdmin("demo", "demo");
        assertTrue(result.message().contains("Demo"));
        assertTrue(result.response().getToken().startsWith("demo-"));
    }

    @Test
    void loginAdmin_withUserCredential_shouldDelegateUserService() {
        when(userService.login("amy", "123456"))
                .thenReturn(UserDto.builder().id(7L).username("amy").build());

        AuthLoginResult result = authFacade.loginAdmin("amy", "123456");

        verify(userService).login("amy", "123456");
        assertTrue(result.response().getToken().startsWith("user-7-"));
    }

    @Test
    void loginFront_withMemberCredential_shouldDelegateMemberService() {
        when(memberService.login("bob", "123456"))
                .thenReturn(MemberDto.builder().id(9L).username("bob").build());

        AuthLoginResult result = authFacade.loginFront("bob", "123456");

        verify(memberService).login("bob", "123456");
        assertTrue(result.response().getToken().startsWith("front-user-9-"));
    }

    @Test
    void verifyToken_shouldDelegateToTokenVerifier() {
        when(tokenVerifier.verify("demo-123")).thenReturn(new TokenPrincipal("ADMIN_DEMO", "demo"));
        TokenPrincipal principal = authFacade.verifyToken("demo-123");
        verify(tokenVerifier).verify("demo-123");
        assertTrue("ADMIN_DEMO".equals(principal.scope()));
    }
}
