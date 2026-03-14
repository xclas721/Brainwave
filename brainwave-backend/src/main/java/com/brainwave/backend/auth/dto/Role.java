package com.brainwave.backend.auth.dto;

/**
 * 後台角色（用於 API 權限與前端選單可見性）。
 */
public enum Role {
    ADMIN,
    EDITOR,
    VIEWER,
    MEMBER
}
