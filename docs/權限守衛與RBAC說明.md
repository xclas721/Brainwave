# RBAC 基礎版說明

## 角色定義

- **後台**：`ADMIN`、`EDITOR`、`VIEWER`（見 `auth/dto/Role.java`）
- **前台**：`MEMBER`

## Token 與權限

- `TokenPrincipal(scope, subject, role)`：登入後由 `TokenVerifier` 帶入 `role`。
- Mock 規則：`ADMIN_*` → role=ADMIN，`FRONT_*` → role=MEMBER。
- `AuthGuardInterceptor`：先驗 scope，再驗 role；受保護路徑由 **設定** 決定（見下方「路徑設定」）。

## 路徑設定（app.auth.guard）

受保護路徑改為**可設定**，新增 API 時不需改 Java，改設定即可。

| 設定鍵 | 說明 | 預設 |
|--------|------|------|
| `app.auth.guard.enabled` | 是否啟用 Guard | true |
| `app.auth.guard.admin-path-prefixes` | 需後台 scope（ADMIN_*）的路徑前綴，符合任一即要求後台登入 | `/api/users`, `/api/members`, `/api/system-configs` |
| `app.auth.guard.front-path-prefixes` | 需前台 scope（FRONT_*）的路徑前綴 | `/api/front/` |
| `app.auth.guard.front-exclude-prefixes` | 自前台路徑排除的前綴（例如登入入口不需驗證） | `/api/front/auth` |

範例：新增 `/api/orders` 需後台登入，在 `application.properties` 或 profile 中設定：

```properties
app.auth.guard.admin-path-prefixes[0]=/api/users
app.auth.guard.admin-path-prefixes[1]=/api/members
app.auth.guard.admin-path-prefixes[2]=/api/system-configs
app.auth.guard.admin-path-prefixes[3]=/api/orders
```

或 YAML：

```yaml
app:
  auth:
    guard:
      admin-path-prefixes:
        - /api/users
        - /api/members
        - /api/system-configs
        - /api/orders
```

## 前端

- 可依 `principal.role` 控制選單顯示（例如僅 ADMIN 顯示「使用者管理」）。

## 替換為 JWT 驗證

預設為 MockTokenVerifier；正式環境可改為 JWT。啟用方式與 claim 約定見 **認證與Token說明.md**（profile=jwt、app.auth.jwt.secret、JwtTokenVerifier）。

## 後續

- 在 `users` 表加 `role` 欄位，登入時從 DB 帶入；JWT 時將 role 寫入 claim。
