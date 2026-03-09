package com.brainwave.backend.auth.facade;

import com.brainwave.backend.auth.dto.AuthLoginResult;
import com.brainwave.backend.auth.dto.TokenPrincipal;

/**
 * 認證入口抽象。
 * 將 Controller 與具體認證流程解耦，便於後續替換策略。
 */
public interface AuthFacade {

    AuthLoginResult loginAdmin(String username, String password);

    AuthLoginResult loginFront(String username, String password);

    TokenPrincipal verifyToken(String rawToken);
}
