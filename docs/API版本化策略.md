# API 版本化策略

> 目的：預先訂定版本策略，未來若要支援 v1 / v2 並行時不破壞既有客戶，實作可依需求再落地。

## 1. 何時需要版本化

- 對外開放 API、有多方客戶或前端版本不一，且**無法同步升級**時。
- 需**破壞性變更**（欄位更名、移除、行為變更）又必須保留舊行為時。
- 若僅內部前後端、可一併部署，可先不引入路徑/header 版本，以**相容式變更**為主。

## 2. 常見策略

| 策略 | 作法 | 優點 | 缺點 |
|------|------|------|------|
| **路徑前綴** | `/api/v1/users`、`/api/v2/users` | 直觀、易快取、易在 Gateway 分流 | URL 變長；需在 Controller 或前綴上掛版本 |
| **Header** | `Accept: application/vnd.api+v1` 或 `X-Api-Version: 1` | URL 不變 | 客戶端須記得帶；快取與分流需依 header 處理 |
| **Query** | `?version=1` | 實作簡單 | 易被忽略、不利快取與 REST 語意 |

**建議**：以**路徑前綴**為預設（`/api/v1/...`），若 Gateway 或客戶端有強需求再考慮 Header。

## 3. 與本專案現況的對應

- 目前路由為 **無版本**：`/api/users`、`/api/health` 等。
- 若要引入 v1：
  1. 可新增前綴：例如 `@RequestMapping("/api/v1/users")`，或使用 `WebMvcConfigurer` / 前綴設定統一加 `/api/v1`。
  2. **Guard 路徑**：`app.auth.guard.admin-path-prefixes` 需包含新路徑（如 `/api/v1/users`），或維持 `/api` 前綴由 Guard 一併涵蓋。
  3. **OpenAPI**：若使用 springdoc，可依 path 或 tag 區分 v1 / v2，方便文件與前端產生器對應。

## 4. 實作時機

- **僅文件**：本檔即為策略約定，新模組可依此預留 v1 路徑或註解。
- **實際改路徑**：當第一個破壞性變更或對外 API 契約確定時，再統一改為 `/api/v1/...` 並更新 Guard / 前端 base URL。
