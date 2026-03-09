package com.brainwave.backend.auth;

import com.brainwave.backend.auth.dto.AuthLoginRequest;
import com.brainwave.backend.auth.dto.AuthLoginResponse;
import com.brainwave.backend.auth.dto.AuthLoginResult;
import com.brainwave.backend.auth.facade.AuthFacade;
import com.brainwave.core.common.BaseController;
import com.brainwave.core.common.Result;
import com.brainwave.core.exception.BusinessException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    private final AuthFacade authFacade;

    @PostMapping("/login")
    public ResponseEntity<Result<AuthLoginResponse>> login(@Valid @RequestBody AuthLoginRequest request) {
        try {
            AuthLoginResult result = authFacade.loginFront(request.getUsername(), request.getPassword());
            return success(result.message(), result.response());
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Result.fail("UNAUTHORIZED", "帳號或密碼錯誤"));
        }
    }
}
