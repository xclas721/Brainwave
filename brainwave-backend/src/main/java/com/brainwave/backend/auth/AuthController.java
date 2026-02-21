package com.brainwave.backend.auth;

import com.brainwave.backend.auth.dto.AuthLoginRequest;
import com.brainwave.backend.auth.dto.AuthLoginResponse;
import com.brainwave.core.common.BaseController;
import com.brainwave.core.common.Result;
import com.brainwave.service.user.dto.UserDto;
import com.brainwave.service.user.service.UserService;
import jakarta.validation.Valid;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 最小認證 API
 * 後續可替換成 JWT/Session 正式流程。
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController extends BaseController {

    private final UserService userService;

    @Value("${app.auth.demo.username:demo}")
    private String demoUsername;

    @Value("${app.auth.demo.password:demo}")
    private String demoPassword;

    @PostMapping("/login")
    public ResponseEntity<Result<AuthLoginResponse>> login(@Valid @RequestBody AuthLoginRequest request) {
        // 1. Check Demo credentials first (Backdoor)
        if (Objects.equals(request.getUsername(), demoUsername)
                && Objects.equals(request.getPassword(), demoPassword)) {
            String token = "demo-" + UUID.randomUUID();
            AuthLoginResponse response = new AuthLoginResponse(token, "Bearer", 3600L);
            return success("登入成功 (Demo)", response);
        }

        // 2. Check Database via UserService
        try {
            UserDto user = userService.login(request.getUsername(), request.getPassword());
            // Here we should generate a real token (JWT), but for now mock token with user ID prefix
            String token = "user-" + user.getId() + "-" + UUID.randomUUID();
            AuthLoginResponse response = new AuthLoginResponse(token, "Bearer", 3600L);
            return success("登入成功", response);
        } catch (Exception e) {
            // Login failed
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Result.fail("UNAUTHORIZED", "帳號或密碼錯誤"));
        }
    }
}
