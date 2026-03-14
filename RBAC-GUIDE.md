# RBAC 基礎版說明（P2-1）

## 角色定義

- **後台**：`ADMIN`、`EDITOR`、`VIEWER`（見 `auth/dto/Role.java`）
- **前台**：`MEMBER`

## Token 與權限

- `TokenPrincipal(scope, subject, role)`：登入後由 `TokenVerifier` 帶入 `role`。
- Mock 規則：`ADMIN_*` → role=ADMIN，`FRONT_*` → role=MEMBER。
- `AuthGuardInterceptor`：先驗 scope，再驗 role；`/api/users`、`/api/members`、`/api/system-configs` 需後台角色（ADMIN/EDITOR/VIEWER）。

## 前端

- 可依 `principal.role` 控制選單顯示（例如僅 ADMIN 顯示「使用者管理」）。

## 後續

- 在 `users` 表加 `role` 欄位，登入時從 DB 帶入；JWT 時將 role 寫入 claim。
