# OpenAPI 與 API 契約（後端）

> 目標：後端提供 OpenAPI 3 規格，供 Swagger UI 檢視與前端型別產生（如 openapi-typescript）使用。

## 1. 用途

- **文件**：依程式註解與端點自動產生 API 規格，減少手寫文件不同步。
- **前端**：可從 `/v3/api-docs` 取得 JSON，產生 TypeScript 型別或 API client（例如 Brainwave-frontend 的 `schema.d.ts`）。

## 2. 實作方式

- **依賴**：springdoc-openapi（見根目錄 `pom.xml` 的 `springdoc.version`，backend 模組引入）。
- **端點**：
  - **JSON 規格**：`GET /v3/api-docs`（依應用 context path 可能為 `http://localhost:8080/v3/api-docs`）。
  - **Swagger UI**：`GET /swagger-ui.html` 或 `/swagger-ui/index.html`（依 springdoc 版本）。
- **設定**：可於 `application.properties` 調整 `springdoc.api-docs.path`、`springdoc.swagger-ui.path`、是否啟用等。

## 3. 與 Guard、CORS 的關係

- **Guard**：若 `/v3/api-docs`、`/swagger-ui/**` 不需登入，請將這些路徑放在 **不需驗證** 的範圍（例如不列入 `admin-path-prefixes` / `front-path-prefixes`），或於 Guard 中排除。
- **CORS**：前端若從不同網域請求 `/v3/api-docs`，需在 `app.cors.allowed-origins` 放行。

## 4. 前端型別產生

- 前端專案可於建置或開發時請求後端 `http://localhost:8080/v3/api-docs`，產出 `schema.d.ts` 或 API client。
- 正式環境可改為指向正式後端 URL；產生腳本通常放在前端 repo（如 `scripts/generate-api.mjs`）。

## 5. 延伸閱讀

- 專案結構與 API 列表：根目錄 **專案目錄結構.md**
- API 版本策略：**API版本化策略.md**
- 認證與 Guard：**認證與Token說明.md**、**權限守衛與RBAC說明.md**
