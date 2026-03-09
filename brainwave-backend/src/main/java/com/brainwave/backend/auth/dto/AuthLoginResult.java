package com.brainwave.backend.auth.dto;

/**
 * AuthFacade 登入結果（保留訊息與 token payload）。
 */
public record AuthLoginResult(String message, AuthLoginResponse response) {
}
