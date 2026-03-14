# 認證與 Token 驗證

> 目標：登入與 Token 驗證可替換，預設為 Mock，正式環境可接 JWT 或 OAuth2。

## 1. 邊界與契約

- **AuthFacade**：登入（admin / front）與 `verifyToken(rawToken)`，回傳 `TokenPrincipal`。
- **TokenVerifier**：單一方法 `TokenPrincipal verify(String rawToken)`，負責解析與驗證 token，並產出 scope、subject、role。
- **TokenPrincipal**：`(scope, subject, role)`，供 AuthGuardInterceptor 做 scope/role 檢查。

登入流程由 AuthFacade 委派給 UserService/MemberService 與 TokenVerifier（或登入時發 token）；後續 API 的 Bearer token 由 AuthGuardInterceptor 呼叫 AuthFacade.verifyToken，再依 TokenPrincipal 決定是否放行。

## 2. 目前預設：MockTokenVerifier

- **用途**：開發與展示用，無簽章驗證。
- **規則**：`demo-*` → ADMIN_DEMO；`front-demo-*` → FRONT_DEMO；`user-{id}-*` → ADMIN_USER；`front-user-{id}-*` → FRONT_USER。
- **註冊**：`@Component`，預設啟用；啟用 JWT 時改為 `@Profile("!jwt")`，僅在非 JWT 情境使用。

## 3. 替換為 JWT 驗證

### 3.1 實作要點

1. **實作 TokenVerifier**：解析 JWT、驗證簽名（與過期等）、自 claim 組出 `TokenPrincipal`。
2. **Claim 對應建議**：
   - `sub` → subject（使用者/會員 ID）
   - scope：自訂 claim（如 `scope` 或 `aud`），或由 subject 型別推斷（後台/前台）。
   - role：自訂 claim（如 `role`），須為 AuthGuardInterceptor 認識的值（ADMIN/EDITOR/VIEWER/MEMBER）。
3. **驗證項目**：簽名、`exp`（過期）、可選 `iss`/`aud`；發 token 端與驗證端需共用 secret 或 public key。

### 3.2 本專案提供的 JWT 範例

- **類別**：`JwtTokenVerifier`（backend），依賴 jjwt。
- **啟用方式**：`spring.profiles.active=jwt` 時註冊為 `@Primary`，Mock 改為 `@Profile("!jwt")`，即改為使用 JWT。
- **設定**：`app.auth.jwt.secret`（或改為 public key 路徑），見 `application.properties` 註解。
- **Claim 約定**：`sub`、`scope`、`role`；若 JWT 未帶 role，可依 scope 預設（ADMIN_*→ADMIN，FRONT_*→MEMBER）。

### 3.3 替換步驟摘要

1. 在 backend 加上 jjwt 依賴（若使用本專案範例）。
2. 設定 `app.auth.jwt.secret`（或 RS256 公鑰）。
3. 啟動時加上 profile：`-Dspring.profiles.active=jwt`（或於 deployment 設定）。
4. 登入端改為發放真實 JWT（含 sub、scope、role），與 JwtTokenVerifier 的 claim 約定一致。

## 4. 與 Guard、RBAC 的關係

- AuthGuardInterceptor 依 **scope** 判斷是否為後台/前台，依 **role** 判斷權限（見 權限守衛與RBAC說明.md）。
- TokenVerifier 只需產出正確的 `TokenPrincipal(scope, subject, role)`，不需知道路徑或權限規則。
