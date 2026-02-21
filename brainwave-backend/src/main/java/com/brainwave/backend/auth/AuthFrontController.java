package com.brainwave.backend.auth;

import com.brainwave.backend.auth.dto.AuthLoginRequest;
import com.brainwave.backend.auth.dto.AuthLoginResponse;
import com.brainwave.core.common.BaseController;
import com.brainwave.core.common.Result;
import com.brainwave.service.member.dto.MemberDto;
import com.brainwave.service.member.service.MemberService;
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
 * 前台會員登入 API（與後台登入分離，可獨立擴充會員狀態與邏輯）
 */
@RestController
@RequestMapping("/api/front/auth")
@RequiredArgsConstructor
public class AuthFrontController extends BaseController {

    private final MemberService memberService;

    @Value("${app.auth.front.demo.username:demo}")
    private String demoUsername;

    @Value("${app.auth.front.demo.password:demo}")
    private String demoPassword;

    @PostMapping("/login")
    public ResponseEntity<Result<AuthLoginResponse>> login(@Valid @RequestBody AuthLoginRequest request) {
        // 1. Check Demo credentials first
        if (Objects.equals(request.getUsername(), demoUsername)
                && Objects.equals(request.getPassword(), demoPassword)) {
            String token = "front-demo-" + UUID.randomUUID();
            AuthLoginResponse response = new AuthLoginResponse(token, "Bearer", 3600L);
            return success("登入成功 (Demo)", response);
        }

        // 2. Check Database via MemberService
        try {
            MemberDto member = memberService.login(request.getUsername(), request.getPassword());
            String token = "front-user-" + member.getId() + "-" + UUID.randomUUID();
            AuthLoginResponse response = new AuthLoginResponse(token, "Bearer", 3600L);
            return success("登入成功", response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Result.fail("UNAUTHORIZED", "帳號或密碼錯誤"));
        }
    }
}
