package com.brainwave.backend.auth.facade;

import com.brainwave.backend.auth.dto.AuthLoginResponse;
import com.brainwave.backend.auth.dto.AuthLoginResult;
import com.brainwave.backend.auth.dto.TokenPrincipal;
import com.brainwave.backend.auth.token.TokenVerifier;
import com.brainwave.service.member.dto.MemberDto;
import com.brainwave.service.member.service.MemberService;
import com.brainwave.service.user.dto.UserDto;
import com.brainwave.service.user.service.UserService;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultAuthFacade implements AuthFacade {

    private static final String TOKEN_TYPE = "Bearer";
    private static final long EXPIRES_IN_SECONDS = 3600L;

    private final UserService userService;
    private final MemberService memberService;
    private final TokenVerifier tokenVerifier;

    @Value("${app.auth.demo.username:demo}")
    private String adminDemoUsername;

    @Value("${app.auth.demo.password:demo}")
    private String adminDemoPassword;

    @Value("${app.auth.front.demo.username:demo}")
    private String frontDemoUsername;

    @Value("${app.auth.front.demo.password:demo}")
    private String frontDemoPassword;

    @Override
    public AuthLoginResult loginAdmin(String username, String password) {
        if (Objects.equals(username, adminDemoUsername) && Objects.equals(password, adminDemoPassword)) {
            return new AuthLoginResult("登入成功 (Demo)",
                    new AuthLoginResponse("demo-" + UUID.randomUUID(), TOKEN_TYPE, EXPIRES_IN_SECONDS));
        }

        UserDto user = userService.login(username, password);
        String token = "user-" + user.getId() + "-" + UUID.randomUUID();
        return new AuthLoginResult("登入成功", new AuthLoginResponse(token, TOKEN_TYPE, EXPIRES_IN_SECONDS));
    }

    @Override
    public AuthLoginResult loginFront(String username, String password) {
        if (Objects.equals(username, frontDemoUsername) && Objects.equals(password, frontDemoPassword)) {
            return new AuthLoginResult("登入成功 (Demo)",
                    new AuthLoginResponse("front-demo-" + UUID.randomUUID(), TOKEN_TYPE, EXPIRES_IN_SECONDS));
        }

        MemberDto member = memberService.login(username, password);
        String token = "front-user-" + member.getId() + "-" + UUID.randomUUID();
        return new AuthLoginResult("登入成功", new AuthLoginResponse(token, TOKEN_TYPE, EXPIRES_IN_SECONDS));
    }

    @Override
    public TokenPrincipal verifyToken(String rawToken) {
        return tokenVerifier.verify(rawToken);
    }
}
